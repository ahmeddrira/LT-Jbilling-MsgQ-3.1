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
package com.sapienter.jbilling.server.payment;

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
           Method methods[] = new Method[4];
           Method aMethod;

           // apply
           Class params[] = new Class[2];
           params[0] = PaymentWS.class;
           params[1] = Integer.class;
           methodName = "applyPayment";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[0] = aMethod;
           // getPayment
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getPayment";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[1] = aMethod;
           // getLatest
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getLatest";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[2] = aMethod;
           // getLast
           params = new Class[2];
           params[0] = Integer.class;
           params[1] = Integer.class;
           methodName = "getLast";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[3] = aMethod;
           
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
            if(m.getName().equals("applyPayment")) {
                PaymentWS arg = (PaymentWS) args[0];
                
                if (arg != null) {
                    validate(arg.getUserId());
                }
            } else if (m.getName().equals("getPayment")) {
                Integer arg = (Integer) args[0];
                
                if (arg != null) {
                    PaymentBL bl = new PaymentBL(arg);
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
