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

import com.sapienter.jbilling.server.item.db.PlanDTO
import com.sapienter.jbilling.server.user.db.CompanyDTO
import grails.plugins.springsecurity.Secured
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.item.PlanItemWS
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.client.pricing.util.PlanHelper
import com.sapienter.jbilling.server.item.db.ItemDTO
import com.sapienter.jbilling.server.pricing.PriceModelWS

/**
 * PlanController
 *
 * @author Brian Cowdery
 * @since 01-Feb-2011
 */
@Secured(['isAuthenticated()'])
class PlanController {

    static pagination = [ max: 10, offset: 0 ]

    def webServicesSession
    def viewUtils
    def filterService
    def breadcrumbService

    def index = {
        redirect action: list, params: params
    }

    /**
     * Get a list of plans and render the list page. If the "applyFilters" parameter is given, the
     * partial "_plans.gsp" template will be rendered instead of the complete list.
     */
    def list = {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        def plans = PlanDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            item {
                eq('entity', new CompanyDTO(session['company_id']))
            }
            order('id', 'desc')
        }

        def selected = params.id ? PlanDTO.get(params.int("id")) : null
        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))

        if (params.applyFilter) {
            render template: 'plans', model: [ plans: plans, selected: selected ]
        } else {
            [ plans: plans, selected: selected ]
        }
    }

    /**
     * Shows details of the selected plan.
     */
    def show = {
        PlanDTO plan = PlanDTO.get(params.int('id'))
        breadcrumbService.addBreadcrumb(controllerName, 'list', null, params.int('id'))

        render template: 'show', model: [ plan: plan ]
    }

    /**
     * Deletes the given plan id and all the plan item prices.
     */
    def delete = {
        if (params.id) {
            def plan = webServicesSession.getPlanWS(params.int('id'))

            webServicesSession.deletePlan(plan.id)
            webServicesSession.deleteItem(plan.itemId)

            log.debug("Deleted plan ${params.id} and subscription product ${plan.itemId}.")

            flash.message = 'plan.deleted'
            flash.args = [ params.id ]
        }

        // render the partial plan list
        params.applyFilter = true
        list()
    }


    // Customer specific pricing

    /**
     * Get the price to be edited and show the 'editCustomerPrice.gsp' view. If no ID is given
     * this screen will allow creation of a new customer-specific price.
     */
    def editCustomerPrice = {
        def userId = params.int('userId')
        def priceId = params.int('id')


        def product = webServicesSession.getItem(params.int('itemId'), userId, null)
        def user = webServicesSession.getUserWS(userId)

        def price
        if (priceId) {
            price = webServicesSession.getCustomerPrices(userId).find{ it.id == priceId }
        } else {
            price = new PlanItemWS()
            price.model = product.defaultPrice
        }

        [ price: price, product: product, user: user, currencies: currencies ]
    }

    def updateStrategy = {
        def priceModel = PlanHelper.bindPriceModel(params)
        render template: '/priceModel/model', model: [ model: priceModel, currencies: currencies ]
    }

    def addChainModel = {
        PriceModelWS priceModel = PlanHelper.bindPriceModel(params)

        // add new price model to end of chain
        def model = priceModel
        while (model.next) {
            model = model.next
        }
        model.next = new PriceModelWS();

        render template: '/priceModel/model', model: [ model: priceModel, currencies: currencies ]
    }

    def removeChainModel = {
        PriceModelWS priceModel = PlanHelper.bindPriceModel(params)
        def modelIndex = params.int('modelIndex')

        // remove price model from the chain
        def model = priceModel
        for (int i = 1; model != null; i++) {
            if (i == modelIndex) {
                model.next = model.next?.next
                break
            }
            model = model.next
        }

        render template: '/priceModel/model', model: [ model: priceModel, currencies: currencies ]
    }

    def addAttribute = {
        PriceModelWS priceModel = PlanHelper.bindPriceModel(params)

        def modelIndex = params.int('modelIndex')
        def attribute = message(code: 'plan.new.attribute.key', args: [ params.attributeIndex ])

        // find the model in the chain, and add a new attribute
        def model = priceModel
        for (int i = 0; model != null; i++) {
            if (i == modelIndex) {
                model.attributes.put(attribute, '')
            }
            model = model.next
        }

        render template: '/priceModel/model', model: [ model: priceModel, currencies: currencies ]
    }

    def removeAttribute = {
        PriceModelWS priceModel = PlanHelper.bindPriceModel(params)

        def modelIndex = params.int('modelIndex')
        def attributeIndex = params.int('attributeIndex')

        // find the model in the chain, remove the attribute
        def model = priceModel
        for (int i = 0; model != null; i++) {
            if (i == modelIndex) {
                def name = params["model.${modelIndex}.attribute.${attributeIndex}.name"]
                model.attributes.remove(name)
            }
            model = model.next
        }

        render template: '/priceModel/model', model: [ model: priceModel, currencies: currencies ]
    }

    /**
     * Validate and save a customer-specific price.
     */
    def saveCustomerPrice = {
        def user = webServicesSession.getUserWS(params.int('userId'))
        def price = new PlanItemWS()
        bindData(price, params, 'price')
        price.model = PlanHelper.bindPriceModel(params)

        try {
            if (!price.id || price.id == 0) {
                log.debug("creating customer ${user.userId} specific price ${price}")

                price = webServicesSession.createCustomerPrice(user.userId, price);

                flash.message = 'created.customer.price'
                flash.args = [ price.itemId ]
            } else {
                log.debug("updating customer ${user.userId} specific price ${price.id}")

                webServicesSession.updateCustomerPrice(user.userId, price);

                flash.message = 'updated.customer.price'
                flash.args = [ price.itemId ]
            }
        } catch (SessionInternalError e) {
            viewUtils.resolveException(flash, session.locale, e);
            render view: 'editCustomerPrice', model: [ price: price, user: user, products: products, currencies: currencies ]
            return
        }

        chain controller: 'customerInspector', action: 'inspect', params: [ id: user.userId ]
    }

    def getCurrencies() {
        def currencies = new CurrencyBL().getCurrencies(session['language_id'].toInteger(), session['company_id'].toInteger())
        return currencies.findAll { it.inUse }
    }

}
