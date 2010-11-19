package jbilling

import com.sapienter.jbilling.server.entity.AchDTO
import com.sapienter.jbilling.server.entity.CreditCardDTO
import com.sapienter.jbilling.server.invoice.InvoiceWS
import com.sapienter.jbilling.server.item.ItemDTOEx
import com.sapienter.jbilling.server.item.ItemTypeWS
import com.sapienter.jbilling.server.mediation.MediationConfigurationWS
import com.sapienter.jbilling.server.mediation.MediationProcessWS
import com.sapienter.jbilling.server.mediation.MediationRecordLineWS
import com.sapienter.jbilling.server.mediation.MediationRecordWS
import com.sapienter.jbilling.server.order.OrderLineWS
import com.sapienter.jbilling.server.order.OrderWS
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx
import com.sapienter.jbilling.server.payment.PaymentWS
import com.sapienter.jbilling.server.process.BillingProcessConfigurationWS
import com.sapienter.jbilling.server.process.BillingProcessWS
import com.sapienter.jbilling.server.user.ContactWS
import com.sapienter.jbilling.server.user.CreateResponseWS
import com.sapienter.jbilling.server.user.UserTransitionResponseWS
import com.sapienter.jbilling.server.user.UserWS
import com.sapienter.jbilling.server.user.ValidatePurchaseWS
import com.sapienter.jbilling.server.user.partner.PartnerWS
import com.sapienter.jbilling.server.util.IWebServicesSessionBean
import com.sapienter.jbilling.server.util.WebServicesSessionSpringBean;
import com.sapienter.jbilling.server.mediation.RecordCountWS
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.util.PreferenceWS
import com.sapienter.jbilling.server.order.OrderProcessWS;


/**
 * Grails managed remote service bean for exported web-services. This bean delegates to
 * the WebServicesSessionBean just like the core JbillingAPI.
 */
class ApiService implements IWebServicesSessionBean {

    def IWebServicesSessionBean webServicesSession

    static transactional = true

    static expose = ['hessian', 'cxfjax', 'httpinvoker']

    public Integer getCallerId() {
        return webServicesSession.getCallerId();
    }

    public Integer getCallerCompanyId() {
        return webServicesSession.getCallerCompanyId();
    }

    public Integer getCallerLanguageId() {
        return webServicesSession.getCallerLanguageId();
    }

    public InvoiceWS getInvoiceWS(Integer invoiceId) {
        return webServicesSession.getInvoiceWS(invoiceId)
    }

    public InvoiceWS getReviewInvoiceWS(Integer invoiceId) {
        return webServicesSession.getReviewInvoiceWS(invoiceId);
    }

    public Integer[] getAllInvoices(Integer userId) {
        return webServicesSession.getAllInvoices(userId)
    }

    public InvoiceWS getLatestInvoice(Integer userId) {
        return webServicesSession.getLatestInvoice(userId)
    }

    public Integer[] getLastInvoices(Integer userId, Integer number) {
        return webServicesSession.getLastInvoices(userId, number)
    }

    public Integer[] getInvoicesByDate(String since, String until) {
        return webServicesSession.getInvoicesByDate(since, until)
    }

    public Integer[] getUserInvoicesByDate(Integer userId, String since, String until) {
        return webServicesSession.getUserInvoicesByDate(userId, since, until)
    }

    public byte[] getPaperInvoicePDF(Integer invoiceId) {
        return webServicesSession.getPaperInvoicePDF(invoiceId)
    }

    public void deleteInvoice(Integer invoiceId) {
        webServicesSession.deleteInvoice(invoiceId)
    }

    public Integer[] createInvoice(Integer userId, boolean onlyRecurring) {
        return webServicesSession.createInvoice(userId, onlyRecurring)
    }

    public Integer createInvoiceFromOrder(Integer orderId, Integer invoiceId) {
        return webServicesSession.createInvoiceFromOrder(orderId, invoiceId)
    }

    public Integer createUser(UserWS newUser) {
        return webServicesSession.createUser(newUser)
    }

    public void deleteUser(Integer userId) {
        webServicesSession.deleteUser(userId)
    }

