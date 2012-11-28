package com.sapienter.jbilling.server.util.amqp;

import uk.co.jemos.podam.api.DataProviderStrategy;
import uk.co.jemos.podam.api.RandomDataProviderStrategy;
import uk.co.jemos.podam.dto.AttributeMetadata;

/**
 * Extension of the {@link RandomDataProviderStrategy} that generates strings that only contain digits.
 * Required because some of the JBilling DTO's store numbers as strings, and these could throw {@link NumberFormatException} for
 * random strings. 
 *  
 * @author smit005
 *
 */
public class JBillingDataStrategy implements DataProviderStrategy {

	private DataProviderStrategy randomDataProviderStrategy = RandomDataProviderStrategy.getInstance();

	public JBillingDataStrategy() {
	}

	@Override
	public Boolean getBoolean(AttributeMetadata arg0) {
		return randomDataProviderStrategy.getBoolean(arg0);
	}

	@Override
	public Byte getByte(AttributeMetadata arg0) {
		return randomDataProviderStrategy.getByte(arg0);
	}

	@Override
	public Byte getByteInRange(byte arg0, byte arg1, AttributeMetadata arg2) {
		return randomDataProviderStrategy.getByteInRange(arg0, arg1, arg2);
	}

	@Override
	public Character getCharacter(AttributeMetadata arg0) {
		return randomDataProviderStrategy.getCharacter(arg0);
	}

	@Override
	public Character getCharacterInRange(char arg0, char arg1,
			AttributeMetadata arg2) {
		return randomDataProviderStrategy.getCharacterInRange(arg0, arg1, arg2);
	}

	@Override
	public Double getDouble(AttributeMetadata arg0) {
		return randomDataProviderStrategy.getDouble(arg0);
	}

	@Override
	public Double getDoubleInRange(double arg0, double arg1,
			AttributeMetadata arg2) {
		return randomDataProviderStrategy.getDoubleInRange(arg0, arg1, arg2);
	}

	@Override
	public Float getFloat(AttributeMetadata arg0) {
		return randomDataProviderStrategy.getFloat(arg0);
	}

	@Override
	public Float getFloatInRange(float arg0, float arg1, AttributeMetadata arg2) {
		return randomDataProviderStrategy.getFloatInRange(arg0, arg1, arg2);
	}

	@Override
	public Integer getInteger(AttributeMetadata arg0) {
		return randomDataProviderStrategy.getInteger(arg0);
	}

	@Override
	public int getIntegerInRange(int arg0, int arg1, AttributeMetadata arg2) {
		return randomDataProviderStrategy.getIntegerInRange(arg0, arg1, arg2);
	}

	@Override
	public Long getLong(AttributeMetadata arg0) {
		return randomDataProviderStrategy.getLong(arg0);
	}

	@Override
	public Long getLongInRange(long arg0, long arg1, AttributeMetadata arg2) {
		return randomDataProviderStrategy.getLongInRange(arg0, arg1, arg2);
	}

	@Override
	public Short getShort(AttributeMetadata arg0) {
		return randomDataProviderStrategy.getShort(arg0);
	}

	@Override
	public Short getShortInRange(short arg0, short arg1, AttributeMetadata arg2) {
		return randomDataProviderStrategy.getShortInRange(arg0, arg1, arg2);
	}

	@Override
	public String getStringOfLength(int arg0, AttributeMetadata arg1) {
		return randomDataProviderStrategy.getInteger(arg1).toString().substring(0, arg0);
	}

	@Override
	public String getStringValue(AttributeMetadata arg0) {
		return randomDataProviderStrategy.getInteger(arg0).toString();
	}
	
}
