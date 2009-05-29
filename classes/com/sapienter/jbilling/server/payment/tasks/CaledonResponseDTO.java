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

public class CaledonResponseDTO {

	private String text;
	private String code;
	private String auth;
	private String uid;
	private String avs;
	private String echo;
	private String warning;
	private String vbv_cavv_result;
	private String dup;
	private String iso;
	
	@CaledonParam(name="TEXT")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@CaledonParam(name="CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@CaledonParam(name="AUTH")
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	@CaledonParam(name="UID")
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@CaledonParam(name="AVS")
	public String getAvs() {
		return avs;
	}
	public void setAvs(String avs) {
		this.avs = avs;
	}
	
	@CaledonParam(name="ECHO")
	public String getEcho() {
		return echo;
	}
	public void setEcho(String echo) {
		this.echo = echo;
	}
	
	@CaledonParam(name="WARNING")
	public String getWarning() {
		return warning;
	}
	public void setWarning(String warning) {
		this.warning = warning;
	}
	
	@CaledonParam(name="VBV_CAVV_RESULT")
	public String getVbv_cavv_result() {
		return vbv_cavv_result;
	}
	public void setVbv_cavv_result(String vbv_cavv_result) {
		this.vbv_cavv_result = vbv_cavv_result;
	}
	
	@CaledonParam(name="DUP")
	public String getDup() {
		return dup;
	}
	public void setDup(String dup) {
		this.dup = dup;
	}
	
	@CaledonParam(name="ISO")
	public String getIso() {
		return iso;
	}
	public void setIso(String iso) {
		this.iso = iso;
	}
}
