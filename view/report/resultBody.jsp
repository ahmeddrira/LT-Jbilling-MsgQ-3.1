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

<%@ page language="java" import="com.sapienter.jbilling.client.util.Constants, org.apache.struts.Globals"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


<p align="center">
	<%-- The download page won't be served by the struts contoller,
	     therefore, the request will lack the messages. These are 
	     necessary for the report to show the proper column names. --%>
	<bean:define name='<%=Globals.MESSAGES_KEY%>'
	             scope="request"
	             toScope="session"
	             id="sessionMessages"/>
	<html:link page="/report/download.jsp">
		<bean:message key="report.download.prompt"/>
	</html:link> <br/>
	<logic:present name='<%=Constants.SESSION_REPORT_LINK%>' scope="session">
	<html:link page='<%= "/report/form.jsp?back=yes&"  + session.getAttribute(Constants.SESSION_REPORT_LINK) %>'>
		<bean:message key="report.link"/>
	</html:link>
	</logic:present>
</p>

<bean:define id="firstField" value="0" scope="page"/>
<logic:equal name='<%=Constants.SESSION_REPORT_DTO%>'
			 property="idColumn"
			 value="1"
			 scope="session">
	<bean:define id="firstField" value="1" scope="page"/>
	<logic:equal name='<%=Constants.SESSION_REPORT_DTO%>'
				 property="agregated"
				 value="false"
				 scope="session">
		<bean:define id="showId" value="yes" scope="page"/>
	</logic:equal>
</logic:equal>

<table class="list">
	<tr class="listH">
		<logic:iterate  name='<%=Constants.SESSION_REPORT_DTO%>'
							property="fields"
							scope="session"
							id="field"
							offset='<%= (String)pageContext.getAttribute("firstField") %>'>
			<logic:equal name="field"
				             property="isShown"
				             value="1">
   	        	<td><bean:message name="field" property="titleKey"/> </td>
			</logic:equal>
		</logic:iterate>	
		<logic:present name="showId" scope="page">
			<td></td>
		</logic:present>
	</tr>		
	<jbilling:reportExecute>
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
			<logic:present name="showId" scope="page">
				<td class="list">
					<bean:define id="link" name='<%=Constants.SESSION_REPORT_DTO%>' property="link"/>
					<html:link page='<%= (String) pageContext.getAttribute("link") %>'
						       paramId="id"
						       paramName="rowID">
						 <bean:message key="reports.link.prompt"/>
					</html:link>
				</td>
			</logic:present>
		</tr>
	</jbilling:reportExecute>
</table>
