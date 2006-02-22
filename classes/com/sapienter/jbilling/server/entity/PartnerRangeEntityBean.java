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
