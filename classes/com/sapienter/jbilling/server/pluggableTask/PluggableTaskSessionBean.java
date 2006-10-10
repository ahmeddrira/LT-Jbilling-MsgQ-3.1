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

package com.sapienter.jbilling.server.pluggableTask;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.PluggableTaskEntityLocal;

/**
 *
 * This is the session facade for the invoices in general. It is a statless
 * bean that provides services not directly linked to a particular operation
 *
 * @author emilc
 * @ejb:bean name="PluggableTaskSession"
 *           display-name="A stateless bean for pluggableTasks"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type="remote"
 *           jndi-name="com/sapienter/jbilling/server/pluggableTask/PluggableTaskSession"
 * 
 * @jboss.security-proxy name="com.sapienter.jbilling.server.pluggableTask.TaskMethodSecurity"
 **/
public class PluggableTaskSessionBean implements SessionBean {

    Logger log = null;

    /**
    * Create the Session Bean
    * @throws CreateException
    * @ejb:create-method view-type="remote"
    */
    public void ejbCreate() throws CreateException {
        log = Logger.getLogger(PluggableTaskSessionBean.class);
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public PluggableTaskDTOEx getDTO(Integer typeId, 
            Integer entityId) throws SessionInternalError {
        try {
            PluggableTaskBL bl = new PluggableTaskBL();
            try {
                bl.set(entityId, typeId);
                return bl.getDTO();
            } catch (FinderException e1) {
                return null;
            }
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public PluggableTaskDTOEx[] getAllDTOs(Integer entityId) 
            throws SessionInternalError {
        try {
            PluggableTaskBL bl = new PluggableTaskBL();
            Collection tasks = bl.getHome().findAllByEntity(entityId);
            PluggableTaskDTOEx[] retValue = 
                new PluggableTaskDTOEx[tasks.size()];
            int f = 0;
            for (Iterator it = tasks.iterator(); it.hasNext(); f++) {
                bl.set((PluggableTaskEntityLocal) it.next());
                retValue[f] = bl.getDTO();
            }
             return retValue;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void create(Integer executorId, PluggableTaskDTOEx dto) {
        PluggableTaskBL bl = new PluggableTaskBL();
        bl.create(dto);
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void createParameter(Integer executorId, Integer taskId, PluggableTaskParameterDTOEx dto) {
        PluggableTaskBL bl = new PluggableTaskBL();
        bl.createParameter(taskId, dto);
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void update(Integer executorId, PluggableTaskDTOEx dto) {
        PluggableTaskBL bl = new PluggableTaskBL();
        bl.update(executorId, dto);
    }
    
    /**
     * @ejb:interface-method view-type="remote"
     */
    public void updateAll(Integer executorId, PluggableTaskDTOEx dto[]) {
        PluggableTaskBL bl = new PluggableTaskBL();
        for (int f = 0; f < dto.length; f++) {
            bl.update(executorId, dto[f]);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void delete(Integer executorId, Integer id) 
            throws FinderException {
        PluggableTaskBL bl = new PluggableTaskBL(id);
        bl.delete(executorId);
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void deleteParameter(Integer executorId, Integer id) {
        PluggableTaskBL bl = new PluggableTaskBL();
        bl.deleteParameter(executorId, id);
    }

    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public void updateParameters(Integer executorId, PluggableTaskDTOEx dto) 
            throws SessionInternalError {
        try {
            PluggableTaskBL bl = new PluggableTaskBL();
            
            bl.updateParameters(dto);
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
    }

}
