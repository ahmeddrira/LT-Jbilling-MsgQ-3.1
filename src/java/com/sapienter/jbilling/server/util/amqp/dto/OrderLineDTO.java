package com.sapienter.jbilling.server.util.amqp.dto;

import java.math.BigDecimal;

import com.sapienter.jbilling.server.util.amqp.types.OrderLineType;

public class OrderLineDTO {
	private Integer id;
	private Integer itemId;
	private OrderLineType lineType;
	private boolean useItem;
	private BigDecimal quantity;
	private BigDecimal amount;
	private String productNumber;
	private String productGLCode;
	private String productDescription;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public OrderLineType getLineType() {
		return lineType;
	}

	public void setLineType(OrderLineType lineType) {
		this.lineType = lineType;
	}

	public boolean isUseItem() {
		return useItem;
	}

	public void setUseItem(boolean useItem) {
		this.useItem = useItem;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public String getProductGLCode() {
		return productGLCode;
	}

	public void setProductGLCode(String productGLCode) {
		this.productGLCode = productGLCode;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderLineDTO [id=").append(id).append(", itemId=")
				.append(itemId).append(", lineType=").append(lineType)
				.append(", useItem=").append(useItem).append(", quantity=")
				.append(quantity).append(", amount=").append(amount)
				.append(", productNumber=").append(productNumber)
				.append(", productGLCode=").append(productGLCode)
				.append(", productDescription=").append(productDescription)
				.append("]");
		return builder.toString();
	}
}
