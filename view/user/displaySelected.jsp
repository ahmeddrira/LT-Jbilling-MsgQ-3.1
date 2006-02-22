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

<logic:present name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>'
		           scope="session">
<table class="info" border="0">
	<tr>
	     <th class="info" colspan="2"><bean:message key="user.selected.title"/></th>
	</tr>
	<tr class="infoA">
 	    <td class="infoprompt"><bean:message key="user.prompt.id" /></td>
 	    <td class="infodata">
	         <bean:write name='<%=Constants.SESSION_USER_ID%>' 
		                             scope="session"/>
		</td>
	</tr>
	<tr class="infoB">
 	    <td class="infoprompt"><bean:message key="user.prompt.name" /></td>
	    <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' 
		                                 property="firstName"
		                                 scope="session"/>&nbsp
	            <bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' 
		                                property="lastName"
		                                scope="session"/>
		</td>
	</tr>
	<tr class="infoA">
 	    <td class="infoprompt"><bean:message key="contact.prompt.organizationName" /></td>
	    <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_CONTACT_DTO%>' 
		                                 property="organizationName"
		                                 scope="session"/>
		</td>
	</tr>
</table>
</logic:present>