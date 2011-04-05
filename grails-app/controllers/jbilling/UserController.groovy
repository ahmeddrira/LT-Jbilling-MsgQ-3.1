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
import com.sapienter.jbilling.client.ViewUtils
import com.sapienter.jbilling.server.util.IWebServicesSessionBean
import com.sapienter.jbilling.server.user.db.UserStatusDAS
import com.sapienter.jbilling.server.user.db.UserDTO
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import com.sapienter.jbilling.server.util.Constants
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.user.UserWS
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.user.ContactWS
import com.sapienter.jbilling.client.user.UserHelper

@Secured(['isAuthenticated()'])
class UserController {

    static pagination = [ max: 10, offset: 0 ]

    IWebServicesSessionBean webServicesSession
    ViewUtils viewUtils

    def index = {
        redirect action: list, params: params
    }

    def getList(GrailsParameterMap params) {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        return UserDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            and {
				roles {
					ne('id', Constants.TYPE_CUSTOMER)
				}
                eq('company', new CompanyDTO(session['company_id']))
                eq('deleted', 0)
            }
            order('id', 'desc')
        }
    }

    def list = {
        def users = getList(params)
        def selected = params.id ? UserDTO.get(params.int("id")) : null

        if (params.applyFilter) {
            render template: 'users', model: [ users: users, selected: selected ]
        } else {
            render view: 'list', model: [ users: users, selected: selected ]
        }
    }

    def show = {
        def user = UserDTO.get(params.int('id'))
        render template: 'show', model: [ selected: user ]

    }

    def edit = {
        def user
        def contacts

        try {
            user = params.id ? webServicesSession.getUserWS(params.int('id')) : new UserWS()
            contacts = user ? webServicesSession.getUserContactsWS(user.userId) : null
        } catch (SessionInternalError e) {
            log.error("Could not fetch WS object", e)

            flash.error = 'user.not.found'
            flash.args = [ params.id ]

            redirect controller: 'user', action: 'list'
            return
        }

        def company = CompanyDTO.get(session['company_id'])

        [ user: user, contacts: contacts, company: company ]
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

                flash.message = 'user.created'
                flash.args = [ user.userId ]

            } else {
                log.debug("saving changes to user ${user.userId}")

                webServicesSession.updateUser(user)

                flash.message = 'user.updated'
                flash.args = [ user.userId ]
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
}
