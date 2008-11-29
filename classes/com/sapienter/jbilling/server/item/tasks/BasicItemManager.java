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
package com.sapienter.jbilling.server.item.tasks;

import java.math.BigDecimal;
import java.util.Vector;

import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.ItemDecimalsException;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.ItemDAS;
import com.sapienter.jbilling.server.mediation.Record;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;

public class BasicItemManager extends PluggableTask implements IItemPurchaseManager {

    protected ItemDTO item = null;
    private OrderLineDTO latestLine = null;
    
    public void addItem(Integer itemID, Integer quantity, Integer language,
            Integer userId, Integer entityId, Integer currencyId,
            OrderDTO newOrder, Vector<Record> records) throws TaskException {
    	
    	addItem(itemID, new Double(quantity), language, userId, entityId, currencyId,
            newOrder, records);
    }
    
    public void addItem(Integer itemID, Double quantity, Integer language,
            Integer userId, Integer entityId, Integer currencyId,
            OrderDTO newOrder, Vector<Record> records) throws TaskException {

        // Validate decimal quantity with the item
    	if( quantity != null && (quantity % 1) > 0 ) {
    		
    		try {
	        	ItemBL bl = new ItemBL();
	        	bl.set(itemID);
		        if( bl.getEntity().getHasDecimals().intValue() == 0 ) {
		        	latestLine = null;
		        	throw new ItemDecimalsException( "Item does not allow Decimals" );
		        }
    		} catch( Exception e ) {
    			throw new TaskException(e);
    		}
    	}
        
    	// check if the item is already in the order
        OrderLineDTO line = (OrderLineDTO) newOrder.getLine(itemID);

        OrderLineDTO myLine = new OrderLineDTO();
        ItemDTO item = new ItemDTO();
        item.setId(itemID);
        myLine.setItem(item);
        myLine.setQuantity(quantity);
        populateOrderLine(language, userId, entityId, currencyId, myLine, records);
        if (line == null) { // not yet there
            newOrder.getLines().add(myLine);
            latestLine = myLine;
        } else {
            // the item is there, I just have to update the quantity
        	BigDecimal dec = new BigDecimal( line.getQuantity().toString() );
        	dec = dec.add( new BigDecimal( quantity.toString() ) );
            line.setQuantity( new Double( dec.doubleValue() ));
            
            // and also the total amount for this order line
            dec = new BigDecimal(line.getAmount().toString());
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
            Integer entityId, Integer currencyId, OrderLineDTO line, 
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
            line.setQuantity(1.0);
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
        line.setCreateDatetime(null);
        line.setDeleted(0);
        line.setTypeId(item.getOrderLineTypeId());
        line.setEditable(editable);
        line.setItem(item);
        
        // also add the JPA item (no proxy, or the gui will complain)
        line.setItem(new ItemDAS().findNow(line.getItemId()));
    }

    public OrderLineDTO getLatestLine() {
        return latestLine;
    }
}
