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

package com.sapienter.jbilling.client.user;

import java.rmi.RemoteException;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.PreferencesCrudActionBase;

public class PartnerDefaultsAction extends PreferencesCrudActionBase<PartnerDefaultsActionContext> {
	private static final String FORM = "partnerDefault";
	
	private static final String FIELD_RATE ="rate";
	private static final String FIELD_FEE = "fee";
	private static final String FIELD_FEE_CURRENCY="fee_currency";
	private static final String FIELD_ONE_TIME ="chbx_one_time";
	private static final String FIELD_PERIOD_UNITS ="period_unit_id";
	private static final String FIELD_PERIOD_VALUE ="period_value";
	private static final String FIELD_PROCESS ="chbx_process";
	private static final String FIELD_CLERK ="clerk";
	
	private static final String FORWARD_EDIT = "partnerDefault_edit";
	private static final String MESSAGE_UPDATE_SUCCESS = "partner.default.updated";
	
	public PartnerDefaultsAction(){
		super(FORM, "partner defaults", FORWARD_EDIT);
	}
	
	@Override
	protected PartnerDefaultsActionContext doEditFormToDTO() {
		PartnerDefaultsActionContext dto = new PartnerDefaultsActionContext();
		dto.setRate(string2float((String)myForm.get(FIELD_RATE)));
		dto.setFee(string2float((String)myForm.get(FIELD_FEE)));
		dto.setFeeCurrency((Integer) myForm.get(FIELD_FEE_CURRENCY));
		dto.setOneTime(getCheckBoxBooleanValue(FIELD_ONE_TIME));
        dto.setPeriodUnitId((Integer) myForm.get(FIELD_PERIOD_UNITS));
        dto.setPeriodValue(getIntegerFieldValue(FIELD_PERIOD_VALUE));
        dto.setProcess(getCheckBoxBooleanValue(FIELD_PROCESS));
        dto.setClerk(getIntegerFieldValue(FIELD_CLERK));
        return dto;
	}
	
	@Override
	protected ForwardAndMessage doUpdate(PartnerDefaultsActionContext dto) throws RemoteException {
		getUserSession().setEntityParameters(entityId, dto.asPreferencesMap());
		return getForwardEdit(MESSAGE_UPDATE_SUCCESS);
	}
	
	@Override
	protected ForwardAndMessage doSetup() throws RemoteException {
        // set up[ which preferences do we need
        Integer[] ids = new Integer[] {
        		Constants.PREFERENCE_PART_DEF_RATE, 
        		Constants.PREFERENCE_PART_DEF_FEE, 
        		Constants.PREFERENCE_PART_DEF_FEE_CURR,
        		Constants.PREFERENCE_PART_DEF_ONE_TIME,
        		Constants.PREFERENCE_PART_DEF_PER_UNIT,
        		Constants.PREFERENCE_PART_DEF_PER_VALUE,
        		Constants.PREFERENCE_PART_DEF_AUTOMATIC,
        		Constants.PREFERENCE_PART_DEF_CLERK,
        };
        
        PreferencesMap prefs = getEntityPreferences(ids);
        myForm.set(FIELD_RATE, prefs.getString(Constants.PREFERENCE_PART_DEF_RATE));
        myForm.set(FIELD_FEE, prefs.getString(Constants.PREFERENCE_PART_DEF_FEE));
        myForm.set(FIELD_FEE_CURRENCY, prefs.getInteger(Constants.PREFERENCE_PART_DEF_FEE_CURR));
        myForm.set(FIELD_ONE_TIME, prefs.getBoolean(Constants.PREFERENCE_PART_DEF_ONE_TIME));
        myForm.set(FIELD_PERIOD_UNITS, prefs.getInteger(Constants.PREFERENCE_PART_DEF_PER_UNIT));
        myForm.set(FIELD_PERIOD_VALUE, prefs.getString(Constants.PREFERENCE_PART_DEF_PER_VALUE));
        myForm.set(FIELD_PROCESS, prefs.getBoolean(Constants.PREFERENCE_PART_DEF_AUTOMATIC));
        myForm.set(FIELD_CLERK, prefs.getString(Constants.PREFERENCE_PART_DEF_CLERK));
        
        return getForwardEdit();
	}
	
}
