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

/*
 * Created on Apr 3, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.user;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.CommonConstants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.NotificationSessionLocal;
import com.sapienter.jbilling.interfaces.NotificationSessionLocalHome;
import com.sapienter.jbilling.interfaces.PartnerEntityLocal;
import com.sapienter.jbilling.interfaces.PartnerEntityLocalHome;
import com.sapienter.jbilling.interfaces.PartnerPayoutEntityLocal;
import com.sapienter.jbilling.interfaces.PartnerPayoutEntityLocalHome;
import com.sapienter.jbilling.interfaces.PartnerRangeEntityLocal;
import com.sapienter.jbilling.interfaces.PartnerRangeEntityLocalHome;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.entity.PartnerRangeDTO;
import com.sapienter.jbilling.server.item.CurrencyBL;
import com.sapienter.jbilling.server.list.ResultList;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.notification.NotificationNotFoundException;
import com.sapienter.jbilling.server.payment.PaymentBL;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.MapPeriodToCalendar;
import com.sapienter.jbilling.server.util.audit.EventLogger;

/**
 * @author Emil
 */
public class PartnerBL extends ResultList 
        implements PartnerSQL {
    private JNDILookup EJBFactory = null;
    private PartnerEntityLocalHome partnerHome = null;
    private PartnerEntityLocal partner = null;
    private PartnerRangeEntityLocalHome partnerRangeHome = null;
    private PartnerRangeEntityLocal partnerRange = null;
    private PartnerPayoutEntityLocalHome payoutHome = null;
    private PartnerPayoutEntityLocal payout = null;
    private Logger log = null;
    private EventLogger eLogger = null;

    public PartnerBL(Integer partnerId) 
            throws NamingException, FinderException {
        init();

        set(partnerId);
    }
    
    public PartnerBL() throws NamingException {
        init();
    }
    
    public PartnerBL(PartnerEntityLocal entity) 
            throws NamingException{
        partner = entity;
        init();
    }
    
    public void set(Integer partnerId) 
            throws FinderException {
        partner = partnerHome.findByPrimaryKey(partnerId);
    }
    
    public void setPayout(Integer payoutId) 
            throws FinderException {
        payout = payoutHome.findByPrimaryKey(payoutId);
    }

    private void init() throws NamingException {
        log = Logger.getLogger(PartnerBL.class);     
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        partnerHome = (PartnerEntityLocalHome) EJBFactory.lookUpLocalHome(
                PartnerEntityLocalHome.class,
                PartnerEntityLocalHome.JNDI_NAME);
        payoutHome = (PartnerPayoutEntityLocalHome) EJBFactory.lookUpLocalHome(
                PartnerPayoutEntityLocalHome.class,
                PartnerPayoutEntityLocalHome.JNDI_NAME);
        payout = null;
        partnerRangeHome = (PartnerRangeEntityLocalHome) EJBFactory.lookUpLocalHome(
                PartnerRangeEntityLocalHome.class,
                PartnerRangeEntityLocalHome.JNDI_NAME);
        partnerRange = null;
    }

    public PartnerEntityLocal getEntity() {
        return partner;
    }
    
    public Integer create(PartnerDTOEx dto) 
            throws CreateException, SessionInternalError {
        Float zero = new Float(0);
        log.debug("creating partner");
        partner = partnerHome.create(dto.getBalance(), zero, zero, 
                zero, dto.getOneTime(), 
                dto.getPeriodUnitId(), dto.getPeriodValue(), 
                dto.getNextPayoutDate(), dto.getAutomaticProcess());
        partner.setPercentageRate(dto.getPercentageRate());
        partner.setReferralFee(dto.getReferralFee());
        partner.setFeeCurrencyId(dto.getFeeCurrencyId());
        partner.setDuePayout(zero);
        try {
            UserBL clerk = new UserBL(dto.getRelatedClerkUserId());
            partner.setRelatedClerk(clerk.getEntity());
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        log.debug("created partner id " + partner.getId());
        
        return partner.getId();
    }
    
    public void update(Integer executorId, PartnerDTOEx dto) 
            throws FinderException, NamingException {

        eLogger.audit(executorId, Constants.TABLE_PARTNER, partner.getId(),
                EventLogger.MODULE_USER_MAINTENANCE, 
                EventLogger.ROW_UPDATED, null, null, 
                null);
        partner.setBalance(dto.getBalance());
        partner.setPercentageRate(dto.getPercentageRate());
        partner.setReferralFee(dto.getReferralFee());
        partner.setFeeCurrencyId(dto.getFeeCurrencyId());
        partner.setOneTime(dto.getOneTime());
        partner.setPeriodUnitId(dto.getPeriodUnitId());
        partner.setPeriodValue(dto.getPeriodValue());
        partner.setNextPayoutDate(dto.getNextPayoutDate());
        partner.setAutomaticProcess(dto.getAutomaticProcess());
        UserBL clerk = new UserBL(dto.getRelatedClerkUserId());
        partner.setRelatedClerk(clerk.getEntity());
    }
    
    /**
     * This is called from a new transaction
     * @param payoutDto
     */
    public void processPayout(Integer partnerId) 
            throws CreateException, NamingException, SQLException, 
                FinderException, SessionInternalError, PluggableTaskException,
                TaskException {
        boolean notPaid;
        partner = partnerHome.findByPrimaryKey(partnerId);
        // find out the date ranges for this payout
        Date startDate, endDate, dates[];
        dates = calculatePayoutDates();
        startDate = dates[0];
        endDate = dates[1];
       
        // see if this partner should be paid on-line
        boolean doProcess = partner.getAutomaticProcess().intValue() == 1;
        
        // some handy data
        Integer currencyId = partner.getUser().getCurrencyId();
        Integer entityId = partner.getUser().getEntity().getId();
        Integer userId = partner.getUser().getUserId();
        
        if (doProcess) {
            // now creating the row
            Float zero = new Float(0);
            payout = payoutHome.create(startDate, endDate, zero, zero, zero);
            payout.setPartner(partner);
        } else {
            payout = null; // to avoid confustion
        }
        
        // get the total for this payout
        PartnerPayoutDTOEx dto = calculatePayout(startDate, endDate, 
                currencyId);
        
        if (doProcess) {
            PaymentDTOEx payment = PaymentBL.findPaymentInstrument(entityId,
                    userId);
            if (payment == null) {
                // this partner doesn't have a way to get paid
                eLogger.warning(entityId, partnerId, 
                        EventLogger.MODULE_USER_MAINTENANCE, 
                        EventLogger.CANT_PAY_PARTNER, 
                        Constants.TABLE_PARTNER);
                notPaid = true;
            } else {
                payment.setAmount(dto.getPayment().getAmount());
                payment.setCurrencyId(currencyId);
                payment.setUserId(userId);
                payment.setPaymentDate(partner.getNextPayoutDate());
                notPaid = !processPayment(payment, entityId, dto, true);
             }
        } else {
            notPaid = true;
            // just notify to the clerk in charge
            notifyPayout(entityId, partner.getRelatedClerk().
                    getLanguageIdField(), dto.getPayment().
                        getAmount(), startDate, endDate, true);
        }
        
        if (notPaid) {
            // let know that this partner should have been paid.
            notifyPayout(entityId, partner.getRelatedClerk().
                    getLanguageIdField(), dto.getPayment().
                        getAmount(), startDate, endDate, true);
            // set the partner due payout
            partner.setDuePayout(dto.getPayment().getAmount());
        }

    }
    
    /**
     * This is to be called from the client, when creating a manual payout
     * @param partnerId
     * @param start
     * @param end
     * @param payment
     * @return
     */
    public Integer processPayout(Integer partnerId, Date start, Date end,
            PaymentDTOEx payment, Boolean process) 
            throws FinderException, CreateException, SessionInternalError,
                SQLException, NamingException {
        
        partner = partnerHome.findByPrimaryKey(partnerId);
        Float zero = new Float(0);
        payout = payoutHome.create(start, end, zero, zero, zero);
                    payout.setPartner(partner);
        payout.setPartner(partner);
        
        // get the total for this payout
        PartnerPayoutDTOEx dto = calculatePayout(start, end, 
                payment.getCurrencyId());
    
        // finish the payment
        payment.setIsRefund(new Integer(1));
        payment.setAttempt(new Integer(1));
        processPayment(payment, partner.getUser().getEntity().getId(), dto,
                process.booleanValue());
        return payment.getResultId();
    }
    
    public Date[] calculatePayoutDates() 
            throws NamingException, SQLException, FinderException, 
                SessionInternalError{
        Date retValue[] = new Date[2];        
        // for this I have to find the last payout for this partner
        Integer payoutId = getLastPayout(partner.getId());
        Date lastEndDate;
        // the return value of 'empty' from a function (max) could vary from db to db
        if (payoutId != null && payoutId.intValue() != 0) {
            PartnerPayoutEntityLocal previousPayout = 
                    payoutHome.findByPrimaryKey(payoutId);
            lastEndDate = previousPayout.getEndingDate();
        } else {
            // if this is the first payout, calculate from the creation of the partner
            lastEndDate = partner.getUser().getCreateDateTime();
        }
        retValue[0] = lastEndDate;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(lastEndDate);
        cal.add(MapPeriodToCalendar.map(partner.getPeriodUnitId()), 
                partner.getPeriodValue().intValue());
        retValue[1] = cal.getTime();
        log.debug("Dates for partner " + partner.getId() + " start= " + retValue[0] +
                " end " + retValue[1]);
        return retValue;
    }
    
    private boolean processPayment(PaymentDTOEx payment, Integer entityId,
            PartnerPayoutDTOEx dto, boolean process) 
            throws NamingException, CreateException, FinderException, 
                    SessionInternalError {
        PaymentBL paymentBL = new PaymentBL();
        boolean retValue;
        // isRefund is not null, so having to decide it is better to use refund.
        payment.setPayoutId(payout.getId());
        payment.setIsRefund(new Integer(1));
        payment.setAttempt(new Integer(1));
        payment.setBalance(new Float(0));
                
        // process the payment realtime
        Integer result = Constants.RESULT_OK;
        if (process) {
            result = paymentBL.processPayment(entityId, payment);
            if (result == null) { // means no pluggable task config.
                result = Constants.RESULT_UNAVAILABLE;
            }
        } else {
            // create the payment row
            paymentBL.create(payment);
        }
        // and link it to this payout row
        payout.setPayment(paymentBL.getEntity());
                
        // update this partner fields if the payment went through
        if (result.equals(Constants.RESULT_OK)) {
            applyPayout(dto);
            // this partner just got a full payout
            partner.setDuePayout(new Float(0));
            // if there was something paid, notify
            if (dto.getPayment().getAmount().floatValue() > 0) {
                log.debug("payout notification partner = " + partner.getId() +
                  " with language = " + partner.getUser().getLanguageIdField());
                notifyPayout(entityId, partner.getUser().
                        getLanguageIdField(), dto.getPayment().
                            getAmount(), dto.getStartingDate(), 
                            dto.getEndingDate(), false);
            }
            retValue = true;
        } else {
            retValue = false;
        }
        payment.setResultId(result);

        return retValue;
    }
    
    /**
     * Goes over the payments/refunds of the current partner for the
     * given period. It will update the records selected linking them to 
     * the new payout record and the totals of the payout record if 
     * such record has been initialized.
     * @param start
     * @param end
     * @return
     */
    public PartnerPayoutDTOEx calculatePayout(Date start, Date end, Integer currencyId) 
            throws NamingException, SQLException, FinderException,
                SessionInternalError {
    	BigDecimal total = new BigDecimal("0");
        BigDecimal paymentTotal = new BigDecimal("0");
        BigDecimal refundTotal = new BigDecimal("0");
        
        log.debug("Calculating payout partner " + partner.getId() + " from " + 
                start + " to " + end);
        Connection conn = EJBFactory.lookUpDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(paymentsInPayout);
        stmt.setInt(1, partner.getId().intValue());
        stmt.setDate(2, new java.sql.Date(start.getTime()));
        stmt.setDate(3, new java.sql.Date(end.getTime()));
        ResultSet result = stmt.executeQuery();
        // since esql doesn't support dates, a direct call is necessary
        while (result.next()) {
            PaymentBL payment = new PaymentBL(new Integer(result.getInt(1)));
            Integer paymentCurrencyId = payment.getEntity().getCurrencyId();
            Integer entityId = partner.getUser().getEntity().getId();
            
            // the amount will have to be in the requested currency
            // convert then the payment amout
            CurrencyBL currency = new CurrencyBL();
            BigDecimal paymentAmount = new BigDecimal(currency.convert(paymentCurrencyId, 
                    currencyId, payment.getEntity().getAmount(), entityId).toString());
            log.debug("payment amount = " + paymentAmount);
            BigDecimal amount = new BigDecimal(calculateCommission(paymentAmount.floatValue(), currencyId, 
                    payment.getEntity().getUser(), payout != null)); 
            log.debug("commission = " + amount);
            
            // payments add, refunds take
            if (payment.getEntity().getIsRefund().intValue() == 0) {
                total = total.add(amount);
                paymentTotal = paymentTotal.add(amount);
            } else {
            	total = total.subtract(amount);
                refundTotal = refundTotal.add(amount);
            }
            if (payout != null) {
                // update the payment record with the new payout
                payment.getEntity().setPayoutIncludedIn(payout);
            }
        }
        result.close();
        stmt.close();
        conn.close();
        
        if (payout != null) {
            // update the payout row
            payout.setPaymentsAmount(new Float(paymentTotal.floatValue()));
            payout.setRefundsAmount(new Float(refundTotal.floatValue()));
        }
        
        log.debug("total " + total + " currency = " + currencyId);
        PartnerPayoutDTOEx retValue = new PartnerPayoutDTOEx();
        retValue.getPayment().setAmount(new Float(total.floatValue()));
        retValue.getPayment().setCurrencyId(currencyId);
        retValue.setRefundsAmount(new Float(refundTotal.floatValue()));
        retValue.setPaymentsAmount(new Float(paymentTotal.floatValue()));
        retValue.setStartingDate(start);
        retValue.setEndingDate(end);
        
        return retValue;
    }
    
    /**
     * This will return the id of the lates payout that was successfull
     * @param partnerId
     * @return
     * @throws NamingException
     * @throws SQLException
     */
    private Integer getLastPayout(Integer partnerId) 
            throws NamingException, SQLException {
        Integer retValue = null;
        Connection conn = EJBFactory.lookUpDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(lastPayout);
        stmt.setInt(1, partnerId.intValue());
        ResultSet result = stmt.executeQuery();
        // since esql doesn't support max, a direct call is necessary
        if (result.next()) {
            retValue = new Integer(result.getInt(1));
        }
        result.close();
        stmt.close();
        conn.close();
        log.debug("Finding last payout ofr partner " + partnerId + " result = " + retValue);
        return retValue;
    }
    
    /**
     * Will update the partner fields with the total of this payout
     * @param dto
     */
    public void applyPayout(PartnerPayoutDTOEx dto) 
            throws SessionInternalError {
        // the balance goes down with a payout
    	BigDecimal newBalance = new BigDecimal(partner.getBalance().toString());
    	newBalance = newBalance.subtract(new BigDecimal(dto.getPayment().getAmount().toString())); 
        partner.setBalance(new Float(newBalance.floatValue()));
        
        // add this payout to her total
        BigDecimal newTotal = new BigDecimal(partner.getTotalPayouts().toString());
        newTotal = newTotal.add(new BigDecimal(dto.getPayment().getAmount().toString()));
        partner.setTotalPayouts(new Float(newTotal.floatValue()));
        
        // the next payout
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(partner.getNextPayoutDate());
        cal.add(MapPeriodToCalendar.map(partner.getPeriodUnitId()), 
                partner.getPeriodValue().intValue());
        partner.setNextPayoutDate(cal.getTime());

    }
    
    public void notifyPayout(Integer entityId, Integer languageId,
            Float total, Date start, Date end, boolean clerk) 
            throws NamingException, SessionInternalError, CreateException,
				FinderException {
        // make the notification
        NotificationBL notification = new NotificationBL();
        try {
	        MessageDTO message = notification.getPayoutMessage(entityId, 
	                languageId, total, start, end, clerk, partner.getId());
	 
	        NotificationSessionLocalHome notificationHome =
	                (NotificationSessionLocalHome) EJBFactory.lookUpLocalHome(
	                NotificationSessionLocalHome.class,
	                NotificationSessionLocalHome.JNDI_NAME);
	
	        NotificationSessionLocal notificationSess = 
	                notificationHome.create();
	        if (!clerk) {
	            notificationSess.notify(partner.getUser(), message);
	        } else {
	            notificationSess.notify(partner.getRelatedClerk(), message);

	        }
        } catch (NotificationNotFoundException e) {
        	//  this entity has not defined
        	// a message for the payout
        	log.warn("A payout message shoule've been sent, but entity " + 
        			entityId + " has not defined a notification");
        }
    }
    
    public float calculateCommission(float amount, Integer currencyId, 
            UserEntityLocal user, boolean update) 
            throws SessionInternalError, NamingException, FinderException,
                SQLException {
        log.debug("Calculating commision on " + amount); 
        float result;
        BigDecimal decAmount = new BigDecimal(amount);
        if (partner.getOneTime().intValue() == 1) {
            // this partner gets paid once per customer she brings
            Integer flag = user.getCustomer().getReferralFeePaid();
            if (flag == null || flag.intValue() == 0) {
                if (update) { // otherwise just calculate
                    user.getCustomer().setReferralFeePaid(
                            new Integer(1));
                }
            } else {
                // it got a fee from this guy already
                return 0;
            }
        } 
        
        // find the rate
        BigDecimal rate = null;
        BigDecimal fee = null;
        if (partner.getRanges().size() > 0) {
            getRangedCommission();
            rate = new BigDecimal(partnerRange.getPercentageRate().toString());
            fee = new BigDecimal(partnerRange.getReferralFee().toString());
        } else {
            rate = new BigDecimal(partner.getPercentageRate().toString());
            fee = new BigDecimal(partner.getReferralFee().toString());
        }

        log.debug("using rate " + rate + " fee " + fee);
        // apply the rate to get the commission value
        if (rate != null) {
            result = decAmount.divide(new BigDecimal("100"), 
            		CommonConstants.BIGDECIMAL_SCALE, 
            		CommonConstants.BIGDECIMAL_ROUND).multiply(rate).floatValue();
        } else if (fee != null) {
            CurrencyBL currency = new CurrencyBL();
            Integer partnerCurrencyId = partner.getFeeCurrencyId();
            if (partnerCurrencyId == null) {
                log.info("Partner without currency, using entity's as default");
                partnerCurrencyId = partner.getUser().getEntity().getCurrencyId();
            }
            result = currency.convert(partnerCurrencyId, 
                    currencyId, new Float(fee.floatValue()), 
                    partner.getUser().getEntity().getId()).floatValue();
        } else {
            throw new SessionInternalError(
                    "Partner without commission configuration");
        }
        log.debug("result = " + result);
        return result;
    }
    
    /**
     * Go over the rates for this partner and return the right
     * range for the amount of customers
     * After the call, the variable partnerRange is set to the right range
     */
    private void getRangedCommission() 
            throws NamingException, SQLException {
        int totalCustomers = getCustomersCount();
        // if there were more than just 20 rows, this would have to
        // be done all with plain sql instead of ejbs
        Vector rates = new Vector(partner.getRanges());
        Collections.sort(rates, new PartnerRangeComparator());
        partnerRange = null; // to get an exception if there are no ranges
        
        for (int f = 0; f < rates.size(); f++) {
            partnerRange = (PartnerRangeEntityLocal) rates.get(f);
            if (partnerRange.getRangeFrom().intValue() <= totalCustomers &&
                    partnerRange.getRangeTo().intValue() >= totalCustomers) {
                break;
            }
        }
        // we will always return a rate. If none were found, the last one
        // (biggest) is returned
    }
    
    private int getCustomersCount() 
            throws SQLException, NamingException {
        int retValue = 0;
        Connection conn = EJBFactory.lookUpDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(countCustomers);
        stmt.setInt(1, partner.getId().intValue());
        ResultSet result = stmt.executeQuery();
        // since esql doesn't support max, a direct call is necessary
        if (result.next()) {
            retValue = result.getInt(1);
        }
        result.close();
        stmt.close();
        conn.close();
        return retValue;
    }
    
    public PartnerDTOEx getDTO() 
            throws NamingException {
        PartnerDTOEx retValue = new PartnerDTOEx();
        retValue.setAutomaticProcess(partner.getAutomaticProcess());
        retValue.setBalance(partner.getBalance());
        retValue.setDuePayout(partner.getDuePayout());
        retValue.setReferralFee(partner.getReferralFee());
        retValue.setFeeCurrencyId(partner.getFeeCurrencyId());
        retValue.setId(partner.getId());
        retValue.setNextPayoutDate(partner.getNextPayoutDate());
        retValue.setOneTime(partner.getOneTime());
        retValue.setPercentageRate(partner.getPercentageRate());
        retValue.setPeriodUnitId(partner.getPeriodUnitId());
        retValue.setPeriodValue(partner.getPeriodValue());
        retValue.setRelatedClerkUserId(partner.getRelatedClerk().getUserId());
        
        // add all the payouts
        PartnerPayoutDTOEx payouts[] = new PartnerPayoutDTOEx[
                partner.getPayouts().size()];
        int index = 0;
        for (Iterator it = partner.getPayouts().iterator(); it.hasNext(); ) {
            payout = (PartnerPayoutEntityLocal) 
                    it.next();
            payouts[index] = getPayoutDTO();
            index++;
        }
        retValue.setPayouts(payouts);
        
        // add the ranges
        if (partner.getRanges().size() > 0) {
            PartnerRangeDTO ranges[] = new PartnerRangeDTO[
                    partner.getRanges().size()];
            Vector beans = new Vector(partner.getRanges()); 
            Collections.sort(beans, new PartnerRangeComparator());
            int f = 0;
            for (Iterator it = beans.iterator(); it.hasNext();) {
                PartnerRangeDTO range = new PartnerRangeDTO();
                partnerRange = (PartnerRangeEntityLocal) it.next();
                range.setId(partnerRange.getId());
                range.setPercentageRate(partnerRange.getPercentageRate());
                range.setRangeFrom(partnerRange.getRangeFrom());
                range.setRangeTo(partnerRange.getRangeTo());
                range.setReferralFee(partnerRange.getReferralFee());
                
                ranges[f++] = range;
            }
            retValue.setRanges(ranges);
        } else {
            retValue.setRanges(null);
        }
        
        return retValue;
    }
    
    public PartnerPayoutDTOEx getLastPayoutDTO(Integer partnerId) 
            throws SQLException, NamingException, FinderException {
        PartnerPayoutDTOEx retValue = null;
        
        Integer payoutId = getLastPayout(partnerId);
        if (payoutId != null && payoutId.intValue() != 0) {
            payout = payoutHome.findByPrimaryKey(payoutId);
            retValue = getPayoutDTO();
        }
        return retValue;
    }
    
    public PartnerPayoutDTOEx getPayoutDTO() 
            throws NamingException {
        PartnerPayoutDTOEx payoutDto = new PartnerPayoutDTOEx(payout.getId(),
                payout.getStartingDate(),
                payout.getEndingDate(), payout.getPaymentsAmount(),
                payout.getRefundsAmount(), payout.getBalanceLeft());
        if (payout.getPayment() != null) {
            PaymentBL payment = new PaymentBL(payout.getPayment());
            payoutDto.setPayment(payment.getDTO());
            payoutDto.setPaymentId(payout.getPayment().getId());
        }
        payoutDto.setPartnerId(payout.getPartner().getId());
        return payoutDto;
    }
    
    public CachedRowSet getList(Integer entityId)
            throws SQLException, Exception{

        prepareStatement(PartnerSQL.list);
        cachedResults.setInt(1,entityId.intValue());
        execute();
        conn.close();
        return cachedResults;
    }

    public CachedRowSet getPayoutList(Integer partnerId)
            throws SQLException, Exception{

        prepareStatement(PartnerSQL.listPayouts);
        cachedResults.setInt(1, partnerId.intValue());
        execute();
        conn.close();
        return cachedResults;
    }
    
    /**
     * Remove the existing ranges and create rows with 
     * the values of the parameter
     * @param ranges
     */
    public void setRanges(Integer executorId, PartnerRangeDTO[] ranges) 
            throws CreateException, RemoveException {
        eLogger.audit(executorId, Constants.TABLE_PARTNER_RANGE, partner.getId(),
                EventLogger.MODULE_USER_MAINTENANCE, 
                EventLogger.ROW_UPDATED, null, null, null);
        // remove existing ranges (a clear will only set the partner_id = null)
        for (Iterator it = partner.getRanges().iterator(); it.hasNext();) {
            partnerRange = (PartnerRangeEntityLocal) it.next();
            partnerRange.remove();
            it = partner.getRanges().iterator();
        }
        
        // may be this is a delete
        if (ranges == null) {
            return;
        }
        // go through the array creating the rows
        for (int f = 0; f < ranges.length; f++) {
            partnerRange = partnerRangeHome.create( 
                    ranges[f].getRangeFrom(), ranges[f].getRangeTo());
            partnerRange.setPercentageRate(ranges[f].getPercentageRate());
            partnerRange.setReferralFee(ranges[f].getReferralFee());
            partner.getRanges().add(partnerRange);
        }
    }

}
