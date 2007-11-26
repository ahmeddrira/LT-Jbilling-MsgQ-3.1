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
