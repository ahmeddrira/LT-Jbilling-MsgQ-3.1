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

<%@ page language="java" import="com.sapienter.jbilling.server.report.ReportDTOEx"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page="/payout.do?action=setup">
				<bean:message key="partner.link.manualPayout"/>
			</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page="/user/payoutList.jsp">
				<bean:message key="partner.link.payouts"/>			
			</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page="/user/list.jsp?forpartner=yes">
				<bean:message key="partner.link.customers"/>
			</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page="/reportList.do?type=7">
				<bean:message key="partner.link.report"/>
			</html:link>
		</td>
	</tr>
</table>