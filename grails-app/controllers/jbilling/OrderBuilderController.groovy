/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package jbilling

import grails.plugins.springsecurity.Secured
import com.sapienter.jbilling.server.item.db.ItemDTO
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.order.OrderWS
import com.sapienter.jbilling.server.user.db.UserDTO

import com.sapienter.jbilling.server.order.db.OrderPeriodDTO
import com.sapienter.jbilling.server.order.db.OrderBillingTypeDTO
import com.sapienter.jbilling.server.util.Constants
import com.sapienter.jbilling.server.user.contact.db.ContactDTO
import com.sapienter.jbilling.server.order.OrderLineWS
import com.sapienter.jbilling.common.SessionInternalError
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

import com.sapienter.jbilling.server.order.db.OrderStatusDTO
import java.math.RoundingMode

/**
 * OrderController
 *
 * @author Brian Cowdery
 * @since 20-Jan-2011
 */
@Secured(['isAuthenticated()'])
class OrderBuilderController {

    def webServicesSession
    def messageSource
    def viewUtils

    def breadcrumbService

    def index = {
        redirect action: 'edit'
    }

    /**
     * Get a filtered list of products
     *
     * @param company company
     * @param params parameter map containing filter criteria
     * @return filtered list of products
     */
    def getProducts(CompanyDTO company, GrailsParameterMap params) {
        def filterBy = params['product.filterBy']
        def typeId = params.int('product.typeId')

        // filter on item type, item id and internal number
        def products = ItemDTO.createCriteria().list() {
            and {
                if (filterBy && filterBy != message(code: 'products.filter.by.default')) {
                    or {
                        eq('id', params.int('filterBy'))
                        ilike('internalNumber', "%${filterBy}%")
                    }
                }

                if (typeId) {
                    itemTypes {
                        eq('id', typeId)
                    }
                }

                isEmpty('plans')
                eq('deleted', 0)
                eq('entity', company)
            }
            order('id', 'asc')
        }

        // if no results found, try filtering by description
        if (!products && filterBy) {
            products = ItemDTO.createCriteria().list() {
                and {
                    isEmpty('plans')
                    eq('deleted', 0)
                    eq('entity', company)
                }
                order('id', 'asc')
            }.findAll {
                it.getDescription(session['language_id']).toLowerCase().contains(filterBy.toLowerCase())
            }
        }

        return products
    }

    /**
     * Get a filtered list of plans
     *
     * @param company company
     * @param params parameter map containing filter criteria
     * @return filtered list of products
     */
    def getPlans(CompanyDTO company, GrailsParameterMap params) {
        def filterBy = params['plan.filterBy']
        def typeId = params.int('plan.typeId')

        // filter on item type, item id and internal number
        def plans = ItemDTO.createCriteria().list() {
            and {
                if (filterBy && filterBy != message(code: 'products.filter.by.default')) {
                    or {
                        eq('id', params.int('filterBy'))
                        ilike('internalNumber', "%${filterBy}%")
                    }
                }

                if (typeId) {
                    itemTypes {
                        eq('id', typeId)
                    }
                }

                isNotEmpty('plans')
                eq('deleted', 0)
                eq('entity', company)
            }
            order('id', 'asc')
        }

        log.debug("Found: ${plans.size()} plans")

        // if no results found, try filtering by description
        if (!plans && filterBy) {
            plans = ItemDTO.createCriteria().list() {
                and {
                    isNotEmpty('plans')
                    eq('deleted', 0)
                    eq('entity', company)
                }
                order('id', 'asc')
            }.findAll {
                it.getDescription(session['language_id']).toLowerCase().contains(filterBy.toLowerCase())
            }
        }

        return plans
    }

