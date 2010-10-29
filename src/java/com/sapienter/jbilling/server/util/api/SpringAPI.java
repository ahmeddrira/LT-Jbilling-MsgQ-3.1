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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.ItemTypeWS;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.mediation.MediationConfigurationWS;
import com.sapienter.jbilling.server.mediation.MediationProcessWS;
import com.sapienter.jbilling.server.mediation.MediationRecordLineWS;
import com.sapienter.jbilling.server.mediation.MediationRecordWS;
import com.sapienter.jbilling.server.mediation.RecordCountWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.process.BillingProcessConfigurationWS;
import com.sapienter.jbilling.server.process.BillingProcessWS;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.CreateResponseWS;
import com.sapienter.jbilling.server.user.UserTransitionResponseWS;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.user.ValidatePurchaseWS;
import com.sapienter.jbilling.server.user.partner.PartnerWS;
import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.server.util.RemoteContext;

public class SpringAPI implements JbillingAPI {

    private IWebServicesSessionBean session = null;

    public SpringAPI() {
        this(RemoteContext.Name.API_CLIENT);
    }

    public SpringAPI(RemoteContext.Name bean) {
        session = (IWebServicesSessionBean) RemoteContext.getBean(bean);
    }
    
    

    public CreateResponseWS create(UserWS user, OrderWS order)
			throws SessionInternalError {
		return session.create(user, order);
	}

	public Integer[] createInvoice(Integer userId, boolean onlyRecurring)
			throws SessionInternalError {
		return session.createInvoice(userId, onlyRecurring);
	}

	public Integer createInvoiceFromOrder(Integer orderId, Integer invoiceId)
			throws SessionInternalError {
		return session.createInvoiceFromOrder(orderId, invoiceId);
	}

	public Integer createItem(ItemDTOEx item) throws SessionInternalError {
		return session.createItem(item);
	}

	public Integer createItemCategory(ItemTypeWS itemType)
			throws SessionInternalError {
		return session.createItemCategory(itemType);
	}

	public void createMediationConfiguration(MediationConfigurationWS cfg) {
		session.createMediationConfiguration(cfg);
	}

	public Integer createOrder(OrderWS order) throws SessionInternalError {
		return session.createOrder(order);
	}

	public Integer createOrderAndInvoice(OrderWS order)
			throws SessionInternalError {
		return session.createOrderAndInvoice(order);
	}

	public PaymentAuthorizationDTOEx createOrderPreAuthorize(OrderWS order)
			throws SessionInternalError {
		return session.createOrderPreAuthorize(order);
	}

	public Integer createUpdateBillingProcessConfiguration(
			BillingProcessConfigurationWS ws) throws SessionInternalError {
		return session.createUpdateBillingProcessConfiguration(ws);
	}

	public Integer createUpdateOrder(OrderWS order) throws SessionInternalError {
		return session.createUpdateOrder(order);
	}

	public void deleteInvoice(Integer invoiceId) {
		session.deleteInvoice(invoiceId);
	}

	public void deleteItem(Integer itemId) {
		session.deleteItem(itemId);
	}

	public void deleteItemCategory(Integer itemCategoryId) {
		session.deleteItemCategory(itemCategoryId);
	}

	public void deleteMediationConfiguration(Integer cfgId) {
		session.deleteMediationConfiguration(cfgId);
	}

	public void deleteOrder(Integer id) throws SessionInternalError {
		session.deleteOrder(id);
	}

	public void deleteUser(Integer userId) throws SessionInternalError {
		session.deleteUser(userId);
	}

	public void generateRules(String rulesData) throws SessionInternalError {
		session.generateRules(rulesData);
	}

	public Integer[] getAllInvoices(Integer userId) {
		return session.getAllInvoices(userId);
	}

	public ItemTypeWS[] getAllItemCategories() {
		return session.getAllItemCategories();
	}

	public ItemDTOEx[] getAllItems() throws SessionInternalError {
		return session.getAllItems();
	}

	public List<MediationConfigurationWS> getAllMediationConfigurations() {
		return session.getAllMediationConfigurations();
	}