    public void updateUserContact(Integer userId, Integer typeId, ContactWS contact) {
        webServicesSession.updateUserContact(userId, typeId, contact)
    }

    public void updateUser(UserWS user) {
        webServicesSession.updateUser(user)
    }

    public UserWS getUserWS(Integer userId) {
        return webServicesSession.getUserWS(userId)
    }

    public ContactWS[] getUserContactsWS(Integer userId) {
        return webServicesSession.getUserContactsWS(userId)
    }

    public Integer getUserId(String username) {
        return webServicesSession.getUserId(username)
    }

    public Integer[] getUsersInStatus(Integer statusId) {
        return webServicesSession.getUsersInStatus(statusId)
    }

    public Integer[] getUsersNotInStatus(Integer statusId) {
        return webServicesSession.getUsersNotInStatus(statusId)
    }

    public Integer[] getUsersByCustomField(Integer typeId, String value) {
        return webServicesSession.getUsersByCustomField(typeId, value)
    }

    public Integer[] getUsersByCreditCard(String number) {
        return webServicesSession.getUsersByCreditCard(number)
    }

    public Integer[] getUsersByStatus(Integer statusId, boolean inStatus) {
        return webServicesSession.getUsersByStatus(statusId, inStatus)
    }

    public CreateResponseWS create(UserWS user, OrderWS order) {
        return webServicesSession.create(user, order)
    }

    public Integer authenticate(String username, String password) {
        return webServicesSession.authenticate(username, password)
    }

    public void processPartnerPayouts(Date runDate) {
        webServicesSession.processPartnerPayouts(runDate)
    }

    public PartnerWS getPartner(Integer partnerId) {
        return webServicesSession.getPartner(partnerId)
    }

    public void updateCreditCard(Integer userId, CreditCardDTO creditCard) {
        webServicesSession.updateCreditCard(userId, creditCard)
    }

    public PaymentAuthorizationDTOEx createOrderPreAuthorize(OrderWS order) {
        return webServicesSession.createOrderPreAuthorize(order)
    }

    public Integer createOrder(OrderWS order) {
        return webServicesSession.createOrder(order)
    }

    public OrderWS rateOrder(OrderWS order) {
        return webServicesSession.rateOrder(order)
    }

    public OrderWS[] rateOrders(OrderWS[] orders) {
        return webServicesSession.rateOrders(orders)
    }

    public void updateItem(ItemDTOEx item) {
        webServicesSession.updateItem(item)
    }

    void deleteItem(Integer itemId) {
        webServicesSession.deleteItem(itemId)
    }

    public Integer createOrderAndInvoice(OrderWS order) {
        return webServicesSession.createOrderAndInvoice(order)
    }

    public void updateOrder(OrderWS order) {
        webServicesSession.updateOrder(order)
    }

    public Integer createUpdateOrder(OrderWS order) {
        return webServicesSession.createUpdateOrder(order);
    }

    public OrderWS getOrder(Integer orderId) {
        return webServicesSession.getOrder(orderId)
    }

    public Integer[] getOrderByPeriod(Integer userId, Integer periodId) {
        return webServicesSession.getOrderByPeriod(userId, periodId)
    }

    public OrderLineWS getOrderLine(Integer orderLineId) {
        return webServicesSession.getOrderLine(orderLineId)
    }

    public void updateOrderLine(OrderLineWS line) {
        webServicesSession.updateOrderLine(line)
    }

    public OrderWS getLatestOrder(Integer userId) {
        return webServicesSession.getLatestOrder(userId)
    }

    public Integer[] getLastOrders(Integer userId, Integer number) {
        return webServicesSession.getLastOrders(userId, number)
    }

    public void deleteOrder(Integer id) {
        webServicesSession.deleteOrder(id)
    }

    public OrderWS getCurrentOrder(Integer userId, Date date) {
        return webServicesSession.getCurrentOrder(userId, date)
    }

    public OrderWS updateCurrentOrder(Integer userId, OrderLineWS[] lines, String pricing, Date date, String eventDescription) {
        return webServicesSession.updateCurrentOrder(userId, lines, pricing, date, eventDescription)
    }

