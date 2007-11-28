/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.Resources;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.CrudActionBase;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.ItemSession;
import com.sapienter.jbilling.interfaces.ItemSessionHome;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.ItemPriceDTOEx;

public class MaintainAction extends CrudActionBase<ItemDTOEx> {
	private static final String FORM = "item";

	private static final String FIELD_PRICES = "prices";
	private static final String FIELD_PERCENTAGE = "percentage";
	private static final String FIELD_TYPES = "types";
	private static final String FIELD_MANUAL_PRICE = "chbx_priceManual";
	private static final String FIELD_INTERNAL_NUMBER = "internalNumber";
	private static final String FIELD_DESCRIPTION = "description";
	private static final String FIELD_ID = "id";
	private static final String FIELD_LANGUAGE = "language";
	
	private static final String FORWARD_EDIT = "item_edit";
	private static final String FORWARD_LIST = "item_list";
	private static final String FORWARD_PROMOTION = "item_promotion";
	private static final String FORWARD_DELETED = "item_deleted";
	
	private static final String MESSAGE_CREATED_OK = "item.create.done";
	private static final String MESSAGE_UPDATED_OK = "item.update.done";
	
	private final ItemSession myItemSession;
	
	public MaintainAction(){
		super(FORM, "item");
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            ItemSessionHome itemHome =
                    (ItemSessionHome) EJBFactory.lookUpHome(
                    ItemSessionHome.class,
                    ItemSessionHome.JNDI_NAME);
            myItemSession = itemHome.create();
		} catch (Exception e) {
			throw new SessionInternalError(
					"Initializing item CRUD action: " + e.getMessage());
		}
	}
	
	@Override
	protected ItemDTOEx doEditFormToDTO() throws RemoteException {
		ItemDTOEx dto = new ItemDTOEx();
        dto.setDescription((String) myForm.get(FIELD_DESCRIPTION));
        dto.setEntityId((Integer) session.getAttribute(Constants.SESSION_ENTITY_ID_KEY));
        dto.setNumber((String) myForm.get(FIELD_INTERNAL_NUMBER));
        dto.setPriceManual((Boolean) myForm.get(FIELD_MANUAL_PRICE) ? 1 : 0);
        dto.setTypes((Integer[]) myForm.get(FIELD_TYPES));
        dto.setPercentage(string2float((String) myForm.get(FIELD_PERCENTAGE)));

        // because of the bad idea of using the same bean for item/type/price,
        // the validation has to be manual
        if (dto.getTypes().length == 0) {
            String field = Resources.getMessage(request, "item.prompt.types"); 
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.required", field));
        }
        
        // get the prices. At least one has to be present
        dto.setPrices((Vector) myForm.get(FIELD_PRICES));
        boolean atLeastOnePriceFound = false;
        for (int f = 0; f < dto.getPrices().size(); f++) {
        	ItemPriceDTOEx nextPrice = (ItemPriceDTOEx)dto.getPrices().get(f);
            LOG.debug("Now processing item price " + f + " data:" + nextPrice); 
        	String priceStr = nextPrice.getPriceForm(); 
            if (priceStr != null && priceStr.trim().length() > 0) {
                Float price = string2float(priceStr);
                if (price == null) {
                    String field = Resources.getMessage(request, "item.prompt.price"); 
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("errors.float", field));
                    break;
                } else {
                    atLeastOnePriceFound = true;
                }
                nextPrice.setPrice(price);
            }
        }

        // either is a percentage or a price is required.
        if (!atLeastOnePriceFound && dto.getPercentage() == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("item.error.price"));
        }
        return dto;
	}
	
	@Override
	protected ForwardAndMessage doCreate(ItemDTOEx dto) throws RemoteException {
        // we pass a null language, so it'll pick up the one from
        // the entity
        Integer newItem = myItemSession.create(dto, null);
        
        ForwardAndMessage result;
        if ("promotion".equals(myForm.get("create"))){
            // the id of the new item is needed later, when the
            // promotion record is created
            session.setAttribute(Constants.SESSION_ITEM_ID, newItem);
            result = new ForwardAndMessage(FORWARD_PROMOTION, MESSAGE_CREATED_OK);
            LOG.debug("Processing an item for a promotion");
        } else {
        	result = new ForwardAndMessage(FORWARD_LIST, MESSAGE_CREATED_OK);
        }
        return result;
	}
	
	@Override
	protected ForwardAndMessage doUpdate(ItemDTOEx dto) throws RemoteException {
        dto.setId(selectedId);
        myItemSession.update(executorId, dto, (Integer) myForm.get(FIELD_LANGUAGE));
        return new ForwardAndMessage(FORWARD_LIST, MESSAGE_UPDATED_OK);
	}
	
	@Override
	protected ForwardAndMessage doSetup() throws RemoteException {
        // the price is actually irrelevant in this call, since it's going
        // to be overwirtten by the user's input
        // in this case the currency doesn't matter, it
        ItemDTOEx dto = myItemSession.get(selectedId, languageId, null, null, entityId);
        // the prices have to be localized
        for (int f = 0; f < dto.getPrices().size(); f++) {
            ItemPriceDTOEx pr = (ItemPriceDTOEx) dto.getPrices().get(f);
            pr.setPriceForm(float2string(pr.getPrice()));
        }
        myForm.set(FIELD_INTERNAL_NUMBER, dto.getNumber());
        myForm.set(FIELD_DESCRIPTION, dto.getDescription());
        myForm.set(FIELD_MANUAL_PRICE, dto.getPriceManual().intValue() > 0);
        myForm.set(FIELD_TYPES, dto.getTypes());
        myForm.set(FIELD_ID, dto.getId());
        myForm.set(FIELD_PRICES, dto.getPrices());
        myForm.set(FIELD_LANGUAGE, languageId);
        if (dto.getPercentage() != null) {
            myForm.set(FIELD_PERCENTAGE, float2string(dto.getPercentage()));
        } else {
            // otherwise it will pickup the percentage of a 
            // previously edited item!
            myForm.set(FIELD_PERCENTAGE, null);
        }
        
        return new ForwardAndMessage(FORWARD_EDIT);
	}
	
	@Override
	protected ForwardAndMessage doDelete() throws RemoteException {
		myItemSession.delete(executorId, selectedId);
		return new ForwardAndMessage(FORWARD_DELETED);
	}
	
	@Override
	public ActionForward execute(ActionMapping aMapping, ActionForm aForm, HttpServletRequest aRequest, HttpServletResponse aResponse) 
			throws IOException, ServletException {

		if (aRequest.getParameter("id") != null) {
            // this is being called from anywhere, to check out a invoice
            Integer itemId = Integer.valueOf(aRequest.getParameter("id"));
            aRequest.getSession(false).setAttribute(Constants.SESSION_LIST_ID_SELECTED, itemId);
        }
		return super.execute(aMapping, aForm, aRequest, aResponse);
	}
	
	@Override
    protected boolean isCancelled(HttpServletRequest request) {
        return !request.getParameter("mode").equals("setup");
    }     
    
    @Override
    protected boolean isResetRequested() {
        return request.getParameter("reload") != null || super.isResetRequested();
    }
    
    @Override
    protected void preReset() {
    	//call to super would re-init the form from mapping 
    	setForward(FORWARD_EDIT);
    }
    
    @Override
    public void reset() {
    	super.reset();
        // this is just a change of language the requires a reload
        // of the bean
        languageId = (Integer) myForm.get(FIELD_LANGUAGE);
        setup();
    }
    
    @Override
    protected void resetCachedList() {
        session.removeAttribute(Constants.SESSION_LIST_KEY + FORM);
    }
    
    @Override
    protected void preEdit() {
    	super.preEdit();
    	setForward(FORWARD_EDIT);
    }
    
}
