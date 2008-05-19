/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
 * Created on Oct 25, 2004
 *
 */
package com.sapienter.jbilling.server.pluggableTask;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.db.OrderBillingTypeDTO;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.db.BaseUser;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.Util;

/**
 * @author Emil
 * This task will create a new purchase order with the item specified by the
 * task parameter and mark the invoice as processed with invoice.overdue_step = 0.
 * 
 * Situations considered
 * payable not overdue  -> nothing
 * payable overdue (pure) -> penalty
 * payable overdue partialy paid -> penalty on balance
 * not payable, not paid, delegated -> penalty on total
 * not payable, partialy paid, delegated -> penalty on previous balance
 * Since the task is running on the day after the due date ... :
 * not payable, not paid, delegated and paid after due date -> penalty on total
 * not payable, not paid, delegated and paid partialy after due date -> penalty on total
 * not payable, not paid, delegated and paid before due date -> nothing
 * not payable, not paid, delegated and paid partialy before due date -> penalty on balance
 * 
 * The one situation NOT considered is if many invoices get delegated to
 * a single one. This shouldn't happend becasue when an invoice is generated it will
 * inherit the previous one automaticaly
 */
public class BasicPenaltyTask extends PluggableTask implements PenaltyTask {

    public static final String PARAMETER_ITEM = "item";
    /* (non-Javadoc)
     * @see com.sapienter.jbilling.server.pluggableTask.PenaltyTask#process(com.sapienter.betty.interfaces.InvoiceEntityLocal)
     */
    public void process(Integer invoiceId) 
            throws TaskException {
        Logger log = Logger.getLogger(BasicPenaltyTask.class);
        InvoiceBL invoiceBL;
        try {
            invoiceBL = new InvoiceBL(invoiceId);
        } catch (Exception e2) {
            throw new TaskException(e2);
        }
        InvoiceEntityLocal invoice = invoiceBL.getEntity();
        Integer userId = invoice.getUser().getUserId();
        Integer languageId = invoice.getUser().getLanguageIdField();
        Integer currencyId = invoice.getCurrencyId();
        Integer entityId = invoice.getUser().getEntity().getId();

        // find the item to base the penalty on
        Integer itemId = (Integer) parameters.get(PARAMETER_ITEM);
        
        if (itemId == null) {
            throw new TaskException("An item has to be in a parameter");
        }
        
        log.debug("Processing overdue invoice " + invoiceId + ". Item = " + itemId);
        ItemBL item;
        try {
            item = new ItemBL(itemId);
        } catch (SessionInternalError e) {
            throw new TaskException ("Can not find item!");
        } catch (Exception e) {
            throw new TaskException(e);
        }
        
        // create the order 
        OrderDTO summary = new OrderDTO();
        OrderPeriodDTO period = new OrderPeriodDTO();
        period.setId(Constants.ORDER_PERIOD_ONCE);
        summary.setOrderPeriod(period);
        OrderBillingTypeDTO type = new OrderBillingTypeDTO();
        type.setId(Constants.ORDER_BILLING_PRE_PAID);
        summary.setOrderBillingType(type);
        summary.setCreateDate(Calendar.getInstance().getTime());
        summary.setCurrencyId(currencyId);
        BaseUser user = new BaseUser();
        user.setId(userId);
        summary.setBaseUserByUserId(user);
        
        try {
            
            // to add the item, we have to find the total first
            // start by getting the invoice total
            BigDecimal penaltyBase = null;
            // not delegated: pure
            if (invoice.getDelegatedInvoice() == null) {
                penaltyBase = new BigDecimal(invoice.getBalance().toString());
            } else {
                // has been delegated, the balance will be 0
                InvoiceBL newInvoice = new InvoiceBL(
                        invoice.getDelegatedInvoice());
                penaltyBase = new BigDecimal(invoice.getTotal().toString());
                penaltyBase = penaltyBase.subtract(new BigDecimal(newInvoice.getTotalPaid()));
                penaltyBase = penaltyBase.subtract(new BigDecimal(invoiceBL.getTotalPaid()));
            }
            Float fee;
            // if the item is a percentage ..
            if (item.getEntity().getPercentage() != null) {
            	penaltyBase = penaltyBase.divide(new BigDecimal("100"), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUND);
            	penaltyBase = penaltyBase.multiply(new BigDecimal(
                        item.getEntity().getPercentage().toString()));
                fee = new Float(penaltyBase.floatValue());
            } else if (penaltyBase.floatValue() > 0) {
                fee = new Float(item.getPrice(userId, 
                        currencyId, entityId).floatValue());
            } else {
                fee = new Float(0F);
            }
            
            // may be nothing is being charged
            if (fee.floatValue() < 0.01F) {
                invoice.setOverdueStep(new Integer(0));
                return;
            }
            
            // round it to the nearest cent
            fee = new Float(Util.round(fee.floatValue(), 2));
            
            // now add the item to the po
            OrderLineDTO line = new OrderLineDTO();
            line.setAmount(fee);
            // compose the description
            // The text of this line has to be i18n
            // find the locale if not there yet
            Locale locale;
            try {
                UserBL userBl = new UserBL(invoice.getUser());
                locale = userBl.getLocale();
            } catch (Exception e) {
                log.debug("Exception finding locale to add delegated invoice " +
                        "line", e);
                throw new TaskException(e);
            }

            ResourceBundle bundle = ResourceBundle.getBundle("entityNotifications", 
                    locale);
            SimpleDateFormat df = new SimpleDateFormat(
                    bundle.getString("format.date"));
            StringBuffer delLine = new StringBuffer();
            delLine.append(" - ");
            delLine.append(bundle.getString("invoice.line.delegated"));
            delLine.append(" " + invoice.getNumber() + " ");
            delLine.append(bundle.getString("invoice.line.delegated.due"));
            delLine.append(" " + df.format(invoice.getDueDate()));
 
            line.setDescription(item.getEntity().getDescription(languageId) +
                    delLine.toString());
            line.setItemId(itemId);
            line.setTypeId(Constants.ORDER_LINE_TYPE_PENALTY);
            summary.getLines().add(line);
            
            // create the db record
            OrderBL order = new OrderBL();
            order.set(summary);
            order.create(entityId, new Integer(1), summary);
            
            // and update this invoice so it's not picked up again
            invoice.setOverdueStep(new Integer(0));
        } catch (Exception e1) {
            throw new TaskException(e1);
        } 
    }

}
