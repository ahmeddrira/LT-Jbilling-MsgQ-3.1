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

package com.sapienter.jbilling.client.task;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.UpdateOnlyCrudActionBase;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskParameterDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskSession;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskSessionHome;

public class MaintainAction extends
		UpdateOnlyCrudActionBase<PluggableTaskDTOEx> {

	private static final String FORM_PARAMETER = "parameter";
	private static final String MESSAGE_UPDATED = "task.parameter.update.done";
	private static final String FORWARD_EDIT = "parameter_edit";
	
	private final PluggableTaskSession mySession;

	public MaintainAction() {
		super(FORM_PARAMETER, "pluggable task parameters",
				FORWARD_EDIT);

		try {
			JNDILookup EJBFactory = JNDILookup.getFactory(false);
			PluggableTaskSessionHome pluggableTaskHome = (PluggableTaskSessionHome) EJBFactory
					.lookUpHome(PluggableTaskSessionHome.class,
							PluggableTaskSessionHome.JNDI_NAME);
			mySession = pluggableTaskHome.create();
		} catch (Exception e) {
			throw new SessionInternalError(
					"Initializing pluggable task parameters CRUD action: " + e.getMessage());

		}
	}

	@Override
	protected PluggableTaskDTOEx doEditFormToDTO() throws RemoteException {
		PluggableTaskDTOEx result = (PluggableTaskDTOEx) session
				.getAttribute(Constants.SESSION_PLUGGABLE_TASK_DTO);
		String values[] = (String[]) myForm.get("value");
		String names[] = (String[]) myForm.get("name");

		List<PluggableTaskParameterDTOEx> allParams = getParamsImpl(result);
		for (int f = 0; f < values.length; f++) {
			PluggableTaskParameterDTOEx next = allParams.get(f);
			next.setValue(values[f]);
			try {
				next.expandValue();
			} catch (NumberFormatException e) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"task.parameter.prompt.invalid", names[f]));
			}
		}
		return result;
	}
	
	@Override
	protected ForwardAndMessage doUpdate(PluggableTaskDTOEx dto) throws RemoteException {
        mySession.updateParameters(executorId, dto);
        return getForwardEdit(MESSAGE_UPDATED);
	}
	
	@Override
	protected ForwardAndMessage doSetup() throws RemoteException {
        Integer type = null;
        if (request.getParameter("type").equals("notification")) {
            type = PluggableTaskDTOEx.TYPE_EMAIL;
        }
        PluggableTaskDTOEx dto = mySession.getDTO(type, entityId);
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
        return getForwardEdit();
	}
	
	protected boolean isCancelled(HttpServletRequest request) {
		return !request.getParameter("mode").equals("setup")
				|| super.isCancelled(request);
	}
	
	@SuppressWarnings("unchecked")
	private Vector<PluggableTaskParameterDTOEx> getParamsImpl(PluggableTaskDTOEx dto){
		return dto.getParameters();
	}


}
