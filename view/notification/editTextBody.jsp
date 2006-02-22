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

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<p class="title"><bean:message key="notification.compose.title"/></p>
<p class="instr"><bean:message key="notification.compose.instr"/></p>

<jbilling:getOptions language="true"/>
<html:form action="/notificationMaintain?action=edit&mode=notification">
	<table border="0">
		<tr>
			<td colspan="2">
				<table><tr class="form">
			<td  class="form_prompt"><bean:message key="notification.prompt.use"/></td>
			<td><html:checkbox property="chbx_use_flag"/></td>
			</tr></table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table><tr>
					<td align="right">
						<html:select property="language">
						 	<html:options collection='<%=Constants.PAGE_LANGUAGES%>' 
				                    property="code"
				                    labelProperty="description"/>
					    </html:select>
					</td>
					<td align="left">
						<html:submit styleClass="form_button" property="reload">
							<bean:message key="all.prompt.reload"/>
						</html:submit>						
					</td>
				</tr></table>
			</td>
		</tr>
		<logic:iterate id="line" name="message" property="sections"
			           scope="session" indexId="index">
	    	<tr>
	    		<td><bean:write name="message" 
	    					  property='<%= "sectionNumbers[" + index + "]" %>'/></td>
	    		<td><html:textarea rows="15" cols="50"
	    				property='<%= "sections[" + index + "]" %>'/></td>
	    		
	    	</tr>
	    </logic:iterate>
		<tr><td colspan="2" align="center">
			<html:submit styleClass="form_button">
				<bean:message key="all.prompt.submit"/>
			</html:submit>
		</td></tr>
	</table>
</html:form>