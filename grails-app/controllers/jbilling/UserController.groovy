package jbilling

import com.sapienter.jbilling.client.ViewUtils
import com.sapienter.jbilling.common.Constants
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.entity.AchDTO
import com.sapienter.jbilling.server.entity.CreditCardDTO
import com.sapienter.jbilling.server.user.ContactWS
import com.sapienter.jbilling.server.user.UserWS
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.user.db.UserDTO
import com.sapienter.jbilling.server.user.db.UserStatusDAS
import com.sapienter.jbilling.server.util.IWebServicesSessionBean
import grails.plugins.springsecurity.Secured
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.user.EntityBL
import com.sapienter.jbilling.client.authentication.JBillingPasswordEncoder
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

@Secured(['isAuthenticated()'])
class UserController {

    static pagination = [ max: 10, offset: 0 ]

    IWebServicesSessionBean webServicesSession
    ViewUtils viewUtils
    PasswordEncoder passwordEncoder

    def filterService
    def recentItemService
    def breadcrumbService
    
    def index = {
        redirect action: list, params: params
    }

    /**
     * Get a list of users and render the list page. If the "applyFilters" parameter is given, the
     * partial "_users.gsp" template will be rendered instead of the complete user list.
     */
    def list = {
        def filters = filterService.getFilters(FilterType.CUSTOMER, params)
        def statuses = new UserStatusDAS().findAll()

        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        def users = UserDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            and {
                filters.each { filter ->
                    if (filter.value) {
                        switch (filter.constraintType) {
                            case FilterConstraint.EQ:
                                eq(filter.field, filter.value)
                                break

                            case FilterConstraint.LIKE:
                                like(filter.field, filter.stringValue)
                                break

                            case FilterConstraint.DATE_BETWEEN:
                                between(filter.field, filter.startDateValue, filter.endDateValue)
                                break

                            case FilterConstraint.STATUS:
                                eq("userStatus", statuses.find{ it.id == filter.integerValue })
                                break
                        }
                    }
                }
				roles {
					eq('id', Constants.TYPE_CUSTOMER)
				}
                eq('company', new CompanyDTO(session['company_id']))
                eq('deleted', 0)
            }
            order('id', 'asc')
        }

        def selected = params.id ? UserDTO.get(params.int("id")) : null

        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))

        if (params.applyFilter) {
            render template: 'users', model: [users: users, selected: selected, statuses: statuses, filters: filters ]
        } else {
            [ users: users, selected: selected, statuses: statuses, filters: filters ]
        }
    }

    /**
     * Show details of the selected user. By default, this action renders the "_details.gsp" template.
     * When rendering for an AJAX request the template defined by the "template" parameter will be rendered.
     */
    def show = {
        UserDTO user = UserDTO.get(params.int('id'))
        def revenue = webServicesSession.getTotalRevenueByUser(params.int('id'))

        recentItemService.addRecentItem(params.int('id'), RecentItemType.CUSTOMER)
        breadcrumbService.addBreadcrumb(controllerName, 'list', params.template ?: null, params.int('id'))

        render template: params.template ?: 'show', model: [ selected: user, revenue: revenue ]
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
            customer {
                parent {
                    eq('baseUser.id', params.int('id'))
                }
            }
        }

        def parent = UserDTO.get(params.int('id'))
        System.out.println("Parent id: " + params.id + "  = " + parent)

        render template: 'users', model: [ users: children, parent: parent ]
    }

    /**
     * Updates the notes for the given user id.
     */
    def saveNotes = {
        webServicesSession.saveCustomerNotes(params.int('id'), params.notes)

        def user = UserDTO.get(params.int('id'))
        render template: 'show', model: [ selected: user ]
    }

    /**
     * Delete the given user id.
     */
    def delete = {
        if (params.id) {
            webServicesSession.deleteUser(params.int('id'))

            log.debug("Deleted user ${params.id}.")

            flash.message = 'customer.deleted'
            flash.args = [ params.id ]
        }

        // render the partial user list
        params.applyFilter = true
        list()
    }

    /**
     * Get the user to be edited and show the "edit.gsp" view. If no ID is given this view
     * will allow creation of a new user.
     */
    def edit = {
        def user = params.id ? webServicesSession.getUserWS(params.int('id')) : null
        def contacts = user ? webServicesSession.getUserContactsWS(user.userId) : null
        def parent = params.parentId ? webServicesSession.getUserWS(params.int('parentId')) : null
        def company = CompanyDTO.get(session['company_id'])

        breadcrumbService.addBreadcrumb(controllerName, actionName, params.id ? 'update' : 'create', params.int('id'))

        [ user: user, contacts: contacts, parent: parent, company: company, currencies: currencies ]
    }

    /**
     * Validate and save a user.
     */
    def save = {
        def user = new UserWS()
        user.setMainRoleId(Constants.TYPE_CUSTOMER)
        bindData(user, params, 'user')

        def company = CompanyDTO.get(session['company_id'])
        def contactTypes = company.contactTypes
        def primaryContactTypeId = params.int('primaryContactTypeId')

        // bind primary user contact and custom contact fields
        def contact = new ContactWS()
        bindData(contact, params, 'contact-' + params.primaryContactTypeId)
        contact.fieldIDs = new Integer[params.contactField.size()]
        contact.fieldValues = new Integer[params.contactField.size()]
        params.contactField.eachWithIndex { id, value, i ->
            contact.fieldIDs[i] = id.toInteger()
            contact.fieldValues[i] = value
        }
        user.setContact(contact)

        log.debug("Primary contact: ${contact}")

        // bind secondary contact types
        def contacts = contactTypes.findAll{ it.id != primaryContactTypeId }.collect{
            def otherContact = new ContactWS()
            bindData(otherContact, params, 'contact-' + it.id)
            return otherContact;
        }

        log.debug("Secondary contacts: ${contacts}")

        // bind credit card object if parameters present
        if (params.creditCard.any { key, value -> value }) {
            def creditCard = new CreditCardDTO()
            bindData(creditCard, params, 'creditCard')
            bindExpiryDate(creditCard, params)

            // update credit card only if not obscured
            if (!creditCard.number.startsWith('*'))
                user.setCreditCard(creditCard)
        }

        // bind ach object if parameters present
        if (params.ach.any { key, value -> value }) {
            def ach = new AchDTO()
            bindData(ach, params, 'ach')
            user.setAch(ach)

            log.debug("ACH ${ach}")
        }

        log.debug("Customer ACH ${user.ach}")

        // set automatic payment type
        if (params.creditCardAutoPayment) user.setAutomaticPaymentType(Constants.AUTO_PAYMENT_TYPE_CC)
        if (params.achAutoPayment) user.setAutomaticPaymentType(Constants.AUTO_PAYMENT_TYPE_ACH)

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
            render view: 'edit', model: [ user: user, contacts: contacts, company: company, currencies: currencies ]
            return
        }

        chain action: 'list', params: [ id: user.userId ]
    }

    def bindExpiryDate(CreditCardDTO creditCard, GrailsParameterMap params) {
        Calendar calendar = Calendar.getInstance()
        calendar.clear()
        calendar.set(Calendar.MONTH, params.int('expiryMonth'))
        calendar.set(Calendar.YEAR, params.int('expiryYear'))

        creditCard.expiry = calendar.getTime()
    }

    def getCurrencies() {
        def currencies = new CurrencyBL().getCurrencies(session['language_id'].toInteger(), session['company_id'].toInteger())
        return currencies.findAll { it.inUse }
    }
}
