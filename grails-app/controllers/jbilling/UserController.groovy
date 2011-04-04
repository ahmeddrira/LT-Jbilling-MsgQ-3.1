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

        [ users: users, selected: selected ]
    }

    def show = {
        def user = UserDTO.get(params.int('id'))
        render template: 'show', model: [ selected: user ]

    }

    def edit = {
        def user = params.id ? webServicesSession.getUserWS(params.int('id')) : new UserWS()
        def company = CompanyDTO.get(session['company_id'])

        [ user: user, company: company ]
    }


    /**
     * Validate and save a user.
     */
    def save = {
        def user = new UserWS()
        bindData(user, params, 'user')

        def company = CompanyDTO.get(session['company_id'])
        def contactTypes = company.contactTypes
        def primaryContactTypeId = params.int('primaryContactTypeId')

        // bind primary user contact and custom contact fields
        def contact = new ContactWS()
        bindData(contact, params, "contact-${params.primaryContactTypeId}")
        contact.type = primaryContactTypeId
		contact.include = params.get("contact-${params.primaryContactTypeId}.include") ? 1 : 0

        if (params.contactField) {
            contact.fieldIDs = new Integer[params.contactField.size()]
            contact.fieldValues = new Integer[params.contactField.size()]
            params.contactField.eachWithIndex { id, value, i ->
                contact.fieldIDs[i] = id.toInteger()
                contact.fieldValues[i] = value
            }
        }

        user.setContact(contact)

        log.debug("Primary contact: ${contact}")


        // bind secondary contact types
        def contacts = []
        contactTypes.findAll{ it.id != primaryContactTypeId }.each{
            // bind if contact object if parameters present
            if (params["contact-${it.id}"].any { key, value -> value }) {
                def otherContact = new ContactWS()
                bindData(otherContact, params, "contact-${it.id}")
                otherContact.type = it.id
				//checkbox values are not bound automatically since it throws a data conversion error
				otherContact.include= params.get('contact-' + it.id + '.include') ? 1 : 0

                contacts << otherContact;
            }
        }

        log.debug("Secondary contacts: ${contacts}")

        // set password
        def oldUser = (user.userId && user.userId != 0) ? webServicesSession.getUserWS(user.userId) : null
        if (oldUser) {
            if (params.newPassword) {
                // validate that the entered confirmation password matches the users existing password
                if (!passwordEncoder.isPasswordValid(oldUser.password, params.oldPassword, null)) {
                    flash.error = 'customer.current.password.doesnt.match.existing'
                    render view: 'edit', model: [ user: user, contacts: contacts, company: company, currencies: currencies ]
                    return
                }
            }
        } else {
            if (!params.newPassword) {
                flash.error = 'customer.create.without.password'
                render view: 'edit', model: [ user: user, contacts: contacts, company: company, currencies: currencies ]
                return
            }
        }

        // verify passwords
        if (params.newPassword == params.verifiedPassword) {
            if (params.newPassword) user.setPassword(params.newPassword)

        } else {
            flash.error = 'customer.passwords.dont.match'
            render view: 'edit', model: [ user: user, contacts: contacts, company: company, currencies: currencies ]
            return
        }

        try {
            // save or update
            if (!oldUser) {
                log.debug("creating user ${user}")

                user.userId = webServicesSession.createUser(user)

                flash.message = 'customer.created'
                flash.args = [ user.userId ]

            } else {
                log.debug("saving changes to user ${user.userId}")

                webServicesSession.updateUser(user)

                if (user.ach) {
                    webServicesSession.updateAch(user.userId, user.ach)
                }

                flash.message = 'customer.updated'
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
            company = CompanyDTO.get(session['company_id'])
            render view: 'edit', model: [ user: user, contacts: contacts, company: company ]
            return
        }

        chain action: 'list', params: [ id: user.userId ]
    }
}
