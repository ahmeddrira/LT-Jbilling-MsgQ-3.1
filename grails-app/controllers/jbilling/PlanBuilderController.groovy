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
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.item.PlanWS
import com.sapienter.jbilling.server.item.PlanItemWS
import com.sapienter.jbilling.server.pricing.PriceModelWS
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.item.ItemDTOEx
import com.sapienter.jbilling.server.pricing.strategy.PriceModelStrategy
import com.sapienter.jbilling.server.pricing.util.AttributeUtils

/**
 * Plan builder controller
 *
 * @author Brian Cowdery
 * @since 01-Feb-2011
 */
@Secured(['isAuthenticated()'])
class PlanBuilderController {

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
        // filter on item type, item id and internal number
        def products = ItemDTO.createCriteria().list() {
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
            products = ItemDTO.createCriteria().list() {
                and {
                    eq('deleted', 0)
                    eq('entity', company)
                }
                order('id', 'asc')
            }.findAll {
                it.getDescription(session['language_id']).toLowerCase().contains(params.filterBy.toLowerCase())
            }
        }

        return products
    }

    def editFlow = {
        /**
         * Initializes the plan builder, putting necessary data into the flow and conversation
         * contexts so that it can be referenced later.
         */
        initialize {
            action {
                def plan = params.id ? webServicesSession.getPlanWS(params.int('id')) : new PlanWS()
                def product = plan?.itemId ? webServicesSession.getItem(plan.itemId, session['user_id'], null) : new ItemDTOEx()

                def company = CompanyDTO.get(session['company_id'])
                def itemTypes = company.itemTypes.sort{ it.id }

                def currencies = new CurrencyBL().getCurrencies(session['language_id'], session['company_id'])
                currencies = currencies.findAll{ it.inUse }

                // subscription product defaults for new plans
                if (!product.id || product.id == 0) {
                    product.hasDecimals = 0
                    product.priceManual = 0
                    product.entityId = company.id
                    product.types = [ itemTypes?.asList()?.first().id ]

                    def priceModel = new PriceModelWS()
                    priceModel.type = PriceModelStrategy.METERED
                    priceModel.rate = BigDecimal.ZERO
                    priceModel.currencyId = session['currency_id']

                    product.defaultPrice = priceModel
                }

                log.debug("plan subscription product ${product}")

                // add breadcrumb
                breadcrumbService.addBreadcrumb(controllerName, actionName, params.id ? 'update' : 'create', params.int('id'))

                // model scope for this flow
                flow.company = company
                flow.itemTypes = itemTypes
                flow.currencies = currencies

                // conversation scope
                conversation.plan = plan
                conversation.product = product
                conversation.products = getProducts(company, params)
            }
            on("success").to("build")
        }

        /**
         * Renders the plan details tab panel.
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
                if (params.typeId == null)
                    params.typeId = flow.itemTypes?.asList()?.first()?.id

                params.template = 'products'
                conversation.products = getProducts(flow.company, params)
            }
            on("success").to("build")
        }

        /**
         * Add a new price for the given product id, and render the review panel.
         */
        addPrice {
            action {
                // product being added
                def productId = params.int('id')
                def product = conversation.products.find{ it.id == productId }

                // build a new plan item, using the default item price model
                // as the new objects starting values
                def priceModel = new PriceModelWS(product.defaultPrice)
                priceModel.id = null

                // add price to the plan
                conversation.plan.planItems << new PlanItemWS(productId, priceModel)

                params.newLineIndex = conversation.plan.planItems.size() - 1
                params.template = 'review'
            }
            on("success").to("build")
        }

        /**
         * Updates a price and renders the review panel.
         */
        updatePrice {
            action {
                def plan = conversation.plan

                // update plan item and price model
                def index = params.int('index')
                def planItem = plan.planItems[index]
                def oldStrategy = planItem.model.type
                bindData(planItem, params["price-${index}"])
                bindData(planItem.model, params["model-${index}"])

                // bind and validate attributes
                planItem.model.attributes.clear()
                params.attribute.each{ i, attr ->
                    if (attr instanceof Map) {
                        if (attr.name) {
                            planItem.model.attributes.put(attr.name, attr.value)
                        }
                    }
                }

                try {
                    def strategy = Enum.valueOf(PriceModelStrategy.class, planItem.model.type).getStrategy()
                    AttributeUtils.validateAttributes(planItem.model.attributes, strategy)
                } catch (SessionInternalError e) {
                    viewUtils.resolveException(flow, session.locale, e)
                }

                // update the list
                plan.planItems[index] = planItem
                conversation.plan = plan

                // if changing the strategy, show the edited line again
                // otherwise re-order the list of lines by precedence
                if (oldStrategy != params["model-${index}.type"]) {
                    params.newLineIndex = index

                } else {
                    conversation.plan.planItems = conversation.plan.planItems.sort { it.precedence }.reverse()
                }
                params.template = 'review'
            }
            on("success").to("build")
        }

        /**
         * Removes a item price from the plan and renders the review panel.
         */
        removePrice {
            action {
                conversation.plan.planItems.remove(params.int('index'))
                params.template = 'review'
            }
            on("success").to("build")
        }

        /**
         * Adds a new attribute field to the plan price model, and renders the review panel.
         * The rendered review panel will have the edited line open for further modification.
         */
        addAttribute {
            action {
                def index = params.int('index')
                def attribute = message(code: 'plan.new.attribute.key', args: [ params.id ])
                conversation.plan.planItems[index].model.attributes.put(attribute, '')

                params.newLineIndex = index
                params.template = 'review'
            }
            on("success").to("build")
        }

        /**
         * Removes the given attribute name from a plan price model, and renders the review panel.
         * The rendered review panel will have the edited line open for further modification.
         */
        removeAttribute {
            action {
                def index = params.int('index')
                conversation.plan.planItems[index].model.attributes.remove(params.attribute)

                params.newLineIndex = index
                params.template = 'review'

            }
            on("success").to("build")
        }

        /**
         * Updates the plan description and renders the review panel.
         */
        updatePlan {
            action {
                bindData(conversation.plan, params, 'plan')
                bindData(conversation.product, params, 'product')
                bindData(conversation.product.defaultPrice, params, 'price')

                conversation.product.priceManual = params.priceManual ? 1 : 0
                conversation.product.types = [ params.int('productTypeId') ]

                // sort prices by precedence
                conversation.plan.planItems = conversation.plan.planItems.sort { it.precedence }.reverse()

                params.template = 'review'
            }
            on("success").to("build")
        }

        /**
         * Shows the plan builder. This is the "waiting" state that branches out to the rest
         * of the flow. All AJAX actions and other states that build on the order should
         * return here when complete.
         *
         * If the parameter 'template' is set, then a partial view template will be rendered instead
         * of the complete 'build.gsp' page view (workaround for the lack of AJAX support in web-flow).
         */
        build {
            on("details").to("showDetails")
            on("products").to("showProducts")
            on("addPrice").to("addPrice")
            on("updatePrice").to("updatePrice")
            on("removePrice").to("removePrice")
            on("addAttribute").to("addAttribute")
            on("removeAttribute").to("removeAttribute")
            on("update").to("updatePlan")
            on("save").to("savePlan")
            on("cancel").to("finish")
        }

        /**
         * Saves the plan and exits the builder flow.
         */
        savePlan {
            action {
                try {
                    def plan = conversation.plan
                    def product = conversation.product

                    if (!plan.id || plan.id == 0) {
                        log.debug("creating plan subscription item ${product}")
                        product.id = plan.itemId = webServicesSession.createItem(product)

                        log.debug("creating plan ${plan}")
                        plan.id = webServicesSession.createPlan(plan)

                    } else {
                        log.debug("saving changes to plan subscription item ${product.id}")
                        webServicesSession.updateItem(product)

                        log.debug("saving changes to plan ${plan.id}")
                        webServicesSession.updatePlan(plan)
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
            redirect controller: 'plan', action: 'list', id: conversation.plan?.id
        }
    }
}
