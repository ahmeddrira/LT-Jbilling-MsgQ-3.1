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

package com.sapienter.jbilling.client.item;

import java.rmi.RemoteException;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.CrudActionBase;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.ItemSession;
import com.sapienter.jbilling.interfaces.ItemSessionHome;
import com.sapienter.jbilling.server.item.PromotionDTOEx;

public class PromotionMaintainAction extends CrudActionBase<PromotionDTOEx> {
	private static final String FORM_PROMOTION = "promotion";
	private static final String FIELD_ID = "id";
	private static final String FIELD_CODE = "code";
	private static final String FIELD_NOTES = "notes";
	private static final String FIELD_ONLY_ONCE = "chbx_once";
	private static final String FIELD_GROUP_SINCE = "since";
	private static final String FIELD_GROUP_UNTIL = "until";

	private static final String MESSAGE_CREATE_SUCCESS = "promotion.create.done";
	private static final String MESSAGE_UPDATE_SUCCESS = "promotion.update.done";

	private static final String FORWARD_LIST = "promotion_list";
	private static final String FORWARD_EDIT = "promotion_edit";
	private static final String FORWARD_DELETED = "promotion_deleted";
	
	private final ItemSession myItemSession;
	
	public PromotionMaintainAction(){
		super(FORM_PROMOTION, "promotion");
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            ItemSessionHome itemHome =
                    (ItemSessionHome) EJBFactory.lookUpHome(
                    ItemSessionHome.class,
                    ItemSessionHome.JNDI_NAME);
        
            myItemSession = itemHome.create();
        } catch (Exception e) {
			throw new SessionInternalError(
					"Initializing promotion CRUD action: " + e.getMessage());
        }
	}
	
	@Override
	protected PromotionDTOEx doEditFormToDTO() {
		PromotionDTOEx dto = new PromotionDTOEx();
        dto.setCode((String) myForm.get(FIELD_CODE));
        dto.setNotes((String) myForm.get(FIELD_NOTES));
        dto.setOnce(((Boolean) myForm.get(FIELD_ONLY_ONCE)).booleanValue() ? 1 : 0);
        dto.setSince(parseDate(FIELD_GROUP_SINCE, "promotion.prompt.since"));
        dto.setUntil(parseDate(FIELD_GROUP_UNTIL, "promotion.prompt.until"));
        return dto;
	}
	
	@Override
	protected ForwardAndMessage doCreate(PromotionDTOEx dto) throws RemoteException {
        // this is the item that has been created for this promotion
        dto.setItemId((Integer) session.getAttribute(Constants.SESSION_ITEM_ID));
        myItemSession.createPromotion(executorId, entityId, dto);
        return new ForwardAndMessage(FORWARD_LIST, MESSAGE_CREATE_SUCCESS);
	}
	
	@Override
	protected ForwardAndMessage doUpdate(PromotionDTOEx dto) throws RemoteException {
        dto.setId((Integer) myForm.get("id"));
        myItemSession.updatePromotion(executorId, dto);
        return new ForwardAndMessage(FORWARD_LIST, MESSAGE_UPDATE_SUCCESS);
	}
	
	@Override
	protected ForwardAndMessage doSetup() throws RemoteException {
        PromotionDTOEx dto = myItemSession.getPromotion(selectedId);
        myForm.set(FIELD_ID, dto.getId());
        myForm.set(FIELD_CODE, dto.getCode());
        myForm.set(FIELD_NOTES, dto.getNotes());
        myForm.set(FIELD_ONLY_ONCE, (dto.getOnce().intValue() == 1));
        // new parse the dates
        setFormDate(FIELD_GROUP_SINCE, dto.getSince());
        setFormDate(FIELD_GROUP_UNTIL, dto.getUntil());
                 
		// the item id will be needed if the user wants to edit the 
		// item related with this promotion.
		session.setAttribute(Constants.SESSION_LIST_ID_SELECTED, dto.getItemId());
		session.setAttribute(Constants.SESSION_PROMOTION_DTO, dto);
		return new ForwardAndMessage(FORWARD_EDIT);
	}
	
	@Override
	protected ForwardAndMessage doDelete() throws RemoteException {
		PromotionDTOEx dto = (PromotionDTOEx) session.getAttribute(Constants.SESSION_PROMOTION_DTO);
        Integer promotionId = dto.getId();
        myItemSession.deletePromotion(executorId, promotionId);
        return new ForwardAndMessage(FORWARD_DELETED);
	}
	
	@Override
	protected void resetCachedList() {
		session.removeAttribute(Constants.SESSION_LIST_KEY + "promotion");
	}
}
