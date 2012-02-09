/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
 */

package com.sapienter.jbilling.server.user.balance;

import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.server.item.CurrencyBL;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.event.NewOrderEvent;
import com.sapienter.jbilling.server.order.event.NewQuantityEvent;
import com.sapienter.jbilling.server.order.event.OrderDeletedEvent;
import com.sapienter.jbilling.server.order.event.OrderAddedOnInvoiceEvent;
import com.sapienter.jbilling.server.payment.event.PaymentDeletedEvent;
import com.sapienter.jbilling.server.payment.event.PaymentSuccessfulEvent;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.system.event.Event;
import com.sapienter.jbilling.server.system.event.EventManager;
import com.sapienter.jbilling.server.system.event.task.IInternalEventsTask;
import com.sapienter.jbilling.server.user.db.CustomerDTO;
import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.user.event.DynamicBalanceChangeEvent;
import com.sapienter.jbilling.server.util.audit.EventLogger;
import java.math.BigDecimal;

import com.sapienter.jbilling.server.util.db.CurrencyDTO;
import org.apache.log4j.Logger;

/**
 *
 * @author emilc
 */
public class DynamicBalanceManagerTask extends PluggableTask implements IInternalEventsTask {
    private static final Logger LOG = Logger.getLogger(DynamicBalanceManagerTask.class);

    @SuppressWarnings("unchecked")
    private static final Class<Event> events[] = new Class[] { 
        PaymentSuccessfulEvent.class,
        OrderDeletedEvent.class,
        NewOrderEvent.class,
        PaymentDeletedEvent.class,
        OrderAddedOnInvoiceEvent.class,
        NewQuantityEvent.class
    };

    public Class<Event>[] getSubscribedEvents() {
        return events;
    }

    public void process(Event event) throws PluggableTaskException {
        updateDynamicBalance(event.getEntityId(), determineUserId(event), determineAmount(event));
    }

