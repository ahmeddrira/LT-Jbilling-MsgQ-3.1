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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;

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
     * @ejb.transaction type="Required"
     */
    public void updateParameters(PluggableTaskDTOEx dto) 
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
