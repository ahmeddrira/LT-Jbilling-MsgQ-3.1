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

<%@ page language="java" import="com.sapienter.jbilling.client.util.Constants, com.sapienter.jbilling.server.report.ReportDTOEx"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


<html:errors/>

<p class="title"><bean:message key="report.list.title"/></p>
<p class="instr"><bean:message key="report.list.instr"/></p>

<table class="list">
	<jbilling:reportList mode="entity"/>	
	<logic:iterate  name='<%=Constants.SESSION_REPORT_LIST%>'
					scope="session"
					id="report">
		<jbilling:permission typeId="5" foreignId='<%= ((ReportDTOEx)pageContext.getAttribute("report")).getId() %>'>
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
			<td>
				<bean:message name="report" property="titleKey"/>
			</td>
			<td>
				<html:link page="/report/form.jsp" paramId="report_id" 
						paramName="report" paramProperty="id">
					<bean:message key="report.list.go"/>
				</html:link>
			</td>
		</tr>
		</jbilling:permission>
	</logic:iterate>
</table>