    private BigDecimal determineAmount(Event event) {
        if (event instanceof PaymentSuccessfulEvent) {
            PaymentSuccessfulEvent payment = (PaymentSuccessfulEvent) event;
            // get the amount
            BigDecimal amount = payment.getPayment().getAmount();
            // check if amount is ZERO
            if(amount.equals(BigDecimal.ZERO)) {
                LOG.debug("Amount is ZERO "+amount);
                return BigDecimal.ZERO;
            }
            else {
                // get currency in which payment was made
                CurrencyDTO paymentCurrency =  payment.getPayment().getCurrency();
                LOG.debug("Payment was made in currency "+paymentCurrency.getSymbol());
                // get the user currency
                CurrencyDTO userDefaultCurrency = new UserDAS().find(payment.getPayment().getUserId()).getCurrency();
                LOG.debug("User's default currency is "+userDefaultCurrency.getSymbol());
                // determine currency of user
                if(paymentCurrency.getId()!=userDefaultCurrency.getId()) {
                    // currency is different so convert the amount
                    return new CurrencyBL().convert(paymentCurrency.getId(), userDefaultCurrency.getId(), amount, payment.getEntityId());
                }
                else {
                    LOG.debug("Currencies are same");
                    return payment.getPayment().getAmount();
                }
            }

        } else if (event instanceof OrderDeletedEvent) {
            OrderDeletedEvent order = (OrderDeletedEvent) event;
            // get the amount
            BigDecimal amount = order.getOrder().getTotal();
            if(amount.equals(BigDecimal.ZERO)) {
                LOG.debug("Amount is ZERO "+amount);
                return BigDecimal.ZERO;
            }

            CurrencyDTO orderCurrency = order.getOrder().getCurrency();
            CurrencyDTO userDefaultCurrency = new UserDAS().find(order.getOrder().getUserId()).getCurrency();
            if (order.getOrder().getOrderPeriod().getId() ==  com.sapienter.jbilling.server.util.Constants.ORDER_PERIOD_ONCE) {
                if(orderCurrency.getId() != userDefaultCurrency.getId()) {
                    // currency is different so convert the amount
                    return new CurrencyBL().convert(orderCurrency.getId(), userDefaultCurrency.getId(), amount, order.getEntityId());
                }
                else {
                    LOG.debug("Currency is same in order");
                    return order.getOrder().getTotal();
                }
            } else {
                return BigDecimal.ZERO;
            }

        } else if (event instanceof NewOrderEvent) {
            NewOrderEvent order = (NewOrderEvent) event;
            // get the amount
            BigDecimal amount = order.getOrder().getTotal();
            if(amount.equals(BigDecimal.ZERO)) {
                LOG.debug("Amount is ZERO in order "+order.getOrder().getId());
                return BigDecimal.ZERO;
            }
            if (order.getOrder().getOrderPeriod().getId() ==  com.sapienter.jbilling.server.util.Constants.ORDER_PERIOD_ONCE) {
                CurrencyDTO orderCurrency = order.getOrder().getCurrency();
                CurrencyDTO userDefaultCurrency = new UserDAS().find(order.getOrder().getUserId()).getCurrency();
                if(orderCurrency.getId()!=userDefaultCurrency.getId()) {
                    // currency is different so convert the amount
                    return new CurrencyBL().convert(orderCurrency.getId(), userDefaultCurrency.getId(), amount, order.getEntityId()).multiply(new BigDecimal(-1));
                }
                else {
                    LOG.debug("Currency is same in order");
                    return order.getOrder().getTotal().multiply(new BigDecimal(-1));
                }
            } else {
                return BigDecimal.ZERO;
            }

        } else if (event instanceof PaymentDeletedEvent) {
            PaymentDeletedEvent payment = (PaymentDeletedEvent) event;
            // get the amount
            BigDecimal amount = payment.getPayment().getAmount();
            // check if amount is ZERO
            if(amount.equals(BigDecimal.ZERO)) {
                LOG.debug("Amount is ZERO "+amount);
                return BigDecimal.ZERO;
            }

            // get currency in which payment was made
            CurrencyDTO paymentCurrency =  payment.getPayment().getCurrency();
            LOG.debug("Payment was made in currency "+paymentCurrency.getSymbol());
            // get the user currency
            CurrencyDTO userDefaultCurrency = new UserDAS().find(payment.getPayment().getBaseUser().getUserId()).getCurrency();
            LOG.debug("User's default currency is "+userDefaultCurrency.getSymbol());
            // determine currency of user
            if(paymentCurrency.getId()!= userDefaultCurrency.getId()) {
                // currency is different so convert the amount
                return new CurrencyBL().convert(paymentCurrency.getId(), userDefaultCurrency.getId(), amount, payment.getEntityId()).multiply(new BigDecimal(-1));
            }
            else {
                LOG.debug("Currencies are same");
                return payment.getPayment().getAmount().multiply(new BigDecimal(-1));
            }

        } else if (event instanceof OrderAddedOnInvoiceEvent) {

            LOG.debug("Instance of OrderAddedOnInvoiceEvent");
            OrderAddedOnInvoiceEvent orderOnInvoiceEvent = (OrderAddedOnInvoiceEvent) event;
            OrderAddedOnInvoiceEvent order = (OrderAddedOnInvoiceEvent) event;
            // get the amount
            BigDecimal amount = order.getOrder().getTotal();
            if(amount.equals(BigDecimal.ZERO)) {
                LOG.debug("Amount is ZERO in order "+order.getOrder().getId());
                return BigDecimal.ZERO;
            }
            if (order.getOrder().getOrderPeriod().getId() !=  com.sapienter.jbilling.server.util.Constants.ORDER_PERIOD_ONCE) {
                CurrencyDTO orderCurrency = order.getOrder().getCurrency();
                CurrencyDTO userDefaultCurrency = new UserDAS().find(order.getOrder().getUserId()).getCurrency();
                if(orderCurrency.getId()!=userDefaultCurrency.getId()) {
                    // currency is different so convert the amount
                    return new CurrencyBL().convert(orderCurrency.getId(), userDefaultCurrency.getId(), amount, order.getEntityId()).multiply(new BigDecimal(-1));
                }
                else {
                    LOG.debug("Currency is same in order");
                    return order.getOrder().getTotal().multiply(new BigDecimal(-1));
                }
            } else {
                return BigDecimal.ZERO;
            }

        } else if (event instanceof NewQuantityEvent) {
            NewQuantityEvent nq = (NewQuantityEvent) event;
            LOG.debug("Instance of NewQuantityEvent Event");
            if (new OrderDAS().find(nq.getOrderId()).getOrderPeriod().getId() ==
                    com.sapienter.jbilling.server.util.Constants.ORDER_PERIOD_ONCE) {
                BigDecimal newTotal, oldTotal;
                // new order line, or old one updated?
                if (nq.getNewOrderLine() == null) {
                    // new
                    oldTotal = BigDecimal.ZERO;
                    newTotal = nq.getOrderLine().getAmount();
                    if (nq.getNewQuantity().compareTo(BigDecimal.ZERO) == 0) {
                        // it is a delete
                        newTotal = newTotal.multiply(new BigDecimal(-1));
                    }
                } else {
                    // old
                    oldTotal = nq.getOrderLine().getAmount();
                    newTotal = nq.getNewOrderLine().getAmount();
                }
                LOG.debug("Old order line's item is "+nq.getOrderLine().getItem());
                // convert before returning
                int oldOrderLineCurrencyId = nq.getOrderLine().getItem().getDefaultPrice().getCurrency().getId();
                int newOrderLineCurrencyId = nq.getNewOrderLine().getItem().getDefaultPrice().getCurrency().getId();
                LOG.debug("OLD order line currency is "+nq.getOrderLine().getItem().getDefaultPrice().getCurrency().getDescription());
                LOG.debug("NEW order line currency is "+nq.getNewOrderLine().getItem().getDefaultPrice().getCurrency().getDescription());

                // finding the current user's default currency id from the order
                int currentUserCurrencyId = new OrderDAS().find(nq.getOrderId()).getCurrencyId();
                // convert oldOrderLineCurrency and newOrderLineCurrency to userDefaultCurrency and then return
                oldTotal = new CurrencyBL().convert(oldOrderLineCurrencyId, currentUserCurrencyId, oldTotal, nq.getEntityId());
                newTotal = new CurrencyBL().convert(newOrderLineCurrencyId, currentUserCurrencyId, newTotal, nq.getEntityId());
                // now return after converting operations
                return newTotal.subtract(oldTotal).multiply(new BigDecimal(-1));

            } else {
                return BigDecimal.ZERO;
            }
        }  else {
            LOG.error("Can not determine amount for event " + event);
            return null;
        }
    }

