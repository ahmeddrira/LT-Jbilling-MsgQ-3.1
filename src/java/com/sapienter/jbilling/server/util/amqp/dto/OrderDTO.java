package com.sapienter.jbilling.server.util.amqp.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderDTO {
	private Integer id;
	private Integer userId;
	private Integer period;
	private Integer currencyId;
	private Date activeDate;
	private boolean isCurrent;
	private Integer billingTypeId;
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

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Date getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public Integer getBillingTypeId() {
		return billingTypeId;
	}

	public void setBillingTypeId(Integer billingTypeId) {
		this.billingTypeId = billingTypeId;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderDTO [id=").append(id).append(", userId=")
				.append(userId).append(", period=").append(period)
				.append(", currencyId=").append(currencyId)
				.append(", activeDate=").append(activeDate)
				.append(", isCurrent=").append(isCurrent)
				.append(", billingTypeId=").append(billingTypeId)
				.append(", orderLines=").append(orderLines)
				.append(", metaFields=").append(metaFields).append("]");
		return builder.toString();
	}

}
