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
import com.sapienter.jbilling.interfaces.ItemTypeEntityLocal;
import com.sapienter.jbilling.interfaces.ItemTypeEntityLocalHome;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.DescriptionBL;
import com.sapienter.jbilling.server.util.audit.EventLogger;

public class ItemTypeBL {
    private JNDILookup EJBFactory = null;
    private ItemTypeEntityLocalHome itemTypeHome = null;
    private ItemTypeEntityLocal itemType = null;
    private Logger log = null;
    private EventLogger eLogger = null;
    
    public ItemTypeBL(Integer itemTypeId) 
            throws NamingException, FinderException {
        init();
        set(itemTypeId);
    }
    
    public ItemTypeBL() throws NamingException {
        init();
    }
    
    private void init() throws NamingException {
        log = Logger.getLogger(ItemTypeBL.class);     
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        itemTypeHome = (ItemTypeEntityLocalHome) EJBFactory.lookUpLocalHome(
                ItemTypeEntityLocalHome.class,
                ItemTypeEntityLocalHome.JNDI_NAME);
    }

    public ItemTypeEntityLocal getEntity() {
        return itemType;
    }
    
    public void set(Integer id) throws FinderException {
        itemType = itemTypeHome.findByPrimaryKey(id);
    }
    
    public void create(ItemTypeDTOEx dto) 
            throws CreateException {
        itemType = itemTypeHome.create(dto.getEntityId(), 
                dto.getOrderLineTypeId(), dto.getDescription());        
    }
    
    public void update(Integer executorId, ItemTypeDTOEx dto) 
            throws SessionInternalError {
        eLogger.audit(executorId, Constants.TABLE_ITEM_TYPE, itemType.getId(),
                EventLogger.MODULE_ITEM_TYPE_MAINTENANCE, 
                EventLogger.ROW_UPDATED, null,  
                itemType.getDescription(), null);

        itemType.setDescription(dto.getDescription());
        itemType.setOrderLineTypeId(dto.getOrderLineTypeId());
    }
    
    public void delete(Integer executorId) 
            throws RemoveException, NamingException, FinderException {
        Integer itemTypeId = itemType.getId();
        itemType.remove();
        // now remove all the descriptions 
        DescriptionBL desc = new DescriptionBL();
        desc.delete(Constants.TABLE_ITEM_TYPE, itemTypeId);

        eLogger.audit(executorId, Constants.TABLE_ITEM_TYPE, itemTypeId,
                EventLogger.MODULE_ITEM_TYPE_MAINTENANCE, 
                EventLogger.ROW_DELETED, null, null,null);
    }
}
