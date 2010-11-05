package jbilling

import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeCategoryDTO;
import com.sapienter.jbilling.server.user.UserBL 

class PluginsController {
	
    def webServicesSession
    
    def index = { 
        redirect (action:listCategories)
    }
    
    def listCategories ={
        UserBL userbl = new UserBL(webServicesSession.getCallerId());
        Integer languageId = userbl.getEntity().getLanguageIdField();
        List categorylist= PluggableTaskTypeCategoryDTO.list();
        log.info "Categories found= " + categorylist?.size()
        render (view:"categories", model:[lst:categorylist, languageId:languageId])
    }

}
