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

import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.client.ViewUtils
import com.sapienter.jbilling.client.user.UserHelper
import com.sapienter.jbilling.client.util.SortableCriteria
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.user.UserWS
import com.sapienter.jbilling.server.user.contact.db.ContactDTO
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.user.partner.PartnerWS
import com.sapienter.jbilling.server.user.partner.db.Partner
import com.sapienter.jbilling.server.user.db.UserDTO
import com.sapienter.jbilling.server.user.db.UserStatusDTO
import com.sapienter.jbilling.server.util.db.CurrencyDTO
import com.sapienter.jbilling.server.util.Constants
import com.sapienter.jbilling.server.util.IWebServicesSessionBean
import com.sapienter.jbilling.server.user.partner.PartnerBL

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

        def statuses= UserStatusDTO.findAll()
        return Partner.createCriteria().list(
            max:    params.max,
            offset: params.offset
        ) {
            and {
                createAlias("baseUser.contact", "contact")
                filters.each { filter ->
                    if (filter.value) {
                        if (filter.constraintType == FilterConstraint.STATUS) {
                            baseUser {
                                eq("userStatus", statuses.find{ it.id == filter.integerValue })
                            }
                        } else {
                            addToCriteria(filter.getRestrictions());
                        }
                    }
                }
                baseUser {
                    eq('deleted', 0)
                    eq('company', company)
                }
            }
            // apply sorting
            SortableCriteria.sort(params, delegate)
        }
    }

    /**
     */
    def list = {
        
        def filters = filterService.getFilters(FilterType.PARTNER, params)
        
        def partners= getList(filters, params)
        
        log.debug "Found ${partners?.size()} partners."
        
        def selected = params.id ? Partner.get(params.int("id")) : null
        
        def contact = selected ? ContactDTO.findByUserId(selected?.baseUser?.id) : null

        //def crumbDescription = selected ? UserHelper.getDisplayName(selected.baseUser, contact) : null
        
        breadcrumbService.addBreadcrumb(controllerName, 'list', null, null)
        
        if (params.applyFilter) {
            render template: 'partners', model: [ partners: partners, selected: selected, contact: contact, filters:filters ]
            return 
        } 
        render view: 'list', model: [ partners: partners, selected: selected, contact: contact, filters:filters]
        
    }

    def show = {
        
        def partner= Partner.get(params.int('id'))
        
        def contact = partner ? ContactDTO.findByUserId(partner?.baseUser.id) : null

        breadcrumbService.addBreadcrumb(controllerName, 'show', null, partner.id, UserHelper.getDisplayName(partner.baseUser, contact))

        render template: 'show', model: [ selected: partner, contact: contact ]
    }

    def edit = {
        
        def user
        def partner
        def contacts

        try {
            
            partner= params.id ? webServicesSession.getPartner(params.int('id')) : new PartnerWS()
            log.debug partner?.relatedClerkUserId
            user= (params.id &&  partner) ? webServicesSession.getUserWS(partner?.userId) : new UserWS()
            contacts = params.id ? webServicesSession.getUserContactsWS(user.userId) : null
            
        } catch (SessionInternalError e) {
            log.error("Could not fetch WS object", e)
            flash.error = 'partner.not.found'
            flash.args = [params.id]
            redirect action: 'list', params:params
            return
        }

        [ partner: partner, user: user, contacts: contacts, company: company, currencies: currencies, clerks:clerks ]
    }

    /**
     * Validate and Save the Partner User
     */
    def save = {
        
        def partner= new PartnerWS()
        def user = new UserWS()
        
        bindData(partner, params)
        UserHelper.bindUser(user, params)
        
        if (params.int('nextPayoutYear')) {
            def nextPayout= Calendar.getInstance();
            nextPayout.clear()
            nextPayout.set(params.int('nextPayoutYear'), params.int('nextPayoutMonth'), params.int('nextPayoutDay'))
            partner.setNextPayoutDate(nextPayout.getTime())
        } else {
            log.error "Next payout date is mandatory for partner."
            //show error
        }
        
        def contacts = []
        UserHelper.bindContacts(user, contacts, company, params)

        def oldUser = (user.userId && user.userId != 0) ? webServicesSession.getUserWS(user.userId) : null
        UserHelper.bindPassword(user, oldUser, params, flash)

        if (flash.error) {
            render view: 'edit', model: [ partner: partner, user: user, contacts: contacts, company: company, currencies: currencies, clerks:clerks ]
            return
        }

        try {
            // save or update
            if (!oldUser) {
                log.debug("creating user ${user}")

                partner.id = webServicesSession.createPartner(user, partner)
                
                flash.message = 'partner.created'
                flash.args = [user.userId]

            } else {
            
                log.debug("saving changes to user ${user.userId} & ${user.customerId}")
                webServicesSession.updateUser(user)
                
                //save partner
                def partnerService= new PartnerBL()
                partner.setBaseUser(UserDTO.get(user.userId))
                partner.id= partnerService.update(partner)

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
            render view: 'edit', model: [ partner: partner, user: user, contacts: contacts, company: company, currencies: currencies, clerks:clerks ]
            return
        }

        chain action: 'list', params: [ id: partner.id ]
    }

    def delete = {
        if (params.id) {
            webServicesSession.deletePartner(params.int('id'))
            log.debug("Deleted partner ${params.id}.")
        }

        flash.message = 'partner.deleted'
        flash.args = [params.id]

        // render the partial user list
        params.applyFilter = true
        list()
    }

    def getClerks() {
        return UserDTO.createCriteria().list() {
            and {
                or {
                    isEmpty('roles')
                    roles {
                        eq('id', Constants.TYPE_CLERK)
                    }
                }

                eq('company', company)
                eq('deleted', 0)
            }
            order('id', 'desc')
        }
    }
    
    def getCurrencies() {
        def currencies = new CurrencyBL().getCurrencies(session['language_id'].toInteger(), session['company_id'].toInteger())
        return currencies.findAll { it.inUse }
    }
    
    def getCompany() {
        CompanyDTO.get(session['company_id'])
    }
}
