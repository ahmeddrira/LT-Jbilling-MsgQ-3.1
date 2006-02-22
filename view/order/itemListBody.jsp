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

<%@ page language="java"  import="com.sapienter.jbilling.client.util.Constants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


<jbilling:genericList setup="true" type='<%=Constants.LIST_TYPE_ITEM_ORDER%>' method="ejb"/>      

<table class="list">
	<tr class="listH">
		<td><bean:message key="item.prompt.description"/></td>
		<td></td>
		<td><bean:message key="item.prompt.price"/></td>
		<td></td>
		<td></td>
	</tr>		
		<jbilling:genericList>
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
			<jbilling:insertDataRow/>
			<html:form action="/newOrderItem?action=add">
			<input type="hidden" name="itemID" value="<bean:write name="rowID"/>" />
			<td><input class="form_button" type="submit" value="<bean:message key="order.item.add"/>"/></td>
			<td><html:text property="quantity" size="4" value="1" /></td>
			</html:form>
		</tr>
		</jbilling:genericList>
</table>

