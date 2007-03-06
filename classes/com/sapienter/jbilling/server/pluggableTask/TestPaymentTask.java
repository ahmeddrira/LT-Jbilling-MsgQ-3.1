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
 * Created on 12-May-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.pluggableTask;

import java.util.Calendar;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;

/**
 * This is only a test so the framwork has a task of this type.
 * @author Emil
 */
public class TestPaymentTask extends PluggableTask implements PaymentTask {

    /* (non-Javadoc)
     * @see com.sapienter.jbilling.server.pluggableTask.PaymentTask#process(com.sapienter.betty.server.payment.PaymentDTOEx)
     */
    public boolean process(PaymentDTOEx paymentInfo) {
        boolean retValue = false;
        
        paymentInfo.setMethodId(new Integer(1));
        
        if (paymentInfo.getCreditCard().getNumber().charAt(0) == '4') {
            paymentInfo.setResultId(Constants.RESULT_OK);
        } else {
            paymentInfo.setResultId(Constants.RESULT_FAIL);
        }
        
        return retValue;
    }

    public void failure(Integer userId, Integer retries) {
        // not doing anything right now
    }
    
    /**
     * returns OK for visa only
     */
    public PaymentAuthorizationDTOEx preAuth(CreditCardDTO cc, Float amount, Integer currencyId)
            throws PluggableTaskException {
        PaymentAuthorizationDTO dto = new PaymentAuthorizationDTO();
        dto.setCreateDate(Calendar.getInstance().getTime());
        dto.setApprovalCode("myApproval");
        dto.setCode1("super code 1");
        PaymentAuthorizationDTOEx retValue = new PaymentAuthorizationDTOEx(dto);
        retValue.setResult(cc.getNumber().charAt(0) == '4');
        
        return retValue;
      }
    
    public PaymentAuthorizationDTOEx confirmPreAuth(PaymentAuthorizationDTOEx auth, PaymentDTOEx paymentInfo) throws PluggableTaskException {
        // TODO Auto-generated method stub
        return null;
    }

}
