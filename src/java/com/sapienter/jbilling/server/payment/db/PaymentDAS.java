/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sapienter.jbilling.server.payment.db;

import java.util.*;

import com.sapienter.jbilling.common.CommonConstants;
import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.server.payment.PaymentBL;
import com.sapienter.jbilling.server.process.db.BillingProcessDAS;
import com.sapienter.jbilling.server.process.db.BillingProcessDTO;
import org.apache.tools.ant.types.resources.Restrict;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.util.db.AbstractDAS;
import com.sapienter.jbilling.server.util.db.CurrencyDAS;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;

import java.math.BigDecimal;

public class PaymentDAS extends AbstractDAS<PaymentDTO> {

    // used for the web services call to get the latest X
    public List<Integer> findIdsByUserLatestFirst(Integer userId, int maxResults) {
        Criteria criteria = getSession().createCriteria(PaymentDTO.class)
                .add(Restrictions.eq("deleted", 0))
                .createAlias("baseUser", "u")
                    .add(Restrictions.eq("u.id", userId))
                .setProjection(Projections.id()).addOrder(Order.desc("id"))
                .setMaxResults(maxResults);
        return criteria.list();
    }

    public PaymentDTO create(BigDecimal amount, PaymentMethodDTO paymentMethod,
            Integer userId, Integer attempt, PaymentResultDTO paymentResult,
            CurrencyDTO currency) {

        PaymentDTO payment = new PaymentDTO();
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setBaseUser(new UserDAS().find(userId));
        payment.setAttempt(attempt);
        payment.setPaymentResult(paymentResult);
        payment.setCurrency(new CurrencyDAS().find(currency.getId()));
        payment.setCreateDatetime(Calendar.getInstance().getTime());
        payment.setDeleted(new Integer(0));
        // todo dont know why it is setting isRefund to explicitly 0 ?
//        payment.setIsRefund(new Integer(0));
        payment.setIsPreauth(new Integer(0));

        return save(payment);

    }

    /**
     * * query="SELECT OBJECT(p) FROM payment p WHERE p.userId = ?1 AND
     * p.balance >= 0.01 AND p.isRefund = 0 AND p.isPreauth = 0 AND p.deleted =
     * 0"
     * 
     * @param userId
     * @return
     */
    public Collection findWithBalance(Integer userId) {

        UserDTO user = new UserDAS().find(userId);

        Criteria criteria = getSession().createCriteria(PaymentDTO.class);
        criteria.add(Restrictions.eq("baseUser", user));
        criteria.add(Restrictions.ge("balance", Constants.BIGDECIMAL_ONE_CENT));
        criteria.add(Restrictions.eq("isRefund", 0));
        criteria.add(Restrictions.eq("isPreauth", 0));
        criteria.add(Restrictions.eq("deleted", 0));

        return criteria.list();
    }

    public BigDecimal findTotalRevenueByUser(Integer userId) {
        Criteria criteria = getSession().createCriteria(PaymentDTO.class);
        criteria.add(Restrictions.eq("deleted", 0))
                .createAlias("baseUser", "u")
                    .add(Restrictions.eq("u.id", userId))
        		.createAlias("paymentResult", "pr")
        		.add(Restrictions.ne("pr.id", CommonConstants.PAYMENT_RESULT_FAILED));
        criteria.add(Restrictions.eq("isRefund", 0));
        criteria.setProjection(Projections.sum("amount"));
        criteria.setComment("PaymentDAS.findTotalRevenueByUser-Gross Receipts");

        BigDecimal grossReceipts= criteria.uniqueResult() == null ? BigDecimal.ZERO : (BigDecimal) criteria.uniqueResult();
        
        Criteria criteria2 = getSession().createCriteria(PaymentDTO.class);
        criteria2.add(Restrictions.eq("deleted", 0))
                .createAlias("baseUser", "u")
                    .add(Restrictions.eq("u.id", userId))
            		.createAlias("paymentResult", "pr")
            		.add(Restrictions.ne("pr.id", CommonConstants.PAYMENT_RESULT_FAILED));
        criteria2.add(Restrictions.eq("isRefund", 1));
        criteria2.setProjection(Projections.sum("amount"));
        criteria2.setComment("PaymentDAS.findTotalRevenueByUser-Gross Refunds");
        
        BigDecimal refunds= criteria2.uniqueResult() == null ? BigDecimal.ZERO : (BigDecimal) criteria2.uniqueResult();
        
        //net revenue = gross - all refunds
        return ( grossReceipts.subtract(refunds));
    }
    
    public BigDecimal findTotalBalanceByUser(Integer userId) {
        
        //user's payments which are not refunds
        Criteria criteria = getSession().createCriteria(PaymentDTO.class);
        criteria.add(Restrictions.eq("deleted", 0))
            .createAlias("baseUser", "u")
            .add(Restrictions.eq("u.id", userId))
            .add(Restrictions.eq("isRefund", 0));
        
        criteria.setProjection(Projections.sum("balance"));
        criteria.setComment("PaymentDAS.findTotalBalanceByUser");
        BigDecimal paymentBalances = (criteria.uniqueResult() == null ? BigDecimal.ZERO : (BigDecimal) criteria.uniqueResult());

        //calculate refunds
        Criteria criteria2 = getSession().createCriteria(PaymentDTO.class);
        criteria2.add(Restrictions.eq("deleted", 0))
                .createAlias("baseUser", "u")
                    .add(Restrictions.eq("u.id", userId))
                    .add(Restrictions.eq("isRefund", 1));
        
        criteria2.setProjection(Projections.sum("balance"));
        criteria2.setComment("PaymentDAS.findTotalBalanceByUser-less_Refunds");
        BigDecimal refunds= criteria2.uniqueResult() == null ? BigDecimal.ZERO : (BigDecimal) criteria2.uniqueResult();

        return paymentBalances.subtract(refunds);
    }
    

