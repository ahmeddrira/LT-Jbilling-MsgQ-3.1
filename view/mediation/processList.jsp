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

<p class="title"> <bean:message key="mediation.process.list.title"/> </p>
<p class="instr"> <bean:message key="mediation.process.list.instr"/> </p>

<logic:present name="mediation_process_list">
<table class="list">
	<tr class="listH">
		<td><bean:message key="mediation.process.list.id"/></td>
		<td><bean:message key="mediation.process.list.mediation_id"/></td>
		<td><bean:message key="mediation.process.list.start_datetime"/></td>
		<td><bean:message key="mediation.process.list.end_datetime"/></td>
		<td><bean:message key="mediation.process.list.orders"/></td>
	</tr>		
  	<logic:iterate id="oneProcess" name="mediation_process_list" 
		           scope="session" indexId="index">
	
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
		    <bean:write name="oneProcess" 
                        property="id"/>
		</td>
		<td class="list">
		    <bean:write name="oneProcess" 
                        property="configuration.id"/>
		</td>
		<td class="list">
		    <bean:write name="oneProcess"
                        property="startDatetime"
                        formatKey="format.timestamp"/>
		</td>
		<td class="list">
		    <bean:write name="oneProcess"
                        property="endDatetime"
                        formatKey="format.timestamp"/>
		</td>
		<td class="list">
		    <bean:write name="oneProcess"
                        property="ordersAffected"/>
		</td>

		</tr>
	</logic:iterate>
</table>
</logic:present>

<logic:notPresent name="mediation_process_list">
   <bean:message key="mediation.process.list.empty"/>
</logic:notPresent>