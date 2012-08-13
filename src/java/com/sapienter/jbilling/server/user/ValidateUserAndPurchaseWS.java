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

package com.sapienter.jbilling.server.user;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Result object for validatePurchase API method.
 */
public class ValidateUserAndPurchaseWS extends ValidatePurchaseWS implements Serializable {

    private Integer accountStatus;
    private BigDecimal dynamicBalance;

    public ValidateUserAndPurchaseWS() {
        super();
        accountStatus = 1;
        dynamicBalance = BigDecimal.ZERO;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public BigDecimal getDynamicBalance() {
        return dynamicBalance;
    }

    public void setDynamicBalance(BigDecimal dynamicBalance) {
        this.dynamicBalance = dynamicBalance;
    }
    
}
