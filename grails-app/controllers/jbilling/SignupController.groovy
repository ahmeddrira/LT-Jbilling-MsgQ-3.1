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

import com.sapienter.jbilling.server.user.contact.db.ContactDTO
import com.sapienter.jbilling.server.user.db.UserDTO
import com.sapienter.jbilling.server.util.db.CurrencyDTO
import com.sapienter.jbilling.server.util.db.LanguageDTO
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.util.Constants
import com.sapienter.jbilling.server.user.db.UserStatusDAS
import com.sapienter.jbilling.server.user.UserDTOEx
import com.sapienter.jbilling.server.user.db.SubscriberStatusDAS
import com.sapienter.jbilling.server.user.permisson.db.RoleDTO
import com.sapienter.jbilling.server.user.contact.db.ContactTypeDTO
import com.sapienter.jbilling.server.user.contact.db.ContactMapDTO
import com.sapienter.jbilling.server.util.db.JbillingTable

/**
 * SignupController 
 *
 * @author Brian Cowdery
 * @since 10/03/11
 */
class SignupController {

    def index = {
    }

    def save = {
        // validate admin user password
        if (!params['user.password']) {
            flash.error = 'customer.create.without.password'
            render view: 'index'
            return
        }

        if (params['user.password'] != params.verifiedPassword) {
            flash.error = 'customer.passwords.dont.match'
            render view: 'index'
            return
        }


        /*
            Create the new entity, root user and basic contact information
         */

        // create company
        def language = LanguageDTO.get(params.languageId)
        def currency = CurrencyDTO.get(params.currencyId)
        def company = createCompany(language, currency)
        log.debug("saved company ${company}")

        // root admin user
        def user = createUser(language, currency, company)
        log.debug("saved user ${user}")

        // primary contact type
        def primaryContactType = createPrimaryContactType(language, company)
        log.debug("saved primary contact type ${primaryContactType}")

        // contact information
        def userContact = createUserContact(user, primaryContactType)
        log.debug("saved user contact ${userContact}")

        def entityContact = createEntityContact(company)
        log.debug("saved entity contact ${entityContact}")


        flash.message = 'signup.successful'
        flash.args = [ entityContact.organizationName, user.userName ]
        render view: 'login', action: 'auth', model: [ userName: user.userName, companyId: company.id ]
    }


    def createCompany(language, currency) {
        def company = new CompanyDTO(
                description: params['contact.organizationName'],
                createDatetime: new Date(),
                language: language,
                currency: currency
        ).save()

        return company
    }

    def createUser(language, currency, company) {
        def user = new UserDTO()
        bindData(user, params, 'user')
        user.deleted = 0
        user.userStatus = new UserStatusDAS().find(UserDTOEx.STATUS_ACTIVE)
        user.subscriberStatus = new SubscriberStatusDAS().find(UserDTOEx.SUBSCRIBER_ACTIVE)
        user.language = language
        user.currency = currency
        user.company = company
        user.createDatetime = new Date()
        user.roles << RoleDTO.get(Constants.TYPE_ROOT)
        user.save()

        return user
    }

    def createPrimaryContactType(language, company) {
        def primaryContactType = new ContactTypeDTO(
                entity: company,
                isPrimary: 1,
        ).save()

        primaryContactType.setDescription("Primary", language.id)

        return primaryContactType
    }

    def createUserContact(user, primaryContactType) {
        def userContact = new ContactDTO()
        bindData(userContact, params, 'contact')
        userContact.deleted = 0
        userContact.createDate = new Date()
        userContact.userId = user.id
        userContact.save()

        // map contact to the user table
        // map contact to the primary contact type
        new ContactMapDTO(
                jbillingTable: JbillingTable.findByName(Constants.TABLE_BASE_USER),
                contactType: primaryContactType,
                contact: userContact
        ).save()

        return userContact
    }

    def createEntityContact(company) {
        def entityContact = new ContactDTO()
        bindData(entityContact, params, 'contact')
        entityContact.deleted = 0
        entityContact.createDate = new Date()
        entityContact.save()

        // map contact to the entity table
        // map contact to the base entity contact type
        new ContactMapDTO(
                jbillingTable: JbillingTable.findByName(Constants.TABLE_ENTITY),
                contactType: ContactTypeDTO.get(Constants.ENTITY_CONTACT_TYPE),
                contact: entityContact
        ).save()

        return entityContact
    }
}
