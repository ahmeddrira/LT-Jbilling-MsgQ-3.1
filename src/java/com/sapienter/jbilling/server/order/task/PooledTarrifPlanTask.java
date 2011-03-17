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

import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.order.event.NewQuantityEvent;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.admin.ParameterDescription;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.util.AttributeUtils;
import com.sapienter.jbilling.server.system.event.Event;
import com.sapienter.jbilling.server.system.event.task.IInternalEventsTask;
import com.sapienter.jbilling.server.user.db.CustomerDTO;
import com.sapienter.jbilling.server.user.db.UserDTO;


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
        NewQuantityEvent.class
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
            NewQuantityEvent nq = (NewQuantityEvent) event;
            OrderLineDTO orderLine= nq.getOrderLine();
            
            LOG.debug("orderLine.getItemId()=" + orderLine.getItemId());
            
            try { 
	            //check if the item of this Order matches the item that needs to be pooled/added
	            if (orderLine.getItemId().equals(new Integer((String) parameters.get(PARAMETER_POOL_ADD_PRODUCT_ID.getName())))) {
	            	updateIncludedQuantity(nq);
	            } else if (orderLine.getItemId().equals(new Integer((String) parameters.get(PARAMETER_POOL_USER_PRODUCT_ID.getName())))) {
	            	updateParentsCurrentOrderQuantity(nq);
	            }
            } catch (NumberFormatException e) {
            	throw new PluggableTaskException("PooledTarrifPlanTask parameter values must be an integer!", e);
            }
		}

	}
	
	private void updateParentsCurrentOrderQuantity(NewQuantityEvent nq) {
		LOG.debug("Method: updateParentsCurrentOrderQuantity");
		OrderLineDTO orderLine= nq.getOrderLine();
		
		CustomerDTO customer= orderLine.getPurchaseOrder().getBaseUserByUserId().getCustomer();
		
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
    				if (nq.getNewQuantity().subtract(nq.getOldQuantity()).intValue() > 0 )
    				{
    					LOG.debug("adding.. " + nq.getNewOrderLine());
    					order.getLines().add(nq.getNewOrderLine());
    				}
    				break outer;
    			}
    		}
		}
		
	}
	
	private void updateIncludedQuantity(NewQuantityEvent nq) {
		LOG.debug("updateIncludedQuantity");
		OrderLineDTO orderLine= nq.getOrderLine();
    	UserDTO user= orderLine.getPurchaseOrder().getBaseUserByUserId();
    	
    	if (null != user) { 
    		//find if one of the items of the user's orders matches the item that receives the pool
    		outer:
    		for (OrderDTO order: user.getOrders()) {
    			for(OrderLineDTO line: order.getLines()) {
    				LOG.debug("Line Item iD: " + line.getItemId() );
    				if (line.getItemId().equals(new Integer((String) parameters.get(PARAMETER_POOL_RECEIVE_PRODUCT_ID.getName())))) {
    					LOG.debug("Found matching item id to update included quantity");
    					if (null != line.getItem().getDefaultPrice() && null != line.getItem().getDefaultPrice().getType())
    					{
    						LOG.debug("Plan has PriceModelDTO. " + line.getItem().getDefaultPrice().getType());
    						if (line.getItem().getDefaultPrice().getType().equals(PriceModelStrategy.GRADUATED)) {
        						PriceModelDTO priceModel= line.getItem().getDefaultPrice();
        						BigDecimal multiPlierQty= new BigDecimal((String) parameters.get(PARAMETER_QUANTITY_MULTIPLIER.getName()));
        						BigDecimal included = AttributeUtils.getDecimal(priceModel.getAttributes(), "included");
        						
        						LOG.debug("Adding amount " + multiPlierQty.multiply( nq.getNewQuantity().subtract(nq.getOldQuantity())  ));
        						LOG.debug("included is " + included );
        						
        						//add or delete from pool in units of multiplierQuantity for each quantity added or deleted
        						included.add( multiPlierQty.multiply( nq.getNewQuantity().subtract(nq.getOldQuantity())  ) );
        						//set new included quantity to the existing model
        						priceModel.getAttributes().put("included", included.toString());
        						break outer;
        					}
    					}
    				}
    			}
    		}
    	}
	}

}
