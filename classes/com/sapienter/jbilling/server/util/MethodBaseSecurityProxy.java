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
