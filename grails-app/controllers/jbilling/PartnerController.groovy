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

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.security.authentication.encoding.PasswordEncoder

import com.sapienter.jbilling.client.ViewUtils
import com.sapienter.jbilling.client.user.UserHelper
import com.sapienter.jbilling.client.util.SortableCriteria
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.user.UserWS
import com.sapienter.jbilling.server.user.contact.db.ContactDTO
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.user.db.UserDTO
import com.sapienter.jbilling.server.util.Constants
import com.sapienter.jbilling.server.util.IWebServicesSessionBean

@Secured(["MENU_100"])
class PartnerController {

    static pagination = [ max: 10, offset: 0, sort: 'id', order: 'desc' ]

    IWebServicesSessionBean webServicesSession
    ViewUtils viewUtils
    PasswordEncoder passwordEncoder

    def filterService
    def recentItemService
    def breadcrumbService
    def springSecurityService

    def index = {
        redirect action: list, params: params
    }

    def getList(filters, GrailsParameterMap params) {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset
        params.sort = params?.sort ?: pagination.sort
        params.order = params?.order ?: pagination.order

        return UserDTO.createCriteria().list(
        max:    params.max,
        offset: params.offset
        ) {
            and {
                roles {
                    eq('id', Constants.TYPE_PARTNER)
                }
            }
            // apply sorting
            SortableCriteria.sort(params, delegate)
        }
    }

    /**
     */
    def list = {
        def filters = filterService.getFilters(FilterType.CUSTOMER, params)
        def users= getList(filters, params)
        if (users) println "${users.size()}"
        def selected = params.id ? UserDTO.get(params.int("id")) : null
        def contact = selected ? ContactDTO.findByUserId(selected.id) : null

        def crumbDescription = selected ? UserHelper.getDisplayName(selected, contact) : null
        breadcrumbService.addBreadcrumb(controllerName, 'list', null, selected?.id, crumbDescription)
        
        if (params.applyFilter) {
            render template: 'partners', model: [ partners: users, selected: selected, contact: contact ]
        } else {
            render view: 'list', model: [ partners: users, selected: selected, contact: contact]
        }
    }


    def edit = {
        def user
        def contacts

        try {
            user = params.id ? webServicesSession.getUserWS(params.int('id')) : new UserWS()
            contacts = params.id ? webServicesSession.getUserContactsWS(user.userId) : null
        } catch (SessionInternalError e) {
            log.error("Could not fetch WS object", e)
            flash.error = 'partner.not.found'
            flash.args = [params.id]
            redirect action: 'list', params:params
            return
        }

        [ user: user, contacts: contacts, company: company ]
    }

    def show = {
        def user = UserDTO.get(params.int('id'))
        def contact = user ? ContactDTO.findByUserId(user.id) : null

        breadcrumbService.addBreadcrumb(controllerName, 'list', null, user.userId, UserHelper.getDisplayName(user, contact))

        render template: 'show', model: [ selected: user, contact: contact ]
    }

    /**
     * Validate and save a user.
     */
    def save = {
        def user = new UserWS()
        UserHelper.bindUser(user, params)

        def contacts = []
        def company = CompanyDTO.get(session['company_id'])
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
                log.debug("creating user ${user}")

                user.userId = webServicesSession.createUser(user)

                flash.message = 'partner.created'
                flash.args = [user.userId]

            } else {
                log.debug("saving changes to user ${user.userId}")

                webServicesSession.updateUser(user)

                flash.message = 'partner.updated'
                flash.args = [user.userId]
            }

            // save secondary contacts
            if (user.userId) {
                contacts.each{
                    webServicesSession.updateUserContact(user.userId, it.type, it);
                }
            }

        } catch (SessionInternalError e) {
            viewUtils.resolveException(flash, session.locale, e)
            render view: 'edit', model: [ user: user, contacts: contacts, company: company ]
            return
        }

        chain action: 'list', params: [ id: user.userId ]
    }

    def delete = {
        if (params.id) {
            webServicesSession.deleteUser(params.int('id'))
            log.debug("Deleted partner ${params.id}.")
        }

        flash.message = 'partner.deleted'
        flash.args = [params.id]

        // render the partial user list
        params.applyFilter = true
        list()
    }

    def getCompany() {
        CompanyDTO.get(session['company_id'])
    }
}
