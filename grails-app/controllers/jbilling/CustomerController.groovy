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

import com.sapienter.jbilling.client.ViewUtils
import com.sapienter.jbilling.client.user.UserHelper
import com.sapienter.jbilling.client.util.DownloadHelper
import com.sapienter.jbilling.client.util.SortableCriteria
import com.sapienter.jbilling.common.Constants
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.process.db.PeriodUnitDTO
import com.sapienter.jbilling.server.user.UserWS
import com.sapienter.jbilling.server.user.contact.db.ContactDAS
import com.sapienter.jbilling.server.user.contact.db.ContactDTO
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.user.db.UserDTO
import com.sapienter.jbilling.server.user.db.UserStatusDAS
import com.sapienter.jbilling.server.util.IWebServicesSessionBean
import com.sapienter.jbilling.server.util.csv.CsvExporter
import com.sapienter.jbilling.server.util.csv.Exporter
import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.hibernate.FetchMode
import org.hibernate.criterion.MatchMode
import org.hibernate.criterion.Restrictions
import org.springframework.security.authentication.encoding.PasswordEncoder

@Secured(["MENU_90"])
class CustomerController {

    static pagination = [max: 10, offset: 0, sort: 'id', order: 'desc']

    IWebServicesSessionBean webServicesSession
    ViewUtils viewUtils
    PasswordEncoder passwordEncoder

    def filterService
    def recentItemService
    def breadcrumbService
    def springSecurityService
    def subAccountService

    @Secured(["hasAnyRole('MENU_90', 'CUSTOMER_15')"])
    def index = {
        redirect action: list, params: params
    }

