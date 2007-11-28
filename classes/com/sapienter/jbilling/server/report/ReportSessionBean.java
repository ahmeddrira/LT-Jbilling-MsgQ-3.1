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
