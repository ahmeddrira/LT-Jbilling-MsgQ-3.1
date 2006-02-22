/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/

/*
 * Created on 27-Apr-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.pluggableTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.OrderEntityLocal;
import com.sapienter.jbilling.interfaces.OrderLineEntityLocal;
import com.sapienter.jbilling.server.entity.InvoiceDTO;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.invoice.InvoiceLineDTOEx;
import com.sapienter.jbilling.server.invoice.NewInvoiceDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.MapPeriodToCalendar;
import com.sapienter.jbilling.server.util.PreferenceBL;
import com.sapienter.jbilling.server.util.Util;

/**
 * This task will copy all the lines on the orders and invoices
 * to the new invoice, considering the periods involved for each
 * order, but not the fractions of perios. It will not copy the
 * lines that are taxes.
 * The quantity and total of each line will be multiplied by the
 * amount of periods.
 * @author Emil
 */
public class BasicCompositionTask extends PluggableTask
        implements InvoiceCompositionTask {

    // this could be a pluggable task parameter if some entities require
    // more digits
    private final int DECIMAL_DIGITS = 2;
    
    private Locale locale = null;
    /* (non-Javadoc)
     * @see com.sapienter.jbilling.server.pluggableTask.InvoiceCompositionTask#apply(com.sapienter.betty.server.invoice.NewInvoiceDTO, com.sapienter.betty.server.entity.BillingProcessDTO)
     */
    public void apply(NewInvoiceDTO invoiceDTO, Integer userId)
            throws TaskException {
        /*
         *  go over the orders first
         */
        Logger log = Logger.getLogger(BasicCompositionTask.class);
        
        for (int orderIndex=0; orderIndex < invoiceDTO.getOrders().size(); orderIndex++) {
            OrderEntityLocal order = (OrderEntityLocal) invoiceDTO.
                    getOrders().get(orderIndex);
            // check if this order has notes that should make it into the invoice
            if (order.getNotesInInvoice() != null &&
                    order.getNotesInInvoice().intValue() == 1 &&
                    order.getNotes() != null && order.getNotes().length() > 0) {
                 if (invoiceDTO.getCustomerNotes() == null) {
                    invoiceDTO.setCustomerNotes(new String());
                 }
                 invoiceDTO.setCustomerNotes(invoiceDTO.getCustomerNotes() + 
                         " " + order.getNotes());
            }
            // get the amount of periods this order will be billed
            int periods = ((Integer) invoiceDTO.getPeriodsCount().get(orderIndex)).intValue();
            
            // now go over the lines of this order, and generate the invoice
            // lines from them, excluding the tax ones.
            Iterator orderLines = order.getOrderLines().iterator();
            while (orderLines.hasNext()) {
                OrderLineEntityLocal orderLine = 
                        (OrderLineEntityLocal) orderLines.next();
                // skip deleted lines
                if (orderLine.getDeleted().intValue() == 1) {
                    continue;
                }
                for (int period = 1; period <= periods; period++) {
                    log.debug("Adding order line from " + order.getId() +
                            " quantity " + orderLine.getQuantity() +
                            " period " + period + " price " + orderLine.getPrice());
                    // this would probably have to exlude taxes, calculate
                    // interests, then recalculate taxes, etc...
                    // now the whole orders is just copied. 
                    InvoiceLineDTOEx invoiceLine = null;
                    if (orderLine.getType().getId().equals(
                            Constants.ORDER_LINE_TYPE_ITEM)) {
                        String desc;
                        try {
                            desc = composeDescription(userId,  order, 
                                    orderLine.getDescription(), period,
                                    (Date) invoiceDTO.getStarts().get(orderIndex));
                        } catch (SessionInternalError e) {
                            throw new TaskException(e);
                        }
                        Integer type;
                        if (userId.equals(order.getUser().getUserId())) {
                            type = Constants.INVOICE_LINE_TYPE_ITEM;
                        } else {
                            type = Constants.INVOICE_LINE_TYPE_SUB_ACCOUNT;
                        }
                        invoiceLine = new InvoiceLineDTOEx(null, desc,
                                orderLine.getAmount(), 
                                orderLine.getPrice(), orderLine.getQuantity(),
                                type, new Integer(0), orderLine.getItemId(),
                                order.getUser().getUserId());
                        
                    } else if (orderLine.getType().getId().equals(
                            Constants.ORDER_LINE_TYPE_TAX)){
                        // tax lines have to be consolidated
                        int taxLine = taxLinePresent(invoiceDTO.getResultLines(), 
                                orderLine.getDescription());
                        if (taxLine >= 0) {
                            // we have this tax already: add up the total
                            invoiceLine = (InvoiceLineDTOEx) invoiceDTO.
                                    getResultLines().get(taxLine);
                            invoiceLine.setAmount(new Float(invoiceLine.
                                    getAmount().floatValue() +
                                    orderLine.getAmount().floatValue()));
                            continue;
                        } 
                        // it is not there yet: add
                        invoiceLine = new InvoiceLineDTOEx(null,
                                orderLine.getDescription(),
                                new Float(orderLine.getAmount().floatValue()), 
                                orderLine.getPrice(), 
                                null, Constants.INVOICE_LINE_TYPE_TAX, 
                                new Integer(0), orderLine.getItemId(),
                                order.getUser().getUserId());
                    } else if (orderLine.getType().getId().equals(
                            Constants.ORDER_LINE_TYPE_PENALTY)){
                        invoiceLine = new InvoiceLineDTOEx(null, 
                                orderLine.getDescription(),
                                orderLine.getAmount(),
                                null, null,
                                Constants.INVOICE_LINE_TYPE_PENALTY, 
                                new Integer(0), orderLine.getItemId(),
                                order.getUser().getUserId());
                    }
                    
                    // for the invoice to make sense when it is displayed,
                    // each line has to be rounded to the decimals shown
                    invoiceLine.setAmount(new Float(Util.round(
                            invoiceLine.getAmount().floatValue(), 
                            DECIMAL_DIGITS)));
                    invoiceDTO.addResultLine(invoiceLine);
                }
            }
        }
        
        /*
         * now the invoices
         */
        Iterator invoices = invoiceDTO.getInvoices().iterator();
        while (invoices.hasNext()) {
            InvoiceDTO invoice = (InvoiceDTO) invoices.next();
            // the whole invoice will be added as a single line
            // this will probably will have a good deal of logic, to 
            // take the taxes out, etc ...
            
            // The text of this line has to be i18n
            // find the locale if not there yet
            try {
                if (locale == null) {
                    InvoiceBL bl = new InvoiceBL(invoice.getId());
                    UserBL user = new UserBL(bl.getEntity().getUser());
                    locale = user.getLocale();
                }
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
            delLine.append(bundle.getString("invoice.line.delegated"));
            delLine.append(" " + invoice.getNumber() + " ");
            delLine.append(bundle.getString("invoice.line.delegated.due"));
            delLine.append(" " + df.format(invoice.getDueDate()));
            
            InvoiceLineDTOEx invoiceLine = new InvoiceLineDTOEx(null, 
                    delLine.toString(), invoice.getBalance(), null, null, 
                    Constants.INVOICE_LINE_TYPE_DUE_INVOICE, new Integer(0),
                    null, null);
            invoiceDTO.addResultLine(invoiceLine);
        }
    }
    
    private String composeDescription(Integer userId,
            OrderEntityLocal order, String desc,
            int period, Date start) throws SessionInternalError{
        StringBuffer retValue = new StringBuffer();
        
        retValue.append(desc);
        
        if (!order.getPeriod().getId().equals(Constants.ORDER_PERIOD_ONCE)) {
            Integer orderUnitId = order.getPeriod().getUnitId();
            Integer orderValueId = order.getPeriod().getValue();
            // calculate the dates from/to
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            if (period > 1) {
                cal.add(MapPeriodToCalendar.map(order.getPeriod().getUnitId()), 
                        (period - 1) * orderValueId.intValue());
            }
            Date from = cal.getTime();
            cal.add(MapPeriodToCalendar.map(order.getPeriod().getUnitId()), 
                    1 * orderValueId.intValue());
            // take one day from the end date, so both dates are in the range
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date to = cal.getTime();
            
            // now add this to the line
            if (locale == null) {
                try {
                    UserBL user = new UserBL(order.getUser());
                    locale = user.getLocale();
                } catch (Exception e) {
                    throw new SessionInternalError(e);
                }
            }
            ResourceBundle bundle = ResourceBundle.getBundle("entityNotifications", 
                    locale);

            Logger.getLogger(BasicCompositionTask.class).debug(
                    "Composing for period " + period + " from " + from +
                    " to " + to + " format:" + bundle.getString("format.date"));
           
            retValue.append(" ");
            retValue.append(bundle.getString("invoice.line.period"));
            retValue.append(" ");
            SimpleDateFormat df = new SimpleDateFormat(
                    bundle.getString("format.date"));
            retValue.append(df.format(from));
            retValue.append(" ");
            retValue.append(bundle.getString("invoice.line.to"));
            retValue.append(" ");
            retValue.append(df.format(to));
            
        }

        // if the entity wants, add the order ID to the end of the line
        PreferenceBL pref = null; 
        try {
            pref = new PreferenceBL();
            pref.set(order.getUser().getEntity().getId(), 
                    Constants.PREFERENCE_ORDER_IN_INVOICE_LINE);
        } catch (Exception e) { // the default is good 
        } 

        if (pref.getInt() == 1) {
            // get the string from the i18n file
            if (locale == null) {
                try {
                    UserBL user = new UserBL(order.getUser());
                    locale = user.getLocale();
                } catch (Exception e) {
                    throw new SessionInternalError(e);
                }
            }
            ResourceBundle bundle = ResourceBundle.getBundle("entityNotifications", 
                    locale);
            retValue.append(bundle.getString("invoice.line.orderNumber"));
            retValue.append(" ");
            retValue.append(order.getId().toString());
       }


        return retValue.toString();
    }

    private int taxLinePresent(Vector lines, String desc) {
        for (int f = 0; f < lines.size(); f++) {
            InvoiceLineDTOEx line = (InvoiceLineDTOEx) lines.get(f);
            if (line.getTypeId().equals(Constants.ORDER_LINE_TYPE_TAX)) {
                if (line.getDescription().equals(desc)) {
                    return f;
                }
            }
        }
        
        return -1;
    }
}
