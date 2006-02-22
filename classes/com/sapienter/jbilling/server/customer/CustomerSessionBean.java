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

package com.sapienter.jbilling.server.customer;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.ContactDTOEx;

/**
 * @ejb:bean name="CustomerSession"
 *           display-name="The customer session facade"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type="remote"
 *           jndi-name="com/sapienter/jbilling/server/item/CustomerSession"
 * 
 * @ejb.resource-ref res-ref-name="jdbc/ApplicationDS"
 *                   res-type="javax.sql.DataSource"
 * 					 res-auth="Container"
 * 
 * @jboss.resource-ref res-ref-name="jdbc/ApplicationDS"
 *                     jndi-name="java:/ApplicationDS"
 */
public class CustomerSessionBean implements SessionBean {
    // -------------------------------------------------------------------------
    // Static
    // -------------------------------------------------------------------------
    static Logger log = null;
    /**
    *
    * @ejb:create-method view-type="remote"
    */
    public void ejbCreate() throws CreateException {
        log = Logger.getLogger(CustomerSessionBean.class);

    }


    /**
    * @ejb:interface-method view-type="remote"
    */
	public ContactDTOEx getPrimaryContactDTO(Integer userId)
			throws SessionInternalError {
	    try {
            ContactBL bl = new ContactBL();
            bl.set(userId);
	    	return bl.getDTO();
	    } catch (Exception e) {
            log.error("Exception retreiving the customer contact", e);
            throw new SessionInternalError("Customer primary contact");
	    }
	}
	
	// EJB Callbacks

    /** 
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
        log = Logger.getLogger(CustomerSessionBean.class);

    }

}
