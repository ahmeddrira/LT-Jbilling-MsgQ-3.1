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
import java.util.Collection;
import java.util.Date;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

 
/**
 * @ejb:bean name="EntityEntity" 
 *          display-name="representation of the table ENTITY" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/EntityEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/EntityEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="entity"
 *
 * @ejb:finder signature="EntityEntityLocal findByExternalId(java.lang.String id)"
 *             query="SELECT OBJECT(a) 
 *                      FROM entity a
 *                     WHERE a.externalId = ?1" 
 *             result-type-mapping="Local"
 *
 * @ejb:finder signature="Collection findEntities()"
 *             query="SELECT OBJECT(a) 
 *                      FROM entity a" 
 *             result-type-mapping="Local"
 * 
 * @ejb.value-object name="Entity"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "entity"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class EntityEntityBean implements EntityBean {

    //  CMP fields -----------------------------------------------------
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
     * @jboss:column-name name="external_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getExternalId();
    public abstract void setExternalId(String id);

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
     * @jboss:column-name name="create_datetime"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getCreateDate();
    public abstract void setCreateDate(Date create);
    
    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="language_id"
     * @jboss.method-attributes read-only="true"
      */
     public abstract Integer getLanguageId();
     public abstract void setLanguageId(Integer languageId);
     
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
     
     // CMR fields ----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-users"
     *               role-name="entity-has-users"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Collection getUsers();
    /**
     * @ejb.interface-method view-type="local"
     * @param users
     */
    public abstract void setUsers(Collection users);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="report-entity"
     *               role-name="entity-has-reports"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="report_id"            
     * @jboss.relation-table table-name="report_entity_map"
     *                       create-table="false"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Collection getReports();
    public abstract void setReports(Collection reports);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-items"
     *               role-name="entity-has-items"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Collection getItems();
    public abstract void setItems(Collection items);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-payment_method"
     *               role-name="entity-has-payment_methods"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="payment_method_id"
     * @jboss.relation-table table-name="entity_payment_method_map"
     *                       create-table="false"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Collection getPaymentMethods();
    public abstract void setPaymentMethods(Collection methods);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-invoice_delivery_method"
     *               role-name="entity-has-invoice_delivery_methods"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="invoice_delivery_method_id"
     * @jboss.relation-table table-name="entity_invoice_delivery_method_map"
     *                       create-table="false"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Collection getInvoiceDeliveryMethods();
    public abstract void setInvoiceDeliveryMethods(Collection methods);

    /**
     * one-to-many unidirectional
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entities-exchanges"
     *               role-name="entity-has-exchanges"
     *               target-ejb="CurrencyExchangeEntity"
     *               target-role-name="exchange-belongs_to-entities"
     * @jboss.target-relation related-pk-field="id"  
     *                 fk-column="entity_id"    
     * @jboss.method-attributes read-only="true"        
     */
    public abstract Collection getExchanges();
    public abstract void setExchanges(Collection exchanges);

    /**
     * many-to-many with a blind side
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entities-currencies"
     *               role-name="entity-has-currencies"
     *               target-ejb="CurrencyEntity"
     *               target-role-name="currencies-has-entities"
     *               target-multiple="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="currency_id" 
     * @jboss.target-relation related-pk-field="id"  
     *                 fk-column="entity_id"            
     * @jboss.relation-table table-name="currency_entity_map"
     *                       create-table="false"
     */
    public abstract Collection getCurrencies();
    public abstract void setCurrencies(Collection currencies);

    /**
     * one-to-many bidirectional
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-ageing_steps"
     *               role-name="entity-has-steps"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Collection getSteps();
    public abstract void setSteps(Collection ageingSteps);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-contact_field_types"
     *               role-name="entity-has-types"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Collection getContactFieldTypes();
    public abstract void setContactFieldTypes(Collection types);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-contact_types"
     *               role-name="entity-has-types"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Collection getContactTypes();
    public abstract void setContactTypes(Collection types);



    // EJB Callbacks -----------------------------------------------------
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