    private int determineUserId(Event event) {
        if (event instanceof PaymentSuccessfulEvent) {
            PaymentSuccessfulEvent payment = (PaymentSuccessfulEvent) event;
            return payment.getPayment().getUserId();
        } else if (event instanceof OrderDeletedEvent) {
            OrderDeletedEvent order = (OrderDeletedEvent) event;
            return order.getOrder().getBaseUserByUserId().getId();
        } else if (event instanceof NewOrderEvent) {
            NewOrderEvent order = (NewOrderEvent) event;
            return order.getOrder().getBaseUserByUserId().getId();
        } else if (event instanceof PaymentDeletedEvent) {
            PaymentDeletedEvent payment = (PaymentDeletedEvent) event;
            return payment.getPayment().getBaseUser().getId();
        } else if (event instanceof OrderAddedOnInvoiceEvent) {
            OrderAddedOnInvoiceEvent order = (OrderAddedOnInvoiceEvent) event;
            return order.getOrder().getBaseUserByUserId().getId();
        } else if (event instanceof NewQuantityEvent) {
            NewQuantityEvent nq = (NewQuantityEvent) event;
            return new OrderDAS().find(nq.getOrderId()).getBaseUserByUserId().getId();
        }  else {
            LOG.error("Can not determine user for event " + event);
            return 0;
        }
    }

    private void updateDynamicBalance(Integer entityId, Integer userId, BigDecimal amount) {
        UserDTO user = new UserDAS().find(userId);
        CustomerDTO customer = user.getCustomer();

        // get the parent customer that pays, if it exists
        if (customer != null) {
            while (customer.getParent() != null
                    && (customer.getInvoiceChild() == null || customer.getInvoiceChild() == 0)) {                
                customer =  customer.getParent(); // go up one level
            }
        }

        // fail fast condition, no dynamic balance or ammount is zero
        if (customer == null
                || customer.getBalanceType() == Constants.BALANCE_NO_DYNAMIC
                || amount.compareTo(BigDecimal.ZERO) == 0) {
            LOG.debug("Nothing to update");
            return;
        }

        LOG.debug("Updating dynamic balance for " + amount);

        BigDecimal balance = (customer.getDynamicBalance() == null ? BigDecimal.ZERO : customer.getDynamicBalance());

        // register the event, before the balance is changed
        new EventLogger().auditBySystem(entityId,
                                        customer.getBaseUser().getId(),
                                        com.sapienter.jbilling.server.util.Constants.TABLE_CUSTOMER,
                                        user.getCustomer().getId(),
                                        EventLogger.MODULE_USER_MAINTENANCE,
                                        EventLogger.DYNAMIC_BALANCE_CHANGE,
                                        null,
                                        balance.toString(),
                                        null);

        if (customer.getBalanceType() == Constants.BALANCE_CREDIT_LIMIT) {
            customer.setDynamicBalance(balance.subtract(amount));

        } else if (customer.getBalanceType() == Constants.BALANCE_PRE_PAID) {
            customer.setDynamicBalance(balance.add(amount));

        } else {
            customer.setDynamicBalance(balance);
        }

        if (!balance.equals(customer.getDynamicBalance())) {
            DynamicBalanceChangeEvent event = new DynamicBalanceChangeEvent(user.getEntity().getId(),
                                                                            user.getUserId(),
                                                                            customer.getDynamicBalance(), // new
                                                                            balance);                     // old
            EventManager.process(event);
        }
    }
}
