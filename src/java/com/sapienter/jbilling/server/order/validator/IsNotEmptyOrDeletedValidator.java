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
