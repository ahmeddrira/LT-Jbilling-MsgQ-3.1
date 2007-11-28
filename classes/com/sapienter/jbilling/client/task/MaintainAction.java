/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
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
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskSession;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskSessionHome;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskParameterDTO;

public class MaintainAction extends
		UpdateOnlyCrudActionBase<PluggableTaskDTO> {

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
	protected PluggableTaskDTO doEditFormToDTO() throws RemoteException {
		PluggableTaskDTO result = (PluggableTaskDTO) session
				.getAttribute(Constants.SESSION_PLUGGABLE_TASK_DTO);
		String values[] = (String[]) myForm.get("value");
		String names[] = (String[]) myForm.get("name");

		List<PluggableTaskParameterDTO> allParams = getParamsImpl(result);
		for (int f = 0; f < values.length; f++) {
			PluggableTaskParameterDTO next = allParams.get(f);
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
	protected ForwardAndMessage doUpdate(PluggableTaskDTO dto) throws RemoteException {
        mySession.updateParameters(executorId, dto);
        return getForwardEdit(MESSAGE_UPDATED);
	}
	
	@Override
	protected ForwardAndMessage doSetup() throws RemoteException {
        Integer type = null;
        if (request.getParameter("type").equals("notification")) {
            type = PluggableTaskDTO.TYPE_EMAIL;
        }
        PluggableTaskDTO dto = mySession.getDTO(type, entityId);
        // show the values in the form
        String names[] = new String[dto.getParameters().size()];
        String values[] = new String[dto.getParameters().size()];
        
        int f = 0;
        for (PluggableTaskParameterDTO parameter : dto.getParameters()) {
            names[f] = parameter.getName();
            values[f] = parameter.getValue();
            f++;
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
	
	private Vector<PluggableTaskParameterDTO> getParamsImpl(PluggableTaskDTO dto){
		return new Vector<PluggableTaskParameterDTO>(dto.getParameters());
	}


}
