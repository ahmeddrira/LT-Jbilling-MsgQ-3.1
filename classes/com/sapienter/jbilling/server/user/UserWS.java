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

/*
 * Created on Dec 18, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.user;

import java.io.Serializable;

import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.UserDTO;

/**
 * @author Emil
 * @jboss-net.xml-schema urn="sapienter:UserWS"
 */
public class UserWS extends UserDTO implements Serializable {
    private CreditCardDTO creditCard = null;
    private ContactWS contact = null;
    private String role = null;
    private String language = null;
    private String status = null;
    private Integer mainRoleId = null;
    private Integer statusId = null;
    private Integer subscriberStatusId = null;
    private Integer partnerId = null;
    private Integer parentId = null;
    private Boolean isParent = null;
    private Boolean invoiceChild = null;
    
    public Integer getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }
    // to comply with the Java Bean spec.
    public UserWS() {
    }
    
    public UserWS(UserDTOEx dto) {
        super(dto);
        creditCard = dto.getCreditCard();
        role = dto.getMainRoleStr();
        mainRoleId = dto.getMainRoleId();
        language = dto.getLanguageStr();
        status = dto.getStatusStr();
        role = dto.getMainRoleStr();
        statusId = dto.getStatusId();
        subscriberStatusId = dto.getSubscriptionStatusId();
        if (dto.getCustomerDto() != null) {
            partnerId = dto.getCustomerDto().getPartnerId();
            parentId = dto.getCustomerDto().getParentId();
            isParent = dto.getCustomerDto().getIsParent() == null ? false : 
                dto.getCustomerDto().getIsParent().equals(new Integer(1));
            invoiceChild = dto.getCustomerDto().getInvoiceChild() == null ? false : 
                dto.getCustomerDto().getInvoiceChild().equals(new Integer(1));
        }
    }
    
    public String toString() {
        return "credit card = [" + creditCard + "] contact = [" +
                contact + "] type = [" + role + "] language = [" +
                language + "] status = [" + status + "] statusId = [" +
                statusId + "] subscriberStatusId = [" + subscriberStatusId +
                "] roleId = [" + mainRoleId + "] " +  " parentId = [" + parentId +
                "] " + super.toString();
                
    }
    /**
     * @return
     */
    public ContactWS getContact() {
        return contact;
    }

    /**
     * @param contact
     */
    public void setContact(ContactWS contact) {
        this.contact = contact;
    }

    /**
     * @return
     */
    public CreditCardDTO getCreditCard() {
        return creditCard;
    }

    /**
     * @param creditCard
     */
    public void setCreditCard(CreditCardDTO creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return
     */
    public String getRole() {
        return role;
    }

    /**
     * @param type
     */
    public void setRole(String type) {
        this.role = type;
    }

    /**
     * @return
     */
    public Integer getMainRoleId() {
        return mainRoleId;
    }

    /**
     * @param mainRoleId
     */
    public void setMainRoleId(Integer mainRoleId) {
        this.mainRoleId = mainRoleId;
    }

    /**
     * @return
     */
    public Integer getStatusId() {
        return statusId;
    }

    /**
     * @param statusId
     */
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    public Integer getSubscriberStatusId() {
        return subscriberStatusId;
    }
    public void setSubscriberStatusId(Integer subscriberStatusId) {
        this.subscriberStatusId = subscriberStatusId;
    }
    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public Boolean getIsParent() {
        return isParent;
    }
    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }
    public Boolean getInvoiceChild() {
        return invoiceChild;
    }
    public void setInvoiceChild(Boolean invoiceChild) {
        this.invoiceChild = invoiceChild;
    }


}
