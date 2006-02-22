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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="AchEntity" 
 *          display-name="Object representation of the table ACH" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/AchEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/AchEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="ach"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * 
 * @ejb.value-object name="Ach"
 * 
 * @jboss:table-name "ach"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class AchEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(String aba, String account, Integer type, 
    		String bankName, String accountName) 
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
                    Constants.TABLE_ACH));

        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the ach table");
        }

        setId(newId);
        setAbaRouting(aba);
        setAccountName(accountName);
        setAccountType(type);
        setBankAccount(account);
        setBankName(bankName);
        
        return newId;
    }
    public void ejbPostCreate(String aba, String account, Integer type, 
    		String bankName, String accountName) {}


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
     * @jboss:column-name name="aba_routing"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getAbaRouting();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAbaRouting(String routing);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="bank_account"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getBankAccount();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setBankAccount(String number);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="account_type"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getAccountType();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAccountType(Integer type);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="bank_name"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getBankName();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setBankName(String name);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="account_name"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getAccountName();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAccountName(String name);


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
