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

package com.sapienter.jbilling.server.customer;

import java.sql.SQLException;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.CustomerEntityLocal;
import com.sapienter.jbilling.interfaces.CustomerEntityLocalHome;
import com.sapienter.jbilling.server.list.ResultList;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @author Emil
 */
public final class CustomerBL extends ResultList implements CustomerSQL {
    
    private CustomerEntityLocal customer = null;
    private CustomerEntityLocalHome customerHome = null;
    private final Logger LOG = Logger.getLogger(CustomerBL.class);
    private JNDILookup EJBFactory = null;

    
    public CustomerBL() {};
    
    public CustomerBL(Integer id) throws FinderException {
        try {
            EJBFactory = JNDILookup.getFactory(false);
            customerHome = (CustomerEntityLocalHome) EJBFactory.lookUpLocalHome(
                    CustomerEntityLocalHome.class,
                    CustomerEntityLocalHome.JNDI_NAME);
            customer = customerHome.findByPrimaryKey(id);
        } catch (ClassCastException e) {
            throw new SessionInternalError(e);
        } catch (NamingException e) {
            throw new SessionInternalError(e);
        } 
    }
    
    public CustomerEntityLocal getEntity() {
        return customer;
    }

    public CachedRowSet getList(int entityID, Integer userRole,
            Integer userId) 
            throws SQLException, Exception{
        
        if(userRole.equals(Constants.TYPE_ROOT)) {
            prepareStatement(CustomerSQL.listRoot); 
            cachedResults.setInt(1,entityID);
        } else if(userRole.equals(Constants.TYPE_CLERK)) {
            prepareStatement(CustomerSQL.listClerk);
            cachedResults.setInt(1,entityID);
        } else if(userRole.equals(Constants.TYPE_PARTNER)) {
            prepareStatement(CustomerSQL.listPartner);
            cachedResults.setInt(1, entityID);
            cachedResults.setInt(2, userId.intValue());
        } else {
            throw new Exception("The user list for the type " + userRole + 
                    " is not supported");
        }
        
        execute();
        conn.close();
        return cachedResults;
    }

    // this is the list for the Customer menu option, where only
    // customers/partners are listed. Meant for the clients customer service
    public CachedRowSet getCustomerList(int entityID, Integer userRole,
            Integer userId) 
            throws SQLException, Exception {
        
        if(userRole.equals(Constants.TYPE_INTERNAL) || 
                userRole.equals(Constants.TYPE_ROOT) || 
                userRole.equals(Constants.TYPE_CLERK)) {
            prepareStatement(CustomerSQL.listCustomers);
            cachedResults.setInt(1,entityID);
        } else if(userRole.equals(Constants.TYPE_PARTNER)) {
            prepareStatement(CustomerSQL.listPartner);
            cachedResults.setInt(1, entityID);
            cachedResults.setInt(2, userId.intValue());
        } else {
            throw new Exception("The user list for the type " + userRole + 
                    " is not supported");
        }
        
        execute();
        conn.close();
        return cachedResults;
    }
    
    public CachedRowSet getSubAccountsList(Integer userId) 
            throws SQLException, Exception {
        
        // find out the customer id of this user
        UserBL user = new UserBL(userId);
        
        prepareStatement(CustomerSQL.listSubaccounts);
        cachedResults.setInt(1,user.getEntity().getCustomer().getId().
                intValue());
        
        execute();
        conn.close();
        return cachedResults;
    }

}
