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
 * Created on 6-Apr-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ContactMapEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="ContactEntity" 
 *          display-name="Object representation of the table Contact" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ContactEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ContactEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="contact"
 *
 * @ejb:finder signature="ContactEntityLocal findPrimaryContact(java.lang.Integer userId) "
 * 			   query="SELECT OBJECT(c)
 *                      FROM base_user a, contact c
 *                     WHERE a.userId = c.userId
 *                       AND a.userId = ?1"
 * 
 * @ejb:finder signature="ContactEntityLocal findContact(java.lang.Integer userId, java.lang.Integer typeId) "
 *             query="SELECT OBJECT(c)
 *                      FROM base_user a, contact c, jbilling_table d
 *                     WHERE a.userId = c.contactMap.foreignId
 *                       AND c.contactMap.tableId = d.id
 *                       AND d.name = 'base_user'
 *                       AND c.contactMap.type.id = ?2
 *                       AND a.userId = ?1"
 *
 *
 * @ejb:finder signature="ContactEntityLocal findEntityContact(java.lang.Integer entityId) "
 * 			   query="SELECT OBJECT(c)
 *                      FROM contact c, jbilling_table d
 *                     WHERE c.contactMap.foreignId = ?1
 *                       AND c.contactMap.tableId = d.id
 *                       AND d.name = 'entity'"
 *
 * @ejb:finder signature="ContactEntityLocal findInvoiceContact(java.lang.Integer invoiceId) "
 *             query="SELECT OBJECT(c)
 *                      FROM contact c, jbilling_table d
 *                     WHERE c.contactMap.foreignId = ?1
 *                       AND c.contactMap.tableId = d.id
 *                       AND d.name = 'invoice'"
 *
 * @ejb.value-object name = "Contact"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "contact"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ContactEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(ContactMapEntityLocal map) 
            throws CreateException {
        Integer newId;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer( generator.getNextSequenceNumber(
                    Constants.TABLE_CONTACT));

        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the contact table");
        }
        setId(newId);
        setCreateDate(Calendar.getInstance().getTime());
        setDeleted(new Integer(0));

        return newId;
    }

    public void ejbPostCreate(ContactMapEntityLocal map) {
        setContactMap(map);
    }
 
    // CMP field accessors ----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getId();
    public abstract void setId(Integer itemId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="ORGANIZATION_NAME"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getOrganizationName();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setOrganizationName(String org);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="STREET_ADDRES1"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getAddress1();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setAddress1(String addr);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="STREET_ADDRES2"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getAddress2();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setAddress2(String addr2);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="CITY"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getCity();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setCity(String city);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="STATE_PROVINCE"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getStateProvince();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setStateProvince(String stPr);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="POSTAL_CODE"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getPostalCode();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setPostalCode(String pco);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="COUNTRY_CODE"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getCountryCode();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setCountryCode(String cc);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="LAST_NAME"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getLastName();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setLastName(String ln);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="FIRST_NAME"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getFirstName();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setFirstName(String fn);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="PERSON_INITIAL"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getInitial();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setInitial(String in);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="PERSON_TITLE"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getTitle();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setTitle(String ti);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="PHONE_COUNTRY_CODE"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getPhoneCountryCode();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setPhoneCountryCode(Integer pcc);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="PHONE_AREA_CODE"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getPhoneAreaCode();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setPhoneAreaCode(Integer pac);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="PHONE_PHONE_NUMBER"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getPhoneNumber();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setPhoneNumber(String pn);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="FAX_COUNTRY_CODE"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getFaxCountryCode();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setFaxCountryCode(Integer pcc);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="FAX_AREA_CODE"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getFaxAreaCode();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setFaxAreaCode(Integer pac);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="FAX_PHONE_NUMBER"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getFaxNumber();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setFaxNumber(String pn);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="EMAIL"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getEmail();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setEmail(String em);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="CREATE_DATETIME"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getCreateDate();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setCreateDate(Date cdt);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="deleted"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDeleted();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setDeleted(Integer deleted);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="notification_include"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getInclude();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setInclude(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="user_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getUserId();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setUserId(Integer userId);

    //  CMR fields  ----------------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="contact-contact_map"
     *               role-name="contact-has-map"
     */
    public abstract ContactMapEntityLocal getContactMap();
    public abstract void setContactMap(ContactMapEntityLocal map);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="contact-contact_field"
     *               role-name="contact-has-per_entity_fields"
     */
    public abstract Collection getFields();
    public abstract void setFields(Collection fields);

	// EJB Callbacks
	
    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbLoad()
     */
    public void ejbLoad() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbRemove()
     */
    public void ejbRemove()
        throws RemoveException, EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbStore()
     */
    public void ejbStore() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
     */
    public void setEntityContext(EntityContext arg0)
        throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
