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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<p class="title"><bean:message key="user.changePassword.title"/></p>
<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>


<html:form action="/changePassword" focus="password">
<table class="form">

  <tr class="form">
    <td class="form_prompt">
      <bean:message key="prompt.password"/>
    </td>
    <td>
      <html:password property="password" size="20" maxlength="20"/>
    </td>
  </tr>

  <tr class="form">
    <td class="form_prompt">
      <bean:message key="user.prompt.verifyPassword"/>
    </td>
    <td>
      <html:password property="verifyPassword" size="20" maxlength="20"/>
    </td>
  </tr>

  <tr>
    <td colspan="2" class="form_button">
      <html:submit styleClass="form_button" property="submit" value="Submit"/>
    </td>
  </tr>

</table>

</html:form>
