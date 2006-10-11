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
package com.sapienter.jbilling.client.process;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;

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
public class Trigger implements Job{
	
    private static Logger log = null;
    
    /**
     * Initialize tool Trigger. Load properties from jbilling.properties and set up Quartz job/trigger
     * 
     *  process.time=YYYYMMDD-HHmm (a full date followed by HH is the hours in 24hs format and mm the minutes).
     *  process.frequency=X (where X is an integer >= 0 and < 1440
     *  
     *  The fist property indicates at what time of the day the trigger has to happen for the very first time. After this first 
     *  run you will need X minutes (specified by 'process.frequency') to run the trigger again. Since only the billing process 
     *  can run more than once per day, we need some logic in the ejbTimout method to verify if the call is the first one of the 
     *  day (where it runs all the services) or not (where it runs only the billing process).
     *  
     *  The first property is optional. If it is not present, or its value is null, then the next trigger will happen at 
     *  startup + minutes indicated in 'process.frequency'.
     *
     */
    public static void Initialize() {
    	log = Logger.getLogger(Trigger.class);
    	
    	// Load properties from jbilling.properties
    	String time = null;
    	String frequency = null;
    	
    	try {
    		time = Util.getSysProp("process.time");
    		frequency = Util.getSysProp("process.frequency");
    	} catch (Exception e){
    		// just eat it
    	}
    	
    	// both null or empty, log one message and return
    	if( (time == null || time.length() == 0) && (frequency==null || frequency.length() == 0) )
    	{
    		log.info("No schedule information found.");
    		return;
    	}
    	
    	Date startTime = null;
    	int interval = 0;
    	
    	// if process.time absents and frequency is zero, fire every 0:00AM
    	GregorianCalendar cal = new GregorianCalendar();
    	if( ( time == null || time.length() == 0 ) && "0".equals(frequency)) {
    		cal.add(Calendar.DATE, 1);
    		startTime = Util.truncateDate(cal.getTime());
    		interval = 24 * 60;
    	} else if(time == null || time.length() == 0) { // process.time is not present, start at startup + frequency
    		try {
    			interval = Integer.parseInt(frequency);
    		} catch(NumberFormatException e) {
    			log.debug(e);
				log.info("Error:" + e.getMessage() + " Schedule does not start.");
				
				// Leave
				return;
    		}
    		cal.add(Calendar.MINUTE, interval);
    		startTime = Util.truncateDate(cal.getTime());
    	} else { // Its normal one
    		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmm");
    		try {
    			interval = Integer.parseInt(frequency);
    			if(interval == 0) {
    				log.info("The frequency can not be zero when time is specified.");
    				return;
    			}
				startTime = df.parse(time);
			} catch (ParseException e) {
				log.debug(e);
				log.info("Error:" + e.getMessage() + " Schedule does not start.");
				// Leave
				return;
			} catch(NumberFormatException e) {
				log.debug(e);
				log.info("Error:" + e.getMessage() + " Schedule does not start.");
				//Leave
				return;
			}			
    	}
    	
    	// startup trigger
    	try {
    		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			Scheduler sched = schedFact.getScheduler();
			JobDetail jbillingJob = new JobDetail("jbilling", Scheduler.DEFAULT_GROUP, Trigger.class);
			
			SimpleTrigger trigger = new SimpleTrigger("jbillingTrigger", 
					Scheduler.DEFAULT_GROUP, 
					startTime,
					null,
					SimpleTrigger.REPEAT_INDEFINITELY,
					interval * 60 * 1000);
			
			trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT);
			
			sched.scheduleJob(jbillingJob, trigger);
			
			sched.start();
		} catch (SchedulerException e) {
			log.debug(e);
		}
    }
    
	public static void main(String[] args) {
 
	}

	public void execute(JobExecutionContext ctx) throws JobExecutionException {
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
			today = Util.truncateDate(today);

			// Determine if this call first time of today
			boolean firstOfToday = true;
			Date lastFire = ctx.getPreviousFireTime();
			
			if(lastFire == null) { // Should be first time call
				firstOfToday = true;
			} else {
				lastFire = Util.truncateDate(lastFire);
				firstOfToday = lastFire.before(today);
			}

			// run the billing process
			log.info("Running trigger for " + today);
			log.info("Starting billing process at " + 
					Calendar.getInstance().getTime());
			remoteBillingProcess.trigger(today);
			log.info("Ended billing process at " + 
					Calendar.getInstance().getTime());

			// now the ageing process
			if (firstOfToday) {
				log.info("Starting ageing process at " + 
						Calendar.getInstance().getTime());
				remoteBillingProcess.reviewUsersStatus(today);
				log.info("Ended ageing process at " + 
						Calendar.getInstance().getTime());


				// now the partner payout process
				log.info("Starting partner process at " + 
						Calendar.getInstance().getTime());
				remoteUser.processPayouts(today);
				log.info("Ended partner process at " + 
						Calendar.getInstance().getTime());


				// finally the orders about to expire notification
				log.info("Starting order notification at " + 
						Calendar.getInstance().getTime());
				remoteOrder.reviewNotifications(today);
				log.info("Ended order notification at " + 
						Calendar.getInstance().getTime());


				// the invoice reminders
				log.info("Starting invoice reminders at " + 
						Calendar.getInstance().getTime());
				remoteInvoice.sendReminders(today);
				log.info("Ended invoice reminders at " + 
						Calendar.getInstance().getTime());


				// the invoice penalties
				log.info("Starting invoice penalties at " + 
						Calendar.getInstance().getTime());
				remoteInvoice.processOverdue(today);
				log.info("Ended invoice penalties at " + 
						Calendar.getInstance().getTime());

				// update the listing statistics
				log.info("Starting list stats at " + 
						Calendar.getInstance().getTime());
				remoteList.updateStatistics();
				log.info("Ended list stats at " + 
						Calendar.getInstance().getTime());

				// send credit card expiration emails
				log.info("Starting credit card expiration at " + 
						Calendar.getInstance().getTime());
				remoteUser.notifyCreditCardExpiration(today);
				log.info("Ended credit card expiration at " + 
						Calendar.getInstance().getTime());
			}

		} catch (RemoteException e) {
			log.debug(e);
		} catch (NamingException e) {
			log.debug(e);
		} catch (CreateException e) {
			log.debug(e);
		} catch (SessionInternalError e) {
			log.debug(e);
		}
		
	}
}
