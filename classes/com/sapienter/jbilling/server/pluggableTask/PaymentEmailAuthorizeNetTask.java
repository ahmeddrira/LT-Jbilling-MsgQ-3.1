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
 * Created on Apr 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sapienter.jbilling.server.pluggableTask;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @author Emil
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PaymentEmailAuthorizeNetTask extends PaymentAuthorizeNetTask {
    public boolean process(PaymentDTOEx paymentInfo) 
            throws PluggableTaskException {
        Logger log = Logger.getLogger(PaymentEmailAuthorizeNetTask.class);
        boolean retValue = super.process(paymentInfo);
        String address = (String) parameters.get("email_address");
        try {
            UserBL user = new UserBL(paymentInfo.getUserId());
            String message;
            if (paymentInfo.getResultId().equals(Constants.RESULT_OK)) {
                message = "payment.success";
            } else {
                message = "payment.fail";
            }
            String params[] = new String[6];
            params[0] = paymentInfo.getUserId().toString();
            params[1] = user.getEntity().getUserName();
            params[2] = paymentInfo.getId().toString();
            params[3] = paymentInfo.getAmount().toString();
            if (paymentInfo.getAuthorization() != null) {
                params[4] = paymentInfo.getAuthorization().getTransactionId();
                params[5] = paymentInfo.getAuthorization().getApprovalCode();
            } else {
                params[4] = "Not available";
                params[5] = "Not available";
            }
            log.debug("Bkp 6 " + params[0] + " " + params[1] + " " + params[2] + " " + params[3] + " " + params[4] + " " + params[5] + " ");
            NotificationBL.sendSapienterEmail(address, 
                    user.getEntity().getEntity().getId(), message, null, 
                    params);
        } catch (Exception e) {
            
            log.warn("Cant send receit email");
        }
        
        return retValue;
    }
}
