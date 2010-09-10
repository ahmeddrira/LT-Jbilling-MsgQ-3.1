package com.sapienter.jbilling.server.util;

public interface IWebServicesCaller {

	public void setCallerId(int callerId);

	public void setCallerCompanyId(int callerCompanyId);

	public void setCallerUserName(String callerUserName);

	public String getCallerUserName();

	public int getCallerId();

	public int getCallerCompanyId();

}