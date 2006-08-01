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
 * Created on Jun 18, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.pluggableTask;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.PaperInvoiceBatchEntityLocal;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.invoice.InvoiceDTOEx;
import com.sapienter.jbilling.server.invoice.PaperInvoiceBatchBL;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.ContactDTOEx;

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
	
	private void init(UserEntityLocal user, MessageDTO message) 
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
    public void deliver(UserEntityLocal user, MessageDTO message)
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
    
    public byte[] getPDF(UserEntityLocal user, MessageDTO message)
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
    
    public String getPDFFile(UserEntityLocal user, MessageDTO message)
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
