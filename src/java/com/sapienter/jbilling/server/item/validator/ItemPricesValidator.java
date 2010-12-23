/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.item.validator;

import com.sapienter.jbilling.server.item.ItemPriceDTOEx;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.List;

/**
 * ItemPricesValidator
 *
 * @author Brian Cowdery
 * @since 23/12/10
 */
public class ItemPricesValidator implements ConstraintValidator<ItemPrices, List<ItemPriceDTOEx>>, Serializable {

    private static final long serialVersionUID = 1L;

    public void initialize(ItemPrices itemPrices) {
    }

    public boolean isValid(List<ItemPriceDTOEx> prices, ConstraintValidatorContext constraintContext) {
        for (ItemPriceDTOEx price : prices)
            if (price.getPriceAsDecimal() != null)
                return true;

        return false;
    }
}
