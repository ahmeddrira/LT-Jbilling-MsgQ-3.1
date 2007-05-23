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

package com.sapienter.jbilling.server.list;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.ListFieldEntityLocal;
import com.sapienter.jbilling.server.customer.CustomerBL;
import com.sapienter.jbilling.server.entity.CurrencyDTO;
import com.sapienter.jbilling.server.entity.ListFieldDTO;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.item.CurrencyBL;
import com.sapienter.jbilling.server.item.ItemListBL;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.payment.PaymentBL;
import com.sapienter.jbilling.server.process.BillingProcessBL;
import com.sapienter.jbilling.server.user.PartnerBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.GetSelectableOptions;
import com.sapienter.jbilling.server.util.PreferenceBL;

/**
 *
 * Generic list provider.
 *
 * @author emilc
 * @ejb:bean name="com/sapienter/jbilling/server/util/ListSession"
 *           display-name="The list session facade"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type="remote"
 *           jndi-name="com/sapienter/jbilling/server/util/ListSession"
 * 
 **/

public class ListSessionBean implements javax.ejb.SessionBean {

    Logger log = null;

    public void ejbActivate() { }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public void ejbCreate() {
    }
    public void ejbPassivate() {
        log.debug("getting passivated");
    }
    public void ejbRemove() {
        log.debug("getting removed");
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public CachedRowSet getList(String type, Hashtable parameters) 
            throws SessionInternalError {
        
        CachedRowSet retValue = null;
        try {
            log.debug("List requested for type" + type + " param = " + 
                    parameters);

            if (type.equals(Constants.LIST_TYPE_CUSTOMER)) {
                CustomerBL list = new CustomerBL();
                int entityId =  ((Integer) parameters.get("entityId")).intValue();
                Integer userType = (Integer) parameters.get("userType");
                Integer userId = (Integer) parameters.get("userId");
                retValue = list.getList(entityId, userType, userId); 
            } else if (type.equals(Constants.LIST_TYPE_CUSTOMER_SIMPLE)) {
                CustomerBL list = new CustomerBL();
                int entityId =  ((Integer) parameters.get("entityId")).intValue();
                Integer userType = (Integer) parameters.get("userType");
                Integer userId = (Integer) parameters.get("userId");
                retValue = list.getCustomerList(entityId, userType, userId);
            } else if (type.equals(Constants.LIST_TYPE_SUB_ACCOUNTS)) {
                CustomerBL list = new CustomerBL();
                Integer userId = (Integer) parameters.get("userId");
                retValue = list.getSubAccountsList(userId);
            } else if (type.equals(Constants.LIST_TYPE_PARTNERS_CUSTOMER)) {
                CustomerBL list = new CustomerBL();
                Integer partnerId = (Integer) parameters.get("partnerId");
                PartnerBL partner = new PartnerBL(partnerId);
                int entityId = partner.getEntity().getUser().getEntity().
                        getId().intValue();
                Integer userType = Constants.TYPE_PARTNER;
                Integer userId = partner.getEntity().getUser().getUserId();
                retValue = list.getCustomerList(entityId, userType, userId);
            } else if (type.equals(Constants.LIST_TYPE_ITEM_TYPE)) {
                ItemListBL list = new ItemListBL();
                Integer entityId = (Integer) parameters.get("entityId");
                // Integer languageId = (Integer) parameters.get("languageId");
                retValue = list.getTypeList(entityId);
            } else if (type.equals(Constants.LIST_TYPE_ITEM)) {
                ItemListBL list = new ItemListBL();
                Integer entityId = (Integer) parameters.get("entityId");
                //Integer languageId = (Integer) parameters.get("languageId");
                retValue = list.getList(entityId);
            } else if (type.equals(Constants.LIST_TYPE_ITEM_USER_PRICE)) {
                ItemListBL list = new ItemListBL();
                Integer entityId = (Integer) parameters.get("entityId");
                Integer userId = (Integer) parameters.get("userId");
                Integer languageId = (Integer) parameters.get("languageId");
                retValue = list.getUserPriceList(entityId, userId, languageId);
            } else if (type.equals(Constants.LIST_TYPE_PROMOTION)) {
                ItemListBL list = new ItemListBL();
                Integer entityId = (Integer) parameters.get("entityId");
                Integer languageId = (Integer) parameters.get("languageId");
                retValue = list.getPromotionList(entityId, languageId);
            } else if (type.equals(Constants.LIST_TYPE_PAYMENT) ||
                    type.equals(Constants.LIST_TYPE_REFUND)) {
                PaymentBL list = new PaymentBL();
                Integer entityId = (Integer) parameters.get("entityId");
                Integer languageId = (Integer) parameters.get("languageId");
                Integer userType = (Integer) parameters.get("userType");
                Integer userId = (Integer) parameters.get("userId");
                retValue = list.getList(entityId, languageId, userType,
                        userId, type.equals(Constants.LIST_TYPE_REFUND));                                
            } else if (type.equals(Constants.LIST_TYPE_ORDER)) {
                OrderBL list = new OrderBL();
                Integer entityId = (Integer) parameters.get("entityId");
                Integer userType = (Integer) parameters.get("userType");
                Integer userId = (Integer) parameters.get("userId");
                retValue = list.getList(entityId, userType, userId);                                
            } else if (type.equals(Constants.LIST_TYPE_INVOICE)) {
                InvoiceBL list = new InvoiceBL();
                Integer userId = (Integer) parameters.get("userId");
                retValue = list.getPayableInvoicesByUser(userId);
            } else if (type.equals(Constants.LIST_TYPE_INVOICE_ORDER)) {
                InvoiceBL list = new InvoiceBL();
                Integer orderId = (Integer) parameters.get("orderId");
                retValue = list.getList(orderId);
            } else if (type.equals(Constants.LIST_TYPE_PAYMENT_USER)) {
                PaymentBL list = new PaymentBL();
                // this one gets the payments of the selected user
                Integer languageId = (Integer) parameters.get("languageId");
                Integer userId = (Integer) parameters.get("userId");
                retValue = list.getRefundableList(languageId, userId);
            } else if (type.equals(Constants.LIST_TYPE_INVOICE_GRAL)) {
                InvoiceBL list = new InvoiceBL();
                Integer entityId = (Integer) parameters.get("entityId");
                Integer userType = (Integer) parameters.get("userType");
                Integer userId = (Integer) parameters.get("userId");
                retValue = list.getList(entityId, userType, userId);  
            } else if (type.equals(Constants.LIST_TYPE_PROCESS)) {
                BillingProcessBL list = new BillingProcessBL();
                Integer entityId = (Integer) parameters.get("entityId");
                retValue = list.getList(entityId);
            } else if (type.equals(Constants.LIST_TYPE_PROCESS_INVOICES)) {
                InvoiceBL list = new InvoiceBL();
                Integer processId = (Integer) parameters.get("processId");
                retValue = list.getInvoicesByProcessId(processId);  
            } else if (type.equals(Constants.LIST_TYPE_PROCESS_ORDERS)) {
                OrderBL list = new OrderBL();
                Integer processId = (Integer) parameters.get("processId");
                retValue = list.getOrdersByProcessId(processId);  
            } else if (type.equals(Constants.LIST_TYPE_NOTIFICATION_TYPE)) {
                NotificationBL list = new NotificationBL();
                Integer languageId = (Integer) parameters.get("languageId");
                retValue = list.getTypeList(languageId);  
            } else if (type.equals(Constants.LIST_TYPE_PARTNER)) {
                PartnerBL list = new PartnerBL();
                Integer entityId = (Integer) parameters.get("entityId");  
                retValue = list.getList(entityId);   
            } else if (type.equals(Constants.LIST_TYPE_PAYOUT)) {        
                PartnerBL list = new PartnerBL();
                Integer partnerId = (Integer) parameters.get("partnerId");  
                retValue = list.getPayoutList(partnerId);   
            } else {
                log.error("list type " + type + " is not supported");
                throw new Exception("list type " + type +
                        " is not supported");
            }
                                 
        } catch (Exception e) {
            log.error("Exception retreiving list " + type, e);
            throw new SessionInternalError("Generic list");
        }
        
        return retValue;
    }
    
    /**
    * @ejb:interface-method view-type="remote"
    */
    public ListDTO getDtoList(String type, Hashtable parameters) 
            throws SessionInternalError {
        ListDTO retValue = null;
        
        try {
            if (type.equals(Constants.LIST_TYPE_ITEM_ORDER)) {
                ItemListBL list = new ItemListBL();
                Integer entityId = (Integer) parameters.get("entityId");
                Integer languageId = (Integer) parameters.get("languageId");
                Integer userId = (Integer) parameters.get("userId");
                retValue = list.getOrderList(entityId, languageId, userId); 
            }
        } catch (Exception e) {
            log.error("Exception retreiving list " + type, e);
            throw new SessionInternalError("Generic list");
        }
        
        return retValue;
    }    

    /**
     * This really doesn't belong to the list session bean, but it needs some
     * remote interface, so might as well put it here. All the real code it in
     * GetSelectableOptions.java
     * @ejb:interface-method view-type="remote"
     */
    public Collection getOptions(String type, Integer languageId, 
            Integer entityId, Integer executorType) throws SessionInternalError {
        log.debug("getting option " + type);
        return GetSelectableOptions.getOptions(type, languageId, entityId,
                executorType);
    }
    
    /**
     * Returns a map to all the currencies with 'id' - 'symbol'.
     * This is useful as an application wide object for reference
     * @return
     * @ejb:interface-method view-type="remote"
     */
    public CurrencyDTO[] getCurrencySymbolsMap() 
            throws SessionInternalError {
        try {
            CurrencyBL currency = new CurrencyBL();
            CurrencyDTO[] retValue = currency.getSymbols();
            log.debug("symbols total = " + retValue.length + " content=" + retValue);
            return retValue;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    public void setSessionContext(javax.ejb.SessionContext sessionContext) {
        log = Logger.getLogger(ListSessionBean.class);
    }
    
    /*
     * The above are legacy methods. The follwing are the new list methods
     * @author Emil
     */
    
    /**
     * @ejb:interface-method view-type="remote"
     */
    public void updateStatistics() 
            throws SessionInternalError {
        try {
            ListBL list = new ListBL();
            list.updateStatistics();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    /**
     * @ejb:interface-method view-type="remote"
     */
    public CachedRowSet getPage(Integer start, Integer end, Integer size, 
            Integer listId, Integer entityId, Boolean direction, 
            Integer fieldId, Hashtable parameters)
            throws SessionInternalError {
        try {
            ListBL list = new ListBL();
            return list.getPage(start, end, size.intValue(), listId, entityId, 
                    direction.booleanValue(), fieldId, parameters);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public CachedRowSet search(String start, String end, Integer fieldId, 
            Integer listId, Integer entityId, Hashtable parameters)
            throws SessionInternalError {
        try {
            ListBL list = new ListBL();
            return list.search(start, end, fieldId, listId, entityId,
                    parameters);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public PagedListDTO getPagedListDTO(Integer listId, String legacyName, 
            Integer entityId, Integer userId) 
            throws SessionInternalError{
        if (listId == null && legacyName == null) {
            throw new SessionInternalError("Can not identify the list");
        }
        
        PagedListDTO retValue = new PagedListDTO();
        try {
            ListBL bl = new ListBL();
            if (listId == null) {
                bl.set(legacyName);
                retValue.setListId(bl.getEntity().getId());
            } else {
                bl.set(listId);
                retValue.setListId(listId);
            }
            // find the searcheable fields
            Vector fields = new Vector();
            // find the key id, it is now the only one ordenable
            for (Iterator it = bl.getEntity().getListFields().iterator();
                    it.hasNext();) {
                ListFieldEntityLocal field = (ListFieldEntityLocal) 
                        it.next();
                if (field.getOrdenable().intValue() == 1) {
                    retValue.setKeyFieldId(field.getId());
                }
                if (field.getSearchable().intValue() == 1) {
                    fields.add(new ListFieldDTO(field.getId(), 
                            field.getTitleKey(), field.getColumnName(), 
                            field.getOrdenable(), field.getSearchable(), 
                            field.getDataType()));
                }
            }
            ListFieldDTO[] arr = new ListFieldDTO[fields.size()];
            retValue.setFields((ListFieldDTO[]) fields.toArray(arr));
            log.debug("There are "  + retValue.getFields().length + " searchable fields");
            // see if there's a count
            ListEntityBL eBl = new ListEntityBL();
            try {
                eBl.set(retValue.getListId(), entityId);
                retValue.setCount(eBl.getEntity().getTotalRecords());
            } catch (FinderException e) {
                // no statistics for this entity
                retValue.setCount(null);
            }
            // get the page size from the user's preferences
            PreferenceBL pref = new PreferenceBL();
            try {
                pref.setForUser(userId, Constants.PREFERENCE_PAGE_SIZE);
            } catch (FinderException e) {
                // use the defaults
            }
            retValue.setPageSize(new Integer(pref.getInt()));
            // the title key
            retValue.setTitleKey(bl.getEntity().getTitleKey());
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
        return retValue;
    }
}
