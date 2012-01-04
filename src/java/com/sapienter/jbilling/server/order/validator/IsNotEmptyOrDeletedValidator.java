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

package com.sapienter.jbilling.server.order.validator;

import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * IsNotEmptyOrDeletedValidator
 *
 * @author Juan Vidal
 * @since 04/01/2012
 */
public class IsNotEmptyOrDeletedValidator implements ConstraintValidator<IsNotEmptyOrDeleted, OrderLineWS[]> {

    private static final Logger LOG = Logger.getLogger(DateBetweenValidator.class);

    public void initialize(IsNotEmptyOrDeleted isNotEmptyOrDeleted) {
    }

    public boolean isValid(OrderLineWS[] orderLines, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (orderLines.length == 0) {
                return false;
            } else {
                Integer deletedQty = 0;
                for (OrderLineWS orderLine : orderLines) {
                    deletedQty += orderLine.getDeleted();
                }

                if (deletedQty == orderLines.length) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (ClassCastException e) {
            LOG.debug("Property does not contain a java.util.Date object.");
        }

        return false;
    }
}
