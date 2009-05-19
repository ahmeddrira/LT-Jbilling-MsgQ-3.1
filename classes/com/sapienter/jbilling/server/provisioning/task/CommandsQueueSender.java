/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.provisioning.task;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.audit.EventLogger;

/**
 * @author othman
 * 
 */
public class CommandsQueueSender {
	private static final Logger LOG = Logger
			.getLogger(CommandsQueueSender.class);
	private EventLogger eLogger = null;
	private String queueName = "provisioning_commands_queue";
	private QueueConnection conn;
	private Integer entityId;
	private MapMessage message;
	private Queue myQueue;
	private OrderDTO order;
	private QueueSession session;

	public CommandsQueueSender(OrderDTO order) throws JMSException,
			NamingException {
		this.order = order;
		this.setEntityId(order.getUser().getCompany().getId());
		setupPTP();
		message = session.createMapMessage();
		eLogger = EventLogger.getInstance();
	}

	private void setupPTP() throws JMSException, NamingException {
		InitialContext iniCtx = new InitialContext();
		Object tmp = iniCtx.lookup("java:/ConnectionFactory");
		QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;

		conn = qcf.createQueueConnection();
		myQueue = (Queue) iniCtx.lookup("queue/" + queueName);
		session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		conn.start();
	}

	/**
	 * @param commandQueue
	 * @throws JMSException
	 * @throws NamingException
	 */
	private void postCommand(LinkedList<StringPair> commandQueue,
			String eventType) throws JMSException, NamingException {
		String command = null;

		// sets command id
		UUID uid = UUID.randomUUID();

		message.setStringProperty("id", uid.toString());
		LOG.debug("set message property id=" + uid.toString());
		message.setIntProperty("entityId", getEntityId());
		LOG.debug("set message property entityId=" + getEntityId());
		message.setIntProperty("order_id", order.getId());
		LOG.debug("set message property order_id=" + order.getId());
		Integer order_line_id = null;
		// populate Message properties with command queue pairs
		for (Iterator<StringPair> it = commandQueue.iterator(); it.hasNext();) {
			StringPair param = (StringPair) it.next();

			if (param.getName().equals("command")) {
				command = param.getValue();
				LOG.debug("command: " + command);
			}

			// lookup order line associated with command
			if (param.getName().equals("order_line_id")) {
				

				try {
					order_line_id = Integer.valueOf(param.getValue());
				} catch (NumberFormatException e) {
				}

				LOG.debug("order line id associated with command: [" + command
						+ "," + order_line_id + "]");

				if (order_line_id != null) { // found order line id associated with command
					OrderBL order_bl = new OrderBL(order);
					OrderLineDTO line=order_bl.getOrderLine(order_line_id);
					LOG.debug("old order line ProvisioningRequestId: "
							+ line.getProvisioningRequestId());
					// update order line's provisioningRequestId
					line.setProvisioningRequestId(uid.toString());
					LOG.debug(" updated order line ProvisioningRequestId: "
							+ line.getProvisioningRequestId());
					// update order line's provisioningStatus
					if (eventType.equals(ProvisioningCommandsRulesTask.ACTIVATED_EVENT_TYPE)) {
						order_bl.setProvisioningStatus(order_line_id,
								Constants.PROVISIONING_STATUS_PENDING_ACTIVE);

					} else if (eventType.equals(ProvisioningCommandsRulesTask.DEACTIVATED_EVENT_TYPE)) {
						order_bl.setProvisioningStatus(order_line_id,
								Constants.PROVISIONING_STATUS_PENDING_INACTIVE);

					}
				}
			}

			message.setStringProperty(param.getName(), param.getValue());
			LOG.debug("set Message property : (" + param.getName() + ","
					+ param.getValue() + ")");
		}

		QueueSender sender = session.createSender(myQueue);

		sender.send(message);
		LOG.debug("Message for command '" + command + "'"
				+ " sent successfully");
		sender.close();

		LOG.debug("adding event log messages");

		// add a log for message id
		eLogger.auditBySystem(entityId, Constants.TABLE_ORDER_LINE, order_line_id, EventLogger.MODULE_PROVISIONING,
				EventLogger.PROVISIONING_UUID, null, uid.toString(), null);
		// add a log for command value
		eLogger.auditBySystem(entityId, Constants.TABLE_ORDER_LINE, order_line_id, EventLogger.MODULE_PROVISIONING,
				EventLogger.PROVISIONING_COMMAND, null, command, null);

	}

	/**
	 * @param commands
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void postCommandsQueue(LinkedList<LinkedList<StringPair>> commands,
			String eventType) throws JMSException, NamingException {
		LOG.debug("calling postCommandsQueue()");

		if (commands == null) {
			LOG.debug("Found NULL commands queue Object");

			return;
		}

		if (commands.isEmpty()) {
			LOG.debug("Found empty commands queue. No commands to post. Returning. ");

			return;
		}

		for (Iterator<LinkedList<StringPair>> it = commands.iterator(); it
				.hasNext();) {
			LinkedList<StringPair> commandQueue = (LinkedList<StringPair>) it
					.next();

			postCommand(commandQueue, eventType);
		}
	}

	/**
	 * @return the entityId
	 */
	public Integer getEntityId() {
		return entityId;
	}

	/**
	 * @return the queueName
	 */
	public String getQueueName() {
		return queueName;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	/**
	 * @param queueName
	 *            the queueName to set
	 */
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
}
