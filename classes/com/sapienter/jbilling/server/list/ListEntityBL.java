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
