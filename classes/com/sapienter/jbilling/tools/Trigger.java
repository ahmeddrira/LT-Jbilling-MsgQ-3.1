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
 * Created on Jul 14, 2004
 *
 */
package com.sapienter.jbilling.tools;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.BillingProcessSession;
import com.sapienter.jbilling.interfaces.BillingProcessSessionHome;
import com.sapienter.jbilling.interfaces.InvoiceSession;
import com.sapienter.jbilling.interfaces.InvoiceSessionHome;
import com.sapienter.jbilling.interfaces.OrderSession;
import com.sapienter.jbilling.interfaces.OrderSessionHome;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.list.ListSessionBean;
import com.sapienter.jbilling.server.util.Context;

/**
 * @author Emil
 *
 */
public class Trigger {
	
    
    
	public static void main(String[] args) {
		BillingProcessSession remoteBillingProcess = null;
		
		
        try {
        	// get a session for the remote interfaces
			BillingProcessSessionHome billingProcessHome =
			    (BillingProcessSessionHome) JNDILookup.getFactory(true).lookUpHome(
			    BillingProcessSessionHome.class,
			    BillingProcessSessionHome.JNDI_NAME);
			remoteBillingProcess = billingProcessHome.create();
            UserSessionHome userHome =
                (UserSessionHome) JNDILookup.getFactory(true).lookUpHome(
                UserSessionHome.class,
                UserSessionHome.JNDI_NAME);
            UserSession remoteUser = userHome.create();
            OrderSessionHome orderHome =
                (OrderSessionHome) JNDILookup.getFactory(true).lookUpHome(
                OrderSessionHome.class,
                OrderSessionHome.JNDI_NAME);
            OrderSession remoteOrder = orderHome.create();
            InvoiceSessionHome invoiceHome =
                (InvoiceSessionHome) JNDILookup.getFactory(true).lookUpHome(
                InvoiceSessionHome.class,
                InvoiceSessionHome.JNDI_NAME);
            InvoiceSession remoteInvoice = invoiceHome.create();
            ListSessionBean remoteList = (ListSessionBean) Context.getBean(
                    Context.Name.LIST_SESSION);
			
            // determine the date for this run
			Date today = Calendar.getInstance().getTime();
            Integer step = null; //means all
			if (args.length > 0) {
				today = Util.parseDate(args[0]);
                if (args.length >= 2) {
                    step = Integer.valueOf(args[1]);
                }
			}
			today = Util.truncateDate(today);
			
			// run the billing process
            if (step == null || step.intValue() == 1) {
    			System.out.println("Running trigger for " + today);
    			System.out.println("Starting billing process at " + 
    					Calendar.getInstance().getTime());
    			remoteBillingProcess.trigger(today);
    			System.out.println("Ended billing process at " + 
    					Calendar.getInstance().getTime());
            }
			
			// now the ageing process
            if (step == null || step.intValue() == 2) {
    			System.out.println("Starting ageing process at " + 
    					Calendar.getInstance().getTime());
    			remoteBillingProcess.reviewUsersStatus(today);
    			System.out.println("Ended ageing process at " + 
    					Calendar.getInstance().getTime());
            }

			// now the partner payout process
            if (step == null || step.intValue() == 3) {
    			System.out.println("Starting partner process at " + 
    					Calendar.getInstance().getTime());
    			remoteUser.processPayouts(today);
    			System.out.println("Ended partner process at " + 
    					Calendar.getInstance().getTime());
            }
			
			// finally the orders about to expire notification
            if (step == null || step.intValue() == 4) {
    			System.out.println("Starting order notification at " + 
    					Calendar.getInstance().getTime());
    			remoteOrder.reviewNotifications(today);
    			System.out.println("Ended order notification at " + 
    					Calendar.getInstance().getTime());
            }
			
            // the invoice reminders
            if (step == null || step.intValue() == 5) {
                System.out.println("Starting invoice reminders at " + 
                        Calendar.getInstance().getTime());
                remoteInvoice.sendReminders(today);
                System.out.println("Ended invoice reminders at " + 
                        Calendar.getInstance().getTime());
            }

            // the invoice penalties
            if (step == null || step.intValue() == 6) {
                System.out.println("Starting invoice penalties at " + 
                        Calendar.getInstance().getTime());
                remoteInvoice.processOverdue(today);
                System.out.println("Ended invoice penalties at " + 
                        Calendar.getInstance().getTime());
            }
            
            // update the listing statistics
            if (step == null || step.intValue() == 7) {
                System.out.println("Starting list stats at " + 
                        Calendar.getInstance().getTime());
                remoteList.updateStatistics();
                System.out.println("Ended list stats at " + 
                        Calendar.getInstance().getTime());
            }

            // send credit card expiration emails
            if (step == null || step.intValue() == 8) {
                System.out.println("Starting credit card expiration at " + 
                        Calendar.getInstance().getTime());
                remoteUser.notifyCreditCardExpiration(today);
                System.out.println("Ended credit card expiration at " + 
                        Calendar.getInstance().getTime());
            }

		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SessionInternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
