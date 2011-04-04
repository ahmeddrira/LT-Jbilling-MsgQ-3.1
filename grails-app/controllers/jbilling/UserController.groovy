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
}
