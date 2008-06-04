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

package com.sapienter.jbilling.server.payment;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.PaymentAuthorizationEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentAuthorizationEntityLocalHome;
import com.sapienter.jbilling.interfaces.PaymentEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentEntityLocalHome;
import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;

public class PaymentAuthorizationBL {
    private JNDILookup EJBFactory = null;
    private PaymentAuthorizationEntityLocalHome paymentAuthorizationHome = null;
    private PaymentAuthorizationEntityLocal paymentAuthorization = null;
    private static final Logger LOG = Logger.getLogger(PaymentAuthorizationBL.class); 

    public PaymentAuthorizationBL(Integer paymentAuthorizationId) 
            throws NamingException, FinderException {
        init();
        set(paymentAuthorizationId);
    }
    
    public PaymentAuthorizationBL(PaymentAuthorizationEntityLocal entity) 
            throws NamingException {
        init();
        paymentAuthorization = entity;
    }

    public PaymentAuthorizationBL() throws NamingException {
        init();
    }

    private void init() throws NamingException {
        EJBFactory = JNDILookup.getFactory(false);
        paymentAuthorizationHome = (PaymentAuthorizationEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                PaymentAuthorizationEntityLocalHome.class,
                PaymentAuthorizationEntityLocalHome.JNDI_NAME);

    }

    public PaymentAuthorizationEntityLocal getEntity() {
        return paymentAuthorization;
    }
    
    public void set(Integer id) throws FinderException {
        paymentAuthorization = paymentAuthorizationHome.findByPrimaryKey(id);
    }
    
    public void create(PaymentAuthorizationDTO dto, Integer paymentId) 
            throws CreateException {
        // create the record, there's no need for an event to be logged 
        // since the timestamp and the user are already in the paymentAuthorization row
        paymentAuthorization = paymentAuthorizationHome.create(
                dto.getProcessor(), dto.getCode1());
            
        paymentAuthorization.setApprovalCode(dto.getApprovalCode());
        paymentAuthorization.setAVS(dto.getAVS());
        paymentAuthorization.setCardCode(dto.getCardCode());
        paymentAuthorization.setCode2(dto.getCode2());
        paymentAuthorization.setCode3(dto.getCode3());
        paymentAuthorization.setMD5(dto.getMD5());
        paymentAuthorization.setTransactionId(dto.getTransactionId());
        paymentAuthorization.setResponseMessage(Util.truncateString(dto.getResponseMessage(),200));
        
        // all authorization have to be linked to a payment
        try {
            PaymentBL payment = new PaymentBL(paymentId);
            paymentAuthorization.setPayment(payment.getEntity());
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    public PaymentAuthorizationDTO getDTO() 
            throws FinderException {
        PaymentAuthorizationDTO dto = new PaymentAuthorizationDTO();
        dto.setApprovalCode(paymentAuthorization.getApprovalCode());
        dto.setAVS(paymentAuthorization.getAVS());
        dto.setCardCode(paymentAuthorization.getCardCode());
        dto.setCode1(paymentAuthorization.getCode1());
        dto.setCode2(paymentAuthorization.getCode2());
        dto.setCode3(paymentAuthorization.getCode3());
        dto.setMD5(paymentAuthorization.getMD5());
        dto.setId(paymentAuthorization.getId());
        dto.setProcessor(paymentAuthorization.getProcessor());        
        dto.setTransactionId(paymentAuthorization.getTransactionId());
        dto.setCreateDate(paymentAuthorization.getCreateDate());
        dto.setResponseMessage(paymentAuthorization.getResponseMessage());
        return dto;
    }
        
    public PaymentAuthorizationDTO getPreAuthorization(Integer userId) {
        PaymentAuthorizationDTO auth = null;
        try {
            EJBFactory = JNDILookup.getFactory(false);
            PaymentEntityLocalHome paymentHome = (PaymentEntityLocalHome) 
                    EJBFactory.lookUpLocalHome(
                    PaymentEntityLocalHome.class,
                    PaymentEntityLocalHome.JNDI_NAME);

            Collection payments = paymentHome.findPreauth(userId);
            // at the time, use the very first one
            if (!payments.isEmpty()) {
                PaymentEntityLocal payment = (PaymentEntityLocal) payments.toArray()[0];
                Collection auths = payment.getAuthorizations();
                if (!auths.isEmpty()) {
                    paymentAuthorization = 
                            (PaymentAuthorizationEntityLocal) auths.toArray()[0];
                    auth = getDTO();
                } else {
                    LOG.warn("Auth payment found, but without auth record?");
                }
            }
        } catch (Exception e) {
            LOG.warn("Exceptions finding a pre authorization", e);
        }
        LOG.debug("Looking for preauth for " + userId + " result " + auth);
        return auth;
    }

    public void markAsUsed(PaymentDTOEx user) {
        paymentAuthorization.getPayment().setBalance(0F);
        // this authorization got used by a real payment. Link them
        try {
            PaymentBL payment = new PaymentBL(user.getId());
            paymentAuthorization.getPayment().setPayment(payment.getEntity());
        } catch (Exception e) {
            throw new SessionInternalError("linking authorization to user payment",
                    PaymentAuthorizationBL.class, e);
        } 
    }
}
