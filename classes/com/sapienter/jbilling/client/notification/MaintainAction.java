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

package com.sapienter.jbilling.client.notification;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.CrudActionBase;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.NotificationSession;
import com.sapienter.jbilling.interfaces.NotificationSessionHome;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.MessageSection;

public class MaintainAction extends CrudActionBase<MessageDTO> {

	private static final String FORM = "notification";

	private static final String FIELD_SECTION_CONSTANTS = "sectionNumbers";
	private static final String FIELD_SECTIONS = "sections";
	private static final String FIELD_USE_ME = "chbx_use_flag";
	private static final String FIELD_LANGUAGE = "language";
	
	private static final String FORWARD_EDIT = "notification_edit";
	private static final String MESSAGE_UPDATE_OK = "notification.message.update.done";

	private final NotificationSession myNotificationSession;

	public MaintainAction() {
		super(FORM, "notification");
		log = Logger.getLogger(MaintainAction.class);
		try {
			JNDILookup EJBFactory = JNDILookup.getFactory(false);
			NotificationSessionHome notificationHome = (NotificationSessionHome) EJBFactory
					.lookUpHome(NotificationSessionHome.class,
							NotificationSessionHome.JNDI_NAME);
			myNotificationSession = notificationHome.create();
		} catch (Exception e) {
			throw new SessionInternalError(
					"Initializing notification CRUD action: " + e.getMessage());
		}
	}
	
	@Override
	protected MessageDTO doEditFormToDTO() throws RemoteException {
		MessageDTO dto = new MessageDTO();
        dto.setLanguageId((Integer) myForm.get("language"));
        dto.setTypeId(selectedId);
        dto.setUseFlag((Boolean) myForm.get("chbx_use_flag"));
        // set the sections
        String sections[] = (String[]) myForm.get("sections");
        Integer sectionNumbers[] = (Integer[]) myForm.get("sectionNumbers");
        for (int f = 0; f < sections.length; f++) {
            dto.addSection(new MessageSection(sectionNumbers[f], sections[f]));
            log.debug("adding section:" + f + " "  + sections[f]);
        }
        log.debug("message is " + dto);
        return dto;
	}
	
	@Override
	public String update(Object dtoHolder) {
		if (request.getParameter("reload") != null) {
			// this is just a change of language the requires a reload
			// of the bean
			languageId = (Integer) myForm.get(FIELD_LANGUAGE);
			setup();

			//forward is set inside setup(), don't need to return anything.
			return null;
		}
		return super.update(dtoHolder);
	}
	
	@Override
	protected ForwardAndMessage doUpdate(MessageDTO dto) throws RemoteException {
        myNotificationSession.createUpdate(dto, entityId);
        return new ForwardAndMessage(FORWARD_EDIT, MESSAGE_UPDATE_OK);
	}
	
	@Override
	protected ForwardAndMessage doSetup() throws RemoteException {
        MessageDTO dto = myNotificationSession.getDTO(selectedId, languageId, entityId);
        myForm.set(FIELD_LANGUAGE, languageId);
        myForm.set(FIELD_USE_ME, dto.getUseFlag());
        // now cook the sections for the form's taste
        String sections[] = new String[dto.getContent().length];
        Integer sectionNubmers[] = new Integer[dto.getContent().length];
        for (int f = 0; f < sections.length; f++) {
            sections[f] = dto.getContent()[f].getContent();
            sectionNubmers[f] = dto.getContent()[f].getSection();
        }
        myForm.set(FIELD_SECTIONS, sections);
        myForm.set(FIELD_SECTION_CONSTANTS, sectionNubmers);
        return new ForwardAndMessage(FORWARD_EDIT);
	}
	
	@Override
	protected void resetCachedList() {
		session.removeAttribute(Constants.SESSION_LIST_KEY + FORM);
	}
	
	@Override
	protected ForwardAndMessage doDelete() throws RemoteException {
		throw new UnsupportedOperationException(
				"Set of notification events is fixed. You can not delete it, only switch it off");
	}
	
	@Override
	protected ForwardAndMessage doCreate(MessageDTO dto) throws RemoteException {
		throw new UnsupportedOperationException(
				"Set of notification events is fixed. You can not create it, only switch it on");
	}

	protected boolean isCancelled(HttpServletRequest request) {
		return !request.getParameter("mode").equals("setup");
	}
	
	@Override
	protected void preEdit() {
		super.preEdit();
		setForward(FORWARD_EDIT);
	}

}
