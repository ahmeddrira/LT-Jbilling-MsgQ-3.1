/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.payment.tasks;

public enum CaledonRequestType {
	
	NONE(""), ALL("*"), SALE("S"), AUTH_ONLY("A"), VOID("V"), PREAUTH("P"), 
	RETURN("R"), RETURN_VOID("M"), FORCE_POST("F"), COMPLETION("C"), BALANCE_REQ("B"), 
	SETTLEMENT("D"), DISCARD_BATCH("T"), CONTRA_ADD("+"), CONTRA_DELETE("-"), 
	CONTRA_QUERY("Q"), LEVEL_3_DETAIL("L"),	CANADIAN_AVS("I"), COMMERCIAL_CARD("K"), 
	DEBIT("Y");
	
	private String code;
	
	private CaledonRequestType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}