/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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
package com.sapienter.jbilling.server.user.tasks;

import java.util.Date;

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
    private static final String PARAMETER_ITEM_TYPE_ID = "item_type_id";
    
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
        
        UserBL user = getUser(null);
        Integer status = user.getEntity().getSubscriberStatus().getId();

        if (isLastRetry()) {
            if (status.equals(UserDTOEx.SUBSCRIBER_PENDING_EXPIRATION)) {
                user.updateSubscriptionStatus(UserDTOEx.SUBSCRIBER_EXPIRED);
            } else {
                LOG.warn("Last retry, but user not in pending expariation. Status = " + status);
            }
        } else {
            // not paying is not good
            if (status.equals(UserDTOEx.SUBSCRIBER_ACTIVE)) {
                user.updateSubscriptionStatus(UserDTOEx.SUBSCRIBER_PENDING_EXPIRATION);
            } else if (!status.equals(UserDTOEx.SUBSCRIBER_PENDING_EXPIRATION)) {
                LOG.warn("Not clear what to do with a customer in status " + status);
            }
        }
    }
    
    public void paymentSuccessful(Integer entityId, PaymentDTOEx payment) {
        this.payment = payment;
        this.entityId = entityId;
        
        if (!isPaymentApplicable(false)) {
            return;
        }

        UserBL user = getUser(null);

        // currently, any payment get's you to active, regardless of the amount.
        // hence, this is not supporting partial payments ... event a partial 
        // payment will take you to active.
        user.updateSubscriptionStatus(UserDTOEx.SUBSCRIBER_ACTIVE);
    }
    
    public void subscriptionEnds(Integer userId, Date newActiveUntil, 
            Date oldActiveUntil) {
        UserBL user = null;
        // it is known that both are different
        if (oldActiveUntil == null || (newActiveUntil != null && 
                newActiveUntil.after(oldActiveUntil))) {
            user = getUser(userId);
            if (user.getEntity().getSubscriberStatus().getId() ==
                    UserDTOEx.SUBSCRIBER_ACTIVE) {
                user.updateSubscriptionStatus(
                        UserDTOEx.SUBSCRIBER_PENDING_UNSUBSCRIPTION);
            } else {
                LOG.info("Should go to pending unsubscription, but is in " + 
                        user.getEntity().getSubscriberStatus().getDescription(1));
            }
        } else if (newActiveUntil == null) { // it's going back to on-going (subscribed)
            user = getUser(userId);
            if (user.getEntity().getSubscriberStatus().getId() ==
                    UserDTOEx.SUBSCRIBER_PENDING_UNSUBSCRIPTION) {
                user.updateSubscriptionStatus(
                        UserDTOEx.SUBSCRIBER_ACTIVE);
            } else {
                LOG.info("Should go to active, but is in " + 
                        user.getEntity().getSubscriberStatus().getDescription(1));
            }
        }
    }
    
    public void subscriptionEnds(Integer userId, Date date) {
        UserBL user = getUser(userId);
        if (!user.isCurrentlySubscribed(date)) {
            user.updateSubscriptionStatus(UserDTOEx.SUBSCRIBER_UNSUBSCRIBED);
        } 
    }
    
    private boolean isPaymentApplicable(boolean failed) {
        if (payment != null && payment.getIsRefund() == 0) {
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
        
        // it is the number of retries plus one for the initial process
        if (payment.getAttempt().intValue() >= 
                config.getEntity().getRetries().intValue() + 1) { // 
            return true;
        } else {
            return false;
        }
    }
    
    private UserBL getUser(Integer userId) {
        // find the user, and its status
        UserBL user = null; 
        try {
            if (userId == null) {
                user = new UserBL(payment.getUserId());
            } else {
                user = new UserBL(userId);
            }
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }        
        return user;
    }
}
