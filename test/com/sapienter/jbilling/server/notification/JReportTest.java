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
 * Created on Jun 28, 2004
 *
 */
package com.sapienter.jbilling.server.notification;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import junit.framework.TestCase;

import com.sapienter.jbilling.server.entity.ContactDTO;
import com.sapienter.jbilling.server.entity.InvoiceLineDTO;
import com.sapienter.jbilling.server.invoice.InvoiceDTOEx;

/**
 * @author Emil
 *
 */
public class JReportTest extends TestCase {

	public void testGeneratePaperInvoice() {
		try {
			InvoiceDTOEx invoice = new InvoiceDTOEx();
			invoice.setId(new Integer(2910));
			GregorianCalendar cal = new GregorianCalendar();
			invoice.setCreateDateTime(cal.getTime());
			cal.add(Calendar.DATE, 10);
			invoice.setDueDate(cal.getTime());
				
			Vector lines = new Vector();
			lines.add(new InvoiceLineDTO(new Integer(1), "10 classes of yoga", new Float(105.383838834), 
					new Float(10.532342342), new Integer(10), null, null));
			lines.add(new InvoiceLineDTO(new Integer(1), "suntaning ", new Float(100), 
					new Float(10), new Integer(10), null, null));
			lines.add(new InvoiceLineDTO(new Integer(1), "Late payment fee ", new Float(100), 
					null, null, null, null));

			invoice.setInvoiceLines(lines);
			
			ContactDTO to, from;
			to = new ContactDTO();
			to.setAddress1("3423 Surrey Rd.");
			to.setCity("Vancouver");
			to.setStateProvince("BC");
			to.setPostalCode("V4S 3E4");
			to.setFirstName("Isabelle");
			to.setLastName("Oppenheim");
			
			from = new ContactDTO();
			from.setOrganizationName("Natural Green Landscaping");
			from.setAddress1("345 Brooks Av.");
			from.setCity("Burnaby");
			from.setStateProvince("BC");
			from.setPostalCode("V5Q 3E4");
			
			
			NotificationBL.generatePaperInvoiceAsFile("simple_invoice", invoice, from, to, 
					"Take advantage of our new promotion! Two for the price of one until October 31st.",
					"Thank you for your business!", new Integer(1));
			System.out.println("Done");
		} catch(Exception e) {
			e.printStackTrace();
			fail("Exception:" + e.getMessage());
		}
	}
}
