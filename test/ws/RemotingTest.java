import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.CreateResponseWS;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.server.util.RemoteContext;
import junit.framework.TestCase;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;


public class RemotingTest extends TestCase {

    private final static Integer USER_ID = 2;
    private final static Integer AUTH_OK = 0;

    private IWebServicesSessionBean service = null;

    public void testHessian() {

        service = (IWebServicesSessionBean) RemoteContext.getBean(RemoteContext.Name.API_CLIENT);

        System.out.println("Hessian tests");
        makeCalls();
        System.out.println("Hessian tests done");
    }

    public void testWebServices() {

        service = (IWebServicesSessionBean) RemoteContext.getBean(RemoteContext.Name.API_CLIENT_2);

        System.out.println("Web Services tests");
        makeCalls();
        System.out.println("Web Services tests done");
    }

    public void testInvoker() {

        service = (IWebServicesSessionBean) RemoteContext.getBean(RemoteContext.Name.API_CLIENT_3);

        System.out.println("HTTP Invoker tests");
        makeCalls();
        System.out.println("HTTP Invoker tests done");
    }


    /*
    * TODO: add here calls and asserts to cover every method of IWebServicesSessionBean
    */
    private void makeCalls() {
        try {
            // the goal is to test that the call can be done, not to test business logic
            // example:

            // test InvoiceWS
            InvoiceWS invoice = service.getInvoiceWS(15);
            assertNotNull(invoice);
            assertEquals(15, invoice.getId().intValue());

            invoice = service.getLatestInvoice(USER_ID);
            assertNotNull(invoice);
            assertEquals(invoice.getUserId(), USER_ID);

            Integer[] invoicesIds = service.getLastInvoices(USER_ID, 5);
            assertNotNull(invoicesIds);
            assertFalse(invoicesIds.length == 0);

            invoicesIds = service.getInvoicesByDate("2006-01-01", "2007-01-01");
            assertNotNull(invoicesIds);
            assertFalse(invoicesIds.length == 0);

            invoicesIds = service.getUserInvoicesByDate(2, "2006-07-26", "2006-07-29");
            assertNotNull(invoicesIds);
            assertFalse(invoicesIds.length == 0);

//            byte[] pdf = service.getPaperInvoicePDF(15);
//            assertTrue(pdf.length > 0);

            invoicesIds = service.createInvoice(USER_ID, false);
            assertTrue(invoicesIds.length > 0);

            service.createInvoice(USER_ID, false);

            service.deleteInvoice(invoicesIds[0]);
            try {
                invoice = service.getInvoiceWS(invoicesIds[0]);
                fail("invoice should be deleted");
            } catch (Exception ex) {
                //invoice not found
            }

            // orders WS
            OrderWS orderWS = createOrderWs();
//
//            // create orders
//            Integer orderId = service.createOrder(orderWS);
//            assertNotNull(orderId);
//            Integer invoiceId = service.createInvoiceFromOrder(orderId, null);
//            assertNotNull(invoiceId);
//            service.deleteOrder(orderId);
//
//            PaymentAuthorizationDTOEx auth = service.createOrderPreAuthorize(orderWS);
//            assertNotNull(auth);
//            assertEquals(Boolean.TRUE, auth.getResult());
//
//            orderWS = createOrderWs();
//            orderWS.setPricingFields(PricingField.setPricingFieldsValue(new PricingField[]{new PricingField("subtract", new BigDecimal("1.0"))}));
//            OrderWS newOrder = service.rateOrder(orderWS);
//            assertNotNull(newOrder);
//
//            orderWS = createOrderWs();
//            orderWS.setPricingFields(PricingField.setPricingFieldsValue(new PricingField[]{new PricingField("subtract", new BigDecimal("1.0"))}));
//            OrderWS[] orders = service.rateOrders(new OrderWS[]{orderWS});
//            assertNotNull(orders);
//            assertTrue(orders.length > 0);
//            newOrder = orders[0];
//            newOrder.setNotes("new notes");
//            service.updateOrder(newOrder);
//            newOrder = service.getOrder(orders[0].getId());
//            assertNotNull(newOrder);
//            assertEquals("new notes", newOrder.getNotes());
//
//            invoiceId = service.createOrderAndInvoice(orderWS);
//            assertNotNull(invoiceId);
//
            Integer[] orderIds = service.getOrderByPeriod(USER_ID, 1);
            assertNotNull(orderIds);
            assertTrue(orderIds.length > 0);
//
//            Integer orderLineId = newOrder.getOrderLines()[0].getId();
//            OrderLineWS orderLine = service.getOrderLine(orderLineId);
//            assertNotNull(orderLine);
//            orderLine.setDescription("new description");
//            service.updateOrderLine(orderLine);
//            orderLine = service.getOrderLine(orderLineId);
//            assertNotNull(orderLine);
//            assertEquals("new description", orderLine.getDescription());
//
            orderWS = service.getLatestOrder(USER_ID);
            assertNotNull(orderWS);
//
            orderIds = service.getLastOrders(USER_ID, 1);
            assertNotNull(orderIds);
            assertTrue(orderIds.length > 0);

//            orderWS = service.getCurrentOrder(USER_ID, new Date());
//            assertNotNull(orderWS);
//
//            OrderWS orderAfterWS = service.updateCurrentOrder(USER_ID, new OrderLineWS[]{createOrderLineWS()}, null, new Date(), "Event from WS");
//            assertNotNull(orderAfterWS);
//            assertEquals(orderWS.getId(), orderAfterWS.getId());
//
            // users WS
            UserWS userWS = createUserWS();
            Integer newUserId = service.createUser(userWS);
            assertNotNull(newUserId);

            UserWS newUser = service.getUserWS(newUserId);
            assertNotNull(newUser);

//            newUser.setLanguageId(2);
//            service.updateUser(newUser);
//            newUser = service.getUserWS(newUserId);
//            assertNotNull(newUser);
//            assertEquals(Integer.valueOf(2), newUser.getLanguageId());

            ContactWS[] userContacts = service.getUserContactsWS(newUserId);
            assertNotNull(userContacts);
            assertTrue(userContacts.length > 0);

            ContactWS contactWS = newUser.getContact();
            contactWS.setCity("testCity");
            service.updateUserContact(newUserId, 2, contactWS);
            newUser = service.getUserWS(newUserId);
            assertNotNull(newUser);
            assertNotNull(newUser.getContact());
            assertEquals("testCity", newUser.getContact().getCity());

            Integer userId = service.getUserId(newUser.getUserName());
            assertEquals(newUserId, userId);

            Integer[] userIds = service.getUsersInStatus(1);
            assertNotNull(userIds);
            assertTrue(userIds.length > 0);

            userIds = service.getUsersByStatus(1, true);
            assertNotNull(userIds);
            assertTrue(userIds.length > 0);

            userIds = service.getUsersByCustomField(1, "serial-from-ws");
            assertNotNull(userIds);
            assertTrue(userIds.length > 0);

            Integer authResult = service.authenticate(newUser.getUserName(), "asdfasdf1");
            assertNotNull(authResult);
            assertEquals(AUTH_OK, authResult);

//            CreditCardDTO cc = new CreditCardDTO();
//            cc.setName("Frodo Baggins2");
//            cc.setNumber("4111111111111153");
//            service.updateCreditCard(newUserId, cc);
//
//            newUser = service.getUserWS(newUserId);
//            assertNotNull(newUser);
//            assertTrue(newUser.getCreditCard().getNumber().endsWith("1153"));

//            userIds = service.getUsersByCreditCard(newUser.getCreditCard().getNumber());
//            assertNotNull(userIds);
//            assertTrue(userIds.length > 0);

//            userWS = createUserWS();
//            orderWS = createOrderWs();
//            CreateResponseWS response = service.create(userWS, orderWS);
//            assertNotNull(response);
//            assertNotNull(response.getUserId());
//            assertNotNull(response.getOrderId());

//            service.deleteUser(newUserId);
//            newUser = service.getUserWS(newUserId);
//            assertNull(newUser);

//    public PaymentAuthorizationDTOEx payInvoice(Integer invoiceId)
//    public Integer applyPayment(PaymentWS payment, Integer invoiceId)
//    public PaymentWS getPayment(Integer paymentId)
//    public PaymentWS getLatestPayment(Integer userId)
//    public Integer[] getLastPayments(Integer userId, Integer number)
//    public PaymentAuthorizationDTOEx processPayment(PaymentWS  payment);
//    public ValidatePurchaseWS validatePurchase(Integer userId, Integer itemId, String fields);
//    public ValidatePurchaseWS validateMultiPurchase(Integer userId,  Integer[] itemId, String[] fields);

//    public void updateItem(ItemDTOEx item);
//    public Integer createItem(ItemDTOEx  item)
//    public ItemDTOEx[] getAllItems()
//    public UserTransitionResponseWS[] getUserTransitions(Date from, Date to)
//    public UserTransitionResponseWS[] getUserTransitionsAfterId(Integer id)
//    public ItemDTOEx getItem(Integer itemId, Integer userId, String pricing);
//    public InvoiceWS getLatestInvoiceByItemType(Integer userId,  Integer itemTypeId)
//    public Integer[] getLastInvoicesByItemType(Integer userId, Integer itemTypeId, Integer number)
//    public OrderWS getLatestOrderByItemType(Integer userId, Integer itemTypeId)
//    public Integer[] getLastOrdersByItemType(Integer userId, Integer itemTypeId, Integer number)
//    public BigDecimal isUserSubscribedTo(Integer userId, Integer itemId);
//    public Integer[] getUserItemsByCategory(Integer userId, Integer categoryId);
//    public ItemDTOEx[] getItemByCategory(Integer itemTypeId);
//    public ItemTypeWS[] getAllItemCategories();
//    public Integer createItemCategory(ItemTypeWS itemType)
//    public void updateItemCategory(ItemTypeWS itemType)
//    public void updateAch(Integer userId, AchDTO ach)
//    public void setAuthPaymentType(Integer userId, Integer autoPaymentType, boolean use)
//    public Integer getAuthPaymentType(Integer userId)
//    public void generateRules(String rulesData)


            // test getLatestInvoice
            //...

            // the rest of the tests to include every method of IWebServicesSessionBean

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception" + e.getMessage());
        }
    }

