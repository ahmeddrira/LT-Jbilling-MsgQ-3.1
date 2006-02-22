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

 <table class="info">
 	 <tr>
 	 	<th class="info" colspan="2">
 	 		<bean:message key="contact.info.title"/>
 	 	</th>
 	 </tr>
	 <tr class="infoA">
		 <td class="infoprompt"><bean:message key="contact.prompt.organizationName"/></td>
		 <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="organizationName"  /></td>
	 </tr>
	 <tr class="infoB">
		 <td class="infoprompt"><bean:message key="contact.prompt.firstName"/></td>
		 <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="firstName"  /></td>
	 </tr>
	 <tr class="infoA">
		 <td class="infoprompt"><bean:message key="contact.prompt.lastName"/></td>
		 <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="lastName"  /></td>
	 </tr>
	 <tr class="infoB">
		 <td class="infoprompt"><bean:message key="contact.prompt.phoneNumber"/></td>
		 <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="phoneAreaCode" /> - <bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="phoneNumber"  /></td>
	 </tr>
	 <tr class="infoA">
		 <td class="infoprompt"><bean:message key="contact.prompt.email"/></td>
		 <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="email"  /></td>
	 </tr>
	 <tr class="infoB">
		 <td class="infoprompt"><bean:message key="contact.prompt.address1"/></td>
		 <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="address1"  /></td>
	 </tr>
	 <tr class="infoA">
		 <td class="infoprompt"><bean:message key="contact.prompt.address2"/></td>
		 <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="address2"  /></td>
	 </tr>
	 <tr class="infoB">
		 <td class="infoprompt"><bean:message key="contact.prompt.city"/></td>
		 <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="city"  /></td>
	 </tr>
	 <tr class="infoA">
		 <td class="infoprompt"><bean:message key="contact.prompt.stateProvince"/></td>
		 <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="stateProvince"  /></td>
	 </tr>
	 <tr class="infoB">
		 <td class="infoprompt"><bean:message key="contact.prompt.postalCode"/></td>
		 <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="postalCode"  /></td>
	 </tr>
	 <tr class="infoA">
		 <td class="infoprompt"><bean:message key="contact.prompt.countryCode"/></td>
		 <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' property="countryCode"  /></td>
	 </tr>
	 
	 <jbilling:permission permission='<%=Constants.P_USER_EDIT_LINKS%>'>
	 <tr>
		<td class="infocommands" colspan="2">
			<html:link action="/contactEdit.do?action=setup"
				       paramId="userId"
				       paramName='<%=Constants.SESSION_CUSTOMER_DTO%>'
				       paramProperty="userId">
				<bean:message key="contact.edit.link"/>
			</html:link>
		</td>
	</tr>
	</jbilling:permission>
	 
</table>
