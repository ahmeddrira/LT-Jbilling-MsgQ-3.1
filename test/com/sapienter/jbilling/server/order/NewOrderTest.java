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

package com.sapienter.jbilling.server.order;

import javax.ejb.FinderException;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.NewOrderSession;
import com.sapienter.jbilling.interfaces.NewOrderSessionHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * Test Cases for the user classes
 * 
 * @author emilc
 *
 */
public class NewOrderTest extends TestCase {

    /**
     * Constructor for GeneralTest.
     * @param name
     */
    public NewOrderTest(String name) {
        super(name);
    }

    public void testLinesManipulation() {
        try {
            Integer userId = new Integer(1);
            Integer entityId = new Integer(1);
            NewOrderDTO summary = new NewOrderDTO();
            summary.setPeriod(new Integer(1));
            summary.setUserId(userId);
            NewOrderSessionHome nOrderHome =
                    (NewOrderSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    NewOrderSessionHome.class,
                    NewOrderSessionHome.JNDI_NAME); 

            // complete this order's data
            summary.setBillingTypeId(Constants.ORDER_BILLING_POST_PAID);

            NewOrderSession nOrderS = nOrderHome.create(summary, new Integer(1));

            NewOrderDTO thisOrder = nOrderS.addItem(new Integer(1), 
                    new Integer(5), userId, entityId);
            assertNotNull(thisOrder);
            // add a few items
            thisOrder = nOrderS.addItem(new Integer(2), new Integer(3), userId, entityId);
            thisOrder = nOrderS.addItem(new Integer(5), new Integer(1), userId, entityId);
            thisOrder = nOrderS.addItem(new Integer(10), new Integer(1000),
                    userId, entityId);
            assertEquals(thisOrder.getNumberOfLines(), new Integer(4));
            try {
                // let's test the reaction to an unexsiting item
                thisOrder = nOrderS.addItem(new Integer(99999), 
                        new Integer(1000), userId, entityId);
                fail("This item shouldn't have been added");
            } catch (FinderException e) {
                // good
            }
            // let's test the addition of more units to an existing line
            thisOrder = nOrderS.addItem(new Integer(2), new Integer(3), userId, entityId);
            assertEquals(
                ((OrderLineDTOEx) (thisOrder
                    .getOrderLinesMap()
                    .get(new Integer(2))))
                    .getQuantity()
                    .intValue(),
                6);
            assertEquals(thisOrder.getNumberOfLines(), new Integer(4));
 
            // let's try a delete...
            thisOrder = nOrderS.deleteItem(new Integer(2));
            assertEquals(thisOrder.getNumberOfLines(), new Integer(3));

            // now make a modification and test that it recalculates
            // a line total well
            OrderLineDTOEx thisLine =
                (OrderLineDTOEx) thisOrder.getOrderLine("10");
            assertNotNull(thisLine); // it has to be there
            thisLine.setQuantity(new Integer(10));
            thisLine.setPrice(new Float(10));
            thisOrder = nOrderS.recalculate(thisOrder, new Integer(1));
            thisLine = (OrderLineDTOEx) thisOrder.getOrderLine("10");
            assertTrue(thisLine.getAmount().floatValue() == 10F * 10F);

            // finally, try to create the order and see
            // it'd be good to be able to fetch this order and verify it has
            // what it should. Right now is not available.
            Integer newOrderId = nOrderS.createUpdate(new Integer(1), 
                    new Integer(1), thisOrder);
            assertNotNull(newOrderId);

            System.out.println("Order " + newOrderId + " created.");
            System.out.println(thisOrder);

        } catch (FinderException e) {
            fail("There's some data missing in the database (items?");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

}
