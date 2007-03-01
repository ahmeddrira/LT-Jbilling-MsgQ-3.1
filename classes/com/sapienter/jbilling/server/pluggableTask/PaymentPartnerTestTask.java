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
 * Created on Apr 6, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.pluggableTask;

import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @author Emil
 */
public class PaymentPartnerTestTask
    extends PluggableTask
    implements PaymentTask {

    /* (non-Javadoc)
     * @see com.sapienter.jbilling.server.pluggableTask.PaymentTask#process(com.sapienter.betty.server.payment.PaymentDTOEx)
     */
    public boolean process(PaymentDTOEx paymentInfo)
            throws PluggableTaskException {
        if (paymentInfo.getPayoutId() == null) {
            return true;
        }
        paymentInfo.setResultId(Constants.RESULT_OK);
        return false;
    }

    /* (non-Javadoc)
     * @see com.sapienter.jbilling.server.pluggableTask.PaymentTask#failure(java.lang.Integer, java.lang.Integer)
     */
    public void failure(Integer userId, Integer retry) {
        // TODO Auto-generated method stub

    }
    
    public PaymentAuthorizationDTOEx preAuth(CreditCardDTO cc, Float amount, Integer currencyId) 
        throws PluggableTaskException {
        return null;

    }


}
