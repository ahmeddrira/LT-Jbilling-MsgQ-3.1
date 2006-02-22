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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/session-1.0" prefix="sess" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<logic:present name='<%=Constants.SESSION_USER_ID%>' 
	                         scope="session">
<%-- These links make sense only if this is not self-edition --%>
<logic:notEqual name='<%=Constants.SESSION_USER_ID%>' 
                scope="session" 
                value='<%=((Integer) session.getAttribute(Constants.SESSION_LOGGED_USER_ID)).toString() %>' >
<jbilling:permission permission='<%=Constants.P_USER_EDIT_LINKS%>'>	                         
<table>
	<tr>
		<td class="leftMenuOption">
		<html:link styleClass="leftMenu" page="/userMaintain.do?action=delete">
		   <bean:message key="user.edit.delete"/>
		</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
		<html:link styleClass="leftMenu" page="/contactEdit.do?action=setup">
		   <bean:message key="user.edit.contact"/>
		</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
		<html:link styleClass="leftMenu" page="/creditCardMaintain.do?action=setup&mode=creditCard">
		   <bean:message key="user.edit.creditCard"/>
		</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
		<html:link styleClass="leftMenu" page="/achMaintain.do?action=setup&mode=ach">
		   <bean:message key="user.edit.ach"/>
		</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
		<html:link styleClass="leftMenu" page="/item/userPriceCreate.jsp?create=yes">
		   <bean:message key="user.edit.price"/>
		</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
		<html:link styleClass="leftMenu" page="/item/userPriceList.jsp">
		   <bean:message key="user.edit.listPrice"/>
		</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
		<html:link styleClass="leftMenu" page="/userMaintain.do?action=order">
		   <bean:message key="user.edit.order"/>
		</html:link>
		</td>
	</tr>
	
</table>
</jbilling:permission>
</logic:notEqual>
</logic:present>