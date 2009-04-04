package com.sapienter.jbilling.server.payment.db;

import java.util.Calendar;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

/**
 * 
 * @author abimael
 *
 */
public class PaymentAuthorizationDAS extends AbstractDAS<PaymentAuthorizationDTO> {

	public PaymentAuthorizationDTO create(String processor, String code1) {
		
		PaymentAuthorizationDTO auto = new PaymentAuthorizationDTO();
		auto.setProcessor(processor);
		auto.setCode1(code1);
		auto.setCreateDate(Calendar.getInstance().getTime());
		
		return save(auto);
	}

}
