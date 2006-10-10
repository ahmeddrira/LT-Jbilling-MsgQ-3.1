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

<p class="title"><bean:message key="system.task.title"/></p>
<p class="instr">
   <bean:message key="system.task.instr"/>
</p>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<jbilling:getOptions taskClasses="true"/>
<html:form action="/task?action=edit" >
  <table class="form">
  	<tr class="form">
  		<td class="form_prompt"><bean:message key="system.task.prompt.id"/></td>
  		<td class="form_prompt"><bean:message key="system.task.prompt.type"/></td>
  		<td class="form_prompt"><bean:message key="system.task.prompt.processing_order"/></td>
  		<td></td>
  	</tr>
  	
  	<logic:iterate id="oneTask" name="task" property="tasks"
			           scope="session" indexId="index">
	   <tr class="form">
	    	<td>
	    		<bean:write name="oneTask" property="id" />
	    	</td>
	    	<td>
	    		<html:select property='<%= "tasks[" + index + "].typeId" %>'>
			   		<html:options collection='<%=Constants.PAGE_TASK_CLASSES%>' 
				 		property="code"
						labelProperty="description"	/>
		  		</html:select>
	    	</td>
	    	<td>
	    		<html:text property='<%= "tasks[" + index + "].processingOrder" %>' size="2"/>
	    	</td>
	    	<td>
		    	<html:link page="/task.do?action=delete" paramName="oneTask"
	    			paramProperty="id" paramId="id">
					<bean:message key="all.prompt.delete"/>
				</html:link>
	    	</td>
    	</tr>
		<logic:notEmpty name="oneTask" property="parameters">
		<tr><td colspan="4" align="center">
		
        <table class="form">				           
	  	<logic:iterate id="oneParameter" name="oneTask" property="parameters"
	  				indexId="subIndex">
	      <tr class="form">
	    	<td>
	    		<html:text property='<%= "tasks[" + index + "].parameters[" + subIndex + "].name" %>'/>
	    	</td>
	    	<td>
	    		<html:text property='<%= "tasks[" + index + "].parameters[" + subIndex + "].value" %>'/>
	    	</td>
			<td>
		    	<html:link page="/task.do?action=deleteParameter" paramName="oneParameter"
	    			paramProperty="id" paramId="id">
					<bean:message key="all.prompt.delete"/>
				</html:link>
	    	</td>	    	
     	  </tr>
		</logic:iterate>
   	    </table>
		</td></tr>
		</logic:notEmpty>
		
		<tr><td colspan="4" align="center">
		    	<html:link page="/task.do?action=addParameter" paramName="oneTask"
	    			paramProperty="id" paramId="id">
					<bean:message key="system.task.addParameter"/>
				</html:link>
    	</td></tr>
		
		
    </logic:iterate>

	<tr><td colspan="4" align="center">
		<html:link page="/task.do?action=add">
			<bean:message key="system.task.add"/>
		</html:link>
	</td></tr>
	<tr>
   	<td class="form_button" colspan="4" align="center">
		<html:submit styleClass="form_button">
			<bean:message key="all.prompt.submit"/>
		</html:submit>
	</td>
	</tr>
    
  </table>
</html:form>
  