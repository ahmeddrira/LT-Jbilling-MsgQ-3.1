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
import com.sapienter.jbilling.server.pricing.db.PriceModelStrategy
import com.sapienter.jbilling.server.pricing.util.AttributeUtils
import com.sapienter.jbilling.server.item.ItemTypeBL
import com.sapienter.jbilling.server.pricing.PriceModelBL
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO
import com.sapienter.jbilling.server.util.Constants
import com.sapienter.jbilling.client.pricing.util.PlanHelper
import com.sapienter.jbilling.server.item.PlanItemBundleWS
import com.sapienter.jbilling.server.item.db.PlanItemBundleDTO

/**
 * Plan builder controller
 *
 * @author Brian Cowdery
 * @since 01-Feb-2011
 */
@Secured(['isAuthenticated()'])
class PlanBuilderController {

    def webServicesSession
    def viewUtils

    def breadcrumbService
    def productService

    def index = {
        redirect action: 'edit'
    }

    /**
     * Sorts a list of PlanItemWS objects by precedence and itemId.
     *
     * @param planItems plan items
     * @return sorted list of plan items
     */
    def sortPlanItems(planItems) {
        // precedence in ascending order, item id in descending
        return planItems.sort { a, b->
            (b.precedence <=> a.precedence) ?: (a.itemId <=> b.itemId)
        }
    }

