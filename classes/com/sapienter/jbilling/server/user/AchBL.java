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

package com.sapienter.jbilling.server.user;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.AchEntityLocal;
import com.sapienter.jbilling.interfaces.AchEntityLocalHome;
import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.audit.EventLogger;

public class AchBL {
    private JNDILookup EJBFactory = null;
    private AchEntityLocalHome achHome = null;
    private AchEntityLocal ach = null;
    private Logger log = null;
    private EventLogger eLogger = null;
    
    public AchBL(Integer achId) 
            throws NamingException, FinderException {
        init();
        set(achId);
    }
    
    public AchBL() throws NamingException {
        init();
    }
    
    public AchBL(AchEntityLocal row)
            throws NamingException {
        init();
        ach = row;
    }
    
    private void init() throws NamingException {
        log = Logger.getLogger(AchBL.class);     
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        achHome = (AchEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                AchEntityLocalHome.class,
                AchEntityLocalHome.JNDI_NAME);
    }

    public AchEntityLocal getEntity() {
        return ach;
    }
    
    public void set(Integer id) throws FinderException {
        ach = achHome.findByPrimaryKey(id);
    }
    
    public void set(AchEntityLocal pEntity) {
        ach = pEntity;
    }
    
    public Integer create(AchDTO dto) 
            throws CreateException {
        ach = achHome.create(dto.getAbaRouting(), dto.getBankAccount(),
        		dto.getAccountType(), dto.getBankName(),
				dto.getAccountName()); 
                
        return ach.getId();       
    }
    
    public void update(Integer executorId, AchDTO dto) 
            throws SessionInternalError {
        if (executorId != null) {
            eLogger.audit(executorId, Constants.TABLE_ACH, 
                    ach.getId(),
                    EventLogger.MODULE_CREDIT_CARD_MAINTENANCE, 
                    EventLogger.ROW_UPDATED, null,  
                    ach.getBankAccount(), null);
        }
        ach.setAbaRouting(dto.getAbaRouting());
        ach.setAccountName(dto.getAccountName());
        ach.setAccountType(dto.getAccountType());
        ach.setBankAccount(dto.getBankAccount());
        ach.setBankName(dto.getBankName());
    }
    
    public void delete(Integer executorId) 
            throws RemoveException, NamingException, FinderException {
        // now delete this ach record
        eLogger.audit(executorId, Constants.TABLE_ACH, 
                ach.getId(),
                EventLogger.MODULE_CREDIT_CARD_MAINTENANCE, 
                EventLogger.ROW_DELETED, null, null,null);
        
        ach.remove();
    }
    
    public AchDTO getDTO() {
        AchDTO dto = new AchDTO();
        
        dto.setId(ach.getId());
        dto.setAbaRouting(ach.getAbaRouting());
        dto.setAccountName(ach.getAccountName());
        dto.setAccountType(ach.getAccountType());
        dto.setBankAccount(ach.getBankAccount());
        dto.setBankName(ach.getBankName());
        
        return dto;
    }
}