	public List<MediationProcessWS> getAllMediationProcesses() {
		return session.getAllMediationProcesses();
	}

	public Integer getAuthPaymentType(Integer userId)
			throws SessionInternalError {
		return session.getAuthPaymentType(userId);
	}

	public BillingProcessWS getBillingProcess(Integer processId) {
		return session.getBillingProcess(processId);
	}

	public BillingProcessConfigurationWS getBillingProcessConfiguration()
			throws SessionInternalError {
		return session.getBillingProcessConfiguration();
	}

	public List<Integer> getBillingProcessGeneratedInvoices(Integer processId) {
		return session.getBillingProcessGeneratedInvoices(processId);
	}

	public Integer getCallerCompanyId() {
		return session.getCallerCompanyId();
	}

	public Integer getCallerId() {
		return session.getCallerId();
	}

	public Integer getCallerLanguageId() {
		return session.getCallerLanguageId();
	}

	public OrderWS getCurrentOrder(Integer userId, Date date)
			throws SessionInternalError {
		return session.getCurrentOrder(userId, date);
	}

	public Integer[] getInvoicesByDate(String since, String until)
			throws SessionInternalError {
		return session.getInvoicesByDate(since, until);
	}

	public InvoiceWS getInvoiceWS(Integer invoiceId)
			throws SessionInternalError {
		return session.getInvoiceWS(invoiceId);
	}

	public ItemDTOEx getItem(Integer itemId, Integer userId, String pricing) {
		return session.getItem(itemId, userId, pricing);
	}

	public ItemDTOEx[] getItemByCategory(Integer itemTypeId) {
		return session.getItemByCategory(itemTypeId);
	}

	public Integer getLastBillingProcess() throws SessionInternalError {
		return session.getLastBillingProcess();
	}

	public Integer[] getLastInvoices(Integer userId, Integer number)
			throws SessionInternalError {
		return session.getLastInvoices(userId, number);
	}

	public Integer[] getLastInvoicesByItemType(Integer userId,
			Integer itemTypeId, Integer number) throws SessionInternalError {
		return session.getLastInvoicesByItemType(userId, itemTypeId, number);
	}

	public Integer[] getLastOrders(Integer userId, Integer number)
			throws SessionInternalError {
		return session.getLastOrders(userId, number);
	}

	public Integer[] getLastOrdersByItemType(Integer userId,
			Integer itemTypeId, Integer number) throws SessionInternalError {
		return session.getLastOrdersByItemType(userId, itemTypeId, number);
	}

	public Integer[] getLastPayments(Integer userId, Integer number)
			throws SessionInternalError {
		return session.getLastPayments(userId, number);
	}

	public InvoiceWS getLatestInvoice(Integer userId)
			throws SessionInternalError {
		return session.getLatestInvoice(userId);
	}

	public InvoiceWS getLatestInvoiceByItemType(Integer userId,
			Integer itemTypeId) throws SessionInternalError {
		return session.getLatestInvoiceByItemType(userId, itemTypeId);
	}

	public OrderWS getLatestOrder(Integer userId) throws SessionInternalError {
		return session.getLatestOrder(userId);
	}

	public OrderWS getLatestOrderByItemType(Integer userId, Integer itemTypeId)
			throws SessionInternalError {
		return session.getLatestOrderByItemType(userId, itemTypeId);
	}

	public PaymentWS getLatestPayment(Integer userId)
			throws SessionInternalError {
		return session.getLatestPayment(userId);
	}

	public List<MediationRecordLineWS> getMediationEventsForOrder(
			Integer orderId) {
		return session.getMediationEventsForOrder(orderId);
	}

	public List<MediationRecordWS> getMediationRecordsByMediationProcess(
			Integer mediationProcessId) {
		return session
				.getMediationRecordsByMediationProcess(mediationProcessId);
	}

	public List<RecordCountWS> getNumberOfMediationRecordsByStatuses() {
		return session.getNumberOfMediationRecordsByStatuses();
	}

	public OrderWS getOrder(Integer orderId) throws SessionInternalError {
		return session.getOrder(orderId);
	}

	public Integer[] getOrderByPeriod(Integer userId, Integer periodId)
			throws SessionInternalError {
		return session.getOrderByPeriod(userId, periodId);
	}

