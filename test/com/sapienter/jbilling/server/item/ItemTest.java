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

package com.sapienter.jbilling.server.item;

import java.util.Vector;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ItemSession;
import com.sapienter.jbilling.interfaces.ItemSessionHome;

public class ItemTest extends TestCase {

    /**
     * Constructor for ItemTest.
     * @param arg0
     */
    public ItemTest(String arg0) {
        super(arg0);
    }

    public void testItemGeneral() {
        try {
            ItemSessionHome customerHome =
                    (ItemSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    ItemSessionHome.class,
                    ItemSessionHome.JNDI_NAME);
            ItemSession remoteSession = customerHome.create();
            
            Integer languageId = new Integer(1);
            Integer entityId = new Integer(1);
            Integer userId = new Integer(1);

            // ITEM TYPE
            // create
            /*
            ItemTypeDTOEx typeDto = new ItemTypeDTOEx();
            typeDto.setDescription("Junit test type");
            typeDto.setEntityId(entityId);
            Integer newTypeId = remoteSession.createType(typeDto, languageId);
            // get
            typeDto = remoteSession.getType(newTypeId, languageId);
            assertNotNull("getting type", typeDto);
            assertEquals("get type - entity", typeDto.getEntityId(), entityId);
            assertEquals("get type - description", typeDto.getDescription(), 
                    "Junit test type");
            // update
            typeDto.setDescription("Modified");
            remoteSession.updateType(userId, typeDto, languageId);
            typeDto = remoteSession.getType(newTypeId, languageId);
            assertEquals("update type ", typeDto.getDescription(), 
                    "Modified");

                   */ 
            //ITEM
            // create
            ItemDTOEx itemDto = new ItemDTOEx();
            itemDto.setEntityId(entityId);
            itemDto.setDescription("Junit test item");
            itemDto.setPriceManual(new Integer(0));
            Integer types[] = new Integer[1];
            //types[0] = newTypeId;
            itemDto.setTypes(types);
            ItemPriceDTOEx price = new ItemPriceDTOEx(null, new Float(7.11F), new Integer(2));
            Vector prices = new Vector();
            prices.add(price);
            itemDto.setPrices(prices);
            Integer newItemId = remoteSession.create(itemDto, languageId);
            // get
            itemDto = remoteSession.get(newItemId, languageId, userId, 
                    null, entityId);
            assertNotNull("getting item", itemDto);
            assertEquals("get item = entity", itemDto.getEntityId(), entityId);
            assertEquals("get item - description", itemDto.getDescription(),
                    "Junit test item");
            assertEquals("get item = manual", itemDto.getPriceManual(), 
                    new Integer(0));
            // update
            itemDto.setDescription("Modified");
            remoteSession.update(userId, itemDto, languageId);
            itemDto = remoteSession.get(newItemId, languageId, userId, 
                    new Integer(3), entityId);
            assertEquals("update item - description", itemDto.getDescription(),
                    "Modified");
                    
            // PRICE
            ItemUserPriceDTOEx priceDto = new ItemUserPriceDTOEx();
            priceDto.setItemId(newItemId);
            priceDto.setPrice(new Float(100F));
            priceDto.setUserId(userId);
            priceDto.setCurrencyId(new Integer(4));
            Integer priceId = remoteSession.createPrice(userId, priceDto);
            // get
            priceDto = remoteSession.getPrice(priceId);
            assertNotNull(priceDto);
            assertEquals("get price - price", priceDto.getPrice(), 
                    new Float(100F));
            // update
            priceDto.setPrice(new Float(29.10F));
            remoteSession.updatePrice(userId, priceDto);
            // verify that if I ask for this item, it uses this price
            itemDto = remoteSession.get(newItemId, languageId, userId, 
                    new Integer(4), entityId);
            assertEquals("get item - new price", itemDto.getPrice(), 
                    new Float(29.10F));
            // verify that the customer gets the partner price
            itemDto = remoteSession.get(new Integer(1), languageId, new Integer(7), 
                    new Integer(1), entityId);
            assertNotNull("customer price null", itemDto);
            assertEquals("customer belongs to partner price", itemDto.getPrice(), 
                    new Float(90F));
            itemDto = remoteSession.get(new Integer(1), languageId, new Integer(10), 
                    new Integer(1), entityId);
            assertNotNull("2 customer price null", itemDto);
            assertEquals("customer doesnt' belong to partner price", itemDto.getPrice(), 
                    new Float(109.99F));
            itemDto = remoteSession.get(new Integer(1), languageId, new Integer(9), 
                    new Integer(1), entityId);
            assertNotNull("3 customer price null", itemDto);
            assertEquals("customer own price", itemDto.getPrice(), 
                    new Float(87F));


            // PROMOTION
            //create
            PromotionDTOEx promotionDto = new PromotionDTOEx();
            promotionDto.setCode("JUNIT-PROMO");
            promotionDto.setItemId(newItemId);
            promotionDto.setOnce(new Integer(1));
            Integer newPromoId = remoteSession.createPromotion(userId, 
                    entityId, promotionDto);
            // get 
            promotionDto = remoteSession.getPromotion(newPromoId);
            PromotionDTOEx another = remoteSession.getPromotion(entityId, 
                    promotionDto.getCode());
            assertEquals("get promo", another.getId(), promotionDto.getId());
            assertEquals("get promo - code", promotionDto.getCode(),
                    "JUNIT-PROMO");
            assertEquals("get promo - once", promotionDto.getOnce(),
                    new Integer(1));
            // update
            promotionDto.setNotes("notes from junit");
            remoteSession.updatePromotion(userId, promotionDto);
            promotionDto = remoteSession.getPromotion(newPromoId);
            assertEquals("update promo - notes", promotionDto.getNotes(),
                    "notes from junit");
            
            // delete the item
            remoteSession.delete(userId, newItemId);
            itemDto = remoteSession.get(newItemId, languageId, userId, 
                    new Integer(4), entityId);
            assertEquals("delete item ", itemDto.getDeleted(), new Integer(1));
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("Exception : " + e.getMessage());
        }
    }

}
