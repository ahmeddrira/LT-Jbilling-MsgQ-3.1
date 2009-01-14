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

package com.sapienter.jbilling.server.provisioning;


import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.apache.log4j.Logger;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDAS;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @author othman
 * 
 *         This is the session facade for the provisioning process and its
 *         related services.
 * 
 * @ejb:bean name="ProvisioningProcessSession"
 *           display-name="The provisioning process session facade"
 *           type="Stateless" transaction-type="Container" view-type="both"
 *           jndi-name=
 *           "com/sapienter/jbilling/server/provisioning/ProvisioningProcessSession"
 * 
 */
public class ProvisioningProcessSessionBean implements SessionBean {
	private static final Logger LOG = Logger.getLogger(ProvisioningProcessSessionBean.class);
	SessionContext ctx = null;

	/**
	 * @ejb:create-method view-type="remote"
	 */
	public void ejbCreate() throws CreateException {
	}

	/**
	 * @ejb:interface-method view-type="remote"
	 * @ejb.transaction type="Required"
	 */
	public void trigger() throws SessionInternalError {
		LOG.debug("calling ProvisioningProcessSessionBean trigger() method");

		try {
			ProvisioningProcessBL processBL = new ProvisioningProcessBL();

			processBL.activateOrders();
			processBL.deActivateOrders();
		} catch (Exception e) {
			throw new SessionInternalError(e);
		}
	}

	/**
	 * 
	 * @ejb:interface-method view-type="remote"
	 * @ejb.transaction type="Required"
	 */
	public void updateProvisioningStatus(Integer in_order_id,
			Integer in_order_line_id, String result) {
		OrderDAS orderDb = new OrderDAS();
		OrderDTO order = orderDb.find(in_order_id);
		OrderBL order_bl = new OrderBL(order);
		OrderLineDAS lineDb = new OrderLineDAS();
		OrderLineDTO order_line = lineDb.findNow(in_order_line_id);

		if (result.equals("fail")) {
			order_bl.setProvisioningStatus(in_order_line_id,
					Constants.PROVISIONING_STATUS_FAILED);
			LOG.debug("Provisioning status set to 'FAILED' for order line : "
					+ order_line);
		} else if (result.equals("success")) {
			if (order_line.getProvisioningStatus().equals(
					Constants.PROVISIONING_STATUS_PENDING_ACTIVE)) {
				order_bl.setProvisioningStatus(in_order_line_id,
						Constants.PROVISIONING_STATUS_ACTIVE);

			} else if (order_line.getProvisioningStatus().equals(
					Constants.PROVISIONING_STATUS_PENDING_INACTIVE)) {
				order_bl.setProvisioningStatus(in_order_line_id,
						Constants.PROVISIONING_STATUS_INACTIVE);

			}
		} else {
			LOG.error("Can not process message with result property value  "
					+ result);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	@Override
	public void ejbActivate() throws EJBException, RemoteException {

		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	@Override
	public void ejbPassivate() throws EJBException, RemoteException {

		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	@Override
	public void ejbRemove() throws EJBException, RemoteException {

		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	@Override
	public void setSessionContext(SessionContext newCtx) throws EJBException,
			RemoteException {
		ctx = newCtx;
	}
}
