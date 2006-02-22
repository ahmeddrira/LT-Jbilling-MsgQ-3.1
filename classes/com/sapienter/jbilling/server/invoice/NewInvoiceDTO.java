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
 * Created on 20-Apr-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.invoice;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.OrderEntityLocal;
import com.sapienter.jbilling.server.entity.InvoiceDTO;
import com.sapienter.jbilling.server.order.TimePeriod;

public class NewInvoiceDTO extends InvoiceDTO {
	private Vector orders= null;
    private Vector invoices= null;
    private Vector resultLines = null;
    private Vector startPeriods = null;
    private Vector endPeriods = null;
    private Vector periodsCount = null;
    private Integer entityId = null;
    private Date billingDate = null;
    private TimePeriod dueDatePeriod = null;
    boolean dateIsRecurring;
    

    public NewInvoiceDTO() {
        orders = new Vector();
        invoices = new Vector();
        resultLines = new Vector();
        startPeriods = new Vector();
        endPeriods = new Vector();
        periodsCount = new Vector();
        Logger.getLogger(NewInvoiceDTO.class).debug("New invoice object with date = " + billingDate);
    }
    
    public void setDate(Date newDate) {
        billingDate = newDate;
    }
    
    /**
     * Use the earliest day, with priority to recurring orders
     * Used only for the parameter invoice date = begining of period invoiced
     * @param newDate
     * @param isRecurring
     */
    public void setDate(Date newDate, boolean isRecurring) {
        if (billingDate == null) {
            billingDate = newDate;
            dateIsRecurring = isRecurring;
        } else if (dateIsRecurring) {
            if (newDate.before(billingDate) && isRecurring) {
                billingDate = newDate;
            }
        } else {
            if (!isRecurring && billingDate.before(newDate)) {
            } else {
                billingDate = newDate;
                dateIsRecurring = isRecurring;
            }
        }
    }
    
    public void addOrder(OrderEntityLocal order, Date start, Date end,
            int periods) throws SessionInternalError {
    	Logger.getLogger(NewInvoiceDTO.class).debug("Adding order " + 
                order.getId() + " to new invoice");
        orders.add(order);
        if (start != null && end != null && start.after(end)) {
            // how come it starts after it ends ???
            throw new SessionInternalError("Adding " +
                    "order " + order.getId() + " with a period that" 
                    + " starts after it ends:" + start + " " +
                    end);
        }        
        startPeriods.add(start);
        endPeriods.add(end);
        periodsCount.add(new Integer(periods));
    }
    
    public void addInvoice(InvoiceDTO line) {
        invoices.add(line);
    }
    
    public Vector getOrders() {
    	return orders;
    }
    
    public Vector getStarts() {
        return startPeriods;
    }
    
    public Vector getEnds() {
        return endPeriods;
    }
    
    public Vector getInvoices() {
    	return invoices;
    }
    
	public Vector getResultLines() {
		return resultLines;
	}
	
	public void addResultLine(InvoiceLineDTOEx line) {
		resultLines.add(line);
	}
	
	/**
	 * 
	 * @return If this object holds any order lines or invoice lines,
	 * therefore if it makes sense to apply invoice composition tasks to it.
	 */
	public boolean isEmpty() {
		return orders.isEmpty() && invoices.isEmpty();
	}
	
	/**
     * @return If after the invoice composition tasks lines have
     * been inserted in the resultLines vector.
     */
    public boolean areLinesGeneratedEmpty() {
		return resultLines.isEmpty();
	}
	
	public String validate() {
		String message = null;
		
		if (getDueDate() == null) {
            // due date is mandaroty
			message = "Due date is null";
		} else if (getDueDate().before(getBillingDate())) {
            // the due date has to be after the invoice's billing date
            message = "Due date has to be past the billing date";
		}
		
		return message;
	}
    /**
     * @return
     */
    public Date getBillingDate() {
        return billingDate;
    }

    /**
     * @param date
     */
    public void setBillingDate(Date date) {
        billingDate = date;
    }
    
    public void calculateTotal() {
        Iterator lines = resultLines.iterator();
        float total = 0F;
        while (lines.hasNext()) {
            InvoiceLineDTOEx line = (InvoiceLineDTOEx) lines.next();
            total += line.getAmount().floatValue();
        }
        
        setTotal(new Float(total));
    }

    /**
     * @return
     */
    public Vector getPeriodsCount() {
        return periodsCount;
    }

    /**
     * @return Returns the entityId.
     */
    public Integer getEntityId() {
        return entityId;
    }
    /**
     * @param entityId The entityId to set.
     */
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }
    public TimePeriod getDueDatePeriod() {
        return dueDatePeriod;
    }
    public void setDueDatePeriod(TimePeriod dueDatePeriod) {
        this.dueDatePeriod = dueDatePeriod;
    }
}
