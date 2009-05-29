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

import java.text.NumberFormat;
import java.text.ParseException;

import org.apache.commons.httpclient.util.Base64;

public class CaledonRequestDTO {
	
	private String termid;
	private String air;
	private String amt;
	private String auth;
	private String avs;
	private String card;
	private String cvv2;
	private String desc;
	private String echo;
	private String exp;
	private String iso;
	private String l2;
	private String l3;
	private String operid;
	private String pass;
	private String ref;
	private String resend;
	private String seq;
	private String showdup;
	private String swver;
	private String termtype;
	private String track;
	private String track1;
	private String track2;
	private String type;
	private String uid;
	private String vbv_cavv;
	private String vbv_status;
	private String vbv_xid;
	
	@CaledonParam(name="TERMID", mandatoryCodes="*", optionalCodes="", 
			position=0, pattern="[A-Za-z0-9]{8}")
	public String getTermid() {
		return termid;
	}
	public void setTermid(String termid) {
		this.termid = termid;
	}
	
	@CaledonParam(name="AIR", optionalCodes="SFRC", pattern=".{1,147}")
	public String getAir() {
		return air;
	}
	public void setAir(String air) {
		this.air = air;
	}
	
	@CaledonParam(name="AMT", mandatoryCodes="SVFRMAPCY", pattern="[0-9]{1,7}")
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	
	public double getAmount() {
		double result;
		try {
			result = NumberFormat.getIntegerInstance().parse(this.amt).doubleValue() / 100;
		} catch (ParseException e) {
			result = 0;
		}
		return result;
	}
	public void setAmount(double amount) {
		if (amount <= 0) {
			amt = "0";
		} else {
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setGroupingUsed(false);
			amt = nf.format(amount * 100);
		}
	}
	
	@CaledonParam(name="AUTH", mandatoryCodes="FY", pattern="[A-Za-z0-9]{2,6}")
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	@CaledonParam(name="AVS", mandatoryCodes="I", optionalCodes="SRAP", 
			pattern="[A-Za-z0-9 -]{1,237}")
	public String getAvs() {
		return avs;
	}
	public void setAvs(String avs) {
		this.avs = avs;
	}
	
