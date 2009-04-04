package com.sapienter.jbilling.server.payment.db;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.sapienter.jbilling.server.invoice.db.InvoiceDAS;
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO;
import com.sapienter.jbilling.server.util.db.AbstractDAS;

/**
 * 
 * @author abimael
 * 
 */
public class PaymentInvoiceMapDAS extends AbstractDAS<PaymentInvoiceMapDTO> {

	public PaymentInvoiceMapDTO create(InvoiceDTO invoice,
			PaymentDTO payment, Float realAmount) {
		PaymentInvoiceMapDTO map = new PaymentInvoiceMapDTO();
		map.setInvoiceEntity(invoice);
		map.setPayment(payment);
		map.setAmount(realAmount.floatValue());
		map.setCreateDatetime(Calendar.getInstance().getTime());
		return save(map);
	}

	public void deleteAllWithInvoice(InvoiceDTO invoice) {
		
		InvoiceDTO inv = new InvoiceDAS().find(invoice.getId());
		Criteria criteria = getSession().createCriteria(
				PaymentInvoiceMapDTO.class);
		criteria.add(Restrictions.eq("invoiceEntity", inv));

		List<PaymentInvoiceMapDTO> results = criteria.list();

		if (results != null && !results.isEmpty()) {
			for (PaymentInvoiceMapDTO paym : results) {
				delete(paym);
			}
		} else {
		}
	}

}
