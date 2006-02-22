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
 * Created on Nov 26, 2004
 *
 */
package com.sapienter.jbilling.server.list;

import java.util.Calendar;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ListEntityEntityLocal;
import com.sapienter.jbilling.interfaces.ListEntityEntityLocalHome;
import com.sapienter.jbilling.interfaces.ListEntityLocal;

/**
 * @author Emil
 *
 */
public class ListEntityBL {
    private JNDILookup EJBFactory = null;
    private ListEntityEntityLocalHome listEntityHome = null;
    private ListEntityEntityLocal listEntity = null;
    private Logger log = null;

    public ListEntityBL(Integer listEntityId) 
            throws NamingException, FinderException {
        init();
        set(listEntityId);
    }

    public ListEntityBL() throws NamingException {
        init();
    }

    public ListEntityBL(ListEntityEntityLocal listEntity) throws NamingException {
        init();
        set(listEntity);
    }

    private void init() throws NamingException {
        log = Logger.getLogger(ListEntityBL.class);     
        EJBFactory = JNDILookup.getFactory(false);
        listEntityHome = (ListEntityEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                ListEntityEntityLocalHome.class,
                ListEntityEntityLocalHome.JNDI_NAME);
    }

    public ListEntityEntityLocal getEntity() {
        return listEntity;
    }
    
    public ListEntityEntityLocalHome getHome() {
        return listEntityHome;
    }

    public void set(Integer id) throws FinderException {
        listEntity = listEntityHome.findByPrimaryKey(id);
    }
    
    public void set(Integer listId, Integer entityId)
            throws FinderException {
        listEntity = listEntityHome.findByEntity(listId, entityId);
    }
    
    public void set(ListEntityEntityLocal listEntity) {
        this.listEntity = listEntity;
    }
    
    public void create(ListEntityLocal list, Integer entityId, 
            Integer count) 
            throws CreateException {
        listEntity = listEntityHome.create(list, entityId, count);
    }
    
    public void update(Integer count) {
        listEntity.setTotalRecords(count);
        listEntity.setLastUpdate(Calendar.getInstance().getTime());
    }

}
