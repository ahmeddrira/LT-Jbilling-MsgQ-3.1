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

package com.sapienter.jbilling.client.process;

import java.rmi.RemoteException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.util.UpdateOnlyCrudActionBase;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.BillingProcessSession;
import com.sapienter.jbilling.interfaces.BillingProcessSessionHome;
import com.sapienter.jbilling.server.entity.BillingProcessConfigurationDTO;

public class ConfigurationMaintainAction extends
		UpdateOnlyCrudActionBase<BillingProcessConfigurationDTO> {

	private static final String FORM_CONFIGURATION = "configuration";

	private static final String FIELD_GROUP_RUN = "run";
	private static final String FIELD_GENERATE_REPORT = "chbx_generateReport";
	private static final String FIELD_RETRIES = "retries";
	private static final String FIELD_RETRIES_DAYS = "retries_days";
	private static final String FIELD_REPORT_DAYS = "report_days";
	private static final String FIELD_PERIOD_UNIT = "period_unit_id";
	private static final String FIELD_PERIOD_VALUE = "period_unit_value";
	private static final String FIELD_DUE_DATE_UNIT = "due_date_unit_id";
	private static final String FIELD_DUE_DATE_VALUE = "due_date_value";
	private static final String FIELD_DF_FM = "chbx_df_fm";
	private static final String FIELD_RECURRING_ONLY = "chbx_only_recurring";
	private static final String FIELD_INVOICE_DATE = "chbx_invoice_date";
	private static final String FIELD_AUTO_PAYMENT = "chbx_auto_payment";
	private static final String FIELD_MAX_PEROIDS = "maximum_periods";
	private static final String FIELD_APPLY_PAYMENT = "chbx_payment_apply";

	private static final String FORWARD_EDIT = "configuration_edit";

	private static final String MESSAGE_UPDATE_SUCCESS = "process.configuration.updated";

	private final BillingProcessSession myBillingSession;

	public ConfigurationMaintainAction() {
		super(FORM_CONFIGURATION, "billing process configuration", FORWARD_EDIT);
		try {
			JNDILookup EJBFactory = JNDILookup.getFactory(false);
			BillingProcessSessionHome processHome = (BillingProcessSessionHome) EJBFactory
					.lookUpHome(BillingProcessSessionHome.class,
							BillingProcessSessionHome.JNDI_NAME);

			myBillingSession = processHome.create();

		} catch (Exception e) {
			throw new SessionInternalError(
					"Initializing billing process configuration"
							+ " CRUD action: " + e.getMessage());

		}
	}

	@Override
	protected BillingProcessConfigurationDTO doEditFormToDTO()
			throws RemoteException {
		BillingProcessConfigurationDTO dto = new BillingProcessConfigurationDTO();
		dto.setEntityId(entityId);

		dto.setRetries(getIntegerFieldValue(FIELD_RETRIES));
		dto.setNextRunDate(parseDate(FIELD_GROUP_RUN,
				"process.configuration.prompt.nextRunDate"));
		dto.setDaysForRetry(getIntegerFieldValue(FIELD_RETRIES_DAYS));
		dto.setDaysForReport(getIntegerFieldValue(FIELD_REPORT_DAYS));
		dto.setGenerateReport(getCheckBoxValue(FIELD_GENERATE_REPORT));
		dto.setDfFm(getCheckBoxValue(FIELD_DF_FM));
		dto.setOnlyRecurring(getCheckBoxValue(FIELD_RECURRING_ONLY));
		dto.setInvoiceDateProcess(getCheckBoxValue(FIELD_INVOICE_DATE));
		dto.setAutoPayment(getCheckBoxValue(FIELD_AUTO_PAYMENT));
		dto.setAutoPaymentApplication(getCheckBoxValue(FIELD_APPLY_PAYMENT));
		dto.setPeriodUnitId(getIntegerFieldValue(FIELD_PERIOD_UNIT));
		dto.setPeriodValue(getIntegerFieldValue(FIELD_PERIOD_VALUE));
		dto.setDueDateUnitId(getIntegerFieldValue(FIELD_DUE_DATE_UNIT));
		dto.setDueDateValue(getIntegerFieldValue(FIELD_DUE_DATE_VALUE));
		dto.setMaximumPeriods(getIntegerFieldValue(FIELD_MAX_PEROIDS));

		if (dto.getAutoPayment().intValue() == 0
				&& dto.getRetries().intValue() > 0) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"process.configuration.error.auto"));
		}

		return dto;
	}

	@Override
	protected ForwardAndMessage doUpdate(BillingProcessConfigurationDTO dto)
			throws RemoteException {
		myBillingSession.createUpdateConfiguration(executorId, dto);
		return new ForwardAndMessage(FORWARD_EDIT, MESSAGE_UPDATE_SUCCESS);
	}

	@Override
	protected ForwardAndMessage doSetup() throws RemoteException {
		BillingProcessConfigurationDTO dto;
		dto = myBillingSession.getConfigurationDto(entityId);

		setFormDate(FIELD_GROUP_RUN, dto.getNextRunDate());

		myForm.set(FIELD_GENERATE_REPORT, isOne(dto.getGenerateReport()));
		myForm.set(FIELD_DF_FM, isOneMayBeNull(dto.getDfFm()));
		myForm
				.set(FIELD_RECURRING_ONLY, isOneMayBeNull(dto
						.getOnlyRecurring()));
		myForm.set(FIELD_INVOICE_DATE, isOneMayBeNull(dto
				.getInvoiceDateProcess()));
		myForm.set(FIELD_AUTO_PAYMENT, isOneMayBeNull(dto.getAutoPayment()));
		myForm.set(FIELD_APPLY_PAYMENT, isOneMayBeNull(dto
				.getAutoPaymentApplication()));

		myForm.set(FIELD_RETRIES, stringMayBeNull(dto.getRetries()));
		myForm.set(FIELD_RETRIES_DAYS, stringMayBeNull(dto.getDaysForRetry()));
		myForm.set(FIELD_REPORT_DAYS, stringMayBeNull(dto.getDaysForReport()));
		myForm.set(FIELD_MAX_PEROIDS, stringMayBeNull(dto.getMaximumPeriods()));

		myForm.set(FIELD_PERIOD_UNIT, dto.getPeriodUnitId().toString());
		myForm.set(FIELD_PERIOD_VALUE, dto.getPeriodValue().toString());
		myForm.set(FIELD_DUE_DATE_UNIT, dto.getDueDateUnitId().toString());
		myForm.set(FIELD_DUE_DATE_VALUE, dto.getDueDateValue().toString());

		return getForwardEdit();
	}

	private Integer getCheckBoxValue(String fieldName) {
		Boolean value = (Boolean) myForm.get(fieldName);
		return value.booleanValue() ? 1 : 0;
	}

	private Boolean isOne(Integer integer) {
		return Integer.valueOf(1).equals(integer);
	}

	private Boolean isOneMayBeNull(Integer integer) {
		return integer == null ? null : isOne(integer);
	}

	private String stringMayBeNull(Integer integer) {
		return integer == null ? null : integer.toString();
	}

}