    def getList(filters, statuses, GrailsParameterMap params) {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset
        params.sort = params?.sort ?: pagination.sort
        params.order = params?.order ?: pagination.order

        return UserDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            createAlias("contact", "contact")
            createAlias("customer", "customer")
            and {
                filters.each { filter ->
                    if (filter.value) {
                        // handle user status separately from the other constraints
                        // we need to find the UserStatusDTO to compare to
                        if (filter.constraintType == FilterConstraint.STATUS) {
                            eq("userStatus", statuses.find{ it.id == filter.integerValue })

                        } else if (filter.field == 'contact.fields') {
                            String typeId = params['contactFieldTypes']
                            String ccfValue= filter.stringValue
                            log.debug "Contact Field Type ID: ${typeId}, CCF Value: ${ccfValue}"
                            if (typeId && ccfValue) {
                                createAlias("contact.fields", "field")
                                createAlias("field.type", "type")
                                setFetchMode("type", FetchMode.JOIN)
                                eq("type.id", typeId.toInteger())
                                addToCriteria(Restrictions.ilike("field.content", ccfValue, MatchMode.ANYWHERE))
                            }
                        } else {
                            addToCriteria(filter.getRestrictions());
                        }
                    }
                }
                roles {
                    eq('id', Constants.TYPE_CUSTOMER)
                }
                eq('company', new CompanyDTO(session['company_id']))
                eq('deleted', 0)

                if (SpringSecurityUtils.ifNotGranted("CUSTOMER_17")) {
                    if (SpringSecurityUtils.ifAnyGranted("CUSTOMER_18")) {
                        // restrict query to sub-account user-ids
                        'in'('id', subAccountService.getSubAccountUserIds())
                    } else {
                        // limit list to only this customer
                        eq('id', session['user_id'])
                    }
                }
            }

            // apply sorting
            SortableCriteria.sort(params, delegate)
        }
    }

    /**
     * Get a list of users and render the list page. If the "applyFilters" parameter is given, the
     * partial "_users.gsp" template will be rendered instead of the complete user list.
     */
    @Secured(["hasAnyRole('MENU_90', 'CUSTOMER_15')"])
    def list = {
        def filters = filterService.getFilters(FilterType.CUSTOMER, params)
        def statuses = new UserStatusDAS().findAll()
        def selected = params.id ? UserDTO.get(params.int("id")) : null
        def users = []
        if (SpringSecurityUtils.ifNotGranted("MENU_90")) {
            log.debug "Customer , so you will view only yourself."
            users << UserDTO.get(springSecurityService.principal.id)
            selected = users[0]
        }
        else {
            log.debug "Super user , so you can view anything"
            users = getList(filters, statuses, params)
        }
        def contact = selected ? ContactDTO.findByUserId(selected.id) : null

        def crumbDescription = selected ? UserHelper.getDisplayName(selected, contact) : null
        breadcrumbService.addBreadcrumb(controllerName, 'list', null, selected?.id, crumbDescription)

        if (params.applyFilter || params.partial) {
            render template: 'customers', model: [users: users, selected: selected, contact: contact, statuses: statuses, filters: filters ]
        } else {
            render view: 'list', model: [ users: users, selected: selected, contact: contact, statuses: statuses, filters: filters ]
        }
    }

    /**
     * Applies the set filters to the user list, and exports it as a CSV for download.
     */
    @Secured(["CUSTOMER_16"])
    def csv = {
        def filters = filterService.getFilters(FilterType.CUSTOMER, params)
        def statuses = new UserStatusDAS().findAll()

        params.max = CsvExporter.MAX_RESULTS
        def users = getList(filters, statuses, params)

        if (users.totalCount > CsvExporter.MAX_RESULTS) {
            flash.error = message(code: 'error.export.exceeds.maximum')
            flash.args = [ CsvExporter.MAX_RESULTS ]
            redirect action: 'list'

        } else {
            DownloadHelper.setResponseHeader(response, "users.csv")
            Exporter<UserDTO> exporter = CsvExporter.createExporter(UserDTO.class);
            render text: exporter.export(users), contentType: "text/csv"
        }
    }

    /**
     * Show details of the selected user. By default, this action renders the "_show.gsp" template.
     * When rendering for an AJAX request the template defined by the "template" parameter will be rendered.
     */
    @Secured(["CUSTOMER_15"])
    def show = {
        def user = UserDTO.get(params.int('id'))
        def contact = new ContactDAS().findPrimaryContact(user.userId)
        def revenue = webServicesSession.getTotalRevenueByUser(user.userId)

        recentItemService.addRecentItem(user.userId, RecentItemType.CUSTOMER)
        breadcrumbService.addBreadcrumb(controllerName, 'list', params.template ?: null, user.userId, UserHelper.getDisplayName(user, contact))

        render template: params.template ?: 'show', model: [ selected: user, contact: contact, revenue: revenue ]
    }

    /**
     * Fetches a list of sub-accounts for the given user id and renders the user list "_table.gsp" template.
     */
    def subaccounts = {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        def children = UserDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            and{
                customer {
                    parent {
                        eq('baseUser.id', params.int('id'))
                    }
                }
                eq('deleted', 0)
            }
        }

        def parent = UserDTO.get(params.int('id'))
        render template: 'customers', model: [ users: children, parent: parent ]
    }

    /**
     * Shows all customers of the given partner id
     */
    def partner = {
        def filter = new Filter(type: FilterType.CUSTOMER, constraintType: FilterConstraint.EQ, field: 'customer.partner.id', template: 'id', visible: true, integerValue: params.id)
        filterService.setFilter(FilterType.CUSTOMER, filter)

        redirect action: list
    }

    /**
     * Updates the notes for the given user id.
     */
    @Secured(["CUSTOMER_11"])
    def saveNotes = {
        if (params.id) {
            webServicesSession.saveCustomerNotes(params.int('id'), params.notes)

            log.debug("Updating notes for user ${params.id}.")

            flash.message = 'customer.notes'
            flash.args = [ params.id as String ]
        }

        // render user list with selected id
        list()
    }

    /**
     * Delete the given user id.
     */
    @Secured(["CUSTOMER_12"])
    def delete = {
        if (params.id) {
            webServicesSession.deleteUser(params.int('id'))

            flash.message = 'customer.deleted'
            flash.args = [ params.id ]
            log.debug("Deleted user ${params.id}.")

            // remove the id from the list in session.
            subAccountService.removeSubAccountUserId(params.int('id'))
        }

        // render the partial user list
        params.partial = true
        list()
    }

    /**
     * Get the user to be edited and show the "edit.gsp" view. If no ID is given this view
     * will allow creation of a new user.
     */
    @Secured(["hasAnyRole('CUSTOMER_10', 'CUSTOMER_11')"])
    def edit = {
        def user
        def contacts
        def parent

        try {
            user = params.id ? webServicesSession.getUserWS(params.int('id')) : null
            contacts = user ? webServicesSession.getUserContactsWS(user.userId) : null
            parent = params.parentId ? webServicesSession.getUserWS(params.int('parentId')) : null

        } catch (SessionInternalError e) {
            log.error("Could not fetch WS object", e)

            flash.error = 'customer.not.found'
            flash.args = [ params.id as String ]

            redirect controller: 'customer', action: 'list'
            return
        }

        def crumbName = params.id ? 'update' : 'create'
        def crumbDescription = params.id ? UserHelper.getDisplayName(user, user.contact) : null
        breadcrumbService.addBreadcrumb(controllerName, actionName, crumbName, params.int('id'), crumbDescription)

        def periodUnits = PeriodUnitDTO.list()

        [ user: user, contacts: contacts, parent: parent, company: company, currencies: currencies, periodUnits:periodUnits ]
    }

    /**
     * Validate and save a user.
     */
    @Secured(["hasAnyRole('CUSTOMER_10', 'CUSTOMER_11')"])
    def save = {
        def user = new UserWS()
        UserHelper.bindUser(user, params)

        def contacts = []
        UserHelper.bindContacts(user, contacts, company, params)

        def oldUser = (user.userId && user.userId != 0) ? webServicesSession.getUserWS(user.userId) : null
        UserHelper.bindPassword(user, oldUser, params, flash)

        if (flash.error) {
            render view: 'edit', model: [ user: user, contacts: contacts, company: company ]
            return
        }

        try {
            // save or update
            if (!oldUser) {
                if (SpringSecurityUtils.ifAllGranted("CUSTOMER_10")) {

                    user.userId = webServicesSession.createUser(user)
                    flash.message = 'customer.created'
                    flash.args = [ user.userId as String ]

                    // add the id to the list in session.
                    subAccountService.addSubAccountUserId(user)

                } else {
                    render view: '/login/denied'
                    return
                }

            } else {
                if (SpringSecurityUtils.ifAllGranted("CUSTOMER_11")) {

                    webServicesSession.updateUser(user)

                    // ACH updates are not handled through updateUser. Make a separate API call
                    // to update the customers ACH data if it's present
                    if (user.ach) {
                        webServicesSession.updateAch(user.userId, user.ach)
                    }

                    // payment data deletions
                    if (params.deleteAch) {
                        log.debug("deleting ACH for user ${user.userId}")
                        webServicesSession.deleteAch(user.userId)
                    }

                    if (params.deleteCreditCard) {
                        log.debug("deleting Credit Card for user ${user.userId}")
                        webServicesSession.deleteCreditCard(user.userId)
                    }

                    flash.message = 'customer.updated'
                    flash.args = [ user.userId as String ]

                } else {
                    render view: '/login/denied'
                    return
                }
            }

            // save contacts
            if (user.userId) {
                contacts.each{
                    webServicesSession.updateUserContact(user.userId, it.type, it);
                }
            }

        } catch (SessionInternalError e) {
            viewUtils.resolveException(flash, session.locale, e)
            render view: 'edit', model: [ user: user, contacts: contacts, company: company, currencies: currencies ]
            return
        }

        chain action: 'list', params: [ id: user.userId ]
    }

    def getCurrencies() {
        def currencies = new CurrencyBL().getCurrencies(session['language_id'].toInteger(), session['company_id'].toInteger())
        return currencies.findAll { it.inUse }
    }

    def getCompany() {
        CompanyDTO.get(session['company_id'])
    }
}
