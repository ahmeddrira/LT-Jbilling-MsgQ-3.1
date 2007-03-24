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
package com.sapienter.jbilling.server.user.tasks;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.process.ConfigurationBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.UserDTOEx;

public class BasicSubscriptionStatusManagerTask extends PluggableTask implements
        ISubscriptionStatusManager {
    
    private static final Logger LOG = Logger.getLogger(BasicSubscriptionStatusManagerTask.class);
    private PaymentDTOEx payment;
    private Integer entityId;
    
    public void paymentFailed(Integer entityId, PaymentDTOEx payment) {
        this.payment = payment;
        this.entityId = entityId;
        
        if (!isPaymentApplicable(true)) {
            LOG.debug("This payment can't be processed " + payment);
            return;
        }
        
        LOG.debug("A payment failed " + payment);
        
        UserBL user = getUser();
        Integer status = user.getEntity().getSubscriptionStatus().getId();

        if (isLastRetry()) {
            if (!status.equals(UserDTOEx.SUBSCRIBER_PENDING_EXPIRATION)) {
                LOG.warn("Changing subsc status to expired, but from " + status);
            }
            user.updateSubscriptionStatus(UserDTOEx.SUBSCRIBER_EXPIRED);
        } else {
            // not paying is not good
            if (status.equals(UserDTOEx.SUBSCRIBER_ACTIVE)) {
                user.updateSubscriptionStatus(UserDTOEx.SUBSCRIBER_PENDING_EXPIRATION);
            } else if (!status.equals(UserDTOEx.SUBSCRIBER_PENDING_EXPIRATION)) {
                LOG.warn("Not clear what to do with a customer is stauts " + status);
            }
        }
    }
    
    public void paymentSuccessful(Integer entityId, PaymentDTOEx payment) {
        this.payment = payment;
        this.entityId = entityId;
        
        if (!isPaymentApplicable(false)) {
            return;
        }

        UserBL user = getUser();

        // currently, any payment get's you to active, regardless of the amount.
        // hence, this is not supporting partial payments ... event a partial 
        // payment will take you to active.
        user.updateSubscriptionStatus(UserDTOEx.SUBSCRIBER_ACTIVE);
    }
    
    private boolean isPaymentApplicable(boolean failed) {
        if (payment != null && payment.getIsRefund() != null && 
                payment.getIsRefund().intValue() == 0) {
            if (failed) {
                if (payment.getAttempt() != null) {
                     return true;
                } else {
                    return false;
                }
            }
            return true;
        }
        
        return false;
    }
    
    private boolean isLastRetry() {
        ConfigurationBL config = null;
        try {
            config = new ConfigurationBL(entityId);
        } catch (Exception e) {
            throw new SessionInternalError("Processing payment to change status", 
                    BasicSubscriptionStatusManagerTask.class, e);
        }
        
        if (payment.getAttempt().intValue() >= 
                config.getEntity().getRetries().intValue()) {
            return true;
        } else {
            return false;
        }
    }
    
    private UserBL getUser() {
        // find the user, and its status
        UserBL user = null; 
        try {
            user = new UserBL(payment.getUserId());
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }        
        return user;
    }
}
