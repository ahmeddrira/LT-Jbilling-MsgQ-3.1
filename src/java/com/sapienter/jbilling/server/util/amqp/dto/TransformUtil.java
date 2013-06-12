package com.sapienter.jbilling.server.util.amqp.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sapienter.jbilling.server.metafields.MetaFieldValueWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.amqp.types.AccountStatus;
import com.sapienter.jbilling.server.util.amqp.types.OrderBillingType;
import com.sapienter.jbilling.server.util.amqp.types.OrderLineType;
import com.sapienter.jbilling.server.util.amqp.types.OrderPeriodType;

public class TransformUtil {
	private TransformUtil() {
	}
	
	private static Map<Integer, AccountStatus> accountStatusMap;
	private static Map<Integer, OrderLineType> orderLineTypeMap;
	private static Map<Integer, OrderBillingType> orderBillingTypeMap;
	private static Map<Integer, OrderPeriodType> orderPeriodTypeMap;
	
	static {
		accountStatusMap = new HashMap<Integer, AccountStatus>();
		accountStatusMap.put(1, AccountStatus.active);
		accountStatusMap.put(2, AccountStatus.overdue1);
		accountStatusMap.put(3, AccountStatus.overdue2);
		accountStatusMap.put(4, AccountStatus.overdue3);
		accountStatusMap.put(5, AccountStatus.suspended1);
		accountStatusMap.put(6, AccountStatus.suspended2);
		accountStatusMap.put(7, AccountStatus.suspended3);
		accountStatusMap.put(8, AccountStatus.deleted);
		
		orderLineTypeMap = new HashMap<Integer, OrderLineType>();
		orderLineTypeMap.put(Constants.ORDER_LINE_TYPE_ITEM, OrderLineType.itemLine);
		orderLineTypeMap.put(Constants.ORDER_LINE_TYPE_TAX, OrderLineType.taxLine);
		orderLineTypeMap.put(Constants.ORDER_LINE_TYPE_PENALTY, OrderLineType.penaltyLine);
		
		orderBillingTypeMap = new HashMap<Integer, OrderBillingType>();
		orderBillingTypeMap.put(Constants.ORDER_BILLING_POST_PAID, OrderBillingType.postPaid);
		orderBillingTypeMap.put(Constants.ORDER_BILLING_PRE_PAID, OrderBillingType.prePaid);
		
		orderPeriodTypeMap = new HashMap<Integer, OrderPeriodType>();
		orderPeriodTypeMap.put(Constants.ORDER_PERIOD_ALL_ORDERS, OrderPeriodType.allOrders);
		orderPeriodTypeMap.put(Constants.ORDER_PERIOD_ONCE, OrderPeriodType.once);
	}

