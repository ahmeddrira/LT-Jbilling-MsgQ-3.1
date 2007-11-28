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
 * Created on Dec 24, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.util;

import java.lang.reflect.Method;

import javax.ejb.EJBContext;
import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.jboss.security.SecurityProxy;

import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.UserDTOEx;

/**
 * @author Emil
 */
public abstract class MethodBaseSecurityProxy implements SecurityProxy {

    protected Logger log = null; 
    private Method methods[] = null;
    protected EJBContext context = null;

    public void init(Class beanHome, Class beanRemote,
            Object securityMgr)
            throws InstantiationException {
       init(beanHome, beanRemote, null, null, securityMgr);
    }
    
    public abstract void init(Class beanHome, Class beanRemote,
            Class beanLocalHome, Class beanLocal, Object securityMgr)
            throws InstantiationException;

    public abstract void invoke(Method m, Object[] args, Object bean)
            throws SecurityException;
                
    
    public void setEJBContext(EJBContext ctx) {
       //log.debug("setEJBContext, ctx="+ctx);
       context = ctx;
    }
    
    public void invokeHome(Method m, Object[] args)
        throws SecurityException {
       // We don't validate access to home methods
    }
    
    protected boolean isMethodPresent(Method m) {
        boolean retValue = false;
        
        for (int f = 0; f < methods.length; f++) {
            if (methods[f].equals(m)) {
                retValue = true;
                break;
            }
        }
        
        return retValue;
    }
    
    /**
     * @param methods
     */
    public void setMethods(Method[] methods) {
        this.methods = methods;
    }
    
    /**
     * This method is pretty heavy ... it should be useing a cached mapped.
     * @param userId
     * @param permission
     */
    protected void validatePermission(Integer userId, Integer permission) {
        UserBL user;
        try {
            user = new UserBL(userId);
        } catch (FinderException e) {
            throw new SecurityException("User not found " + userId);
        }
        UserDTOEx dto = new UserDTOEx();
        dto.setPermissions(user.getPermissions());
        if (!dto.isGranted(permission)) {
            throw new SecurityException("Permission " + permission +
                    " not grated for " + userId);
        }
    }
}
