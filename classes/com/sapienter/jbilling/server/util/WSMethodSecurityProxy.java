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

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.OrderLineEntityLocal;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.payment.PaymentBL;
import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.UserWS;

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
           Method methods[] = new Method[21];
           Method aMethod;
           int i = 0;

           // getInvoiceWS
           Class params[] = new Class[1];
           params[0] = Integer.class;
           methodName = "getInvoiceWS";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // getLatestInvoice
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getLatestInvoice";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;

           // getLastInvoices
           params = new Class[2];
           params[0] = Integer.class;
           params[1] = Integer.class;
           methodName = "getLastInvoices";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // getUserWS
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getUserWS";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // deleteUser
           // the parameters are the same
           methodName = "deleteUser";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // updateUser
           params = new Class[1];
           params[0] = UserWS.class;
           methodName ="updateUser";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;

           // updateUserContact
           params = new Class[3];
           params[0] = Integer.class;
           params[1] = Integer.class;
           params[2] = ContactWS.class;
           methodName ="updateUserContact";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;

           // createOrder
           params = new Class[1];
           params[0] = OrderWS.class;
           methodName = "createOrder";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // updateOrder - takes same parameters as create
           methodName = "updateOrder";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // getOrder
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getOrder";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // deleteOrder
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "deleteOrder";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // getLatestOrder
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getLatestOrder";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // getLastOrders
           params = new Class[2];
           params[0] = Integer.class;
           params[1] = Integer.class;
           methodName = "getLastOrders";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // applyPayment
           params = new Class[2];
           params[0] = PaymentWS.class;
           params[1] = Integer.class;
           methodName = "applyPayment";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // getPayment
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getPayment";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // getLatestPayment
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getLatestPayment";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // getLastPayments
           params = new Class[2];
           params[0] = Integer.class;
           params[1] = Integer.class;
           methodName = "getLastPayments";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // getOrderLine
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getOrderLine";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;
           
           // updateOrderLine
           params = new Class[1];
           params[0] = OrderLineWS.class;
           methodName = "updateOrderLine";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;

           // getOrderByPeriod
           params = new Class[2];
           params[0] = Integer.class;
           params[1] = Integer.class;
           methodName = "getOrderByPeriod";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;

           // getUserContactsWS
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getUserContactsWS";
           aMethod = beanLocal.getDeclaredMethod(methodName, params);
           methods[i++] = aMethod;

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
            if(m.getName().equals("getInvoiceWS")) {
                Integer arg = (Integer) args[0];
                
                if (arg != null) {
                    InvoiceBL bl = new InvoiceBL(arg);
                    validate(bl.getEntity().getUser().getUserId());
                }
            } else if (m.getName().equals("getLatestInvoice") || 
                    m.getName().equals("getLastInvoices")) {
                Integer userId = (Integer) args[0];
                
                if (userId != null) {
                    UserBL bl = new UserBL(userId);
                    validate(userId);
                }
            } else if(m.getName().equals("getUserWS") || 
                    m.getName().equals("deleteUser") ||
                    m.getName().equals("updateUserContact") ||
                    m.getName().equals("getUserContactsWS")) {
                Integer arg = (Integer) args[0];
            
                if (arg != null) {
                    validate(arg);
                }
            } else if(m.getName().equals("updateUser")) { // it only takes a UserWS as argument
                UserWS arg = (UserWS) args[0];
                if (arg != null) {
                    validate(arg.getUserId());
                }
            } else if(m.getName().equals("createOrder") || m.getName().equals("updateOrder")) {
                OrderWS arg = (OrderWS) args[0];
                
                if (arg != null) {
                    validate(arg.getUserId());
                    if (m.getName().equals("updateOrder")) {
                        OrderBL bl = new OrderBL(arg.getId());
                        validate(bl.getEntity().getUser().getUserId());
                    }
                }
            } else if(m.getName().equals("getOrder") || 
                    m.getName().equals("deleteOrder")) {
                Integer arg = (Integer) args[0];
                
                if (arg != null) {
                    OrderBL bl = new OrderBL(arg);
                    validate(bl.getEntity().getUser().getUserId());
                }
            } else if(m.getName().equals("getOrderLine")) {
                Integer arg = (Integer) args[0];
                
                if (arg != null) {
                    OrderBL bl = new OrderBL();
                    OrderLineEntityLocal line = bl.getOrderLine(arg);
                    validate(line.getOrder().getUser().getUserId());
                }
            } else if(m.getName().equals("updateOrderLine")) {
                OrderLineWS arg = (OrderLineWS) args[0];
                
                if (arg != null) {
                    OrderBL bl = new OrderBL();
                    OrderLineEntityLocal line = bl.getOrderLine(arg.getId());
                    validate(line.getOrder().getUser().getUserId());
                }
                
            } else if (m.getName().equals("getLatestOrder") || 
                    m.getName().equals("getLastOrders") ||
                    m.getName().equals("getOrderByPeriod")) {
                Integer arg = (Integer) args[0];
                if (arg != null) {
                    validate(arg);
                }
            } else if(m.getName().equals("applyPayment")) {
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
            } else if (m.getName().equals("getLatestPayment") ||
                    m.getName().equals("getLastPayments")) {
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
