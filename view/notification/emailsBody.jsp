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


<p class="title"><bean:message key="notification.emails.title"/></p>
<p class="instr"><bean:message key="notification.emails.instr"/></p>

<html:form action="/notificationEmails">
	<table class="form">
    	<tr class="form">
    		<td class="form_prompt"> <bean:message key="notification.emails.separator"/> </td>
    		<td align="left"><html:text property="separator" size="3"/></td>
		</tr>
		<tr>
    		<td colspan="2"><html:textarea rows="15" cols="50"
    				property="content" readonly="true"/></td>
    	</tr>
		<tr><td colspan="2"  class="form_button">
			<html:submit styleClass="form_button">
				<bean:message key="notification.emails.all.button"/>
			</html:submit>
		</td></tr>
	</table>
</html:form>