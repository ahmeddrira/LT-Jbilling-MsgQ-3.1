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

import java.util.List;

import grails.plugins.springsecurity.Secured
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.util.db.LanguageDTO
import com.sapienter.jbilling.server.util.db.InternationalDescription
import com.sapienter.jbilling.server.util.InternationalDescriptionWS
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO
import com.sapienter.jbilling.server.order.OrderPeriodWS

import com.sapienter.jbilling.server.payment.tasks.PaymentSageTask.Params;
import com.sapienter.jbilling.server.process.db.PeriodUnitDTO

/**
 * OrderPeriodController 
 *
 * @author Vikas Bodani
 * @since 09-Mar-2011
 */


@Secured(["isAuthenticated()", "MENU_99"])
class OrderPeriodController {

	static pagination = [ max: 10, offset: 0 ]
	def breadcrumbService
	def webServicesSession
	def viewUtils
	
    def index = {
        redirect action: list, params: params
    }

    def list = {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset
		
		def periods= getPeriodsForEntity()
		
        breadcrumbService.addBreadcrumb(controllerName, actionName, null, null)
		
		if (params.applyFilter) {
            render template: 'periods', model: [ periods: periods ]
		} else {
			render view: 'list', model:[periods: periods]
		}
	}
	
    def show = {
        def period = OrderPeriodDTO.get(params.int('id'))

        breadcrumbService.addBreadcrumb(controllerName, 'list', null, period.id, period.getDescription(session['language_id']))

        render template: 'show', model: [ selected: period ]
    }
    
	def getPeriodsForEntity () {
		return OrderPeriodDTO.createCriteria().list(
			max:    params.max,
			offset: params.offset
		) {
			eq('company', new CompanyDTO(session['company_id']))
			order("id", "desc")
		}
	}
	
    def edit = {
        def period = params.id ? OrderPeriodDTO.get(params.int('id')) : null

        def crumbName = params.id ? 'update' : 'create'
        def crumbDescription = params.id ? period?.getDescription(session['language_id']) : null
        
        breadcrumbService.addBreadcrumb(controllerName, actionName, crumbName, params.int('id'), crumbDescription)

        def periodUnits = PeriodUnitDTO.list()
        
        render template: 'edit', model: [ period: period, periodUnits: periodUnits ]
    }
    
	def save = {
        
        OrderPeriodWS ws= new OrderPeriodWS()
        bindData(ws, params)
        
		log.debug ws
		InternationalDescriptionWS descr=
			new InternationalDescriptionWS(session['language_id'] as Integer, params.description)
        log.debug descr
		ws.descriptions.add descr
		ws.setEntityId(session['company_id'].toInteger())
        log.debug ws
        
		try {
			boolean retVal= webServicesSession.updateOrCreateOrderPeriod(ws);
			flash.message= 'config.periods.updated'
		} catch (SessionInternalError e){
			viewUtils.resolveException(flash, session.locale, e);
		} catch (Exception e) {
			log.error e.getMessage()
			flash.error = 'config.periods.saving.error'
		}
		redirect (action: 'list')
		
	}
	
	def delete = {
		log.debug 'delete called on ' + params.id
        if (params.id) {
            def period= OrderPeriodDTO.get(params.int('id'))
            if (period) {
                try {
                    boolean retVal= webServicesSession.deleteOrderPeriod(params.id?.toInteger());
                    if (retVal) { 
                        flash.message= 'config.periods.delete.success'
                        flash.args = [ params.id ]
                    } else {
                        flash.info = 'config.periods.delete.failure'
                    }
                } catch (SessionInternalError e){
                    viewUtils.resolveException(flash, session.locale, e);
                } catch (Exception e) {
                    log.error e.getMessage()
                    flash.error = 'config.periods.delete.error'
                }
            }
        }

        // render the period list
        params.applyFilter = true
        list()
	}
	
}
