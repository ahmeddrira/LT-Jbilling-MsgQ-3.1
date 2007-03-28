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

package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.PaymentEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="PaymentAuthorizationEntity" 
 *          display-name="Object representation of the table PAYMENT_AUTHORIZATION" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PaymentAuthorizationEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PaymentAuthorizationEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="payment_authorization"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * 
 * @ejb.value-object name="PaymentAuthorization"
 * 
 * @jboss:table-name "payment_authorization"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class PaymentAuthorizationEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(String processor, String code) 
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
                    Constants.TABLE_PAYMENT_AUTHORIZATION));

        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the payment_authorization table");
        }

        setId(newId);
        setProcessor(processor);
        setCode1(code);
        setCreateDate(Calendar.getInstance().getTime());
        
        return newId;
    }
    public void ejbPostCreate(String processor, String code) {}


    //  CMP field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getId();
    public abstract void setId(Integer ruleId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="processor"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getProcessor();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setProcessor(String processor);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="code1"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getCode1();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCode1(String code);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="code2"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getCode2();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCode2(String code);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="code3"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getCode3();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCode3(String code);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="approval_code"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getApprovalCode();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setApprovalCode(String code);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="avs"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getAVS();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAVS(String avs);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="transaction_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getTransactionId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setTransactionId(String id);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="md5"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getMD5();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setMD5(String md5);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="card_code"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getCardCode();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCardCode(String code);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="create_datetime"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getCreateDate();
    public abstract void setCreateDate(Date date);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="response_message"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getResponseMessage();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setResponseMessage(String message);
    // CMR fields ------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="payment-authorization"
     *               role-name="authorization-belongs_to-payment"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="payment_id"            
     */
    public abstract PaymentEntityLocal getPayment();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPayment(PaymentEntityLocal payment);

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
