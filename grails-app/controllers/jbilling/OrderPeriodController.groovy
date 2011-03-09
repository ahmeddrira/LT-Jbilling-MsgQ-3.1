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
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.util.db.LanguageDTO
import com.sapienter.jbilling.server.util.db.InternationalDescription
import com.sapienter.jbilling.server.util.InternationalDescriptionWS
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO

/**
 * OrderPeriodController 
 *
 * @author Vikas Bodani
 * @since 09-Mar-2011
 */


@Secured(['isAuthenticated()'])
class OrderPeriodController {

	static pagination = [ max: 10, offset: 0 ]
	
	def breadcrumbService
	
    def index = {
        redirect action: list, params: params
    }

    def list = {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset
		
		def periods= OrderPeriodDTO.createCriteria().list(
			max:    params.max,
			offset: params.offset
		) {
			eq('company', new CompanyDTO(session['company_id']))
		}
		
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
		[periods: periods]
	}
	
	def save = {
		def cnt = params.recCnt.toInteger()
		log.debug "Records Count: ${cnt}"
	}
	
}
