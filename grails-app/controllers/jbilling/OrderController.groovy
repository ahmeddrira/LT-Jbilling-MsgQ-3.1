package jbilling

import grails.plugins.springsecurity.Secured;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.user.UserWS;

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
		recentItemService.addRecentItem(_orderId, RecentItemType.ORDER)
		
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
	
}
