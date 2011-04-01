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
import com.sapienter.jbilling.server.process.AgeingWS;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.client.util.Constants
import com.sapienter.jbilling.server.util.db.PreferenceDTO
import com.sapienter.jbilling.server.util.db.PreferenceTypeDTO;
 
/**
 * ConfigurationController 
 *
 * @author Brian Cowdery
 * @since 03-Jan-2011
 */
@Secured(['isAuthenticated()'])
class ConfigController {

    def breadcrumbService
	def webServicesSession
	def viewUtils
	def userSession
	
    def index = {
        def preferenceTypes = PreferenceTypeDTO.list()

        breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)

        [ preferenceTypes : preferenceTypes ]
    }

    def show = {
        def selected = PreferenceTypeDTO.get(params.int('id'))

        render template: 'show', model: [ selected : selected ]
    }


	
	def aging = {
		log.debug "config.aging ${session['language_id']}"
		AgeingWS[] array= webServicesSession.getAgeingConfiguration(session['language_id'] as Integer)
		breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
		def gracePeriod= userSession.getEntityPreference(session['company_id'] as Integer, Constants.PREFERENCE_GRACE_PERIOD)
		[ageingSteps: array, gracePeriod:gracePeriod]
	}
	
	def saveAging = {
		
		def cnt = params.recCnt.toInteger()
		log.debug "Records Count: ${cnt}"
		
		AgeingWS[] array= new AgeingWS[cnt]
		for (int i=0; i < cnt; i++) {
			log.debug "${params['obj[' + i + '].statusId']}"
			AgeingWS ws= new AgeingWS()
			bindData(ws, params["obj["+i+"]"])
			array[i]= ws
		}
		
		for (AgeingWS dto: array) { 
			log.debug "Printing: ${dto.toString()}"
		}
		try {
			webServicesSession.saveAgeingConfiguration(array, params.int('gracePeriod'), session['language_id'] as Integer)
			flash.message= 'config.ageing.updated'
		} catch (SessionInternalError e){
			viewUtils.resolveException(flash, session.locale, e);
		} catch (Exception e) {
			log.error e.getMessage()
			flash.error = 'config.error.saving.ageing'
		}
		redirect (action: 'aging')
	}

	def periods = {
		redirect controller: 'orderPeriod', action: 'list'
	}
		
	def mediation = {
		redirect controller: 'mediationConfig', action: 'list'
	}
}
