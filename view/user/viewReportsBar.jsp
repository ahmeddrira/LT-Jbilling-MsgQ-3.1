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

<%@ page language="java" import="com.sapienter.jbilling.server.report.ReportDTOEx,com.sapienter.jbilling.client.util.Constants"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
	
<table>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page='<%="/reportTrigger.do?mode=customer&id=" + ReportDTOEx.REPORT_ORDER%>'>
				<bean:message key="user.report.orders"/>
			</html:link>
		</td>
	</tr>
	
	<logic:notPresent name='<%=Constants.SESSION_CUSTOMER_DTO%>' 
		property="customerDto.parentId">
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page='<%="/reportTrigger.do?mode=customer&id=" + ReportDTOEx.REPORT_INVOICE%>'>
				<bean:message key="user.report.invoices"/>
			</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page='<%="/reportTrigger.do?mode=customer&id=" + ReportDTOEx.REPORT_PAYMENT%>'>
				<bean:message key="user.report.payments"/>
			</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page='<%="/reportTrigger.do?mode=customer&id=" + ReportDTOEx.REPORT_REFUND%>'>
				<bean:message key="user.report.refunds"/>
			</html:link>
		</td>
	</tr>
	</logic:notPresent>
	
	<%-- not a report, this is the list of children accounts --%>
	<logic:present name='<%=Constants.SESSION_CUSTOMER_DTO%>' property="customerDto.isParent">
		<logic:equal name='<%=Constants.SESSION_CUSTOMER_DTO%>' 
			property="customerDto.isParent" value="1">
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page="/user/listSubAccounts.jsp">
				<bean:message key="user.report.sub_accounts"/>
			</html:link>
		</td>
		</logic:equal>
	</logic:present>
</table>
