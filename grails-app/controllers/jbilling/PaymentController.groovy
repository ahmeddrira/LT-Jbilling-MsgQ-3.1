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
import com.sapienter.jbilling.server.payment.db.PaymentDTO
import com.sapienter.jbilling.server.user.db.CompanyDTO
import org.hibernate.FetchMode
import org.hibernate.criterion.Restrictions
import org.hibernate.criterion.Criterion
import org.hibernate.Criteria

/**
 * PaymentController 
 *
 * @author Brian Cowdery
 * @since 04/01/11
 */
@Secured(['isAuthenticated()'])
class PaymentController {

    static pagination = [ max: 25, offset: 0 ]

    def webServicesSession
    def viewUtils
    def filterService
    def recentItemService
    def breadcrumbService

    def index = {
        redirect action: list, params: params
    }

    /**
     * Gets a list of payments and renders the the list page. If the "applyFilters" parameter is given,
     * the partial "_payments.gsp" template will be rendered instead of the complete payments list page.
     */
    def list = {
        def filters = filterService.getFilters(FilterType.PAYMENT, params)

        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        def payments = PaymentDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            createAlias('baseUser', 'u')
            createAlias('invoicesMap', 'i', Criteria.LEFT_JOIN)

            and {
                filters.each { filter ->
                    if (filter.value) {
                        switch (filter.constraintType) {
                            case FilterConstraint.EQ:
                                addToCriteria(Restrictions.eq(filter.field, filter.value))
                                break

                            case FilterConstraint.LIKE:
                                addToCriteria(Restrictions.like(filter.field, filter.stringValue))
                                break

                            case FilterConstraint.DATE_BETWEEN:
                                addToCriteria(Restrictions.between(filter.field, filter.startDateValue, filter.endDateValue))
                                break
                        }
                    }
                }

                eq('u.company', new CompanyDTO(session['company_id']))
                eq('deleted', 0)
            }
        }

        def selected = params.id ? PaymentDTO.get(params.int("id")) : null

        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))

        if (params.applyFilter) {
            render template: 'payments', model: [ payments: payments, selected: selected, filters: filters ]
        } else {
            [ payments: payments, selected: selected, filters: filters ]
        }
    }

    /**
     * Show details of the selected payment.
     */
    def show = {
        PaymentDTO payment = PaymentDTO.get(params.int('id'))
        recentItemService.addRecentItem(params.int('id'), RecentItemType.PAYMENT)
        breadcrumbService.addBreadcrumb(controllerName, 'list', params.template ?: null, params.int('id'))

        render template: 'show', model: [ selected: payment ]
    }

    /**
     * Convenience shortcut, this action shows all payments for the given user id.
     */
    def user = {
        def filter =  new Filter(type: FilterType.PAYMENT, constraintType: FilterConstraint.EQ, field: 'u.id', template: 'id', visible: true, integerValue: params.id)
        filterService.setFilter(FilterType.PAYMENT, filter)

        redirect action: list
    }

    /**
     * Delete the given payment id
     */
    def delete = {
        if (params.id) {
            // webServicesSession.deletePayment(params.int('id'))

            log.debug("Deleted payment ${params.id}.")

            flash.message = 'payment.deleted'
            flash.args = [ params.id ]
        }

        // render the partial payments list
        params.applyFilter = true
        list()
    }

    /**
     * Gets the payment to be edited and shows the "edit.gsp" view. This edit action cannot be used
     * to create a new payment, as creation requires a wizard style flow where the user is selected first.
     */
    def edit = {
        def payment = webServicesSession.getPayment(params.int('id'))
        def user = webServicesSession.getUserWS(payment.userId)

        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))

        [ payment: payment, user: user ]
    }

    def save = {

    }

}
