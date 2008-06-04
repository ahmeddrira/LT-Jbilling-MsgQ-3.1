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

package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="BillingProcessConfigurationEntity" 
 *          display-name="Object representation of the table BILLING_PROCESS_CONFIGURATION" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/BillingProcessConfigurationEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/BillingProcessConfigurationEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="billing_process_configuration"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * 
 * @ejb:finder signature="BillingProcessConfigurationEntityLocal findByEntity(java.lang.Integer entityId)"
 *             query="SELECT OBJECT(b) 
 *                      FROM billing_process_configuration b 
 *                     WHERE b.entityId = ?1"
 *             result-type-mapping="Local"
 * 
 * @ejb.value-object name="BillingProcessConfiguration"
 * 
 * @jboss:table-name "billing_process_configuration"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class BillingProcessConfigurationEntityBean implements EntityBean {
    private JNDILookup EJBFactory = null;
    private Logger log = null;
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer entityId, Date runDate, Integer generateFlag) 
            throws CreateException {

        Integer newId;

        try {
        	EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer(generator.getNextSequenceNumber(
                    Constants.TABLE_BILLING_PROCESS_CONFIGURATION));

        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the billing_process_configuration table");
        }

        setId(newId);
        setNextRunDate(runDate);
        setGenerateReport(generateFlag);
        setEntityId(entityId);
        
        return newId;
    }
    public void ejbPostCreate(Integer entityId, Date runDate, Integer generateFlag) {}


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
     * @jboss:column-name name="next_run_date"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getNextRunDate();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setNextRunDate(Date date);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="generate_report"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getGenerateReport();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setGenerateReport(Integer flag);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="retries"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getRetries();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setRetries(Integer retires);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="days_for_retry"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDaysForRetry();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDaysForRetry(Integer days);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="days_for_report"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDaysForReport();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDaysForReport(Integer days);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="entity_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getEntityId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setEntityId(Integer entityId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="review_status"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getReviewStatus();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setReviewStatus(Integer status);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="period_unit_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getPeriodUnitId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPeriodUnitId(Integer period);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="period_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getPeriodValue();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPeriodValue(Integer periodValue);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="due_date_unit_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDueDateUnitId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDueDateUnitId(Integer unit);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="due_date_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDueDateValue();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDueDateValue(Integer value);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="df_fm"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDfFm();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDfFm(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="only_recurring"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getOnlyRecurring();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOnlyRecurring(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="invoice_date_process"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getInvoiceDateProcess();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setInvoiceDateProcess(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="auto_payment"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getAutoPayment();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAutoPayment(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="auto_payment_application"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getAutoPaymentApplication();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAutoPaymentApplication(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="maximum_periods"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getMaximumPeriods();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setMaximumPeriods(Integer periods);
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
        try { 
            log = Logger.getLogger(this.getClass());
        } catch (Exception e) {}
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
