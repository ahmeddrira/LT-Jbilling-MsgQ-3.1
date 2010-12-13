package jbilling

import com.sapienter.jbilling.client.ViewUtils;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask 
import com.sapienter.jbilling.server.pluggableTask.admin.ParameterDescription;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDAS;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeCategoryDAS 
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeCategoryDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeDAS 
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskTypeDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskWS;
import com.sapienter.jbilling.server.user.UserBL 
import com.sapienter.jbilling.server.util.WebServicesSessionSpringBean;
import com.sapienter.jbilling.common.SessionInternalError;

import org.springframework.context.support.ReloadableResourceBundleMessageSource 
import org.springframework.security.access.annotation.Secured 

@Secured(['isAuthenticated()'])
class PluginController {
    
    WebServicesSessionSpringBean webServicesSession;
    ReloadableResourceBundleMessageSource messageSource
    PluggableTaskDAS pluggableTaskDAS
    ViewUtils viewUtils
    
    def index = { 
        redirect (action:listCategories)
    }
    
    /*
     * Lists all the categories. The same for every company
     */
    def listCategories ={
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
            // add the category id to the session, so the 'create' button can know 
            // which category to create for
            session.selected_category_id = categoryId;
            // put the category in the session, it comes handy later
            PluggableTaskTypeCategoryDTO category = new PluggableTaskTypeCategoryDAS().find(
                    session.selected_category_id);
            session.selected_category=category; // comes handy later
        
            // show the list of the plug-ins
            render template: "plugins", model:[plugins:lstByCateg]
        } else {
            log.error "No Category selected?"
        }
    }
    
    def show = {
        Integer taskId = params.id.toInteger();
        PluggableTaskDTO dto = pluggableTaskDAS.find(taskId);
        render template: "show", model:[plugin:dto]
    }
    
    def showForm = {
        // find out the category name
        PluggableTaskTypeCategoryDTO category = session.selected_category.attach();
        
        List<PluggableTaskTypeDTO> typesList = new PluggableTaskTypeDAS().findAllByCategory(category.getId());
        // show the form with the description
        render (view:"form", model:
                [description:category.getDescription(session.language_id),
                 types:typesList, parametersDesc : getDescriptions(typesList.get(0).getId())])
    }
    
    /*
     * This is called when a new type is picked from the drop down list of plug-in types (classes)
     * and the parameters need to be re-rendered
     */
    def getTypeParametersDescriptions = {
        log.info "Getting parameters for plug-in type " + params.typeId;
        
        render template:"formParameters", model:[parametersDesc : getDescriptions(params.typeId as Integer) ]
    }
    
    private List<ParameterDescription> getDescriptions(Integer typeId) {
        PluggableTaskTypeDTO type = new PluggableTaskTypeDAS().find(typeId);
        // create a new class to extract the parameters descriptions
        PluggableTask thisTask = PluggableTaskManager.getInstance(type.getClassName(), 
            type.getCategory().getInterfaceName());
        return thisTask.getParameterDescriptions();
    }
    
    def save = {
        // Create a new object from the form
        PluggableTaskWS newTask = new PluggableTaskWS();
        bindData(newTask, params);
        for(String key: params.keySet()) { // manually bind the plug-in parameters
            if (key.startsWith("plg-parm-")) {
                newTask.getParameters().put(key.substring(9),params.get(key));
            }
        }
        
        // save
        Locale locale = session.locale;
        try {
            log.info "now saving " + newTask + " by " + session.user_id;
            Integer pluginId;
            if (newTask.getId() == null || newTask.getId() == 0) {
                pluginId = webServicesSession.createPlugin(session.user_id as Integer, newTask);
            	pluggableTaskDAS.invalidateCache(); // or the list won't have the new plug-in
            
            	// the message
            	flash.message = messageSource.getMessage("plugins.create.new_plugin_saved", [pluginId].toArray(), locale);
            } else { 
                // it is an update
                webServicesSession.updatePlugin(session.user_id, newTask);
            	flash.message = messageSource.getMessage("plugins.create.plugin_updated", [newTask.getId()].toArray(), locale);
                pluginId = newTask.getId();
            }
            
            // forward to the list of plug-in types and the new plug-in selected
            showListAndPlugin(pluginId);
        } catch(SessionInternalError e) {
            // process the exception so the error messages from validation are
            // put in the flash
            viewUtils.resolveExceptionForValidation(flash, locale, e);
            PluggableTaskTypeCategoryDTO category = session.selected_category;
            
            // render the form again, with all the data
            render (view:"form", model:
                [description: category.getDescription(session.language_id),
                 types: new PluggableTaskTypeDAS().findAllByCategory(category.getId()),
                 pluginws: newTask,
                 parametersDesc : getDescriptions(newTask.getTypeId())])
        }
    }
    
    private void showListAndPlugin(Integer pluginId) {
        PluggableTaskDTO dto = new PluggableTaskDAS().find(pluginId);
        render (view: "showListAndPlugin", model:
            [plugin: dto,
             plugins: pluggableTaskDAS.findByEntityCategory(session.company_id, dto.getType().getCategory().getId())]);
    }
    
    def cancel = {
        flash.message = messageSource.getMessage("plugins.edit.canceled",null, session.locale);
        if ((params.plugin_id as Integer) > 0 ) {
            // go the the list with the plug selected
            showListAndPlugin(params.plugin_id as Integer);
        } else {
            // it was creating a new one
            redirect (action:listCategories)
        }
    }
    
    def edit = {
        PluggableTaskDTO dto =  pluggableTaskDAS.find(params.id as Integer);
        if (dto != null) {
            PluggableTaskTypeCategoryDTO category = session.selected_category.attach();
            render (view:"form", model:
                [description: category.getDescription(session.language_id),
                 types: new PluggableTaskTypeDAS().findAllByCategory(category.getId()),
                 pluginws: new PluggableTaskWS(dto),
                 parametersDesc : getDescriptions(dto.getType().getId())])
        } else {
            flash.error="plugins.error.invalid_id";
            render (view:"categories");
        }
    }
    
    def delete = {
        Integer id = params.id as Integer;
        webServicesSession.deletePlugin(session.user_id, id);
        pluggableTaskDAS.invalidateCache(); // or the list will still show the deleted plug-in
        flash.message = messageSource.getMessage("plugins.delete.done",[id].toArray(), session.locale);
        redirect (action:listCategories)
    }
}
