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
package com.sapienter.jbilling.server.order;

import java.lang.reflect.Method;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.util.WSMethodBaseSecurityProxy;

/**
 * @author Emil
 */
public class WSMethodSecurityProxy extends WSMethodBaseSecurityProxy {

    public void init(Class beanHome, Class beanRemote,
            Class beanLocalHome, Class beanLocal, Object securityMgr)
            throws InstantiationException {
                
       log = Logger.getLogger(WSMethodSecurityProxy.class);
       String methodName = null;
       try {
           Method methods[] = new Method[6];
           Method aMethod;
           // create
           Class params[] = new Class[1];
           params[0] = OrderWS.class;
           methodName = "create";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[0] = aMethod;
           // update - takes same parameters as create
           methodName = "update";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[1] = aMethod;
           // get
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "get";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[2] = aMethod;
           // delete
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "delete";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[3] = aMethod;
           // getLatest
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getLatest";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[4] = aMethod;
           // getLast
           params = new Class[2];
           params[0] = Integer.class;
           params[1] = Integer.class;
           methodName = "getLast";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[5] = aMethod;
           
           // set the parent methods
           setMethods(methods);          

       } catch(NoSuchMethodException e) {
          String msg = "Failed to find method " + methodName;
          log.error(msg, e);
          throw new InstantiationException(msg);
       }
    }
    
    public void invoke(Method m, Object[] args, Object bean)
            throws SecurityException {
        //log.debug("invoke, m=" + m);
        if (!isMethodPresent(m)) {
            return;
        }
        try {
            // Take the OrderWS
            if(m.getName().equals("create") || m.getName().equals("update")) {
                OrderWS arg = (OrderWS) args[0];
                
                if (arg != null) {
                    validate(arg.getUserId());
                    if (m.getName().equals("update")) {
                        OrderBL bl = new OrderBL(arg.getId());
                        validate(bl.getEntity().getUser().getUserId());
                    }
                }
            } else if(m.getName().equals("get") || 
                    m.getName().equals("delete")) {
                Integer arg = (Integer) args[0];
                
                if (arg != null) {
                    OrderBL bl = new OrderBL(arg);
                    validate(bl.getEntity().getUser().getUserId());
                }
            } else if (m.getName().equals("getLatest") || 
                    m.getName().equals("getLast")) {
                Integer arg = (Integer) args[0];
                if (arg != null) {
                    validate(arg);
                }
            }
        } catch (SessionInternalError e) {
            log.error("Exception ", e);
            throw new SecurityException(e.getMessage());
        } catch (NamingException e) {
            log.error("Exception ", e);
            throw new SecurityException(e.getMessage());
        } catch (FinderException e) {
            // no need to log, this simply means that the request is rejected
            throw new SecurityException(e.getMessage());
        }
    }
}
