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
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p align="center">
	<html:link page='<%= "/report/form.jsp?back=yes&"  + session.getAttribute(Constants.SESSION_REPORT_LINK) %>'>
		<bean:message key="report.link"/>
	</html:link>
</p>
<logic:present parameter="user_report_id">
	<p align="center">
		<bean:message key="report.user.delete.confirm" 
				arg0='<%= request.getParameter(Constants.REQUEST_USER_REPORT_ID) %>'/> <br/>
		<html:link page='<%= "/report/delete.jsp?confirm=yes&rid=" + request.getParameter(Constants.REQUEST_USER_REPORT_ID) %>' >
			<bean:message key="all.prompt.yes"/>
		</html:link> <br/>
		<html:link page="/report/delete.jsp?confirm=no">
			<bean:message key="all.prompt.no"/>
		</html:link> <br/>
	</p> 
</logic:present>

<logic:present parameter="confirm">
	<logic:equal parameter="confirm"
		         value="yes">
		<jbilling:reportDelete reportId='<%= Integer.valueOf(request.getParameter("rid")) %>'/>
    	<html:errors/>
    	<logic:messagesNotPresent>
    		<p><bean:message key="report.user.delete.done"/></p>
    	</logic:messagesNotPresent>
   </logic:equal>
   <logic:equal parameter="confirm"
		         value="no">
	   <p><bean:message key="report.user.delete.no"/></p>
   </logic:equal>
</logic:present>
