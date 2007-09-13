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

package com.sapienter.jbilling.server.user;

import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.common.PermissionIdComparator;
import com.sapienter.jbilling.common.PermissionTypeIdComparator;
import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.PermissionDTO;
import com.sapienter.jbilling.server.entity.UserDTO;

/**
 * @author emilc
 */
public final class UserDTOEx extends UserDTO implements java.io.Serializable  {

    // constants
    
    // user status in synch with db table user_status
    public static final Integer STATUS_ACTIVE = new Integer(1); // this HAS to be the very first status
    public static final Integer STATUS_SUSPENDED = new Integer(5); 
    public static final Integer STATUS_DELETED = new Integer(8); // this HAS to be the last status
    
    // subscriber status in synch with db table subscriber_status
    public static final Integer SUBSCRIBER_ACTIVE = new Integer(1); 
    public static final Integer SUBSCRIBER_PENDING_UNSUBSCRIPTION = new Integer(2);
    public static final Integer SUBSCRIBER_UNSUBSCRIBED = new Integer(3);
    public static final Integer SUBSCRIBER_PENDING_EXPIRATION= new Integer(4);
    public static final Integer SUBSCRIBER_EXPIRED = new Integer(5);
    public static final Integer SUBSCRIBER_NONSUBSCRIBED = new Integer(6);
    public static final Integer SUBSCRIBER_DISCONTINUED = new Integer(7);
    
    // private fields
    private Integer entityId = null;
    private Menu menu = null;
    private Vector permissions = null;
    private Vector permissionsTypeId = null; // same as before but sorted by type
    private Vector<Integer> roles = null;
    private Integer mainRoleId = null;
    private String mainRoleStr = null;
    private String languageStr = null;
    private Integer statusId = null;
    private String statusStr = null;
    private Integer subscriptionStatusId = null;
    private String subscriptionStatusStr = null;
    private CreditCardDTO creditCard = null; 
    private AchDTO ach = null;
    private Integer lastInvoiceId = null;
    private String currencySymbol = null;
    private String currencyName = null;
    private Locale locale = null;
    // extended fields
    private CustomerDTOEx customerDto = null;
    private PartnerDTOEx partnerDto = null;


    /**
     * Constructor for UserDTOEx.
     * @param userId
     * @param entityId
     * @param userName
     * @param password
     * @param deleted
     */
    public UserDTOEx(Integer userId, Integer entityId, String userName,
            String password, Integer deleted, Integer language, Integer roleId,
            Integer currencyId, Date creation, Date modified, Date lLogin, 
            Integer failedAttempts) {
        // set the base dto fields
        super(userId, userName, password, deleted, language, currencyId, 
                creation, modified, lLogin, failedAttempts);
        // the entity id
        setEntityId(entityId);
        // the permissions are defaulted to nothing
        permissions = new Vector();
        roles = new Vector<Integer>();
        if (roleId != null) {
            // we ask for at least one role for this user
            roles.add(roleId);
            mainRoleId = roleId;
        }
        
    }
    
    public UserDTOEx(UserWS dto, Integer entityId) {
        super(dto);
        creditCard = dto.getCreditCard();
        mainRoleStr = dto.getRole();
        mainRoleId = dto.getMainRoleId();
        languageStr = dto.getLanguage();
        statusStr = dto.getStatus();
        statusId = dto.getStatusId();
        subscriptionStatusId = dto.getSubscriberStatusId();
        this.entityId = entityId;
        
        roles = new Vector<Integer>();
        roles.add(mainRoleId);
        
        if (mainRoleId.equals(Constants.TYPE_CUSTOMER)) {
            customerDto = new CustomerDTOEx();
            customerDto.setPartnerId(dto.getPartnerId());
            customerDto.setParentId(dto.getParentId());
            customerDto.setIsParent(dto.getIsParent() == null ? new Integer(0) :
                dto.getIsParent().booleanValue() ? new Integer(1) : new Integer(0));
            customerDto.setInvoiceChild(dto.getInvoiceChild() == null ? new Integer(0) :
                dto.getInvoiceChild().booleanValue() ? new Integer(1) : new Integer(0));
            if (dto.getCreditCard() != null) {
                customerDto.setAutoPaymentType(Constants.AUTO_PAYMENT_TYPE_CC);
            }
        }
    }
    
    public UserDTOEx() {
        super();
    }

    // this expects the Vector to be sorted already
    public void setPermissions(Vector permissions) {
        this.permissions = permissions;
    }
    
