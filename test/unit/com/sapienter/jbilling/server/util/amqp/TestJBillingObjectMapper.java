package com.sapienter.jbilling.server.util.amqp;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.sapienter.jbilling.server.metafields.MetaFieldValueWS;


/**
 * Object mapper for handling the overriden methods in JBilling DTO's
 * Using pattern as described here http://stackoverflow.com/questions/6346018/deserializing-json-into-object-with-overloaded-methods-using-jackson
 * @author smit005
 *
 */
public class TestJBillingObjectMapper extends JBillingObjectMapper {

	public TestJBillingObjectMapper() {
		super();

		this.getSerializationConfig().addMixInAnnotations(MapObjectValue.class, MapObjectValueMixIn.class);
		this.getDeserializationConfig().addMixInAnnotations(MapObjectValue.class, MapObjectValueMixIn.class);
		this.getSerializationConfig().addMixInAnnotations(MetaFieldValueWS.class, MetaFieldValueWSMixIn2.class);
		this.getDeserializationConfig().addMixInAnnotations(MetaFieldValueWS.class, MetaFieldValueWSMixIn2.class);
	}

}

abstract class MapObjectValueMixIn {
	@JsonIgnore abstract Object getValue();
	@JsonIgnore abstract void setValue(Object o);	
}

abstract class MetaFieldValueWSMixIn2 {
	@JsonIgnore public abstract void setDefaultValue(Object value);
	@JsonIgnore public abstract Object getDefaultValue();
}
