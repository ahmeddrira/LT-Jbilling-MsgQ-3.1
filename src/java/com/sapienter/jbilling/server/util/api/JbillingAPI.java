/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.util.api;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.ItemTypeWS;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.mediation.MediationConfigurationWS;
import com.sapienter.jbilling.server.mediation.MediationRecordLineWS;
import com.sapienter.jbilling.server.mediation.MediationRecordWS;
import com.sapienter.jbilling.server.mediation.db.MediationConfiguration;
import com.sapienter.jbilling.server.mediation.db.MediationProcess;
import com.sapienter.jbilling.server.mediation.db.MediationRecordDTO;
import com.sapienter.jbilling.server.mediation.db.MediationRecordLineDTO;
import com.sapienter.jbilling.server.mediation.db.MediationRecordStatusDTO;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.process.BillingProcessDTOEx;
import com.sapienter.jbilling.server.process.db.BillingProcessConfigurationDTO;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.CreateResponseWS;
import com.sapienter.jbilling.server.user.UserTransitionResponseWS;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.user.ValidatePurchaseWS;
import com.sapienter.jbilling.server.user.partner.db.Partner;

import javax.jms.Message;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface JbillingAPI {
    
    /*
        Users
     */

    public UserWS getUserWS(Integer userId) throws JbillingAPIException;
    public Integer createUser(UserWS newUser) throws JbillingAPIException;
    public void updateUser(UserWS user) throws JbillingAPIException;
    public void deleteUser(Integer userId) throws JbillingAPIException;

    public ContactWS[] getUserContactsWS(Integer userId) throws JbillingAPIException;
    public void updateUserContact(Integer userId, Integer typeId, ContactWS contact) throws JbillingAPIException;

    public void updateCreditCard(Integer userId, com.sapienter.jbilling.server.entity.CreditCardDTO creditCard) throws JbillingAPIException;
    public void updateAch(Integer userId, AchDTO ach) throws JbillingAPIException;

    public void setAutoPaymentType(Integer userId, Integer autoPaymentType, boolean use) throws JbillingAPIException;
    public Integer getAutoPaymentType(Integer userId) throws JbillingAPIException;

    public Integer[] getUsersByStatus(Integer statusId, boolean in) throws JbillingAPIException;
    public Integer[] getUsersInStatus(Integer statusId) throws JbillingAPIException;
    public Integer[] getUsersNotInStatus(Integer statusId) throws JbillingAPIException;
    public Integer[] getUsersByCustomField(Integer typeId, String value) throws JbillingAPIException;
    public Integer[] getUsersByCreditCard(String number) throws JbillingAPIException;

    public Integer getUserId(String username) throws JbillingAPIException;

    @Deprecated
    public Integer authenticate(String username, String password) throws JbillingAPIException;

    public void processPartnerPayouts(Date runDate) throws JbillingAPIException;
    public Partner getPartner(Integer partnerId) throws JbillingAPIException;

    public UserTransitionResponseWS[] getUserTransitions(Date from, Date to) throws JbillingAPIException;
    public UserTransitionResponseWS[] getUserTransitionsAfterId(Integer id) throws JbillingAPIException;

    public CreateResponseWS create(UserWS user, OrderWS order) throws JbillingAPIException;


    /*
        Items
     */

    public ItemDTOEx getItem(Integer itemId, Integer userId, PricingField[] fields) throws JbillingAPIException;
    public ItemDTOEx[] getAllItems() throws JbillingAPIException;
    public Integer createItem(ItemDTOEx item) throws JbillingAPIException;
    public void updateItem(ItemDTOEx item) throws JbillingAPIException;
    public void deleteItem(Integer itemId) throws JbillingAPIException;

    public ItemDTOEx[] getItemByCategory(Integer itemTypeId) throws JbillingAPIException;
    public Integer[] getUserItemsByCategory(Integer userId, Integer categoryId) throws JbillingAPIException;

    public ItemTypeWS[] getAllItemCategories() throws JbillingAPIException;
    public Integer createItemCategory(ItemTypeWS itemType) throws JbillingAPIException;
    public void updateItemCategory(ItemTypeWS itemType) throws JbillingAPIException;
    public void deleteItemCategory(Integer itemCategoryId) throws JbillingAPIException;

    public BigDecimal isUserSubscribedTo(Integer userId, Integer itemId) throws JbillingAPIException;

    public InvoiceWS getLatestInvoiceByItemType(Integer userId, Integer itemTypeId) throws JbillingAPIException;
    public Integer[] getLastInvoicesByItemType(Integer userId, Integer itemTypeId, Integer number) throws JbillingAPIException;

    public OrderWS getLatestOrderByItemType(Integer userId, Integer itemTypeId) throws JbillingAPIException;
    public Integer[] getLastOrdersByItemType(Integer userId, Integer itemTypeId, Integer number) throws JbillingAPIException;

    public ValidatePurchaseWS validatePurchase(Integer userId, Integer itemId, PricingField[] fields) throws JbillingAPIException;
    public ValidatePurchaseWS validateMultiPurchase(Integer userId, Integer[] itemIds, PricingField[][] fields) throws JbillingAPIException;


    /*
        Orders
     */

    public OrderWS getOrder(Integer orderId) throws JbillingAPIException;
    public Integer createOrder(OrderWS order) throws JbillingAPIException;
    public void updateOrder(OrderWS order) throws JbillingAPIException;
    public Integer createUpdateOrder(OrderWS order) throws JbillingAPIException;
    public void deleteOrder(Integer id) throws JbillingAPIException;

    public Integer createOrderAndInvoice(OrderWS order) throws JbillingAPIException;

    public OrderWS getCurrentOrder(Integer userId, Date date) throws JbillingAPIException;
    public OrderWS updateCurrentOrder(Integer userId, OrderLineWS[] lines, PricingField[] fields, Date date, String eventDescription) throws JbillingAPIException; 

    public OrderLineWS getOrderLine(Integer orderLineId) throws JbillingAPIException;
    public void updateOrderLine(OrderLineWS line) throws JbillingAPIException;

    public Integer[] getOrderByPeriod(Integer userId, Integer periodId) throws JbillingAPIException;
    public OrderWS getLatestOrder(Integer userId) throws JbillingAPIException;
    public Integer[] getLastOrders(Integer userId, Integer number) throws JbillingAPIException;

    public OrderWS rateOrder(OrderWS order) throws JbillingAPIException;
    public OrderWS[] rateOrders(OrderWS orders[]) throws JbillingAPIException;

    public PaymentAuthorizationDTOEx createOrderPreAuthorize(OrderWS order) throws JbillingAPIException;


    /*
        Invoices
     */

    public InvoiceWS getInvoiceWS(Integer invoiceId) throws JbillingAPIException;

    public Integer[] createInvoice(Integer userId, boolean onlyRecurring) throws JbillingAPIException;
    public Integer createInvoiceFromOrder(Integer orderId, Integer invoiceId) throws JbillingAPIException;
    public void deleteInvoice(Integer invoiceId) throws JbillingAPIException;

    public Integer[] getAllInvoices(Integer userId) throws JbillingAPIException;
    public InvoiceWS getLatestInvoice(Integer userId) throws JbillingAPIException;
    public Integer[] getLastInvoices(Integer userId, Integer number) throws JbillingAPIException;

    public Integer[] getInvoicesByDate(String since, String until) throws JbillingAPIException;
    public Integer[] getUserInvoicesByDate(Integer userId, String since, String until) throws JbillingAPIException;

    public byte[] getPaperInvoicePDF(Integer invoiceId) throws JbillingAPIException;


    /*
        Payments
     */

    public PaymentWS getPayment(Integer paymentId) throws JbillingAPIException;
    public PaymentWS getLatestPayment(Integer userId) throws JbillingAPIException;
    public Integer[] getLastPayments(Integer userId, Integer number) throws JbillingAPIException;

    public PaymentAuthorizationDTOEx payInvoice(Integer invoiceId) throws JbillingAPIException;
    public Integer applyPayment(PaymentWS payment, Integer invoiceId) throws JbillingAPIException;
    public PaymentAuthorizationDTOEx processPayment(PaymentWS payment) throws JbillingAPIException;


    /*
        Billing process
     */

    public void triggerBilling(Date runDate) throws JbillingAPIException;
    public void triggerAgeing(Date runDate) throws JbillingAPIException;

    public BillingProcessConfigurationDTO getBillingProcessConfiguration() throws JbillingAPIException;
    public Integer createUpdateBillingProcessConfiguration(BillingProcessConfigurationDTO dto) throws JbillingAPIException;

    public BillingProcessDTOEx getBillingProcess(Integer processId) throws JbillingAPIException;
    public Integer getLastBillingProcess() throws JbillingAPIException;

    public BillingProcessDTOEx getReviewBillingProcess() throws JbillingAPIException;
    public BillingProcessConfigurationDTO setReviewApproval(Boolean flag) throws JbillingAPIException;

    public Collection getBillingProcessGeneratedInvoices(Integer processId) throws JbillingAPIException;


    /*
        Mediation process
     */

    public void triggerMediation() throws JbillingAPIException;
    public boolean isMediationProcessing() throws JbillingAPIException;

    public List<MediationProcess> getAllMediationProcesses() throws JbillingAPIException;
    public List<MediationRecordLineWS> getMediationEventsForOrder(Integer orderId) throws JbillingAPIException;
    public List<MediationRecordWS> getMediationRecordsByMediationProcess(Integer mediationProcessId) throws JbillingAPIException;
    public Map<Integer, Long> getNumberOfMediationRecordsByStatuses() throws JbillingAPIException;

    public List<MediationConfigurationWS> getAllMediationConfigurations() throws JbillingAPIException;
    public void createMediationConfiguration(MediationConfigurationWS cfg) throws JbillingAPIException;
    public List<Integer> updateAllMediationConfigurations(List<MediationConfigurationWS> configurations) throws JbillingAPIException;
    public void deleteMediationConfiguration(Integer cfgId) throws JbillingAPIException;



    /*
        Provisioning process
     */

    public void triggerProvisioning() throws JbillingAPIException;

    public void updateOrderAndLineProvisioningStatus(Integer inOrderId, Integer inLineId, String result) throws JbillingAPIException;
    public void updateLineProvisioningStatus(Integer orderLineId, Integer provisioningStatus) throws JbillingAPIException;


    /*
        Utilities
     */

    public void generateRules(String rulesData) throws JbillingAPIException;
}
