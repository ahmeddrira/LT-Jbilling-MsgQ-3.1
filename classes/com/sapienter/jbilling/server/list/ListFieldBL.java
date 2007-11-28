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

/*
 * Created on Nov 26, 2004
 *
 */
package com.sapienter.jbilling.server.list;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ListFieldEntityLocal;
import com.sapienter.jbilling.interfaces.ListFieldEntityLocalHome;

/**
 * @author Emil
 *
 */
public class ListFieldBL {
    private JNDILookup EJBFactory = null;
    private ListFieldEntityLocalHome listFieldHome = null;
    private ListFieldEntityLocal listField = null;
    private Logger log = null;

    public ListFieldBL(Integer listFieldId) 
            throws NamingException, FinderException {
        init();
        set(listFieldId);
    }

    public ListFieldBL() throws NamingException {
        init();
    }

    public ListFieldBL(ListFieldEntityLocal listField) throws NamingException {
        init();
        set(listField);
    }

    private void init() throws NamingException {
        log = Logger.getLogger(ListFieldBL.class);     
        EJBFactory = JNDILookup.getFactory(false);
        listFieldHome = (ListFieldEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                ListFieldEntityLocalHome.class,
                ListFieldEntityLocalHome.JNDI_NAME);
    }

    public ListFieldEntityLocal getEntity() {
        return listField;
    }
    
    public ListFieldEntityLocalHome getHome() {
        return listFieldHome;
    }

    public void set(Integer id) throws FinderException {
        listField = listFieldHome.findByPrimaryKey(id);
    }
    
    public void set(ListFieldEntityLocal listField) {
        this.listField = listField;
    }
}
