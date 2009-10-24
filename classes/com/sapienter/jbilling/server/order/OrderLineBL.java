/*
 * jBilling - The Enterprise Open Source Billing System
 * Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

 * This file is part of jbilling.

 * jbilling is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * jbilling is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.order;

import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.user.UserBL;
import java.math.BigDecimal;

/**
 *
 * @author emilc
 */
public class OrderLineBL {

    public static void addLine(OrderDTO order, OrderLineDTO line) {
        UserBL user = new UserBL(order.getUserId());
        addItem(line.getItemId(), line.getQuantity(), user.getLanguage(), order.getUserId(),
                user.getEntity().getEntity().getId(), order.getCurrencyId(), order, line);
    }


    public static void addItem(OrderDTO order, Integer itemId) {
        addItem(order, itemId, 1.0);
    }

    public static void addItem(OrderDTO order, Integer itemId, Integer quantity) {
        addItem(order, itemId, quantity.doubleValue());
    }

    public static void addItem(OrderDTO order, Integer itemId, Double quantity) {
        UserBL user = new UserBL(order.getUserId());
        addItem(itemId, quantity, user.getLanguage(), order.getUserId(), 
                user.getEntity().getEntity().getId(), order.getCurrencyId(), order, null);
    }

    public static void addItem(Integer itemID, Double quantity, Integer language,
            Integer userId, Integer entityId, Integer currencyId,
            OrderDTO newOrder, OrderLineDTO myLine) {
        // check if the item is already in the order
        OrderLineDTO line = (OrderLineDTO) newOrder.getLine(itemID);

        if (myLine == null) {
            myLine = new OrderLineDTO();
            ItemDTO item = new ItemDTO();
            item.setId(itemID);
            myLine.setItem(item);
            myLine.setQuantity(quantity);
        }
        populateWithSimplePrice(language, userId, entityId, currencyId, itemID, myLine);
        myLine.setDefaults();
        if (line == null) { // not yet there
            newOrder.getLines().add(myLine);
            myLine.setPurchaseOrder(newOrder);
        } else {
            // the item is there, I just have to update the quantity
        	BigDecimal dec = new BigDecimal( line.getQuantity().toString() );
        	dec = dec.add( new BigDecimal( quantity.toString() ) );
            line.setQuantity( new Double( dec.doubleValue() ));

            // and also the total amount for this order line
            dec = new BigDecimal(line.getAmount().toString());
            dec = dec.add(new BigDecimal(myLine.getAmount().toString()));
            line.setAmount(new Float(dec.floatValue()));
        }
    }
    /**
     * Returns an order line with everything correctly
     * initialized. It does not call plug-ins to set the price
     * @param language
     * @param userId
     * @param entityId
     * @param currencyId
     * @return
     */
    public static void populateWithSimplePrice(Integer language, Integer userId,
            Integer entityId, Integer currencyId, Integer itemId, OrderLineDTO line) {

        ItemBL itemBl = new ItemBL(itemId);
        ItemDTO item = itemBl.getEntity();

        // it takes the line type of the first category this item belongs too...
        // TODO: redo, when item categories are redone
        Integer type = item.getItemTypes().iterator().next().getOrderLineTypeId();
        Boolean editable = OrderBL.lookUpEditable(type);

        if (line.getDescription() == null) {
            line.setDescription(item.getDescription(language));
        }
        if (line.getQuantity() == null) {
            line.setQuantity(1.0);
        }
        if (line.getPrice() == null) {
            line.setPrice((item.getPercentage() == null) ? 
                itemBl.getPriceByCurrency(currencyId, entityId) :
                item.getPercentage());
        }
        if (line.getAmount() == null) {
            BigDecimal additionAmount = null;
            // normal price, multiply by quantity
            if (item.getPercentage() == null) {
                additionAmount = line.getPrice();
                additionAmount = additionAmount.multiply(
                        new BigDecimal(line.getQuantity()));
            } else {
                // percentage ignores the quantity
                additionAmount = item.getPercentage();
            }
            line.setAmount(new Float(additionAmount.floatValue()));
        }
        line.setItemPrice(0);
        line.setCreateDatetime(null);
        line.setDeleted(0);
        line.setTypeId(type);
        line.setEditable(editable);
        line.setItem(item);
    }
}
