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
import com.sapienter.jbilling.server.process.db.PeriodUnitDTO
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO
import com.sapienter.jbilling.server.order.db.OrderBillingTypeDTO
import com.sapienter.jbilling.server.util.Constants
import com.sapienter.jbilling.server.user.contact.db.ContactDTO
import com.sapienter.jbilling.server.order.OrderLineWS
import com.sapienter.jbilling.common.SessionInternalError
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import com.sapienter.jbilling.server.item.db.ItemDAS

/**
 * OrderController
 *
 * @author Brian Cowdery
 * @since 20-Jan-2011
 */
@Secured(['isAuthenticated()'])
class OrderBuilderController {

    static pagination = [ max: 25, offset: 0 ]

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
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        // filter on item type, item id and internal number
        def products = ItemDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            and {
                if (params.filterBy && params.filterBy != message(code: 'products.filter.by.default')) {
                    or {
                        eq('id', params.int('filterBy'))
                        ilike('internalNumber', "%${params.filterBy}%")
                    }
                }

                if (params.typeId) {
                    itemTypes {
                        eq('id', params.int('typeId'))
                    }
                }

                eq('deleted', 0)
                eq('entity', company)
            }
            order('id', 'asc')
        }

        // if no results found, try filtering by description
        if (!products && params.filterBy) {
            products = ItemDTO.createCriteria().list(
                    max:    params.max,
                    offset: params.offset
            ) {
                and {
                    eq('deleted', 0)
                    eq('entity', company)
                }
                order('id', 'asc')
            }

            /*
                Collections.#findAll() changes the collection type and looses the queries totalCount needed
                for pagination. Keep it in a separate variable before filtering by description.
             */
            return [
                totalCount: products.totalCount,
                list: products.findAll { it.getDescription().toLowerCase().contains(params.filterBy.toLowerCase()) }
            ]
        }

        return [ list: products, totalCount: products.totalCount ]
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
                    order.period        = Constants.ORDER_PERIOD_ONCE
                    order.billingTypeId = Constants.ORDER_BILLING_POST_PAID
                    order.activeSince   = new Date()
                    order.orderLines    = []
                }

                // add breadcrumb for order editing
                if (params.id) {
                    breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))
                }

                // available order periods
                def company = CompanyDTO.get(session['company_id'])
                def orderPeriods = company.orderPeriods.collect { new OrderPeriodDTO(it.id) } << new OrderPeriodDTO(Constants.ORDER_PERIOD_ONCE)
                orderPeriods.sort { it.id }

                // available order types
                def orderBillingTypes = [
                        new OrderBillingTypeDTO(Constants.ORDER_BILLING_PRE_PAID),
                        new OrderBillingTypeDTO(Constants.ORDER_BILLING_POST_PAID)
                ]

                // model scope for this flow
                flow.company = company;
                flow.orderPeriods = orderPeriods
                flow.orderBillingTypes = orderBillingTypes
                flow.user = user
                flow.contact = contact

                // conversation scope
                conversation.order = order
                conversation.products = getProducts(company, params)
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
                params.template = 'products'
                conversation.products = getProducts(flow.company, params)
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

                // rate order and render the review pane
                params.template = 'review'
                params.newLineIndex = lines.size() - 1
                conversation.order = webServicesSession.rateOrder(order)

                log.debug("New order line added to index: ${params.newLineIndex}")
            }
            on("success").to("build")
        }

        /**
         * Updates an order line  and renders the review panel.
         */
        updateOrderLine {
            action {
                def order = conversation.order

                def index = params.int('index')
                def line = order.orderLines[index]
                bindData(line, params["line-${index}"])
                order.orderLines[index] = line

                params.template = 'review'
                conversation.order = webServicesSession.rateOrder(order)
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

                params.template = 'review'
                conversation.order = webServicesSession.rateOrder(order)
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

                params.template = 'review'
                conversation.order = webServicesSession.rateOrder(order)
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
            on("addLine").to("addOrderLine")
            on("updateLine").to("updateOrderLine")
            on("removeLine").to("removeOrderLine")
            on("update").to("updateOrder")
            on("save").to("saveOrder")
            // on("save").to("beforeSave")
            on("cancel").to("finish")
        }

        // example action that can display an extra page BEFORE the order is saved.
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
                        webServicesSession.createOrder(order)
                    } else {
                        // todo: rate order clears the order and order line ID's. Fix rate order before we can update!
                        webServicesSession.updateOrder(order)
                    }

                } catch (SessionInternalError e) {
                    viewUtils.resolveException(flash, session.locale, e);
                    error()
                }
            }
            on("error").to("build")
            on("success").to("finish")
        }

        finish {
            redirect controller: 'order', action: 'list', id: conversation?.order?.id
        }
    }

}
