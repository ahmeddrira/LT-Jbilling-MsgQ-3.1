package jbilling

import grails.plugins.springsecurity.Secured;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.customer.CustomerBL;
import com.sapienter.jbilling.server.user.db.CustomerDTO;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.item.CurrencyBL;


/**
 * 
 * @author vikas bodani
 * @since  20-Jan-2011
 *
 */

@Secured(['isAuthenticated()'])
class OrderController {

	static pagination = [ max: 25, offset: 0 ]

    def webServicesSession
    def viewUtils
    def filterService
    def recentItemService
	def breadcrumbService

    def index = {
        redirect action: list, params: params
    }

	def list = {

		if (params.id) {
			redirect (action: 'showListAndOrder', params: [id: params.id as Integer])
		}
		
		def filters = filterService.getFilters(FilterType.ORDER, params)
		
		def orders = getFilteredOrders (filters)
		
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
		log.debug "Found ${orders?.size()} orders"
		
		if (params.applyFilter) {
			render template: 'orders', model: [orders:orders, filters:filters]
		} else {
			[orders:orders, filters:filters]
		}

	}
	
	def show = {
		
		Integer _orderId= params.id as Integer
		OrderWS order= webServicesSession.getOrder(_orderId)
		UserWS user= webServicesSession.getUserWS(order.getUserId())
		
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, _orderId)
		recentItemService.addRecentItem(_orderId, RecentItemType.ORDER)
		
		log.debug "Invoices Generated: ${order.getGeneratedInvoices()}"
		render template:'order', model: [order: order, user:user]
	}
	
	def showListAndOrder = {
		
		def filters = filterService.getFilters(FilterType.ORDER, params)
		def orders = getFilteredOrders (filters)
		Integer _orderId= params.id as Integer
		OrderWS order= webServicesSession.getOrder(_orderId)
		recentItemService.addRecentItem(_orderId, RecentItemType.ORDER)
		render view: 'showListAndOrder', model:[orders:orders, order:order, filters:filters]
	}
	
	def getFilteredOrders(filters) {
		params.max = params?.max?.toInteger() ?: pagination.max
		params.offset = params?.offset?.toInteger() ?: pagination.offset
		
		return OrderDTO.createCriteria().list(
			max:    params.max,
			offset: params.offset
		) {
			and {
				filters.each { filter ->
					if (filter.value) {
						switch (filter.constraintType) {
							case FilterConstraint.EQ:
								eq(filter.field, filter.value)
								break

							case FilterConstraint.DATE_BETWEEN:
								between(filter.field, filter.startDateValue, filter.endDateValue)
								break
						}
					}
				}
				eq('deleted', 0)
			}
			order("id", "desc")
		}
	}
	
	def generateInvoice = {
		log.debug 'generateInvoice for order ${params.id}'
		
		OrderDTO order= new OrderDAS().find(params.int('id'))
		if (!order.getStatusId().equals(
			Constants.ORDER_STATUS_ACTIVE)) {
			flash.error ='order.error.geninvoice.inactive'
			redirect (action: 'showListAndOrder', params: [id: params.id as Integer])
		}
		
		Integer invoiceID= webServicesSession.createInvoiceFromOrder(order.getId(), null)
		
		flash.message ='order.geninvoice.success'
		flash.args = [order.getId()]
		redirect controller: 'invoice', action: 'showListAndInvoice', params: [id: invoiceID]
	}

	def applyToInvoice = {

		def invoices= getApplicableInvoices(params.int('userId'))
		log.debug "ApplyToInvoice: Found ${invoices?.size()} invoices for User ${params.userId}"

		if (!invoices || invoices.size() == 0) {
			flash.error = 'order.error.unpaid.invoices.not.found'
			flash.args = [params.userId]
			redirect (action: 'showListAndOrder', params: [id: params.id as Integer]) 
		}
		session.applyToInvoiceOrderId= params.int('id')
		[invoices:invoices, currencies: currencies, orderId: params.id]
	}

	def apply = {
		log.debug 'apply: for order ${params.id}'
		
		OrderDTO order= new OrderDAS().find(params.int('id'))
		if (!order.getStatusId().equals(
			Constants.ORDER_STATUS_ACTIVE)) {
			flash.error ='order.error.apply.invoice'
			redirect (action: 'showListAndOrder', params: [id: params.id as Integer])
		}
		
		Integer invoiceID= webServicesSession.createInvoiceFromOrder(order.getId(), params.int('invoiceId'))
		flash.message = 'order.succcessfully.applied.to.invoice'
		flash.args = [params.id, invoiceId]
		redirect (action: 'list', params: [id: params.id])
	}
	
	def getApplicableInvoices(Integer userId) {
		
		CustomerDTO payingUser
		List<InvoiceWS> invoices
		
		UserDTO user= new UserDAS().find(userId)
		if (user.getCustomer()?.getParent()) {
			payingUser= new CustomerBL(user.getCustomer().getId()).getInvoicableParent()
			invoices= webServicesSession.getAllInvoicesForUser(payingUser.getBaseUser().getId())
		} else {
			invoices= webServicesSession.getAllInvoicesForUser(user.getId())
		}
		
		return invoices;
	}
	
	def getCurrencies() {
		def currencies = new CurrencyBL().getCurrencies(session['language_id'].toInteger(), session['company_id'].toInteger())
		return currencies.findAll{ it.inUse }
	}
}
