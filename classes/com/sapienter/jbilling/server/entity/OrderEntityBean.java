/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.OrderPeriodEntityLocal;
import com.sapienter.jbilling.interfaces.OrderPeriodEntityLocalHome;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="OrderEntity" 
 *          display-name="Object representation of the table PURCHASE_ORDER" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/OrderEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/OrderEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="purchase_order"
 *
 * @ejb.value-object name="Order"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @ejb:finder signature="Collection findByUser_Status(java.lang.Integer userId, java.lang.Integer statusId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM purchase_order a 
 *                     WHERE a.user.userId = ?1
 *                       AND a.statusId = ?2
 *                       AND a.deleted = 0 "
 *             result-type-mapping="Local"
 *             
 * @ejb:finder signature="Collection findByUserSubscriptions(java.lang.Integer userId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM purchase_order a 
 *                     WHERE a.user.userId = ?1
 *                       AND a.statusId = 1
 *                       AND a.period.id <> 1
 *                       AND a.deleted = 0 "
 *             result-type-mapping="Local"
 *
 *
 * @jboss:table-name "purchase_order"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */

public abstract class OrderEntityBean implements javax.ejb.EntityBean {
    
    private static final Logger LOG = Logger.getLogger(OrderEntityBean.class);
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer entityId, Integer periodId, 
            Integer createBy, Integer userId, Integer billingTypeId,
            Integer currencyId) throws CreateException {
        Integer newId;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId =
                new Integer(
                    generator.getNextSequenceNumber(
                        Constants.TABLE_PUCHASE_ORDER));
        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the purchase order table");
        }
        setId(newId);
        setCreateDate(Calendar.getInstance().getTime());
        setCreatedBy(createBy);
        setDeleted(new Integer(0));
        setBillingTypeId(billingTypeId);
        setCurrencyId(currencyId);
        // by default the order is created to be process by the billing cycle
        setStatusId(Constants.ORDER_STATUS_ACTIVE);

        return newId;
    }

    public void ejbPostCreate(Integer entityId, Integer periodId, 
            Integer createBy, Integer userId, Integer billingTypeId,
            Integer currencyId) {

        try {

            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            // set the period FK
            OrderPeriodEntityLocalHome orderLineTypeHome =
                    (OrderPeriodEntityLocalHome) EJBFactory.lookUpLocalHome(
                    OrderPeriodEntityLocalHome.class,
                    OrderPeriodEntityLocalHome.JNDI_NAME);
            OrderPeriodEntityLocal period =
                orderLineTypeHome.findByPrimaryKey(periodId);
            setPeriod(period);

            // set the user FK (who is buying/paying)
            UserBL userBl = new UserBL(userId);
            setUser(userBl.getEntity());
        } catch (Exception e) {
            // can't do much ...
            LOG.debug("Error post creating the order ", e);
        }
    }

    // CMP field accessors -----------------------------------------------------
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
      * @jboss:column-name name="billing_type_id"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Integer getBillingTypeId();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setBillingTypeId(Integer billingId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="notify"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getNotify();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setNotify(Integer flag);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="active_since"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Date getActiveSince();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setActiveSince(Date active);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="active_until"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Date getActiveUntil();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setActiveUntil(Date until);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="create_datetime"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Date getCreateDate();
    public abstract void setCreateDate(Date create);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="next_billable_day"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Date getNextBillableDay();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setNextBillableDay(Date date);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="created_by"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Integer getCreatedBy();
    public abstract void setCreatedBy(Integer createdBy);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="status_id"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Integer getStatusId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setStatusId(Integer status);

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
      * @jboss:column-name name="currency_id"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Integer getCurrencyId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCurrencyId(Integer currencyId);
     
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="last_notified"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getLastNotified();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setLastNotified(Date date);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="notification_step"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getNotificationStep();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setNotificationStep(Integer steps);

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
     * @jboss:column-name name="own_invoice"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getOwnInvoice();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOwnInvoice(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="anticipate_periods"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getAnticipatePeriods();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAnticipatePeriods(Integer periods);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="notes_in_invoice"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getNotesInInvoice();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setNotesInInvoice(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="notes"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getNotes();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setNotes(String notes);

   // CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="order-order_lines"
     *               role-name="order-has-orderlines"
     */
    public abstract Collection getOrderLines();
    public abstract void setOrderLines(Collection lines);

    /**
     * 
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="order-order_period"
     *               role-name="order-belongs-to-period"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="period_id"            
     */
    public abstract OrderPeriodEntityLocal getPeriod();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPeriod(OrderPeriodEntityLocal period);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-orders"
     *               role-name="order-belongs-to-user"
     * @jboss.relation related-pk-field="userId"  
     *                 fk-column="user_id"            
     */
    public abstract UserEntityLocal getUser();
    public abstract void setUser(UserEntityLocal user);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="orders-period"
     *               role-name="orders-generates-invoices"
     */
    public abstract Collection getProcesses();
    public abstract void setProcesses(Collection processes);

    //  EJB callbacks -----------------------------------------------------------

    /**
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbLoad()
     */
    public void ejbLoad() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbRemove()
     */
    public void ejbRemove()
        throws RemoveException, EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbStore()
     */
    public void ejbStore() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
     */
    public void setEntityContext(EntityContext arg0)
        throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
