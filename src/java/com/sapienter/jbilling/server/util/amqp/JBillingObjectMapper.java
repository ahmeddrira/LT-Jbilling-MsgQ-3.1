package com.sapienter.jbilling.server.util.amqp;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.sapienter.jbilling.server.entity.InvoiceLineDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.metafields.MetaFieldValueWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.pricing.PriceModelWS;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.user.ValidateUserAndPurchaseWS;


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
		this.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		// Set the mixin configurations
		this.getDeserializationConfig().addMixInAnnotations(UserWS.class, UserWSMixIn.class);
		this.getSerializationConfig().addMixInAnnotations(UserWS.class, UserWSMixIn.class);

		this.getDeserializationConfig().addMixInAnnotations(OrderWS.class, OrderWSMixIn.class);
		this.getSerializationConfig().addMixInAnnotations(OrderWS.class, OrderWSMixIn.class);

		this.getDeserializationConfig().addMixInAnnotations(OrderLineWS.class, OrderLineWSMixIn.class);
		this.getSerializationConfig().addMixInAnnotations(OrderLineWS.class, OrderLineWSMixIn.class);

		this.getDeserializationConfig().addMixInAnnotations(ItemDTOEx.class, ItemDTOExMixIn.class);
		this.getSerializationConfig().addMixInAnnotations(ItemDTOEx.class, ItemDTOExMixIn.class);
		
		this.getDeserializationConfig().addMixInAnnotations(PriceModelWS.class, PriceModelWSMixIn.class);
		this.getSerializationConfig().addMixInAnnotations(PriceModelWS.class, PriceModelWSMixIn.class);
		
		this.getDeserializationConfig().addMixInAnnotations(InvoiceWS.class, InvoiceWSMixIn.class);
		this.getSerializationConfig().addMixInAnnotations(InvoiceWS.class, InvoiceWSMixIn.class);
		
		this.getDeserializationConfig().addMixInAnnotations(InvoiceLineDTO.class, InvoiceLineDTOMixIn.class);
		this.getSerializationConfig().addMixInAnnotations(InvoiceLineDTO.class, InvoiceLineDTOMixIn.class);
		
		this.getDeserializationConfig().addMixInAnnotations(ValidateUserAndPurchaseWS.class, ValidateUserAndPurchaseWSMixIn.class);
		this.getSerializationConfig().addMixInAnnotations(ValidateUserAndPurchaseWS.class, ValidateUserAndPurchaseWSMixIn.class);
		
		this.getDeserializationConfig().addMixInAnnotations(MetaFieldValueWS.class, MetaFieldValueWSMixIn.class);
		this.getSerializationConfig().addMixInAnnotations(MetaFieldValueWS.class, MetaFieldValueWSMixIn.class);

		this.getDeserializationConfig().addMixInAnnotations(StackTraceElement.class, StackTraceElementMixIn.class);
		this.getSerializationConfig().addMixInAnnotations(StackTraceElement.class, StackTraceElementMixIn.class);
	}

	@JsonAnySetter
	public void handleUnknown(String key, Object value) {
		// Ignore
	}

}

abstract class UserWSMixIn {
	@JsonProperty("owningUserId") public Integer id;

	@JsonIgnore public abstract void setOwingBalance(BigDecimal value);
	@JsonIgnore public abstract BigDecimal getOwingBalanceAsDecimal();
	@JsonIgnore public abstract void setDynamicBalance(BigDecimal value);
	@JsonIgnore public abstract BigDecimal getDynamicBalanceAsDecimal();
	@JsonIgnore public abstract void setAutoRecharge(BigDecimal value);
	@JsonIgnore public abstract void setCreditLimit(BigDecimal value);
	@JsonIgnore public abstract void setMetaFields(MetaFieldValueWS[] metaFields);
	@JsonIgnore public abstract Integer getOwningEntityId();

}

abstract class OrderWSMixIn {
	@JsonIgnore public abstract void setTotal(BigDecimal value);
	@JsonIgnore public abstract void setAmount(BigDecimal value);
	@JsonIgnore public abstract Integer getOwningEntityId();
	@JsonIgnore public abstract Integer getOwningUserId();
	@JsonIgnore public abstract BigDecimal getTotalAsDecimal();
}

abstract class OrderLineWSMixIn {
	@JsonIgnore public abstract void setAmount(BigDecimal value);
	@JsonIgnore public abstract void setQuantity(BigDecimal value);
	@JsonIgnore public abstract void setPrice(BigDecimal value);
	@JsonIgnore public abstract void setPriceAsDecimal(BigDecimal value);
}

abstract class ItemDTOExMixIn {
	@JsonIgnore public abstract void setPercentage(BigDecimal value);
	@JsonIgnore public abstract void setPrice(BigDecimal value);
	@JsonIgnore public abstract Integer getOwningEntityId();
	@JsonIgnore public abstract Integer getOwningUserId();
	@JsonIgnore public abstract void setPriceAsDecimal(BigDecimal value);
	@JsonIgnore public abstract BigDecimal getPriceAsDecimal();
}

abstract class PriceModelWSMixIn {
	@JsonIgnore public abstract void setRate(BigDecimal value);
}

abstract class InvoiceWSMixIn {
	@JsonIgnore public abstract void setBalance(BigDecimal value);
	@JsonIgnore public abstract void setCarriedBalance(BigDecimal value);
	@JsonIgnore public abstract void setTotal(BigDecimal value);
	@JsonIgnore public abstract BigDecimal getTotalAsDecimal();
	@JsonIgnore public abstract BigDecimal getBalanceAsDecimal();
	@JsonIgnore public abstract BigDecimal getCarriedBalanceAsDecimal();
	@JsonIgnore public abstract Integer getOwningEntityId();
	@JsonIgnore public abstract Integer getOwningUserId();
}

abstract class InvoiceLineDTOMixIn {
	@JsonIgnore public abstract void setAmount(BigDecimal value);
	@JsonIgnore public abstract void setPrice(BigDecimal value);
	@JsonIgnore public abstract void setQuantity(BigDecimal value);
}

abstract class ValidateUserAndPurchaseWSMixIn {
    @JsonProperty public abstract Boolean getSuccess();
    @JsonProperty public abstract String[] getMessage();
    @JsonProperty public abstract Boolean getAuthorised();
    @JsonProperty public abstract String getQuantity();
    
	@JsonIgnore public abstract BigDecimal getQuantityAsDecimal();
	@JsonIgnore public abstract void setQuantity(BigDecimal value);
	@JsonIgnore public abstract void setQuantity(Double value);
}

abstract class MetaFieldValueWSMixIn {
	@JsonIgnore public abstract void setDefaultValue(Object value);
	@JsonIgnore public abstract Object getDefaultValue();
	@JsonIgnore public abstract BigDecimal getDecimalValueAsDecimal();
	@JsonIgnore public abstract void setDecimalValueAsDecimal(BigDecimal value);
}

@JsonIgnoreProperties("declaringClass")
abstract class StackTraceElementMixIn {
}
