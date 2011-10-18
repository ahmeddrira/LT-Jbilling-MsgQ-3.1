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

import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.mediation.MediationConfigurationWS
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeCategoryDAS
import com.sapienter.jbilling.server.util.Constants
import com.sapienter.jbilling.server.mediation.db.MediationConfiguration

/**
* MediationConfigController
*
* @author Vikas Bodani
* @since 15-Feb-2011
*/

@Secured(["MENU_99"])
class MediationConfigController {

    static pagination = [ max: 10, offset: 0 ]
	
	def webServicesSession
	def viewUtils
	def breadcrumbService

    def index = {
        redirect action: 'list'
    }

    def list = {

        def configurations= webServicesSession.getAllMediationConfigurations()
        
        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))
		
		if (params.applyFilter) {
			//flash.message=flash.message
			render template: 'configs', model:[types: configurations, readers: readers] 
		} else {
        	render view: 'list', model: [types: configurations, readers: readers]
		}
    }
    
    def show = {
        
        def configId= params.int('id')
        
        log.debug "Show config id $params.id"
        
        def config= MediationConfiguration.get(configId)
        
        if ( config.entityId != session['company_id']) {
            flash.error = 'configuration.does.not.exists.for.entity' 
            list()
        }
        
        breadcrumbService.addBreadcrumb(controllerName, actionName, null, configId)
        
        render template: 'show', model: [selected: config]
        
    }
	
    def edit = {
        
        def configId= params.int('id')
        
        def config = configId ? MediationConfiguration.get(configId) : null

        def crumbName = configId ? 'update' : 'create'
        def crumbDescription = params.id ? config?.name : null
        breadcrumbService.addBreadcrumb(controllerName, actionName, crumbName, configId, crumbDescription)
        
        render template: 'edit', model: [ config: config, readers: readers]
    }
    
	def save = {
        
		def ws= new MediationConfigurationWS()
        
		bindData(ws, params)
        
		if ( params.int('id') > 0 ) {
            log.debug "config exists.."
            webServicesSession.updateAllMediationConfigurations([ws])
            flash.message = 'mediation.config.update.success'
		} else {
            log.debug "New config.."
            ws.setCreateDatetime new Date()
            ws.setEntityId webServicesSession.getCallerCompanyId()
            webServicesSession.createMediationConfiguration(ws)
            flash.message = 'mediation.config.create.success'
        }
		
		redirect action: 'list'
	}
	
	def delete = {
        
        try {
            webServicesSession.deleteMediationConfiguration(params.int('id'))
            flash.message = 'mediation.config.delete.success'
        } catch (SessionInternalError e){
            viewUtils.resolveException(flash, session.locale, e);
        } catch (Exception e) {
            log.error e.getMessage()
            flash.error = 'mediation.config.delete.failure'
        }
        
        // render list
        params.applyFilter = true
		list()
        
	}
	
    def getReaders() {
        
        def readers= new ArrayList<PluggableTaskDTO>()
        for(PluggableTaskDTO reader: PluggableTaskDTO.list()) {
            if (reader?.type?.category?.getId() == Constants.PLUGGABLE_TASK_MEDIATION_READER) {
                readers.add reader
            }
        }
        return readers
    }
}
