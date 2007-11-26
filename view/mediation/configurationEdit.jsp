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

<p class="title"><bean:message key="mediation.configuration.title"/></p>
<p class="instr">
   <bean:message key="mediation.configuration.instr"/>
</p>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<html:form action="/mediation/configuration.do?action=edit" >
  <table class="form">
  	<tr class="form">
  		<td class="form_prompt"><bean:message key="mediation.configuration.prompt.id"/></td>
  		<td class="form_prompt"><bean:message key="mediation.configuration.prompt.date"/></td>
  		<td class="form_prompt"><bean:message key="mediation.configuration.prompt.name"/></td>
  		<td class="form_prompt"><bean:message key="mediation.configuration.prompt.order"/></td>
  		<td class="form_prompt"><bean:message key="mediation.configuration.prompt.task"/></td>
  		<td></td>
  	</tr>
  	
  	<logic:iterate id="oneConfig" name="medConfiguration" property="configurations"
			           scope="session" indexId="index">
	   <tr class="form">
	    	<td>
	    		<bean:write name="oneConfig" property="id" />
	    	</td>
	    	<td>
			    <bean:write name="oneConfig"
                        property="createDatetime"
                        formatKey="format.timestamp"/>
	    	</td>
	    	<td>
	    		<html:text property='<%= "configurations[" + index + "].name" %>' size="20"/>
	    	</td>
	    	<td>
	    		<html:text property='<%= "configurations[" + index + "].orderValue" %>' size="2"/>
	    	</td>
	    	<td>
	    		<html:text property='<%= "configurations[" + index + "].pluggableTask.id" %>' size="3"/>
	    	</td>
	    	<td>
		    	<html:link page="/mediation/configuration.do?action=delete" paramName="oneConfig"
	    			paramProperty="id" paramId="id">
					<bean:message key="all.prompt.delete"/>
				</html:link>
	    	</td>
    	</tr>
		
		
    </logic:iterate>

	<tr><td colspan="6" align="center">
		<html:link page="/mediation/configuration.do?action=add">
			<bean:message key="mediation.configuration.add"/>
		</html:link>
	</td></tr>
	<tr>
   	<td class="form_button" colspan="6" align="center">
		<html:submit styleClass="form_button">
			<bean:message key="all.prompt.submit"/>
		</html:submit>
	</td>
	</tr>
    
  </table>
</html:form>
  