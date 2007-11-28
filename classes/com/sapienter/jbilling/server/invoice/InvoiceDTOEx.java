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
 * Created on 10-May-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.invoice;

import java.util.Vector;

import com.sapienter.jbilling.server.entity.InvoiceDTO;
import com.sapienter.jbilling.server.payment.PaymentInvoiceMapDTOEx;

/**
 * @author Emil
 */
public class InvoiceDTOEx extends InvoiceDTO {

    private Integer delegatedInvoiceId = null;
    private Vector paymentMap = null;
    private Integer userId = null;
    private Vector invoiceLines = null;
    private Vector orders = null; //array of orderDto (not Ex)
    private String currencyName = null;
    private String currencySymbol = null;
    private Integer invoicesIncluded[] = null;
    
    public InvoiceDTOEx() {
    	super();
    }
    /**
     * @param id
     * @param createDateTime
     * @param billingProcessId
     * @param dueDate
     * @param total
     * @param toProcess
     * @param deleted
     * @param paymentAttempts
     */
    public InvoiceDTOEx(InvoiceDTO dto, Integer delegatedInvoiceId) {
        super(dto);
        this.delegatedInvoiceId = delegatedInvoiceId;
        paymentMap = new Vector();
        invoiceLines = new Vector();
        orders = new Vector();
    }

    /**
     * @param otherValue
     */
    public InvoiceDTOEx(InvoiceDTO otherValue) {
        super(otherValue);
    }

    /**
     * @return
     */
    public Integer getDelegatedInvoiceId() {
        return delegatedInvoiceId;
    }

    /**
     * @param integer
     */
    public void setDelegatedInvoiceId(Integer integer) {
        delegatedInvoiceId = integer;
    }

    public void addPayment(PaymentInvoiceMapDTOEx payment) {
        paymentMap.add(payment);
    }
    
    public Vector getPaymentMap() {
        return paymentMap;
    }
    /**
     * @return
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param integer
     */
    public void setUserId(Integer integer) {
        userId = integer;
    }

    /**
     * @return
     */
    public Vector getInvoiceLines() {
        return invoiceLines;
    }

    /**
     * @param invoiceLines
     */
    public void setInvoiceLines(Vector invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    /**
     * @return
     */
    public Vector getOrders() {
        return orders;
    }

    /**
     * @param orders
     */
    public void setOrders(Vector orders) {
        this.orders = orders;
    }

    /**
     * @return
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * @param currencyName
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    /**
     * @return
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    /**
     * @param currencySymbol
     */
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    /*
     * This are methods required by JasperReports to get the invoice lines
     * into a paper invoice. This bean acts as a 'BeanFactory' of invoice
     * line beans. 
     
	public static Object[] getBeanArray() {
		return invoiceLines.toArray();
	}

	public static Collection getBeanCollection() {
		return Arrays.asList(data);
	}
	*/
    public Integer[] getInvoicesIncluded() {
        return invoicesIncluded;
    }
    public void setInvoicesIncluded(Integer[] invoicesIncluded) {
        this.invoicesIncluded = invoicesIncluded;
    }
}
