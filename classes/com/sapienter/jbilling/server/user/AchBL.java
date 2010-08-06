/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sapienter.jbilling.server.user;


import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.user.db.AchDAS;
import com.sapienter.jbilling.server.user.db.AchDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.audit.EventLogger;
import com.sapienter.jbilling.server.system.event.EventManager;
import com.sapienter.jbilling.server.user.event.AchUpdateEvent;
import com.sapienter.jbilling.server.user.event.AchDeleteEvent;

public class AchBL {
    private AchDAS achDas = null;
    private AchDTO ach = null;
    private Logger log = null;
    private EventLogger eLogger = null;
    
    public AchBL(Integer achId) {
        init();
        set(achId);
    }
    
    public AchBL() {
        init();
    }
    
    public AchBL(AchDTO row) {
        init();
        ach = row;
    }
    
    private void init() {
        log = Logger.getLogger(AchBL.class);     
        eLogger = EventLogger.getInstance();        
        achDas = new AchDAS();
    }

    public AchDTO getEntity() {
        return ach;
    }
    
    public void set(Integer id) {
        ach = achDas.find(id);
    }
    
    public void set(AchDTO pEntity) {
        ach = pEntity;
    }
    
    public Integer create(AchDTO dto) {
        ach = achDas.create(dto.getBaseUser(), dto.getAbaRouting(), dto.getBankAccount(),
                dto.getAccountType(), dto.getBankName(), dto.getAccountName(), dto.getGatewayKey());

	    if (ach.getBaseUser() != null) {
	        log.debug("create: Generating Update ACH event " + ach.getBaseUser().getCompany().getId());
	    	EventManager.process(new AchUpdateEvent(ach, ach.getBaseUser().getCompany().getId()));
	    }

        return ach.getId();
    }
    
    public void update(Integer executorId, AchDTO dto) {
        if (executorId != null) {
            eLogger.audit(executorId, dto.getBaseUser().getId(), 
                    Constants.TABLE_ACH, ach.getId(),
                    EventLogger.MODULE_CREDIT_CARD_MAINTENANCE, 
                    EventLogger.ROW_UPDATED, null,  
                    ach.getBankAccount(), null);
        }
        ach.setAbaRouting(dto.getAbaRouting());
        ach.setAccountName(dto.getAccountName());
        ach.setAccountType(dto.getAccountType());
        ach.setBankAccount(dto.getBankAccount());
        ach.setBankName(dto.getBankName());
        ach.setGatewayKey(dto.getGatewayKey());
        if (ach.getBaseUser() != null) {
            log.debug("create: Generating Update ACH event " + ach.getBaseUser().getCompany().getId());
            EventManager.process(new AchUpdateEvent(ach, ach.getBaseUser().getCompany().getId()));
        }
    }
    
    public void delete(Integer executorId) {
        //delete the external ach info by invoking the AchDeleteEvent.
        if (ach.getBaseUser() != null) {
            log.debug("create: Generating Delete ACH event " + ach.getBaseUser().getCompany().getId());
            EventManager.process(new AchDeleteEvent(ach, ach.getBaseUser().getCompany().getId()));
        }
        //for the logger
        Integer userId = ach.getBaseUser().getId();
        Integer achId = ach.getId();
        
        ach.getBaseUser().getAchs().remove(ach);
        
        //now delete this ach record
        achDas.delete(ach);
        ach= null;
        eLogger.audit(executorId, userId, 
                Constants.TABLE_ACH, achId,
                EventLogger.MODULE_CREDIT_CARD_MAINTENANCE, 
                EventLogger.ROW_DELETED, null, null,null);
    }
    
    public AchDTO getDTO() {
        AchDTO dto = new AchDTO();
        
        dto.setId(ach.getId());
        dto.setAbaRouting(ach.getAbaRouting());
        dto.setAccountName(ach.getAccountName());
        dto.setAccountType(ach.getAccountType());
        dto.setBankAccount(ach.getBankAccount());
        dto.setBankName(ach.getBankName());
        dto.setGatewayKey(ach.getGatewayKey());
        
        return dto;
    }
}
