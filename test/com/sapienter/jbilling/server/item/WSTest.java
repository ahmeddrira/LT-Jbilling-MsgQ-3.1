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

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;

/**
 * @author Emil
 */
public class WSTest extends TestCase {
      

    public void testCreate() {
        try {
        	String endpoint = "http://localhost/jboss-net/services/billing";
        
            
            Service  service = new Service();
            Call  call = (Call) service.createCall();
            call.setTargetEndpointAddress( new java.net.URL(endpoint) );
            call.setOperationName("createItem");
            //call.setReturnClass(Integer.class);
            call.setUsername("admin");
            call.setPassword("asdfasdf");
            

            // ItemDTOEx            
            QName qn = new QName("http://www.sapienter.com/billing", "ItemDTOEx");
            BeanSerializerFactory ser1 = new BeanSerializerFactory(
                    ItemDTOEx.class, qn);
            BeanDeserializerFactory ser2 = new BeanDeserializerFactory (
                    ItemDTOEx.class, qn);
            call.registerTypeMapping(ItemDTOEx.class, qn, ser1, ser2); 

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
            Integer ret = (Integer) call.invoke( new Object[] { newItem } );
            assertNotNull("The item was not created", ret);
            System.out.println("Done!");
            
           
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

    public void testGetAllItems() {
        try {
        	String endpoint = "http://localhost/jboss-net/services/billing";
        
            
            Service  service = new Service();
            Call  call = (Call) service.createCall();
            call.setTargetEndpointAddress( new java.net.URL(endpoint) );
            call.setOperationName("getAllItems");
            call.setUsername("admin");
            call.setPassword("asdfasdf");
            

            // ItemDTOEx
            QName qn = new QName("http://www.sapienter.com/billing", "ItemDTOEx");
            BeanSerializerFactory ser1 = new BeanSerializerFactory(
                    ItemDTOEx.class, qn);
            BeanDeserializerFactory ser2 = new BeanDeserializerFactory(
                    ItemDTOEx.class, qn);
            call.registerTypeMapping(ItemDTOEx.class, qn, ser1, ser2);
            
            // ItemPriceDTOEx
            qn = new QName("http://www.sapienter.com/billing", "ItemPriceDTOEx");
            ser1 = new BeanSerializerFactory(
                    ItemPriceDTOEx.class, qn);
            ser2 = new BeanDeserializerFactory(
                    ItemPriceDTOEx.class, qn);
            call.registerTypeMapping(ItemPriceDTOEx.class, qn, ser1, ser2);

            /*
             * Get all items
             */
             
            System.out.println("Getting all items");
            ItemDTOEx[] items = (ItemDTOEx[]) call.invoke( new Object[] { } );
            assertNotNull("The items were not retrieved", items);
            
            for (int i = 0; i < items.length; i++) {
                System.out.println("Description: " + items[i].getDescription());
                System.out.println("Price: " + items[i].getPrice());
            }
            System.out.println("Done!");
            
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

}
