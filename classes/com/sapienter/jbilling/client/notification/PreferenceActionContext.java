package com.sapienter.jbilling.client.notification;

import java.util.HashMap;

import com.sapienter.jbilling.client.util.Constants;

public class PreferenceActionContext {
	private boolean mySelfDelivery;
	private boolean myShowNotes;
	private boolean myInvoiceReminders;
	private Integer myOrderDays1;
	private Integer myOrderDays2;
	private Integer myOrderDays3;
	private Integer myFirstReminder;
	private Integer myNextReminder;
	
	public boolean validateDayValuesIncremental() {
		boolean isOk = true;
		if (myOrderDays1 != null){
			isOk &= myOrderDays2 == null || myOrderDays2 > myOrderDays1;
			isOk &= myOrderDays3 == null || myOrderDays3 > myOrderDays1;
		}
		
		if (myOrderDays2 != null){
			isOk &= myOrderDays3 == null || myOrderDays3 > myOrderDays2; 
		}
		
		return isOk;
	}

	public void setSelfDelivery(boolean selfDelivery) {
		mySelfDelivery = selfDelivery;
	}

	public void setShowNotes(boolean showNotes) {
		myShowNotes = showNotes;
	}

	public void setInvoiceReminders(boolean invoiceReminders) {
		myInvoiceReminders = invoiceReminders;
	}

	public void setOrderDays1(Integer orderDays1) {
		myOrderDays1 = orderDays1;
	}

	public void setOrderDays2(Integer orderDays2) {
		myOrderDays2 = orderDays2;
	}

	public void setOrderDays3(Integer orderDays3) {
		myOrderDays3 = orderDays3;
	}

	public void setFirstReminder(Integer firstReminder) {
		myFirstReminder = firstReminder;
	}

	public void setNextReminder(Integer nextReminder) {
		myNextReminder = nextReminder;
	}

	public boolean validateReminders() {
		boolean result = true;
		if (myInvoiceReminders){
			result = myFirstReminder != null && myNextReminder != null;
		}
		return result;
	}
	
	public HashMap<Integer, Integer> asPreferencesMap(){
        HashMap<Integer, Integer> params = new HashMap<Integer, Integer>();
        params.put(Constants.PREFERENCE_PAPER_SELF_DELIVERY, asPreferenceValue(mySelfDelivery));
        params.put(Constants.PREFERENCE_SHOW_NOTE_IN_INVOICE, asPreferenceValue(myShowNotes));
        params.put(Constants.PREFERENCE_USE_INVOICE_REMINDERS, asPreferenceValue(myInvoiceReminders));

        params.put(Constants.PREFERENCE_DAYS_ORDER_NOTIFICATION_S1, myOrderDays1); 
        params.put(Constants.PREFERENCE_DAYS_ORDER_NOTIFICATION_S2, myOrderDays2);
        params.put(Constants.PREFERENCE_DAYS_ORDER_NOTIFICATION_S3, myOrderDays3);
        params.put(Constants.PREFERENCE_FIRST_REMINDER, myFirstReminder); 
        params.put(Constants.PREFERENCE_NEXT_REMINDER, myNextReminder);
        
        return params;
	}
	
	private static final int asPreferenceValue(boolean value){
		return value ? 1 : 0;
	}
	

}