	private static <K, V> K getKey(Map<K, V> map, V value) {
		for (Entry<K, V> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static AccountStatus toAccountStatus(Integer fromValue) {
		return accountStatusMap.get(fromValue);
	}
	
	public static Integer transform(AccountStatus fromValue) {
		return getKey(accountStatusMap, fromValue);
	}

	public static OrderLineType toOrderLineType(Integer fromValue) {
		return orderLineTypeMap.get(fromValue);
	}
	
	public static Integer transform(OrderLineType fromValue) {
		return getKey(orderLineTypeMap, fromValue);
	}

	public static OrderBillingType toOrderBillingType(Integer fromValue) {
		return orderBillingTypeMap.get(fromValue);
	}

	public static Integer transform(OrderPeriodType fromValue) {
		return getKey(orderPeriodTypeMap, fromValue);
	}

	public static OrderPeriodType toOrderPeriodType(Integer fromValue) {
		return orderPeriodTypeMap.get(fromValue);
	}

	public static Integer transform(OrderBillingType fromValue) {
		return getKey(orderBillingTypeMap, fromValue);
	}	
	
	public static OrderDTO transform(OrderWS fromValue) {
		OrderDTO toValue = new OrderDTO();
		
		toValue.setActiveSince(fromValue.getActiveSince());
		toValue.setActiveUntil(fromValue.getActiveUntil());
		toValue.setBillingType(TransformUtil.toOrderBillingType(fromValue.getBillingTypeId()));
		toValue.setCurrencyId(fromValue.getCurrencyId());
		toValue.setCreatedById(fromValue.getCreatedBy());
		toValue.setIsCurrent(fromValue.getIsCurrent());
		toValue.setId(fromValue.getId());
		toValue.setPeriod(TransformUtil.toOrderPeriodType(fromValue.getPeriod()));
		toValue.setUserId(fromValue.getUserId());

		for (MetaFieldValueWS mf : fromValue.getMetaFields()) {
			toValue.getMetaFields().put(mf.getFieldName(), mf.getStringValue());
		}
		
		for (OrderLineWS ol : fromValue.getOrderLines()) {
			toValue.getOrderLines().add(TransformUtil.transform(ol));
		}
		
		return toValue;
	}

	public static OrderLineDTO transform(OrderLineWS fromValue) {
		OrderLineDTO toValue = new OrderLineDTO();
		
		toValue.setAmount(fromValue.getAmountAsDecimal());
		toValue.setId(fromValue.getId());
		toValue.setItemId(fromValue.getItemId());
		toValue.setQuantity(fromValue.getQuantityAsDecimal());
		toValue.setLineType(TransformUtil.toOrderLineType(fromValue.getTypeId()));
		toValue.setUseItem(fromValue.getUseItem());
		toValue.setProductDescription(fromValue.getItemDto().getDescription());
		toValue.setProductGLCode(fromValue.getItemDto().getGlCode());
		toValue.setProductNumber(fromValue.getItemDto().getNumber());
		
		return toValue;
	}

	public static OrderWS transform(
			com.sapienter.jbilling.server.util.amqp.dto.OrderDTO fromValue) {
		OrderWS toValue = new OrderWS();
		
		toValue.setUserId(fromValue.getUserId());
		toValue.setPeriod(TransformUtil.transform(fromValue.getPeriod()));
		toValue.setCurrencyId(fromValue.getCurrencyId());
		toValue.setActiveSince(fromValue.getActiveSince());	
		toValue.setActiveUntil(fromValue.getActiveUntil());
		toValue.setIsCurrent(fromValue.getIsCurrent());
		toValue.setBillingTypeId(TransformUtil.transform(fromValue.getBillingType()));

		toValue.setOrderLines(TransformUtil.transform(fromValue.getOrderLines()));
		toValue.setMetaFields(TransformUtil.transform(fromValue.getMetaFields()));
		
		return null;
	}

	public static MetaFieldValueWS[] transform(Map<String, String> fromValue) {
		MetaFieldValueWS[] toValue = new MetaFieldValueWS[fromValue.size()];
		int i = 0;
		
		for (Entry<String, String> fromEntry : fromValue.entrySet()) {
			MetaFieldValueWS mfv = new MetaFieldValueWS();
			mfv.setFieldName(fromEntry.getKey());
			mfv.setStringValue(fromEntry.getValue());
			toValue[i++] = mfv;
		}
		
		return toValue;
	}

	public static OrderLineWS[] transform(Collection<OrderLineDTO> fromValue) {
		OrderLineWS[] toValue = new OrderLineWS[fromValue.size()];
		int i = 0;
		
		for (OrderLineDTO fromEntry : fromValue) {
			toValue[i++] = TransformUtil.transform(fromEntry);
		}
		
		return toValue;
	}

	private static OrderLineWS transform(OrderLineDTO fromValue) {
		OrderLineWS toValue = new OrderLineWS();
		
		toValue.setId(fromValue.getId());
		toValue.setItemId(fromValue.getItemId());
		toValue.setTypeId(TransformUtil.transform(fromValue.getLineType()));
		toValue.setQuantity(fromValue.getQuantity());
		toValue.setUseItem(fromValue.isUseItem());
		
		return toValue;
	}

}
