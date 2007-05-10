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

package com.sapienter.jbilling.server.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.BillingProcessEntityLocal;
import com.sapienter.jbilling.interfaces.CreditCardEntityLocal;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;
import com.sapienter.jbilling.interfaces.LanguageEntityLocal;
import com.sapienter.jbilling.interfaces.LanguageEntityLocalHome;
import com.sapienter.jbilling.interfaces.MenuOptionEntityLocal;
import com.sapienter.jbilling.interfaces.MenuOptionEntityLocalHome;
import com.sapienter.jbilling.interfaces.OrderBillingTypeEntityLocal;
import com.sapienter.jbilling.interfaces.OrderBillingTypeEntityLocalHome;
import com.sapienter.jbilling.interfaces.OrderEntityLocal;
import com.sapienter.jbilling.interfaces.OrderEntityLocalHome;
import com.sapienter.jbilling.interfaces.OrderLineEntityLocal;
import com.sapienter.jbilling.interfaces.OrderLineEntityLocalHome;
import com.sapienter.jbilling.interfaces.OrderPeriodEntityLocal;
import com.sapienter.jbilling.interfaces.OrderPeriodEntityLocalHome;
import com.sapienter.jbilling.interfaces.OrderProcessEntityLocal;
import com.sapienter.jbilling.interfaces.OrderStatusEntityLocal;
import com.sapienter.jbilling.interfaces.OrderStatusEntityLocalHome;
import com.sapienter.jbilling.interfaces.ReportEntityLocal;
import com.sapienter.jbilling.interfaces.ReportEntityLocalHome;
import com.sapienter.jbilling.interfaces.ReportFieldEntityLocal;
import com.sapienter.jbilling.interfaces.ReportTypeEntityLocal;
import com.sapienter.jbilling.interfaces.ReportUserEntityLocal;
import com.sapienter.jbilling.interfaces.RoleEntityLocal;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.interfaces.UserEntityLocalHome;
import com.sapienter.jbilling.server.entity.BillingProcessDTO;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.OrderLineDTO;
import com.sapienter.jbilling.server.entity.OrderProcessDTO;
import com.sapienter.jbilling.server.entity.ReportUserDTO;
import com.sapienter.jbilling.server.entity.UserDTO;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.item.CurrencyBL;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.order.OrderDTOEx;
import com.sapienter.jbilling.server.order.OrderLineComparator;
import com.sapienter.jbilling.server.order.OrderLineDTOEx;
import com.sapienter.jbilling.server.process.OrderProcessIdComparator;
import com.sapienter.jbilling.server.report.Field;
import com.sapienter.jbilling.server.report.FieldComparator;
import com.sapienter.jbilling.server.report.ReportDTOEx;
import com.sapienter.jbilling.server.user.AchBL;
import com.sapienter.jbilling.server.user.CustomerDTOEx;
import com.sapienter.jbilling.server.user.EntityBL;
import com.sapienter.jbilling.server.user.MenuOption;
import com.sapienter.jbilling.server.user.PartnerBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.UserDTOEx;

/**
 *
 * This is the class to provide initialization of DTOs with
 * the entities values. It can't be instantiated, all the 
 * methods are static
 * 
 * @author Emil
 */

/*
 * This code can't be testest through a JUnit test case 
 * because it will start using local interfaces ... from
 * a remote client ;)
 */
public class DTOFactory {

    /**
     * The constructor is private, do it doesn't get instantiated.
     * All the methods then are static.
     */
    private DTOFactory() {
    }

    /**
     * Get's an entity bean of the user, using the username,
     * and then creates a DTO with that data.
     * It is not setting permissions or menu
     * @param username
     * @return UserDTO
     * @throws HomeFactoryException
     * @throws CreateException
     * @throws NamingException
     * @throws FinderException
     */
    public static UserDTOEx getUserDTO(String username, Integer entityId)
        throws CreateException, NamingException, FinderException,
            SessionInternalError {

        Logger log = Logger.getLogger(DTOFactory.class);
        log.debug("getting the user " + username);

        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        UserEntityLocalHome UserHome =
                (UserEntityLocalHome) EJBFactory.lookUpLocalHome(
                UserEntityLocalHome.class,
                UserEntityLocalHome.JNDI_NAME);

        UserEntityLocal user = UserHome.findByUserName(username, entityId);
        return getUserDTOEx(user);
    }

