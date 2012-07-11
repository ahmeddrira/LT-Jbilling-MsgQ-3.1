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

package com.sapienter.jbilling.server.security;

import com.sapienter.jbilling.server.invoice.db.InvoiceDAS;
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO;
import com.sapienter.jbilling.server.item.db.ItemDAS;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.ItemTypeDAS;
import com.sapienter.jbilling.server.item.db.ItemTypeDTO;
import com.sapienter.jbilling.server.item.db.PlanDAS;
import com.sapienter.jbilling.server.item.db.PlanDTO;
import com.sapienter.jbilling.server.mediation.db.MediationConfiguration;
import com.sapienter.jbilling.server.mediation.db.MediationConfigurationDAS;
import com.sapienter.jbilling.server.mediation.db.MediationProcess;
import com.sapienter.jbilling.server.mediation.db.MediationProcessDAS;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDAS;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.payment.db.PaymentDAS;
import com.sapienter.jbilling.server.payment.db.PaymentDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskBL;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.process.db.BillingProcessDAS;
import com.sapienter.jbilling.server.process.db.BillingProcessDTO;
import com.sapienter.jbilling.server.user.partner.db.Partner;
import com.sapienter.jbilling.server.user.partner.db.PartnerDAS;
import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import org.hibernate.ObjectNotFoundException;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * WSSecurityMethodMapper
 *
 * @author Brian Cowdery
 * @since 02-11-2010
 */
public class WSSecurityMethodMapper {

    private static final Class WS_INTERFACE = IWebServicesSessionBean.class;

    /** Types of secured web-service methods */
    public static enum Type {

        USER {
            public WSSecured getMappedSecuredWS(Serializable id) {
                return id != null ? new MappedSecuredWS(null, (Integer) id) : null;
            }
        },

        PARTNER {
            public WSSecured getMappedSecuredWS(Serializable id) {
                Partner partner = new PartnerDAS().find(id);
                return partner != null ? new MappedSecuredWS(null, partner.getUser().getId()) : null;
            }
        },

        ITEM {
            public WSSecured getMappedSecuredWS(Serializable id) {
                ItemDTO item = new ItemDAS().find(id);
                return item != null ? new MappedSecuredWS(item.getEntity().getId(), null) : null;
            }
        },

        ITEM_CATEGORY {
            public WSSecured getMappedSecuredWS(Serializable id) {
                ItemTypeDTO itemType = new ItemTypeDAS().find(id);
                return itemType != null ? new MappedSecuredWS(itemType.getEntity().getId(), null) : null;
            }
        },

        ORDER {
            public WSSecured getMappedSecuredWS(Serializable id) {
                OrderDTO order = new OrderDAS().find(id);
                return order != null ? new MappedSecuredWS(null, order.getUserId()) : null;
            }
        },

        ORDER_LINE {
            public WSSecured getMappedSecuredWS(Serializable id) {
                OrderLineDTO line = new OrderLineDAS().find(id);
                return line != null ? new MappedSecuredWS(null, line.getPurchaseOrder().getUserId()) : null;
            }
        },

        INVOICE {
            public WSSecured getMappedSecuredWS(Serializable id) {
                InvoiceDTO invoice = new InvoiceDAS().find(id);
                return invoice != null ? new MappedSecuredWS(null, invoice.getUserId()) : null;
            }
        },

        PAYMENT {
            public WSSecured getMappedSecuredWS(Serializable id) {
                PaymentDTO payment = new PaymentDAS().find(id);
                return payment != null ? new MappedSecuredWS(null, payment.getBaseUser().getId()) : null;
            }
        },

        BILLING_PROCESS {
            public WSSecured getMappedSecuredWS(Serializable id) {
                BillingProcessDTO process = new BillingProcessDAS().find(id);
                return process != null ? new MappedSecuredWS(process.getEntity().getId(), null) : null;
            }
        },

        MEDIATION_PROCESS {
            public WSSecured getMappedSecuredWS(Serializable id) {
                MediationProcess process = new MediationProcessDAS().find(id);
                return process != null ? new MappedSecuredWS(process.getConfiguration().getEntityId(), null) : null;
            }
        },

        MEDIATION_CONFIGURATION {
            public WSSecured getMappedSecuredWS(Serializable id) {
                MediationConfiguration config = new MediationConfigurationDAS().find(id);
                return config != null ? new MappedSecuredWS(config.getEntityId(), null) : null;
            }
        },

