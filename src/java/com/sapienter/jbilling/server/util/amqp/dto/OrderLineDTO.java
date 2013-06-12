package com.sapienter.jbilling.server.util.amqp.dto;

import java.math.BigDecimal;

public class OrderLineDTO {
	private Integer id;
	private Integer itemId;
	private Integer typeId;
	private boolean useItem;
	private BigDecimal quantity;
	private BigDecimal amount;
}
