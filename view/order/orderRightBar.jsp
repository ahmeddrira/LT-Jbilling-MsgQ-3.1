<%--
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
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