    private OrderWS createOrderWs() {
        OrderWS order = new OrderWS();
        order.setUserId(2);
        order.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        order.setPeriod(1); // once
        order.setCurrencyId(1);


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

    private UserWS createUserWS() {
        UserWS newUser = new UserWS();
        newUser.setUserName("testUserName-" + Calendar.getInstance().getTimeInMillis());
        newUser.setPassword("asdfasdf1");
        newUser.setLanguageId(1);
        newUser.setMainRoleId(5);
        newUser.setParentId(null);
        newUser.setStatusId(UserDTOEx.STATUS_ACTIVE);
        newUser.setCurrencyId(null);
        newUser.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        // add a contact
        newUser.setContact(createContactWS());

        // add a credit card
        CreditCardDTO cc = new CreditCardDTO();
        cc.setName("Frodo Baggins");
        cc.setNumber("4111111111111152");

        // valid credit card must have a future expiry date to be valid for payment processing
        Calendar expiry = Calendar.getInstance();
        expiry.set(Calendar.YEAR, expiry.get(Calendar.YEAR) + 1);
        cc.setExpiry(expiry.getTime());

        newUser.setCreditCard(cc);

        return newUser;
    }

    private ContactWS createContactWS() {
        ContactWS contact = new ContactWS();
        contact.setEmail("frodo@shire.com");
        contact.setFirstName("Frodo");
        contact.setLastName("Baggins");
        String fields[] = new String[2];
        fields[0] = "1";
        fields[1] = "2";
        String fieldValues[] = new String[2];
        fieldValues[0] = "serial-from-ws";
        fieldValues[1] = "FAKE_2";
        contact.setFieldNames(fields);
        contact.setFieldValues(fieldValues);
        contact.setType(2);
        return contact;
    }

}
