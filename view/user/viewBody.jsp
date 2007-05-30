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

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<table class="info" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="info" colspan="2"><bean:message key="user.info.title"/></th>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="user.prompt.id"/></td>
		<td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="userId" scope="session"/></td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="user.prompt.username"/></td>
		<td  class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="userName" scope="session"/></td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="user.prompt.type"/></td>
		<td  class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="mainRoleStr" scope="session"/></td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="user.prompt.language"/></td>
		<td  class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="languageStr" scope="session"/></td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="user.prompt.status"/></td>
		<td  class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="statusStr" scope="session"/></td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="currency.external.prompt.name"/></td>
		<td  class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="currencyName" scope="session"/></td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="user.prompt.lastLogin"/></td>
		<td  class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="lastLogin" scope="session" formatKey="format.timestamp"/></td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="user.prompt.subscriber_status"/></td>
		<td  class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="subscriptionStatusStr" scope="session"/></td>
	</tr>
	
	<logic:notEqual name='<%=Constants.SESSION_USER_DTO%>'
					 property="mainRoleId"
					 scope="session"
					 value='<%=Constants.TYPE_CUSTOMER.toString()%>'>
	<logic:present name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="customerDto" scope="session">
	<logic:present name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="customerDto.parentId" scope="session">
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="user.prompt.parent"/></td>
		<td class="infodata">
			<html:link page="/userMaintain.do?action=setup" 
				       paramId="id" 
					   paramName='<%=Constants.SESSION_CUSTOMER_DTO%>'
					   paramProperty="customerDto.parentId">		
			    <bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			         property="customerDto.parentId" scope="session"/></td>
	        </html:link>
	     </td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="user.prompt.invoiceChild"/></td>
		<td class="infodata">
		 	<logic:equal name='<%=Constants.SESSION_CUSTOMER_DTO%>'
			 		property="customerDto.invoiceChild" 
			 		value="1">
			 		<bean:message key="all.prompt.yes"/>
		 	</logic:equal>
		 	<logic:equal name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			 		property="customerDto.invoiceChild" 
			 		value="0">
			 		<bean:message key="all.prompt.no"/>
		 	</logic:equal>
		</td>
	</tr>
	</logic:present>
	</logic:present>
	</logic:notEqual>
	
	<logic:present name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="customerDto" scope="session">
	<logic:present name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="customerDto.totalSubAccounts" scope="session">
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="user.prompt.totalSubAccounts"/></td>
		<td  class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="customerDto.totalSubAccounts" scope="session"/></td>
	</tr>
	</logic:present>
	</logic:present>
	
	<jbilling:permission permission='<%=Constants.P_USER_EDIT_LINKS%>'>
	<tr>
		<td class="infocommands" colspan="2">
			<html:link page="/user/edit.jsp">
				<bean:message key="user.edit.link"/>
			</html:link>
		</td>
	</tr>
	</jbilling:permission>

</table>