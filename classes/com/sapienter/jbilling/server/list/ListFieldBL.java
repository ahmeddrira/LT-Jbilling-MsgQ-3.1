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
