package com.sapienter.jbilling.server.util.audit;

public class EventLoggerSQL {
	
	public static String searchLog = "SELECT old_num from event_log" +
    			" WHERE module_id = " + EventLogger.MODULE_WEBSERVICES +
    			" AND message_id = " + EventLogger.USER_TRANSITIONS_LIST +
    			" AND entity_id = ? ORDER BY id DESC";

}