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
package com.sapienter.jbilling.server.item.tasks;

import java.math.BigDecimal;
import java.util.Vector;

import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.mediation.Record;
import com.sapienter.jbilling.server.order.NewOrderDTO;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.OrderLineDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;

public class BasicItemManager extends PluggableTask implements IItemPurchaseManager {

    protected ItemDTOEx item = null;
    private OrderLineDTOEx latestLine = null;
    
    public void addItem(Integer itemID, Integer quantity, Integer language,
            Integer userId, Integer entityId, Integer currencyId,
            NewOrderDTO newOrder, Vector<Record> records) throws TaskException {

        // check if the item is already in the order
        OrderLineDTOEx line = (OrderLineDTOEx) newOrder.getOrderLine(itemID);

        OrderLineDTOEx myLine = new OrderLineDTOEx();
        myLine.setItemId(itemID);
        myLine.setQuantity(quantity);
        populateOrderLine(language, userId, entityId, currencyId, myLine, records);
        if (line == null) { // not yet there
            newOrder.setOrderLine(itemID, myLine);
            latestLine = myLine;
        } else {
            // the item is there, I just have to update the quantity
            line.setQuantity( new Integer(
                    line.getQuantity().intValue() + quantity.intValue()));
            // and also the total amount for this order line
            BigDecimal dec = new BigDecimal(line.getAmount().toString());
            dec = dec.add(new BigDecimal(myLine.getAmount().toString()));
            line.setAmount(new Float(dec.floatValue()));
            latestLine = line;
        }
    }
    
    /**
     * line can not be null, nor line.getItemId. All the rest will be populated
     * @param language
     * @param userId
     * @param entityId
     * @param currencyId
     * @param line
     */
    public void populateOrderLine(Integer language, Integer userId, 
            Integer entityId, Integer currencyId, OrderLineDTOEx line, 
            Vector<Record> records) {
        ItemBL itemBL = new ItemBL(line.getItemId());
        if (records != null) {
            Vector<PricingField> fields = new Vector<PricingField>();
            for (Record record : records) {
                fields.addAll(record.getFields());
            }
            itemBL.setPricingFields(fields);
        }        
        item = itemBL.getDTO(language, userId, entityId, 
                currencyId);

        Boolean editable = OrderBL.lookUpEditable(item.getOrderLineTypeId());

        if (line.getDescription() == null) {
            line.setDescription(item.getDescription());
        }
        if (line.getQuantity() == null) {
            line.setQuantity(1);
        }
        if (line.getPrice() == null) {
            line.setPrice((item.getPercentage() == null) ? item.getPrice() :
                item.getPercentage());
        }
        if (line.getAmount() == null) {
            BigDecimal additionAmount = null;
            // normal price, multiply by quantity
            if (item.getPercentage() == null) {
                additionAmount = new BigDecimal(line.getPrice().toString());
                additionAmount = additionAmount.multiply(
                        new BigDecimal(line.getQuantity().toString()));
            } else {
                // percentage ignores the quantity
                additionAmount = new BigDecimal(item.getPercentage().toString());
            }
            line.setAmount(new Float(additionAmount.floatValue()));
        }
        line.setItemPrice(0);
        line.setCreateDate(null);
        line.setDeleted(0);
        line.setTypeId(item.getOrderLineTypeId());
        line.setEditable(editable);
        line.setItem(item);
    }

    public OrderLineDTOEx getLatestLine() {
        return latestLine;
    }
}
