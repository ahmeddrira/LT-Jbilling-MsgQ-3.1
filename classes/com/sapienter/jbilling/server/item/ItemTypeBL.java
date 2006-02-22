/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
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
import com.sapienter.jbilling.server.util.EventLogger;

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
