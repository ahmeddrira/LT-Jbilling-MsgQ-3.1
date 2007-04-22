package com.sapienter.jbilling.client.user;

import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.entity.CreditCardDTO;


public class PaymentMethodCrudContext<DTO> {
	private final DTO myDto;
	private Boolean myIsAutomaticPayment;
	
	public PaymentMethodCrudContext(DTO dto){
		myDto = dto;
	}

	public void setIsAutomaticPayment(boolean value){
		myIsAutomaticPayment = Boolean.valueOf(value);
	}
	
	public Boolean isAutomaticPayment() {
		return myIsAutomaticPayment;
	}
	
	public DTO getDto(){
		return myDto;
	}

	//just aliases 
	public static class CCContext extends PaymentMethodCrudContext<CreditCardDTO> {
		public CCContext(CreditCardDTO dto) {
			super(dto);
		}
	}
	
	public static class AchContext extends PaymentMethodCrudContext<AchDTO> {
		public AchContext(AchDTO dto){
			super(dto);
		}
	}
	
}