	public OrderWS[] getUserOrders(Integer userId) {
		return webServicesSession.getUserOrders(userId);
	}
	
    public PaymentAuthorizationDTOEx payInvoice(Integer invoiceId) {
        return webServicesSession.payInvoice(invoiceId)
    }

    public Integer applyPayment(PaymentWS payment, Integer invoiceId) {
        return webServicesSession.applyPayment(payment, invoiceId)
    }

    public PaymentWS getPayment(Integer paymentId) {
        return webServicesSession.getPayment(paymentId)
    }

    public PaymentWS getLatestPayment(Integer userId) {
        return webServicesSession.getLatestPayment(userId)
    }

    public Integer[] getLastPayments(Integer userId, Integer number) {
        return webServicesSession.getLastPayments(userId, number)
    }

    public PaymentAuthorizationDTOEx processPayment(PaymentWS payment) {
        return webServicesSession.processPayment(payment)
    }

    public ValidatePurchaseWS validatePurchase(Integer userId, Integer itemId, String fields) {
        return webServicesSession.validatePurchase(userId, itemId, fields)
    }

    public ValidatePurchaseWS validateMultiPurchase(Integer userId, Integer[] itemId, String[] fields) {
        return webServicesSession.validateMultiPurchase(userId, itemId, fields)
    }

    public Integer createItem(ItemDTOEx item) {
        return webServicesSession.createItem(item)
    }

    public ItemDTOEx[] getAllItems() {
        return webServicesSession.getAllItems()
    }

    public UserTransitionResponseWS[] getUserTransitions(Date from, Date to) {
        return webServicesSession.getUserTransitions(from, to)
    }

    public UserTransitionResponseWS[] getUserTransitionsAfterId(Integer id) {
        return webServicesSession.getUserTransitionsAfterId(id)
    }

    public ItemDTOEx getItem(Integer itemId, Integer userId, String pricing) {
        return webServicesSession.getItem(itemId, userId, pricing)
    }

    public InvoiceWS getLatestInvoiceByItemType(Integer userId, Integer itemTypeId) {
        return webServicesSession.getLatestInvoiceByItemType(userId, itemTypeId)
    }

    public Integer[] getLastInvoicesByItemType(Integer userId, Integer itemTypeId, Integer number) {
        return webServicesSession.getLastInvoicesByItemType(userId, itemTypeId, number)
    }

    public OrderWS getLatestOrderByItemType(Integer userId, Integer itemTypeId) {
        return webServicesSession.getLatestOrderByItemType(userId, itemTypeId)
    }

    public Integer[] getLastOrdersByItemType(Integer userId, Integer itemTypeId, Integer number) {
        return webServicesSession.getLastOrdersByItemType(userId, itemTypeId, number)
    }

    public String isUserSubscribedTo(Integer userId, Integer itemId) {
        return webServicesSession.isUserSubscribedTo(userId, itemId)
    }

    public Integer[] getUserItemsByCategory(Integer userId, Integer categoryId) {
        return webServicesSession.getUserItemsByCategory(userId, categoryId)
    }

    public ItemDTOEx[] getItemByCategory(Integer itemTypeId) {
        return webServicesSession.getItemByCategory(itemTypeId)
    }

    public ItemTypeWS[] getAllItemCategories() {
        return webServicesSession.getAllItemCategories()
    }

    public Integer createItemCategory(ItemTypeWS itemType) {
        return webServicesSession.createItemCategory(itemType)
    }

    public void updateItemCategory(ItemTypeWS itemType) {
        webServicesSession.updateItemCategory(itemType)
    }

    void deleteItemCategory(Integer itemCategoryId) {
        webServicesSession.deleteItemCategory(itemCategoryId)
    }

    public void updateAch(Integer userId, AchDTO ach) {
        webServicesSession.updateAch(userId, ach)
    }

    public void setAuthPaymentType(Integer userId, Integer autoPaymentType, boolean use) {
        webServicesSession.setAuthPaymentType(userId, autoPaymentType, use)
    }

    public Integer getAuthPaymentType(Integer userId) {
        return webServicesSession.getAuthPaymentType(userId)
    }