        PLUG_IN {
            public WSSecured getMappedSecuredWS(Serializable id) {
                PluggableTaskDTO task = new PluggableTaskBL((Integer)id).getDTO();
                return task != null ? new MappedSecuredWS(task.getEntityId(), null) : null;
            }
        },

        PLAN {
            public WSSecured getMappedSecuredWS(Serializable id) {
                PlanDTO plan = new PlanDAS().find(id);
                return plan != null ? new MappedSecuredWS(plan.getItem().getEntity().getId(), null) : null;
            }
        };



        /**
         * implemented by each Type to return a secure object for validation based on the given ID.
         *
         * @param id id of the object type
         * @return secure object for validation
         */
        public abstract WSSecured getMappedSecuredWS(Serializable id);
    }

    /** Secured web-service methods */
    public static enum WSSecureMethod {

        GET_USER                        ("getUserWS", 0, Type.USER),        
        DELETE_USER                     ("deleteUser", 0, Type.USER),
        GET_USER_CONTACT                ("getUserContactsWS", 0, Type.USER),
        UPDATE_USER_CONTACT             ("updateUserContact", 0, Type.USER), // todo: should validate user and contact type ids
        UPDATE_CREDIT_CARD              ("updateCreditCard", 0, Type.USER),
        UPDATE_ACH                      ("updateAch", 0, Type.USER),
        SET_AUTH_PAYMENT_TYPE           ("setAuthPaymentType", 0, Type.USER),
        GET_AUTH_PAYMENT_TYPE           ("getAuthPaymentType", 0, Type.USER),
        GET_PARTNER                     ("getPartner", 0, Type.PARTNER),

        GET_ITEM                        ("getItem", 0, Type.ITEM), // todo: should validate item id and user id
        DELETE_ITEM                     ("deleteItem", 0, Type.ITEM),
        DELETE_ITEM_CATEGORY            ("deleteItemCategory", 0, Type.ITEM_CATEGORY),
        GET_USER_ITEMS_BY_CATEGORY      ("getUserItemsByCategory", 0, Type.USER),
        IS_USER_SUBSCRIBED_TO           ("isUserSubscribedTo", 0, Type.USER),        
        GET_LATEST_INVOICE_BY_ITEM_TYPE ("getLatestInvoiceByItemType", 0, Type.USER),
        GET_LAST_INVOICES_BY_ITEM_TYPE  ("getLastInvoicesByItemType", 0, Type.USER),
        GET_LATEST_ORDER_BY_ITEM_TYPE   ("getLatestOrderByItemType", 0, Type.USER),
        GET_LAST_ORDERS_BY_ITEM_TYPE    ("getLastOrdersByItemType", 0, Type.USER),

        VALIDATE_PURCHASE               ("validatePurchase", 0, Type.USER),
        VALIDATE_MULTI_PURCHASE         ("validateMultiPurchase", 0, Type.USER),

        GET_ORDER                       ("getOrder", 0, Type.ORDER),
        DELETE_ORDER                    ("deleteOrder", 0, Type.ORDER),
        GET_CURRENT_ORDER               ("getCurrentOrder", 0, Type.USER),
        UPDATE_CURRENT_ORDER            ("updateCurrentOrder", 0, Type.USER),
        GET_ORDER_LINE                  ("getOrderLine", 0, Type.ORDER_LINE),
        GET_ORDER_BY_PERIOD             ("getOrderByPeriod", 0, Type.USER),
        GET_LATEST_ORDER                ("getLatestOrder", 0, Type.USER),
        GET_LAST_ORDERS                 ("getLastOrders", 0, Type.USER),
        
        GET_INVOICE                     ("getInvoiceWS", 0, Type.INVOICE),
        CREATE_INVOICE                  ("createInvoice", 0, Type.USER),
        CREATE_INVOICE_FROM_ORDER       ("createInvoiceFromOrder", 0, Type.ORDER),
        DELETE_INVOICE                  ("deleteInvoice", 0, Type.INVOICE),
        GET_ALL_INVOICES                ("getAllInvoices", 0, Type.USER),
        GET_LATEST_INVOICES             ("getLatestInvoice", 0, Type.USER),
        GET_LAST_INVOICES               ("getLastInvoices", 0, Type.USER),
        GET_USER_INVOICES_BY_DATE       ("getUserInvoicesByDate", 0, Type.USER),
        GET_PAPER_INVOICE_PDF           ("getPaperInvoicePDF", 0, Type.INVOICE),

