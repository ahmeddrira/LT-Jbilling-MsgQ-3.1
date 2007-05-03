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
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;
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
import org.apache.struts.validator.Resources;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.ItemSession;
import com.sapienter.jbilling.interfaces.ItemSessionHome;
import com.sapienter.jbilling.interfaces.NewOrderSession;
import com.sapienter.jbilling.interfaces.OrderSession;
import com.sapienter.jbilling.interfaces.OrderSessionHome;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.entity.PartnerRangeDTO;
import com.sapienter.jbilling.server.item.PromotionDTOEx;
import com.sapienter.jbilling.server.order.NewOrderDTO;
import com.sapienter.jbilling.server.order.OrderDTOEx;
import com.sapienter.jbilling.server.order.OrderPeriodDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskParameterDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskSession;
import com.sapienter.jbilling.server.user.PartnerDTOEx;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.util.MapPeriodToCalendar;
import com.sapienter.jbilling.server.util.OptionDTO;

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
    private Integer languageId = null;
    private Integer entityId = null;
    private Integer executorId = null;
    
    private final FormHelper formHelper;

    public GenericMaintainAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request) { 
        this.mapping = mapping;
        this.request = request;
        log = Logger.getLogger(GenericMaintainAction.class);
        errors = new ActionErrors();
        messages = new ActionMessages();
        session = request.getSession(false);
        myForm = (DynaValidatorForm) form;
        formHelper = new FormHelper(session);
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
        formHelper = new FormHelper(session);
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

        languageId = (Integer) session.getAttribute(
                Constants.SESSION_LANGUAGE);
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
        PartnerRangeDTO[] partnerRangesData = null;
        
        // do the validation, before moving any info to the dto
        errors = new ActionErrors(myForm.validate(mapping, request));
        
        if (!errors.isEmpty()) {
            return(retValue);
        }                
        
        if (mode.equals("order")) {
            // this is kind of a wierd case. The dto in the session is all
            // it is required to edit.
            NewOrderDTO summary = (NewOrderDTO) session.getAttribute(
                    Constants.SESSION_ORDER_SUMMARY);
            summary.setPeriod((Integer) myForm.get("period"));
            summary.setActiveSince(parseDate("since", 
                    "order.prompt.activeSince"));
            summary.setActiveUntil(parseDate("until", 
                    "order.prompt.activeUntil"));
            summary.setNextBillableDay(parseDate("next_billable",
                    "order.prompt.nextBillableDay"));
            summary.setBillingTypeId((Integer) myForm.get("billingType"));
            summary.setPromoCode((String) myForm.get("promotion_code"));
            summary.setNotify(new Integer(((Boolean) myForm.
                    get("chbx_notify")).booleanValue() ? 1 : 0));
            summary.setDfFm(new Integer(((Boolean) myForm.
                    get("chbx_df_fm")).booleanValue() ? 1 : 0));
            summary.setOwnInvoice(new Integer(((Boolean) myForm.
                    get("chbx_own_invoice")).booleanValue() ? 1 : 0));
            summary.setNotesInInvoice(new Integer(((Boolean) myForm.
                    get("chbx_notes")).booleanValue() ? 1 : 0));
            summary.setNotes((String) myForm.get("notes"));
            summary.setAnticipatePeriods(getInteger("anticipate_periods"));
            summary.setPeriodStr(getOptionDescription(summary.getPeriod(),
                    Constants.PAGE_ORDER_PERIODS, session));
            summary.setBillingTypeStr(getOptionDescription(
                    summary.getBillingTypeId(),
                    Constants.PAGE_BILLING_TYPE, session));
            summary.setDueDateUnitId((Integer) myForm.get("due_date_unit_id"));
            summary.setDueDateValue(getInteger("due_date_value"));

            // return any date validation errors to user
            if (!errors.isEmpty()) {
                return "edit";
            }
            
            // if she wants notification, we need a date of expiration
            if (summary.getNotify().intValue() == 1 && 
            		summary.getActiveUntil() == null) {
            	errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("order.error.notifyWithoutDate", 
                        "order.prompt.notify"));
                return "edit";
            }
            
            // validate the dates if there is a date of expiration
            if (summary.getActiveUntil() != null) {
                Date start = summary.getActiveSince() != null ?
                        summary.getActiveSince() : 
                        Calendar.getInstance().getTime();
                start = Util.truncateDate(start);
                // it has to be grater than the starting date
                if (!summary.getActiveUntil().after(start)) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("order.error.dates", 
                            "order.prompt.activeUntil"));
                    return "edit";
                }
                // only if it is a recurring order
                if (!summary.getPeriod().equals(new Integer(1))) {
                    // the whole period has to be a multiple of the period unit
                    // This is true, until there is support for prorating.
                    JNDILookup EJBFactory = null;
                    OrderSessionHome orderHome;
                    try {
                        EJBFactory = JNDILookup.getFactory(false);
                        orderHome = (OrderSessionHome) EJBFactory.lookUpHome(
                                OrderSessionHome.class,
                                OrderSessionHome.JNDI_NAME);
            
                        OrderSession orderSession = orderHome.create();
                        OrderPeriodDTOEx period = orderSession.getPeriod(
                                languageId, summary.getPeriod());
                        GregorianCalendar toTest = new GregorianCalendar();
                        toTest.setTime(start);
                        while (toTest.getTime().before(summary.getActiveUntil())) {
                            toTest.add(MapPeriodToCalendar.map(period.getUnitId()),
                                    period.getValue().intValue());
                        }
                        if (!toTest.getTime().equals(summary.getActiveUntil())) {
                            log.debug("Fraction of a period:" + toTest.getTime() +
                                    " until: " + summary.getActiveUntil());
                            errors.add(ActionErrors.GLOBAL_ERROR,
                                    new ActionError("order.error.period"));
                            return "edit";
                        }
                    } catch (Exception e) {
                        throw new SessionInternalError("Validating date periods", 
                                GenericMaintainAction.class, e);
                    }
                }
            }

            // validate next billable day
            OrderDTOEx orderDTO = (OrderDTOEx) session.getAttribute(
                    Constants.SESSION_ORDER_DTO);
            // if a date was submitted, check that it is >= old date or
            // greater than today if old date is null.
            if (summary.getNextBillableDay() != null) {
                if (orderDTO != null && orderDTO.getNextBillableDay() != null) {
                    if (summary.getNextBillableDay().before(
                            orderDTO.getNextBillableDay())) {
                        // new date is less than old date
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("order.error.nextBillableDay.hasOldDate"));
                        return "edit";                    
                    }
                } else if (!summary.getNextBillableDay().after(
                        Calendar.getInstance().getTime())) {
                    // old date doesn't exist and new date is not after todays date
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("order.error.nextBillableDay.noOldDate"));
                    return "edit";                    
                }
            } else {
                // else no date was submitted, check that old date isn't null
                if (orderDTO != null && orderDTO.getNextBillableDay() != null) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("order.error.nextBillableDay.null"));
                    return "edit";
                }
            }

            // now process this promotion if specified
            if (summary.getPromoCode() != null && 
                    summary.getPromoCode().length() > 0) {
                try {
                    JNDILookup EJBFactory = JNDILookup.getFactory(false);
                    ItemSessionHome itemHome =
                            (ItemSessionHome) EJBFactory.lookUpHome(
                            ItemSessionHome.class,
                            ItemSessionHome.JNDI_NAME);
            
                    ItemSession itemSession = itemHome.create();
                    PromotionDTOEx promotion = itemSession.getPromotion(
                            (Integer) session.getAttribute(
                            Constants.SESSION_ENTITY_ID_KEY), 
                            summary.getPromoCode());
                    
                    if (promotion == null) {
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("promotion.error.noExist", 
                                "order.prompt.promotion"));
                        return "edit";
                    } 
                    // if this is an update or the promotion hasn't been 
                    // used by the user
                    if (summary.getId() != null || itemSession.
                            promotionIsAvailable(promotion.getId(),
                                summary.getUserId(), 
                                promotion.getCode()).booleanValue()) {
                        summary = ((NewOrderSession) remoteSession).addItem(
                                promotion.getItemId(), new Integer(1),
                                summary.getUserId(), entityId);
                        session.setAttribute(Constants.SESSION_ORDER_SUMMARY, 
                                summary);
                    } else {
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("promotion.error.alreadyUsed", 
                                "order.prompt.promotion"));
                        return "edit";
                    }                                
                    
                                
                } catch (Exception e) {
                }
            }
            
            return "items";
        } else if (mode.equals("parameter")) { /// for pluggable task parameters
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
        } else if (mode.equals("ranges")) {
            retValue = "partner"; // goes to the partner screen
            String from[] = (String[]) myForm.get("range_from");
            String to[] = (String[]) myForm.get("range_to");
            String percentage[] = (String[]) myForm.get("percentage_rate");
            String referral[] = (String[]) myForm.get("referral_fee");
            Vector ranges = new Vector();
            
            for (int f = 0; f < from.length; f++) {
                if (from[f] != null && from[f].trim().length() > 0) {
                    PartnerRangeDTO range = new PartnerRangeDTO();
                    try {
                        range.setRangeFrom(getInteger2(from[f]));
                        range.setRangeTo(getInteger2(to[f]));
                        range.setPercentageRate(string2float(percentage[f]));
                        range.setReferralFee(string2float(referral[f]));
                        if (range.getRangeFrom() == null || range.getRangeTo() == null ||
                                (range.getPercentageRate() == null && range.getReferralFee() == null) ||
                                (range.getPercentageRate() != null && range.getReferralFee() != null)) {
                            errors.add(ActionErrors.GLOBAL_ERROR,
                                    new ActionError("partner.ranges.error", 
                                            new Integer(f + 1)));
                        } else {
                            ranges.add(range);
                        }
                    } catch (NumberFormatException e) {
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("partner.ranges.error", 
                                new Integer(f + 1)));
                    }
                } 
            }
            
            partnerRangesData = new PartnerRangeDTO[ranges.size()];
            ranges.toArray(partnerRangesData);
            if (errors.isEmpty()) {
                PartnerDTOEx p = new PartnerDTOEx();
                p.setRanges(partnerRangesData);
                int ret = p.validateRanges();
                if (ret == 2) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("partner.ranges.error.consec"));
                } else if (ret == 3) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("partner.ranges.error.gap"));
                }
            }
            
            if (!errors.isEmpty()) {
                retValue = "edit";
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
            } else  if (mode.equals("ranges")) {
                PartnerDTOEx partner = (PartnerDTOEx) session.getAttribute(
                        Constants.SESSION_PARTNER_DTO);
                partner.setRanges(partnerRangesData); 
                ((UserSession) remoteSession).updatePartnerRanges(executorId, 
                        partner.getId(), partnerRangesData);
                messageKey = "partner.ranges.updated";
                retValue = "partner";
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
                
        if (mode.equals("order")) {
            OrderDTOEx dto = (OrderDTOEx) session.getAttribute(
                    Constants.SESSION_ORDER_DTO);
            myForm.set("period", dto.getPeriodId());
            myForm.set("chbx_notify", new Boolean(dto.getNotify() == null ?
                    false : dto.getNotify().intValue() == 1));
            setFormDate("since", dto.getActiveSince());
            setFormDate("until", dto.getActiveUntil());
            setFormDate("next_billable", dto.getNextBillableDay());
            myForm.set("due_date_unit_id", dto.getDueDateUnitId());
            myForm.set("due_date_value", dto.getDueDateValue() == null ?
                    null : dto.getDueDateValue().toString());
            myForm.set("chbx_df_fm", new Boolean(dto.getDfFm() == null ?
                    false : dto.getDfFm().intValue() == 1));
            myForm.set("chbx_own_invoice", new Boolean(dto.getOwnInvoice() == null ?
                    false : dto.getOwnInvoice().intValue() == 1));
            myForm.set("chbx_notes", new Boolean(dto.getNotesInInvoice() == null ?
                    false : dto.getNotesInInvoice().intValue() == 1));
            myForm.set("notes", dto.getNotes());
            myForm.set("anticipate_periods", dto.getAnticipatePeriods() == null ?
                    null : dto.getAnticipatePeriods().toString());

            myForm.set("billingType", dto.getBillingTypeId());
            if (dto.getPromoCode() != null) {
                myForm.set("promotion_code", dto.getPromoCode());
            }
        } else if (mode.equals("parameter")) { /// for pluggable task parameters
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
        } else if (mode.equals("ranges")) {
            PartnerDTOEx partner = (PartnerDTOEx) session.getAttribute(
                    Constants.SESSION_PARTNER_DTO);
            PartnerRangeDTO ranges[] = partner.getRanges();
            String arr1[] = new String[20];
            String arr2[] = new String[20];
            String arr3[] = new String[20];
            String arr4[] = new String[20];
            // add 20 ranges to the session for edition
            for (int f = 0; f < 20; f++) {
                if (ranges != null && ranges.length > f) {
                    arr1[f] = ranges[f].getRangeFrom().toString();
                    arr2[f] = ranges[f].getRangeTo().toString();
                    arr3[f] = float2string(ranges[f].getPercentageRate());
                    arr4[f] = float2string(ranges[f].getReferralFee());
                } else {
                    arr1[f] = null;
                    arr2[f] = null;
                    arr3[f] = null;
                    arr4[f] = null;
                }
            }
            
            myForm.set("range_from", arr1);
            myForm.set("range_to", arr2);
            myForm.set("percentage_rate", arr3);
            myForm.set("referral_fee", arr4);
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
    
    public Date parseDate(String prefix, String prompt) {
        Date date = null;
        String year = (String) myForm.get(prefix + "_year");
        String month = (String) myForm.get(prefix + "_month");
        String day = (String) myForm.get(prefix + "_day");
        
        // if one of the fields have been entered, all should've been
        if ((year.length() > 0 && (month.length() <= 0 || day.length() <= 0)) ||
            (month.length() > 0 && (year.length() <= 0 || day.length() <= 0)) ||
            (day.length() > 0 && (month.length() <= 0 || year.length() <= 0)) ) {
            // get the localized name of this field
            String field = Resources.getMessage(request, prompt); 
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("errors.incomplete.date", field));
            return null;
        }
        if (year.length() > 0 && month.length() > 0 && day.length() > 0) {
            try {
                date = Util.getDate(Integer.valueOf(year), 
                        Integer.valueOf(month), Integer.valueOf(day));
            } catch (Exception e) {
                log.info("Exception when converting the fields to integer", e);
                date = null;
            }
            if (date == null) {
                // get the localized name of this field
                String field = Resources.getMessage(request, prompt); 
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("errors.date", field));
            } 
        }
        
        return date;
    }
    
    private void setFormDate(String prefix, Date date) {
        if (date != null) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            myForm.set(prefix + "_month", String.valueOf(cal.get(
                    GregorianCalendar.MONTH) + 1));
            myForm.set(prefix + "_day", String.valueOf(cal.get(
                    GregorianCalendar.DAY_OF_MONTH)));
            myForm.set(prefix + "_year", String.valueOf(cal.get(
                    GregorianCalendar.YEAR)));
        } else {
            myForm.set(prefix + "_month", null);
            myForm.set(prefix + "_day", null);
            myForm.set(prefix + "_year", null);
        }
    }
    
    private void required(String field, String key) {
        if (field == null || field.trim().length() == 0) {
            String name = Resources.getMessage(request, key);
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("errors.required", name));
        }
    }
    
    private void required(Date field, String key) {
        if (field == null) {
            String name = Resources.getMessage(request, key);
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("errors.required", name));
        }
    }

    public static void cleanUpSession(HttpSession session) {
        Enumeration entries = session.getAttributeNames();
        for (String entry = (String)entries.nextElement(); 
                entries.hasMoreElements();
                entry = (String)entries.nextElement()) {
            if (!entry.startsWith("sys_") && !entry.startsWith("org.apache.struts")) {
                //Logger.getLogger(GenericMaintainAction.class).debug("removing " + entry);
                session.removeAttribute(entry);
                // you can't modify the colleciton and keep iterating with the
                // same reference (doahhh :p )
                entries = session.getAttributeNames();
            }                
        }
        
    }        

    public static String getOptionDescription(Integer id, String optionType,
            HttpSession session) throws SessionInternalError {
        Vector options = (Vector) session.getAttribute("SESSION_" + 
                optionType);
        if (options == null) {
            throw new SessionInternalError("can't find the vector of options" +
                    " in the session:" + optionType);
        }
        
        OptionDTO option;
        for (int f=0; f < options.size(); f++) {
            option = (OptionDTO) options.get(f);
            if (option.getCode().compareTo(id.toString()) == 0) {
                return option.getDescription();
            }
        }
        
        throw new SessionInternalError("id " + id + " not found in options " +
                optionType);
    }

    private UserDTOEx getUser(Integer userId) 
            throws SessionInternalError, FinderException {    
        UserDTOEx retValue = null;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            UserSessionHome userHome =
                    (UserSessionHome) EJBFactory.lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);
            UserSession userSession = userHome.create();
                    
            retValue = userSession.getUserDTOEx(userId); 
        } catch (FinderException e) {
            throw new FinderException();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
        return retValue;
    }
    
    private Integer getInteger(String fieldName) {
        String field = (String) myForm.get(fieldName);
        return getInteger2(field);
    }
    
    private Integer getInteger2(String str) {
        Integer retValue;
        if (str != null && str.trim().length() > 0) {
            retValue = Integer.valueOf(str);
        } else {
            retValue = null;
        }
        
        return retValue;
    }
    
    public ActionErrors getErrors() {
        return errors;
    }
    
	private String float2string(Float arg) {
		return formHelper.float2string(arg);
	}

	private Float string2float(String arg) {
		return formHelper.string2float(arg);
	}

}
