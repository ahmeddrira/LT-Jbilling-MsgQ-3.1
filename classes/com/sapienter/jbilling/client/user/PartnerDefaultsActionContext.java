package com.sapienter.jbilling.client.user;

import java.util.HashMap;

import com.sapienter.jbilling.client.util.Constants;

public class PartnerDefaultsActionContext {

	private Float myRate;
	private Float myFee;
	private Integer myFeeCurrency;
	private Integer myPeriodUnitId;
	private Integer myPeriod;
	private boolean myProcess;
	private boolean myOneTime;
	private Integer myClerk;

	public void setRate(Float rate) {
		myRate = rate;
	}

	public void setFee(Float fee) {
		myFee = fee;
	}

	public void setFeeCurrency(Integer feeCurrency) {
		myFeeCurrency = feeCurrency;
	}

	public void setOneTime(boolean oneTime) {
		myOneTime = oneTime;
	}

	public void setPeriodUnitId(Integer periodUnitId) {
		myPeriodUnitId = periodUnitId;
	}

	public void setPeriodValue(Integer period) {
		myPeriod = period;
	}

	public void setProcess(Boolean process) {
		myProcess = process;
	}

	public void setClerk(Integer clerk) {
		myClerk = clerk;
	}
	
	public HashMap<Integer, Number> asPreferencesMap(){
		HashMap<Integer, Number> result = new HashMap<Integer, Number>();
        result.put(Constants.PREFERENCE_PART_DEF_RATE, myRate);
        result.put(Constants.PREFERENCE_PART_DEF_FEE, myFee);
        result.put(Constants.PREFERENCE_PART_DEF_FEE_CURR, myFeeCurrency);
        result.put(Constants.PREFERENCE_PART_DEF_ONE_TIME, asPreferenceValue(myOneTime));
        result.put(Constants.PREFERENCE_PART_DEF_PER_UNIT, myPeriodUnitId);
        result.put(Constants.PREFERENCE_PART_DEF_PER_VALUE, myPeriod);
        result.put(Constants.PREFERENCE_PART_DEF_AUTOMATIC, asPreferenceValue(myProcess));
        result.put(Constants.PREFERENCE_PART_DEF_CLERK, myClerk);
        return result;
	}
	
	private static final int asPreferenceValue(boolean value){
		return value ? 1 : 0;
	}
	
}