	public OrderLineWS getOrderLine(Integer orderLineId)
			throws SessionInternalError {
		return session.getOrderLine(orderLineId);
	}

	public byte[] getPaperInvoicePDF(Integer invoiceId)
			throws SessionInternalError {
		return session.getPaperInvoicePDF(invoiceId);
	}

	public PartnerWS getPartner(Integer partnerId) throws SessionInternalError {
		return session.getPartner(partnerId);
	}

	public PaymentWS getPayment(Integer paymentId) throws SessionInternalError {
		return session.getPayment(paymentId);
	}

	public BillingProcessWS getReviewBillingProcess() {
		return session.getReviewBillingProcess();
	}

	public ContactWS[] getUserContactsWS(Integer userId)
			throws SessionInternalError {
		return session.getUserContactsWS(userId);
	}

	public Integer getUserId(String username) throws SessionInternalError {
		return session.getUserId(username);
	}

	public Integer[] getUserInvoicesByDate(Integer userId, String since,
			String until) throws SessionInternalError {
		return session.getUserInvoicesByDate(userId, since, until);
	}

	public Integer[] getUserItemsByCategory(Integer userId, Integer categoryId) {
		return session.getUserItemsByCategory(userId, categoryId);
	}

	public Integer[] getUsersByCreditCard(String number)
			throws SessionInternalError {
		return session.getUsersByCreditCard(number);
	}

	public Integer[] getUsersByCustomField(Integer typeId, String value)
			throws SessionInternalError {
		return session.getUsersByCustomField(typeId, value);
	}

	public Integer[] getUsersByStatus(Integer statusId, boolean in)
			throws SessionInternalError {
		return session.getUsersByStatus(statusId, in);
	}

	public Integer[] getUsersInStatus(Integer statusId)
			throws SessionInternalError {
		return session.getUsersInStatus(statusId);
	}

	public Integer[] getUsersNotInStatus(Integer statusId)
			throws SessionInternalError {
		return session.getUsersNotInStatus(statusId);
	}

	public UserTransitionResponseWS[] getUserTransitions(Date from, Date to)
			throws SessionInternalError {
		return session.getUserTransitions(from, to);
	}

	public UserTransitionResponseWS[] getUserTransitionsAfterId(Integer id)
			throws SessionInternalError {
		return session.getUserTransitionsAfterId(id);
	}

	public UserWS getUserWS(Integer userId) throws SessionInternalError {
		return session.getUserWS(userId);
	}

	public boolean isMediationProcessing() {
		return session.isMediationProcessing();
	}

	public BigDecimal isUserSubscribedTo(Integer userId, Integer itemId) {
		return session.isUserSubscribedTo(userId, itemId);
	}

	public PaymentAuthorizationDTOEx payInvoice(Integer invoiceId)
			throws SessionInternalError {
		return session.payInvoice(invoiceId);
	}

	public void processPartnerPayouts(Date runDate) {
		session.processPartnerPayouts(runDate);
	}

	public PaymentAuthorizationDTOEx processPayment(PaymentWS payment) {
		return session.processPayment(payment);
	}

	public OrderWS rateOrder(OrderWS order) throws SessionInternalError {
		return session.rateOrder(order);
	}

	public OrderWS[] rateOrders(OrderWS[] orders) throws SessionInternalError {
		return session.rateOrders(orders);
	}

	public void setAuthPaymentType(Integer userId, Integer autoPaymentType,
			boolean use) throws SessionInternalError {
		session.setAuthPaymentType(userId, autoPaymentType, use);
	}

	public BillingProcessConfigurationWS setReviewApproval(Boolean flag)
			throws SessionInternalError {
		return session.setReviewApproval(flag);
	}

	public void triggerAgeing(Date runDate) {
		session.triggerAgeing(runDate);
	}

	public void triggerBilling(Date runDate) {
		session.triggerBilling(runDate);
	}

	public void triggerMediation() {
		session.triggerMediation();
	}

	public void triggerProvisioning() {
		session.triggerProvisioning();
	}

