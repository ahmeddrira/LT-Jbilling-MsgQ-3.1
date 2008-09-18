/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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
 * Created on Jun 18, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.pluggableTask;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.PaperInvoiceBatchEntityLocal;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.invoice.InvoiceDTOEx;
import com.sapienter.jbilling.server.invoice.PaperInvoiceBatchBL;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.ContactDTOEx;
import com.sapienter.jbilling.server.user.db.UserDTO;

/**
 * @author Emil
 */
public class PaperInvoiceNotificationTask
        extends PluggableTask implements NotificationTask {
    // pluggable task parameters names
    public static final String PARAMETER_DESIGN = "design";
	private String design;
	private ContactBL contact;
	private ContactDTOEx to;
	private Integer entityId;
	private InvoiceDTOEx invoice;
	private ContactDTOEx from;

    /* (non-Javadoc)
     * @see com.sapienter.jbilling.server.pluggableTask.NotificationTask#deliver(com.sapienter.betty.interfaces.UserEntityLocal, com.sapienter.betty.server.notification.MessageDTO)
     */
	
	private void init(UserDTO user, MessageDTO message) 
			throws TaskException{
        design = (String) parameters.get(PARAMETER_DESIGN);
		invoice = (InvoiceDTOEx) message.getParameters().get(
		        		"invoiceDto");
		try {
	        contact = new ContactBL();
	        contact.setInvoice(invoice.getId());
	        to = contact.getDTO();
			entityId = user.getEntity().getId();
			contact.setEntity(entityId);
	        from = contact.getDTO();
        } catch (Exception e) {
            throw new TaskException(e);
        }
	}
    public void deliver(UserDTO user, MessageDTO message)
            throws TaskException {
        if (!message.getTypeId().equals(MessageDTO.TYPE_INVOICE_PAPER)) {
            // this task is only to notify about invoices
            return;
        }
		try {
			init(user, message);
        	NotificationBL.generatePaperInvoiceAsFile(design, invoice, from, to, 
        			message.getContent()[0].getContent(), 
					message.getContent()[1].getContent(), entityId,
                    user.getUserName(), user.getPassword());
        	// update the batch record
        	Integer processId = (Integer) message.getParameters().get(
    				"processId");
        	PaperInvoiceBatchBL batchBL = new PaperInvoiceBatchBL();
        	PaperInvoiceBatchEntityLocal record = batchBL.createGet(processId);
        	record.setTotalInvoices(new Integer(record.getTotalInvoices().
        			intValue() + 1));
        	// link the batch to this invoice
        	InvoiceBL invoiceBL = new InvoiceBL(invoice.getId());
        	record.getInvoices().add(invoiceBL.getEntity());
        } catch (Exception e) {
            throw new TaskException(e);
        }
    }
    
    public byte[] getPDF(UserDTO user, MessageDTO message)
    		throws SessionInternalError {
    	try {
    		init(user, message);
    		Logger.getLogger(PaperInvoiceNotificationTask.class).debug("now message1 = " + message.getContent()[0].getContent());
        	return NotificationBL.generatePaperInvoiceAsStream(design, invoice, from, to, 
        			message.getContent()[0].getContent(), 
					message.getContent()[1].getContent(), entityId,
                    user.getUserName(), user.getPassword());
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    public String getPDFFile(UserDTO user, MessageDTO message)
            throws SessionInternalError {
        try {
            init(user, message);
            return NotificationBL.generatePaperInvoiceAsFile(design, invoice, from, to, 
                    message.getContent()[0].getContent(), 
                    message.getContent()[1].getContent(), entityId,
                    user.getUserName(), user.getPassword());
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
}
