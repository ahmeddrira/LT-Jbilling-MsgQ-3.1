/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.client.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.user.UserDTOEx;

/**
 * Prepares the a bean to make available to the page the collection
 * of options for order periods
 * 
 * @author emilc
 *
 * @jsp:tag name="getUser"
 *          body-content="empty"
 */
public class GetUserTag extends TagSupport {
    
    Integer userId = null;
    Boolean createForm = new Boolean(false);
    
    public int doStartTag() throws JspException {
        
        Logger log = Logger.getLogger(GetUserTag.class);
        HttpSession session = pageContext.getSession();


		try {
            // get the order session bean
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            UserSessionHome orderHome =
                    (UserSessionHome) EJBFactory.lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);
            UserSession remoteUser = orderHome.create();
            // make the call
            UserDTOEx dto  = remoteUser.getUserDTOEx(userId);
    
            // finally, make the data available to the page
            pageContext.setAttribute(Constants.PAGE_USER_DTO,
                    dto, PageContext.PAGE_SCOPE);
            
            // see if the form bean has to be createn and initialized        
            if (createForm.booleanValue()) {
                // I have to go finding all sort of object to produce the
                // form bean
                String action = "/userMaintain"; // all starts with the action
                ModuleConfig moduleConfig = RequestUtils.getModuleConfig(
                        pageContext);
                String mappingName = RequestUtils.getActionMappingName(action);
                ActionMapping mapping = (ActionMapping) moduleConfig.
                        findActionConfig(mappingName);
                        
                ActionServlet servlet =  (ActionServlet) pageContext.
                        getServletContext().getAttribute(
                        Globals.ACTION_SERVLET_KEY);
                DynaValidatorForm form = (DynaValidatorForm) 
                        RequestUtils.createActionForm(
                            (HttpServletRequest) pageContext.getRequest(),
                            mapping, moduleConfig, servlet);
                
                // set the fields with the data from the db
                form.set("id", dto.getUserId());
                form.set("entity", dto.getEntityId());
                form.set("type", dto.getMainRoleId());
                form.set("status", dto.getStatusId());
                form.set("username", dto.getUserName());
                //password may be crypted, we can not hint it anymore
                //form.set("password", dto.getPassword());
                form.set("password", "");
                form.set("language", dto.getLanguageId());
                form.set("currencyId", dto.getCurrencyId());
                if (dto.getCustomerDto() != null) {
                    form.set("deliveryMethodId", 
                            dto.getCustomerDto().getInvoiceDeliveryMethodId());
                    form.set("due_date_unit_id", 
                            dto.getCustomerDto().getDueDateUnitId());
                    form.set("due_date_value", dto.getCustomerDto()
                            .getDueDateValue() == null ? null : dto
                            .getCustomerDto().getDueDateValue().toString());
                    form.set("chbx_df_fm", new Boolean(dto.getCustomerDto().getDfFm() 
                            == null ? false : dto.getCustomerDto().getDfFm().intValue() 
                                    == 1));
                    form.set("chbx_excludeAging", new Boolean(
                            dto.getCustomerDto().getExcludeAging() 
                                == null ? false : 
                            dto.getCustomerDto().getExcludeAging().intValue() == 1));
                    if (dto.getCustomerDto().getPartnerId() != null) {
                        form.set("partnerId", dto.getCustomerDto().getPartnerId()
                                .toString());
                    } else {
                        form.set("partnerId", null);
                    }
                    form.set("subscriberStatus", dto.getSubscriptionStatusId());
                }
                
                // make it available to the jsp
                session.setAttribute("userEdit", form);
            }
            
            // if this is a partner, leave the id in the session to allow for 
            // edition of its attributes
            if (dto.getPartnerDto() != null) {
                session.setAttribute(Constants.SESSION_PARTNER_ID, 
                        dto.getPartnerDto().getId());
            } else {
                // make sure there's no confusion
                session.removeAttribute(Constants.SESSION_PARTNER_ID);
            }
		} catch (Exception e) {
		    log.error("Exception on getting a user information", e);
		    throw new JspException(e);
		}

        return SKIP_BODY;
    }

    /**
     * @jsp:attribute required="true"
     *                rtexprvalue="true"
     *                type="java.lang.Integer"
     * @return
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param integer
     */
    public void setUserId(Integer integer) {
        userId = integer;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     * @return
     */
    public Boolean getCreateForm() {
        return createForm;
    }

    /**
     * @param boolean1
     */
    public void setCreateForm(Boolean boolean1) {
        createForm = boolean1;
    }

}
