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

package com.sapienter.jbilling.client.user;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.user.ContactDTOEx;

public class ContactEditAction extends Action {

    DynaValidatorForm contactForm = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    
        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        Logger log = Logger.getLogger(ContactEditAction.class);
        HttpSession session = request.getSession();
        String forward = "edit";
        // Look up the module configuration information. Later will need it
        // to create the dynamic bean
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request,
                servlet.getServletContext());
        
        Integer userId = (request.getParameter("userId") == null) 
                ? (Integer) session.getAttribute(
                        Constants.SESSION_CONTACT_USER_ID)
                : Integer.valueOf(request.getParameter("userId"));
                        
                        
        String action = request.getParameter("action");

        log.debug("In contact edit action = " + action);

        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            UserSessionHome userHome =
                    (UserSessionHome) EJBFactory.lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);
        
            UserSession myRemoteSession = userHome.create();
            
            contactForm = (DynaValidatorForm) form;
        
            if (action != null && action.equals("setup")) {
                // let's create the form bean and initialized it with the
                // data from the database
                
                fillForm(myRemoteSession.getPrimaryContactDTO(userId), request,
                        mapping, moduleConfig);
                contactForm.set("type", null);
                session.setAttribute("contact", contactForm);
                
                // make sure the user id is now in the session
                // (if called from a request, it'll be lost when submiting)
                session.setAttribute(Constants.SESSION_CONTACT_USER_ID,
                        userId);
            } else if (request.getParameter("reload") != null) {
                Integer type = (Integer) contactForm.get("type");
                ContactDTOEx dbContact;
                try {
                    dbContact = myRemoteSession.getContactDTO(
                            userId, type);
                } catch (FinderException e) {
                    // it is a new one
                    Integer entityId = (Integer) session.getAttribute(
                            Constants.SESSION_ENTITY_ID_KEY);
                    dbContact = myRemoteSession.getVoidContactDTO(entityId);
                }
                fillForm(dbContact, request, mapping, moduleConfig);
                session.setAttribute("contact", contactForm);
            } else { // send the information to the server for update
                
                ContactDTOEx contact = new ContactDTOEx();
                
                Integer type = (Integer) contactForm.get("type");
                contact.setOrganizationName(cleanField("organizationName"));
                contact.setAddress1(cleanField("address1"));
                contact.setAddress2(cleanField("address2"));
                contact.setCity(cleanField("city"));
                contact.setStateProvince(cleanField("stateProvince"));
                contact.setPostalCode(cleanField("postalCode"));
                contact.setCountryCode(cleanField("countryCode"));
                contact.setLastName(cleanField("lastName"));
                contact.setFirstName(cleanField("firstName"));
                contact.setInitial(cleanField("initial"));
                contact.setTitle(cleanField("title"));
                contact.setPhoneCountryCode((Integer) contactForm.get("phoneCountryCode"));
                String area = (String) contactForm.get("phoneAreaCode");
                area = area.trim();
                if (area.length() > 0) {
                    contact.setPhoneAreaCode(Integer.valueOf(area));
                }
                contact.setPhoneNumber(cleanField("phoneNumber"));
                contact.setFaxCountryCode((Integer) contactForm.get("faxCountryCode"));
                contact.setFaxAreaCode((Integer) contactForm.get("faxAreaCode"));
                contact.setFaxNumber(cleanField("faxNumber"));
                contact.setEmail(cleanField("email"));
                contact.setFields((Hashtable) contactForm.get("fields"));
                contact.setInclude(new Integer(((Boolean) contactForm.get("chbx_include")).
                        booleanValue() ? 1 : 0));
                
                myRemoteSession.setContact(contact, userId, type);  
                
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                        new ActionMessage("user.contact.edit.done"));
                saveMessages(request, messages);
                
                // remove the user's list so the contact info will be refreshed
                session.removeAttribute(Constants.SESSION_LIST_KEY + 
                        Constants.LIST_TYPE_CUSTOMER);
                forward = "done";
            }
                
        } catch (Exception e) {
            log.error("Exception!", e);
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("all.internal"));
            saveErrors(request, errors);
        }
        
        log.debug("After contact: action = " + action);
        log.debug("Fields number:" + ((Hashtable) contactForm.get(
                "fields")).size());
        for (Iterator it = ((Hashtable) contactForm.get(
                "fields")).keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            log.debug("field " + key + " is " + ((Hashtable) contactForm.get(
                "fields")).get(key));
        }
        return mapping.findForward(forward);
    }
    
    // we don't want to insert blanks or empty strings in the DB
    private String cleanField(String field) {
        String retValue = (String) contactForm.get(field);
        if (retValue == null) return null;
        retValue = retValue.trim();
        
        return (retValue.length() > 0 ? retValue : null);
    }
    
    private void fillForm(ContactDTOEx dbContact,
            HttpServletRequest request, ActionMapping mapping,
            ModuleConfig moduleConfig) {
        contactForm = (DynaValidatorForm) RequestUtils
                .createActionForm(request, mapping, moduleConfig, servlet);
        
        contactForm.set("organizationName",
                dbContact.getOrganizationName());
        contactForm.set("address1", dbContact.getAddress1());
        contactForm.set("address2", dbContact.getAddress2());
        contactForm.set("city", dbContact.getCity());
        contactForm.set("stateProvince", dbContact.getStateProvince());
        contactForm.set("postalCode", dbContact.getPostalCode());
        contactForm.set("countryCode", dbContact.getCountryCode());
        contactForm.set("lastName", dbContact.getLastName());
        contactForm.set("firstName", dbContact.getFirstName());
        contactForm.set("initial", dbContact.getInitial());
        contactForm.set("title", dbContact.getTitle());
        contactForm.set("phoneCountryCode",
                dbContact.getPhoneCountryCode());
        contactForm.set("phoneAreaCode", 
                dbContact.getPhoneAreaCode() != null ?
                dbContact.getPhoneAreaCode().toString() : null);
        contactForm.set("phoneNumber", dbContact.getPhoneNumber());
        contactForm.set("faxCountryCode",
                dbContact.getFaxCountryCode());
        contactForm.set("faxAreaCode",
                dbContact.getFaxAreaCode());
        contactForm.set("faxNumber", dbContact.getFaxNumber());
        contactForm.set("email", dbContact.getEmail());
        contactForm.set("fields", dbContact.getFields());
        Boolean include = new Boolean(false);
        if (dbContact.getInclude() != null) {
            include = new Boolean(dbContact.getInclude().intValue() == 1);
        }
        contactForm.set("chbx_include", include);
    }
}
