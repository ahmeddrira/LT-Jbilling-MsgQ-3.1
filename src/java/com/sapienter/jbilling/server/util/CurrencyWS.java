/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
 */

package com.sapienter.jbilling.server.util;

import com.sapienter.jbilling.server.util.db.CurrencyDTO;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * CurrencyWS
 *
 * @author Brian Cowdery
 * @since 07/04/11
 */
public class CurrencyWS implements Serializable {

    private Integer id;
    private String description;
    private String symbol;
    private String code;
    private String countryCode;
    private Boolean inUse;
    private String rate;
    private String sysRate;
    private Date fromDate;

    private boolean defaultCurrency;

    public CurrencyWS() {
    }

    public CurrencyWS(CurrencyDTO dto, boolean defaultCurrency) {
        this.id = dto.getId();
        this.description = dto.getDescription();
        this.symbol = dto.getSymbol();
        this.code = dto.getCode();
        this.countryCode = dto.getCountryCode();
        this.inUse = dto.getInUse();

        setRate(dto.getRate());
        setSysRate(dto.getSysRate());

        this.defaultCurrency = defaultCurrency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public String getRate() {
        return rate;
    }

    public BigDecimal getRateAsDecimal() {
        return rate != null ? new BigDecimal(rate) : null;
    }

    public void setRate(String rate) {
        if(!StringUtils.isEmpty(rate)) {
            this.rate = rate;
        } else {
            this.rate = null;
        }
    }

    public void setRate(BigDecimal rate) {
        this.rate = (rate != null ? rate.toString() : null);
    }

    public void setRateAsDecimal(BigDecimal rate) {
        setRate(rate);
    }

    public String getSysRate() {
        return sysRate;
    }

    public BigDecimal getSysRateAsDecimal() {
        return sysRate != null ? new BigDecimal(sysRate) : null;
    }

    public void setSysRate(String sysRate) {
        this.sysRate = sysRate;
    }

    public void setSysRate(BigDecimal systemRate) {
        this.sysRate = (systemRate != null ? systemRate.toString() : null);
    }

    public void setSysRateAsDecimal(BigDecimal systemRate) {
        setSysRate(systemRate);
    }

    public boolean isDefaultCurrency() {
        return defaultCurrency;
    }

    public boolean getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(boolean defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    @Override
    public String toString() {
        return "CurrencyWS{"
               + "id=" + id
               + ", symbol='" + symbol + '\''
               + ", code='" + code + '\''
               + ", countryCode='" + countryCode + '\''
               + ", inUse=" + inUse
               + ", rate='" + rate + '\''
               + ", systemRate='" + sysRate + '\''
               + ", isDefaultCurrency=" + defaultCurrency
               + ", fromDate=" + fromDate
               + '}';
    }
}
