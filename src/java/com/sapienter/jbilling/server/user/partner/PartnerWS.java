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

package com.sapienter.jbilling.server.user.partner;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.sapienter.jbilling.server.process.db.PeriodUnitDTO;
import com.sapienter.jbilling.server.security.WSSecured;
import com.sapienter.jbilling.server.user.db.CustomerDTO;
import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.user.partner.db.Partner;
import com.sapienter.jbilling.server.user.partner.db.PartnerPayout;
import com.sapienter.jbilling.server.user.partner.db.PartnerRange;
import com.sapienter.jbilling.server.util.api.validation.CreateValidationGroup;
import com.sapienter.jbilling.server.util.api.validation.UpdateValidationGroup;
import com.sapienter.jbilling.server.util.db.CurrencyDAS;

/**
 * PartnerWS
 *
 * @author Brian Cowdery
 * @since 25-10-2010
 */
public class PartnerWS implements WSSecured, Serializable {

    @Min(value = 1, message = "validation.error.min,1", groups = UpdateValidationGroup.class)
    @Max(value = 0, message = "validation.error.max,0", groups = CreateValidationGroup.class)
    private Integer id;
    private Integer periodUnitId;
    private Integer userId;
    @NotNull(message="validation.error.notnull")
    private Integer feeCurrencyId;
    @NotNull(message="validation.error.notnull")
    private String balance;
    private String totalPayments;
    private String totalRefunds;
    private String totalPayouts;
    private String percentageRate;
    private String referralFee;
    private Boolean oneTime;
    private Integer periodValue;
    @NotNull(message="validation.error.notnull")
    private Date nextPayoutDate;
    private String duePayout;
    private Boolean automaticProcess;
    @NotNull(message="validation.error.notnull")
    private Integer relatedClerkUserId;
    
    private List<PartnerRangeWS> ranges = new ArrayList<PartnerRangeWS>(0);
    private List<PartnerPayoutWS> partnerPayouts = new ArrayList<PartnerPayoutWS>(0);
    private List<Integer> customerIds = new ArrayList<Integer>(0);

    public PartnerWS() {
    }

    public PartnerWS(Partner dto) {
        this.id = dto.getId();
        this.periodUnitId = dto.getPeriodUnit() != null ? dto.getPeriodUnit().getId() : null;
        this.userId = dto.getUser() != null ? dto.getUser().getId() : null;
        this.feeCurrencyId = dto.getFeeCurrency() != null ? dto.getFeeCurrency().getId() : null;
        setBalance(dto.getBalance());
        setTotalPayments(dto.getTotalPayments());
        setTotalRefunds(dto.getTotalRefunds());
        setTotalPayouts(dto.getTotalPayouts());
        setPercentageRate(dto.getPercentageRate());
        setReferralFee(dto.getReferralFee());
        this.oneTime = dto.getOneTime()>0;
        this.periodValue = dto.getPeriodValue();
        this.nextPayoutDate = dto.getNextPayoutDate();
        setDuePayout(dto.getDuePayout());
        this.automaticProcess = dto.getAutomaticProcess()>0;

        this.relatedClerkUserId= dto.getBaseUserByRelatedClerk().getId();
        
        // partner ranges
        this.ranges = new ArrayList<PartnerRangeWS>(dto.getRanges().size());
        for (PartnerRange range : dto.getRanges())
            ranges.add(new PartnerRangeWS(range));

        // partner payouts
        this.partnerPayouts = new ArrayList<PartnerPayoutWS>(dto.getPartnerPayouts().size());
        for (PartnerPayout payout : dto.getPartnerPayouts())
            partnerPayouts.add(new PartnerPayoutWS(payout));        

        // partner customer ID's
        this.customerIds = new ArrayList<Integer>(dto.getCustomers().size());
        for (CustomerDTO customer : dto.getCustomers())
            customerIds.add(customer.getId());
    }

    public Integer getRelatedClerkUserId() {
        return relatedClerkUserId;
    }

