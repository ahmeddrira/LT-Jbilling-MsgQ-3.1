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

import javax.ejb.FinderException;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.CrudActionBase;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.ItemUserPriceDTO;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.item.ItemSessionBean;
import com.sapienter.jbilling.server.util.Context;

public class PriceMaintainAction extends CrudActionBase<ItemUserPriceDTO> {

    private final ItemSessionBean myItemSession;
    private static final String FORM_PRICE = "price";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_CURRENCY_ID = "currencyId";
    private static final String FIELD_ID = "id";
    private static final String MESSAGE_CREATE_SUCCESS = "item.user.price.create.done";
    private static final String MESSAGE_CREATE_DUPLICATE = "item.user.price.create.duplicate";
    private static final String MESSAGE_UPDATE_SUCCESS = "item.user.price.update.done";
    private static final String FORWARD_LIST = "price_list";
    private static final String FORWARD_CREATE = "price_create";
    private static final String FORWARD_EDIT = "price_edit";
    private static final String FORWARD_DELETED = "price_deleted";

    public PriceMaintainAction() {
        super(FORM_PRICE, "item price");
        myItemSession = (ItemSessionBean) Context.getBean(Context.Name.ITEM_SESSION);
    }

    @Override
    protected ItemUserPriceDTO doEditFormToDTO() {
        ItemUserPriceDTO dto = new ItemUserPriceDTO();
        dto.setPrice(string2float((String) myForm.get(FIELD_PRICE)));
        dto.setCurrency(new CurrencyDTO((Integer) myForm.get(FIELD_CURRENCY_ID)));
        return dto;
    }

    @Override
    protected ForwardAndMessage doCreate(ItemUserPriceDTO dto)
            throws RemoteException {
        dto.setItem(new ItemDTO(selectedId));
        dto.setUser(new UserDTO(getSessionUserId()));
        Integer createdId = myItemSession.createPrice(executorId, dto);

        return createdId == null ? new ForwardAndMessage(FORWARD_LIST, MESSAGE_CREATE_DUPLICATE) : //
                new ForwardAndMessage(FORWARD_LIST, MESSAGE_CREATE_SUCCESS);
    }

    @Override
    protected ForwardAndMessage doUpdate(ItemUserPriceDTO dto) throws RemoteException {
        dto.setId((Integer) myForm.get("id"));
        myItemSession.updatePrice(executorId, dto);
        return new ForwardAndMessage(FORWARD_LIST, MESSAGE_UPDATE_SUCCESS);
    }

    @Override
    protected ForwardAndMessage doSetup() throws RemoteException {
        ForwardAndMessage result;
        // for prices, a setup is needed when creating one, because
        // the item information is displayed
        final ItemDTO itemDto;

        // to get a price I need the user and the item
        // the item: it's just been selected from a list, so it is in selectedId
        // the user:
        Integer userId = getSessionUserId();

        // check if I'm being called from the list of prices or from
        // a create
        final ItemUserPriceDTO dto;
        if (getSessionItemPriceId() != null) {
            // called from the prices list
            Integer itemPriceId = (Integer) getSessionItemPriceId();
            dto = myItemSession.getPrice(itemPriceId);
            selectedId = dto.getItemId();
        } else {
            // called from the items list
            dto = myItemSession.getPrice(userId, selectedId);
        }

        if (dto != null) { // the combination is found
            myForm.set(FIELD_ID, dto.getId());
            myForm.set(FIELD_PRICE, float2string(dto.getPrice()));
            myForm.set(FIELD_CURRENCY_ID, dto.getCurrencyId());
            // the id of the price is left in the session, so it can
            // be used later in the delete
            session.setAttribute(Constants.SESSION_ITEM_PRICE_ID, dto.getId());
            // as a currency, as pass just a 1 because I don't care
            // about the price
            itemDto = myItemSession.get(selectedId, languageId, null, 1,
                    entityId);

            result = new ForwardAndMessage(FORWARD_EDIT);
        } else { // it's a create
            // this is a create, because there no previous price for this
            // user-item combination.
            // I need the currency of the user, because the price will
            // be defaulted to this item's price
            UserDTOEx user;
            try {
                user = getUser(userId);
            } catch (FinderException e) {
                throw new SessionInternalError(e);
            }
            itemDto = myItemSession.get(selectedId, languageId, null, user.getCurrencyId(), entityId);
            // We then use this item's current price
            myForm.set(FIELD_PRICE, float2string(itemDto.getPrice()));
            myForm.set(FIELD_CURRENCY_ID, user.getCurrencyId());
            result = new ForwardAndMessage(FORWARD_CREATE);
        }

        // the item dto is needed, because its data is just displayed
        // with <bean>, it is not edited with <html:text>
        session.setAttribute(Constants.SESSION_ITEM_DTO, itemDto);
        return result;
    }

    @Override
    protected ForwardAndMessage doDelete() throws RemoteException {
        myItemSession.deletePrice(executorId, getSessionItemPriceId());
        return new ForwardAndMessage(FORWARD_DELETED);
    }

    @Override
    protected void resetCachedList() {
        session.removeAttribute(Constants.SESSION_LIST_KEY + "price");
    }

    private Integer getSessionUserId() {
        return (Integer) session.getAttribute(Constants.SESSION_USER_ID);
    }

    private Integer getSessionItemPriceId() {
        return (Integer) session.getAttribute(Constants.SESSION_ITEM_PRICE_ID);
    }
}
