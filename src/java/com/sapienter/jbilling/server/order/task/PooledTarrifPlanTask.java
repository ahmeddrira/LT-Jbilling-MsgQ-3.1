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

package com.sapienter.jbilling.server.order.task;

import java.math.BigDecimal;

//import org.apache.log4j.Logger;
import org.apache.log4j.Logger;
import com.sapienter.jbilling.server.pricing.db.PriceModelStrategy;

import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.PlanDTO;
import com.sapienter.jbilling.server.item.db.PlanItemDTO;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.order.event.NewQuantityEvent;
import com.sapienter.jbilling.server.order.event.NewOrderEvent;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.admin.ParameterDescription;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.util.AttributeUtils;
import com.sapienter.jbilling.server.system.event.Event;
import com.sapienter.jbilling.server.system.event.task.IInternalEventsTask;
import com.sapienter.jbilling.server.user.db.CustomerDTO;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.order.OrderLineBL;

/**
 * @author Vikas Bodani
 * @since 17 March 2011
 */
public class PooledTarrifPlanTask extends PluggableTask implements IInternalEventsTask {

	private static final ParameterDescription PARAMETER_POOL_ADD_PRODUCT_ID 
		= new ParameterDescription("pool_add_product_id", true, ParameterDescription.Type.STR);
	private static final ParameterDescription PARAMETER_POOL_RECEIVE_PRODUCT_ID = 
		new ParameterDescription("pool_receive_product_id", true, ParameterDescription.Type.STR);
	private static final ParameterDescription PARAMETER_POOL_USER_PRODUCT_ID = 
		new ParameterDescription("pool_user_product_id", true, ParameterDescription.Type.STR);
	private static final ParameterDescription PARAMETER_QUANTITY_MULTIPLIER = 
		new ParameterDescription("quantity_multiplier", true, ParameterDescription.Type.STR);

	//initializer for pluggable params
    { 
    	descriptions.add(PARAMETER_POOL_RECEIVE_PRODUCT_ID);
    	descriptions.add(PARAMETER_POOL_USER_PRODUCT_ID);
    	descriptions.add(PARAMETER_QUANTITY_MULTIPLIER);
    	descriptions.add(PARAMETER_POOL_ADD_PRODUCT_ID);
    }
    
	private static final Logger LOG = Logger.getLogger(PooledTarrifPlanTask.class);
	
	@SuppressWarnings("unchecked")
    private static final Class<Event> events[] = new Class[] { 
        NewQuantityEvent.class,
        NewOrderEvent.class
    };
	
	
	public Class<Event>[] getSubscribedEvents() {
		return events;
	}

	public void process(Event event) throws PluggableTaskException {
		LOG.debug(PARAMETER_POOL_ADD_PRODUCT_ID + " value is " + parameters.get(PARAMETER_POOL_ADD_PRODUCT_ID.getName()));
		LOG.debug(PARAMETER_POOL_USER_PRODUCT_ID + " value is " + parameters.get(PARAMETER_POOL_USER_PRODUCT_ID.getName()));
		LOG.debug(PARAMETER_POOL_RECEIVE_PRODUCT_ID + " value is " + parameters.get(PARAMETER_POOL_RECEIVE_PRODUCT_ID.getName()));
		LOG.debug(PARAMETER_QUANTITY_MULTIPLIER + " value is " + parameters.get(PARAMETER_QUANTITY_MULTIPLIER.getName()));
		
		if (event instanceof NewQuantityEvent) {
			LOG.debug("NewQuantityEvent");
            NewQuantityEvent nq = (NewQuantityEvent) event;
            //nq.getOrderLine will never be null. The purpose of getting this orderLine
            //is to strictly find the matching ITem ID
            handleUpdateAction(nq.getOrderLine(), event);
		} else if (event instanceof NewOrderEvent) {
			LOG.debug("NewOrderEvent");
			NewOrderEvent no= (NewOrderEvent) event;
			for(OrderLineDTO orderLine: no.getOrder().getLines()) {
				handleUpdateAction(orderLine, event); 
			}
		}
	}
	
