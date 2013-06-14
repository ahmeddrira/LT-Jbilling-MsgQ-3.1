package com.sapienter.jbilling.server.util.amqp.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sapienter.jbilling.server.util.amqp.types.OrderBillingType;
import com.sapienter.jbilling.server.util.amqp.types.OrderPeriodType;
import com.sapienter.jbilling.server.util.amqp.types.OrderStatusType;

public class OrderDTO {
	private Integer id;
	private Integer userId;
	private Integer createdById;
	private OrderPeriodType period;
	private Integer currencyId;
	private Date activeSince;
	private Date activeUntil;
	private Integer isCurrent;
	private OrderBillingType billingType;
	private OrderStatusType status;
	private Collection<OrderLineDTO> orderLines;
	private Map<String, String> metaFields;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public OrderPeriodType getPeriod() {
		return period;
	}

	public void setPeriod(OrderPeriodType period) {
		this.period = period;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Date getActiveUntil() {
		return activeUntil;
	}

	public void setActiveUntil(Date activeUntil) {
		this.activeUntil = activeUntil;
	}

	public Date getActiveSince() {
		return activeSince;
	}

	public void setActiveSince(Date date) {
		this.activeSince = date;
	}

	public OrderBillingType getBillingType() {
		return billingType;
	}

	public void setBillingType(OrderBillingType billingType) {
		this.billingType = billingType;
	}

	public OrderStatusType getStatus() {
		return status;
	}

	public void setStatus(OrderStatusType status) {
		this.status = status;
	}

	public Collection<OrderLineDTO> getOrderLines() {
		if (orderLines == null) {
			orderLines = new ArrayList<OrderLineDTO>();
		}
		return orderLines;
	}

	public void setOrderLines(Collection<OrderLineDTO> orderLines) {
		this.orderLines = orderLines;
	}

	public Map<String, String> getMetaFields() {
		if (metaFields == null) {
			metaFields = new HashMap<String, String>();
		}
		return metaFields;
	}

	public void setMetaFields(Map<String, String> metaFields) {
		this.metaFields = metaFields;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public Integer getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(Integer isCurrent) {
		this.isCurrent = isCurrent;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderDTO [id=").append(id).append(", userId=")
				.append(userId).append(", createdById=").append(createdById)
				.append(", period=").append(period).append(", currencyId=")
				.append(currencyId).append(", activeSince=")
				.append(activeSince).append(", activeUntil=")
				.append(activeUntil).append(", isCurrent=").append(isCurrent)
				.append(", billingType=").append(billingType)
				.append(", status=").append(status).append(", orderLines=")
				.append(orderLines).append(", metaFields=").append(metaFields)
				.append("]");
		return builder.toString();
	}

}
