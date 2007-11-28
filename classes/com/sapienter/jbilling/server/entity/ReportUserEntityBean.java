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
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ReportEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.interfaces.UserEntityLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="ReportUserEntity" 
 *          display-name="Object representation of the table ReportUser" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ReportUserEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ReportUserEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="report_user"
 *
 * @ejb:finder signature="Collection findByTypeUser(java.lang.Integer type,java.lang.Integer userId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM report_user a 
 *                     WHERE a.user.userId = ?2
 *                       AND a.report.id = ?1 "
 *             result-type-mapping="Local"
 *
 * @ejb.value-object name="ReportUser"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "report_user"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ReportUserEntityBean implements EntityBean {
    
    private Logger log = null;
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(String title, ReportEntityLocal report, 
            Integer user) throws CreateException {
        Integer newId;
        try {
            log.debug("creating");
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer( generator.getNextSequenceNumber(
                    Constants.TABLE_REPORT_USER));

            log.debug("new id ready");
        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the report user table");
        }
        setId(newId);
        setTitle(title);
        setCreateDatetime(Calendar.getInstance().getTime());


        return newId;

    }
    
    public void ejbPostCreate(String title, ReportEntityLocal report,
            Integer user) {
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            UserEntityLocalHome userHome =
                    (UserEntityLocalHome) EJBFactory.lookUpLocalHome(
                    UserEntityLocalHome.class,
                    UserEntityLocalHome.JNDI_NAME);
            UserEntityLocal userRow = userHome.findByPrimaryKey(user);
            setUser(userRow);
        } catch (Exception e) {
            log.fatal("exception in post create",e);
        }

        setReport(report);
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
     * @jboss:column-name name="create_datetime"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getCreateDatetime();
    public abstract void setCreateDatetime(Date date);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="title"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getTitle();
    public abstract void setTitle(String title);

    //  CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="report_user-report_fields"
     *               role-name="report_user-has-report_fields"
     *               target-ejb="ReportFieldEntity"
     *               target-role-name="fields-belongs-to-report_user"
     *               target-cascade-delete="yes"
     * @jboss.target-relation related-pk-field="id"  
     *                 fk-column="report_user_id"            
     */
    public abstract Collection getFields();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setFields(Collection fields);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="report-report_user"
     *               role-name="report_users-modify-report"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="report_id"            
     */
    public abstract ReportEntityLocal getReport();
    public abstract void setReport(ReportEntityLocal report);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-reports"
     *               role-name="report-belongs-to-user"
     * @jboss.relation related-pk-field="userId"  
     *                 fk-column="user_id"            
     */
    public abstract UserEntityLocal getUser();
    public abstract void setUser(UserEntityLocal user);

    // EJB Callbacks -----------------------------------------------------
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
        log = Logger.getLogger(ReportUserEntityBean.class);
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
