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

<%@ page language="java" import="com.sapienter.jbilling.client.util.Constants, com.sapienter.jbilling.server.user.MenuOption"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:iterate name='<%=Constants.SESSION_USER_DTO%>'
	                       property="menu.options"
	                       scope="session"
	                       id="option">
	<bean:define id="flag" value="no"/>
	<logic:equal name="option" property="selected" value="true">
		<td class="menuSelected">
		<bean:define id="flag" value="yes"/>
	</logic:equal>
	<logic:equal name="flag" value="no">
		<td class="menu">
	</logic:equal>
		<html:link page='<%= ((MenuOption) pageContext.getAttribute("option")).getLink() %>'
				paramId="menu_option" paramName="option" paramProperty="id">
			<bean:write name="option" property="display"/>
		</html:link>
	</td>
</logic:iterate>
