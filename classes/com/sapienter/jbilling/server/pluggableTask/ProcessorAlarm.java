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

/**
 * Alarm for notification of payment processor fail/unavailable
 * payment results.
 * @author Lucas Pickstone
 */
public interface ProcessorAlarm {
    /**
     * Initialize before fail, unavailable or successful is called.
     * @param processorName The payment processor used.
     * @param entityId The entity (company) id of the payment.
     */
    public void init(String processorName, Integer entityId);

    /**
     * Payment processed, but failed/declined.
     */
    public void fail();

    /**
     * Processor was unavailable.
     */
    public void unavailable();

    /**
     * Payment processed and successful.
     */
    public void successful();
}
