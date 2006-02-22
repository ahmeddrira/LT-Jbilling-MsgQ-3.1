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

package com.sapienter.jbilling.server.report;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.SessionInternalError;

/**
 *
 * This is the session facade for the reports in general. It is a statless
 * bean that provides services not directly linked to a particular operation
 *
 * @author emilc
 * @ejb:bean name="ReportSession"
 *           display-name="A stateless bean for invoices"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type="remote"
 *           jndi-name="com/sapienter/jbilling/server/invoice/ReportSession"
 * 
 **/
public class ReportSessionBean implements SessionBean {

    Logger log = null;

    /**
    * Create the Session Bean
    * @throws CreateException
    * @ejb:create-method view-type="remote"
    */
    public void ejbCreate() throws CreateException {
    }

    /**
     * This retrives the report for the given type (report id)
     * @ejb:interface-method view-type="remote"
     */
    public ReportDTOEx getReportDTO(Integer type, Integer entityId) 
            throws SessionInternalError {
        
        try {
            ReportBL bl = new ReportBL();
            return bl.getReport(type, entityId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }        
    }
    
    /**
     * This retrives a user saved report
     * @ejb:interface-method view-type="remote"
     */
    public ReportDTOEx getReportDTO(Integer userReportId) 
            throws SessionInternalError {
    
        try {
            ReportBL bl = new ReportBL();
            return bl.getReport(userReportId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }        
    }    
    
    /**
     * @ejb:interface-method view-type="remote"
     */
    public CachedRowSet execute(ReportDTOEx report) 
            throws SessionInternalError {
        try {
            ReportBL logic = new ReportBL();
            return logic.execute(report);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public Collection getList(Integer entityId) 
            throws SessionInternalError {
        try {
            ReportBL logic = new ReportBL();
            return logic.getList(entityId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
    }

    /**
     * Returns a vector of ReportDTOEx that belong
     * to the given report type
     * @ejb:interface-method view-type="remote"
     */
    public Collection getListByType(Integer typeId) 
            throws SessionInternalError {
        try {
            ReportBL logic = new ReportBL();
            return logic.getListByType(typeId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void save(ReportDTOEx report, Integer user, String title) 
            throws SessionInternalError {
        try {
            ReportBL logic = new ReportBL();
            logic.save(report, user, title);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public Collection getUserList(Integer report, Integer userId) 
            throws SessionInternalError {
        try {
            ReportBL logic = new ReportBL();
            return logic.getUserList(report, userId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void delete(Integer userReportId) 
            throws SessionInternalError {
        try {
            ReportBL logic = new ReportBL();
            logic.delete(userReportId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
    }
           

    // EJB Callbacks -------------------------------------------------

    /* (non-Javadoc)
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.SessionBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext arg0)
            throws EJBException, RemoteException {
        log = Logger.getLogger(ReportSessionBean.class);
    }

}
