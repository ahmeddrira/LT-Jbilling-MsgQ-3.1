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

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ListEntityEntityLocal;
import com.sapienter.jbilling.interfaces.ListFieldEntityEntityLocal;
import com.sapienter.jbilling.interfaces.ListFieldEntityEntityLocalHome;
import com.sapienter.jbilling.interfaces.ListFieldEntityLocal;

/**
 * @author Emil
 *
 */
public class ListFieldEntityBL {
    private JNDILookup EJBFactory = null;
    private ListFieldEntityEntityLocalHome fieldEntityHome = null;
    private ListFieldEntityEntityLocal fieldEntity = null;
    private Logger log = null;

    public ListFieldEntityBL(Integer fieldEntityId) 
            throws NamingException, FinderException {
        init();
        set(fieldEntityId);
    }

    public ListFieldEntityBL() throws NamingException {
        init();
    }

    public ListFieldEntityBL(ListFieldEntityEntityLocal fieldEntity) throws NamingException {
        init();
        set(fieldEntity);
    }

    private void init() throws NamingException {
        log = Logger.getLogger(ListFieldEntityBL.class);     
        EJBFactory = JNDILookup.getFactory(false);
        fieldEntityHome = (ListFieldEntityEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                ListFieldEntityEntityLocalHome.class,
                ListFieldEntityEntityLocalHome.JNDI_NAME);
    }

    public ListFieldEntityEntityLocal getEntity() {
        return fieldEntity;
    }
    
    public ListFieldEntityEntityLocalHome getHome() {
        return fieldEntityHome;
    }

    public void set(Integer id) throws FinderException {
        fieldEntity = fieldEntityHome.findByPrimaryKey(id);
    }
    
    public void set(ListFieldEntityEntityLocal fieldEntity) {
        this.fieldEntity = fieldEntity;
    }
    
    public void set(Integer fieldId, Integer listEntityId) 
            throws FinderException {
        fieldEntity = fieldEntityHome.findByFieldEntity(fieldId, listEntityId);
    }

    public void createUpdate(ListFieldEntityLocal field, 
            ListEntityEntityLocal entity, Integer min, Integer max, 
            String minStr, String maxStr, Date minDate, Date maxDate) 
            throws CreateException {
        try {
            fieldEntity = fieldEntityHome.findByFieldEntity(field.getId(), 
                    entity.getId());
        } catch (FinderException e) {
            fieldEntity = fieldEntityHome.create(entity, field);
        }

        fieldEntity.setMax(max);
        fieldEntity.setMin(min);
        fieldEntity.setMaxStr(maxStr);
        fieldEntity.setMinStr(minStr);
        fieldEntity.setMinDate(minDate);
        fieldEntity.setMaxDate(maxDate);
    }
}
