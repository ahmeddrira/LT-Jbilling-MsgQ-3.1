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

<p class="title"><bean:message key="task.parameter.title"/></p>
<p class="instr">
	<bean:message key="task.parameter.instr"/>
	<logic:equal parameter="type" value="notification">
	   <br/>
	   <bean:message key="all.prompt.help" />
	   <jbilling:help page="notifications" anchor="emailParameters">
		   <bean:message key="all.prompt.here" />
	   </jbilling:help>
	</logic:equal>
</p>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>


<html:form action="/parameterMaintain?action=edit&mode=parameter">
	<table class="form">
		<logic:iterate id="aValue" name="parameter" property="value"
			           scope="session" indexId="index">
	    	<tr class="form">
	    		<td class="form_prompt"><bean:write name="parameter" 
	    					  property='<%= "name[" + index + "]" %>'/></td>
	    		<td><html:text 
	    				property='<%= "value[" + index + "]" %>'/></td>
	    		
	    	</tr>
	    </logic:iterate>
		<tr><td colspan="2"  class="form_button">
			<html:submit styleClass="form_button">
				<bean:message key="all.prompt.submit"/>
			</html:submit>
		</td></tr>
	</table>
</html:form>