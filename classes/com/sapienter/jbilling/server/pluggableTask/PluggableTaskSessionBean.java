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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskBL;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDAS;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskParameterDTO;
import com.sapienter.jbilling.server.util.db.DBUtil;

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
 * the security code uses entity beans and the collections of those have to be
 * in a transaction. When all is migrated to JPA, try using "Never" 
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
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            if (!tx.isActive()) tx.begin();
            
            PluggableTaskDAS das = new PluggableTaskDAS();
            Collection tasks = das.findAllByEntity(entityId);
            PluggableTaskDTO[] retValue = 
                new PluggableTaskDTO[tasks.size()];
            retValue = (PluggableTaskDTO[]) tasks.toArray(retValue);
            
            if (tx.isActive()) tx.commit();

            return retValue;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception getting all dtos", PluggableTaskSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void create(Integer executorId, PluggableTaskDTO dto) {
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            tx.begin();
            
            PluggableTaskBL bl = new PluggableTaskBL();
            bl.create(dto);
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception creating tasks", PluggableTaskSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void createParameter(Integer executorId, Integer taskId, PluggableTaskParameterDTO dto) {
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            tx.begin();
            
            PluggableTaskBL bl = new PluggableTaskBL();
            bl.createParameter(taskId, dto);
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception creating parameter", PluggableTaskSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void update(Integer executorId, PluggableTaskDTO dto) {
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            tx.begin();
            
            PluggableTaskBL bl = new PluggableTaskBL();
            bl.update(executorId, dto);
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception updating task", PluggableTaskSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
    }
    
    /**
     * @ejb:interface-method view-type="remote"
     */
    public PluggableTaskDTO[] updateAll(Integer executorId, PluggableTaskDTO dto[]) {
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            tx.begin();

            PluggableTaskBL bl = new PluggableTaskBL();
            for (int f = 0; f < dto.length; f++) {
                bl.update(executorId, dto[f]);
                dto[f] = bl.getDTO(); // replace with the new version
            }
            //PluggableTaskDTO[] retValue = getAllDTOs(dto[0].getEntityId());
            
            tx.commit();
            
            return dto;
        } catch (OptimisticLockException e1) {
            throw (e1);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception updating all task", PluggableTaskSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void delete(Integer executorId, Integer id) 
            throws FinderException {
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            tx.begin();

            PluggableTaskBL bl = new PluggableTaskBL(id);
            bl.delete(executorId);
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception deleting task", PluggableTaskSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void deleteParameter(Integer executorId, Integer id) {
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            tx.begin();

            PluggableTaskBL bl = new PluggableTaskBL();
            bl.deleteParameter(executorId, id);
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception deleting parameter", PluggableTaskSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void updateParameters(Integer executorId, PluggableTaskDTO dto) 
            throws SessionInternalError {
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            tx.begin();

            PluggableTaskBL bl = new PluggableTaskBL();           
            bl.updateParameters(dto);
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception updating parameters", PluggableTaskSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
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
