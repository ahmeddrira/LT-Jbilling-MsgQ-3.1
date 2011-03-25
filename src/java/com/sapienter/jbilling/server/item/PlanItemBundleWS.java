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

package com.sapienter.jbilling.server.item;


import com.sapienter.jbilling.server.item.db.PlanItemBundleDTO;
import com.sapienter.jbilling.server.util.Constants;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * PlanItemBundleWS
 *
 * @author Brian Cowdery
 * @since 25/03/11
 */
public class PlanItemBundleWS implements Serializable {

    public static final String TARGET_SELF = "SELF";
    public static final String TARGET_BILLABLE = "BILLABLE";

    private Integer id;
    private String quantity = "0";
    private Integer periodId = Constants.ORDER_PERIOD_ONCE;
    private String targetCustomer = TARGET_SELF;
    private boolean addIfExists = true;

    public PlanItemBundleWS() {
    }

    public PlanItemBundleWS(PlanItemBundleDTO dto) {
        this.id = dto.getId();
        this.addIfExists = dto.addIfExists();

        setQuantity(dto.getQuantity());

        if (dto.getPeriod() != null) this.periodId = dto.getPeriod().getId();
        if (dto.getTargetCustomer() != null) this.targetCustomer = dto.getTargetCustomer().name();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public BigDecimal getQuantityAsDecimal() {
        return quantity != null ? new BigDecimal(quantity) : null;
    }

    public void setQuantityAsDecimal(BigDecimal quantity) {
        setQuantity(quantity);
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = (quantity != null ? quantity.toString() : null);
    }

    public Integer getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    public String getTargetCustomer() {
        return targetCustomer;
    }

    public void setTargetCustomer(String targetCustomer) {
        this.targetCustomer = targetCustomer;
    }

    public boolean addIfExists() {
        return addIfExists;
    }

    public void setAddIfExists(boolean addIfExists) {
        this.addIfExists = addIfExists;
    }

    @Override
    public String toString() {
        return "PlanItemBundleWS{"
               + "id=" + id
               + ", quantity='" + quantity + '\''
               + ", periodId=" + periodId
               + ", targetCustomer='" + targetCustomer + '\''
               + ", addIfExists=" + addIfExists
               + '}';
    }
}
