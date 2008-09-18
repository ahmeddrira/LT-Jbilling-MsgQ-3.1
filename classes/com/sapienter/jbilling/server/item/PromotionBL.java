/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sapienter.jbilling.server.item;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.PromotionEntityLocal;
import com.sapienter.jbilling.interfaces.PromotionEntityLocalHome;
import com.sapienter.jbilling.server.item.db.PromotionDAS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.audit.EventLogger;
import com.sapienter.jbilling.server.util.db.generated.Promotion;

public class PromotionBL {
    private JNDILookup EJBFactory = null;
    private PromotionEntityLocalHome promotionHome = null;
    private PromotionEntityLocal promotion = null;
    private Logger log = null;
    private EventLogger eLogger = null;
    
    public PromotionBL(Integer promotionId) 
            throws NamingException, FinderException {
        init();
        set(promotionId);
    }
    
    public PromotionBL() throws NamingException {
        init();
    }
    
    private void init() throws NamingException {
        log = Logger.getLogger(PromotionBL.class);     
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        promotionHome = (PromotionEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                PromotionEntityLocalHome.class,
                PromotionEntityLocalHome.JNDI_NAME);
    }

    public PromotionEntityLocal getEntity() {
        return promotion;
    }
    
    public void set(Integer id) throws FinderException {
        promotion = promotionHome.findByPrimaryKey(id);
    }
    
    public Integer create(Integer entityId, PromotionDTOEx dto) 
            throws CreateException {
        // can't allow duplicates of entity/code
        if (isPresent(entityId, dto.getCode())) {
            return null;
        }
        promotion = promotionHome.create(dto.getItemId(), dto.getCode(), 
                dto.getOnce()); 
        promotion.setNotes(dto.getNotes());
        promotion.setSince(dto.getSince());
        promotion.setUntil(dto.getUntil());
                
        return promotion.getId();       
    }
    
    public void update(Integer executorId, PromotionDTOEx dto) 
            throws SessionInternalError {
        eLogger.audit(executorId, Constants.TABLE_PROMOTION, 
                promotion.getId(),
                EventLogger.MODULE_PROMOTION_MAINTENANCE, 
                EventLogger.ROW_UPDATED, null,  
                promotion.getCode(), null);

        promotion.setCode(dto.getCode());
        promotion.setNotes(dto.getNotes());
        promotion.setOnce(dto.getOnce());
        promotion.setSince(dto.getSince());
        promotion.setUntil(dto.getUntil());
    }
    
    public void delete(Integer executorId) 
            throws RemoveException, NamingException, FinderException {
        // first, mark the assosiated item as deleted
        ItemBL item = new ItemBL(promotion.getItem().getId());
        item.delete(executorId);
        // now delete this promotion record
        eLogger.audit(executorId, Constants.TABLE_PROMOTION, 
                promotion.getId(),
                EventLogger.MODULE_PROMOTION_MAINTENANCE, 
                EventLogger.ROW_DELETED, null, null,null);
        
        promotion.remove();
    }
    
    public PromotionDTOEx getDTO() {
        PromotionDTOEx dto = new PromotionDTOEx();
        
        dto.setId(promotion.getId());
        dto.setCode(promotion.getCode());
        dto.setItemId(promotion.getItem().getId());
        dto.setNotes(promotion.getNotes());
        dto.setOnce(promotion.getOnce());
        dto.setSince(promotion.getSince());
        dto.setUntil(promotion.getUntil());
        
        return dto;
    }
    
    /**
     * Verifies if there's a promotion for the entity/code. The entity
     * comes from the related item.
     */
    public boolean isPresent(Integer entityId, String code) {
        Promotion pro = new PromotionDAS().findByEntityCode(entityId, code);
        if (pro == null) {
            return false;
        } else {
            try {
                set(pro.getId());
            } catch (FinderException e) {
            }
            return true;
        }
    }
    
    /**
     * Verifies if the promotion is a one time type, and if it is, if it has
     * been used for the specified user.
     * @param userId
     * @param code
     * @return
     */
    public boolean isAvailable(Integer userId, String code) {
        boolean retValue = true;
        if (promotion.getOnce().intValue() == 1) {
            retValue = !promotion.userUsedPromotion(userId, code);
        } 
        
        log.debug("returning " + retValue);
        return retValue;
    }
}
