/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
 */

package jbilling

import grails.plugins.springsecurity.Secured
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.user.contact.ContactFieldTypeWS
import com.sapienter.jbilling.server.user.contact.db.ContactFieldTypeDTO
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.util.InternationalDescriptionWS
import com.sapienter.jbilling.server.util.db.EnumerationDTO
import com.sapienter.jbilling.server.util.db.LanguageDTO

/**
 * ContactFieldConfigController 
 *
 * @author Vikas Bodani
 * @since 31-Jan-2011
 */


@Secured(["MENU_99"])
class ContactFieldConfigController {
	
	def webServicesSession
	def viewUtils
	def recentItemService
	def breadcrumbService

    def index = {
        redirect action: list, params: params
    }

    def list = {
        def types = ContactFieldTypeDTO.createCriteria().list() {
            eq('entity', new CompanyDTO(session['company_id']))
            order('id', 'asc')
        }

        def selected = params.id ? ContactFieldTypeDTO.get(params.int("id")) : null

        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))

        [ types: types, selected: selected, languages: languages ]
    }
	
    def show = {
        def type = ContactFieldTypeDTO.get(params.int('id'))

        breadcrumbService.addBreadcrumb(controllerName, 'list', null, type.id, type.getDescription(session['language_id']))

        render template: 'show', model: [ selected: type ]
    }
    
    def edit = {
        def type = params.id ? ContactFieldTypeDTO.get(params.int('id')) : null

        def crumbName = params.id ? 'update' : 'create'
        def crumbDescription = params.id ? type?.getDescription(session['language_id']) : null
        
        breadcrumbService.addBreadcrumb(controllerName, actionName, crumbName, params.int('id'), crumbDescription)
        
        def dataTypeList= ['', 'String','Integer', 'Decimal', 'Boolean']
        dataTypeList.addAll(EnumerationDTO.list().collect{it.name})
        
        render template: 'edit', model: [ type: type, dataTypeList: dataTypeList]
    }
    
	def save = {
        
		ContactFieldTypeWS fieldWS= new ContactFieldTypeWS();
        
        bindData(fieldWS, params)
        
        fieldWS.readOnly= params.readOnly ? 1 : 0 
        fieldWS.companyId = session['company_id'].toInteger()
        
        InternationalDescriptionWS descr= 
				new InternationalDescriptionWS(session['language_id'], params.description)
		fieldWS.descriptions.add descr
		
		log.debug "Contact Field WS ${fieldWS}"
		
		try {
			webServicesSession.saveCustomContactField(fieldWS)
		} catch (SessionInternalError e) {
			log.error e
			viewUtils.resolveException(flash, session.locale, e)
		}
		flash.message = 'custom.fields.save.success'
		redirect action: 'list'
	}
	
	def getLanguages() {
		return LanguageDTO.list()
	}
}
