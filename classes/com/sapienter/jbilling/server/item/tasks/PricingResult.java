/*
 * jBilling - The Enterprise Open Source Billing System
 * Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

 * This file is part of jbilling.

 * jbilling is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * jbilling is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.item.tasks;

import com.sapienter.jbilling.server.rule.Result;
import java.math.BigDecimal;

/**
 *
 * @author emilc
 */
public class PricingResult extends Result {
    private final Integer itemId;
    private final Integer userId;
    private final Integer currencyId;
    private BigDecimal price;

    public PricingResult(Integer itemId, Integer userId, Integer currencyId) {
        this.itemId = itemId;
        this.userId = userId;
        this.currencyId = currencyId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setPrice(String price) {
        this.price = new BigDecimal(price);
    }

    public String toString() {
        return  "PricingResult:" +
                "itemId=" + itemId + " " +
                "userId=" + userId + " " +
                "currencyId=" + currencyId + " " +
                "price=" + price + " " + super.toString();
    }

}
