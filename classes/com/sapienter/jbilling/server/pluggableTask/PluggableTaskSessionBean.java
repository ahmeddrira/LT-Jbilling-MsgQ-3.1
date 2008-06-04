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

package com.sapienter.jbilling.server.pluggableTask;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskBL;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDAS;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskParameterDTO;

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
 * Even when using JPA, container transactions are required. This is because
 * transactional demarcation is taked from the application server.
 *  
 * @ejb.transaction type="Required"
 * @jboss.security-proxy name="com.sapienter.jbilling.server.pluggableTask.TaskMethodSecurity"
 **/
public class PluggableTaskSessionBean implements SessionBean {

    //private static final Logger LOG = Logger.getLogger(PluggableTaskSessionBean.class);

    /**
    * Create the Session Bean
    * @throws CreateException
    * @ejb:create-method view-type="remote"
    */
    public void ejbCreate() throws CreateException {
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public PluggableTaskDTO getDTO(Integer typeId, 
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
    public PluggableTaskDTO[] getAllDTOs(Integer entityId) 
            throws SessionInternalError {
            
        PluggableTaskDAS das = new PluggableTaskDAS();
        Collection tasks = das.findAllByEntity(entityId);
        PluggableTaskDTO[] retValue = 
            new PluggableTaskDTO[tasks.size()];
        retValue = (PluggableTaskDTO[]) tasks.toArray(retValue);
        
        return retValue;
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void create(Integer executorId, PluggableTaskDTO dto) {
            
        PluggableTaskBL bl = new PluggableTaskBL();
        bl.create(dto);
        
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void createParameter(Integer executorId, Integer taskId, PluggableTaskParameterDTO dto) {
            
        PluggableTaskBL bl = new PluggableTaskBL();
        bl.createParameter(taskId, dto);
    }

    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="RequiresNew"
     */
    public void update(Integer executorId, PluggableTaskDTO dto) {
            
        PluggableTaskBL bl = new PluggableTaskBL();
        bl.update(executorId, dto);
        
    }
    
    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="RequiresNew"
     */
    public PluggableTaskDTO[] updateAll(Integer executorId, PluggableTaskDTO dto[]) {

        PluggableTaskBL bl = new PluggableTaskBL();
        for (int f = 0; f < dto.length; f++) {
            bl.update(executorId, dto[f]);
            dto[f] = bl.getDTO(); // replace with the new version
        }
        
        return dto;
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
     * @ejb.transaction type="RequiresNew"
     */
    public void updateParameters(Integer executorId, PluggableTaskDTO dto) 
            throws SessionInternalError {

        PluggableTaskBL bl = new PluggableTaskBL();           
        try {
            bl.updateParameters(dto);
        } catch (FinderException e) {
            throw new SessionInternalError("Not found?", PluggableTaskSessionBean.class, e);
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
