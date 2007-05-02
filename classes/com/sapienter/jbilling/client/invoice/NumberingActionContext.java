package com.sapienter.jbilling.client.invoice;

public class NumberingActionContext {
    private String myPrefix;
    private String myNumber;
    
    public NumberingActionContext(String prefix, String number){
    	myNumber = number;
    	myPrefix = prefix;
    }
    
    public String getNumber() {
		return myNumber;
	}
    
    public String getPrefix() {
		return myPrefix;
	}
}
