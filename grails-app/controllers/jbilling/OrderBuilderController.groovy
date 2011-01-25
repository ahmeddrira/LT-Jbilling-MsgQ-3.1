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
    def breadcrumbService

    def index = {
        redirect action: 'edit'
    }

    def editFlow = {
        /**
         * Initializes the order builder, putting necessary data into the flow context
         * so that it can be referenced later.
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

                // available order periods
                def company = CompanyDTO.get(session['company_id'])
                def orderPeriods = company.orderPeriods
                orderPeriods.add(new OrderPeriodDTO(Constants.ORDER_PERIOD_ONCE))
                orderPeriods.sort{ it.id }

                // available order types
                def orderBillingTypes = [
                        new OrderBillingTypeDTO(Constants.ORDER_BILLING_PRE_PAID),
                        new OrderBillingTypeDTO(Constants.ORDER_BILLING_POST_PAID)
                ]

                // objects added to the 'flow' scope are available for duration of the builder conversation
                [ company: company, orderPeriods: orderPeriods, orderBillingTypes: orderBillingTypes, user: user, contact: contact, order: order ]
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
                params.max = params?.max?.toInteger() ?: pagination.max
                params.offset = params?.offset?.toInteger() ?: pagination.offset

                def products = ItemDTO.createCriteria().list(
                        max:    params.max,
                        offset: params.offset
                ) {
                    and {
                        if (params.filterBy && params.filterBy != message(code: 'products.filter.by.default')) {
                            or {
                                eq('id', params.int('filterBy'))
                                ilike('internalNumber', "%${params.filterBy}%")
                                // todo: filter by item description (maybe use sql restriction?)
                            }
                        }

                        if (params.typeId) {
                            itemTypes {
                                eq('id', params.int('typeId'))
                            }
                        }

                        eq('deleted', 0)
                        eq('entity', flow.company)
                    }
                }

                params.template = 'products'
                [ products: products ]
            }
            on("success").to("build")
        }

        /**
         * Updates the main order attributes (period, billing type, active dates etc.) and
         * renders the order review panel.
         */
        updateOrder {
            action {
                bindData(flow.order, params)
                params.template = 'review'
            }
            on("success").to("build")
        }

        /**
         * Adds a product to the order as a new order line.
         */
        addOrderLine {
            action {
                // build line
                def line = new OrderLineWS()
                line.typeId = Constants.ORDER_LINE_TYPE_ITEM
                line.quantity = BigDecimal.ONE
                line.itemId = params.int('id')
                line.useItem = true

                // add to order lines
                def order = flow.order
                def lines = order.orderLines as List
                lines.add(line)
                order.orderLines = lines.toArray()

                params.template = 'review'
                [ order: webServicesSession.rateOrder(order) ]
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
            on("update").to("updateOrder")
            on("add").to("addOrderLine")
            on("save").to("saveOrder")
        }

        /**
         * Saves the order and exits the builder flow.
         */
        saveOrder {
            action {

            }
            on("success").to("finish")
        }

        finish {
            redirect controller: 'order', action: 'list', id: flow?.order?.id
        }
    }

}
