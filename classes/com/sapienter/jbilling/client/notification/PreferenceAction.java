/*
 The contents of this file are subject to the Jbilling Public License
 Version 1.1 (the "License"); you may not use this file except in
 compliance with the License. You may obtain a copy of the License at
 http://www.jbilling.com/JPL/

 Software distributed under the License is distributed on an "AS IS"
 basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 License for the specific language governing rights and limitations
 under the License.

 The Original Code is jbilling.

 The Initial Developer of the Original Code is Emiliano Conde.
 Portions created by Sapienter Billing Software Corp. are Copyright 
 (C) Sapienter Billing Software Corp. All Rights Reserved.

 Contributor(s): ______________________________________.
 */

/*
 * Created on Jul 16, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sapienter.jbilling.client.notification;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.PreferencesCrudActionBase;
import com.sapienter.jbilling.client.util.PreferencesMap;

/**
 * @author Emil
 * 
 */
public class PreferenceAction extends PreferencesCrudActionBase<PreferenceActionContext> {
	private static final String MESSAGE_UPDATED_OK = "notification.preference.update";
	private static final String FORM = "notificationPreference";
	private static final String FORWARD_EDIT = "notificationPreference_edit";

	private static final String FIELD_SELF_DELIVERY = "chbx_self_delivery";
	private static final String FIELD_SHOW_NOTES = "chbx_show_notes";
	private static final String FIELD_ORDER_DAYS_1 = "order_days1";
	private static final String FIELD_ORDER_DAYS_2 = "order_days2";
	private static final String FIELD_ORDER_DAYS_3 = "order_days3";
	private static final String FIELD_INVOICE_REMINDER = "chbx_invoice_reminders";
	private static final String FIELD_FIRST_REMINDER = "first_reminder";
	private static final String FIELD_NEXT_REMINDER = "next_reminder";

	public PreferenceAction() {
		super(FORM, "notification preferences", FORWARD_EDIT);
	}
	
	@Override
	protected PreferenceActionContext doEditFormToDTO() throws RemoteException {
		PreferenceActionContext result = new PreferenceActionContext(); 
		result.setSelfDelivery(getCheckBoxBooleanValue(FIELD_SELF_DELIVERY));
		result.setShowNotes(getCheckBoxBooleanValue(FIELD_SHOW_NOTES));
		result.setInvoiceReminders(getCheckBoxBooleanValue(FIELD_INVOICE_REMINDER));
		result.setOrderDays1(getIntegerFieldValue(FIELD_ORDER_DAYS_1));
		result.setOrderDays2(getIntegerFieldValue(FIELD_ORDER_DAYS_2));
		result.setOrderDays3(getIntegerFieldValue(FIELD_ORDER_DAYS_3));
		
		result.setFirstReminder(getIntegerFieldValue(FIELD_FIRST_REMINDER));
		result.setNextReminder(getIntegerFieldValue(FIELD_NEXT_REMINDER));
		
		if (!result.validateDayValuesIncremental()){
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("notification.orderDays.error"));
		}
		
		if (!result.validateReminders()){
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("notification.reminders.error"));
		}
		
		return result;
	}
	
	@Override
	protected ForwardAndMessage doSetup() throws RemoteException {
        Integer[] ids = new Integer[] { //
        		Constants.PREFERENCE_PAPER_SELF_DELIVERY, 
        		Constants.PREFERENCE_SHOW_NOTE_IN_INVOICE, 
        		Constants.PREFERENCE_DAYS_ORDER_NOTIFICATION_S1, 
        		Constants.PREFERENCE_DAYS_ORDER_NOTIFICATION_S2, 
        		Constants.PREFERENCE_DAYS_ORDER_NOTIFICATION_S3, 
        		Constants.PREFERENCE_FIRST_REMINDER, 
        		Constants.PREFERENCE_NEXT_REMINDER, 
        		Constants.PREFERENCE_USE_INVOICE_REMINDERS,
        };
        PreferencesMap prefs = getEntityPreferences(ids);
        
        myForm.set(FIELD_SELF_DELIVERY, prefs.getBoolean(Constants.PREFERENCE_PAPER_SELF_DELIVERY));
        myForm.set(FIELD_SHOW_NOTES, prefs.getBoolean(Constants.PREFERENCE_SHOW_NOTE_IN_INVOICE));
        myForm.set(FIELD_INVOICE_REMINDER, prefs.getBoolean(Constants.PREFERENCE_USE_INVOICE_REMINDERS));
        
        myForm.set(FIELD_ORDER_DAYS_1, prefs.getString(Constants.PREFERENCE_DAYS_ORDER_NOTIFICATION_S1));
        myForm.set(FIELD_ORDER_DAYS_2, prefs.getString(Constants.PREFERENCE_DAYS_ORDER_NOTIFICATION_S2));
        myForm.set(FIELD_ORDER_DAYS_3, prefs.getString(Constants.PREFERENCE_DAYS_ORDER_NOTIFICATION_S3));
        
        myForm.set(FIELD_FIRST_REMINDER, prefs.getString(Constants.PREFERENCE_FIRST_REMINDER));
        myForm.set(FIELD_NEXT_REMINDER, prefs.getString(Constants.PREFERENCE_NEXT_REMINDER));
        
        return getForwardEdit();
	}

	@Override
	protected ForwardAndMessage doUpdate(PreferenceActionContext dto) throws RemoteException {
		HashMap<Integer, Integer> preferencesMap = dto.asPreferencesMap();
        getUserSession().setEntityParameters(entityId, preferencesMap);
        return getForwardEdit(MESSAGE_UPDATED_OK);
	}

}