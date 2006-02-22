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

<logic:notEqual name='<%=Constants.SESSION_USER_DTO%>'
					 property="mainRoleId"
					 scope="session"
					 value='<%=Constants.TYPE_CUSTOMER.toString()%>'>

<table>
	<jbilling:permission permission='<%=Constants.P_USER_CREATE%>'>
	<tr>
		<td class="leftMenuOption">
		<html:link styleClass="leftMenu" page="/user/create.jsp?customer=yes&create=yes">
		   <bean:message key="user.create.customer.link"/>
		</html:link>
		</td>
	</tr>
	</jbilling:permission>
	<tr>
		<td class="leftMenuOption">
		<html:link styleClass="leftMenu" page="/user/list.jsp">
		   <bean:message key="user.list.customer.link"/>
		</html:link>
		</td>
	</tr>
</table>

</logic:notEqual>

<logic:equal name='<%=Constants.SESSION_USER_DTO%>'
					 property="mainRoleId"
					 scope="session"
					 value='<%=Constants.TYPE_CUSTOMER.toString()%>'>
  <table>
	<tr>
		<td class="leftMenuOption">
		<html:link styleClass="leftMenu" page="/paymentMaintain.do?action=last_invoice">
		   <bean:message key="payment.inital.link"/>
		</html:link>
		</td>
	</tr>
	<tr>
		<td class="leftMenuOption">
		<html:link styleClass="leftMenu" page="/invoiceMaintain.do?latest=yes">
		   <bean:message key="invoice.initial.link"/>
		</html:link>
		</td>
	</tr>
	
  </table>
					
</logic:equal>