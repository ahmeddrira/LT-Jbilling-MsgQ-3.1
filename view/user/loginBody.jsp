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

<p class="title"><bean:message key="login.title"/></p>
<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>


<html:form action="/verifyPassword" focus="userName">
<table class="form">

  <tr class="form">
    <td class="form_prompt">
      <bean:message key="prompt.username"/>
    </td>
    <td>
      <html:text property="userName" size="20" maxlength="20"/>
    </td>
  </tr>

  <tr class="form">
    <td class="form_prompt">
      <bean:message key="prompt.password"/>
    </td>
    <td>
      <html:password property="password" size="20" maxlength="20"/>
    </td>
  </tr>

  <%
    // if the entityId is present, put it in a hidden field, 
    // otherwise show a field for the user to enter it.
    String entityId = request.getParameter("entityId");
	Integer asInt = null;
	try { asInt = Integer.valueOf(entityId); }
	catch (Exception e) {}

    if (entityId == null || entityId.length() == 0 || asInt == null) {
  %>
	  <tr class="form">
		<td class="form_prompt">
		  <bean:message key="prompt.entityId"/>
		</td>
		<td>
		  <html:text property="entityId" size="4" maxlength="4"/>
		</td>
	  </tr>
  <% } else { %>
  	<html:hidden property="entityId" value='<%=request.getParameter("entityId") %>' />
  <% } %>
  
  <logic:present parameter="internalKey">
  	<html:hidden property="internalKey" value='<%=request.getParameter("internalKey") %>' />
  </logic:present>  

  <tr>
    <td colspan="2" class="form_button">
      <html:submit styleClass="form_button" property="submit" value="Submit"/>
    </td>
  </tr>

</table>

</html:form>


<%-- Experiment for Container base authentication 
<form method="POST" action="j_security_check">
	<input type="text" name="j_username">
	<input type="password" name="j_password">
	<input type="submit"/>
</form>
--%>

