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

package com.sapienter.jbilling.server.util;

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
import com.sapienter.jbilling.server.entity.ReportUserDTO;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.item.CurrencyBL;
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
            dto.setLastInvoiceId(invoiceBL.getLastByUser(user.getUserId()));
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
            customerDto.setInvoiceChild(user.getCustomer().getInvoiceChild() == null ?
                    new Integer(0) : user.getCustomer().getInvoiceChild());
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
