/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 * Created on May 12, 2005
 */
package com.sapienter.jbilling.client.user;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.CrudActionBase;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.entity.PartnerRangeDTO;
import com.sapienter.jbilling.server.user.PartnerDTOEx;

/**
 * @author Emil
 */
public class PartnerRangesMaintainAction extends CrudActionBase<PartnerRangedMaintainActionContext> {
	private static final String MESSAGE_UPDATED_OK = "partner.ranges.updated";

	private static final String FORM = "ranges";

	private static final String FIELD_REFERRAL_FEE = "referral_fee";
	private static final String FIELD_PERCENTAGE = "percentage_rate";
	private static final String FIELD_RANGE_TO = "range_to";
	private static final String FIELD_RANGE_FROM = "range_from";

	private static final String FORWARD_EDIT = "ranges_edit";
	private static final String FORWARD_PARTNER = "ranges_partner";
	
	private UserSession myUserSession;
	
	public PartnerRangesMaintainAction(){
		super(FORM, "ranges");
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            UserSessionHome userHome =
                    (UserSessionHome) EJBFactory.lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);
            myUserSession = userHome.create();
        } catch (Exception e) {
			throw new SessionInternalError(
					"Initializing order CRUD action: " + e.getMessage());
        }
	}
	
	@Override
	protected void preEdit() {
		super.preEdit();
		setForward(FORWARD_EDIT);
	}
	
	@Override
	protected PartnerRangedMaintainActionContext doEditFormToDTO() {
        String from[] = (String[]) myForm.get(FIELD_RANGE_FROM);
        String to[] = (String[]) myForm.get(FIELD_RANGE_TO);
        String percentage[] = (String[]) myForm.get(FIELD_PERCENTAGE);
        String referral[] = (String[]) myForm.get(FIELD_REFERRAL_FEE);
        List<PartnerRangeDTO> ranges = new ArrayList<PartnerRangeDTO>(from.length);
        
        for (int f = 0; f < from.length; f++) {
            if (from[f] != null && from[f].trim().length() > 0) {
                PartnerRangeDTO range = new PartnerRangeDTO();
                try {
                    range.setRangeFrom(parseInteger(from[f]));
                    range.setRangeTo(parseInteger(to[f]));
                    range.setPercentageRate(string2float(percentage[f]));
                    range.setReferralFee(string2float(referral[f]));

                    if (range.getRangeFrom() == null || range.getRangeTo() == null ||
                            (range.getPercentageRate() == null && range.getReferralFee() == null) ||
                            (range.getPercentageRate() != null && range.getReferralFee() != null)) {
                    
                    	errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("partner.ranges.error", f + 1));
                    } else {
                        ranges.add(range);
                    }
                } catch (NumberFormatException e) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("partner.ranges.error", f + 1)); 
                }
            } 
        }
        
        PartnerRangeDTO[] data = new PartnerRangeDTO[ranges.size()];
        ranges.toArray(data);
        
        if (errors.isEmpty()) {
            PartnerDTOEx p = new PartnerDTOEx();
            p.setRanges(data);
            int ret = p.validateRanges();
            if (ret == 2) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("partner.ranges.error.consec"));
            } else if (ret == 3) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("partner.ranges.error.gap"));
            }
        }
        return new PartnerRangedMaintainActionContext(data);
	}
	
	@Override
	protected ForwardAndMessage doSetup() throws RemoteException {
        PartnerDTOEx partner = (PartnerDTOEx) session.getAttribute(
                Constants.SESSION_PARTNER_DTO);
        PartnerRangeDTO ranges[] = partner.getRanges();
        
        final int MAX_RANGES = 20;
        String[] allFrom = new String[MAX_RANGES];
		String[] allTo = new String[MAX_RANGES];
		String[] allPercentages = new String[MAX_RANGES];
		String[] allReferralFees = new String[MAX_RANGES];

		if (ranges != null) {
			for (int f = 0; f < Math.min(MAX_RANGES, ranges.length); f++) {
				PartnerRangeDTO next = ranges[f];
				allFrom[f] = next.getRangeFrom().toString();
				allTo[f] = next.getRangeTo().toString();
				allPercentages[f] = float2string(next.getPercentageRate());
				allReferralFees[f] = float2string(next.getReferralFee());
			}
		}

        myForm.set(FIELD_RANGE_FROM, allFrom);
        myForm.set(FIELD_RANGE_TO, allTo);
        myForm.set(FIELD_PERCENTAGE, allPercentages);
        myForm.set(FIELD_REFERRAL_FEE, allReferralFees);
        
        return new ForwardAndMessage(FORWARD_EDIT);
	}
	
	@Override
	protected ForwardAndMessage doUpdate(PartnerRangedMaintainActionContext dto) throws RemoteException {
        PartnerDTOEx partner = (PartnerDTOEx) session.getAttribute(
                Constants.SESSION_PARTNER_DTO);
        
        partner.setRanges(dto.getData()); 
        myUserSession.updatePartnerRanges(//
        		executorId, partner.getId(), dto.getData());
        return new ForwardAndMessage(FORWARD_PARTNER, MESSAGE_UPDATED_OK);
	}
	
	@Override
	protected ForwardAndMessage doCreate(PartnerRangedMaintainActionContext dto) {
		throw new IllegalArgumentException("At max 20 ranges are supported. " +
				"Direct create is not available");
	}
	
	@Override
	protected ForwardAndMessage doDelete() throws RemoteException {
		throw new UnsupportedOperationException("Direct delete is not available. " +
				"Just clear the data from one of the ranges");
	}
	
	@Override
	protected void resetCachedList() {
		session.removeAttribute(Constants.SESSION_LIST_KEY + FORM);
	}
	
	private Integer parseInteger(String text){
		return getFormHelper().parseInteger(text);
	}
	
}
