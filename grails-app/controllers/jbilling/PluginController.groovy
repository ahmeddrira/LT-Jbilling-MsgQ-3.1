package jbilling

import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDAS;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeCategoryDTO;
import com.sapienter.jbilling.server.user.UserBL 
import grails.plugins.springsecurity.Secured;

@Secured(['isAuthenticated()'])
class PluginController {
	
    def webServicesSession
	PluggableTaskDAS pluggableTaskDAS
    
    def index = { 
        redirect (action:listCategories)
    }
    
	/*
	 * Lists all the categories. The same for every company
	 */
    def listCategories ={
        UserBL userbl = new UserBL(webServicesSession.getCallerId());
        Integer languageId = session.language_id;
        List categorylist= PluggableTaskTypeCategoryDTO.list();
        log.info "Categories found= " + categorylist?.size()
        render (view:"categories", model:[categories:categorylist])
    }

	/*
	 * This action lists all the plug-ins that belong to a Company and to 
	 * the selected Category
	 */
	def plugins = {
		Integer languageId = session.language_id;
		Integer entityId = session.company_id;
		log.info "entityId=" + entityId
		log.info "selected " + params["id"]
		if (params["id"]) {
			Integer categoryId = Integer.valueOf(params["id"]);
			log.info "Category Id selected=" + categoryId
		
			def lstByCateg= pluggableTaskDAS.findByEntityCategory(entityId, categoryId);
			
			log.info "number of plug-ins=" + lstByCateg.size();
			render template: "plugins", model:[plugins:lstByCateg]
		} else {
			log.info "No Category selected?"
		}
	}
	
	def show = {
		Integer taskId = params.id.toInteger();
		PluggableTaskDTO dto = pluggableTaskDAS.find(taskId);
		Integer languageId = webServicesSession.getCallerLanguageId();
		render template: "show", model:[plugin:dto, languageId:languageId]
	}
}
