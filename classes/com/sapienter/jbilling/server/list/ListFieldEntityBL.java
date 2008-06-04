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
