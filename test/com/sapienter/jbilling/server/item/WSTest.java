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
