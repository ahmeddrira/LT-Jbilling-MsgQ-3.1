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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/session-1.0" prefix="sess" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt" %>

<%-- For convenience only ... --%>
<bean:define id="dto" name='<%=Constants.SESSION_PAYMENT_DTO%>'
		         scope="session"/>

<%-- go through each linked invoice and display a link to it --%>
<logic:notEmpty name="dto" property="paymentMaps">

<p class="title"><bean:message key="payment.prompt.invoices"/></p>

<table class="list">
		<%-- print the headers of the columns --%>
		<tr class="listH">
			<td><bean:message key="invoice.id.prompt"/></td>
			<td><bean:message key="payment.link.date"/></td>
			<td><bean:message key="payment.link.amount"/></td>
			<td></td>
		</tr>
		<%-- now each invoice row --%>
		<logic:iterate name="dto"
					   property="paymentMaps"
					   scope="page"
					   id="map"
					   indexId="index">
			<c:choose>
				<c:when test="${flag == 1}">
					<tr class="listB">
					<c:remove var="flag"/>
				</c:when>
				<c:otherwise>
					<tr class="listA">
					<c:set var="flag" value="1"/>
				</c:otherwise>
		    </c:choose>
				<td class="list" align="right">
					<html:link action="invoiceMaintain" paramId="id" 
							   paramName="map"
							   paramProperty="invoiceId">
						<bean:write name="map" property="invoiceId"/>
					</html:link>
				</td>
				<td class="list">
					<bean:write name="map" property="createDateTime"
						        formatKey="format.timestamp"/>
				</td>
				<td class="list" align="right">
					<bean:write name="map" property="amount"
						        formatKey="format.money"/>
				</td>
				<td class="list" align="right">
				    <jbilling:permission permission='<%=Constants.P_PAYMENT_MODIFY%>'>
					<html:link page="/paymentMaintain.do?action=unlink" paramId="mapId" 
							   paramName="map"
							   paramProperty="id">
						<bean:message key="payment.link.remove"/>
					</html:link>
					</jbilling:permission>
				</td>
			</tr>	
		</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="dto" property="paymentMaps">
	<p class="title"><bean:message key="payment.prompt.noInvoices"/></p>
</logic:empty>
