package com.sapienter.jbilling.server.util.amqp.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.liquidtelecom.jbilling.api.dto.ContactDTO;
import com.liquidtelecom.jbilling.api.dto.OrderDTO;
import com.liquidtelecom.jbilling.api.dto.OrderLineDTO;
import com.liquidtelecom.jbilling.api.dto.UserDTO;
import com.liquidtelecom.jbilling.api.types.AccountStatusType;
import com.liquidtelecom.jbilling.api.types.BalanceType;
import com.liquidtelecom.jbilling.api.types.OrderBillingType;
import com.liquidtelecom.jbilling.api.types.OrderLineType;
import com.liquidtelecom.jbilling.api.types.OrderPeriodType;
import com.liquidtelecom.jbilling.api.types.OrderStatusType;
import com.sapienter.jbilling.common.CommonConstants;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.metafields.MetaFieldValueWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.Constants;

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

	public static BalanceType toBalanceType(Integer fromValue) {
		return balanceTypeMap.get(fromValue);
	}

	public static Integer transform(BalanceType fromValue) {
		return getKey(balanceTypeMap, fromValue);
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
		toValue.setNotes(fromValue.getNotes());
		
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

	public static OrderWS transform(OrderDTO fromValue) {
		OrderWS toValue = new OrderWS();

		toValue.setUserId(fromValue.getUserId());
		toValue.setPeriod(TransformUtil.transform(fromValue.getPeriod()));
		toValue.setCurrencyId(fromValue.getCurrencyId());
		toValue.setCreatedBy(fromValue.getCreatedById());
		toValue.setActiveSince(fromValue.getActiveSince());
		toValue.setActiveUntil(fromValue.getActiveUntil());
		toValue.setIsCurrent(fromValue.getIsCurrent());
		toValue.setBillingTypeId(TransformUtil.transform(fromValue
				.getBillingType()));
		toValue.setStatusId(TransformUtil.transform(fromValue.getStatus()));
		toValue.setNotes(fromValue.getNotes());

		toValue.setOrderLines(TransformUtil.transform(fromValue.getOrderLines()));
		toValue.setMetaFields(TransformUtil.transform(fromValue.getMetaFields()));

		return toValue;
	}

	public static OrderDTO transformFromWS(OrderWS fromValue) {
		OrderDTO toValue = new OrderDTO();

		toValue.setActiveSince(fromValue.getActiveSince());
		toValue.setActiveUntil(fromValue.getActiveUntil());
		toValue.setBillingType(TransformUtil.toOrderBillingType(fromValue
				.getBillingTypeId()));
		toValue.setCreatedById(fromValue.getCreatedBy());
		toValue.setCurrencyId(fromValue.getCurrencyId());
		toValue.setId(fromValue.getId());
		toValue.setIsCurrent(fromValue.getIsCurrent());
		toValue.setStatus(TransformUtil.toOrderStatusType(fromValue
				.getStatusId()));

		toValue.setOrderLines(TransformUtil.transform(fromValue.getOrderLines()));
		toValue.setMetaFields(TransformUtil.transform(fromValue.getMetaFields()));

		return toValue;
	}

	private static Map<String, String> transform(MetaFieldValueWS[] fromValue) {
		if (fromValue == null) {
			return null;
		}

		Map<String, String> toValue = new HashMap<String, String>();
		for (MetaFieldValueWS fromMF : fromValue) {
			toValue.put(fromMF.getFieldName(), fromMF.getStringValue());
		}

		return toValue;
	}

	private static Collection<OrderLineDTO> transform(OrderLineWS[] fromValue) {
		if (fromValue == null) {
			return null;
		}

		Collection<OrderLineDTO> toValue = new ArrayList<OrderLineDTO>();
		for (OrderLineWS fromOrder : fromValue) {
			toValue.add(TransformUtil.transform(fromOrder));
		}

		return toValue;
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

		toValue.setId(fromValue.getId() != null ? fromValue.getId() : 0);
		toValue.setItemId(fromValue.getItemId());
		toValue.setTypeId(TransformUtil.transform(fromValue.getLineType()));
		toValue.setQuantity(fromValue.getQuantity());
		toValue.setUseItem(fromValue.isUseItem());

		return toValue;
	}

	public static UserDTO transform(UserWS fromValue) {
		if (fromValue == null) {
			return null;
		}

		UserDTO toValue = new UserDTO();

		toValue.setAccountStatus(TransformUtil.toAccountStatus(fromValue
				.getStatusId()));
		toValue.setBalanceType(TransformUtil.toBalanceType(fromValue
				.getBalanceType()));
		toValue.setCompanyName(fromValue.getCompanyName());
		toValue.setCreditLimit(fromValue.getCreditLimitAsDecimal());
		toValue.setCurrencyId(fromValue.getCurrencyId());
		toValue.setDynamicBalance(fromValue.getDynamicBalanceAsDecimal());
		toValue.setBalance(fromValue.getOwingBalanceAsDecimal());
		toValue.setUserName(fromValue.getUserName());
		toValue.setId(fromValue.getUserId());
		toValue.setContact(TransformUtil.transform(fromValue.getContact()));

		return toValue;
	}

	private static ContactDTO transform(ContactWS fromValue) {
		if (fromValue == null) {
			return null;
		}

		ContactDTO toValue = new ContactDTO();

		toValue.setAddress1(fromValue.getAddress1());
		toValue.setAddress2(toValue.getAddress2());
		toValue.setCity(fromValue.getCity());
		toValue.setContactTypeDescr(fromValue.getContactTypeDescr());
		toValue.setContactTypeId(fromValue.getContactTypeId());
		toValue.setCountryCode(fromValue.getCountryCode());
		toValue.setCreateDate(fromValue.getCreateDate());
		toValue.setDeleted(fromValue.getDeleted());
		toValue.setEmail(fromValue.getEmail());
		toValue.setFaxAreaCode(fromValue.getFaxAreaCode());
		toValue.setFaxCountryCode(fromValue.getFaxCountryCode());
		toValue.setFaxNumber(fromValue.getFaxNumber());
		toValue.setFirstName(fromValue.getFirstName());
		toValue.setId(fromValue.getId());
		toValue.setInclude(fromValue.getInclude());
		toValue.setInitial(fromValue.getInitial());
		toValue.setLastName(fromValue.getLastName());
		toValue.setOrganizationName(fromValue.getOrganizationName());
		toValue.setPhoneAreaCode(fromValue.getPhoneAreaCode());
		toValue.setPhoneCountryCode(fromValue.getPhoneCountryCode());
		toValue.setPhoneNumber(fromValue.getPhoneNumber());
		toValue.setPostalCode(fromValue.getPostalCode());
		toValue.setStateProvince(fromValue.getStateProvince());
		toValue.setTitle(fromValue.getTitle());
		toValue.setType(fromValue.getType());

		return toValue;
	}

	public static OrderWS merge(
			com.liquidtelecom.jbilling.api.dto.OrderDTO fromValue,
			OrderWS toValue) {

		try {
			// The enumerated types can't call copyIfNotNull() as they have to transform the value first.
			
			copyIfNotNull(fromValue, "id", toValue, "id");
			if (fromValue.getPeriod() != null) {
				toValue.setPeriod(TransformUtil.transform(fromValue.getPeriod()));				
			}
			copyIfNotNull(fromValue, "createdById", toValue, "createdBy");
			copyIfNotNull(fromValue, "currencyId", toValue, "currencyId");
			copyIfNotNull(fromValue, "activeSince", toValue, "activeSince");
			copyIfNotNull(fromValue, "activeUntil", toValue, "activeUntil");
			copyIfNotNull(fromValue, "isCurrent", toValue, "isCurrent");
			if (fromValue.getBillingType() != null) {
				toValue.setBillingTypeId(TransformUtil.transform(fromValue.getBillingType()));
			}
			if (fromValue.getStatus() != null) {
				toValue.setStatusId(TransformUtil.transform(fromValue.getStatus()));
			}
			copyIfNotNull(fromValue, "notes", toValue, "notes");
			
			// TODO: For now not supporting subsclasses such as orderLines or metaDataFields

		} catch (IllegalArgumentException e) {
			throw new SessionInternalError(e);
		} catch (IntrospectionException e) {
			throw new SessionInternalError(e);
		} catch (IllegalAccessException e) {
			throw new SessionInternalError(e);
		} catch (InvocationTargetException e) {
			throw new SessionInternalError(e);
		}

		
		return toValue;
	}

	private static void copyIfNotNull(Object fromBean, String fromPropName, Object toBean, String toPropName) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object valueToCopy = null;
		for (PropertyDescriptor pd : Introspector.getBeanInfo(fromBean.getClass()).getPropertyDescriptors()) {
			if (pd.getName().equals(fromPropName)) {
				valueToCopy = pd.getReadMethod().invoke(fromBean, new Object[] {});
				break;
			}
		}
		
		if (valueToCopy == null) {
			return;
		}
		
		for (PropertyDescriptor pd : Introspector.getBeanInfo(toBean.getClass()).getPropertyDescriptors()) {
			if (pd.getName().equals(toPropName)) {
				valueToCopy = pd.getWriteMethod().invoke(toBean, valueToCopy);
				return;
			}
		}
		
		throw new IllegalArgumentException("No such property '" + toPropName + "' in class '" + toBean.getClass().getName() + "'");
	}
}
