package com.sapienter.jbilling.server.util.amqp;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.sapienter.jbilling.server.entity.InvoiceLineDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.pricing.PriceModelWS;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.user.ValidatePurchaseWS;

/**
 * Object mapper for handling the overriden methods in JBilling DTO's
 * Using pattern as described here http://stackoverflow.com/questions/6346018/deserializing-json-into-object-with-overloaded-methods-using-jackson
 * @author smit005
 *
 */
public class JBillingObjectMapper extends ObjectMapper {

	public JBillingObjectMapper() {
		super();

		this.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		this.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// Set the mixin configurations
		this.getDeserializationConfig().addMixInAnnotations(UserWS.class, UserWSMixIn.class);
		this.getDeserializationConfig().addMixInAnnotations(OrderWS.class, OrderWSMixIn.class);
		this.getDeserializationConfig().addMixInAnnotations(OrderLineWS.class, OrderLineWSMixIn.class);
		this.getDeserializationConfig().addMixInAnnotations(ItemDTOEx.class, ItemDTOExMixIn.class);
		this.getDeserializationConfig().addMixInAnnotations(PriceModelWS.class, PriceModelWSMixIn.class);
		this.getDeserializationConfig().addMixInAnnotations(InvoiceWS.class, InvoiceWSMixIn.class);
		this.getDeserializationConfig().addMixInAnnotations(InvoiceLineDTO.class, InvoiceLineDTOMixIn.class);
		this.getDeserializationConfig().addMixInAnnotations(ValidatePurchaseWS.class, ValidatePurchaseWSMixIn.class);
	}
}

abstract class UserWSMixIn {
	@JsonIgnore public abstract void setOwingBalance(BigDecimal value);
	@JsonIgnore public abstract void setDynamicBalance(BigDecimal value);
	@JsonIgnore public abstract void setAutoRecharge(BigDecimal value);
	@JsonIgnore public abstract void setCreditLimit(BigDecimal value);
}

abstract class OrderWSMixIn {
	@JsonIgnore public abstract void setTotal(BigDecimal value);
	@JsonIgnore public abstract void setAmount(BigDecimal value);
	@JsonIgnore public abstract void getOwningEntityId();
}

abstract class OrderLineWSMixIn {
	@JsonIgnore public abstract void setAmount(BigDecimal value);
	@JsonIgnore public abstract void setQuantity(BigDecimal value);
	@JsonIgnore public abstract void setPrice(BigDecimal value);
}

abstract class ItemDTOExMixIn {
	@JsonIgnore public abstract void setPercentage(BigDecimal value);
	@JsonIgnore public abstract void setPrice(BigDecimal value);
}

abstract class PriceModelWSMixIn {
	@JsonIgnore public abstract void setRate(BigDecimal value);
}

abstract class InvoiceWSMixIn {
	@JsonIgnore public abstract void setBalance(BigDecimal value);
	@JsonIgnore public abstract void setCarriedBalance(BigDecimal value);
	@JsonIgnore public abstract void setTotal(BigDecimal value);
}

abstract class InvoiceLineDTOMixIn {
	@JsonIgnore public abstract void setAmount(BigDecimal value);
	@JsonIgnore public abstract void setPrice(BigDecimal value);
	@JsonIgnore public abstract void setQuantity(BigDecimal value);
}

abstract class ValidatePurchaseWSMixIn {
	@JsonIgnore public abstract void setQuantity(BigDecimal value);
}