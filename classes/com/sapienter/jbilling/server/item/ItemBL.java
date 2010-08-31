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

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.db.ItemDAS;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.ItemTypeDTO;
import com.sapienter.jbilling.server.item.tasks.IPricing;
import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.order.db.OrderLineDAS;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.pricing.PriceModelBL;
import com.sapienter.jbilling.server.user.EntityBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.db.CompanyDAS;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.Context;
import com.sapienter.jbilling.server.util.audit.EventLogger;
import com.sapienter.jbilling.server.util.db.CurrencyDAS;
import org.apache.log4j.Logger;
import org.springmodules.cache.CachingModel;
import org.springmodules.cache.FlushingModel;
import org.springmodules.cache.provider.CacheProviderFacade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ItemBL {
    private ItemDAS itemDas = null;
    private ItemDTO item = null;
    private static final Logger LOG = Logger.getLogger(ItemBL.class);
    private EventLogger eLogger = null;
    private String priceCurrencySymbol = null;
    private List<PricingField> pricingFields = null;

    // item price cache for getPrice()
    private CacheProviderFacade cache;
    private CachingModel cacheModel;
    private FlushingModel flushModel;

    public ItemBL(Integer itemId) 
            throws SessionInternalError {
        try {
            init();
            set(itemId);
        } catch (Exception e) {
            throw new SessionInternalError("Setting item", ItemBL.class, e);
        } 
    }
    
    public ItemBL() {
        init();
    }
    
    public ItemBL(ItemDTO item) {
        this.item = item;
        init();
    }
    
    public void set(Integer itemId) {
        item = itemDas.find(itemId);
    }
    
    private void init() {
        eLogger = EventLogger.getInstance();        
        itemDas = new ItemDAS();
        cache = (CacheProviderFacade) Context.getBean(Context.Name.CACHE);
        cacheModel = (CachingModel) Context.getBean(
                Context.Name.CACHE_MODEL_ITEM_PRICE);
        flushModel = (FlushingModel) Context.getBean(
                Context.Name.CACHE_FLUSH_MODEL_ITEM_PRICE);
    }
    
    public ItemDTO getEntity() {
        return item;
    }

    public Integer create(ItemDTO dto, Integer languageId) 
             {
        EntityBL entity = new EntityBL(dto.getEntityId());
        if (languageId == null) {
            languageId = entity.getEntity().getLanguageId();
        }
        if (dto.getHasDecimals() != null) {
            dto.setHasDecimals(dto.getHasDecimals());
        } else {
            dto.setHasDecimals(0);
        }
        dto.setDeleted(0);

        item = itemDas.save(dto);

        item.setDescription(dto.getDescription(), languageId);
        updateTypes(dto);
        
        return item.getId();
    }
    
    public void update(Integer executorId, ItemDTO dto, 
            Integer languageId)  {

        eLogger.audit(executorId, null, Constants.TABLE_ITEM, item.getId(),
                EventLogger.MODULE_ITEM_MAINTENANCE, 
                EventLogger.ROW_UPDATED, null, null, null);
        
        item.setNumber(dto.getNumber());
        item.setPriceManual(dto.getPriceManual());
        item.setDescription(dto.getDescription(), languageId);
        item.setPercentage(dto.getPercentage());
        item.setHasDecimals(dto.getHasDecimals());
        
        updateTypes(dto);

        // invalidate item/price cache
        invalidateCache();
    }
    
    private void updateTypes(ItemDTO dto) 
            {
        // update the types relationship        
        Collection types = item.getItemTypes();
        types.clear();
        ItemTypeBL typeBl = new ItemTypeBL();
        // TODO verify that all the categories belong to the same
        // order_line_type_id
        for (int f=0; f < dto.getTypes().length; f++) {
            typeBl.set(dto.getTypes()[f]);
            types.add(typeBl.getEntity());
        }
    }
        
    public void delete(Integer executorId) {
        item.setDeleted(new Integer(1));
        eLogger.audit(executorId, null, Constants.TABLE_ITEM, item.getId(),
                EventLogger.MODULE_ITEM_MAINTENANCE, 
                EventLogger.ROW_DELETED, null, null, null);
        
    }

    public static boolean validate(ItemDTO dto) {
        boolean retValue = true;
        
        if (dto.getDescription() == null || dto.getPrice() == null ||
                dto.getPriceManual() == null || 
                dto.getTypes() == null) {
            retValue = false;
        }
        
        return retValue;
    }
    
    public boolean validateDecimals( Integer hasDecimals ){
        if( hasDecimals == 0 ){
            if(new OrderLineDAS().findLinesWithDecimals(item.getId()) > 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns the basic price for an item and currency, without including purchase quantity or
     * the users current usage in the pricing calculation.
     *
     * @param item item to price
     * @param currencyId currency id of requested price
     * @return The price in the requested currency
     */
    public BigDecimal getPriceByCurrency(ItemDTO item, Integer currencyId)  {
        PricingResult result = new PricingResult(item.getId(), null, currencyId);
        item.getDefaultPrice().applyTo(result, null, null);
        
        return result.getPrice();
    }
    
    /**
     * It will call the main getPrice, with the currency of the userId passed
     * @param userId
     * @param entityId
     * @return
     * @throws SessionInternalError
     */
    public BigDecimal getPrice(Integer userId, Integer entityId) throws SessionInternalError {
        UserBL user = new UserBL(userId);
        return getPrice(userId, user.getCurrencyId(), entityId);
    }    
    /**
     * Will find the right price considering the user's special prices and which
     * currencies had been entered in the prices table.
     * @param userId
     * @param currencyId
     * @param entityId
     * @return The price in the requested currency. It always returns a price,
     * otherwise an exception for lack of pricing for an item
     */
    public BigDecimal getPrice(Integer userId, Integer currencyId, Integer entityId) throws SessionInternalError {
        BigDecimal retValue = null;
        CurrencyBL currencyBL;
        
        if (currencyId == null || entityId == null) {
            throw new SessionInternalError("Can't get a price with null parameters. "
                                           + "currencyId = " + currencyId
                                           + " entityId = " + entityId);
        }
        
        try {
            currencyBL = new CurrencyBL(currencyId);
            priceCurrencySymbol = currencyBL.getEntity().getSymbol();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }

        retValue = getPriceByCurrency(item, currencyId);
        
        // run a plug-in with external logic (rules), if available
        try {
            PluggableTaskManager<IPricing> taskManager
                    = new PluggableTaskManager<IPricing>(entityId, Constants.PLUGGABLE_TASK_ITEM_PRICING);
            IPricing myTask = taskManager.getNextClass();
            
            while(myTask != null) {
                retValue = myTask.getPrice(item.getId(), userId, currencyId, pricingFields, retValue);
                myTask = taskManager.getNextClass();
            }
        } catch (Exception e) {
            throw new SessionInternalError("Item pricing task error", ItemBL.class, e);
        }
        
        return retValue;
    }
    
    public ItemDTO getDTO(Integer languageId, Integer userId, 
            Integer entityId, Integer currencyId) 
            throws SessionInternalError {
        
        ItemDTO dto = new ItemDTO(
            item.getId(),
            item.getInternalNumber(),
            item.getEntity(),
            item.getDescription(languageId),
            item.getPriceManual(),
            item.getDeleted(),
            currencyId,
            null,
            item.getPercentage(),
            null, // to be set right after
            item.getHasDecimals() );  
        
        if (currencyId != null && dto.getPercentage() == null) {
            // it wants one price in particular
            dto.setPrice(getPrice(userId, currencyId, entityId));
        }
        
        // set the types
        Integer types[] = new Integer[item.getItemTypes().size()];
        int index = 0;
        for (Iterator it = item.getItemTypes().iterator(); it.hasNext(); 
                index++) {
            ItemTypeDTO type = (ItemTypeDTO) it.next();
        
            types[index] = type.getId();
            
            // it is assumed that an item belongs to categories that have
            // all the same order_line_type_id
            dto.setOrderLineTypeId(type.getOrderLineTypeId());
        }
        dto.setTypes(types);
    
        return dto;
    }

    public ItemDTO getDTO(ItemDTOEx other) {
        ItemDTO retValue = new ItemDTO();

        if (other.getId() != null) {
            retValue.setId(other.getId());
        }

        retValue.setEntity(new CompanyDAS().find(other.getEntityId()));
        retValue.setNumber(other.getNumber());
        retValue.setPercentage(other.getPercentage());
        retValue.setPriceManual(other.getPriceManual());
        retValue.setDeleted(other.getDeleted());
        retValue.setHasDecimals(other.getHasDecimals());
        retValue.setDescription(other.getDescription());
        retValue.setTypes(other.getTypes());
        retValue.setPromoCode(other.getPromoCode());
        retValue.setCurrencyId(other.getCurrencyId());
        retValue.setPrice(other.getPrice());
        retValue.setOrderLineTypeId(other.getOrderLineTypeId());

        // convert PriceModelWS to PriceModelDTO
        retValue.setDefaultPrice(PriceModelBL.getDTO(other.getDefaultPrice()));
        
        return retValue;
    }

    public ItemDTOEx getWS(ItemDTO other) {
        if (other == null) {
            other = item;
        }

        ItemDTOEx retValue = new ItemDTOEx();
        retValue.setId(other.getId());

        retValue.setEntityId(other.getEntity().getId());
        retValue.setNumber(other.getInternalNumber());
        retValue.setPercentage(other.getPercentage());
        retValue.setPriceManual(other.getPriceManual());
        retValue.setDeleted(other.getDeleted());
        retValue.setHasDecimals(other.getHasDecimals());
        retValue.setDescription(other.getDescription());
        retValue.setTypes(other.getTypes());
        retValue.setPromoCode(other.getPromoCode());
        retValue.setCurrencyId(other.getCurrencyId());
        retValue.setPrice(other.getPrice());
        retValue.setOrderLineTypeId(other.getOrderLineTypeId());

        // convert PriceModelDTO to PriceModelWS
        retValue.setDefaultPrice(PriceModelBL.getWS(other.getDefaultPrice()));

        return retValue;
    }
       
    /**
     * @return
     */
    public String getPriceCurrencySymbol() {
        return priceCurrencySymbol;
    }

    /**
     * Returns all items for the given entity.
     * @param entityId
     * The id of the entity.
     * @return an array of all items
     */
    public ItemDTOEx[] getAllItems(Integer entityId) {
        EntityBL entityBL = new EntityBL(entityId);
        CompanyDTO entity = entityBL.getEntity();
        Collection itemEntities = entity.getItems();
        ItemDTOEx[] items = new ItemDTOEx[itemEntities.size()];

        // iterate through returned item entities, converting them into a DTO
        int index = 0;
        for (ItemDTO item: entity.getItems()) {
            set(item.getId());
            items[index++] = getWS(getDTO(entity.getLanguageId(),
                    null, entityId, entity.getCurrencyId()));
        }

        return items;
    }

    /**
     * Returns all items for the given item type (category) id. If no results
     * are found an empty array is returned.
     *
     * @see ItemDAS#findAllByItemType(Integer)
     *
     * @param itemTypeId item type (category) id
     * @return array of found items, empty if none found
     */
    public ItemDTOEx[] getAllItemsByType(Integer itemTypeId) {
        List<ItemDTO> results = new ItemDAS().findAllByItemType(itemTypeId);
        ItemDTOEx[] items = new ItemDTOEx[results.size()];

        int index = 0;
        for (ItemDTO item : results)
            items[index++] = getWS(item);

        return items;
    }

    public void setPricingFields(List<PricingField> fields) {
        pricingFields = fields;
    }

    public void invalidateCache() {
        cache.flushCache(flushModel);
    }
}
