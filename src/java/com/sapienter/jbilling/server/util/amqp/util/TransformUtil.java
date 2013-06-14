package com.sapienter.jbilling.server.util.amqp.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sapienter.jbilling.common.CommonConstants;
import com.sapienter.jbilling.server.metafields.MetaFieldValueWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.amqp.dto.OrderDTO;
import com.sapienter.jbilling.server.util.amqp.dto.OrderLineDTO;
import com.sapienter.jbilling.server.util.amqp.types.AccountStatusType;
import com.sapienter.jbilling.server.util.amqp.types.BalanceType;
import com.sapienter.jbilling.server.util.amqp.types.OrderBillingType;
import com.sapienter.jbilling.server.util.amqp.types.OrderLineType;
import com.sapienter.jbilling.server.util.amqp.types.OrderPeriodType;
import com.sapienter.jbilling.server.util.amqp.types.OrderStatusType;

public class TransformUtil {
	private TransformUtil() {
	}

	private static Map<Integer, AccountStatusType> accountStatusTypeMap;
	private static Map<Integer, BalanceType> balanceTypeMap;
	private static Map<Integer, OrderLineType> orderLineTypeMap;
	private static Map<Integer, OrderBillingType> orderBillingTypeMap;
	private static Map<Integer, OrderPeriodType> orderPeriodTypeMap;
	private static Map<Integer, OrderStatusType> orderStatusTypeMap;

	static {
		accountStatusTypeMap = new HashMap<Integer, AccountStatusType>();
		accountStatusTypeMap.put(1, AccountStatusType.active);
		accountStatusTypeMap.put(2, AccountStatusType.overdue1);
		accountStatusTypeMap.put(3, AccountStatusType.overdue2);
		accountStatusTypeMap.put(4, AccountStatusType.overdue3);
		accountStatusTypeMap.put(5, AccountStatusType.suspended1);
		accountStatusTypeMap.put(6, AccountStatusType.suspended2);
		accountStatusTypeMap.put(7, AccountStatusType.suspended3);
		accountStatusTypeMap.put(8, AccountStatusType.deleted);

		balanceTypeMap = new HashMap<Integer, BalanceType>();
		balanceTypeMap.put(CommonConstants.BALANCE_NO_DYNAMIC,
				BalanceType.noDynamicBalance);
		balanceTypeMap.put(CommonConstants.BALANCE_PRE_PAID,
				BalanceType.prePaid);
		balanceTypeMap.put(CommonConstants.BALANCE_CREDIT_LIMIT,
				BalanceType.creditLimit);

		orderLineTypeMap = new HashMap<Integer, OrderLineType>();
		orderLineTypeMap.put(Constants.ORDER_LINE_TYPE_ITEM,
				OrderLineType.itemLine);
		orderLineTypeMap.put(Constants.ORDER_LINE_TYPE_TAX,
				OrderLineType.taxLine);
		orderLineTypeMap.put(Constants.ORDER_LINE_TYPE_PENALTY,
				OrderLineType.penaltyLine);

		orderBillingTypeMap = new HashMap<Integer, OrderBillingType>();
		orderBillingTypeMap.put(Constants.ORDER_BILLING_POST_PAID,
				OrderBillingType.postPaid);
		orderBillingTypeMap.put(Constants.ORDER_BILLING_PRE_PAID,
				OrderBillingType.prePaid);

		orderPeriodTypeMap = new HashMap<Integer, OrderPeriodType>();
		orderPeriodTypeMap.put(Constants.ORDER_PERIOD_ALL_ORDERS,
				OrderPeriodType.allOrders);
		orderPeriodTypeMap.put(Constants.ORDER_PERIOD_ONCE,
				OrderPeriodType.once);

		orderStatusTypeMap = new HashMap<Integer, OrderStatusType>();
		orderStatusTypeMap.put(CommonConstants.ORDER_STATUS_ACTIVE,
				OrderStatusType.active);
		orderStatusTypeMap.put(CommonConstants.ORDER_STATUS_FINISHED,
				OrderStatusType.finished);
		orderStatusTypeMap.put(CommonConstants.ORDER_STATUS_SUSPENDED,
				OrderStatusType.suspended);
		orderStatusTypeMap.put(CommonConstants.ORDER_STATUS_SUSPENDED_AGEING,
				OrderStatusType.suspendedAgeing);
	}

	private static <K, V> K getKey(Map<K, V> map, V value) {
		for (Entry<K, V> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static AccountStatusType toAccountStatus(Integer fromValue) {
		return accountStatusTypeMap.get(fromValue);
	}

	public static Integer transform(AccountStatusType fromValue) {
		return getKey(accountStatusTypeMap, fromValue);
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

	public static OrderStatusType toOrderStatusType(Integer fromValue) {
		return orderStatusTypeMap.get(fromValue);
	}

	public static Integer transform(OrderStatusType fromValue) {
		return getKey(orderStatusTypeMap, fromValue);
	}

	public static OrderDTO transform(OrderWS fromValue) {
		OrderDTO toValue = new OrderDTO();

		toValue.setActiveSince(fromValue.getActiveSince());
		toValue.setActiveUntil(fromValue.getActiveUntil());
		toValue.setBillingType(TransformUtil.toOrderBillingType(fromValue
				.getBillingTypeId()));
		toValue.setCurrencyId(fromValue.getCurrencyId());
		toValue.setCreatedById(fromValue.getCreatedBy());
		toValue.setIsCurrent(fromValue.getIsCurrent());
		toValue.setId(fromValue.getId());
		toValue.setPeriod(TransformUtil.toOrderPeriodType(fromValue.getPeriod()));
		toValue.setUserId(fromValue.getUserId());
		toValue.setStatus(TransformUtil.toOrderStatusType(fromValue
				.getStatusId()));

		if (fromValue.getMetaFields() != null) {
			for (MetaFieldValueWS mf : fromValue.getMetaFields()) {
				toValue.getMetaFields().put(mf.getFieldName(),
						mf.getStringValue());
			}
		}

		if (fromValue.getOrderLines() != null) {
			for (OrderLineWS ol : fromValue.getOrderLines()) {
				toValue.getOrderLines().add(TransformUtil.transform(ol));
			}
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
		toValue.setProductDescription(fromValue.getDescription());
		if (fromValue.getItemDto() != null) {
			toValue.setProductDescription(fromValue.getItemDto()
					.getDescription());
			toValue.setProductGLCode(fromValue.getItemDto().getGlCode());
			toValue.setProductNumber(fromValue.getItemDto().getNumber());
		}

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
		toValue.setBillingTypeId(TransformUtil.transform(fromValue
				.getBillingType()));
		toValue.setStatusId(TransformUtil.transform(fromValue.getStatus()));

		toValue.setOrderLines(TransformUtil.transform(fromValue.getOrderLines()));
		toValue.setMetaFields(TransformUtil.transform(fromValue.getMetaFields()));

		return null;
	}

	public static MetaFieldValueWS[] transform(Map<String, String> fromValue) {
		if (fromValue == null) {
			return null;
		}

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
		if (fromValue == null) {
			return null;
		}

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
