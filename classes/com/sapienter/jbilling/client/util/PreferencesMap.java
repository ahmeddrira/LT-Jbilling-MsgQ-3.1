/**
 * 
 */
package com.sapienter.jbilling.client.util;

import java.util.Map;

public class PreferencesMap {
	private final Map<Integer, String> myMap;

	public PreferencesMap(Map<Integer, String> map){
		myMap = map;
	}
	
	public String getString(Integer key){
		String result = myMap.get(key);
		return result == null ? "" : result;
	}
	
	public Integer getInteger(Integer key){
		String result = myMap.get(key);
		return Integer.valueOf(result);
	}
	
	public boolean getBoolean(Integer key){
		String result = myMap.get(key);
		return "1".equals(result);
	}
	
}