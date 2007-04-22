package com.sapienter.jbilling.client.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.servlet.http.HttpSession;

import com.sapienter.jbilling.server.user.UserDTOEx;

public class FormHelper {
	private final HttpSession mySession;

	public FormHelper(HttpSession session) {
		mySession = session;
	}

	public String float2string(Float arg) {
		return float2string(arg, mySession);
	}

	public Float string2float(String arg) {
		return string2float(arg, mySession);
	}
	
	public static Float string2float(String arg, HttpSession sess) {
		if (arg == null || arg.trim().length() == 0) {
			return null;
		}
		UserDTOEx user = (UserDTOEx) sess.getAttribute(Constants.SESSION_USER_DTO);
		NumberFormat nf = NumberFormat.getInstance(user.getLocale());

		try {
			return new Float(nf.parse(arg).floatValue());
		} catch (ParseException e) {
			return null;
		}
	}

	public static String float2string(Float arg, HttpSession sess) {
		if (arg == null) {
			return null;
		}
		UserDTOEx user = (UserDTOEx) sess
				.getAttribute(Constants.SESSION_USER_DTO);
		NumberFormat nf = NumberFormat.getInstance(user.getLocale());
		if (nf instanceof DecimalFormat) {
			((DecimalFormat) nf).applyPattern("0.00");
		}
		return nf.format(arg);
	}

}
