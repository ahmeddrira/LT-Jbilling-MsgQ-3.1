<%--
The contents of this file are subject to the Jbilling Public License
 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
--%>

 <%@ page language="java" import="com.sapienter.jbilling.client.util.Constants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>

<table class="info">
	<tr>
		<th class="info" colspan="2"><bean:message key="order.info.title"/></th>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="order.prompt.period"/></td>
		<td class="infodata">
			<bean:write name='<%=Constants.SESSION_ORDER_SUMMARY%>' 
				                    property="periodStr" 
				                    scope="session"/>
		</td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="order.prompt.billingType"/></td>
		<td class="infodata">
			<bean:write name='<%=Constants.SESSION_ORDER_SUMMARY%>' 
				                    property="billingTypeStr" 
				                    scope="session"/>
		</td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="order.prompt.promotion"/></td>
		<td class="infodata">
			<bean:write name='<%=Constants.SESSION_ORDER_SUMMARY%>' 
				                    property="promoCode" 
				                    scope="session"/>
		</td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="order.prompt.activeSince"/></td>
		<td class="infodata">
			<bean:write name='<%=Constants.SESSION_ORDER_SUMMARY%>' 
				                    property="activeSince" 
				                    scope="session"
				                    formatKey="format.date"/>
		</td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="order.prompt.activeUntil"/></td>
		<td class="infodata">
			<bean:write name='<%=Constants.SESSION_ORDER_SUMMARY%>' 
				                    property="activeUntil" 
				                    scope="session"
				                    formatKey="format.date"/>
		</td>
	</tr>
	
</table>