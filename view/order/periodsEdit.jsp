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

<p class="title"><bean:message key="order.periods.title"/></p>
<p class="instr">
   <bean:message key="order.periods.instr"/>
   <bean:message key="all.prompt.help" />
   <jbilling:help page="orders" anchor="periods">
		 <bean:message key="all.prompt.here" />
   </jbilling:help>
</p>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<jbilling:getOptions generalPeriod="true"/>
<html:form action="/orderPeriod?action=edit" >
  <table class="form">
  	<tr class="form">
  		<td class="form_prompt"><bean:message key="order.period.prompt.id"/></td>
  		<td class="form_prompt"><bean:message key="order.period.prompt.value"/></td>
  		<td class="form_prompt"><bean:message key="order.period.prompt.unit"/></td>
  		<td class="form_prompt"><bean:message key="order.period.prompt.description"/></td>
  		<td></td>
  	</tr>
  	
  	<logic:iterate id="anId" name="orderPeriod" property="id"
			           scope="session" indexId="index">
	   <tr class="form">
	    	<td>
	    		<bean:write name="orderPeriod" property='<%= "id[" + index + "]" %>'/>
	    	</td>
	    	<td>
	    		<html:text property='<%= "value[" + index + "]" %>' size="2"/>
	    	</td>
	    	<td>
	    		<html:select property='<%= "unit[" + index + "]" %>'>
			   		<html:options collection='<%=Constants.PAGE_GENERAL_PERIODS%>' 
				 		property="code"
						labelProperty="description"	/>
		  		</html:select>
	    	</td>
	    	<td>
	    		<html:text property='<%= "description[" + index + "]" %>' size="10"/>
	    	</td>
	    	<td>
	    		<html:link page="/orderPeriod.do?action=delete" paramName="orderPeriod"
	    			paramProperty='<%= "id[" + index + "]" %>' paramId="id">
					<bean:message key="order.period.delete"/>
				</html:link>
	    	</td>
    	</tr>
    </logic:iterate>

	<tr><td colspan="5" align="center">
		<html:link page="/orderPeriod.do?action=add">
			<bean:message key="order.period.add"/>
		</html:link>
	</td></tr>
    
	<tr><td colspan="5"  class="form_button">
		<html:submit styleClass="form_button">
			<bean:message key="all.prompt.submit"/>
		</html:submit>
	</td></tr>
  </table>
</html:form>