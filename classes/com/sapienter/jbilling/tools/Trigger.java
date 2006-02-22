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
import com.sapienter.jbilling.server.list.ListSession;
import com.sapienter.jbilling.server.list.ListSessionHome;

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
            ListSessionHome listHome =
                (ListSessionHome) JNDILookup.getFactory(true).lookUpHome(
                ListSessionHome.class,
                ListSessionHome.JNDI_NAME);
            ListSession remoteList = listHome.create();
			
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
