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

/*
 * Created on Dec 18, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.item;

import junit.framework.TestCase;

import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

/**
 * @author Emil
 */
public class WSTest  extends TestCase {
      

    public void testCreate() {
        try {
        	JbillingAPI api = JbillingAPIFactory.getAPI();
            /*
             * Create
             */
            ItemDTOEx newItem = new ItemDTOEx();
            newItem.setDescription("an item from ws");
            newItem.setPrice(new Float(29.5));
            
            
            Integer types[] = new Integer[1];
            types[0] = new Integer(1);
            newItem.setTypes(types);
            newItem.setPriceManual(new Integer(0));
            
            System.out.println("Creating item ...");
            Integer ret = api.createItem(newItem);
            assertNotNull("The item was not created", ret);
            System.out.println("Done!");
            
           
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

    public void testGetAllItems() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();
        
            /*
             * Get all items
             */
             
            System.out.println("Getting all items");
            ItemDTOEx[] items =  api.getAllItems();
            assertNotNull("The items were not retrieved", items);
            assertTrue("Wrong number of items", items.length == 5);

            assertEquals("Description", "Lemonade - 1 per day monthly pass", 
                    items[0].getDescription());
            assertEquals("Price", new Float(10), items[0].getPrice());
            assertEquals("Price Vector", new Float(10), 
                    ((ItemPriceDTOEx) items[0].getPrices().get(0)).getPrice());
            assertEquals("ID", new Integer(1), items[0].getId());
            assertEquals("Number", "DP-1", items[0].getNumber());
            assertEquals("Type 1", new Integer(1), items[0].getTypes()[0]);

            assertEquals("Description", "Lemonade - all you can drink monthly", 
                    items[1].getDescription());
            assertEquals("Price", new Float(20), items[1].getPrice());
            assertEquals("Price Vector", new Float(20), 
                    ((ItemPriceDTOEx) items[1].getPrices().get(0)).getPrice());
            assertEquals("ID", new Integer(2), items[1].getId());
            assertEquals("Number", "DP-2", items[1].getNumber());
            assertEquals("Type 1", new Integer(1), items[1].getTypes()[0]);

            assertEquals("Description", "Coffee - one per day - Monthly", 
                    items[2].getDescription());
            assertEquals("Price", new Float(15), items[2].getPrice());
            assertEquals("Price Vector", new Float(15), 
                    ((ItemPriceDTOEx) items[2].getPrices().get(0)).getPrice());
            assertEquals("ID", new Integer(3), items[2].getId());
            assertEquals("Number", "DP-3", items[2].getNumber());
            assertEquals("Type 1", new Integer(1), items[2].getTypes()[0]);

            assertEquals("Description", "10% Elf discount.", 
                    items[3].getDescription());
            assertEquals("Percentage", new Float(-10.00), 
                    items[3].getPercentage());
            assertEquals("ID", new Integer(14), items[3].getId());
            assertEquals("Number", "J-01", items[3].getNumber());
            assertEquals("Type 12", new Integer(12), items[3].getTypes()[0]);

            assertEquals("Description", "an item from ws", 
                    items[4].getDescription());
            assertEquals("Price", new Float(29.5), items[4].getPrice());
            assertEquals("Price Vector", new Float(29.5), 
                    ((ItemPriceDTOEx) items[4].getPrices().get(0)).getPrice());
            assertEquals("ID", new Integer(24), items[4].getId());
            assertEquals("Type 1", new Integer(1), items[4].getTypes()[0]);

            System.out.println("Done!");
            
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

}
