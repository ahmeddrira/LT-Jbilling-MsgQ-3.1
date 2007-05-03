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

import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
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
import org.apache.struts.validator.FieldChecks;
import org.apache.struts.validator.Resources;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.ItemSession;
import com.sapienter.jbilling.interfaces.ItemSessionHome;
import com.sapienter.jbilling.interfaces.NewOrderSession;
import com.sapienter.jbilling.interfaces.NotificationSession;
import com.sapienter.jbilling.interfaces.OrderSession;
import com.sapienter.jbilling.interfaces.OrderSessionHome;
import com.sapienter.jbilling.interfaces.PaymentSession;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.InvoiceDTO;
import com.sapienter.jbilling.server.entity.PartnerRangeDTO;
import com.sapienter.jbilling.server.entity.PaymentInfoChequeDTO;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.ItemPriceDTOEx;
import com.sapienter.jbilling.server.item.PromotionDTOEx;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.MessageSection;
import com.sapienter.jbilling.server.order.NewOrderDTO;
import com.sapienter.jbilling.server.order.OrderDTOEx;
import com.sapienter.jbilling.server.order.OrderPeriodDTOEx;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
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
    private Integer selectedId = null;
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
        
        selectedId = (Integer) session.getAttribute(
                Constants.SESSION_LIST_ID_SELECTED);
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
        ItemDTOEx itemDto = null;
        PaymentDTOEx paymentDto = null;
        MessageDTO messageDto = null;
        PluggableTaskDTOEx taskDto = null;
        PartnerRangeDTO[] partnerRangesData = null;
        
        // do the validation, before moving any info to the dto
        errors = new ActionErrors(myForm.validate(mapping, request));
        
        // this is a hack for items created for promotions
        if (mode.equals("item") && ((String) myForm.get("create")).equals(
                "promotion")) {
            retValue = "promotion";
            log.debug("Processing an item for a promotion");
        }
        if (mode.equals("payment") && ((String) myForm.get("direct")).equals(
                "yes")) {
            retValue = "fromOrder";
        }
        if (!errors.isEmpty()) {
            return(retValue);
        }                
        
        if (mode.equals("item")) { // an item
            if (request.getParameter("reload") != null) {
                // this is just a change of language the requires a reload
                // of the bean
                languageId = (Integer) myForm.get("language");
                return setup();
            }

            itemDto = new ItemDTOEx();
            itemDto.setDescription((String) myForm.get("description"));
            itemDto.setEntityId((Integer) session.getAttribute(
                    Constants.SESSION_ENTITY_ID_KEY));
            itemDto.setNumber((String) myForm.get("internalNumber"));
            itemDto.setPriceManual(new Integer(((Boolean) myForm.get
                    ("chbx_priceManual")).booleanValue() ? 1 : 0));
            itemDto.setTypes((Integer[]) myForm.get("types"));
            if (((String) myForm.get("percentage")).trim().length() > 0) {
                itemDto.setPercentage(string2float(
                        (String) myForm.get("percentage")));
            }
            // because of the bad idea of using the same bean for item/type/price,
            // the validation has to be manual
            if (itemDto.getTypes().length == 0) {
                String field = Resources.getMessage(request, "item.prompt.types"); 
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("errors.required", field));
            }
            
            // get the prices. At least one has to be present
            itemDto.setPrices((Vector) myForm.get("prices"));
            boolean priceFlag = false;
            for (int f = 0; f < itemDto.getPrices().size(); f++) {
                String priceStr = ((ItemPriceDTOEx)itemDto.getPrices().get(f)).
                        getPriceForm(); 
                log.debug("Now processing item price " + f + " data:" + 
                        (ItemPriceDTOEx)itemDto.getPrices().get(f));
                Float price = null;
                if (priceStr != null && priceStr.trim().length() > 0) {
                    price = string2float(priceStr.trim());
                    if (price == null) {
                        String field = Resources.getMessage(request, "item.prompt.price"); 
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("errors.float", field));
                        break;
                    } else {
                        priceFlag = true;
                    }
                }
                ((ItemPriceDTOEx)itemDto.getPrices().get(f)).setPrice(
                        price);
            }

            // either is a percentage or a price is required.
            if (!priceFlag && itemDto.getPercentage() == null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("item.error.price"));
            }
            
        } else if (mode.equals("payment")) {
            paymentDto = new PaymentDTOEx();
            // the id, only for payment edits
            paymentDto.setId((Integer) myForm.get("id"));
            // set the amount
            paymentDto.setAmount(string2float((String) myForm.get("amount")));
            // set the date
            paymentDto.setPaymentDate(parseDate("date", "payment.date"));
            if (((String) myForm.get("method")).equals("cheque")) {
                // create the cheque dto
                PaymentInfoChequeDTO chequeDto = new PaymentInfoChequeDTO();
                chequeDto.setBank((String) myForm.get("bank"));
                chequeDto.setNumber((String) myForm.get("chequeNumber"));
                chequeDto.setDate(parseDate("chequeDate", "payment.cheque.date"));
                // set the cheque
                paymentDto.setCheque(chequeDto);
                paymentDto.setMethodId(Constants.PAYMENT_METHOD_CHEQUE);
                // validate required fields        
                required(chequeDto.getNumber(),"payment.cheque.number");
                required(chequeDto.getDate(), "payment.cheque.date");
                // cheques now are never process realtime (may be later will support
                // electronic cheques
                paymentDto.setResultId(Constants.RESULT_ENTERED);
                session.setAttribute("tmp_process_now", new Boolean(false));                
                
            } else if (((String) myForm.get("method")).equals("cc")) {
                String ccNumber = (String) myForm.get("ccNumber");
                boolean masked = false;

                // check if cc number is masked
                if (ccNumber != null && ccNumber.length() >= 16
                        && ccNumber.substring(0, 12).equals("************")) {
                    log.debug("cc no. masked; " 
                            + "getting user's existing cc details");
                    // try to get existing cc details
                    UserDTOEx user;
                    try {
                        user = getUser((Integer) session.
                                getAttribute(Constants.SESSION_USER_ID));
                    } catch (FinderException e) {
                        throw new SessionInternalError(e); 
                    }
                    CreditCardDTO existingCcDTO = user.getCreditCard();
                    if(existingCcDTO != null) {
                        String existingNumber = existingCcDTO.getNumber();
                        // check that four last digits match
                        if(existingNumber.substring(
                                existingNumber.length() - 4).equals(
                                ccNumber.substring(ccNumber.length() - 4))) {
                            log.debug("got a matching masked cc number");
                            masked = true;
                            ccNumber = existingNumber;
                        }
                    }
                }

                // do cc validation for non-masked numbers
                if (!masked) {
                    // set up for cc validation, 
                    // (meant for use within Validator framework)

                    // from validator.xml
                    Arg arg = new Arg();
                    arg.setKey("all.prompt.creditCard");
                    arg.setPosition(0);
                    Field field = new Field();
                    field.addArg(arg);
                    field.setProperty("ccNumber");
                    field.setDepends("creditCard");

                    // from validator-rules.xml
                    ValidatorAction va = new ValidatorAction();
                    va.setName("creditCard");
                    va.setClassname("org.apache.struts.validator.FieldChecks");
                    va.setMethod("validateCreditCard");
                    va.setMethodParams("java.lang.Object, "
                            + "org.apache.commons.validator.ValidatorAction, "
                            + "org.apache.commons.validator.Field, "
                            + "org.apache.struts.action.ActionErrors, "
                            + "javax.servlet.http.HttpServletRequest");
                    va.setDepends("");
                    va.setMsg("errors.creditcard");

                    // do cc number validation
                    log.debug("doing credit card number validation");
                    FieldChecks.validateCreditCard(myForm, va, field, errors, 
                            request);

                    // return if credit card validation failed
                    if (!errors.isEmpty()) {
                        return "edit";
                    }
                }

                CreditCardDTO ccDto = new CreditCardDTO();
                ccDto.setNumber(ccNumber);
                ccDto.setName((String) myForm.get("ccName"));
                myForm.set("ccExpiry_day", "01"); // to complete the date
                ccDto.setExpiry(parseDate("ccExpiry", "payment.cc.date"));
                if (ccDto.getExpiry() != null) {
                    // the expiry can't be past today
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(ccDto.getExpiry());
                    cal.add(GregorianCalendar.MONTH, 1); // add 1 month
                    if (Calendar.getInstance().getTime().after(cal.getTime())) {
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("creditcard.error.expired", 
                                    "payment.cc.date"));
                    }
                }
                paymentDto.setCreditCard(ccDto);
                
                // this will be checked when the payment is sent
                session.setAttribute("tmp_process_now", 
                        (Boolean) myForm.get("chbx_processNow"));
                // validate required fields        
                required(ccDto.getNumber(), "payment.cc.number");
                required(ccDto.getExpiry(), "payment.cc.date");
                required(ccDto.getName(), "payment.cc.name");

                // make sure that the cc is valid before trying to get
                // the payment method from it
                if (errors.isEmpty()) {
                    paymentDto.setMethodId(Util.getPaymentMethod(
                            ccDto.getNumber()));
                }

            } else if (((String) myForm.get("method")).equals("ach")) {
            	AchDTO ach = new AchDTO();
            	ach.setAbaRouting((String) myForm.get("aba_code"));
            	ach.setBankAccount((String) myForm.get("account_number"));
            	ach.setAccountType((Integer) myForm.get("account_type"));
            	ach.setBankName((String) myForm.get("bank_name"));
            	ach.setAccountName((String) myForm.get("account_name"));
            	paymentDto.setAch(ach);
                //this will be checked when the payment is sent
                session.setAttribute("tmp_process_now",  new Boolean(true));
                // since it is one big form for all methods, we need to 
                // validate the required manually
                required(ach.getAbaRouting(), "ach.aba.prompt");
                required(ach.getBankAccount(), "ach.account_number.prompt");
                required(ach.getBankName(), "ach.bank_name.prompt");
                required(ach.getAccountName(), "ach.account_name.prompt");
                
                if (errors.isEmpty()) {
                    paymentDto.setMethodId(Constants.PAYMENT_METHOD_ACH);
                }
            }
            // set the customer id selected in the list (not the logged)
            paymentDto.setUserId((Integer) session.getAttribute(
                    Constants.SESSION_USER_ID));
            // specify if this is a normal payment or a refund
            paymentDto.setIsRefund(session.getAttribute("jsp_is_refund") == 
                    null ? new Integer(0) : new Integer(1));
            log.debug("refund = " + paymentDto.getIsRefund());
            // set the selected payment for refunds
            if (paymentDto.getIsRefund().intValue() == 1) {
                PaymentDTOEx refundPayment = (PaymentDTOEx) session.getAttribute(
                        Constants.SESSION_PAYMENT_DTO); 
                /*
                 * Right now, to process a real-time credit card refund it has to be to
                 * refund a previously done credit card payment. This could be
                 * changed, to say, refund using the customer's credit card no matter
                 * how the guy paid initially. But this might be subjet to the
                 * processor features.
                 * 
                 */
                if (((Boolean) myForm.get("chbx_processNow")).booleanValue() &&
                        ((String) myForm.get("method")).equals("cc") &&
                        (refundPayment == null || 
                         refundPayment.getCreditCard() == null ||
                         refundPayment.getAuthorization() == null ||
                         !refundPayment.getResultId().equals(Constants.RESULT_OK))) {

                     errors.add(ActionErrors.GLOBAL_ERROR,
                             new ActionError("refund.error.realtimeNoPayment", 
                                 "payment.cc.processNow"));
                    
                } else {
                    paymentDto.setPayment(refundPayment);
                }
                // refunds, I need to manually delete the list, because
                // in the end only the LIST_PAYMENT will be removed
                session.removeAttribute(Constants.SESSION_LIST_KEY + 
                        Constants.LIST_TYPE_REFUND);
            }
            
            // last, set the currency
            //If a related document is
            // set (invoice/payment) it'll take it from there. Otherwise it
            // wil inherite the one from the user
            paymentDto.setCurrencyId((Integer) myForm.get("currencyId"));
            if (paymentDto.getCurrencyId() == null) {
                try {
                    paymentDto.setCurrencyId(getUser(paymentDto.getUserId()).
                            getCurrencyId());
                } catch (FinderException e) {
                    throw new SessionInternalError(e);
                }
            }
            
            if (errors.isEmpty()) {
                // verify that this entity actually accepts this kind of 
                //payment method
                if (!((PaymentSession) remoteSession).isMethodAccepted((Integer)
                        session.getAttribute(Constants.SESSION_ENTITY_ID_KEY),
                        paymentDto.getMethodId())) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("payment.error.notAccepted", 
                                "payment.method"));
                }
            }

            // just in case there was an error
            log.debug("direct = " + (String) myForm.get("direct"));
            if (((String) myForm.get("direct")).equals("yes")) {
                retValue = "fromOrder";
            }

            log.debug("now payment methodId = " + paymentDto.getMethodId());
            log.debug("now paymentDto = " + paymentDto);
            log.debug("retValue = " + retValue);
 
        } else if (mode.equals("order")) {
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
        } else if (mode.equals("notification")) {
            if (request.getParameter("reload") != null) {
                // this is just a change of language the requires a reload
                // of the bean
                languageId = (Integer) myForm.get("language");
                return setup();
            }
            messageDto = new MessageDTO();
            messageDto.setLanguageId((Integer) myForm.get("language"));
            messageDto.setTypeId(selectedId);
            messageDto.setUseFlag((Boolean) myForm.get("chbx_use_flag"));
            // set the sections
            String sections[] = (String[]) myForm.get("sections");
            Integer sectionNumbers[] = (Integer[]) myForm.get("sectionNumbers");
            for (int f = 0; f < sections.length; f++) {
                messageDto.addSection(new MessageSection(sectionNumbers[f], 
                        sections[f]));
                log.debug("adding section:" + f + " "  + sections[f]);
            }
            log.debug("message is " + messageDto);
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

            if (mode.equals("item")) {
                // we pass a null language, so it'll pick up the one from
                // the entity
                Integer newItem = ((ItemSession) remoteSession).create(
                        itemDto, null);
                messageKey = "item.create.done";
                retValue = "list";
                // an item can be created to create a promotion
                if (((String) myForm.get("create")).equals("promotion")) {
                    retValue = "promotion";
                    // the id of the new item is needed later, when the
                    // promotion record is created
                    session.setAttribute(Constants.SESSION_ITEM_ID, 
                            newItem);
                }
                
            } else if (mode.equals("payment")) {
                // this is not an update, it's the previous step of the review
                // payments have no updates (unmodifiable transactions).
                
                if (paymentDto.getIsRefund().intValue() == 1) {
                    session.setAttribute(Constants.SESSION_PAYMENT_DTO_REFUND, 
                            paymentDto);
                } else {
                    session.setAttribute(Constants.SESSION_PAYMENT_DTO, paymentDto);
                }
                
                if (((String) myForm.get("create")).equals("payout")) {
                    retValue = "reviewPayout";
                } else {
                    retValue = "review";
                }
                messageKey = "payment.review";              
            }                 
        } else { // this is then an update
            retValue = "list";
            if (mode.equals("item")) {
                
                itemDto.setId(selectedId);
                ((ItemSession) remoteSession).update(executorId, itemDto, 
                        (Integer) myForm.get("language"));
                messageKey = "item.update.done";
            } else if (mode.equals("notification")) {
                ((NotificationSession) remoteSession).createUpdate(
                        messageDto, entityId);
                messageKey = "notification.message.update.done";
                retValue = "edit";
            } else if (mode.equals("parameter")) { /// for pluggable task parameters
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
                
        if (mode.equals("item")) {
            // the price is actually irrelevant in this call, since it's going
            // to be overwirtten by the user's input
            // in this case the currency doesn't matter, it
            ItemDTOEx dto = ((ItemSession) remoteSession).get(selectedId, 
                    languageId, null, null, entityId);
            // the prices have to be localized
            for (int f = 0; f < dto.getPrices().size(); f++) {
                ItemPriceDTOEx pr = (ItemPriceDTOEx) dto.getPrices().get(f);
                pr.setPriceForm(float2string(pr.getPrice()));
            }
            myForm.set("internalNumber", dto.getNumber());
            myForm.set("description", dto.getDescription());
            myForm.set("chbx_priceManual", new Boolean(dto.
                    getPriceManual().intValue() > 0 ? 
                            true : false));
            myForm.set("types", dto.getTypes());
            myForm.set("id", dto.getId());
            myForm.set("prices", dto.getPrices());
            myForm.set("language", languageId);
            if (dto.getPercentage() != null) {
                myForm.set("percentage", float2string(dto.getPercentage()));
            } else {
                // otherwise it will pickup the percentage of a 
                // previously edited item!
                myForm.set("percentage", null);
            }
        } else if (mode.equals("payment")) {
            CreditCardDTO ccDto = null;
            AchDTO achDto = null;
            PaymentInfoChequeDTO chequeDto = null;
            
            boolean isEdit = request.getParameter("submode") == null ?
                    false : request.getParameter("submode").equals("edit");
            
            // if an invoice was selected, pre-populate the amount field
            InvoiceDTO invoiceDto = (InvoiceDTO) session.getAttribute(
                    Constants.SESSION_INVOICE_DTO);
            PaymentDTOEx paymentDto = (PaymentDTOEx) session.getAttribute(
                    Constants.SESSION_PAYMENT_DTO);
            if (invoiceDto != null) {
                log.debug("setting payment with invoice:" + invoiceDto.getId());
                
                myForm.set("amount", float2string(invoiceDto.getBalance()));
                //paypal can't take i18n amounts
                session.setAttribute("jsp_paypay_amount", invoiceDto.getBalance());
                myForm.set("currencyId", invoiceDto.getCurrencyId());
            } else if (paymentDto != null) {
                // this works for both refunds and payouts
                log.debug("setting form with payment:" + paymentDto.getId());
                myForm.set("id", paymentDto.getId());
                myForm.set("amount", float2string(paymentDto.getAmount()));
                setFormDate("date", paymentDto.getPaymentDate());
                myForm.set("currencyId", paymentDto.getCurrencyId());
                ccDto = paymentDto.getCreditCard();
                achDto = paymentDto.getAch();
                chequeDto = paymentDto.getCheque();
            } else { // this is not an invoice selected, it's the first call
                log.debug("setting payment without invoice");
                // the date might come handy
                setFormDate("date", Calendar.getInstance().getTime());
                // make the default real-time
                myForm.set("chbx_processNow", new Boolean(true));
                // find out if this is a payment or a refund
            }
            boolean isRefund = session.getAttribute(
                        "jsp_is_refund") != null; 

            // populate the credit card fields with the cc in file
            // if this is a payment creation only
            if (!isRefund && !isEdit && 
                    ((String) myForm.get("ccNumber")).length() == 0) {
                // normal payment, get the selected user cc
                // if the user has a credit card, put it (this is a waste for
                // cheques, but it really doesn't hurt)
                log.debug("getting this user's cc");
                UserDTOEx user;
                try {
                    user = getUser((Integer) session.
                        getAttribute(Constants.SESSION_USER_ID));
                } catch (FinderException e) {
                    throw new SessionInternalError(e); 
                }
                ccDto = user.getCreditCard();
                achDto = user.getAch();
            } 
        
            
            if (ccDto != null) {
                String ccNumber = ccDto.getNumber();
                // mask cc number
                ccNumber = "************" + ccNumber.substring(
                        ccNumber.length() - 4);
                myForm.set("ccNumber", ccNumber);
                myForm.set("ccName", ccDto.getName());
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(ccDto.getExpiry());
                myForm.set("ccExpiry_month", String.valueOf(cal.get(
                        GregorianCalendar.MONTH) + 1));
                myForm.set("ccExpiry_year", String.valueOf(cal.get(
                        GregorianCalendar.YEAR)));
            }    
            
            if (achDto != null) {
            	myForm.set("aba_code", achDto.getAbaRouting());
                myForm.set("account_number", achDto.getBankAccount());
                myForm.set("bank_name", achDto.getBankName());
                myForm.set("account_name", achDto.getAccountName());
                myForm.set("account_type", achDto.getAccountType());
            }

            if (chequeDto != null) {
                myForm.set("bank", chequeDto.getBank());
                myForm.set("chequeNumber", chequeDto.getNumber());
                setFormDate("chequeDate", chequeDto.getDate());
            }

            // if this payment is direct from an order, continue with the
            // page without invoice list
            if (request.getParameter("direct") != null) {
                // the date won't be shown, and it has to be initialized
                setFormDate("date", Calendar.getInstance().getTime());
                myForm.set("method", "cc");
                
                // add the message 
                messages.add(ActionMessages.GLOBAL_MESSAGE,  
                        new ActionMessage("process.invoiceGenerated"));
                retValue = "fromOrder";
            }
            
            // if this is a payout, it has its own page
            if (request.getParameter("payout") != null) {
                retValue = "payout";
            } 
            
            // payment edition has a different layout
            if (isEdit) {
                retValue = "update";
            }

        } else if (mode.equals("order")) {
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
        } else if (mode.equals("notification")) {
            MessageDTO dto = ((NotificationSession) remoteSession).getDTO(
                    selectedId, languageId, entityId);
            myForm.set("language", languageId);
            myForm.set("chbx_use_flag", dto.getUseFlag());
            // now cook the sections for the form's taste
            String sections[] = new String[dto.getContent().length];
            Integer sectionNubmers[] = new Integer[dto.getContent().length];
            for (int f = 0; f < sections.length; f++) {
                sections[f] = dto.getContent()[f].getContent();
                sectionNubmers[f] = dto.getContent()[f].getSection();
            }
            myForm.set("sections", sections);
            myForm.set("sectionNumbers", sectionNubmers);
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
        String retValue = null;
       
        if (mode.equals("item")) {
            ((ItemSession) remoteSession).delete(executorId, selectedId);
        } else if (mode.equals("payment")) {
            PaymentDTOEx paymentDto = (PaymentDTOEx) session
                    .getAttribute(Constants.SESSION_PAYMENT_DTO);
            Integer id = paymentDto.getId();
            ((PaymentSession) (remoteSession)).deletePayment(id);
        }
                
        session.removeAttribute(formName); 
        retValue = "deleted";
        
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
        
        if (mode.equals("payment")) {
            session.removeAttribute(Constants.SESSION_INVOICE_DTO);
            session.removeAttribute(Constants.SESSION_PAYMENT_DTO);
        }
        
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
