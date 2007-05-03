/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/

package com.sapienter.jbilling.client.util;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskParameterDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskSession;

public class GenericMaintainAction {
    private ActionMapping mapping = null;
    private ActionServlet servlet = null;
    private HttpServletRequest request = null;
    private ActionErrors errors = null;
    private ActionMessages messages = null;
    private Logger log = null;
    private HttpSession session = null;
    private DynaValidatorForm myForm = null;
    private String action = null;
    private String mode = null;
    private EJBObject remoteSession = null;
    private String formName = null;
    // handy variables
    private Integer entityId = null;
    private Integer executorId = null;

    public GenericMaintainAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request) { 
        this.mapping = mapping;
        this.request = request;
        log = Logger.getLogger(GenericMaintainAction.class);
        errors = new ActionErrors();
        messages = new ActionMessages();
        session = request.getSession(false);
        myForm = (DynaValidatorForm) form;
    }  
    
    public GenericMaintainAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response,
            ActionServlet servlet, EJBObject remoteSession,
            String formName) 
            throws Exception {
        this.mapping = mapping;
        this.request = request;
        this.remoteSession = remoteSession;
        this.servlet = servlet;
        log = Logger.getLogger(GenericMaintainAction.class);
        errors = new ActionErrors();
        messages = new ActionMessages();
        session = request.getSession(false);
        action = request.getParameter("action");
        mode = request.getParameter("mode");
        this.formName = formName;
        
        if (action == null) {
            throw new Exception("action has to be present in the request");
        }
        if (mode == null || mode.trim().length() == 0) {
            throw new Exception("mode has to be present");
        }
        if (formName == null || formName.trim().length() == 0) {
            throw new Exception("formName has to be present");
        }
        
        // NOT USED ANYMORE
        // selectedId = (Integer) session.getAttribute(Constants.SESSION_LIST_ID_SELECTED);
        // languageId = (Integer) session.getAttribute(Constants.SESSION_LANGUAGE);
        executorId = (Integer) session.getAttribute(
                Constants.SESSION_LOGGED_USER_ID);
        entityId = (Integer) session.getAttribute(
                Constants.SESSION_ENTITY_ID_KEY);
        myForm = (DynaValidatorForm) form;
    }  
    
    public ActionForward process() {
        String forward = null;
        
        log.debug("processing action : " + action);
        try {
            if (action.equals("edit")) {
                try {
                    String reset = (String) myForm.get("reset");
                    if (reset != null && reset.length() > 0) {
                        forward = reset();
                    }
                } catch (IllegalArgumentException e) {
                }
                
                if (forward == null) {
                    forward = edit();
                } 
            } else if(action.equals("setup")) {
                forward = setup();
            } else if(action.equals("delete")) {
                forward = delete();
            } else {
                log.error("Invalid action:" + action);
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("all.internal"));
            }
        } catch (Exception e) {
            log.error("Exception ", e);
            errors.add(ActionErrors.GLOBAL_ERROR,
                   new ActionError("all.internal"));
        }

        // Remove any error messages attribute if none are required
        if ((errors == null) || errors.isEmpty()) {
            request.removeAttribute(Globals.ERROR_KEY);
        } else {
            // Save the error messages we need
            request.setAttribute(Globals.ERROR_KEY, errors);
        }
        
        // Remove any messages attribute if none are required
        if ((messages == null) || messages.isEmpty()) {
            request.removeAttribute(Globals.MESSAGE_KEY);
        } else {
            // Save the messages we need
            request.setAttribute(Globals.MESSAGE_KEY, messages);
        }
        
        log.debug("forwarding to " + mode + "_" + forward);
        return mapping.findForward(mode + "_" + forward);
    }
    
    private String edit() throws SessionInternalError, RemoteException {
        String retValue = "edit";
        String messageKey = null;

        // create a dto with the info from the form and call
        // the remote session
        PluggableTaskDTOEx taskDto = null;

        // do the validation, before moving any info to the dto
        errors = new ActionErrors(myForm.validate(mapping, request));
        
        if (!errors.isEmpty()) {
            return(retValue);
        }                
        
        if (mode.equals("parameter")) { /// for pluggable task parameters
            taskDto = (PluggableTaskDTOEx) session.getAttribute(
                    Constants.SESSION_PLUGGABLE_TASK_DTO);
            String values[] = (String[]) myForm.get("value");
            String names[] = (String[]) myForm.get("name");
            
            for (int f = 0; f < values.length; f++) {
                PluggableTaskParameterDTOEx parameter = 
                        (PluggableTaskParameterDTOEx) taskDto.getParameters()
                            .get(f);
                parameter.setValue(values[f]);
                try {
                    parameter.expandValue();
                } catch (NumberFormatException e) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("task.parameter.prompt.invalid", 
                                names[f]));
                }
            }       
        } else {
            throw new SessionInternalError("mode is not supported:" + mode);
        }

        // some errors could be added during the form->dto copy
        if (!errors.isEmpty()) {
            return(retValue);
        }                

        // if here the validation was successfull, procede to modify the server
        // information
        if (((String) myForm.get("create")).length() > 0) {
            retValue = "create";
        } else { // this is then an update
            retValue = "list";
            if (mode.equals("parameter")) { /// for pluggable task parameters
                ((PluggableTaskSession) remoteSession).updateParameters(
                        executorId, taskDto);
                messageKey = "task.parameter.update.done";
                retValue = "edit";
            }
        }
        
        messages.add(ActionMessages.GLOBAL_MESSAGE, 
                new ActionMessage(messageKey));

        // remove a possible list so there's no old cached list
        session.removeAttribute(Constants.SESSION_LIST_KEY + mode);
        

        if (retValue.equals("list")) {
            // remove the form from the session, otherwise it might show up in a later
            session.removeAttribute(formName);
        }
        
        return retValue;
    }
    
    private String setup() throws SessionInternalError, RemoteException {
        String retValue = null;
        // let's create the form bean and initialized it with the
        // data from the database
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request,
                servlet.getServletContext());
        myForm = (DynaValidatorForm) RequestUtils.createActionForm(
                request, mapping, moduleConfig, servlet);
                
        retValue = "edit";
                
        if (mode.equals("parameter")) { /// for pluggable task parameters
            Integer type = null;
            if (request.getParameter("type").equals("notification")) {
                type = PluggableTaskDTOEx.TYPE_EMAIL;
            }
            PluggableTaskDTOEx dto = ((PluggableTaskSession) remoteSession).
                    getDTO(type, entityId);
            // show the values in the form
            String names[] = new String[dto.getParameters().size()];
            String values[] = new String[dto.getParameters().size()];
            for (int f = 0; f < dto.getParameters().size(); f++) {
                PluggableTaskParameterDTOEx parameter = 
                        (PluggableTaskParameterDTOEx) dto.getParameters().
                                get(f);
                names[f] = parameter.getName();
                values[f] = parameter.getValue();
            }
            myForm.set("name", names);
            myForm.set("value", values);
            // this will be needed for the update                    
            session.setAttribute(Constants.SESSION_PLUGGABLE_TASK_DTO, dto);
        } else {
            throw new SessionInternalError("mode is not supported:" + mode);
        }
        
        log.debug("setup mode=" + mode + " form name=" + formName + 
                " dyna=" + myForm);
                
        session.setAttribute(formName, myForm);
        
        return retValue;
    }
    
    private String delete() throws SessionInternalError, RemoteException {
        session.removeAttribute(formName); 
        String retValue = "deleted";
        
        // remove a possible list so there's no old cached list
        session.removeAttribute(Constants.SESSION_LIST_KEY + mode);
        
        return retValue;
    }
    
    /*
     * 
     */
    private String reset() throws SessionInternalError {
        String retValue = "edit";
        myForm.initialize(mapping);
        return retValue;
    }
    
}