	private void handleUpdateAction(OrderLineDTO orderLine, Event event) throws PluggableTaskException { 
		try { 
			LOG.debug("Order Line ID " + orderLine.getId());
			LOG.debug("orderLine.getItemId()=" + orderLine.getItemId());
            //check if the item of this Order matches the item that needs to be pooled/added
            if (orderLine.getItemId().equals(new Integer((String) parameters.get(PARAMETER_POOL_ADD_PRODUCT_ID.getName())))) {
            	updateIncludedQuantity(orderLine, event);
            } else if (orderLine.getItemId().equals(new Integer((String) parameters.get(PARAMETER_POOL_USER_PRODUCT_ID.getName())))) {
            	updateParentsCurrentOrderQuantity(orderLine, event);
            }
        } catch (NumberFormatException e) {
        	throw new PluggableTaskException("PooledTarrifPlanTask parameter values must be an integer!", e);
        }
	}
	
	private void updateParentsCurrentOrderQuantity(OrderLineDTO line, Event event) {
		LOG.debug("Method: updateParentsCurrentOrderQuantity");
		//OrderLineDTO line= nq.getOrderLine();
		
		CustomerDTO customer= line.getPurchaseOrder().getBaseUserByUserId().getCustomer();
		
		if ( customer.getInvoiceChild().intValue() > 0 ) {
			//do nothing, the customer is invoiced as child
			LOG.debug("Returning, doing nothing. Customer is invoiced as child.");
			return;
		}
		//else invoice to parent's current order
		CustomerDTO parent= customer.getParent();
		UserDTO user= parent.getBaseUser();
		
		if (null != user) { 
    		//find the user's current Order
    		outer:
    		for (OrderDTO order: user.getOrders()) {
    			LOG.debug("Is User Order Current: " + " Order ID " + order.getId() + " is " + (order.getIsCurrent().intValue() > 0));
    			if (order.getIsCurrent().intValue() > 0 ) {
    				//new lines can be added to the usage of parent only in case of additional usage
    				//usage cannot be negetive
    				
    				BigDecimal addlQty= determineAdditionalQuantity(event, line);
    				if (addlQty.compareTo(BigDecimal.ZERO) > 0 )
    				{
    					//below, toAdd line is same as Order Line for NewOrderEvent.
    					//Whereas in case of NewQuantityEvent, we have to be careful
    					//to get the right OrderLineDTO
    					OrderLineDTO toAdd= line;
    					if (event instanceof NewQuantityEvent) {
    						NewQuantityEvent nq = (NewQuantityEvent) event;
	    					if (nq.getOldQuantity().intValue() == 0 ) { 
	    						toAdd=nq.getOrderLine();
	    					} else {
	    						toAdd= nq.getNewOrderLine();
	    					}
    					}
    					LOG.debug("Item ID: " + toAdd.getItemId());
    					boolean orderExists= false;
    					for(OrderLineDTO orderLine: order.getLines()) {
    						LOG.debug("Order Line Item: " + (null != orderLine? orderLine.getItemId() : null) );
    						if (orderLine.getItemId().intValue() == Integer.parseInt((String) parameters.get(PARAMETER_POOL_USER_PRODUCT_ID.getName()))) {
    							//found an existing orderLine with the usage item, update its quantity
    							orderExists=true;
    							orderLine.setQuantity(orderLine.getQuantity().add(addlQty));
    							LOG.debug("New Quantity set to " + orderLine.getQuantity());
    						}
    					}
    					//if a matching OrderLineItem was not found, add this line to the order 
    					if (!orderExists) { 
	    					addLineToOrder(order, toAdd);
    					}
    					//order.getLines().add(nq.getNewOrderLine());
    				}
    				break outer;
    			}
    		}
		}
		
	}
	
