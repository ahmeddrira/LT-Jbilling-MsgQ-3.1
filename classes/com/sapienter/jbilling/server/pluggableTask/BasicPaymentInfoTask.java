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
 * Created on Oct 3, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.pluggableTask;

import java.util.Iterator;

import com.sapienter.jbilling.interfaces.AchEntityLocal;
import com.sapienter.jbilling.interfaces.CreditCardEntityLocal;
import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.user.CreditCardBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;

/**
 * This creates payment dto. It now only goes and fetches the credit card
 * of the given user. It doesn't need to initialize the rest of the payment
 * information (amount, etc), only the info for the payment processor, 
 * usually cc info but it could be electronic cheque, etc...
 * This task should consider that the user is a partner and is being paid
 * (like a refund) and therefore fetch some other information, as getting 
 * paid with a cc seems not to be the norm.
 * @author Emil
 */
public class BasicPaymentInfoTask 
        extends PluggableTask implements PaymentInfoTask {

    /** 
     * This will return an empty payment dto with only the credit card/ach set
     * if a valid credit card is found for the user. Otherwise null.
     * It will check the customer's preference for the automatic payment type.
     */
    public PaymentDTOEx getPaymentInfo(Integer userId) 
            throws TaskException {
        PaymentDTOEx retValue = null;
        try {
        	Integer method = Constants.AUTO_PAYMENT_TYPE_CC; // def to cc
            UserBL userBL = new UserBL(userId);
            CreditCardBL ccBL = new CreditCardBL();
            if (userBL.getEntity().getCustomer() != null) {
            	// now non-customers only use credit cards
            	method = userBL.getEntity().getCustomer().getAutoPaymentType();
            	if (method == null) { 
            		method = Constants.AUTO_PAYMENT_TYPE_CC;
            	}
            }
            
            if (method.equals(Constants.AUTO_PAYMENT_TYPE_CC)) {
	            if (userBL.getEntity().getCreditCard().isEmpty()) {
	                // no credit cards entered! no payment ...
	            } else {
	                // go around the provided cards and get one that is sendable
	                // to the processor
	                for (Iterator it = userBL.getEntity().getCreditCard().
	                        iterator(); it.hasNext(); ) {
	                    ccBL.set((CreditCardEntityLocal) it.next());
	                    if (ccBL.validate()) {
	                        retValue = new PaymentDTOEx();
	                        retValue.setCreditCard(ccBL.getDTO());
	                        retValue.setMethodId(ccBL.getPaymentMethod());
	                        break;
	                    }
	                }
	            }
            } else if (method.equals(Constants.AUTO_PAYMENT_TYPE_ACH)) {
            	AchEntityLocal ach = userBL.getEntity().getAch();
            	if (ach == null) {
            		// no info, no payment
            	} else {
            		retValue = new PaymentDTOEx();
            		retValue.setAch(new AchDTO(null, ach.getAbaRouting(),
            				ach.getBankAccount(), ach.getAccountType(),
							ach.getBankName(), ach.getAccountName()));
            		retValue.setMethodId(Constants.PAYMENT_METHOD_ACH);
            	}
            }
        } catch (Exception e) {
            throw new TaskException(e);
        }
        return retValue;
    }

}
