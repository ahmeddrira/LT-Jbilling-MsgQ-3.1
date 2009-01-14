package com.sapienter.jbilling.server.list.db;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class ListFieldDAS extends AbstractDAS<ListFieldDTO> {
	
	public static Collection<ListFieldDTO> orderFields(Collection<ListFieldDTO> fields) {
		LinkedList<ListFieldDTO> orderFields = new LinkedList<ListFieldDTO>(fields);
		Collections.sort(orderFields);
		return orderFields;
	}

}
