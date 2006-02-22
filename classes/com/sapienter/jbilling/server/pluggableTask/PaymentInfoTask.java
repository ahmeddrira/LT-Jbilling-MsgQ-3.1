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

package com.sapienter.jbilling.server.pluggableTask;

import com.sapienter.jbilling.server.payment.PaymentDTOEx;

/*
 * This task gathers the information necessary to process a payment.
 * Since each customer and entity can have different payment methods
 * this is better placed in a pluggable task.
 * The result of the process call is the payment dto with all the info
 * to later send the payment to the live processor. The methos of the
 * payment has to be also set
 */
public interface PaymentInfoTask {
    
    PaymentDTOEx getPaymentInfo(Integer userId) throws TaskException;
    
}
