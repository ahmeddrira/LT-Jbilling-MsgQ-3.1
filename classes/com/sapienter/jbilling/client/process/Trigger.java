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
import com.sapienter.jbilling.server.mediation.MediationSession;
import com.sapienter.jbilling.server.mediation.MediationSessionHome;

/**
 * @author Emil
 *
 */
public class Trigger implements Job{
	
    private static final Logger LOG = Logger.getLogger(Trigger.class);
    
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
    		LOG.info("No schedule information found.");
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
    			LOG.debug(e);
				LOG.info("Error:" + e.getMessage() + " Schedule does not start.");
				
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
    				LOG.info("The frequency can not be zero when time is specified.");
    				return;
    			}
				startTime = df.parse(time);
			} catch (ParseException e) {
				LOG.debug(e);
				LOG.info("Error:" + e.getMessage() + " Schedule does not start.");
				// Leave
				return;
			} catch(NumberFormatException e) {
				LOG.debug(e);
				LOG.info("Error:" + e.getMessage() + " Schedule does not start.");
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
			LOG.debug(e);
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
            MediationSessionHome mediationHome =
                (MediationSessionHome) JNDILookup.getFactory(true).lookUpHome(
                        MediationSessionHome.class,
                        MediationSessionHome.JNDI_NAME);
            MediationSession remoteMediation = mediationHome.create();

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
            if (Util.getSysPropBooleanTrue("process.run_billing")) {
    			LOG.info("Running trigger for " + today);
    			LOG.info("Starting billing process at " + 
    					Calendar.getInstance().getTime());
    			remoteBillingProcess.trigger(today);
    			LOG.info("Ended billing process at " + 
    					Calendar.getInstance().getTime());
            }

			// now the ageing process
			if (firstOfToday) {
                if (Util.getSysPropBooleanTrue("process.run_ageing")) {
    				LOG.info("Starting ageing process at " + 
    						Calendar.getInstance().getTime());
    				remoteBillingProcess.reviewUsersStatus(today);
    				LOG.info("Ended ageing process at " + 
    						Calendar.getInstance().getTime());
                }

                if (Util.getSysPropBooleanTrue("process.run_partner")) {
                    // now the partner payout process
                    LOG.info("Starting partner process at "
                            + Calendar.getInstance().getTime());
                    remoteUser.processPayouts(today);
                    LOG.info("Ended partner process at "
                            + Calendar.getInstance().getTime());
                }		
                
                if (Util.getSysPropBooleanTrue("process.run_order_expire")) {
                    // finally the orders about to expire notification
                    LOG.info("Starting order notification at "
                            + Calendar.getInstance().getTime());
                    remoteOrder.reviewNotifications(today);
                    LOG.info("Ended order notification at "
                            + Calendar.getInstance().getTime());
                }                
                if (Util.getSysPropBooleanTrue("process.run_invoice_reminder")) {
                    // the invoice reminders
                    LOG.info("Starting invoice reminders at "
                            + Calendar.getInstance().getTime());
                    remoteInvoice.sendReminders(today);
                    LOG.info("Ended invoice reminders at "
                            + Calendar.getInstance().getTime());
                }                
                if (Util.getSysPropBooleanTrue("process.run_penalty")) {
                    // the invoice penalties
                    LOG.info("Starting invoice penalties at "
                            + Calendar.getInstance().getTime());
                    remoteInvoice.processOverdue(today);
                    LOG.info("Ended invoice penalties at "
                            + Calendar.getInstance().getTime());
                }                
                if (Util.getSysPropBooleanTrue("process.run_list")) {
                    // update the listing statistics
                    LOG.info("Starting list stats at "
                            + Calendar.getInstance().getTime());
                    remoteList.updateStatistics();
                    LOG.info("Ended list stats at "
                            + Calendar.getInstance().getTime());
                }                
                if (Util.getSysPropBooleanTrue("process.run_cc_expire")) {
                    // send credit card expiration emails
                    LOG.info("Starting credit card expiration at "
                            + Calendar.getInstance().getTime());
                    remoteUser.notifyCreditCardExpiration(today);
                    LOG.info("Ended credit card expiration at "
                            + Calendar.getInstance().getTime());
                }                
                if (Util.getSysPropBooleanTrue("process.run_mediation")) {
                    // send credit card expiration emails
                    LOG.info("Starting mediation at "
                            + Calendar.getInstance().getTime());
                    remoteMediation.trigger();
                    LOG.info("Ended mediation at "
                            + Calendar.getInstance().getTime());
                }                
			}

		} catch (RemoteException e) {
			LOG.debug(e);
		} catch (NamingException e) {
			LOG.debug(e);
		} catch (CreateException e) {
			LOG.debug(e);
		} catch (SessionInternalError e) {
			LOG.debug(e);
		}
		
	}
}
