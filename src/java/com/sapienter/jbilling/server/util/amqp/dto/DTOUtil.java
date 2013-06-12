package com.sapienter.jbilling.server.util.amqp.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sapienter.jbilling.server.util.amqp.types.AccountStatus;

public class DTOUtil {
	private DTOUtil() {
	}
	
	private static Map<Integer, AccountStatus> accountStatusMap;
	
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
	}
	
	public static AccountStatus fromJBValue(Integer jbValue) {
		return accountStatusMap.get(jbValue);
	}
	
	public static Integer toJBValue(AccountStatus fromValue) {
		for (Entry<Integer, AccountStatus> entry : accountStatusMap.entrySet()) {
			if (entry.getValue() == fromValue) {
				return entry.getKey();
			}
		}
		return null;
	}

}