    def editFlow = {
        /**
         * Initializes the order builder, putting necessary data into the flow and conversation
         * contexts so that it can be referenced later.
         */
        initialize {
            action {
                def order = params.id ? webServicesSession.getOrder(params.int('id')) : new OrderWS()
                def user = UserDTO.get(order?.userId ?: params.int('userId'))
                def contact = ContactDTO.findByUserId(order?.userId ?: user.id)

                // set sensible defaults for new orders
                if (!order.id || order.id == 0) {
                    order.userId        = user.id
                    order.currencyId    = user.currency.id
                    order.statusId      = Constants.ORDER_STATUS_ACTIVE
                    order.period        = Constants.ORDER_PERIOD_ONCE
                    order.billingTypeId = Constants.ORDER_BILLING_POST_PAID
                    order.activeSince   = new Date()
                    order.orderLines    = []
                }

                // add breadcrumb for order editing
                if (params.id) {
                    breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))
                }

                // available order periods, statuses and order types
                def company = CompanyDTO.get(session['company_id'])
                def itemTypes = company.itemTypes.sort{ it.id }
                def orderStatuses = OrderStatusDTO.list()
                def orderPeriods = company.orderPeriods.collect { new OrderPeriodDTO(it.id) } << new OrderPeriodDTO(Constants.ORDER_PERIOD_ONCE)
                orderPeriods.sort { it.id }

                def orderBillingTypes = [
                        new OrderBillingTypeDTO(Constants.ORDER_BILLING_PRE_PAID),
                        new OrderBillingTypeDTO(Constants.ORDER_BILLING_POST_PAID)
                ]

                // model scope for this flow
                flow.company = company
                flow.itemTypes = itemTypes
                flow.orderStatuses = orderStatuses
                flow.orderPeriods = orderPeriods
                flow.orderBillingTypes = orderBillingTypes
                flow.user = user
                flow.contact = contact

                // conversation scope
                conversation.order = order
                conversation.products = getProducts(company, params)
                conversation.plans = getPlans(company, params)
            }
            on("success").to("build")
        }

        /**
         * Renders the order details tab panel.
         */
        showDetails {
            action {
                params.template = 'details'
            }
            on("success").to("build")
        }

        /**
         * Renders the product list tab panel, filtering the product list by the given criteria.
         */
        showProducts {
            action {
                // filter using the first item type by default
                if (params['product.typeId'] == null)
                    params['product.typeId'] = flow.itemTypes?.asList()?.first()?.id

                params.template = 'products'
                conversation.products = getProducts(flow.company, params)
            }
            on("success").to("build")
        }

        /**
         * Renders the plans list tab panel, filtering the plans list by the given criteria.
         */
        showPlans {
            action {
                params.template = 'plans'
                conversation.plans = getPlans(flow.company, params)
            }
            on("success").to("build")
        }


        /**
         * Adds a product to the order as a new order line and renders the review panel.
         */
        addOrderLine {
            action {
                // build line
                def line = new OrderLineWS()
                line.typeId = Constants.ORDER_LINE_TYPE_ITEM
                line.quantity = BigDecimal.ONE
                line.itemId = params.int('id')
                line.useItem = true

                // add line to order
                def order = conversation.order
                def lines = order.orderLines as List
                lines.add(line)
                order.orderLines = lines.toArray()

                // rate order
                if (order.orderLines) {
                    try {
                        order = webServicesSession.rateOrder(order)
                    } catch (SessionInternalError e) {
                        viewUtils.resolveException(flow, session.locale, e)
                    }
                }
                conversation.order = order

                params.newLineIndex = lines.size() - 1
                params.template = 'review'
            }
            on("success").to("build")
        }

        /**
         * Updates an order line  and renders the review panel.
         */
        updateOrderLine {
            action {
                def order = conversation.order

                // update line
                def index = params.int('index')
                def line = order.orderLines[index]
                bindData(line, params["line-${index}"])

                // must have a quantity
                if (!line.quantity) {
                    line.quantity = BigDecimal.ONE
                }

                // if product does not support decimals, drop scale of the given quantity
                def product = conversation.products?.find{ it.id == line.itemId }
                if (product.hasDecimals == 0) {
                    line.quantity = line.getQuantityAsDecimal().setScale(0, RoundingMode.HALF_UP)
                }

                // line may have a price if "allow manual pricing" is set on the item
                // if there is no price, use the item
                if (!params["line-${index}.price"] || !line.price) {
                    line.useItem = true
                }

                log.debug("Updated line: " + line)

                // add line to order
                order.orderLines[index] = line

                // rate order
                if (order.orderLines) {
                    try {
                        order = webServicesSession.rateOrder(order)
                    } catch (SessionInternalError e) {
                        viewUtils.resolveException(flow, session.locale, e)
                    }
                }
                conversation.order = order

                params.template = 'review'
            }
            on("success").to("build")
        }

        /**
         * Removes a line from the order and renders the review panel.
         */
        removeOrderLine {
            action {
                def order = conversation.order

                def index = params.int('index')
                def lines = order.orderLines as List
                lines.remove(index)
                order.orderLines = lines.toArray()

                // rate order
                if (order.orderLines) {
                    try {
                        order = webServicesSession.rateOrder(order)
                    } catch (SessionInternalError e) {
                        viewUtils.resolveException(flow, session.locale, e)
                    }
                }
                conversation.order = order

                params.template = 'review'
            }
            on("success").to("build")
        }

        /**
         * Updates order attributes (period, billing type, active dates etc.) and
         * renders the order review panel.
         */
        updateOrder {
            action {
                def order = conversation.order
                bindData(order, params)

                // one time orders are ALWAYS post-paid
                if (order.period == Constants.ORDER_PERIOD_ONCE)
                    order.billingTypeId = Constants.ORDER_BILLING_POST_PAID

                // rate order
                if (order.orderLines) {
                    try {
                        order = webServicesSession.rateOrder(order)
                    } catch (SessionInternalError e) {
                        viewUtils.resolveException(flow, session.locale, e)
                    }
                }
                conversation.order = order

                params.template = 'review'
            }
            on("success").to("build")
        }

        /**
         * Shows the order builder. This is the "waiting" state that branches out to the rest
         * of the flow. All AJAX actions and other states that build on the order should
         * return here when complete.
         *
         * If the parameter 'template' is set, then a partial view template will be rendered instead
         * of the complete 'build.gsp' page view (workaround for the lack of AJAX support in web-flow).
         */
        build {
            on("details").to("showDetails")
            on("products").to("showProducts")
            on("plans").to("showPlans")
            on("addLine").to("addOrderLine")
            on("updateLine").to("updateOrderLine")
            on("removeLine").to("removeOrderLine")
            on("update").to("updateOrder")
            on("save").to("saveOrder")
            // on("save").to("beforeSave")
            on("cancel").to("finish")
        }

        // example action that displays a page BEFORE the order is saved.
        /*
        beforeSave {
            on("save").to("saveOrder")
            on("cancel").to("finish")
        }
        */

        /**
         * Saves the order and exits the builder flow.
         */
        saveOrder {
            action {
                try {
                    def order = conversation.order

                    if (!order.id || order.id == 0) {
                        log.debug("creating order ${order}")
                        order.id = webServicesSession.createOrder(order)

                    } else {
                        log.debug("saving changes to order ${order.id}")
                        webServicesSession.updateOrder(order)
                    }

                } catch (SessionInternalError e) {
                    viewUtils.resolveException(flow, session.locale, e)
                    error()
                }
            }
            on("error").to("build")
            on("success").to("finish")
        }

        finish {
            redirect controller: 'order', action: 'list', id: conversation.order?.id
        }
    }

}
