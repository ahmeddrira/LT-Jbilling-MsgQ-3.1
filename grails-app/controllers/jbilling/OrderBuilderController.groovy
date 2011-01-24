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
                log.debug("Initializing builder")

                def company = CompanyDTO.get(session['company_id'])
                def user = UserDTO.get(params.int('userId'))
                def order = params.id ? webServicesSession.getOrder(params.int('id')) : new OrderWS()

                // available in the 'flow' scope for duration of the builder
                [ company: company, user: user, order: order ]
            }
            on("success").to("build")
        }

        /**
         * Renders the order details tab panel
         */
        showDetails {
            action {
                params.template = 'details'
            }
            on("success").to("render")
        }

        updateDetails {
            action {
                def order = flow.order

                // update order details

                [ order: order ]
            }
            on("success").to("render")
        }

        /**
         * Renders the product list tab panel, filtering the product list by the given criteria
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

                log.debug "rendering products list"

                params.template = 'products'
                [ products: products ]
            }
            on("success").to("render")
        }

        addProduct {
            action {
                // add the selected product to the order.
                // re-render the order review template with the new product added and the line editor opened.
            }
            on("success").to("render")
        }

        // TODO: we can put some magic in the build.gsp view to merge these two view actions.

        /**
         * Renders a partial view template for AJAX requests (workaround for the lack of AJAX support in webflow).
         */
        render {
            flow
            on("save").to("save")
            on("details").to("showDetails")
            on("products").to("showProducts")
        }

        /**
         * Shows the order builder. This is the "waiting" state that branches out to
         * the rest of the flow. All AJAX actions and other states that build on order
         * should return here when complete.
         */
        build {
            on("save").to("save")
            on("details").to("showDetails")
            on("products").to("showProducts")
        }

        /**
         * Saves the order and exits the builder flow.
         */
        save {
            action {

            }
            on("success").to("finish")
        }

        finish {
            redirect controller: 'order', action: 'list', id: flow?.order?.id
        }
    }

}
