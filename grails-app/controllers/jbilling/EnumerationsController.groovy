package jbilling

import java.util.logging.Logger;

import com.caucho.hessian.io.EnumerationSerializer;
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.util.EnumerationBL;
import com.sapienter.jbilling.server.util.db.LanguageDTO
import com.sapienter.jbilling.server.util.db.EnumerationDTO
import com.sapienter.jbilling.server.util.db.EnumerationValueDTO

class EnumerationsController {

    static pagination = [ max: 10, offset: 0 ]

    def webServicesSession
    def viewUtils
    def recentItemService
    def breadcrumbService

    def index = {
        redirect action: list, params: params
    }
    
    def list = {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        def enums = EnumerationDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            eq('entity', new CompanyDTO(session['company_id']))
            order('name', 'asc')
        }

        def selected = params.id ? EnumerationDTO.get(params.int("id")) : null
        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int("id"))
        
        if (params.applyFilter) {
            render template: 'enumerations', model: [ enumerations: enums, selected: selected ]
        } else {
            render view: 'list', model: [ enumerations: enums, selected: selected ]
        }

    }

    /**
    * Shows details of the selected Enumeration.
    */
   def show = {
       def selected = EnumerationDTO.get(params.int('id'))
       breadcrumbService.addBreadcrumb(controllerName, 'list', null, params.int('id'))
       render template: 'show', model: [ selected: selected]
   }

   def delete = {
       if (params.id) {
           def enumer= EnumerationDTO.get(params.int('id'))
           log.debug "found enumeration ${enumer}"
           enumer?.delete()
           log.debug("Deleted Enumeration ${params.id}.")
       }

       flash.message = 'enumeration.deleted'
       flash.args = [ params.id ]

       // render the partial list
       params.applyFilter = true
       redirect action: 'list'
   }
   
   def edit = {
       def enumeration= params.id ? EnumerationDTO.get(params.int('id')) : new EnumerationDTO()
       def crumbName = params.id ? 'update' : 'create'
       def crumbDescription = params.id ? enumeration?.getName() : null
       breadcrumbService.addBreadcrumb(controllerName, actionName, crumbName, params.int('id'), crumbDescription)
       
       [ enumeration: enumeration]
   }

   def save = {
       
       def enumeration = new EnumerationDTO(params);
       log.debug "Enumeration: ${enumeration}"
       
       if (!enumeration.name) {
           log.debug "Validation error: enumeration name is missing."
           flash.error = 'enumeration.name.empty'
           render view: 'edit', model: [enumeration: enumeration]
           return
       } else {
           def var= EnumerationDTO.findByName(enumeration.name)
           if (var) {
               log.debug "Validation error: enumeration name already exists."
               flash.error = 'enumeration.name.exists'
               render view: 'edit', model: [enumeration: enumeration]
               return
           }
       }
       
       log.debug "enumeration values.size = ${enumeration.values.size}"
       for (def obj: enumeration.values) {
           log.debug "ValueDTO: ${obj}"
           if (!obj.value) {
               log.debug "Validation error: missing Enumeration value."
               flash.error = 'enumeration.value.missing'
               render view: 'edit', model: [enumeration: enumeration]
               return
           }
           obj.enumeration=enumeration
       }
       
//       enumeration.values.each {
//       }

       enumeration.setEntity(new CompanyDTO(session['company_id']));
       EnumerationBL enumerationService= new EnumerationBL();
       // save or update
       if (!enumeration.id || enumeration.id == 0) {
           log.debug("saving new enumeration ${enumeration}")
           enumeration.id = enumerationService.create(enumeration)

           flash.message = 'enumeration.created'
           flash.args = [ enumeration.name ]

       } else {
           log.debug("updating enumeration ${enumeration.id}")

           enumerationService.set(enumeration.id)
           enumerationService.update(enumeration)

           flash.message = 'enumeration.updated'
           flash.args = [ enumeration.name ]
       }

       chain action: 'list', params: [ id: enumeration.id ]
   }
   
}
