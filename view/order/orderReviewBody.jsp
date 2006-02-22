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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


<html:errors/>
<html:form action="/reviewOrder" >
	
	<html:hidden property="action" value="process"></html:hidden>
	<table class="list">
		<tr class="listH">
			<td><bean:message key="item.prompt.description"/></td>
			<td><bean:message key="item.list.quantity"/></td>
			<td><bean:message key="item.prompt.price"/></td>
			<td><bean:message key="order.review.column.total"/></td>
	  	</tr>
	  	
		<logic:iterate id="thisLine" name="orderDTOForm" property="orderLines">
			<bean:define id="itemId" name="thisLine" property="key"/>
			<logic:equal name="thisLine" property="value.editable" value="true">
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
				<td>
				<html:text property='<%= "orderLine(" + itemId + ").description" %>' size="40"/>	
				</td>
				<td>
				<logic:notPresent name="thisLine" 
				              property="value.item.percentage">
				    <html:text property='<%= "orderLine(" + itemId + ").quantity" %>' size="4"/>
                </logic:notPresent>				
				</td>
				<td>
				<logic:equal name="thisLine" 
				              property="value.item.priceManual"
				              value="1">
				    <html:text property='<%= "orderLine(" + itemId + ").priceStr" %>' size="8"/>
				</logic:equal>
				<logic:notEqual name="thisLine" 
				              property="value.item.priceManual"
				              value="1">
				    <logic:notPresent name="thisLine" 
				                 property="value.item.percentage">
				        <bean:write name="thisLine" property="value.price" 
				                    formatKey="format.money"/>
				    </logic:notPresent>
				    <logic:present name="thisLine" 
				                 property="value.item.percentage">
				        <bean:write name="thisLine" property="value.price" 
				                    formatKey="format.percentage"/>
				    </logic:present>

				</logic:notEqual>
				</td>
				<td>
				<bean:write name="thisLine" property="value.amount" formatKey="format.money"/>
				</td>	
			</tr>
			</logic:equal>
		</logic:iterate>
		
		<logic:iterate id="thisLine" name="orderDTOForm" property="orderLines">
			<logic:notEqual name="thisLine" property="value.editable" value="true">
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
				<td>
				<bean:write name="thisLine" property="value.description"/>	
				</td>
				<td>
				<bean:write name="thisLine" property="value.quantity"/>
				</td>
				<td>
				<bean:write name="thisLine" property="value.price"/>
				</td>
				<td>
				<bean:write name="thisLine" property="value.amount" formatKey="format.money"/>
				</td>	
			</tr>
			</logic:notEqual>
		</logic:iterate>
	
		<br/>
	  	
		<tr class="listH">
			<td colspan="2" align="right"><bean:message key="order.summary.total"/></td>
			<td colspan="2" align="right">
				<bean:write name='<%=Constants.SESSION_ORDER_SUMMARY%>' 
				            property="currencySymbol" 
				            scope="session"
				            filter="false"/>
				<bean:write name='<%=Constants.SESSION_ORDER_SUMMARY%>' 
				            property="orderTotal" 
				            scope="session"
				            formatKey="format.money"/>
	        </td>
		</tr>
		<tr>
			<td align="center">
				<html:submit styleClass="form_button" property="recalc" ><bean:message key="order.review.recalc"/></html:submit>
			</td>
			<td align="center">
				<html:submit styleClass="form_button" property="commit"><bean:message key="order.review.commit"/></html:submit>
			</td>
			
		</tr>	  	
	</table>	
</html:form>
