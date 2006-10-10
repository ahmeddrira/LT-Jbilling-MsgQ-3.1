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

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.server.list.ListSession;
import com.sapienter.jbilling.server.list.ListSessionHome;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.util.OptionDTO;

/**
 * Prepares the a bean to make available to the page the collection
 * of options for order periods
 * 
 * @author emilc
 *
 * @jsp:tag name="getOptions"
 *          body-content="empty"
 */
public class GetOptionsTag extends TagSupport {

    // these are the different types of data to fetch    
    private Boolean countries = new Boolean(false);
    private Boolean userType = new Boolean(false);
    private Boolean language = new Boolean(false);
    private Boolean userStatus = new Boolean(false);
    private Boolean itemType = new Boolean(false);
    private Boolean orderPeriod = new Boolean(false);
    private Boolean billingType = new Boolean(false);
    private Boolean generalPeriod = new Boolean(false);
    private Boolean currencies = new Boolean(false);
    private Boolean contactType = new Boolean(false);
    private Boolean deliveryMethod = new Boolean(false);
    private Boolean orderLineType = new Boolean(false);
    private Boolean taskClasses = new Boolean(false);

    // these are flag to indicate some particluar behavior
    private Boolean inSession = new Boolean(false);
    private String map = null;
    
    public int doStartTag() throws JspException {
        
        Logger log = Logger.getLogger(GetOptionsTag.class);

        // pull some data from the session before making the call
        HttpSession session = pageContext.getSession();
        Integer languageId = (Integer) session.getAttribute(
                Constants.SESSION_LANGUAGE);
        if (languageId == null) {
            languageId = new Integer(1); // def to english
        }
        Integer entityId = (Integer) session.getAttribute(
                Constants.SESSION_ENTITY_ID_KEY);
        UserDTOEx user = (UserDTOEx) session.getAttribute(
                Constants.SESSION_USER_DTO);
        Integer executorType = null;
        if (user!= null) {
            executorType = user.getMainRoleId();
        }
        Collection retValue = null;
        String attributeKey = null;
        String type = null;

		try {
            // get the the jndi factory
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            ListSessionHome listHome =
                    (ListSessionHome) EJBFactory.lookUpHome(
                    ListSessionHome.class,
                    ListSessionHome.JNDI_NAME);
            ListSession remoteList = listHome.create();
  
            if (countries.booleanValue()) {
                type = "countries";
                attributeKey = Constants.PAGE_COUNTRIES;
            } else if (userType.booleanValue()) {
                type = "userType";
                attributeKey = Constants.PAGE_USER_TYPES;
            } else if (language.booleanValue()) {
                type = "language";
                attributeKey = Constants.PAGE_LANGUAGES;
            } else if (userStatus.booleanValue()) {
                type = "userStatus";
                attributeKey = Constants.PAGE_USER_STATUS;
            } else if (itemType.booleanValue()) {
                type = "itemType";
                attributeKey = Constants.PAGE_ITEM_TYPES;
            } else if (orderPeriod.booleanValue()) {
                type = "orderPeriod";
                attributeKey = Constants.PAGE_ORDER_PERIODS;
            } else if (billingType.booleanValue()) {
                type = "billingType";
                attributeKey = Constants.PAGE_BILLING_TYPE;
            } else if (generalPeriod.booleanValue()) {
                type = "generalPeriod";
                attributeKey = Constants.PAGE_GENERAL_PERIODS;
            } else if (currencies.booleanValue()) {
                type = "currencies";
                attributeKey = Constants.PAGE_CURRENCIES;
            } else if (contactType.booleanValue()) {
                type = "contactType";
                attributeKey = Constants.PAGE_CONTACT_TYPES;
            } else if (deliveryMethod.booleanValue()) {
                type = "deliveryMethod";
                attributeKey = Constants.PAGE_DELIVERY_METHOD;
            } else if (orderLineType.booleanValue()) {
                type = "orderLineType";
                attributeKey = Constants.PAGE_ORDER_LINE_TYPES;
            } else if (taskClasses.booleanValue()) {
                type = "taskClasses";
                attributeKey = Constants.PAGE_TASK_CLASSES;
            } else {
                log.error("At least one flag has to be present");
                throw new Exception("at least one attribute required");
            }

            // make the call
            retValue = remoteList.getOptions(type , languageId, entityId,
                    executorType);
            log.debug("Got the options for " + type + " there are " + 
                    retValue.size());
    
            // finally, make the data available to the page
            // see if this call is just to map an id to a string, instead
            // of using it for a select
            if (map != null) {
                for (Iterator it = retValue.iterator(); it.hasNext(); ) {
                    OptionDTO option = (OptionDTO) it.next();
                    if (option.getCode().equals(map)) {
                        pageContext.setAttribute("mapped_option",
                                option.getDescription());
                        break;
                    }
                }
            } else {
                pageContext.setAttribute(attributeKey,
                        retValue, PageContext.PAGE_SCOPE);
            }
		    // in some cases, the result is needed also in the session
            if (inSession.booleanValue()) {
                pageContext.setAttribute("SESSION_" + attributeKey,
                        retValue, PageContext.SESSION_SCOPE);
            }
            
            
        } catch (Exception e) {
		    log.error("Exception on getting the order periods", e);
		    throw new JspException(e);
		}

        return SKIP_BODY;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getCountries() {
        return countries;
    }

    /**
     * @param boolean1
     */
    public void setCountries(Boolean boolean1) {
        countries = boolean1;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getUserType() {
        return userType;
    }

    /**
     * @param boolean1
     */
    public void setUserType(Boolean boolean1) {
        userType = boolean1;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getLanguage() {
        return language;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getUserStatus() {
        return userStatus;
    }

    /**
     * @param boolean1
     */
    public void setLanguage(Boolean boolean1) {
        language = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setUserStatus(Boolean boolean1) {
        userStatus = boolean1;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getItemType() {
        return itemType;
    }

    /**
     * @param boolean1
     */
    public void setItemType(Boolean boolean1) {
        itemType = boolean1;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getOrderPeriod() {
        return orderPeriod;
    }

    /**
     * @param boolean1
     */
    public void setOrderPeriod(Boolean boolean1) {
        orderPeriod = boolean1;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getBillingType() {
        return billingType;
    }

    /**
     * @param boolean1
     */
    public void setBillingType(Boolean boolean1) {
        billingType = boolean1;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getInSession() {
        return inSession;
    }

    /**
     * @param boolean1
     */
    public void setInSession(Boolean boolean1) {
        inSession = boolean1;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getGeneralPeriod() {
        return generalPeriod;
    }

    /**
     * @param generalPeriod
     */
    public void setGeneralPeriod(Boolean generalPeriod) {
        this.generalPeriod = generalPeriod;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getCurrencies() {
        return currencies;
    }

    /**
     * @param currencies
     */
    public void setCurrencies(Boolean currencies) {
        this.currencies = currencies;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Integer"
     */
    public String getMap() {
        return map;
    }

    /**
     * @param map
     */
    public void setMap(String map) {
        this.map = map;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getContactType() {
        return contactType;
    }
    public void setContactType(Boolean contactType) {
        this.contactType = contactType;
    }
    
    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getDeliveryMethod() {
        return deliveryMethod;
    }
    public void setDeliveryMethod(Boolean deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getOrderLineType() {
        return orderLineType;
    }
    public void setOrderLineType(Boolean orderLineType) {
        this.orderLineType = orderLineType;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.Boolean"
     */
    public Boolean getTaskClasses() {
        return taskClasses;
    }
    public void setTaskClasses(Boolean taskClasses) {
        this.taskClasses = taskClasses;
    }
}