    public static UserDTOEx getUserDTOEx(Integer userId)
            throws CreateException, NamingException, FinderException,
                SessionInternalError {

        Logger log = Logger.getLogger(DTOFactory.class);
        log.debug("getting the user " + userId);

        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        UserEntityLocalHome userHome =
                (UserEntityLocalHome) EJBFactory.lookUpLocalHome(
                UserEntityLocalHome.class,
                UserEntityLocalHome.JNDI_NAME);

        UserEntityLocal user = userHome.findByPrimaryKey(userId);
        return getUserDTOEx(user);
    }


    public static UserDTOEx getUserDTOEx(UserEntityLocal user) 
            throws NamingException, FinderException, SessionInternalError { 
        UserDTOEx dto = new UserDTOEx(user.getUserId(), 
                user.getEntity().getId(), user.getUserName(),
                user.getPassword(), user.getDeleted(), 
                user.getLanguageIdField(),
                null, user.getCurrencyId(), user.getCreateDateTime(),
                user.getLastStatusChange(), user.getLastLogin(),
                user.getFailedAttmepts()); // I'll set all the roles later

        // get the status
        dto.setStatusId(user.getStatus().getId());
        dto.setStatusStr(user.getStatus().getDescription(user.getLanguageIdField()));
        // the subscriber status
        dto.setSubscriptionStatusId(user.getSubscriptionStatus().getId());
        dto.setSubscriptionStatusStr(user.getSubscriptionStatus().getDescription(
                user.getLanguageIdField()));
        
        // add the roles
        Integer mainRole = new Integer(1000);
        String roleStr = null;
        for (Iterator it = user.getRoles().iterator(); it.hasNext(); ) {
            RoleEntityLocal role = (RoleEntityLocal) it.next();
            dto.getRoles().add(role.getId());
            // the main role is the smallest of them, so they have to be ordered in the
            // db in ascending order (small = important);
            if (role.getId().compareTo(mainRole) < 0) {
                mainRole = role.getId();
                roleStr = role.getTitle(user.getLanguageIdField());
            }
        }
        dto.setMainRoleId(mainRole);
        dto.setMainRoleStr(roleStr);

        // now get the language
        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        LanguageEntityLocalHome languageHome =
                (LanguageEntityLocalHome) EJBFactory.lookUpLocalHome(
                LanguageEntityLocalHome.class,
                LanguageEntityLocalHome.JNDI_NAME);
        LanguageEntityLocal language = languageHome.findByPrimaryKey(
                user.getLanguageIdField());
        dto.setLanguageStr(language.getDescription());
        
        // add the last invoice id
        InvoiceBL invoiceBL = new InvoiceBL();
        try {
            dto.setLastInvoiceId(invoiceBL.getLastByUser(dto.getUserName(),
                    user.getEntity().getId()));
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
        // make sure the currency is set
        if (dto.getCurrencyId() == null) {
            // defaults to the one from the entity
            dto.setCurrencyId(user.getEntity().getCurrencyId());
        }
        CurrencyBL currency = new CurrencyBL(dto.getCurrencyId());
        dto.setCurrencySymbol(currency.getEntity().getSymbol());
        dto.setCurrencyName(currency.getEntity().getDescription(
                dto.getLanguageId()));
        
        // add a credit card if available
        if (!user.getCreditCard().isEmpty()) {
            dto.setCreditCard(getCreditCardDTO((CreditCardEntityLocal)
                    user.getCreditCard().iterator().next()));
        }   
        
        if (user.getAch() != null) {
        	AchBL ach = new AchBL(user.getAch());
            dto.setAch(ach.getDTO());
        }
        
        // if this is a customer, add its dto
        if (user.getCustomer() != null) {
            CustomerDTOEx customerDto = new CustomerDTOEx();
            customerDto.setId(user.getCustomer().getId());
            if (user.getCustomer().getPartner() != null) {
                customerDto.setPartnerId(user.getCustomer().getPartner().getId());
            }
            customerDto.setReferralFeePaid(
                    user.getCustomer().getReferralFeePaid());
            customerDto.setNotes(user.getCustomer().getNotes());
            customerDto.setInvoiceDeliveryMethodId(
                    user.getCustomer().getInvoiceDeliveryMethodId());
            customerDto.setDueDateUnitId(
                    user.getCustomer().getDueDateUnitId());
            customerDto.setDueDateValue(
                    user.getCustomer().getDueDateValue());
            customerDto.setDfFm(user.getCustomer().getDfFm());
            customerDto.setExcludeAging(user.getCustomer().getExcludeAging());
            customerDto.setIsParent(user.getCustomer().getIsParent());
            if (user.getCustomer().getParent() != null) {
                customerDto.setParentId(user.getCustomer().getParent().
                        getUser().getUserId());
            } else if (user.getCustomer().getIsParent() != null &&
                    user.getCustomer().getIsParent().intValue() == 1) {
                customerDto.setTotalSubAccounts(new Integer(user.getCustomer().
                        getChildren().size()));
            }
            
            dto.setCustomerDto(customerDto);
        }
        
        // if this is a partner, add its dto
        if (user.getPartner() != null) {
            PartnerBL partner = new PartnerBL(user.getPartner());
            dto.setPartnerDto(partner.getDTO());
        }
        
        // the locale will be handy
        try {
            UserBL bl = new UserBL(user);
            dto.setLocale(bl.getLocale());
        } catch (Exception e) {
            dto.setLocale(new Locale("en"));
        }
        
        return dto;
    }
    
    public static CreditCardDTO getCreditCardDTO(CreditCardEntityLocal cc) {
        return new CreditCardDTO(cc.getId(), cc.getNumber(), cc.getExpiry(), 
                cc.getName(), cc.getType(), cc.getDeleted(), cc.getSecurityCode());
    }

    public static BillingProcessDTO getBillingProcessDTO(
            BillingProcessEntityLocal process) {
        return new BillingProcessDTO(process.getId(), process.getEntityId(),
                process.getBillingDate(), process.getPeriodUnitId(),
                process.getPeriodValue(), process.getIsReview(), 
                process.getRetriesToDo());
    }
    
    /*
    public static OrderDTO getOrderDTO(Integer orderId)
        throws CreateException, NamingException, FinderException {

        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        OrderEntityLocalHome orderHome =
                (OrderEntityLocalHome) EJBFactory.lookUpLocalHome(
                OrderEntityLocalHome.class,
                OrderEntityLocalHome.JNDI_NAME);

        OrderEntityLocal order = orderHome.findByPrimaryKey(orderId);
        return new OrderDTO(orderId, order.getBillingTypeId(), 
        		order.getNotify(),
                order.getActiveSince(), order.getActiveUntil(), 
                order.getCreateDate(), order.getNextBillableDay(),
                order.getCreatedBy(), order.getStatusId(), order.getDeleted(),
                order.getCurrencyId(), order.getLastNotified(),
                order.getNotificationStep(), order.getDueDateUnitId(),
                order.getDueDateValue(), order.getDfFm(), 
                order.getAnticipatePeriods(), order.getOwnInvoice());

    }
    */
    
    public static OrderLineDTOEx getOrderLineDTOEx(OrderLineEntityLocal line) {
        return new OrderLineDTOEx(line.getId(),
                line.getItemId(), line.getDescription(),
                line.getAmount(), line.getQuantity(),
                line.getPrice(), line.getItemPrice(),
                line.getCreateDate(), line.getDeleted(),
                line.getType().getId(), new Boolean(line.getType().
                    getEditable().intValue() == 1));
    }
    
    public static OrderDTOEx getOrderDTOEx(Integer orderId, Integer languageId)
            throws CreateException, NamingException, FinderException,
                SessionInternalError {
        OrderDTOEx retValue;
        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        OrderEntityLocalHome orderHome =
                (OrderEntityLocalHome) EJBFactory.lookUpLocalHome(
                OrderEntityLocalHome.class,
                OrderEntityLocalHome.JNDI_NAME);

        OrderEntityLocal order = orderHome.findByPrimaryKey(orderId);
        retValue = new OrderDTOEx(orderId, order.getBillingTypeId(), 
        		order.getNotify(),
                order.getActiveSince(), order.getActiveUntil(), 
                order.getCreateDate(), order.getNextBillableDay(),
                order.getCreatedBy(), order.getStatusId(), order.getDeleted(),
                order.getCurrencyId(), order.getLastNotified(),
                order.getNotificationStep(), order.getDueDateUnitId(),
                order.getDueDateValue());
        retValue.setUser(new UserDTO(order.getUser().getUserId(),
                order.getUser().getUserName(), order.getUser().getPassword(),
                order.getUser().getDeleted(), 
                order.getUser().getLanguageIdField(), 
                order.getUser().getCurrencyId(), 
                order.getUser().getCreateDateTime(),
                order.getUser().getLastStatusChange(),
                order.getUser().getLastLogin(),
                order.getUser().getFailedAttmepts()));
        retValue.setPeriodId(order.getPeriod().getId());
        retValue.setDfFm(order.getDfFm());
        retValue.setAnticipatePeriods(order.getAnticipatePeriods());
        retValue.setOwnInvoice(order.getOwnInvoice());
        retValue.setNotes(order.getNotes());
        retValue.setNotesInInvoice(order.getNotesInInvoice());
        
        // add the lines
        BigDecimal orderTotal = new BigDecimal("0"); // the total will be calculated for display
        Integer entityId = order.getUser().getEntity().getId();
        for (Iterator lines = order.getOrderLines().iterator(); 
                lines.hasNext();) {
            OrderLineEntityLocal line = (OrderLineEntityLocal) lines.next();
            if (line.getDeleted().equals(new Integer(0))) {
                OrderLineDTOEx lineDto = getOrderLineDTOEx(line);
                orderTotal = orderTotal.add(new BigDecimal(line.getAmount().toString()));
                if (line.getItemId() != null) {
                    // the language is not important
                    ItemBL itemBL = new ItemBL(line.getItemId());
                    lineDto.setItem(itemBL.getDTO(languageId, 
                            retValue.getUser().getUserId(), entityId, 
                            order.getCurrencyId())); 
                    if (lineDto.getItem().getPromoCode() != null) {
                        retValue.setPromoCode(lineDto.getItem().getPromoCode());
                    }
                }
                retValue.getOrderLines().add(lineDto);
            }
        }
        
        Collections.sort(retValue.getOrderLines(), new OrderLineComparator());
        
        // add the invoices, periods and processes
        // make sure they are in order, so when they are displayed they
        // make some sense ;)
        Collection processes = order.getProcesses();
        Vector processesArray = new Vector(processes);
        Collections.sort(processesArray, new OrderProcessIdComparator());
        
        for (Iterator it = processesArray.iterator(); 
                it.hasNext();) {
            OrderProcessEntityLocal orderProcess = 
                    (OrderProcessEntityLocal) it.next();
                    
            // exclude those process/invoices that belong to a review
            if (orderProcess.getIsReview().intValue() == 1) {
                continue;
            }
            
            // invoices
            InvoiceEntityLocal invoice = orderProcess.getInvoice();
            InvoiceBL invoiceBl = new InvoiceBL(invoice);
            retValue.getInvoices().add(invoiceBl.getDTO());
                    
            // period
            retValue.getPeriods().add(new OrderProcessDTO(orderProcess.getId(),
                    orderProcess.getPeriodStart(), orderProcess.getPeriodEnd(),
                    orderProcess.getPeriodsIncluded(), new Integer(0),
                    orderProcess.getOrigin()));

            // process
            BillingProcessEntityLocal process = 
                    (BillingProcessEntityLocal) orderProcess.getProcess();
            // since and invoice now can be generated without a billing process
            // (online from the gui), this can be null
            if (process != null) {
                retValue.getProcesses().add(new BillingProcessDTO(process.getId(),
                        process.getEntityId(), process.getBillingDate(), 
                        process.getPeriodUnitId(),
                        process.getPeriodValue(), process.getIsReview(),
                        process.getRetriesToDo()));
            } else {
                retValue.getProcesses().add(null);
            }
        }
        
        // set the human readable strings
        retValue.setPeriodStr(getPeriodString(retValue.getPeriodId(),
                languageId));
        retValue.setBillingTypeStr(getBillingTypeString(
                retValue.getBillingTypeId(), languageId));
        // the status would benefit from a cmr ...
        OrderStatusEntityLocalHome orderStatusHome =
                (OrderStatusEntityLocalHome) EJBFactory.lookUpLocalHome(
                OrderStatusEntityLocalHome.class,
                OrderStatusEntityLocalHome.JNDI_NAME);
        OrderStatusEntityLocal orderStatus = orderStatusHome.findByPrimaryKey(
                order.getStatusId());
        retValue.setStatusStr(orderStatus.getDescription(languageId));
        retValue.setTotal(new Float(orderTotal.floatValue()));
        retValue.setTimeUnitStr(Util.getPeriodUnitStr(
                retValue.getDueDateUnitId(), languageId));
        
        // we need the currency symbol to show
        CurrencyBL currencyBL = new CurrencyBL(order.getCurrencyId());
        retValue.setCurrencySymbol(currencyBL.getEntity().getSymbol());
        retValue.setCurrencyName(currencyBL.getEntity().getDescription(
                languageId));
        
        return retValue;
    }
    
    public static String getPeriodString(Integer periodId, Integer languageId) 
            throws NamingException, FinderException {
        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        OrderPeriodEntityLocalHome orderHome =
                (OrderPeriodEntityLocalHome) EJBFactory.lookUpLocalHome(
                OrderPeriodEntityLocalHome.class,
                OrderPeriodEntityLocalHome.JNDI_NAME);

        OrderPeriodEntityLocal period = orderHome.findByPrimaryKey(periodId);
        
        return period.getDescription(languageId);
    }
    
    public static String getBillingTypeString(Integer typeId, 
            Integer languageId) throws NamingException, FinderException {
        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        OrderBillingTypeEntityLocalHome orderHome =
                (OrderBillingTypeEntityLocalHome) EJBFactory.lookUpLocalHome(
                OrderBillingTypeEntityLocalHome.class,
                OrderBillingTypeEntityLocalHome.JNDI_NAME);

        OrderBillingTypeEntityLocal period = 
                orderHome.findByPrimaryKey(typeId);

        return period.getDescription(languageId);
    }
    
    
    public static OrderLineDTO getOrderLineDTO(Integer lineId) 
            throws CreateException, NamingException, FinderException {    
        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        OrderLineEntityLocalHome orderHome =
                (OrderLineEntityLocalHome) EJBFactory.lookUpLocalHome(
                OrderLineEntityLocalHome.class,
                OrderLineEntityLocalHome.JNDI_NAME);

         OrderLineEntityLocal orderLine = orderHome.findByPrimaryKey(lineId);
         return new OrderLineDTO(lineId, orderLine.getItemId(), 
                orderLine.getDescription(), orderLine.getAmount(),
                orderLine.getQuantity(), orderLine.getPrice(), 
                orderLine.getItemPrice(), orderLine.getCreateDate(),
                orderLine.getDeleted());
        
    }
    
    public static Vector invoiceEJB2DTO(Collection invoices) {
        Vector dtos = new Vector();
        
        for (Iterator it = invoices.iterator(); it.hasNext();) {
            InvoiceEntityLocal invoiceEJB = (InvoiceEntityLocal) it.next();
            try {
                InvoiceBL invoice = new InvoiceBL(invoiceEJB);
                dtos.add(invoice.getDTO());
            } catch(Exception e) {}
        }
        
        return dtos;
    }
    
    public static ReportDTOEx getReportDTOEx(Integer reportId, 
            Integer entityId) 
            throws NamingException, FinderException, SessionInternalError {
        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        ReportEntityLocalHome reportHome =
                (ReportEntityLocalHome) EJBFactory.lookUpLocalHome(
                ReportEntityLocalHome.class,
                ReportEntityLocalHome.JNDI_NAME);

        ReportEntityLocal report = reportHome.findByPrimaryKey(reportId);

        EntityBL entity = new EntityBL(entityId);
        ReportDTOEx dto = getReportDTOEx(report, entity.getLocale());
        Collection fields = report.getFields();
        
        for (Iterator it = fields.iterator(); it.hasNext();) {
            ReportFieldEntityLocal field = (ReportFieldEntityLocal) it.next();
            Field fieldDto = getFieldDTO(field);
            fieldDto.setWhereValue(field.getWhereValue());
            
            dto.addField(fieldDto);
        }
        // after the dto is complete, the fields have to be sorted
        // acording to their position attribute        
        Collections.sort(dto.getFields(), new FieldComparator());

        return dto;
    }
    
    public static Field getFieldDTO(ReportFieldEntityLocal field) {
        Field dto = new Field(field.getTable(), field.getColumn(), 
                field.getDataType());
 
        dto.setFunction(field.getFunction());
        dto.setFunctionable(field.getFunctionable());
        dto.setIsGrouped(field.getIsGrouped());
        dto.setIsShown(field.getIsShown());
        dto.setOperator(field.getOperator());
        dto.setOperatorable(field.getOperatorable());
        dto.setOrdenable(field.getOrdenable());
        dto.setOrderPosition(field.getOrderPosition());
        dto.setPosition(field.getPosition());
        dto.setSelectable(field.getSelectable());
        dto.setTitleKey(field.getTitleKey());
        dto.setWherable(field.getWherable());
        dto.setWhereValue(field.getWhereValue());
        
        return dto;
    }
 
    public static ReportDTOEx getReportDTOEx(ReportEntityLocal report,
            Locale locale) { 
    
        ReportDTOEx dto = new ReportDTOEx(report.getTitleKey(), 
                report.getInstructionsKey(), report.getTables(),
                report.getWhere(), locale);
            
        dto.setIdColumn(report.getIdColumn());
        dto.setId(report.getId());
        dto.setLink(report.getLink());
        
        return dto;
    }
  
    public static Collection reportEJB2DTOEx(Collection reports, 
            boolean filter) {
        Vector dtos = new Vector();
        
        for (Iterator it = reports.iterator(); it.hasNext();) {
            ReportEntityLocal reportEJB = (ReportEntityLocal) it.next();
            
            if (filter) {
                for (Iterator it2 = reportEJB.getTypes().iterator(); 
                        it2.hasNext(); ) {
                    ReportTypeEntityLocal type = 
                            (ReportTypeEntityLocal) it2.next();
                    if (type.getShowable().intValue() == 1) {
                        dtos.add(getReportDTOEx(reportEJB, null));
                        break;
                    }
                }
            } else {
                dtos.add(getReportDTOEx(reportEJB, null));
            }
            
        }
        
        return dtos;
    }
    
    public static Collection reportEJB2DTO(Collection reports) {
        Vector dtos = new Vector();
        
        for (Iterator it = reports.iterator(); it.hasNext();) {
            ReportEntityLocal reportEJB = (ReportEntityLocal) it.next();
            dtos.add(getReportDTOEx(reportEJB, null));
        }
        
        return dtos;
    }
    

    public static Collection reportUserEJB2DTO(Collection reports) {
        Vector dtos = new Vector();
        
        for (Iterator it = reports.iterator(); it.hasNext();) {
            ReportUserEntityLocal reportEJB = 
                    (ReportUserEntityLocal) it.next();
            dtos.add(getReportUserDTO(reportEJB));
        }
        
        return dtos;
    }
 
    public static ReportUserDTO getReportUserDTO(
            ReportUserEntityLocal rUser) {
        return new ReportUserDTO(rUser.getId(), 
                rUser.getCreateDatetime(), rUser.getTitle());
    }
 
    public static MenuOption getMenuOption(Integer id, Integer languageId) 
            throws NamingException, FinderException{
        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        MenuOptionEntityLocalHome menuHome =
                (MenuOptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                MenuOptionEntityLocalHome.class,
                MenuOptionEntityLocalHome.JNDI_NAME);
        
        MenuOptionEntityLocal option = menuHome.findByPrimaryKey(id);
        MenuOption opt = new MenuOption();
        opt.setId(option.getId());
        opt.setLevel(option.getLevel());
        opt.setLink(option.getLink());
        opt.setDisplay(option.getDisplay(languageId));
        opt.setParentId((option.getParent() == null ? null : 
                option.getParent().getId()));
        
        return opt;
    }
}
