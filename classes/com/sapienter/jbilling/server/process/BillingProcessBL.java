/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.process;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.*;
import com.sapienter.jbilling.server.entity.BillingProcessDTO;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.invoice.NewInvoiceDTO;
import com.sapienter.jbilling.server.invoice.db.InvoiceDAS;
import com.sapienter.jbilling.server.item.CurrencyBL;
import com.sapienter.jbilling.server.list.ResultList;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.TimePeriod;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderProcessDAS;
import com.sapienter.jbilling.server.order.db.OrderProcessDTO;
import com.sapienter.jbilling.server.order.event.OrderToInvoiceEvent;
import com.sapienter.jbilling.server.payment.PaymentBL;
import com.sapienter.jbilling.server.pluggableTask.*;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.process.db.BillingProcessDAS;
import com.sapienter.jbilling.server.system.event.EventManager;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.db.CustomerDTO;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.MapPeriodToCalendar;
import com.sapienter.jbilling.server.util.PreferenceBL;
import com.sapienter.jbilling.server.util.audit.EventLogger;
import org.apache.log4j.Logger;
import sun.jdbc.rowset.CachedRowSet;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class BillingProcessBL extends ResultList 
        implements ProcessSQL {

    private JNDILookup EJBFactory = null;
    private BillingProcessEntityLocalHome billingProcessHome = null;
    private BillingProcessEntityLocal billingProcess = null;
    private BillingProcessRunEntityLocalHome processRunHome = null;
    private BillingProcessRunEntityLocal processRun = null; 
    private static final Logger LOG = Logger.getLogger(BillingProcessBL.class);
    private EventLogger eLogger = null;
    
    public BillingProcessBL(Integer billingProcessId) 
            throws NamingException, FinderException {
        init();
        set(billingProcessId);
    }
    
    public BillingProcessBL() throws NamingException {
        init();
    }
    
    public BillingProcessBL(BillingProcessEntityLocal row)
            throws NamingException {
        init();
        billingProcess = row;
    }
    
    private void init() throws NamingException {
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        billingProcessHome = (BillingProcessEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                BillingProcessEntityLocalHome.class,
                BillingProcessEntityLocalHome.JNDI_NAME);
    
        // now create the run info row
        processRunHome =
                (BillingProcessRunEntityLocalHome) EJBFactory.lookUpLocalHome(
                BillingProcessRunEntityLocalHome.class,
                BillingProcessRunEntityLocalHome.JNDI_NAME);
    
    }

    public BillingProcessEntityLocal getEntity() {
        return billingProcess;
    }
    
    public BillingProcessEntityLocalHome getHome() {
        return billingProcessHome;
    }
    
    public void set(Integer id) throws FinderException {
        billingProcess = billingProcessHome.findByPrimaryKey(id);
    }
    
    public void set(BillingProcessEntityLocal pEntity) {
        billingProcess = pEntity;
    }
    
    public Integer present(Integer entityId, Integer isReview, Date billingDate) 
            throws SQLException, NamingException {
        Integer retValue = null;
        prepareStatement(ProcessSQL.findTodays);
        cachedResults.setInt(1, entityId.intValue());
        cachedResults.setInt(2, isReview.intValue());
        cachedResults.setDate(3, new java.sql.Date(billingDate.getTime()));
        
        execute();
        conn.close();
        
        if (cachedResults.next()) {
            retValue = new Integer(cachedResults.getInt(1));
        }
        
        return retValue;
    }

    public Integer findOrCreate(BillingProcessDTO dto) 
            throws SQLException, NamingException, FinderException,
                CreateException {
        Integer retValue = present(dto.getEntityId(), dto.getIsReview(),
                dto.getBillingDate());
        if (retValue != null) {
            set(retValue);
        }  else {
            retValue = create(dto);
        }
        
        return retValue; 
    }
    
    public Integer create(BillingProcessDTO dto) 
            throws CreateException, NamingException, FinderException {
        // create the record 
        billingProcess = billingProcessHome.create(dto.getEntityId(),
                dto.getBillingDate(), dto.getPeriodUnitId(), 
                dto.getPeriodValue(), dto.getRetriesToDo());
        billingProcess.setIsReview(dto.getIsReview());
        processRun = processRunHome.create(dto.getBillingDate());
        processRun.setInvoiceGenerated(new Integer(0));

        billingProcess.getRuns().add(processRun);
        
        if (dto.getIsReview().intValue() == 1) {
            ConfigurationBL config = new ConfigurationBL(dto.getEntityId());
            config.getEntity().setReviewStatus(Constants.REVIEW_STATUS_GENERATED);
        }
                
        return billingProcess.getId();       
    }
    
    /**
     * Generates one single invoice for one single purchase order. This is
     * meant to be called outside the billing process.
     * @param orderId
     * @return
     * @throws NamingException
     * @throws FinderException
     * @throws PluggableTaskException
     * @throws CreateException
     * @throws SessionInternalError
     */
    public InvoiceEntityLocal generateInvoice(Integer orderId, 
            Integer invoiceId) 
            throws NamingException, FinderException, PluggableTaskException,
                 CreateException, RemoveException, SessionInternalError,
                 SQLException {
        InvoiceEntityLocal retValue = null;
        // find the order
        OrderBL order = new OrderBL(orderId);
        // define some data
        Integer entityId = order.getEntity().getUser().getEntity().getId();
        ConfigurationBL config = new ConfigurationBL(entityId);
        int maxPeriods = config.getEntity().getMaximumPeriods().intValue();
        boolean paymentApplication = config.getEntity().
                getAutoPaymentApplication().intValue() == 1;
        // The user could be the parent of a sub-account
        Integer userId = findUserId(order.getEntity());
        Date processDate = Calendar.getInstance().getTime();
        processDate = Util.truncateDate(processDate);
        // create the my invoice
        NewInvoiceDTO newInvoice = new NewInvoiceDTO();
        newInvoice.setDate(processDate);
        newInvoice.setIsReview(new Integer(0));
        // find the due date that applies
        TimePeriod period = order.getDueDate();
        newInvoice.setDueDatePeriod(period);
        // this is an isolated invoice that doesn't care about previous
        // overdue invoices
        newInvoice.setCarriedBalance(new Float(0));
        
        try {
            // put the order in the invoice using all the pluggable taks stuff
            addOrderToInvoice(entityId, order.getEntity(), newInvoice,
                    processDate, maxPeriods);
            
            // this means that the user is trying to generate an invoice from
            // an order that the configurated tasks have rejected. Therefore
            // either this is the case an generating this invoice doesn't make
            // sense, or some business rules in the tasks have to be changed
            // (probably with a personalized task for this entity)
            if (newInvoice.getOrders().size() == 0) return null;

            processOrderToInvoiceEvents(newInvoice, entityId);

            // generate the invoice lines
            composeInvoice(entityId, userId, newInvoice);
            // put the resulting invoice in the database
            if (invoiceId == null) {
                // it is a new invoice from a singe order
                retValue = generateDBInvoice(userId, newInvoice, null,
                        Constants.ORDER_PROCESS_ORIGIN_MANUAL);
                // try to get this new invioce paid by previously unlinked 
                // payments
                if (paymentApplication) {
                    PaymentBL pBL = new PaymentBL();
                    pBL.automaticPaymentApplication(retValue);
                }
            } else {
                // it is an order going into an existing invoice
                InvoiceBL invoice = new InvoiceBL(invoiceId);
                boolean isUnpaid = invoice.getEntity().getToProcess().
                        intValue() == 1;
                invoice.update(newInvoice);
                retValue = invoice.getEntity();
                createOrderProcess(newInvoice, retValue, null,
                        Constants.ORDER_PROCESS_ORIGIN_MANUAL);
                eLogger.info(entityId, invoiceId, 
                        EventLogger.MODULE_INVOICE_MAINTENANCE, 
                        EventLogger.INVOICE_ORDER_APPLIED,
                        Constants.TABLE_INVOICE);
                // if the invoice is now not payable, take the user
                // out of ageing
                if (isUnpaid && retValue.getToProcess().intValue() == 0) {
                    AgeingBL ageing = new AgeingBL();
                    ageing.out(retValue.getUser(), null);
                }
            }
        } catch (TaskException e) {
            // this means that the user is trying to generate an invoice from
            // an order that the configurated tasks have rejected. Therefore
            // either this is the case an generating this invoice doesn't make
            // sense, or some business rules in the tasks have to be changed
            // (probably with a personalized task for this entity)
            LOG.warn("Exception in generate invoice ", e);
        }
        
        return retValue;
    }
        
    public InvoiceEntityLocal[] generateInvoice(
            BillingProcessEntityLocal process, UserDTO user, 
            boolean isReview, boolean onlyRecurring)
            throws NamingException, SessionInternalError {

        Integer userId = user.getUserId();
        Integer entityId = user.getEntity().getId();
        
        // get the configuration
        boolean useProcessDateForInvoice = true;
        int maximumPeriods = 1;
        boolean paymentApplication = false;
        try {
            ConfigurationBL config = new ConfigurationBL(
                    process.getEntityId());
            useProcessDateForInvoice = config.getEntity().
                    getInvoiceDateProcess().intValue() == 1;
            maximumPeriods = config.getEntity().getMaximumPeriods().intValue();
            paymentApplication = config.getEntity().getAutoPaymentApplication().
                    intValue() == 1;
        } catch (Exception e) {}
        
        
        
        // this contains the generated invoices, one per due date
        // found in the applicable purchase orders.
        // The key is the object TimePeriod
        Hashtable<TimePeriod,NewInvoiceDTO> newInvoices = new Hashtable<TimePeriod,NewInvoiceDTO>();
        InvoiceEntityLocal[] retValue = null;
        
        LOG.debug("In generateInvoice for user " + userId +
                " process date:" + process.getBillingDate());
        /*
         * Go through the orders first
         */

        boolean includedOrders = false;
        
        // initialize the subaccounts iterator if this user is a parent
        Iterator subAccountsIt = null;
        if (user.getCustomer().getIsParent() != null &&
                user.getCustomer().getIsParent().intValue() == 1) {
            try {
                UserBL parent = new UserBL(userId); 
                subAccountsIt = parent.getEntity().getCustomer().getChildren().
                        iterator();
            } catch (Exception e) {
                throw new SessionInternalError(e);
            } 
        }
        
        do {
            try {
                // get the orders that might be processable for this user
            	OrderDAS orderDas = new OrderDAS();
                Collection<OrderDTO> orders = orderDas.findByUser_Status(userId,
                        Constants.ORDER_STATUS_ACTIVE);

                // go through each of them, and update the DTO if it applies
                for (Iterator ordersIt = orders.iterator(); ordersIt.hasNext();) {
                    OrderDTO order = (OrderDTO) ordersIt.next();
                    LOG.debug("Processing order :" + order.getId());
                    // apply any order processing filter pluggable task
                    try {
                        PluggableTaskManager taskManager = new PluggableTaskManager(
                                entityId,
                                Constants.PLUGGABLE_TASK_ORDER_FILTER);
                        OrderFilterTask task = (OrderFilterTask) taskManager
                                .getNextClass();
                        boolean isProcessable = true;
                        while (task != null) {
                            isProcessable = task.isApplicable(order, process);
                            if (!isProcessable) {
                                break; // no need to keep doing more tests
                            }
                            task = (OrderFilterTask) taskManager.getNextClass();
                        }

                        // include this order only if it complies with all the
                        // rules
                        if (isProcessable) {

                            LOG.debug("Order processable");

                            if (onlyRecurring) {
                                if (order.getOrderPeriod().getId() != Constants.ORDER_PERIOD_ONCE) {
                                    includedOrders = true;
                                    LOG.debug("It is not one-timer. " +
                                            "Generating invoice");
                                }
                            } else {
                                includedOrders = true;
                            }
                            /*
                             * now find if there is already an invoice being
                             * generated for the given due date period
                             */
                            // find the due date that applies to this order
                            OrderBL orderBl = new OrderBL();
                            orderBl.set(order);
                            TimePeriod dueDatePeriod = orderBl.getDueDate();
                            // look it up in the hashtable
                            NewInvoiceDTO thisInvoice = (NewInvoiceDTO) newInvoices
                                    .get(dueDatePeriod);
                            if (thisInvoice == null) {
                                LOG.debug("Adding new invoice for period "
                                        + dueDatePeriod + " process date:"
                                        + process.getBillingDate());
                                // we need a new one with this period
                                // define the invoice date
                                thisInvoice = new NewInvoiceDTO();
                                if (useProcessDateForInvoice) {
                                    thisInvoice.setDate(process.getBillingDate());
                                } else {
                                    thisInvoice.setDate(orderBl.getInvoicingDate(),
                                            order.getOrderPeriod().getId() != Constants.ORDER_PERIOD_ONCE);
                                }
                                thisInvoice.setIsReview(isReview ? new Integer(
                                        1) : new Integer(0));
                                thisInvoice.setCarriedBalance(new Float(0));
                                thisInvoice.setDueDatePeriod(dueDatePeriod);
                            } else {
                                LOG.debug("invoice found for period "
                                        + dueDatePeriod);
                                if (!useProcessDateForInvoice) {
                                    thisInvoice.setDate(orderBl.getInvoicingDate(),
                                            order.getOrderPeriod().getId() != Constants.ORDER_PERIOD_ONCE);
                                }
                            }
                            /*
                             * The order periods plug-in might not add any period. This should not happen
                             * but if it does, the invoice should not be included
                             */
                            if (addOrderToInvoice(entityId, order, thisInvoice,
                                    process.getBillingDate(), maximumPeriods)) {
	                            // add or replace
	                            newInvoices.put(dueDatePeriod, thisInvoice);
                            }
                            LOG.debug("After putting period there are "
                                    + newInvoices.size() + " periods.");

                            // here it would be easy to update this order
                            // to_process and
                            // next_billable_time. I can't do that because these
                            // fields
                            // will be read by the following tasks, and they
                            // will asume
                            // they are not modified.
                        }

                    } catch (PluggableTaskException e) {
                        LOG.fatal("Problems handling order filter task.", e);
                        throw new SessionInternalError(
                                "Problems handling order filter task.");
                    } catch (TaskException e) {
                        LOG.fatal("Problems excecuting order filter task.", e);
                        throw new SessionInternalError(
                                "Problems executing order filter task.");
                    } catch (CreateException e) {
                        LOG.fatal("Couldn't LOG an event", e);
                        throw new SessionInternalError(
                                "Problems login and event.");
                    }
                } // for - all the orders for this user
            } catch (FinderException e) {
                // this means that this user doesn have orders to process
                LOG.info("User " + userId
                        + " without order to process. ");
            }
            
            // see if there is any subaccounts to include in this invoice
            if (subAccountsIt!= null) {
                CustomerDTO customer = null;
                while(subAccountsIt.hasNext()) {
                    customer = (CustomerDTO) 
                            subAccountsIt.next();
                    if (customer.getInvoiceChild() == null || 
                            customer.getInvoiceChild().intValue() == 0) {
                        break;
                    } else {
                        LOG.debug("Subaccount not included in parent's invoice " + 
                                customer.getId());
                        customer = null;
                    }
                }
                if (customer != null) {
                    userId = customer.getBaseUser().getUserId();
                    LOG.debug("Now processing subaccount " + userId);
                } else {
                    subAccountsIt = null;
                    LOG.debug("No more subaccounts to process");
                }
            }
        
        } while(subAccountsIt != null); // until there are no more subaccounts

        if (!includedOrders || newInvoices.size() == 0) {
            // check if invoices without orders are allowed
            PreferenceBL preferenceBL = new PreferenceBL();
            try {
                preferenceBL.set(entityId, 
                        Constants.PREFERENCE_ALLOW_INVOICES_WITHOUT_ORDERS);
            } catch (FinderException fe) {
                // use default
            }
            if (preferenceBL.getInt() == 0) {
                LOG.debug("No applicable orders. No invoice generated " +
                        "(skipping invoices).");
                return null;
            }
        }

        if (!isReview) {
            for (Map.Entry<TimePeriod, NewInvoiceDTO> newInvoiceEntry : newInvoices.entrySet()) {
                processOrderToInvoiceEvents(newInvoiceEntry.getValue(), entityId);
            }
        }

        /*
         * Include those invoices that should've been paid
         * (or have negative balance, as credits)
         */
        LOG.debug("Considering overdue invoices");
        // find the invoice home interface
        InvoiceEntityLocalHome invoiceHome =
                (InvoiceEntityLocalHome) EJBFactory.lookUpLocalHome(
                InvoiceEntityLocalHome.class,
                InvoiceEntityLocalHome.JNDI_NAME);
        // any of the new invoices being created could hold the overdue invoices
        NewInvoiceDTO holder = newInvoices.isEmpty() ? null : 
                (NewInvoiceDTO) newInvoices.elements().nextElement();

        try {
            Collection dueInvoices =
                invoiceHome.findWithBalanceByUser(user.getUserId());
            LOG.debug("Processing invoices for user " + user.getUserId());
            // go through each of them, and update the DTO if it applies
            for (Iterator it = dueInvoices.iterator(); it.hasNext();) {
                InvoiceEntityLocal invoice = (InvoiceEntityLocal) it.next();
                LOG.debug("Processing invoice " + invoice.getId());
                // apply any invoice processing filter pluggable task
                try {
                    PluggableTaskManager taskManager =
                            new PluggableTaskManager(entityId,
                                Constants.PLUGGABLE_TASK_INVOICE_FILTER);
                    InvoiceFilterTask task = (InvoiceFilterTask) 
                            taskManager.getNextClass();
                    boolean isProcessable = true;
                    while (task != null) {
                        isProcessable = task.isApplicable(invoice, process);
                        if (!isProcessable) {
                            break; // no need to keep doing more tests
                        }
                        task = (InvoiceFilterTask) taskManager.getNextClass();
                    }

                    // include this invoice only if it complies with all the rules
                    if (isProcessable) {
                        // check for an invoice
                        if (holder == null) {
                            // Since there are no new invoices (therefore no orders),
                            // don't let invoices with positive balances generate
                            // an invoice.
                            if (invoice.getBalance() > 0) {
                                continue;
                            }

                            // no invoice/s yet (no applicable orders), so create one
                            holder = new NewInvoiceDTO();
                            holder.setDate(process.getBillingDate());
                            holder.setIsReview(isReview ? new Integer(
                                        1) : new Integer(0));
                            holder.setCarriedBalance(new Float(0));

                            // need to set a due date, so use the order default
                            OrderBL orderBl = new OrderBL();
                            OrderDTO order = new OrderDTO();
                            order.setBaseUserByUserId(user);
                            orderBl.set(order);
                            TimePeriod dueDatePeriod = orderBl.getDueDate();

                            holder.setDueDatePeriod(dueDatePeriod);
                            newInvoices.put(dueDatePeriod, holder);
                        }

                        InvoiceBL ibl = new InvoiceBL(invoice);
                        holder.addInvoice(ibl.getDTO());
                        // for those invoices wiht only overdue invoices, the
                        // currency has to be initialized
                        if (holder.getCurrencyId() == null) {
                            holder.setCurrencyId(invoice.getCurrencyId());
                        } else if (!holder.getCurrencyId().equals(
                                invoice.getCurrencyId())) {
                            throw new SessionInternalError("An invoice with different " +
                                    "currency is supported. " +
                                    "Currency = " + holder.getCurrencyId() +
                                    "invoice = " + invoice.getId());                                 
                        }
                        // update the amount of the new invoice that is due to
                        // previously unpaid overdue invoices
                        float iBalance = (invoice.getBalance() 
                                == null) ? 0 : invoice.getBalance().
                                    floatValue();
                        BigDecimal cBalance = new BigDecimal(holder.getCarriedBalance().toString());
                        cBalance = cBalance.add(new BigDecimal(iBalance));
                        holder.setCarriedBalance(new Float(cBalance.floatValue()));

                        // since this invoice is being delegated, reset its balance
                        if (!isReview) {
                            // this means that the delegated_invoice_id has to be set
                            invoice.setToProcess(new Integer(0));
                            if (invoice.getBalance() != null) {
                                invoice.setBalance(new Float(0));
                            }
                        }
                    }
                    
                    LOG.debug("invoice " + invoice.getId() + " result " + 
                            isProcessable);

                } catch (PluggableTaskException e) {
                    LOG.fatal("Problems handling task invoice filter.", e);
                    throw new SessionInternalError("Problems handling task invoice filter.");
                } catch (TaskException e) {
                    LOG.fatal("Problems excecuting task invoice filter.", e);
                    throw new SessionInternalError("Problems executing task invoice filter.");
                }

            }

        } catch (FinderException e) {
            // this means that this user doesn have invoices to process
            LOG.info("User " + user.getUserId()
                    + " without due invoices to process.");
        }

        if (newInvoices.size() == 0) {
            // no orders or invoices for this invoice
            LOG.debug("No applicable orders or invoices. No invoice generated " +
                    "(skipping invoices).");
            return null;
        }

        try {
            retValue = new InvoiceEntityLocal[newInvoices.size()];
            int index = 0;
            for (Iterator it = newInvoices.values().iterator(); 
                    it.hasNext();) {
                NewInvoiceDTO invoice = (NewInvoiceDTO) it.next();    
                /*
                 * Apply invoice composition tasks to the new invoices object
                 */
                composeInvoice(entityId, user.getUserId(), invoice);
                /*
                 * apply this object to the DB, generating the actual rows 
                 */
                // only if the invoice generated actually has some lines in it
                if (invoice.areLinesGeneratedEmpty()) {
                    LOG.warn("User " + user.getUserId() + " had orders or invoices but" +
                        " the invoice composition task didn't generate any lines.");
                    continue;
                } 
                    
                retValue[index] = generateDBInvoice(user.getUserId(), invoice, process,
                        Constants.ORDER_PROCESS_ORIGIN_PROCESS);
                // try to get this new invioce paid by previously unlinked 
                // payments
                if (paymentApplication && !isReview) {
                    PaymentBL pBL = new PaymentBL();
                    pBL.automaticPaymentApplication(retValue[index]);
                }

                index++;
            }
        } catch (PluggableTaskException e1) {
            LOG.error("Error handling pluggable tasks when composing an invoice");
            throw new SessionInternalError(e1);
        } catch (TaskException e1) {
            LOG.error("Task exception when composing an invoice");
            throw new SessionInternalError(e1);
        } catch (Exception e1) {
            LOG.error("Error, probably linking payments", e1);
            throw new SessionInternalError(e1);
        }

        return retValue;
    }
    
    private InvoiceEntityLocal generateDBInvoice(Integer userId, 
            NewInvoiceDTO newInvoice, BillingProcessEntityLocal process,
            Integer origin) 
            throws SessionInternalError, NamingException {
        // The invoice row is created first
        // all that fits in the DTO goes there
        newInvoice.calculateTotal();
        newInvoice.setBalance(newInvoice.getTotal());
        newInvoice.setInProcessPayment(new Integer(1));
        InvoiceBL invoiceBL = new InvoiceBL();
        
        try {
            invoiceBL.create(userId, newInvoice, process);
            invoiceBL.createLines(newInvoice);
        } catch (Exception e) {
            LOG.fatal("CreateException creating invoice record", e);
            throw new SessionInternalError("Couldn't create the invoice record");
        }
          
        createOrderProcess(newInvoice, invoiceBL.getEntity(), process,
                origin);

        return invoiceBL.getEntity();
        
    }

    private void createOrderProcess(NewInvoiceDTO newInvoice, 
            InvoiceEntityLocal invoice, BillingProcessEntityLocal process,
            Integer origin) 
            throws SessionInternalError {
        LOG.debug("Generating order process records...");
        // update the orders involved, now that their old data is not needed
        // anymore
        for (int f=0; f < newInvoice.getOrders().size(); f++) {
                        
            OrderDTO order = (OrderDTO) 
                    newInvoice.getOrders().get(f);           
                    
            LOG.debug(" ... order " + order.getId());
            // this will help later
            Date startOfBillingPeriod = (Date) newInvoice.getPeriods().get(f).firstElement().getStart();
            Date endOfBillingPeriod = (Date) newInvoice.getPeriods().get(f).lastElement().getEnd();
            Integer periods = (Integer) newInvoice.getPeriods().get(f).size();
 
            // We don't update orders if this is just a review
            if (newInvoice.getIsReview().intValue() == 0) {
                // update the to_process if applicable
                updateStatusFinished(order, startOfBillingPeriod, 
                        endOfBillingPeriod); 
                            
                // update this order process field
                updateNextBillableDay(order, endOfBillingPeriod);
            }                
                        
            // create the period and update the order-invoice relationship
            try {
                    
                OrderProcessDAS das = new OrderProcessDAS();
                OrderProcessDTO orderProcess = new OrderProcessDTO();
                orderProcess.setPeriodStart(startOfBillingPeriod);
                orderProcess.setPeriodEnd(endOfBillingPeriod);
                orderProcess.setIsReview(newInvoice.getIsReview());
                orderProcess.setPurchaseOrder(order);
                InvoiceDAS invDas = new InvoiceDAS();
                orderProcess.setInvoice(invDas.find(invoice.getId()));
                BillingProcessDAS proDas = new BillingProcessDAS();
                orderProcess.setBillingProcess(process != null ? proDas.find(process.getId()) : null);
                orderProcess.setPeriodsIncluded(periods);
                orderProcess.setOrigin(origin);
                orderProcess = das.save(orderProcess);
                LOG.debug("created order process id " + orderProcess.getId() +
                        " for order " + order.getId());
                    
            } catch (Exception  e) {
                throw new SessionInternalError(e);
            } 
        }
        
    }
    private void composeInvoice(Integer entityId, Integer userId,
            NewInvoiceDTO newInvoice) 
            throws PluggableTaskException, TaskException, SessionInternalError {
        newInvoice.setEntityId(entityId);
        PluggableTaskManager taskManager =
            new PluggableTaskManager(entityId,
                Constants.PLUGGABLE_TASK_INVOICE_COMPOSITION);
        InvoiceCompositionTask task =
            (InvoiceCompositionTask) taskManager.getNextClass();
        while (task != null) {
            task.apply(newInvoice, userId);
            task = (InvoiceCompositionTask) taskManager.getNextClass();
        }

        String validationMessage = newInvoice.validate();
        if (validationMessage != null) {
            LOG.error(
                "Composing invoice for entity " + entityId
                    + " invalid new invoice object: "
                    + validationMessage);
            throw new SessionInternalError(
                "NewInvoiceDTO:" + validationMessage);
        }
    }
    
    private boolean addOrderToInvoice(Integer entityId, OrderDTO order,
            NewInvoiceDTO newInvoice, Date processDate, int maxPeriods)
            throws CreateException, SessionInternalError, TaskException,
                PluggableTaskException {
        // require the calculation of the period dates
        PluggableTaskManager taskManager = new PluggableTaskManager(
                entityId, Constants.PLUGGABLE_TASK_ORDER_PERIODS);
        OrderPeriodTask optask = (OrderPeriodTask) 
                taskManager.getNextClass();
                        
        if (optask == null) {
            throw new SessionInternalError("There has to be " +
                "one order period pluggable task configured");
        }
        Date start = optask.calculateStart(order);
        Date end = optask.calculateEnd(order, processDate, maxPeriods, start);                         
        Vector<PeriodOfTime> periods = optask.getPeriods();
        // there isn't anything billable from this order
        if (periods.size() == 0) return false;
        
        if (start != null && end != null && start.after(end)) {
            // how come it starts after it ends ???
            throw new SessionInternalError("Calculated for " +
                    "order " + order.getId() + " a period that" 
                    + " starts after it ends:" + start + " " +
                    end);
        }
                        
        // add this order to the invoice being created
        newInvoice.addOrder(order, start, end, periods);
                        
                        
        // prepaid orders shouldn't have to be included
        // past time.
        if (order.getBillingTypeId().compareTo(
                Constants.ORDER_BILLING_PRE_PAID) == 0 &&
                start != null &&  // it has to be recursive too
                processDate.after(start)) {
                                    
            eLogger.warning(entityId, order.getId(), 
                    EventLogger.MODULE_BILLING_PROCESS,
                    EventLogger.BILLING_PROCESS_UNBILLED_PERIOD,
                    Constants.TABLE_PUCHASE_ORDER);
                                    
            LOG.warn("Order " + order.getId() + " is prepaid " +
                "but has past time not billed.");
        }
        
        // initialize the currency of the new invoice 
        if (newInvoice.getCurrencyId() == null) {
            newInvoice.setCurrencyId(order.getCurrencyId());
        } else {
            // now we are not supporting orders with different
            // currencies in the same invoice. Later this could be done
            if (!newInvoice.getCurrencyId().equals(order.getCurrencyId())) {
                throw new SessionInternalError("Orders with different " +
                        "currencies not supported in one invoice. " +
                        "Currency = " + newInvoice.getCurrencyId() +
                        "order = " + order.getId());
            }
        }
        return true;
    }
    
 
    static void updateStatusFinished(OrderDTO order, 
            Date startOfBillingPeriod,
            Date endOfBillingPeriod) 
            throws SessionInternalError{

        // all one timers are done
        if (order.getOrderPeriod().getId() == Constants.ORDER_PERIOD_ONCE) {
            OrderBL orderBL = new OrderBL(order);
            orderBL.setStatus(null, Constants.ORDER_STATUS_FINISHED);
        } else { // recursive orders get more complicated
            // except those that are immortal :)
            if (order.getActiveUntil() == null) {
                return; 
            }
            // see until when the incoming process will cover
            // compare if this is after the order exipres
            Logger log = Logger.getLogger(BillingProcessBL.class);
            log.debug("order " + order.getId() + "end of bp " +
                    endOfBillingPeriod
                    + " active until " + order.getActiveUntil());
            if (endOfBillingPeriod.
                    compareTo(Util.truncateDate(order.getActiveUntil())) >= 0) {
                OrderBL orderBL = new OrderBL(order);
                orderBL.setStatus(null, Constants.ORDER_STATUS_FINISHED);
            }
        }
    }
    
   
                
    static public Date getEndOfProcessPeriod(BillingProcessEntityLocal process) 
                throws SessionInternalError {
        GregorianCalendar cal = new GregorianCalendar();  
         
        cal.setTime(process.getBillingDate());
        cal.add(MapPeriodToCalendar.map(process.getPeriodUnitId()),
                process.getPeriodValue().intValue());
            
        return cal.getTime(); 
    }
        
    static public void updateNextBillableDay(OrderDTO order,
            Date end) throws SessionInternalError{
        // if this order won't be process ever again, the 
        // it shouldn't have a next billable day        
        if (order.getStatusId().equals(Constants.ORDER_STATUS_FINISHED)) {
            order.setNextBillableDay(null);        
        } else {
            order.setNextBillableDay(end);
        } 
    }
    
    public BillingProcessDTOEx getDtoEx(Integer language) 
            throws NamingException, SessionInternalError, FinderException {
        BillingProcessDTOEx retValue = new BillingProcessDTOEx();
        
        retValue.setBillingDate(billingProcess.getBillingDate());
        retValue.setEntityId(billingProcess.getEntityId());
        retValue.setId(billingProcess.getId());
        retValue.setPeriodUnitId(billingProcess.getPeriodUnitId());
        retValue.setPeriodValue(billingProcess.getPeriodValue());
        retValue.setIsReview(billingProcess.getIsReview());
        

        // now add the runs and grand total
        BillingProcessRunDTOEx grandTotal = 
                new BillingProcessRunDTOEx();
        int totalInvoices = 0;
        int runsCounter = 0;
        Vector<BillingProcessRunDTOEx> runs = new Vector<BillingProcessRunDTOEx>();
        // go throuhg every run
        for (Iterator it = billingProcess.getRuns().iterator(); 
                it.hasNext();) {
            BillingProcessRunEntityLocal run = (BillingProcessRunEntityLocal)
                    it.next();
            BillingProcessRunBL runBL = new BillingProcessRunBL(run);
            BillingProcessRunDTOEx runDto = runBL.getDTO(language);
            runs.add(runDto);
            runsCounter++;
            
            totalInvoices += run.getInvoiceGenerated().intValue();
            
            LOG.debug("Run:" + run.getId() + " has " + run.getTotals().size() + 
                    " total records");
            // go over the totals, since there's one per currency
            for (Iterator it2 = run.getTotals().iterator(); it2.hasNext();) {
                // the total to process 
                BillingProcessRunTotalEntityLocal totalRow =
                        (BillingProcessRunTotalEntityLocal) it2.next();
                        
                BillingProcessRunTotalDTOEx totalDto =
                        getTotal(totalRow.getCurrencyId(), runDto.getTotals());
                BillingProcessRunTotalDTOEx sum = 
                        getTotal(totalRow.getCurrencyId(), grandTotal.getTotals());
                BigDecimal totalTmp = new BigDecimal(totalDto.getTotalInvoiced().toString());
                totalTmp = totalTmp.add(new BigDecimal(sum.getTotalInvoiced().toString()));
                sum.setTotalInvoiced(new Float(totalTmp.floatValue()));
                
                totalTmp = new BigDecimal(totalDto.getTotalPaid().toString());
                totalTmp = totalTmp.add(new BigDecimal(sum.getTotalPaid().toString()));
                sum.setTotalPaid(new Float(totalTmp.toString()));
                // can't add up the not paid, because many runs will try to
                // get the same invoices paid, so the not paid field gets
                // duplicated ammounts.
                totalTmp = new BigDecimal(sum.getTotalInvoiced().toString());
                totalTmp = totalTmp.subtract(new BigDecimal(sum.getTotalPaid().toString()));
                sum.setTotalNotPaid(new Float(totalTmp.floatValue()));
                
                // make sure this total has the currency name initialized
                if (sum.getCurrencyName() == null) {
                    CurrencyBL currency = new CurrencyBL(sum.getCurrencyId());
                    sum.setCurrencyName(currency.getEntity().getDescription(
                            language));
                }
                // add the payment method totals
                for (Enumeration en = totalDto.getPmTotals().keys(); 
                        en.hasMoreElements();) {
                    String method = (String) en.nextElement();
                    BigDecimal methodTotal = new BigDecimal(totalDto.getPmTotals().get(method).toString());
                    
                    if (sum.getPmTotals().containsKey(method)) {
                        if (sum.getPmTotals().get(method) != null) {
                        	methodTotal = methodTotal.add(new BigDecimal(((Float) sum.getPmTotals().
                                     get(method)).toString()));
                            
                        }
                    }
                    sum.getPmTotals().put(method, new Float(methodTotal.floatValue()));
                }
                
                LOG.debug("Added total to run dto. PMs in total:" + sum.getPmTotals().size() 
                        + " now grandTotal totals:" + grandTotal.getTotals().size());
            }
        }
        
        grandTotal.setInvoiceGenerated(new Integer(totalInvoices));
        
        retValue.setRetries(new Integer(runsCounter));
        retValue.setRuns(runs);
        retValue.setGrandTotal(grandTotal);
        retValue.setBillingDateEnd(getEndOfProcessPeriod(billingProcess));
        retValue.setOrdersProcessed(new Integer(billingProcess.
                getOrderProcesses().size()));
        
        return retValue;
    }
 
    public CachedRowSet getList(Integer entityId) 
            throws SQLException, Exception {
        prepareStatement(ProcessSQL.generalList);
        cachedResults.setInt(1,entityId.intValue());
        execute();
        conn.close();
        return cachedResults;
    
    }
    
    // to avoid deadlocks ... this does not need the process entity row at all
    // to get the runs
    public CachedRowSet getRuns(Integer processId) throws SQLException,
            Exception {
        prepareStatement(ProcessSQL.findRuns);
        cachedResults.setInt(1, processId.intValue());
        execute();
        conn.close();
        return cachedResults;

    }

    
    public int getLast(Integer entityId) 
            throws SQLException, Exception {
        int retValue = -1;
        prepareStatement(ProcessSQL.lastId);
        cachedResults.setInt(1,entityId.intValue());
        execute();
        conn.close();
        
        if (cachedResults.next()) {
            retValue = cachedResults.getInt(1);
        } 
        
        return retValue; 
    }
    
    public Integer[] getToRetry(Integer entityId) 
            throws SQLException, Exception {
        Vector list = new Vector();

        prepareStatement(ProcessSQL.findToRetry);
        cachedResults.setInt(1, entityId.intValue());
        execute();
        conn.close();

        while (cachedResults.next()) {
            list.add(new Integer(cachedResults.getInt(1)));
        }
        
        Integer retValue[] = new Integer[list.size()];
        list.toArray(retValue);

        return retValue;
    }
    
    
    /**
     * Tries to get paid the invoice of the parameter.
     * The processs Id and runId are only to update the run totals.
     * Only one of them is required. The runId should be passed if
     * this is a retry, otherwise the processId.
     * @param processId
     * @param runId
     * @param newInvoice
     * @throws NamingException
     * @throws SessionInternalError
     * @throws CreateException
     * @throws FinderException
     */
    public void generatePayment(Integer processId, Integer runId,
            Integer invoiceId) 
            throws NamingException, SessionInternalError, CreateException,
                FinderException {
        PaymentSessionLocalHome paymentHome =
                (PaymentSessionLocalHome) EJBFactory.lookUpLocalHome(
                PaymentSessionLocalHome.class,
                PaymentSessionLocalHome.JNDI_NAME);

        InvoiceBL invoiceBL = new InvoiceBL(invoiceId);
        InvoiceEntityLocal newInvoice = invoiceBL.getEntity();
        PaymentSessionLocal paymentSess = paymentHome.create();
        Integer result = paymentSess.generatePayment(newInvoice);
        Integer currencyId = newInvoice.getCurrencyId();
        BillingProcessRunBL run = new BillingProcessRunBL();
        if (processId != null) {
            run.setProcess(processId);
        } else {
            run.set(runId);
        }
        // null means that the payment wasn't successful
        // otherwise the result is the method id
        if (result != null) {
            run.updateNewPayment(currencyId, result, newInvoice.getTotal(), 
                    true);
        } else {
            run.updateNewPayment(currencyId, result, newInvoice.getTotal(), 
                    false);
        }
    }
    
    public BillingProcessRunTotalDTOEx getTotal(Integer currencyId,
            Vector totals) {
        BillingProcessRunTotalDTOEx retValue = null;
        for (int f = 0; f < totals.size(); f++) {
            BillingProcessRunTotalDTOEx total = (BillingProcessRunTotalDTOEx) totals.get(f);
            if (total.getCurrencyId().equals(currencyId)) {
                retValue = total;
                break;
            }
        }
        
        // it is looking for a total that doesn't exist
        if (retValue == null) {
            retValue = new BillingProcessRunTotalDTOEx(null, currencyId,
                    new Float(0), new Float(0), new Float(0));
            totals.add(retValue);
        }
        
        return retValue;
    }

    public BillingProcessDTOEx getReviewDTO(Integer entityId, 
            Integer languageId) 
            throws SessionInternalError, NamingException {
        try {
            billingProcess = billingProcessHome.findReview(entityId); 
            return getDtoEx(languageId);
        } catch (FinderException e) {
            return null;
        }
    }    
    
    public boolean isReviewPresent(Integer entityId) {
        boolean ret = false;
        try {
            billingProcessHome.findReview(entityId);
            ret = true;
        } catch (FinderException e) {
            // nothing to do
        }
        return ret;
    }

    public void purgeReview(Integer entityId, boolean isReview) 
            throws NamingException, CreateException, RemoveException {
        try {
            BillingProcessEntityLocal review = billingProcessHome.findReview(
                    entityId);
            // if we are here, a review exists
            ConfigurationBL configBL = new ConfigurationBL(entityId);
            if (configBL.getEntity().getGenerateReport().intValue() == 1 &&
                    !configBL.getEntity().getReviewStatus().equals(
                        Constants.REVIEW_STATUS_APPROVED) && !isReview) {
                eLogger.warning(entityId, configBL.getEntity().getId(), 
                        EventLogger.MODULE_BILLING_PROCESS, 
                        EventLogger.BILLING_REVIEW_NOT_APPROVED, 
                        Constants.TABLE_BILLING_PROCESS_CONFIGURATION);
            }
            // delete the review
            LOG.debug("Removing review id = " + review.getId() + " from " +
                    " entity " + entityId);
            // this is needed while the order process is JPA, but the billing process is Entity
            OrderProcessDAS processDas = new OrderProcessDAS();
            com.sapienter.jbilling.server.process.db.BillingProcessDTO processDto = new BillingProcessDAS().find(review.getId());
            for (OrderProcessDTO orderDto : processDto.getOrderProcesses()) {
            	processDas.delete(orderDto);
            }
            processDto.getOrderProcesses().clear();
            // otherwise this line would cascade de delete
            review.remove();
        } catch (FinderException e) {
            // there's no review for this entity, nothing to purge/check then
        }
    }

    @SuppressWarnings("unchecked")
    private void processOrderToInvoiceEvents(NewInvoiceDTO newInvoice, Integer entityId) {
        Vector<OrderDTO> orders = newInvoice.getOrders();
        Vector<Vector<PeriodOfTime>> periods = newInvoice.getPeriods();
        for (int i = 0; i < orders.size(); i++) {
            OrderDTO order = orders.get(i);
            Integer orderId = order.getId();
            Integer userId = findUserId(order);
            for (PeriodOfTime period : periods.get(i)) {
                OrderToInvoiceEvent newEvent =
                    new OrderToInvoiceEvent(entityId, userId, orderId);
                newEvent.setStart(period.getStart());
                newEvent.setEnd(period.getEnd());
                EventManager.process(newEvent);
            }
        }
    }

    private Integer findUserId(OrderDTO order) {
        if (order.getUser().getCustomer().getParent() == null){
            return order.getUser().getUserId();
        } else {
            Integer flag = order.getUser().getCustomer().getInvoiceChild();
            if (flag == null || flag == 0) {
                // there is a parent, the invoice should go to her
                return order.getUser().getCustomer().getParent().
                        getBaseUser().getUserId(); // yes, I like CRM!
            } else {
                // the child gets its own invoices
                return order.getUser().getUserId();
            }
        }
    }

}
