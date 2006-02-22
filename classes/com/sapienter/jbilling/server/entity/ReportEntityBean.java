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
 * Created on Jun 13, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

/**
 * @ejb:bean name="ReportEntity" 
 *          display-name="Object representation of the table REPORT" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ReportEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ReportEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="report"
 *
 * @ejb.value-object name="Report"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "report"
 * @jboss.read-only read-only="true"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ReportEntityBean implements EntityBean {

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
     * @jboss:column-name name="titleKey"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getTitleKey();
    public abstract void setTitleKey(String title);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="instructionsKey"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getInstructionsKey();
    public abstract void setInstructionsKey(String instr);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="tables"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getTables();
    public abstract void setTables(String tables);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="where_str"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getWhere();
    public abstract void setWhere(String where);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="id_column"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getIdColumn();
    public abstract void setIdColumn(Integer id);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="link"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getLink();
    public abstract void setLink(String link);

    //  CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="report-report_fields"
     *               role-name="report-has-report_fields"
     *               target-ejb="ReportFieldEntity"
     *               target-role-name="fields-belongs-to-report"
     * @jboss.target-relation related-pk-field="id"  
     *                 fk-column="report_id"            
     */
    public abstract Collection getFields();
    public abstract void setFields(Collection fields);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="report-report_user"
     *               role-name="report-supports-report_users"
     */
    public abstract Collection getReportUsers();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setReportUsers(Collection rUsers);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="report-entity"
     *               role-name="report-belongs_to-entity"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="entity_id"            
     * @jboss.relation-table table-name="report_entity_map"
     *                       create-table="false"
     */
    public abstract Collection getEntity();
    public abstract void setEntity(Collection entity);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="report-type"
     *               role-name="report-belongs_to-type"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="type_id"            
     * @jboss.relation-table table-name="report_type_map"
     *                       create-table="false"
     */
    public abstract Collection getTypes();
    public abstract void setTypes(Collection types);

    //  EJB callbacks -----------------------------------------------------
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
