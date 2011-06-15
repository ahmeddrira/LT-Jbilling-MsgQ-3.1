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
import com.sapienter.jbilling.server.user.CustomerPriceBL
import com.sapienter.jbilling.client.util.SortableCriteria

/**
 * PlanController
 *
 * @author Brian Cowdery
 * @since 01-Feb-2011
 */
@Secured(["isAuthenticated()"])
class PlanController {

    static pagination = [ max: 10, offset: 0, sort: 'id', order: 'desc' ]

    def webServicesSession
    def viewUtils
    def filterService
    def breadcrumbService

    @Secured(["MENU_98"])
    def index = {
        redirect action: list, params: params
    }

    /**
     * Get a list of plans and render the list page. If the "applyFilters" parameter is given, the
     * partial "_plans.gsp" template will be rendered instead of the complete list.
     */
    @Secured(["MENU_98"])
    def list = {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset
        params.sort = params?.sort ?: pagination.sort
        params.order = params?.order ?: pagination.order

        def plans = PlanDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            item {
                eq('entity', new CompanyDTO(session['company_id']))
            }

            // apply sorting
            SortableCriteria.sort(params, delegate)
        }

        def selected = params.id ? PlanDTO.get(params.int("id")) : null
        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'), selected?.item?.internalNumber)

        if (params.applyFilter || params.partial) {
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
        breadcrumbService.addBreadcrumb(controllerName, 'list', null, params.int('id'), plan.item.internalNumber)

        render template: 'show', model: [ plan: plan ]
    }

    /**
     * Deletes the given plan id and all the plan item prices.
     */
    def delete = {
        if (params.id) {
            def plan

            try {
                plan = webServicesSession.getPlanWS(params.int('id'))
            } catch (SessionInternalError e) {
                log.error("Could not fetch WS object", e)

                flash.error = 'plan.not.found'
                flash.args = [ params.id ]

                redirect action: 'list'
                return
            }

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
}
