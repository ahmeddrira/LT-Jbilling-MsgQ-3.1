package com.sapienter.jbilling.client.user;

import com.sapienter.jbilling.server.entity.CreditCardDTO;

public class CreditCardCrudContext {
	private final CreditCardDTO myDto;
	private Boolean myIsAutomaticPayment;

	public CreditCardCrudContext(CreditCardDTO dto){
		myDto = dto;
	}
	
	public void setIsAutomaticPayment(boolean value){
		myIsAutomaticPayment = Boolean.valueOf(value);
	}
	
	public Boolean isAutomaticPayment() {
		return myIsAutomaticPayment;
	}
	
	public CreditCardDTO getDto() {
		return myDto;
	}

}
