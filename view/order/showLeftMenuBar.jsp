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

<jbilling:permission permission='<%=Constants.P_ORDER_LEFT_OPTIONS%>'>	
<table>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page="/processMaintain.do?action=newInvoice">
				<bean:message key="order.newInvoice"/>
			</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page="/order/applyInvoice.jsp">
				<bean:message key="order.applyToInvoice"/>
			</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page="/reviewOrder.do?action=read" paramId="id" 
					   paramName='<%=Constants.SESSION_ORDER_DTO%>' 
					   paramProperty="id">
				<bean:message key="order.prompt.edit"/>			
			</html:link>
		</td>
	</tr>
	<logic:notEqual name='<%=Constants.SESSION_ORDER_DTO%>' 
		            property="statusId"
		            value='<%=Constants.ORDER_STATUS_ACTIVE.toString()%>' >
		<tr>
			<td class="leftMenuOption">
				<html:link styleClass="leftMenu" page='<%="/order/confirmStatus.jsp?statusId=" + Constants.ORDER_STATUS_ACTIVE%>' >
					<bean:message key="order.prompt.activate"/>
				</html:link>
			</td>
		</tr>
	</logic:notEqual>
	<logic:notEqual name='<%=Constants.SESSION_ORDER_DTO%>' 
		            property="statusId"
		            value='<%=Constants.ORDER_STATUS_SUSPENDED.toString()%>' >
		<tr>
			<td class="leftMenuOption">
				<html:link styleClass="leftMenu" page='<%="/order/confirmStatus.jsp?statusId=" + Constants.ORDER_STATUS_SUSPENDED%>' >
					<bean:message key="order.prompt.suspend"/>
				</html:link>
			</td>
		</tr>
	</logic:notEqual>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page="/order/confirmStatus.jsp?statusId=delete">
				<bean:message key="order.prompt.delete"/>
			</html:link>
		</td>
	</tr>
</table>
</jbilling:permission>