    public void generateRules(String rulesData) {
        webServicesSession.generateRules(rulesData)
    }

    
    /*
        Billing process
     */

    public boolean triggerBilling(Date runDate) {
        return webServicesSession.triggerBilling(runDate)
    }

    public void triggerAgeing(Date runDate) {
        webServicesSession.triggerAgeing(runDate)
    }

    public BillingProcessConfigurationWS getBillingProcessConfiguration() {
        return webServicesSession.getBillingProcessConfiguration()
    }

    public Integer createUpdateBillingProcessConfiguration(BillingProcessConfigurationWS ws) {
        return webServicesSession.createUpdateBillingProcessConfiguration(ws)
    }

    public BillingProcessWS getBillingProcess(Integer processId) {
        return webServicesSession.getBillingProcess(processId)
    }

    public Integer getLastBillingProcess() {
        return webServicesSession.getLastBillingProcess()
    }

    public List<OrderProcessWS> getOrderProcesses(Integer orderId) {
        return webServicesSession.getOrderProcesses(orderId);
    }

    public List<OrderProcessWS> getOrderProcessesByInvoice(Integer invoiceId) {
        return webServicesSession.getOrderProcessesByInvoice(invoiceId);
    }

    public BillingProcessWS getReviewBillingProcess() {
        return webServicesSession.getReviewBillingProcess()
    }

    public BillingProcessConfigurationWS setReviewApproval(Boolean flag) {
        return webServicesSession.setReviewApproval(flag)
    }

    public List<Integer> getBillingProcessGeneratedInvoices(Integer processId) {
        return webServicesSession.getBillingProcessGeneratedInvoices(processId)
    }


    /*
       Mediation process
    */

    void triggerMediation() {
        webServicesSession.triggerMediation()
    }

    boolean isMediationProcessing() {
        return webServicesSession.isMediationProcessing()
    }

    public List<MediationProcessWS> getAllMediationProcesses() {
        return webServicesSession.getAllMediationProcesses()
    }

    public List<MediationRecordLineWS> getMediationEventsForOrder(Integer orderId) {
        return webServicesSession.getMediationEventsForOrder(orderId)
    }

    public List<MediationRecordWS> getMediationRecordsByMediationProcess(Integer mediationProcessId) {
        return webServicesSession.getMediationRecordsByMediationProcess(mediationProcessId)
    }

    public List<RecordCountWS> getNumberOfMediationRecordsByStatuses() {
        return webServicesSession.getNumberOfMediationRecordsByStatuses()
    }

    public List<MediationConfigurationWS> getAllMediationConfigurations() {
        return webServicesSession.getAllMediationConfigurations()
    }

    public void createMediationConfiguration(MediationConfigurationWS cfg) {
        webServicesSession.createMediationConfiguration(cfg)
    }

    public List<Integer> updateAllMediationConfigurations(List<MediationConfigurationWS> configurations) {
        return webServicesSession.updateAllMediationConfigurations(configurations)
    }

    public void deleteMediationConfiguration(Integer cfgId) {
        webServicesSession.deleteMediationConfiguration(cfgId)
    }


    /*
       Provisioning process
    */

    public void triggerProvisioning() {
        webServicesSession.triggerProvisioning()
    }

    public void updateOrderAndLineProvisioningStatus(Integer inOrderId, Integer inLineId, String result) {
        webServicesSession.updateOrderAndLineProvisioningStatus(inOrderId, inLineId, result)
    }

    public void updateLineProvisioningStatus(Integer orderLineId, Integer provisioningStatus) {
        webServicesSession.updateLineProvisioningStatus(orderLineId, provisioningStatus)
    }
    
    /*
        Notification
    */
    public void createUpdateNofications(Integer entityId, Integer messageId, MessageDTO dto) {
       webServicesSession.createUpdateNofications(entityId, messageId, dto)
    }
    
    public void saveNotificationPreferences(PreferenceWS[] prefList) {
        webServicesSession.saveNotificationPreferences(prefList)
    }
	
	public void saveCustomerNotes(Integer userId, String notes) {
		webServicesSession.saveCustomerNotes(userId, notes)
	}
    
}
