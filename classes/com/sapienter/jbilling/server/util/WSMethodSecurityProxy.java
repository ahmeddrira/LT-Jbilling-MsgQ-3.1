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
import java.util.ArrayList;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.OrderLineEntityLocal;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
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
    	   ArrayList<Method> methods = new ArrayList<Method>(24);

           // getInvoiceWS
           Class params[] = new Class[1];
           params[0] = Integer.class;
           methodName = "getInvoiceWS";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // getLatestInvoice
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getLatestInvoice";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));

           // getLastInvoices
           params = new Class[2];
           params[0] = Integer.class;
           params[1] = Integer.class;
           methodName = "getLastInvoices";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // getUserWS
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getUserWS";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // deleteUser
           // the parameters are the same
           methodName = "deleteUser";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // updateUser
           params = new Class[1];
           params[0] = UserWS.class;
           methodName ="updateUser";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));

           // updateUserContact
           params = new Class[3];
           params[0] = Integer.class;
           params[1] = Integer.class;
           params[2] = ContactWS.class;
           methodName ="updateUserContact";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));

           // createOrder
           params = new Class[1];
           params[0] = OrderWS.class;
           methodName = "createOrder";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));

           // createOrderPreAuthorize
           params = new Class[1];
           params[0] = OrderWS.class;
           methodName = "createOrderPreAuthorize";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));

           // updateOrder - takes same parameters as create
           methodName = "updateOrder";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // getOrder
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getOrder";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // deleteOrder
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "deleteOrder";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // getLatestOrder
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getLatestOrder";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // getLastOrders
           params = new Class[2];
           params[0] = Integer.class;
           params[1] = Integer.class;
           methodName = "getLastOrders";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // applyPayment
           params = new Class[2];
           params[0] = PaymentWS.class;
           params[1] = Integer.class;
           methodName = "applyPayment";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // getPayment
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getPayment";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // getLatestPayment
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getLatestPayment";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // getLastPayments
           params = new Class[2];
           params[0] = Integer.class;
           params[1] = Integer.class;
           methodName = "getLastPayments";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // getOrderLine
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getOrderLine";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           // updateOrderLine
           params = new Class[1];
           params[0] = OrderLineWS.class;
           methodName = "updateOrderLine";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));

           // getOrderByPeriod
           params = new Class[2];
           params[0] = Integer.class;
           params[1] = Integer.class;
           methodName = "getOrderByPeriod";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));

           // getUserContactsWS
           params = new Class[1];
           params[0] = Integer.class;
           methodName = "getUserContactsWS";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));

           // updateCreditCard
           params = new Class[2];
           params[0] = Integer.class;
           params[1] = CreditCardDTO.class;
           methodName = "updateCreditCard";
           methods.add(beanLocal.getDeclaredMethod(methodName, params));
           
           //payInvoice
           methods.add(beanLocal.getDeclaredMethod("payInvoice", new Class[]{Integer.class}));

           // set the parent methods
           setMethods(methods.toArray(new Method[methods.size()]));          

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
            if(m.getName().equals("getInvoiceWS") || m.getName().equals("payInvoice")) {
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
                    m.getName().equals("getUserContactsWS") ||
                    m.getName().equals("updateCreditCard")) {
                Integer arg = (Integer) args[0];
            
                if (arg != null) {
                    validate(arg);
                }
            } else if(m.getName().equals("updateUser")) { // it only takes a UserWS as argument
                UserWS arg = (UserWS) args[0];
                if (arg != null) {
                    validate(arg.getUserId());
                }
            } else if(m.getName().equals("createOrder") || 
                    m.getName().equals("updateOrder") ||
                    m.getName().equals("createOrderPreAuthorize") ) {
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
