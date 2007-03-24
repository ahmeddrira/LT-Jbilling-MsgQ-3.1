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
        }
    }
    
    public String toString() {
        return "credit card = [" + creditCard + "] contact = [" +
                contact + "] type = [" + role + "] language = [" +
                language + "] status = [" + status + "] statusId = [" +
                statusId + "] subscriberStatusId = [" + subscriberStatusId +
                "] roleId = [" + mainRoleId + "] " +  super.toString();
                
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


}
