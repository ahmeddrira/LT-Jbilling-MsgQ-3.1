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
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


<table class="list">
	<tr class="listH">
		<td colspan="4"><bean:message key="report.user.list.title"/></td>
	</tr>
	
	<jbilling:reportList mode="user"/>	
	<logic:iterate  name='<%=Constants.SESSION_REPORT_LIST_USER%>'
					scope="session"
					id="userReport">
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
			<td class="list">
				<bean:write name="userReport" property="id"/>-
			</td>

			<td class="list">
				<bean:write name="userReport" property="title"/>
			</td>
			<td class="list">
				<html:link page="/report/form.jsp" paramId="user_report_id" 
						paramName="userReport" paramProperty="id">
					<bean:message key="report.user.load.prompt"/>
				</html:link>
			</td>
			<td class="list">
				<html:link page="/report/delete.jsp" paramId="user_report_id" 
						paramName="userReport" paramProperty="id">
					<bean:message key="report.user.delete.prompt"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>

</table>