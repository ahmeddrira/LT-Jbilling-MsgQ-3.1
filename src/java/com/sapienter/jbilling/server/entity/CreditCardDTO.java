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
package com.sapienter.jbilling.server.entity;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlType;

import com.sapienter.jbilling.server.user.validator.CreditCardNumber;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;

/**
 * Only used for web services backward compatibility. 
 * Do not use!
 */
@XmlType(name = "credit-card")
public class CreditCardDTO implements Serializable {

    private Integer id;
    @CreditCardNumber(message = "validation.error.invalid.card.number")
    @NotEmpty(message="validation.error.notnull")
    private String number;
    @NotNull(message="validation.error.notnull")
    private Date expiry;
    @NotEmpty(message="validation.error.notnull")
    private String name;
    private Integer type;
    private Integer deleted;
    private String securityCode;
    private String gatewayKey;

    public CreditCardDTO() {
    }

    public CreditCardDTO(Integer id, String number, Date expiry, String name, Integer type, Integer deleted,
                         String securityCode) {
        this.id = id;
        this.number = number;
        this.expiry = expiry;
        this.name = name;
        this.type = type;
        this.deleted = deleted;
        this.securityCode = securityCode;

    }

    public CreditCardDTO(CreditCardDTO otherValue) {
        this.id = otherValue.id;
        this.number = otherValue.number;
        this.expiry = otherValue.expiry;
        this.name = otherValue.name;
        this.type = otherValue.type;
        this.deleted = otherValue.deleted;
        this.securityCode = otherValue.securityCode;
        this.gatewayKey = otherValue.gatewayKey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getGatewayKey() {
        return gatewayKey;
    }

    public void setGatewayKey(String gatewayKey) {
        this.gatewayKey = gatewayKey;
    }

    @Override
    public String toString() {
        return "CreditCardDTO{" +
               "id=" + id +
               ", number='" + number + '\'' +
               ", expiry=" + expiry +
               ", name='" + name + '\'' +
               ", type=" + type +
               ", deleted=" + deleted +
               ", securityCode='" + securityCode + '\'' +
               ", gatewayKey='" + gatewayKey + '\'' +
               '}';
    }
}
