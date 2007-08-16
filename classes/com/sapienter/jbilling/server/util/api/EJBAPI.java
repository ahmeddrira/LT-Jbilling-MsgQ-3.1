package com.sapienter.jbilling.server.util.api;

import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.security.auth.login.LoginContext;

import org.jboss.security.auth.callback.UsernamePasswordHandler;

import com.sapienter.jbilling.interfaces.WebServicesSession;
import com.sapienter.jbilling.interfaces.WebServicesSessionHome;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.CreateResponseWS;
import com.sapienter.jbilling.server.user.UserTransitionResponseWS;
import com.sapienter.jbilling.server.user.UserWS;

public class EJBAPI implements JbillingAPI {

    private WebServicesSessionHome home = null;

    private WebServicesSession session = null;

    protected EJBAPI(String providerUrl, String username, String password) throws JbillingAPIException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        
        try {
            UsernamePasswordHandler handler = new UsernamePasswordHandler(
                    username, password.toCharArray());
            LoginContext lc = new LoginContext("jbilling API", handler);
            lc.login();
    
            env.put(Context.URL_PKG_PREFIXES,
                            "org.jboss.naming:org.jnp.interfaces");
            env.put(Context.PROVIDER_URL, providerUrl);
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    "org.jnp.interfaces.NamingContextFactory");
            InitialContext ctx = new InitialContext(env);
            home = (WebServicesSessionHome) PortableRemoteObject
                    .narrow(ctx.lookup("com/sapienter/jbilling/server/util/WebServicesSession"),
                            WebServicesSessionHome.class);
            session = home.create();
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }

    }

    public Integer applyPayment(PaymentWS payment, Integer invoiceId)
            throws JbillingAPIException {
        try {
            return session.applyPayment(payment, invoiceId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer authenticate(String username, String password)
            throws JbillingAPIException {
        try {
            return session.authenticate(username, password);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public CreateResponseWS create(UserWS user, OrderWS order)
            throws JbillingAPIException {
        try {
            return session.create(user, order);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer createItem(ItemDTOEx dto) throws JbillingAPIException {
        try {
            return session.createItem(dto);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer createOrder(OrderWS order) throws JbillingAPIException {
        try {
            return session.createOrder(order);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer createOrderAndInvoice(OrderWS order) throws JbillingAPIException {
        try {
            return session.createOrderAndInvoice(order);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public PaymentAuthorizationDTOEx createOrderPreAuthorize(OrderWS order)
            throws JbillingAPIException {
        try {
            return session.createOrderPreAuthorize(order);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer createUser(UserWS newUser) throws JbillingAPIException {
        try {
            return session.createUser(newUser);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public void deleteOrder(Integer id) throws JbillingAPIException {
        try {
            session.deleteOrder(id);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public void deleteUser(Integer userId) throws JbillingAPIException {
        try {
            session.deleteUser(userId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public void deleteInvoice(Integer invoiceId) throws JbillingAPIException {
        try {
            session.deleteInvoice(invoiceId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public ItemDTOEx[] getAllItems() throws JbillingAPIException {
        try {
            return session.getAllItems();
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public InvoiceWS getInvoiceWS(Integer invoiceId)
            throws JbillingAPIException {
        try {
            return session.getInvoiceWS(invoiceId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer[] getInvoicesByDate(String since, String until)
            throws JbillingAPIException {
        try {
            return session.getInvoicesByDate(since, until);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer[] getLastInvoices(Integer userId, Integer number)
            throws JbillingAPIException {
        try {
            return session.getLastInvoices(userId, number);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer[] getLastOrders(Integer userId, Integer number)
            throws JbillingAPIException {
        try {
            return session.getLastOrders(userId, number);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer[] getLastPayments(Integer userId, Integer number)
            throws JbillingAPIException {
        try {
            return session.getLastPayments(userId, number);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public InvoiceWS getLatestInvoice(Integer userId)
            throws JbillingAPIException {
        try {
            return session.getLatestInvoice(userId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public OrderWS getLatestOrder(Integer userId) throws JbillingAPIException {
        try {
            return session.getLatestOrder(userId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public PaymentWS getLatestPayment(Integer userId)
            throws JbillingAPIException {
        try {
            return session.getLatestPayment(userId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public OrderWS getOrder(Integer orderId) throws JbillingAPIException {
        try {
            return session.getOrder(orderId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer[] getOrderByPeriod(Integer userId, Integer periodId)
            throws JbillingAPIException {
        try {
            return session.getOrderByPeriod(userId, periodId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public OrderLineWS getOrderLine(Integer orderLineId)
            throws JbillingAPIException {
        try {
            return session.getOrderLine(orderLineId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public PaymentWS getPayment(Integer paymentId) throws JbillingAPIException {
        try {
            return session.getPayment(paymentId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public ContactWS[] getUserContactsWS(Integer userId)
            throws JbillingAPIException {
        try {
            return session.getUserContactsWS(userId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer getUserId(String username) throws JbillingAPIException {
        try {
            return session.getUserId(username);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public UserTransitionResponseWS[] getUserTransitions(Date from, Date to)
            throws JbillingAPIException {
        try {
            return session.getUserTransitions(from, to);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public UserWS getUserWS(Integer userId) throws JbillingAPIException {
        try {
            return session.getUserWS(userId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer[] getUsersByCustomField(Integer typeId, String value)
            throws JbillingAPIException {
        try {
            return session.getUsersByCustomField(typeId, value);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer[] getUsersInStatus(Integer statusId)
            throws JbillingAPIException {
        try {
            return session.getUsersInStatus(statusId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public Integer[] getUsersNotInStatus(Integer statusId)
            throws JbillingAPIException {
        try {
            return session.getUsersNotInStatus(statusId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public PaymentAuthorizationDTOEx payInvoice(Integer invoiceId)
            throws JbillingAPIException {
        try {
            return session.payInvoice(invoiceId);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public void updateCreditCard(Integer userId, CreditCardDTO creditCard)
            throws JbillingAPIException {
        try {
            session.updateCreditCard(userId, creditCard);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public void updateOrder(OrderWS order) throws JbillingAPIException {
        try {
            session.updateOrder(order);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public void updateOrderLine(OrderLineWS line) throws JbillingAPIException {
        try {
            session.updateOrderLine(line);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public void updateUser(UserWS user) throws JbillingAPIException {
        try {
            session.updateUser(user);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

    public void updateUserContact(Integer userId, Integer typeId,
            ContactWS contact) throws JbillingAPIException {
        try {
            session.updateUserContact(userId, typeId, contact);
        } catch (Exception e) {
            throw new JbillingAPIException(e);
        }
    }

}
