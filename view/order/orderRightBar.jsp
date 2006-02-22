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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


<table class="order_summary">
	<tr class="order_summary_title">
		<td colspan="3"><bean:message key="order.summary.title"/></td>
	</tr>
	<logic:equal name='<%=Constants.SESSION_ORDER_SUMMARY%>'
			property="empty" scope="session" value="false">
		<tr class="order_summary_lines">
			<td colspan="3"><bean:message key="order.summary.lines"/>: 
			<bean:write name='<%=Constants.SESSION_ORDER_SUMMARY%>' property="numberOfLines"/></td>
		</tr>

		<logic:iterate id="orderLine" name='<%=Constants.SESSION_ORDER_SUMMARY%>' property="orderLines" >
			<logic:equal name="orderLine" property="typeId" value="1" >
				<c:choose>
					<c:when test="${colorFlag == 1}">
						<tr class="listB">
						<c:remove var="colorFlag"/>
					</c:when>
					<c:otherwise>
						<tr class="listA">
						<c:set var="colorFlag" value="1"/>
					</c:otherwise>
				</c:choose>
				<td><html:link page="/newOrderItem.do?action=delete&quantity=1" paramId="itemID" paramName="orderLine" paramProperty="itemId">
					<bean:message key="order.summary.deleteItem"/>
					</html:link>
				</td>
				<td><bean:write name="orderLine" property="quantity"/></td>
				<td><bean:write name="orderLine" property="description"/></td>
			</tr>
			</logic:equal>
		</logic:iterate>	

	</logic:equal> 
	<logic:notEqual name='<%=Constants.SESSION_ORDER_SUMMARY%>'
			property="empty" scope="session" value="false">
		<tr class="order_summary_lines">
			<td><bean:message key="order.summary.nolines"/></td>
		</tr>
	</logic:notEqual>
</table>
