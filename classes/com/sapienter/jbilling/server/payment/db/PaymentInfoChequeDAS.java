package com.sapienter.jbilling.server.payment.db;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

/**
 * 
 * @author abimael
 *
 */
public class PaymentInfoChequeDAS extends AbstractDAS<PaymentInfoChequeDTO> {

	public PaymentInfoChequeDTO create() {
		
		return new PaymentInfoChequeDTO();
	}
	
	public PaymentInfoChequeDTO findByPayment(PaymentDTO payment) {
		Criteria criteria = getSession().createCriteria(PaymentInfoChequeDTO.class);
		criteria.add(Restrictions.eq("payment", payment));
		return (PaymentInfoChequeDTO) criteria.uniqueResult();
	}

}
