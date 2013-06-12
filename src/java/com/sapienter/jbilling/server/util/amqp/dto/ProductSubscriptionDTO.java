package com.sapienter.jbilling.server.util.amqp.dto;

public class ProductSubscriptionDTO {
	private Integer itemId;
	private String number;
	private String glCode;
	private Integer deleted;

	private Integer entityId;
	private String description = null;
	private String promoCode = null;
	private Integer currencyId = null;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductSubscriptionDTO [itemId=").append(itemId)
				.append(", number=").append(number).append(", glCode=")
				.append(glCode).append(", deleted=").append(deleted)
				.append(", entityId=").append(entityId)
				.append(", description=").append(description)
				.append(", promoCode=").append(promoCode)
				.append(", currencyId=").append(currencyId).append("]");
		return builder.toString();
	}
}