    public void setRelatedClerkUserId(Integer relatedClerkUserId) {
        this.relatedClerkUserId = relatedClerkUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPeriodUnitId() {
        return periodUnitId;
    }

    public void setPeriodUnitId(Integer periodUnitId) {
        this.periodUnitId = periodUnitId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFeeCurrencyId() {
        return feeCurrencyId;
    }

    public void setFeeCurrencyId(Integer feeCurrencyId) {
        this.feeCurrencyId = feeCurrencyId;
    }

    public String getBalance() {
        return balance;
    }

    public BigDecimal getBalanceAsDecimal() {
        return balance != null ? new BigDecimal(balance) : null;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = (balance != null ? balance.toString() : null);
    }

    public String getTotalPayments() {
        return totalPayments;
    }

    public BigDecimal getTotalPaymentsAsBigDecimal() {
        return totalPayments != null ? new BigDecimal(totalPayments) : null;
    }

    public void setTotalPayments(String totalPayments) {
        this.totalPayments = totalPayments;
    }

    public void setTotalPayments(BigDecimal totalPayments) {
        this.totalPayments = (totalPayments != null ? totalPayments.toPlainString() : null);
    }

    public String getTotalRefunds() {
        return totalRefunds;
    }

    public BigDecimal getTotalRefundsAsDecimal() {
        return totalRefunds != null ? new BigDecimal(totalRefunds) : null;
    }

    public void setTotalRefunds(String totalRefunds) {
        this.totalRefunds = totalRefunds;
    }

    public void setTotalRefunds(BigDecimal totalRefunds) {
        this.totalRefunds = (totalRefunds != null ? totalRefunds.toPlainString() : null);
    }

    public String getTotalPayouts() {
        return totalPayouts;
    }

    public BigDecimal getTotalPayoutsAsDecimal() {
        return totalPayouts != null ? new BigDecimal(totalPayouts) : null;
    }

    public void setTotalPayouts(String totalPayouts) {
        this.totalPayouts = totalPayouts;
    }

    public void setTotalPayouts(BigDecimal totalPayouts) {
        this.totalPayouts = (totalPayouts != null ? totalPayouts.toPlainString() : null);
    }

    public String getPercentageRate() {
        return percentageRate;
    }

    public BigDecimal getPercentageRateAsDecimal() {
        return percentageRate != null ? new BigDecimal(percentageRate) : null;
    }

    public void setPercentageRate(String percentageRate) {
        this.percentageRate = percentageRate;
    }

    public void setPercentageRate(BigDecimal percentageRate) {
        this.percentageRate = (percentageRate != null ? percentageRate.toPlainString() : null);
    }

    public String getReferralFee() {
        return referralFee;
    }

    public BigDecimal getReferralFeeAsDecimal() {
        return referralFee != null ? new BigDecimal(referralFee) : null;
    }

    public void setReferralFee(String referralFee) {
        this.referralFee = referralFee;
    }

    public void setReferralFee(BigDecimal referralFee) {
        this.referralFee = (referralFee != null ? referralFee.toPlainString() : null);
    }

    public Boolean getOneTime() {
        return oneTime;
    }

    public void setOneTime(Boolean oneTime) {
        this.oneTime = oneTime;
    }

    public Integer getPeriodValue() {
        return periodValue;
    }

    public void setPeriodValue(Integer periodValue) {
        this.periodValue = periodValue;
    }

    public Date getNextPayoutDate() {
        return nextPayoutDate;
    }

    public void setNextPayoutDate(Date nextPayoutDate) {
        this.nextPayoutDate = nextPayoutDate;
    }

    public String getDuePayout() {
        return duePayout;
    }

    public BigDecimal getDuePayoutAsDecimal() {
        return duePayout != null ? new BigDecimal(duePayout) : null;
    }

    public void setDuePayout(String duePayout) {
        this.duePayout = duePayout;
    }

    public void setDuePayout(BigDecimal duePayout) {
        this.duePayout = (duePayout != null ? duePayout.toPlainString() : null);
    }

    public Boolean getAutomaticProcess() {
        return automaticProcess;
    }

    public void setAutomaticProcess(Boolean automaticProcess) {
        this.automaticProcess = automaticProcess;
    }

    public List<PartnerRangeWS> getRanges() {
        return ranges;
    }

    public void setRanges(List<PartnerRangeWS> ranges) {
        this.ranges = ranges;
    }

    public List<PartnerPayoutWS> getPartnerPayouts() {
        return partnerPayouts;
    }

    public void setPartnerPayouts(List<PartnerPayoutWS> partnerPayouts) {
        this.partnerPayouts = partnerPayouts;
    }

    public List<Integer> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Integer> customerIds) {
        this.customerIds = customerIds;
    }

    /**
     * Unsupported, web-service security enforced using {@link #getOwningUserId()}
     * @return null
     */
    public Integer getOwningEntityId() {
        return null;
    }

    public Integer getOwningUserId() {
        return getUserId();
    }

    public Partner getPartnerDTO () {
        
        Partner partner = null;
        if ( this.getId()!= null ) {
            PartnerBL partnerBl= new PartnerBL(this.getId());
            partner= partnerBl.getEntity();
        } else {
            partner= new Partner();
            partner.setId(0);
        }
        
        partner.setAutomaticProcess(this.getAutomaticProcess()?1:0);
        partner.setOneTime(this.getOneTime()?1:0);
        partner.setPeriodUnit(new PeriodUnitDTO(this.getPeriodUnitId()));
        partner.setPeriodValue(this.getPeriodValue());
        partner.setNextPayoutDate(this.getNextPayoutDate());
        partner.setBalance(this.getBalanceAsDecimal());
        partner.setPercentageRate(this.getPercentageRateAsDecimal());
        partner.setReferralFee(this.getReferralFeeAsDecimal());
        partner.setFeeCurrency(new CurrencyDAS().find(this.getFeeCurrencyId()));
        partner.setRelatedClerkUserId(this.getRelatedClerkUserId());
        if ( null != this.userId && this.userId.intValue() > 0) {
            partner.setBaseUser(new UserDAS().find(this.userId));
        }
        //partner.setBaseUserByRelatedClerk(new UserDAS().find(partner.getRelatedClerkUserId()));
        
        partner.setTotalPayments(this.getTotalPaymentsAsBigDecimal());
        partner.setTotalRefunds(this.getTotalRefundsAsDecimal());
        partner.setTotalPayouts(this.getTotalPayoutsAsDecimal());
        partner.setDuePayout(this.getDuePayoutAsDecimal());
        
        return partner;
    }
    
    
    @Override
    public String toString() {
        return "PartnerWS{"
               + "id=" + id
               + ", periodUnitId=" + periodUnitId
               + ", userId=" + userId
               + ", feeCurrencyId=" + feeCurrencyId
               + ", balance=" + balance
               + ", totalPayments=" + totalPayments
               + ", totalRefunds=" + totalRefunds
               + ", totalPayouts=" + totalPayouts
               + ", percentageRate=" + percentageRate
               + ", referralFee=" + referralFee
               + ", oneTime=" + oneTime
               + ", periodValue=" + periodValue
               + ", nextPayoutDate=" + nextPayoutDate
               + ", duePayout=" + duePayout
               + ", automaticProcess=" + automaticProcess
               + ", ranges=" + (ranges != null ? ranges.size() : null)
               + ", partnerPayouts=" + (partnerPayouts != null ? partnerPayouts.size() : null)
               + ", customerIds=" + (customerIds != null ? customerIds.size() : null)
               + '}';
    }
}
