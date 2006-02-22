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

/*
 * Created on 27-Feb-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.item;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.EntityEntityLocal;
import com.sapienter.jbilling.interfaces.EntityEntityLocalHome;
import com.sapienter.jbilling.interfaces.ItemEntityLocal;
import com.sapienter.jbilling.server.list.ListDTO;
import com.sapienter.jbilling.server.list.ResultList;

/**
 * @author Emil
 */
public final class ItemListBL extends ResultList 
        implements ItemSQL, Serializable {

	public CachedRowSet getList(Integer entityID) 
            throws SQLException, Exception{
	    prepareStatement(ItemSQL.list);
	    cachedResults.setInt(1,entityID.intValue());
        // the language is now always the entity's
        // cachedResults.setInt(2,languageId.intValue());
	    
	    execute();
	    conn.close();
	    return cachedResults;
	}

    public CachedRowSet getTypeList(Integer entityID) 
            throws SQLException, Exception{
        prepareStatement(ItemSQL.listType);
        cachedResults.setInt(1, entityID.intValue());
        // cachedResults.setInt(2,languageId.intValue());
        execute();
        conn.close();
        return cachedResults;
    }

    public CachedRowSet getUserPriceList(Integer entityID, Integer userId,
            Integer languageId) throws SQLException, Exception{
        prepareStatement(ItemSQL.listUserPrice);
        cachedResults.setInt(1, entityID.intValue());
        cachedResults.setInt(2, userId.intValue());
        cachedResults.setInt(3, languageId.intValue());
        execute();
        conn.close();
        return cachedResults;
    }
    
    public CachedRowSet getPromotionList(Integer entityID, Integer languageId) 
            throws SQLException, Exception{
        prepareStatement(ItemSQL.listPromotion);
        cachedResults.setInt(1,entityID.intValue());
        cachedResults.setInt(2,languageId.intValue());
        
        execute();
        conn.close();
        return cachedResults;
    }

    public ListDTO getOrderList(Integer entityID, Integer languageId, 
            Integer userId) 
            throws FinderException, NamingException, SessionInternalError {
        ListDTO result = new ListDTO();

        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        EntityEntityLocalHome entityHome =
                (EntityEntityLocalHome) EJBFactory.lookUpLocalHome(
                EntityEntityLocalHome.class,
                EntityEntityLocalHome.JNDI_NAME);
        EntityEntityLocal entity = entityHome.findByPrimaryKey(entityID);
        // I wish I could exclude the deleted right here
        Collection items = entity.getItems();
        
        int fields = 4;
        
        result.setTypes(new Integer[fields]);
        result.getTypes()[0] = new Integer(Types.VARCHAR);
        result.getTypes()[1] = new Integer(Types.VARCHAR);
        result.getTypes()[2] = new Integer(Types.VARCHAR);
        result.getTypes()[3] = new Integer(Types.FLOAT);
        
        for (Iterator it = items.iterator(); it.hasNext();) {
            ItemEntityLocal item = (ItemEntityLocal) it.next();
            if (item.getDeleted().intValue() == 1) {
                continue;
            }
            ItemBL itemBL = new ItemBL(item);
            Object columns[] = new Object[fields];
            columns[0] = new String(item.getId().toString());
            columns[1] = new String(item.getDescription(languageId));
            if (item.getPercentage() != null) {
                columns[2] = "%";
                columns[3] = item.getPercentage();
            } else {
                columns[3] = itemBL.getPrice(userId, entityID);
                columns[2] = itemBL.getPriceCurrencySymbol(); 
            }
            result.getLines().add(columns);
        }
        
        return result;
    }
    
}

