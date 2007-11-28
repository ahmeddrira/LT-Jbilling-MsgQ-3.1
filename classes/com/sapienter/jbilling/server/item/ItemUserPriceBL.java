/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
import com.sapienter.jbilling.interfaces.ItemUserPriceEntityLocal;
import com.sapienter.jbilling.interfaces.ItemUserPriceEntityLocalHome;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.EventLogger;

public class ItemUserPriceBL {
    private JNDILookup EJBFactory = null;
    private ItemUserPriceEntityLocalHome itemUserPriceHome = null;
    private ItemUserPriceEntityLocal itemUserPrice = null;
    private Logger log = null;
    private EventLogger eLogger = null;
    
    public ItemUserPriceBL(Integer itemUserPriceId) 
            throws NamingException, FinderException {
        init();
        set(itemUserPriceId);
    }
    
    public ItemUserPriceBL(Integer userId, Integer itemId, Integer currencyId)
            throws NamingException, FinderException {
        init();
        itemUserPrice = itemUserPriceHome.find(userId, itemId, currencyId);
    }
    
    public ItemUserPriceBL() throws NamingException {
        init();
    }
    
    private void init() throws NamingException {
        log = Logger.getLogger(ItemUserPriceBL.class);     
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        itemUserPriceHome = (ItemUserPriceEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                ItemUserPriceEntityLocalHome.class,
                ItemUserPriceEntityLocalHome.JNDI_NAME);
    }

    public ItemUserPriceEntityLocal getEntity() {
        return itemUserPrice;
    }
    
    public void set(Integer id) throws FinderException {
        itemUserPrice = itemUserPriceHome.findByPrimaryKey(id);
    }
    
    public Integer create(Integer userId, Integer itemId, Integer currencyId,
            Float price) 
            throws CreateException {
        itemUserPrice = itemUserPriceHome.create(userId, itemId, price,
                currencyId);
                
        return itemUserPrice.getId();       
    }
    
    public void update(Integer executorId, ItemUserPriceDTOEx dto) 
            throws SessionInternalError {
        eLogger.audit(executorId, Constants.TABLE_ITEM_USER_PRICE, 
                itemUserPrice.getId(),
                EventLogger.MODULE_ITEM_TYPE_MAINTENANCE, 
                EventLogger.ROW_UPDATED, null,  
                itemUserPrice.getPrice().toString(), null);

        itemUserPrice.setPrice(dto.getPrice());
    }
    
    public void delete(Integer executorId) 
            throws RemoveException, NamingException, FinderException {

        eLogger.audit(executorId, Constants.TABLE_ITEM_USER_PRICE, 
                itemUserPrice.getId(),
                EventLogger.MODULE_ITEM_USER_PRICE_MAINTENANCE, 
                EventLogger.ROW_DELETED, null, null,null);
        itemUserPrice.remove();
    }
    
    public ItemUserPriceDTOEx getDTO() {
        ItemUserPriceDTOEx dto = new ItemUserPriceDTOEx();
        
        dto.setId(itemUserPrice.getId());
        dto.setItemId(itemUserPrice.getItem().getId());
        dto.setUserId(itemUserPrice.getUser().getUserId());
        dto.setPrice(itemUserPrice.getPrice());
        dto.setCurrencyId(itemUserPrice.getCurrencyId());
        
        return dto;
    }
    
    /**
     * Verifies if there's a price for the given user/item key, to
     * avoid duplications
     * @param userId
     * @param itemId
     * @return
     */
    public boolean isPresent(Integer userId, Integer itemId,
            Integer currencyId) {
        boolean retValue = true;
        try {
            itemUserPrice = itemUserPriceHome.find(userId, itemId, currencyId);
        } catch (FinderException e) {
            retValue = false;
        } 
        
        return retValue;
    }
}
