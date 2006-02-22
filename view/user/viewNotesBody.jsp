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

<logic:notPresent name='<%=Constants.SESSION_CUSTOMER_DTO%>' 
		property="customerDto.parentId">
<table class="info" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="info"><bean:message key="user.notes.info.title"/></th>
	</tr>
	<tr class="infoA">
		<td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>'  
			property="customerDto.notes" scope="session" filter="false"/></td>
	</tr>
	<jbilling:permission permission='<%=Constants.P_USER_EDIT_LINKS%>'>
	<tr>
		<td class="infocommands">
			<html:link page="/userNotesEdit.do?action=setup">
				<bean:message key="user.edit.notes.link"/>
			</html:link>
		</td>
	</tr>
	</jbilling:permission>
</table>
</logic:notPresent>