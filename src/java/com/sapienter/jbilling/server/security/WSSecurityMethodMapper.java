/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.security;

import com.sapienter.jbilling.server.invoice.db.InvoiceDAS;
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO;
import com.sapienter.jbilling.server.item.db.ItemTypeDAS;
import com.sapienter.jbilling.server.item.db.ItemTypeDTO;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.util.IWebServicesSessionBean;

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
    private enum Type {
        ORDER {
            public WSSecured getMappedSecuredWS(Serializable id) {
                OrderDTO order = new OrderDAS().find(id);
                return order != null ? new MappedSecuredWS(null, order.getUserId()) : null;
            }
        },

        INVOICE {
            public WSSecured getMappedSecuredWS(Serializable id) {
                InvoiceDTO invoice = new InvoiceDAS().find(id);
                return invoice != null ? new MappedSecuredWS(null, invoice.getUserId()) : null;
            }
        },

        USER {
            public WSSecured getMappedSecuredWS(Serializable id) {
                return id != null ? new MappedSecuredWS(null, (Integer) id) : null;
            }
        },

        ITEM_CATEGORY {
            public WSSecured getMappedSecuredWS(Serializable id) {
                ItemTypeDTO itemType = new ItemTypeDAS().find(id);
                return itemType != null ? new MappedSecuredWS(itemType.getEntity().getId(), null) : null;
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
    private enum WSSecureMethod {

        CREATE_INVOICE                  ("createInvoice", 0, Type.USER),
        CREATE_INVOICE_FROM_ORDER       ("createInvoiceFromOrder", 0, Type.ORDER),
        DELETE_INVOICE                  ("deleteInvoice", 0, Type.INVOICE),
        GET_ALL_INVOICES                ("getAllInvoices", 0, Type.USER),
        GET_LAST_INVOICES               ("getLastInvoices", 0, Type.USER),
        GET_PAPER_INVOICE_PDF           ("getPaperInvoicePDF", 0, Type.INVOICE),
        
        PAY_INVOICE                     ("payInvoice", 0, Type.INVOICE),
        
        DELETE_ORDER                    ("deleteOrder", 0, Type.ORDER),
        GET_ORDER_BY_PERIOD             ("getOrderByPeriod", 0, Type.USER),
        GET_LAST_ORDERS_BY_ITEM_TYPE    ("getLastOrdersByItemType", 0, Type.USER),

        DELETE_USER                     ("deleteUser", 0, Type.USER),
        IS_USER_SUBSCRIBED_TO           ("isUserSubscribedTo", 0, Type.USER),
        GET_USER_ITEMS_BY_CATEGORY      ("getUserItemsByCategory", 0, Type.USER),
        UPDATE_ACH                      ("updateAch", 0, Type.USER),
        GET_AUTH_PAYMENT_TYPE           ("getAuthPaymentType", 0, Type.USER),
        SET_AUTH_PAYMENT_TYPE           ("setAuthPaymentType", 0, Type.USER),

        UPDATE_USER_CONTACT             ("updateUserContact", 0, Type.USER),
        GET_USER_CONTACT                ("getUserContactsWS", 0, Type.USER),

        DELETE_ITEM_CATEGORY            ("deleteItemCategory", 0, Type.ITEM_CATEGORY);

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
            // method names so we don't have to worry about duplicates or providing method parameters
            for (Method method : WS_INTERFACE.getDeclaredMethods())
                if (method.getName().equals(methodName))
                    this.method = method;

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
                        if (secure.getIdArgIndex() <= args.length)
                            return secure.getType().getMappedSecuredWS((Serializable) args[secure.getIdArgIndex()]);
                
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
