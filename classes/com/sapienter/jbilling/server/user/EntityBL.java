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
 * Created on May 5, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.user;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.EntityEntityLocal;
import com.sapienter.jbilling.interfaces.EntityEntityLocalHome;
import com.sapienter.jbilling.interfaces.LanguageEntityLocal;
import com.sapienter.jbilling.interfaces.LanguageEntityLocalHome;
import com.sapienter.jbilling.server.entity.ContactDTO;
import com.sapienter.jbilling.server.list.ResultList;

/**
 * @author Emil
 */
public class EntityBL extends ResultList 
        implements EntitySQL {
    private JNDILookup EJBFactory = null;
    private EntityEntityLocalHome entityHome = null;
    private EntityEntityLocal entity = null;
    
    public EntityBL() 
            throws NamingException {
        init();
    }
    
    public EntityBL(Integer id) 
    		throws FinderException, NamingException {
    	init();
    	entity = entityHome.findByPrimaryKey(id);
    }
    
    public EntityBL(String externalId) 
            throws FinderException, NamingException {
        init();
        entity = entityHome.findByExternalId(externalId);
    }
    
    public EntityEntityLocalHome getHome() {
    	return  entityHome;
    }
    
    private void init() throws NamingException {
        EJBFactory = JNDILookup.getFactory(false);
        entityHome = (EntityEntityLocalHome) EJBFactory.lookUpLocalHome(
                EntityEntityLocalHome.class,
                EntityEntityLocalHome.JNDI_NAME);

    }
    
    public EntityEntityLocal getEntity() {
        return entity;
    }
    
    public Locale getLocale() 
    		throws NamingException, FinderException {
    	Locale retValue = null;
    	// get the language first
    	Integer languageId = entity.getLanguageId();
    	LanguageEntityLocalHome languageHome = (LanguageEntityLocalHome) 
				EJBFactory.lookUpLocalHome(
                LanguageEntityLocalHome.class,
                LanguageEntityLocalHome.JNDI_NAME);
    	LanguageEntityLocal language = languageHome.findByPrimaryKey(
    			languageId);
    	String languageCode = language.getCode();
    	
    	// now the country
    	ContactBL contact = new ContactBL();
    	contact.setEntity(entity.getId());
    	String countryCode = contact.getEntity().getCountryCode();
    	
    	if (countryCode != null) {
    		retValue = new Locale(languageCode, countryCode);
    	} else {
    		retValue = new Locale(languageCode);
    	}

    	return retValue;
    }

    /**
     * Creates a new entity with its root user. Uses all defaults.
     * @param user
     * @param contact
     * @param languageId
     * @return
     * @throws Exception
     */
    public Integer create(UserDTOEx user, ContactDTO contact, 
            Integer languageId) 
            throws Exception {
        EntitySignup sign = new EntitySignup(user, contact, languageId);
        return new Integer(sign.process());
    }
    
    public Integer[] getAllIDs() 
            throws SQLException, NamingException {
        Vector list = new Vector();
        
        prepareStatement(EntitySQL.listAll);
        execute();
        conn.close();
        
        while (cachedResults.next()) {
            list.add(new Integer(cachedResults.getInt(1)));
        } 
        
        Integer[] retValue = new Integer[list.size()];
        list.toArray(retValue);
        return retValue;
    }
    
    public CachedRowSet getTables() 
            throws SQLException, NamingException {
        prepareStatement(EntitySQL.getTables);
        execute();
        conn.close();
        
        return cachedResults;
    }
}
