/**
 * 
 */
package com.sapienter.jbilling.server.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Hashtable;

import junit.framework.TestCase;

import com.sapienter.jbilling.server.entity.InvoiceLineDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskWS;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

/**
 * Test class to unit test the functionality of Tax applied to invoices that
 * belong to user from a configured country.
 * 
 * @author Vikas Bodani
 * @since 29-Jul-2011
 * 
 */
public class CountryTaxPluginTest extends TestCase {

    private JbillingAPI api;
    private static final String param1= "charge_carrying_item_id";
    private static final String param2= "tax_country_code";
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        api = JbillingAPIFactory.getAPI();
    }

    public void testAUSContact() {
        System.out.println("Running testAUSContact() now....\n\n\n");
        Integer taxItemID= null;
        Integer pluginInst= null;
        Integer invId=null;
        Integer user1= null;
        try {
            System.out.println("Adding a new user with contact and country set to AU.");
            user1= api.createUser(createUserWS("testCntryTax" + System.currentTimeMillis(), "AU"));
            assertNotNull("Test fail at user creation.", user1);
            
            System.out.println("User ID: " + user1 + "\nCreating the AU Tax Item.");
            taxItemID= createItem();
            assertNotNull("Tax Item Id should not be null.", taxItemID);
            
            System.out.println("Tax Item ID: " + taxItemID + "\nAdding a Country Tax Plugin for AU.");
            PluggableTaskWS newTask = new PluggableTaskWS();
            newTask.setNotes("Australia GST Task.");
            
            Hashtable<String, String> params= new Hashtable<String, String>();
            params.put(param1, String.valueOf(taxItemID));//rate 10%
            params.put(param2, "AU");
            newTask.setParameters(params);
            newTask.setProcessingOrder(Integer.valueOf(3));
            newTask.setTypeId(Integer.valueOf(90));
            pluginInst= api.createPlugin(newTask);
            assertNotNull("Plugin id is not null.", pluginInst);
                            
            System.out.println("Plugin ID: " + pluginInst + "\nCreating order for user 1 & Generate invoice.");
            Integer orderId= api.createOrder(createOrderWs(user1));
            invId= api.createInvoiceFromOrder(orderId, null);
            assertNotNull("Order ID should have value.", invId);
            
            InvoiceWS invoice= api.getLatestInvoice(user1);
            InvoiceLineDTO[] lines= invoice.getInvoiceLines();
            System.out.println("Invoice ID: " + invId + "\nInspecting invoices lines.." + lines.length);
            assertTrue(lines.length > 1);
            for(InvoiceLineDTO line: lines) {
                System.out.println("Line description: " + line);
                System.out.println("Amount set= " + line.amountHasBeenSet());
                System.out.println("Price set= " + line.priceHasBeenSet());
                if (line.getDescription()!=null && line.getDescription().startsWith("Tax or Penalty")) {
                    System.out.println("Tax line found. Amount is: " + line.getAmount().toPlainString());
                    assertTrue(line.getItemId().intValue() == taxItemID.intValue());
                    System.out.println("The amount should be $1 (10%)");
                    assertTrue("The amount should have been $1.", new BigDecimal("1").compareTo(line.getAmount())==0);
                    break;
                }
            }
            
            System.out.println("Testing AU Flat Rate tax.");
            // switch tax item to flat rate of $7
            ItemDTOEx taxItem = api.getItem(taxItemID, api.getCallerId(), new PricingField[]{});
            assertNotNull(taxItem);
            String perc= null;
            taxItem.setPercentage(perc);
            taxItem.setPrice(new BigDecimal(7));
            api.updateItem(taxItem);
            System.out.println("Tax Item set to Fixed price $7.");
            
            System.out.println("Creating new order and invoice");
            
            orderId= api.createOrder(createOrderWs(user1));
            invId= api.createInvoiceFromOrder(orderId, null);
            
            assertNotNull("Order ID should have value.", invId);
            
            invoice= api.getLatestInvoice(user1);
            lines= invoice.getInvoiceLines();
            System.out.println("Invoice ID: " + invId + "\nInspecting invoices lines.." + lines.length);
            assertTrue(lines.length > 1);
            for(InvoiceLineDTO line: lines) {
                System.out.println("Line description: " + line);
                if (line.getDescription()!=null && line.getDescription().startsWith("Tax or Penalty")) {
                    System.out.println("Tax line found. Amount: " + line.getAmount().toString());
                    assertTrue(line.getItemId().intValue() == taxItemID.intValue());
                    System.out.println("The amount should be $7 flat rate.");
                    assertTrue("The amount should have been $7 flat rate.", new BigDecimal(7).compareTo(line.getAmount())==0);
                    break;
                }
            }
            System.out.println("Successful, testAUSContact");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        } finally {
            try {
                api.deletePlugin(pluginInst);
                api.deleteInvoice(invId);
                OrderWS order=api.getLatestOrder(user1);
                api.deleteOrder(order.getId());
                api.deleteItem(taxItemID);
                api.deleteUser(user1);
            } catch (Exception e){}
        }
    }

    
    public void test2NonAUSContact() {
        System.out.println("Running test2NonAUSContact() now....\n\n\n");
        Integer invId=null;
        Integer user2= null;
        try {
            System.out.println("Adding a new user with contact and country set to non au.");
            user2= api.createUser(createUserWS("testCntryTax" + System.currentTimeMillis(), "IN"));
            assertNotNull("Test fail at user creation.", user2);
            
            System.out.println("User ID: " + user2 + "\nTax Item for IN does not exists.");
            System.out.println("Country Tax Plugin for AU exists, but not for IN.");
            System.out.println("Creating order for user 2 & Generate invoice.");
            
            invId= api.createOrderAndInvoice(createOrderWs(user2));
            assertNotNull("Invoice ID should have value.", invId);
            
            InvoiceWS invoice= api.getInvoiceWS(invId);
            InvoiceLineDTO[] lines= invoice.getInvoiceLines();
            System.out.println("Invoice ID: " + invId + "\nInspecting invoices lines..size=" + lines.length);
            
            assertTrue("There should have been only 1 line.", lines.length == 1 );
            
            for(InvoiceLineDTO line: lines) {
                System.out.println(line.getDescription());
                if ( line.getDescription() != null ) {
                    assertFalse( line.getDescription().startsWith("Tax or Penalty") );
                }
            }
            System.out.println("Successful, testNonAUSContact");
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }finally {
            try{
                api.deleteInvoice(invId);
                OrderWS order=api.getLatestOrder(user2);
                api.deleteOrder(order.getId());
                api.deleteUser(user2);
            } catch (Exception e){}
        }
    }
    
    /**
     * @see #assertEquals(java.math.BigDecimal, java.math.BigDecimal)
     *
     * @param message error message if assert fails
     * @param expected expected BigDecimal value
     * @param actual actual BigDecimal value
     */
    private void assertEquals(String message, BigDecimal expected, BigDecimal actual) {
        assertEquals(message,
                (Object) (expected == null ? null : expected.setScale(2, RoundingMode.HALF_UP)),
                (Object) (actual == null ? null : actual.setScale(2, RoundingMode.HALF_UP)));
    }
    
    private Integer createItem() {
        ItemDTOEx item = new ItemDTOEx();
        item.setCurrencyId(1);
        item.setPercentage(new BigDecimal(10));
        item.setPrice(item.getPercentage());
        item.setDescription("AUS Tax Item");
        item.setEntityId(1);
        item.setNumber("AUTAX");
        //fees
        item.setTypes(new Integer[]{Integer.valueOf(22)});
        // item.set
        return api.createItem(item);
    }
    
    private OrderWS createOrderWs(Integer userId) {
        OrderWS order = new OrderWS();
        order.setUserId(userId);
        order.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        order.setPeriod(1); // once
        order.setCurrencyId(1);
        order.setActiveSince(new Date());
        order.setOrderLines(new OrderLineWS[]{createOrderLineWS()});
        return order;
    }

    private OrderLineWS createOrderLineWS() {
        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setDescription("Order line");
        line.setItemId(1);
        line.setQuantity(1);
        line.setPrice(new BigDecimal("10"));
        line.setAmount(new BigDecimal("10"));
        return line;
    }

    private UserWS createUserWS(String userName, String countryCode) {
        UserWS newUser = new UserWS();
        newUser.setUserId(0);
        newUser.setUserName(userName);
        newUser.setPassword("asdfasdfbc123");
        newUser.setLanguageId(1);
        newUser.setMainRoleId(5);
        newUser.setParentId(null);
        newUser.setStatusId(UserDTOEx.STATUS_ACTIVE);
        newUser.setCurrencyId(null);

        // add a contact
        newUser.setContact(createContactWS(countryCode));

        return newUser;
    }

    private ContactWS createContactWS(String countryCode) {
        ContactWS contact = new ContactWS();
        contact.setEmail("rtest@gmail.com");
        contact.setFirstName("Test");
        contact.setLastName("Plugin");
        contact.setType(2);
        contact.setCountryCode(countryCode);
        return contact;
    }

}