	public void updateAch(Integer userId, AchDTO ach)
			throws SessionInternalError {
		session.updateAch(userId, ach);
	}

	public List<Integer> updateAllMediationConfigurations(
			List<MediationConfigurationWS> configurations)
			throws SessionInternalError {
		return session.updateAllMediationConfigurations(configurations);
	}

	public void updateCreditCard(Integer userId, CreditCardDTO creditCard)
			throws SessionInternalError {
		session.updateCreditCard(userId, creditCard);
	}

	public OrderWS updateCurrentOrder(Integer userId, OrderLineWS[] lines,
			String pricing, Date date, String eventDescription)
			throws SessionInternalError {
		return session.updateCurrentOrder(userId, lines, pricing, date,
				eventDescription);
	}

	public void updateItem(ItemDTOEx item) {
		session.updateItem(item);
	}

	public void updateItemCategory(ItemTypeWS itemType)
			throws SessionInternalError {
		session.updateItemCategory(itemType);
	}

	public void updateLineProvisioningStatus(Integer orderLineId,
			Integer provisioningStatus) {
		session.updateLineProvisioningStatus(orderLineId, provisioningStatus);
	}

	public void updateOrder(OrderWS order) throws SessionInternalError {
		session.updateOrder(order);
	}

	public void updateOrderAndLineProvisioningStatus(Integer inOrderId,
			Integer inLineId, String result) {
		session.updateOrderAndLineProvisioningStatus(inOrderId, inLineId,
				result);
	}

	public void updateOrderLine(OrderLineWS line) throws SessionInternalError {
		session.updateOrderLine(line);
	}

	public void updateUser(UserWS user) throws SessionInternalError {
		session.updateUser(user);
	}

	public void updateUserContact(Integer userId, Integer typeId,
			ContactWS contact) throws SessionInternalError {
		session.updateUserContact(userId, typeId, contact);
	}

	public ValidatePurchaseWS validateMultiPurchase(Integer userId,
			Integer[] itemId, String[] fields) {
		return session.validateMultiPurchase(userId, itemId, fields);
	}

	public ValidatePurchaseWS validatePurchase(Integer userId, Integer itemId,
			String fields) {
		return session.validatePurchase(userId, itemId, fields);
	}

	@Override
	public Integer applyPayment(PaymentWS payment, Integer invoiceId) {
		
		return session.applyPayment(payment, invoiceId);
	}

	@Override
	public Integer authenticate(String username, String password) {
		
		return session.authenticate(username, password);
	}

	@Override
	public Integer createUser(UserWS newUser) {
		
		return session.createUser(newUser);
	}

	@Override
	public Integer getAutoPaymentType(Integer userId) {
		return session.getAuthPaymentType(userId);
	}

	@Override
	public ItemDTOEx getItem(Integer itemId, Integer userId,
			PricingField[] fields) {
		return session.getItem(itemId, userId, PricingField.setPricingFieldsValue(fields));
	}

	@Override
	public void setAutoPaymentType(Integer userId, Integer autoPaymentType,
			boolean use) {
		session.setAuthPaymentType(userId, autoPaymentType, use);
		
	}

	@Override
	public OrderWS updateCurrentOrder(Integer userId, OrderLineWS[] lines,
			PricingField[] fields, Date date, String eventDescription) {
		return session.updateCurrentOrder(userId, lines, PricingField.setPricingFieldsValue(fields), date, eventDescription);
	}

	@Override
	public ValidatePurchaseWS validateMultiPurchase(Integer userId,
			Integer[] itemIds, PricingField[][] fields) {
		String[] pricingFields = null;
        if (fields != null) {
            pricingFields = new String[fields.length];
            for (int i = 0; i < pricingFields.length; i++) {
                pricingFields[i] = PricingField.setPricingFieldsValue(
                        fields[i]);
            }
        }
        return session.validateMultiPurchase(userId, itemIds, pricingFields);
	}

	@Override
	public ValidatePurchaseWS validatePurchase(Integer userId, Integer itemId,
			PricingField[] fields) {
		return session.validatePurchase(userId, itemId, PricingField.setPricingFieldsValue(fields));
	}
	
	
}
