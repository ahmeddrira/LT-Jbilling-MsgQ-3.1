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
import com.sapienter.jbilling.common.SessionInternalError;
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
    
    public Integer getRootUser(Integer entityId) {
        try {
            prepareStatement(EntitySQL.findRoot);
            cachedResults.setInt(1, entityId);

            execute();
            conn.close();
            
            cachedResults.next();
            return cachedResults.getInt(1);
        } catch (Exception e) {
            throw new SessionInternalError("Finding root user for entity " + 
                    entity.getId(), EntityBL.class, e);
        } 
    }
}