	@CaledonParam(name="CARD", mandatoryCodes="SFRMAP+-QIKY", optionalCodes="VC",
			pattern="(0|([0-9]{9,25}))")
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	
	@CaledonParam(name="CVV2", optionalCodes="SRAP", pattern="[0-9]{1,4}")
	public String getCvv2() {
		return cvv2;
	}
	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}
	
	@CaledonParam(name="DESC", optionalCodes="SFRC", pattern=".{1,25}")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@CaledonParam(name="ECHO", optionalCodes="*", pattern=".{1,60}")
	public String getEcho() {
		return echo;
	}
	public void setEcho(String echo) {
		this.echo = echo;
	}
	
	@CaledonParam(name="EXP", mandatoryCodes="SFRMAPC", optionalCodes="VC",
			pattern="[0-9]{1,4}")
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	
	@CaledonParam(name="ISO", mandatoryCodes="Y")
	public String getIso() {
		return iso;
	}
	public void setIso(String iso) {
		this.iso = iso;
	}
	
	@CaledonParam(name="L2", optionalCodes="SFRC")
	public String getL2() {
		return l2;
	}
	public void setL2(String l2) {
		this.l2 = l2;
	}
	
	@CaledonParam(name="L3", mandatoryCodes="L")
	public String getL3() {
		return l3;
	}
	public void setL3(String l3) {
		this.l3 = l3;
	}
	
	@CaledonParam(name="OPERID", optionalCodes="*", pattern="[A-Za-z0-9]{1,3}")
	public String getOperid() {
		return operid;
	}
	public void setOperid(String operid) {
		this.operid = operid;
	}
	
	@CaledonParam(name="PASS", optionalCodes="*", pattern="[A-Za-z0-9]{1,16}")
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	@CaledonParam(name="REF", mandatoryCodes="SVFRMAPCLY", pattern="[A-Za-z0-9/-]{1,60}")
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	@CaledonParam(name="RESEND", optionalCodes="SVFRMAPC+-LY", pattern="[YN]")
	public String getResend() {
		return resend;
	}
	public void setResend(String resend) {
		this.resend = resend;
	}
	
	@CaledonParam(name="SEQ", optionalCodes="L", pattern="[0-9]{1,3}")
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	
	@CaledonParam(name="SHOWDUP", optionalCodes="SVFRMAPC+-LY", pattern="[YN]")
	public String getShowdup() {
		return showdup;
	}
	public void setShowdup(String showdup) {
		this.showdup = showdup;
	}
	
	@CaledonParam(name="SWVER", optionalCodes="*", pattern="[^ ]*")
	public String getSwver() {
		return swver;
	}
	public void setSwver(String swver) {
		this.swver = swver;
	}
	
	@CaledonParam(name="TERMTYPE", optionalCodes="*", pattern="[^ ]*")
	public String getTermtype() {
		return termtype;
	}
	public void setTermtype(String termtype) {
		this.termtype = termtype;
	}
	
	@CaledonParam(name="TRACK", mandatoryCodes="SVFRMAPC")
	public String getTrack() {
		return track;
	}
	public void setTrack(String track) {
		this.track = track;
	}
	
	@CaledonParam(name="TRACK1", mandatoryCodes="SVFRMAPC")
	public String getTrack1() {
		return track1;
	}
	public void setTrack1(String track1) {
		this.track1 = track1;
	}
	
	@CaledonParam(name="TRACK2", mandatoryCodes="SVFRMAPC")
	public String getTrack2() {
		return track2;
	}
	public void setTrack2(String track2) {
		this.track2 = track2;
	}
	
	public CaledonRequestType getType() {
		if (this.type == null) {
			return null;
		}
		CaledonRequestType[] types = CaledonRequestType.values();
		for (int i = 0; i < types.length; i++) {
			if (types[i].getCode().equals(this.type))
				return types[i];
		}
		return null;
	}
	public void setType(CaledonRequestType type) {
		this.type = type.getCode();
	}
	
	@CaledonParam(name="TYPE", mandatoryCodes="*", pattern="[SAVPRMFCBDT+-QLIKY]")
	public String getTypeValue() {
		return type;
	}
	public void setTypeValue(String type) {
		this.type = type;
	}
	
	@CaledonParam(name="UID", mandatoryCodes="L", pattern="[0-9]{1,7}")
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@CaledonParam(name="VBV_CAVV", optionalCodes="*", pattern=".{27,28}")
	public String getVbv_cavv() {
		if (vbv_cavv == null) {
			return null;
		}
		return new String(Base64.decode(vbv_cavv.getBytes()));
	}
	public void setVbv_cavv(String vbv_cavv) {
		if (vbv_cavv == null) {
			this.vbv_cavv = null;
		} else {
			this.vbv_cavv = new String(Base64.encode(vbv_cavv.getBytes()));
		}
	}
	
	@CaledonParam(name="VBV_STATUS", optionalCodes="*", pattern="[YA]")
	public String getVbv_status() {
		return vbv_status;
	}
	public void setVbv_status(String vbv_status) {
		this.vbv_status = vbv_status;
	}
	
	@CaledonParam(name="VBV_XID", optionalCodes="*", pattern=".{27,28}")
	public String getVbv_xid() {
		if (vbv_xid == null) {
			return null;
		}
		return new String(Base64.decode(vbv_xid.getBytes()));
	}
	public void setVbv_xid(String vbv_xid) {
		if (vbv_xid == null) {
			this.vbv_xid = null;
		} else {
			this.vbv_xid = new String(Base64.encode(vbv_xid.getBytes()));
		}
	}
}
