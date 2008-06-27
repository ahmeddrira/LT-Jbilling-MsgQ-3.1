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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;
import com.sapienter.jbilling.interfaces.InvoiceLineTypeEntityLocal;
import com.sapienter.jbilling.interfaces.InvoiceLineTypeEntityLocalHome;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="InvoiceLineEntity" 
 *          display-name="Object representation of the table INVOICE_LINE"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/InvoiceLineEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/InvoiceLineEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="invoice_line"
 *
 * @ejb.value-object name="InvoiceLine"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "invoice_line"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class InvoiceLineEntityBean implements EntityBean {
    private Logger log = null;
    private JNDILookup EJBFactory = null;
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(String description, Float amount, Double quantity, 
            Float price, Integer typeId, Integer itemId, Integer sourceUserId,
            Integer isPercentage)
            throws CreateException {

        Integer newId;

        try {
            EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId =
                new Integer(
                    generator.getNextSequenceNumber(Constants.TABLE_INVOICE_LINE));

        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the purchase order table");
        }

        setId(newId);
        if (description.length() > 1000) {
            description = description.substring(0, 1000);
            log.warn("Truncated a invoice line description to " + description);
        }
        setDescription(description);
        setAmount(amount);
        setDeleted(new Integer(0));
        setQuantity(quantity);
        setPrice(price);
        setItemId(itemId);
        setSourceUserId(sourceUserId);
        setIsPercentage(isPercentage);
        
        return newId;
    }

    public void ejbPostCreate(String description, Float amount, Double quantity, 
            Float price, Integer typeId, Integer itemId, Integer sourceUserId,
            Integer isPercentage) {
        try {
            InvoiceLineTypeEntityLocalHome typeHome =
                    (InvoiceLineTypeEntityLocalHome) EJBFactory.lookUpLocalHome(
                    InvoiceLineTypeEntityLocalHome.class,
                    InvoiceLineTypeEntityLocalHome.JNDI_NAME);
            setType(typeHome.findByPrimaryKey(typeId));
        } catch (Exception e) {
            
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
     * @jboss:column-name name="description"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getDescription();
    public abstract void setDescription(String desc);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="amount"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getAmount();
    public abstract void setAmount(Float amount);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="price"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getPrice();
    public abstract void setPrice(Float price);


    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="quantity"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Double getQuantity();
    public abstract void setQuantity(Double quantity);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="deleted"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDeleted();
    public abstract void setDeleted(Integer deleted);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="item_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getItemId();
    public abstract void setItemId(Integer item);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="source_user_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getSourceUserId();
    public abstract void setSourceUserId(Integer userId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="is_percentage"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getIsPercentage();
    public abstract void setIsPercentage(Integer flag);

    // CMR fields -----------------------------------------------------------  
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="invoice-invoice_lines"
     *               role-name="line-belongs_to-invoice"
     *               cascade-delete="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="invoice_id"            
     */
    public abstract InvoiceEntityLocal getInvoice();
    public abstract void setInvoice(InvoiceEntityLocal invoice);

    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="invoice_lines-type"
     *               role-name="line-has-type"
     *               target-ejb="InvoiceLineTypeEntity"
     *               target-role-name="type"
     *               target-multiple="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="type_id"            
     */
    public abstract InvoiceLineTypeEntityLocal getType();
    public abstract void setType(InvoiceLineTypeEntityLocal type);

    // EJB callbacks -----------------------------------------------------------
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
        log = Logger.getLogger(InvoiceLineEntityBean.class);
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