        GET_PAYMENT                     ("getPayment", 0, Type.PAYMENT),
        GET_LATEST_PAYMENTS             ("getLatestPayment", 0, Type.USER),
        GET_LAST_PAYMENTS               ("getLastPayments", 0, Type.USER),
        PAY_INVOICE                     ("payInvoice", 0, Type.INVOICE),

        GET_BILLING_PROCESS             ("getBillingProcess", 0, Type.BILLING_PROCESS),
        GET_BILLING_PROCESS_GENERATED   ("getBillingProcessGeneratedInvoices", 0, Type.BILLING_PROCESS),
        
        GET_ALL_EVENTS_FOR_ORDER        ("getMediationEventsForOrder", 0, Type.ORDER),
        GET_MEDIATION_RECORDS           ("getMediationRecordsByMediationProcess", 0, Type.MEDIATION_PROCESS),
        DELETE_MEDIATION_CONFIGURATION  ("deleteMediationConfiguration", 0, Type.MEDIATION_CONFIGURATION),
        
        UPDATE_ORDER_LINE_PROVISIONING  ("updateOrderAndLineProvisioningStatus", 0, Type.ORDER),
        UPDATE_LINE_PROVISIONING        ("updateLineProvisioningStatus", 0, Type.ORDER_LINE),
        SAVE_CUSTOMER_NOTES             ("saveCustomerNotes", 0, Type.USER),
        NOTIFY_INVOICE_BY_EMAIL         ("notifyInvoiceByEmail", 0, Type.INVOICE),
        NOTIFY_PAYMENT_BY_EMAIL         ("notifyPaymentByEmail", 0, Type.PAYMENT),
        DELETE_PLUGIN                   ("deletePlugin", 0, Type.PLUG_IN),

        GET_PLAN                        ("getPlanWS", 0, Type.PLAN),
        DELETE_PLAN                     ("deletePlan", 0, Type.PLAN),
        ADD_PLAN_PRICE                  ("addPlanPrice", 0, Type.PLAN),
        IS_CUSTOMER_SUBSCRIBED          ("isCustomerSubscribed", 0, Type.PLAN),
        GET_SUBSCRIBED_CUSTOMERS        ("getSubscribedCustomers", 0, Type.PLAN),
        GET_PLANS_BY_SUBSCRIPTION_ITEM  ("getPlansBySubscriptionItem", 0, Type.ITEM),
        GET_PLANS_BY_AFFECTED_ITEM      ("getPlansByAffectedItem", 0, Type.ITEM),
        GET_CUSTOMER_PRICE              ("getCustomerPrice", 0, Type.USER),
        ;
        
        private Method method;
        private Integer IdArgIndex;
        private Type type;

        /**
         * Secure method definition
         *
         * @param methodName method name from web service interface
         * @param idArgIndex method argument index of the entity id
         * @param type type of entity id
         */
        private WSSecureMethod(String methodName, Integer idArgIndex, Type type) {
            // find method in the web-service interface by name. web-services don't allow overloaded
            // method names so we don't have to worry about duplicate names or providing method parameters
            for (Method method : WS_INTERFACE.getDeclaredMethods())
                if (method.getName().equals(methodName))
                    this.method = method;

            if (method == null)
                throw new IllegalArgumentException("Method '" + methodName + "' does not exist on " + WS_INTERFACE);

            this.IdArgIndex = idArgIndex;
            this.type = type;
        }

        public Method getMethod() {
            return method;
        }

        public Integer getIdArgIndex() {
            return IdArgIndex;
        }

        public Type getType() {
            return type; 
        }

        public static WSSecured getMappedSecuredWS(Method method, Object[] args) {
            if (method != null)
                for (WSSecureMethod secure : values()) 
                    if (method.equals(secure.getMethod()))
                        if (secure.getIdArgIndex() <= args.length) {
                            try {
                                return secure.getType().getMappedSecuredWS((Serializable) args[secure.getIdArgIndex()]);
                            } catch (ObjectNotFoundException e) {
                                // hibernate complains loudly... object does not exist, no reason to validate.
                                return null;
                            }
                        }
                
            return null;
        }
    }

    /**
     * Return a WSSecured object mapped from the given method and method arguments for validation.
     * This produced a secure object for validation from web-service method calls that only accept and return
     * ID's instead of WS objects that can be individually validated.
     *
     * @param method method to map
     * @param args method arguments
     * @return instance of WSSecured mapped from the given entity, null if entity could not be mapped.
     */
    public static WSSecured getMappedSecuredWS(Method method, Object[] args) {
        return WSSecureMethod.getMappedSecuredWS(method, args);
    }
}
