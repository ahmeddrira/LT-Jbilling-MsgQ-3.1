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
 * Created on May 11, 2005
 *
 */
package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.PartnerEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="PartnerRangeEntity" 
 *          display-name="Object representation of the table PARTNER_RANGE" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PartnerRangeEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PartnerRangeEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="partner_range"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * @ejb.value-object name="PartnerRange"
 * 
 * @jboss:table-name "partner_range"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class PartnerRangeEntityBean implements EntityBean {
    
    private Logger log = null;
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer from, Integer to) 
            throws CreateException {
        Integer newId;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false); 
            
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer(generator.getNextSequenceNumber(
                    Constants.TABLE_PARTNER_RANGE));

        } catch (Exception e) {
            log.error("Exception creating partner", e);
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the item_price table");
        }

        setId(newId);
        setRangeFrom(from);
        setRangeTo(to);

        return newId;
    }

    public void ejbPostCreate(Integer from, Integer to) {}

    // CMP field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getId();
    public abstract void setId(Integer id);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="percentage_rate"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getPercentageRate();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPercentageRate(Float rate);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="referral_fee"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getReferralFee();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setReferralFee(Float fee);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="range_to"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getRangeTo();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setRangeTo(Integer range);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="range_from"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getRangeFrom();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setRangeFrom(Integer range);

    // CMR ----------------
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="partner-ranges"
     *               role-name="range-belongs_to-partner"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="partner_id"            
     */
    public abstract PartnerEntityLocal getPartner();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPartner(PartnerEntityLocal partner);
    
    
    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbLoad()
     */
    public void ejbLoad() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbRemove()
     */
    public void ejbRemove() throws RemoveException, EJBException,
            RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbStore()
     */
    public void ejbStore() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
     */
    public void setEntityContext(EntityContext arg0) throws EJBException,
            RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

}