	private static void addLineToOrder(OrderDTO order, OrderLineDTO toAdd) {
		OrderLineDTO temp= new OrderLineDTO();
		temp.setOrderLineType(toAdd.getOrderLineType());
        temp.setItem(toAdd.getItem());
        temp.setAmount(toAdd.getAmount());
        temp.setQuantity(toAdd.getQuantity());
        temp.setPrice(toAdd.getPrice());
        temp.setCreateDatetime(new java.util.Date());
        LOG.debug("Adding new Order Line.. " + temp);
		OrderLineBL.addLine(order, temp, false);
	}
	
	//OrderLineDTO line passed here is utilized only in case of a NewOrderEvent. 
	//Only in a new Order event, the quantity will come from the Order Line itself.
	private static BigDecimal determineAdditionalQuantity(Event event, OrderLineDTO line) {
		BigDecimal retVal= null;
		if (event instanceof NewQuantityEvent) {
            NewQuantityEvent nq = (NewQuantityEvent) event;
            LOG.debug("New quantity: " + nq.getNewQuantity() + " Old Quantity: " + nq.getOldQuantity());
            retVal= nq.getNewQuantity().subtract(nq.getOldQuantity());
		} else if (event instanceof NewOrderEvent) {
			retVal= line.getQuantity();
		}
		return retVal;
	}
	
	private void updateIncludedQuantity(OrderLineDTO orderLine, Event event) {
		LOG.debug("updateIncludedQuantity");

    	UserDTO user= orderLine.getPurchaseOrder().getBaseUserByUserId();
    	LOG.debug("For User Id: " + user.getId());
    	
    	if (null != user) { 
    		//find if one of the items of the user's orders matches the item that receives the pool
    		outer:
    		for (OrderDTO order: user.getOrders()) {
    			LOG.debug("User " + user.getId() + " order " + order.getId());
    			for(OrderLineDTO line: order.getLines()) {
    				LOG.debug("Line Item iD: " + line.getItemId() );
    				ItemDTO lineItem= line.getItem();
    				if ( null != lineItem && lineItem.getPlans().size() > 0 ) {
    					//we need to match the receive item it with the item id of the PlanItem of the orders items
    					for (PlanDTO plan: lineItem.getPlans()) {
    						for (PlanItemDTO planItem: plan.getPlanItems()) {
    							LOG.debug("Plan Items Item ID: " + planItem.getItem().getId());
    							//check if planItems itemid matches the receive product id
    							if (planItem.getItem().getId() == Integer.parseInt(parameters.get(PARAMETER_POOL_RECEIVE_PRODUCT_ID.getName())) ) {
    								LOG.debug("Found matching item id to update included quantity");
    								LOG.debug("PlanItem has PriceModelDTO. " + planItem.getModel().getType());
    								//check if the plan item has graduated pricing
    								if (planItem.getModel().getType().equals(PriceModelStrategy.GRADUATED)) {
    									LOG.debug("Strategy Graduated");
    									//check if Item Id matches
    									PriceModelDTO priceModel= planItem.getModel();
    	        						BigDecimal multiPlierQty= new BigDecimal((String) parameters.get(PARAMETER_QUANTITY_MULTIPLIER.getName()));
    	        						BigDecimal included = AttributeUtils.getDecimal(priceModel.getAttributes(), "included");
    	        						
    	        						//careful with the call below, the orderLine passed is used to 
    	        						//determine additional quantity added in case of NewOrderEvent
    	        						BigDecimal addlQty= determineAdditionalQuantity(event, orderLine);
    	        						
    	        						LOG.debug("Adding amount " + multiPlierQty.multiply( addlQty ));
    	        						LOG.debug("included was " + included );
    	        						
    	        						//add or delete from pool in units of multiplierQuantity for each quantity added or deleted
    	        						included= included.add( multiPlierQty.multiply( addlQty ) );
    	        						
    	        						LOG.debug("included is " + included );
    	        						
    	        						//set new included quantity to the existing model
    	        						priceModel.getAttributes().put("included", included.toPlainString());
    	        						break outer;
    								}
    							}
    						}
    					}
    				}
    			}
    		}
    	}
	}

}