    public boolean isGranted(Integer permissionId) {
        PermissionDTO permission = new PermissionDTO(permissionId, null, null);
        if (Collections.binarySearch(permissions, permission,
                new PermissionIdComparator()) >= 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Verifies that a permision for the given type/foreign_id has been
     * granted. This is defenetly an expensive 
     * @param typeId
     * @param foreignId
     * @return
     */
    public boolean isGranted(Integer typeId, Integer foreignId) {
        if (permissionsTypeId == null) {
            permissionsTypeId = (Vector) permissions.clone();
            Collections.sort(permissionsTypeId, 
                    new PermissionTypeIdComparator());
          /*
            Logger.getLogger(UserDTOEx.class).debug("Permissions now = " +
                    permissionsTypeId);
                    */
        }
        boolean retValue;
        PermissionDTO permission = new PermissionDTO(null, typeId, foreignId);
        if (Collections.binarySearch(permissionsTypeId, permission,
                new PermissionTypeIdComparator()) >= 0) {
            retValue = true;
        } else {
            retValue = false;
        }
        
        /*
        Logger.getLogger(UserDTOEx.class).debug("permission for type = " + 
                typeId + " foreignId = " + foreignId + " result = " +
                retValue);
        */
        return retValue;
    }

    public Vector getPermissions() {
        return permissions;
    }
    /**
     * Returns the entityId.
     * @return Integer
     */
    public Integer getEntityId() {
        return entityId;
    }

    /**
     * Sets the entityId.
     * @param entityId The entityId to set
     */
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    /**
     * @return
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * @param menu
     */
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    /**
     * @return
     */
    public Vector getRoles() {
        return roles;
    }

    /**
     * @param vector
     */
    public void setRoles(Vector vector) {
        roles = vector;
    }

    /**
     * @return
     */
    public Integer getMainRoleId() {
        return mainRoleId;
    }

    /**
     * @return
     */
    public String getMainRoleStr() {
        return mainRoleStr;
    }

    /**
     * @param integer
     */
    public void setMainRoleId(Integer integer) {
        mainRoleId = integer;
    }

    /**
     * @param string
     */
    public void setMainRoleStr(String string) {
        mainRoleStr = string;
    }

    /**
     * @return
     */
    public String getLanguageStr() {
        return languageStr;
    }

    /**
     * @param string
     */
    public void setLanguageStr(String string) {
        languageStr = string;
    }

    /**
     * @return
     */
    public Integer getStatusId() {
        return statusId;
    }

    /**
     * @return
     */
    public String getStatusStr() {
        return statusStr;
    }

    /**
     * @param integer
     */
    public void setStatusId(Integer integer) {
        statusId = integer;
    }

    /**
     * @param string
     */
    public void setStatusStr(String string) {
        statusStr = string;
    }

    /**
     * @return
     */
    public CreditCardDTO getCreditCard() {
        return creditCard;
    }

    /**
     * @param cardDTO
     */
    public void setCreditCard(CreditCardDTO cardDTO) {
        creditCard = cardDTO;
    }

    /**
     * @return
     */
    public Integer getLastInvoiceId() {
        return lastInvoiceId;
    }

    /**
     * @param lastInvoiceId
     */
    public void setLastInvoiceId(Integer lastInvoiceId) {
        this.lastInvoiceId = lastInvoiceId;
    }

    /**
     * @return
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    /**
     * @param currencySymbol
     */
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    /**
     * @return
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * @param currencyName
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    /**
     * @return
     */
    public CustomerDTOEx getCustomerDto() {
        return customerDto;
    }

    /**
     * @param customerDto
     */
    public void setCustomerDto(CustomerDTOEx customerDto) {
        this.customerDto = customerDto;
    }

    /**
     * @return
     */
    public PartnerDTOEx getPartnerDto() {
        return partnerDto;
    }

    /**
     * @param partnerDto
     */
    public void setPartnerDto(PartnerDTOEx partnerDto) {
        this.partnerDto = partnerDto;
    }

	/**
	 * @return Returns the ach.
	 */
	public AchDTO getAch() {
		return ach;
	}
	/**
	 * @param ach The ach to set.
	 */
	public void setAch(AchDTO ach) {
		this.ach = ach;
	}
    public Locale getLocale() {
        return locale;
    }
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Integer getSubscriptionStatusId() {
        return subscriptionStatusId;
    }

    public void setSubscriptionStatusId(Integer subscriptionStatusId) {
        this.subscriptionStatusId = subscriptionStatusId;
    }

    public String getSubscriptionStatusStr() {
        return subscriptionStatusStr;
    }

    public void setSubscriptionStatusStr(String subscriptionStatusStr) {
        this.subscriptionStatusStr = subscriptionStatusStr;
    }
}
