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
import com.sapienter.jbilling.server.payment.PaymentWS
import com.sapienter.jbilling.server.entity.CreditCardDTO
import com.sapienter.jbilling.server.entity.AchDTO
import com.sapienter.jbilling.server.entity.PaymentInfoChequeDTO
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.invoice.InvoiceWS
import com.sapienter.jbilling.server.util.db.CurrencyDTO
import com.sapienter.jbilling.server.payment.db.PaymentMethodDTO

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
            webServicesSession.deletePayment(params.int('id'))

            log.debug("Deleted payment ${params.id}.")

            flash.message = 'payment.deleted'
            flash.args = [ params.id ]
        }

        // render the partial payments list
        params.applyFilter = true
        list()
    }

    /**
     * Un-links the given payment ID from the given invoice ID and re-renders
     * the "show payment" view panel.
     */
    def unlink = {
		try {
			webServicesSession.removePaymentLink(params.int('invoiceId'), params.int('id'))
			flash.message = "payment.unlink.success"

		} catch (Exception e) {
			flash.error = "error.invoice.unlink.payment"
		}

		show()
    }

    /**
     * Redirects to the user list and sets a flash message.
     */
    def create = {
        flash.info = 'payment.select.customer'
        redirect controller: 'user', action: 'list'
    }

    /**
     * Gets the payment to be edited and shows the "edit.gsp" view. This edit action cannot be used
     * to create a new payment, as creation requires a wizard style flow where the user is selected first.
     */
    def edit = {
        def payment = params.id ? webServicesSession.getPayment(params.int('id')) : new PaymentWS()
        def user = webServicesSession.getUserWS(payment?.userId ?: params.int('userId'))
        def invoices = getUnpaidInvoices(user.userId)
        def paymentMethods = CompanyDTO.get(session['company_id']).getPaymentMethods()

        // todo: set user's prefered payment instrument


        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))

        [ payment: payment, user: user, invoices: invoices, currencies: currencies, paymentMethods: paymentMethods ]
    }

    def getUnpaidInvoices(Integer userId) {
        def invoiceIds = webServicesSession.getUnpaidInvoices(userId);

        List<InvoiceWS> invoices = new ArrayList<InvoiceWS>(invoiceIds.size());
        for (Integer id : invoiceIds)
            invoices.add(webServicesSession.getInvoiceWS(id))
        return invoices;
    }

    /**
     * Validate and save payment.
     */
    def save = {
        def payment = new PaymentWS()
        bindData(payment, params, 'payment')

        payment.isRefund = params.isRefund ? 1 : 0

        // bind credit card object if parameters present
        if (params.creditCard.any { key, value -> value }) {
            def creditCard = new CreditCardDTO()
            bindData(creditCard, params, 'creditCard')
            bindExpiryDate(creditCard, params)
            payment.setCreditCard(creditCard)
        }

        // bind ach object if parameters present
        if (params.ach.any { key, value -> value }) {
            def ach = new AchDTO()
            bindData(ach, params, 'ach')
            payment.setAch(ach)
        }

        // bind cheque object if parameters present
        if (params.cheque.any { key, value -> value }) {
            def cheque = new PaymentInfoChequeDTO()
            bindData(cheque, params, 'cheque')
            payment.setCheque(cheque)
        }

        // save or update
        try {
            if (!payment.id || payment.id == 0) {
                log.debug("creating payment ${payment}")

                payment.id = webServicesSession.applyPayment(payment, params.int('invoiceId'))

                flash.message = 'payment.created'
                flash.args = [ payment.id ]

            } else {
                log.debug("saving changes to payment ${payment.id}")

                webServicesSession.updatePayment(payment)

                flash.message = 'payment.updated'
                flash.args = [ payment.id ]
            }

        } catch (SessionInternalError e) {
            viewUtils.resolveException(flash, session.local, e)
            render view: edit, model: [ payment: payment ]
            return
        }

        chain action: list, params: [ id: payment.id ]
    }

    def bindExpiryDate(CreditCardDTO creditCard, params) {
        Calendar calendar = Calendar.getInstance()
        calendar.clear()
        calendar.set(Calendar.MONTH, params.int('expiryMonth'))
        calendar.set(Calendar.YEAR, params.int('expiryYear'))

        creditCard.expiry = calendar.getTime()
    }

    def getCurrencies() {
        def currencies = new CurrencyBL().getCurrencies(session['language_id'].toInteger(), session['company_id'].toInteger())
        return currencies.findAll{ it.inUse }
    }
}
