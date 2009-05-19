/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sapienter.jbilling.server.item;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.db.ItemDAS;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.ItemUserPriceDAS;
import com.sapienter.jbilling.server.item.db.ItemUserPriceDTO;
import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.audit.EventLogger;
import com.sapienter.jbilling.server.util.db.CurrencyDAS;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;

public class ItemUserPriceBL {
    private ItemUserPriceDAS itemUserPriceDas = null;
    private ItemUserPriceDTO itemUserPrice = null;
    private Logger log = null;
    private EventLogger eLogger = null;
    
    public ItemUserPriceBL(Integer itemUserPriceId) {
        init();
        set(itemUserPriceId);
    }
    
    public ItemUserPriceBL(Integer userId, Integer itemId, Integer currencyId) {
        init();
        itemUserPrice = itemUserPriceDas.find(userId, itemId, currencyId);
    }
    
    public ItemUserPriceBL() {
        init();
    }
    
    private void init() {
        log = Logger.getLogger(ItemUserPriceBL.class);     
        eLogger = EventLogger.getInstance();        
        itemUserPriceDas = new ItemUserPriceDAS();
    }

    public ItemUserPriceDTO getEntity() {
        return itemUserPrice;
    }
    
    public void set(Integer id) {
        itemUserPrice = itemUserPriceDas.find(id);
    }
    
    public Integer create(Integer userId, Integer itemId, Integer currencyId,
            Float price) {
        UserDTO user = new UserDAS().find(userId);
        ItemDTO item = new ItemDAS().find(itemId);
        CurrencyDTO currency = new CurrencyDAS().find(currencyId);
        itemUserPrice = new ItemUserPriceDTO(user, item, price, currency);
        itemUserPrice = itemUserPriceDas.save(itemUserPrice);
                
        return itemUserPrice.getId();       
    }
    
    public boolean update(Integer executorId, ItemUserPriceDTO dto) 
            throws SessionInternalError {
        if (itemUserPrice == null) {
            return false;
        }

        eLogger.audit(executorId, Constants.TABLE_ITEM_USER_PRICE, 
                itemUserPrice.getId(),
                EventLogger.MODULE_ITEM_TYPE_MAINTENANCE, 
                EventLogger.ROW_UPDATED, null,  
                itemUserPrice.getPrice().toString(), null);

        itemUserPrice.setPrice(dto.getPrice());
        return true;
    }
    
    public void delete(Integer executorId) {

        eLogger.audit(executorId, Constants.TABLE_ITEM_USER_PRICE, 
                itemUserPrice.getId(),
                EventLogger.MODULE_ITEM_USER_PRICE_MAINTENANCE, 
                EventLogger.ROW_DELETED, null, null,null);
        itemUserPriceDas.delete(itemUserPrice);
    }
    
    public ItemUserPriceDTO getDTO() {
        if (itemUserPrice == null) {
            return null;
        }

        ItemUserPriceDTO dto = new ItemUserPriceDTO();
        
        dto.setId(itemUserPrice.getId());
        dto.setItem(itemUserPrice.getItem());
        dto.setUser(itemUserPrice.getUser());
        dto.setPrice(itemUserPrice.getPrice());
        dto.setCurrency(itemUserPrice.getCurrency());
        
        return dto;
    }
    
    /**
     * Verifies if there's a price for the given user/item key, to
     * avoid duplications
     * @param userId
     * @param itemId
     * @return
     */
    public boolean isPresent(Integer userId, Integer itemId,
            Integer currencyId) {

        itemUserPrice = itemUserPriceDas.find(userId, itemId, currencyId);

        if (itemUserPrice == null) {
            return false;
        }
        return true;
    }
}