    def editFlow = {
        /**
         * Initializes the plan builder, putting necessary data into the flow and conversation
         * contexts so that it can be referenced later.
         */
        initialize {
            action {
                def plan
                def product

                try {
                    plan = params.id ? webServicesSession.getPlanWS(params.int('id')) : new PlanWS()
                    product = plan?.itemId ? webServicesSession.getItem(plan.itemId, session['user_id'], null) : new ItemDTOEx()
                } catch (SessionInternalError e) {
                    log.error("Could not fetch WS object", e)

                    session.error = 'plan.not.found'
                    session.args = [ params.id ]

                    redirect controller: 'plan', action: 'list'
                    return
                }

                def company = CompanyDTO.get(session['company_id'])
                def itemTypes = productService.getItemTypes()
                def internalPlansType = productService.getInternalPlansType()

                def currencies = new CurrencyBL().getCurrencies(session['language_id'], session['company_id'])
                currencies = currencies.findAll{ it.inUse }

                def orderPeriods = company.orderPeriods.collect { new OrderPeriodDTO(it.id) } << new OrderPeriodDTO(Constants.ORDER_PERIOD_ONCE)
                orderPeriods.sort { it.id }

                // subscription product defaults for new plans
                if (!product.id || product.id == 0) {
                    product.hasDecimals = 0
                    product.types = [ internalPlansType.id ]
                    product.entityId = company.id

                    def priceModel = new PriceModelWS()
                    priceModel.type = PriceModelStrategy.METERED
                    priceModel.rate = BigDecimal.ZERO
                    priceModel.currencyId = session['currency_id']

                    product.defaultPrice = priceModel
                }


                log.debug("plan subscription product ${product}")

                // defaults for new plans
                if (!plan.id || plan.id == 0) {
                    plan.periodId = orderPeriods.find{ it.id != Constants.ORDER_PERIOD_ONCE }?.id
                }

                log.debug("plan ${plan}")

                // add breadcrumb
                breadcrumbService.addBreadcrumb(controllerName, actionName, params.id ? 'update' : 'create', params.int('id'))

                // model scope for this flow
                flow.company = company
                flow.itemTypes = itemTypes
                flow.currencies = currencies
                flow.orderPeriods = orderPeriods

                // conversation scope
                conversation.plan = plan
                conversation.product = product
                conversation.products = productService.getFilteredProducts(company, params)
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
                if (params.typeId == null && flow.itemTypes)
                    params.typeId = flow.itemTypes?.asList()?.first()?.id

                params.template = 'products'
                conversation.products = productService.getFilteredProducts(flow.company, params)
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

                // empty bundle
                def bundle = new PlanItemBundleWS()

                // add price to the plan
                conversation.plan.planItems << new PlanItemWS(productId, priceModel, bundle)

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
                def index = params.int('index')
                def planItem = conversation.plan.planItems[index]

                if (!planItem.bundle) planItem.bundle = new PlanItemBundleWS()


                bindData(planItem, params, 'price')
                bindData(planItem.bundle, params, 'bundle')
                planItem.model = PlanHelper.bindPriceModel(params)

                try {
                    // validate attributes of updated price
                    PriceModelBL.validateAttributes(planItem.model)

                    // re-order plan items by precedence, unless a validation exception is thrown
                    conversation.plan.planItems = sortPlanItems(conversation.plan.planItems)

                } catch (SessionInternalError e) {
                    viewUtils.resolveException(flow, session.locale, e)
                    params.newLineIndex = index
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
         * Updates a strategy of a model in a pricing chain.
         */
        updateStrategy {
            action {
                def index = params.int('index')
                def planItem = conversation.plan.planItems[index]

                bindData(planItem, params, 'price')
                planItem.model = PlanHelper.bindPriceModel(params)

                params.newLineIndex = index
                params.template = 'review'
            }
            on("success").to("build")
        }

        /**
         * Adds an additional price model to the chain.
         */
        addChainModel {
            action {
                def index = params.int('index')
                def planItem = conversation.plan.planItems[index]
                planItem.model = PlanHelper.bindPriceModel(params)

                // add new price model to end of chain
                def model = planItem.model
                while (model.next) {
                    model = model.next
                }
                model.next = new PriceModelWS();

                params.newLineIndex = index
                params.template = 'review'
            }
            on("success").to("build")
        }

        removeChainModel {
            action {
                def index = params.int('index')
                def modelIndex = params.int('modelIndex')
                def planItem = conversation.plan.planItems[index]
                planItem.model = PlanHelper.bindPriceModel(params)

                // remove price model from the chain
                def model = planItem.model
                for (int i = 1; model != null; i++) {
                    if (i == modelIndex) {
                        model.next = model.next?.next
                        break
                    }
                    model = model.next
                }

                params.newLineIndex = index
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
                def planItem = conversation.plan.planItems[index]
                planItem.model = PlanHelper.bindPriceModel(params)

                def modelIndex = params.int('modelIndex')
                def attribute = message(code: 'plan.new.attribute.key', args: [ params.attributeIndex ])

                // find the model in the chain, and add a new attribute
                def model = planItem.model
                for (int i = 0; model != null; i++) {
                    if (i == modelIndex) {
                        model.attributes.put(attribute, '')
                    }
                    model = model.next
                }

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
                def planItem = conversation.plan.planItems[index]
                planItem.model = PlanHelper.bindPriceModel(params)

                def modelIndex = params.int('modelIndex')
                def attributeIndex = params.int('attributeIndex')

                // find the model in the chain, remove the attribute
                def model = planItem.model
                for (int i = 0; model != null; i++) {
                    if (i == modelIndex) {
                        def name = params["model.${modelIndex}.attribute.${attributeIndex}.name"]
                        model.attributes.remove(name)
                    }
                    model = model.next
                }

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

                // sort prices by precedence
                conversation.plan.planItems = sortPlanItems(conversation.plan.planItems)

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
            // list
            on("details").to("showDetails")
            on("products").to("showProducts")

            // pricing
            on("addPrice").to("addPrice")
            on("updatePrice").to("updatePrice")
            on("removePrice").to("removePrice")

            // pricing model
            on("updateStrategy").to("updateStrategy")
            on("addChainModel").to("addChainModel")
            on("removeChainModel").to("removeChainModel")
            on("addAttribute").to("addAttribute")
            on("removeAttribute").to("removeAttribute")

            // plan
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

                        // set success message in session, contents of the flash scope doesn't survive
                        // the redirect to the order list when the web-flow finishes
                        session.message = 'plan.created'
                        session.args = [ plan.id ]

                    } else {
                        log.debug("saving changes to plan subscription item ${product.id}")
                        webServicesSession.updateItem(product)

                        log.debug("saving changes to plan ${plan.id}")
                        webServicesSession.updatePlan(plan)

                        session.message = 'plan.updated'
                        session.args = [ plan.id ]
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
