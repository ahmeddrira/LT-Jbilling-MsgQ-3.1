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
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Stateful factory alarm class for ProcessorEmailAlarmTask.
 * Decides if emails should be sent.
 * @author Lucas Pickstone
 */
public class ProcessorEmailAlarm {
    private static HashMap alarms = new HashMap();

    private int failedCounter;
    private Queue times;          // holds queue of failure times
    private long lastEmailTime;   // last time email was sent

    private Logger log = Logger.getLogger(ProcessorEmailAlarm.class);

    // Constructor
    public ProcessorEmailAlarm() {
        failedCounter = 0;
        times = new LinkedList();
        lastEmailTime = 0;
    }

    // Factory method to get existing alarms or create a new one
    // for each processor and entityid pair.
    public static ProcessorEmailAlarm getAlarm(String processorName, 
                                               Integer entityId) {
        ProcessorEmailAlarm alarm 
                = (ProcessorEmailAlarm) alarms.get(processorName + entityId);
        if (alarm == null) {
            alarm = new ProcessorEmailAlarm();
            alarms.put(processorName + entityId, alarm);
        }
        return alarm;
    }

    // Returns true if email should be sent
    public boolean fail(int failedLimit, int failedTime, 
                        int timeBetAlarms) {
        failedTime *= 1000; // convert seconds to milliseconds
        failedCounter++;
        long currentTime = (new Date()).getTime();

        // add time to queue
        times.offer(new Long(currentTime));
        // remove any old times no longer needed
        if (times.size() > failedLimit) {
             times.remove();
        }

        // If enough fails counted, check that they occurred within 
        // a period of time specified by failedTime.
        if (failedCounter >= failedLimit) {
            long earliestTime = ((Long) times.peek()).longValue();
            if (currentTime - earliestTime <= failedTime 
                    && canSendEmail(timeBetAlarms)) {
                lastEmailTime = currentTime;
                return true;
            }
        }
        return false;
    }

    // Returns true if email should be sent
    public boolean unavailable(int timeBetAlarms) {
        if (canSendEmail(timeBetAlarms)) {
            lastEmailTime = (new Date()).getTime();
            return true;
        } else {
            return false;
        }
    }

    public void successful() {
        if (failedCounter != 0) {
            failedCounter = 0;
            times = new LinkedList();
        }
    }

    public int getFailedCounter() {
        return failedCounter;
    }

    // Returns true if enought time has elapsed for next alarm.
    private boolean canSendEmail(int timeBetAlarms) {
        long currentTime = (new Date()).getTime();
        timeBetAlarms *= 1000;
        return (currentTime - lastEmailTime > timeBetAlarms);
    }
}