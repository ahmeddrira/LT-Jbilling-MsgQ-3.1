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

Contributor(s): Lucas Pickstone_______________________.
*/

package com.sapienter.jbilling.server.pluggableTask;

import java.util.Date;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;

/**
 * Sends an email when a payment processor is down.
 * @author Lucas Pickstone
 */
public class ProcessorEmailAlarmTask extends PluggableTask
            implements ProcessorAlarm {

    // pluggable task parameters names
    public static final String PARAMETER_FAILED_LIMIT = "failed_limit";
    public static final String PARAMETER_FAILED_TIME = "failed_time";
    public static final String PARAMETER_TIME_BETWEEN_ALARMS 
            = "time_between_alarms";

    // optional parameter
    public static final String PARAMETER_EMAIL_ADDRESS = "email_address";

    private String processorName;
    private Integer entityId;
    private ProcessorEmailAlarm alarm;
    
    private int failedLimit;
    private int failedTime;
    private int timeBetweenAlarms;

    private Logger log = Logger.getLogger(ProcessorEmailAlarmTask.class);
    
    @Override
    public void initializeParamters(PluggableTaskDTO task) throws PluggableTaskException {
    	super.initializeParamters(task);
    	failedLimit = parseInt(parameters.get(PARAMETER_FAILED_LIMIT));
    	failedTime = parseInt(parameters.get(PARAMETER_FAILED_TIME));
    	failedTime = parseInt(parameters.get(PARAMETER_TIME_BETWEEN_ALARMS));
    }

    // Initialisation
    public void init(String processorName, Integer entityId) {
        this.processorName = processorName;
        this.entityId = entityId;
        alarm = ProcessorEmailAlarm.getAlarm(processorName, entityId);
    }

    // Payment processed, but failed/declined.
    public void fail() {
        if (alarm.fail(failedLimit, failedTime, timeBetweenAlarms)) {
            String params[] = new String[4];
            params[0] = processorName;
            params[1] = entityId.toString();
            params[2] = "" + alarm.getFailedCounter();
            params[3] = (new Date()).toString();
            sendEmail("processorAlarm.fail", params);
        }
    }

    // Processor was unavailable.
    public void unavailable() {
        if (alarm.unavailable(timeBetweenAlarms)) {
            String params[] = new String[3];
            params[0] = processorName;
            params[1] = entityId.toString();
            params[2] = (new Date()).toString();
            sendEmail("processorAlarm.unavailable", params);
        }
    }

    // Payment processed and successful.
    public void successful() {
        alarm.successful();
    }

    // Sends email with given messageKey and params.
    private void sendEmail(String messageKey, String[] params) {
        log.debug("Sending alarm email.");

        String address = (String) parameters.get(PARAMETER_EMAIL_ADDRESS);

        try {
            // if email address supplied as parameter, use it,
            if (address != null) {
                NotificationBL.sendSapienterEmail(address, entityId, 
                        messageKey, null, params);
            } 
            // otherwise use the entityId's default address.
            else {
                NotificationBL.sendSapienterEmail(entityId, messageKey, 
                        null, params);
            }
        } catch (Exception e) {
            log.error("Couldn't send email.", e);
        }
    }
    
    private int parseInt(Object object) throws PluggableTaskException {
    	if (object instanceof Number){
    		return ((Number)object).intValue();
    	}
    	if (object instanceof String){
    		try {
    			return Integer.parseInt((String)object);
    		} catch (NumberFormatException e){
    			//fall through
    		}
    	}
    	throw new PluggableTaskException("Number expected: " + object);
    }
}
