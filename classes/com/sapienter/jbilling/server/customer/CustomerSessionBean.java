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
