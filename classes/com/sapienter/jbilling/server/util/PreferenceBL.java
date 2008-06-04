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

package com.sapienter.jbilling.server.util;

import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.PreferenceEntityLocal;
import com.sapienter.jbilling.interfaces.PreferenceEntityLocalHome;
import com.sapienter.jbilling.interfaces.PreferenceTypeEntityLocal;
import com.sapienter.jbilling.interfaces.PreferenceTypeEntityLocalHome;
import com.sapienter.jbilling.server.user.EntityBL;
import com.sapienter.jbilling.server.user.UserBL;

public class PreferenceBL {
    

    // private methods
    private JNDILookup EJBFactory = null;
    private PreferenceEntityLocalHome preferenceHome = null;
    private PreferenceTypeEntityLocalHome typeHome = null;
    private PreferenceEntityLocal preference = null;
    private PreferenceTypeEntityLocal type = null;
    private static Logger LOG = Logger.getLogger(PreferenceBL.class);
    private Locale locale = null;
    
    public PreferenceBL(Integer preferenceId) 
            throws FinderException {
        init();
        preference = preferenceHome.findByPrimaryKey(preferenceId);
    }
    
    public PreferenceBL() {
        init();
    }
    
    private void init() {
        try {
            EJBFactory = JNDILookup.getFactory(false);
            preferenceHome = (PreferenceEntityLocalHome) 
                    EJBFactory.lookUpLocalHome(
                    PreferenceEntityLocalHome.class,
                    PreferenceEntityLocalHome.JNDI_NAME);
            typeHome = (PreferenceTypeEntityLocalHome) 
                    EJBFactory.lookUpLocalHome(
                    PreferenceTypeEntityLocalHome.class,
                    PreferenceTypeEntityLocalHome.JNDI_NAME);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    /**
     * If the entity does not have explicitly set the property
     * it will throw a FinderException, but will still find the
     * defaut.
     * So you need to catch the finder and then use the default
     * @param entityId
     * @param typeId
     * @throws FinderException
     */
    public void set(Integer entityId, Integer typeId) 
            throws FinderException {
        LOG.debug("Now looking for preference " + typeId + " ent " +
                entityId + " table " + Constants.TABLE_ENTITY);
        if (entityId != null) {
            try {
                EntityBL en = new EntityBL(entityId);
                locale = en.getLocale();
            } catch (Exception e) {}
        }
        
        try {
            preference = preferenceHome.findByType_Row(
                    typeId, entityId, Constants.TABLE_ENTITY);
        } catch (FinderException e) {
            preference = null;
            type = typeHome.findByPrimaryKey(typeId);
            throw new FinderException(e.getMessage());
        }
            
    }

    public void setForUser(Integer userId, Integer typeId) 
            throws FinderException {
        try {
            UserBL us = new UserBL(userId);
            locale = us.getLocale();
        } catch (Exception e) {}

        try {
            preference = preferenceHome.findByType_Row(typeId, userId,
                    Constants.TABLE_BASE_USER);
        } catch (FinderException e) {
            preference = null;
            type = typeHome.findByPrimaryKey(typeId);
            throw new FinderException(e.getMessage());
        }

    }

    public void createUpdateForEntity(Integer entityId, Integer preferenceId, 
            Integer intValue, String strValue, Float fValue) 
            throws CreateException {
        // lets see first if this exists
        try {
            set(entityId, preferenceId);
            // it does
            preference.setIntValue(intValue);
            preference.setStrValue(strValue);
            preference.setFloatValue(fValue);
        } catch (FinderException e) {
            // we need a new one
            preferenceHome.create(preferenceId, Constants.TABLE_ENTITY, 
            		entityId, intValue, strValue, fValue);
        }
    }

    public void createUpdateForUser(Integer userId, Integer typeId, 
            Integer intValue, String strValue, Float fValue) 
            throws CreateException {
        // lets see first if this exists
        try {
            setForUser(userId, typeId);
            // it does
            preference.setIntValue(intValue);
            preference.setStrValue(strValue);
            preference.setFloatValue(fValue);
        } catch (FinderException e) {
            // we need a new one
            preferenceHome.create(typeId, Constants.TABLE_BASE_USER, 
                    userId, intValue, strValue, fValue);
        }
    }

    public int getInt() {
        if (preference != null) {
            return preference.getIntValue().intValue();
        } 
        
        return type.getIntDefValue().intValue();
    }

    public float getFloat() {
        if (preference != null) {
            return preference.getFloatValue().floatValue();
        } 
        
        return type.getFloatDefValue().floatValue();
    }

    public String getString() {
        if (preference != null) {
            return preference.getStrValue();
        }
        
        return type.getStrDefValue();
    }
    
    public String getValueAsString() {
        if (preference.getIntValue() != null) {
            return preference.getIntValue().toString();
        } else if (preference.getStrValue() != null) {
            return preference.getStrValue();
        } else if (preference.getFloatValue() != null) {
            if (locale == null) {
                return preference.getFloatValue().toString();
            } else {
                return Util.float2string(preference.getFloatValue(), locale);
            }
        }
        
        return null;
    }
    
    public String getDefaultAsString(Integer id) 
            throws FinderException{
        LOG.debug("Looking for preference default for type " + id);
        type = typeHome.findByPrimaryKey(id);
        if (type.getIntDefValue() != null) {
            return type.getIntDefValue().toString();
        } else if (type.getStrDefValue() != null) {
            return type.getStrDefValue();
        } else if (type.getFloatDefValue() != null) {
            if (locale == null) {
                return  type.getFloatDefValue().toString();
            } else {
                return Util.float2string(type.getFloatDefValue(), locale);
            }
        }
        
        return null;
    }
    
    public boolean isNull() {
    	return preference.getIntValue() == null && 
                preference.getStrValue() == null &&
                preference.getFloatValue() == null;
    }
    
    public PreferenceEntityLocal getEntity() {
        return preference;
    }
    
}