    /**
     * 
     * query="SELECT OBJECT(p) FROM payment p WHERE 
     * p.userId = ?1 AND 
     * p.balance >= 0.01 AND 
     * p.isRefund = 0 AND 
     * p.isPreauth = 1 AND 
     * p.deleted = 0"
     * 
     * @param userId
     * @return
     */
    public Collection<PaymentDTO> findPreauth(Integer userId) {
        
        UserDTO user = new UserDAS().find(userId);

        Criteria criteria = getSession().createCriteria(PaymentDTO.class);
        criteria.add(Restrictions.eq("baseUser", user));
        criteria.add(Restrictions.ge("balance", Constants.BIGDECIMAL_ONE_CENT));
        criteria.add(Restrictions.eq("isRefund", 0));
        criteria.add(Restrictions.eq("isPreauth", 1));
        criteria.add(Restrictions.eq("deleted", 0));

        return criteria.list();

    }

    private static final String BILLING_PROCESS_GENERATED_PAYMENTS_HQL =
            "select payment "
            + " from PaymentDTO payment "
            + " join payment.invoicesMap as invoiceMap "
            + " where invoiceMap.invoiceEntity.billingProcess.id = :billing_process_id "
            + " and payment.deleted = 0 "
            + " and payment.createDatetime >= :start "
            + " and payment.createDatetime <= :end";

    /**
     * Returns a list of all payments that were made to invoices generated by
     * the billing process between the processes start & end times.
     *
     * Payments represent the amount automatically paid by the billing process.
     *
     * @param processId billing process id
     * @param start process run start date
     * @param end process run end date
     * @return list of payments generated by the billing process.
     */
    @SuppressWarnings("unchecked")
    public List<PaymentDTO> findBillingProcessGeneratedPayments(Integer processId, Date start, Date end) {
        Query query = getSession().createQuery(BILLING_PROCESS_GENERATED_PAYMENTS_HQL);
        query.setParameter("billing_process_id", processId);
        query.setParameter("start", start);
        query.setParameter("end", end);

        return query.list();
    }

    private static final String BILLING_PROCESS_PAYMENTS_HQL =
            "select payment "
            + " from PaymentDTO payment "
            + " join payment.invoicesMap as invoiceMap "
            + " where invoiceMap.invoiceEntity.billingProcess.id = :billing_process_id "
            + " and payment.deleted = 0 "
            + " and payment.createDatetime > :end";

    /**
     * Returns a list of all payments that were made to invoices generated by
     * the billing process, after the billing process run had ended.
     *
     * Payments made to generated invoices after the process has finished are still
     * relevant to the process as it shows how much of the balance was paid by
     * users (or paid in a retry process) for this billing period.
     *
     * @param processId billing process id
     * @param end process run end date
     * @return list of payments applied to the billing processes invoices.
     */
    @SuppressWarnings("unchecked")
    public List<PaymentDTO> findBillingProcessPayments(Integer processId, Date end) {
        Query query = getSession().createQuery(BILLING_PROCESS_PAYMENTS_HQL);
        query.setParameter("billing_process_id", processId);
        query.setParameter("end", end);

        return query.list();
    }

    public List<PaymentDTO> findAllPaymentByBaseUserAndIsRefund(Integer userId, Integer isRefund) {

        UserDTO user = new UserDAS().find(userId);
        Criteria criteria = getSession().createCriteria(PaymentDTO.class);
        criteria.add(Restrictions.eq("baseUser", user));
        criteria.add(Restrictions.eq("isRefund",isRefund));
        criteria.add(Restrictions.eq("deleted", 0));

        return criteria.list();

    }

    public List<PaymentDTO> getRefundablePayments(Integer userId) {

        UserDTO user = new UserDAS().find(userId);
        Criteria criteria = getSession().createCriteria(PaymentDTO.class);
        criteria.add(Restrictions.eq("baseUser", user));
        criteria.add(Restrictions.eq("isRefund",0));
        criteria.add(Restrictions.eq("deleted", 0));

        // all payments of the given user which are not refund payments
        List<PaymentDTO> allPayments =  criteria.list();

        Iterator<PaymentDTO> iterator = allPayments.iterator();
        while(iterator.hasNext()) {
            PaymentDTO payment = (PaymentDTO)iterator.next();
            if(PaymentBL.ifRefunded(payment)) {
                // remove this from original list as the payment has already been refunded
//                allPayments.remove(payment);
                iterator.remove();
            }
        }
        return allPayments;
    }

    /**
     * Returns all  refund or non refund payments
     * @param isRefund
     * @return
     */
    public List<PaymentDTO> findAllPaymentIsRefund(Integer isRefund) {

        Criteria criteria = getSession().createCriteria(PaymentDTO.class);
        criteria.add(Restrictions.eq("isRefund", isRefund));

        return criteria.list();
    }
}
