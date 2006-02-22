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

<%@ page language="java" import="com.sapienter.jbilling.client.util.Constants, com.sapienter.jbilling.server.user.UserDTOEx"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/session-1.0" prefix="sess" %>

<p class="title">
<bean:message key="user.create.title"/>
</p>

<p class="instr">
<bean:message key="user.create.instr"/>
</p>

<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>
<html:errors/>


<logic:present parameter="customer">
	<bean:define id="jsp_is_customer" value="yes" toScope="session"/>
</logic:present>
<logic:present parameter="partner">
	<bean:define id="jsp_is_partner" value="yes" toScope="session"/>
</logic:present>
<logic:present parameter="partner_id">
	<bean:define id="jsp_partnerId" value='<%=request.getParameter("partnerId") %>' toScope="session"/>
</logic:present>
<logic:present parameter="frompartner">
    <%
       session.setAttribute("jsp_partnerId", ((UserDTOEx) session.getAttribute(
       			Constants.SESSION_USER_DTO)).getPartnerDto().getId().toString());
    %>
</logic:present>


<html:form action="/userCreate">
	 <table class="form">
	 	<tr class="form">
	 		<td></td>
	 		<td class="form_prompt"><bean:message key="user.prompt.username"/></td>
	 		<td ><html:text property="username" size="15" /></td>
	 	</tr>
	 	<tr class="form">
	 		<td></td>
	 		<td class="form_prompt"><bean:message key="user.prompt.password"/></td>
	 		<td ><html:password property="password" redisplay="false" size="15" /></td>
	 	</tr>
	 	<tr class="form">
	 		<td></td>
	 		<td class="form_prompt"><bean:message key="user.prompt.verifyPassword"/></td>
	 		<td ><html:password property="verifyPassword" redisplay="false" size="15" /></td>
	 	</tr>
	 	<tr class="form">
			<td>
				 <jbilling:help page="users" anchor="email">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
	 		<td class="form_prompt"><bean:message key="user.prompt.email"/></td>
	 		<td ><html:text property="email" size="50" /></td>
	 	</tr>

    	<logic:notPresent name="jsp_is_customer" scope="session">
    	<logic:notPresent name="jsp_is_partner" scope="session">    		
	    <jbilling:permission permission='<%=Constants.P_USER_CREATE_TYPE%>'>
	 		<jbilling:getOptions userType="true"/>
			 <tr class="form">
			 	 <td></td>
				 <td class="form_prompt"><bean:message key="user.prompt.type"/></td>
				 <td >
				 	<%-- This call will return only those roles that are GREATER than the caller --%>
				 	<html:select property="type">
						<html:options collection='<%=Constants.PAGE_USER_TYPES%>' 
				                      property="code"
				                      labelProperty="description"/>
					</html:select>
				 </td>
			 </tr>
 		</jbilling:permission>
		</logic:notPresent>
		</logic:notPresent>
		<logic:present name="jsp_is_customer" scope="session">
			<html:hidden property="type" value='<%=Constants.TYPE_CUSTOMER.toString()%>' />
		</logic:present>
		<logic:present name="jsp_is_partner" scope="session">
			<html:hidden property="type" value='<%=Constants.TYPE_PARTNER.toString()%>' />
		</logic:present>

	 	<tr class="form">
			<td>
				 <jbilling:help page="users" anchor="currency">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
	 		<td class="form_prompt"><bean:message key="currency.external.prompt.name"/></td>
	 		<td >
	 			<jbilling:getOptions currencies="true"/>
				<html:select property="currencyId">
					<html:options collection='<%=Constants.PAGE_CURRENCIES%>' 
								  property="code"
								  labelProperty="description"/>
				</html:select>
	 		</td>
	 	</tr>
	 	
	 	<%-- display the partner field if this is a customer and there's no partner
	 	     specified in the request --%>
	 	<logic:present name="jsp_is_customer" scope="session">
    	<logic:notPresent name="jsp_partnerId" scope="session">
	 	<tr class="form">
			<td>
				 <jbilling:help page="users" anchor="partner">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
	 		<td class="form_prompt"><bean:message key="user.prompt.partner"/></td>
	 		<td ><html:text property="partnerId" size="5" /></td>
	 	</tr>
	 	</logic:notPresent>
    	<logic:present name="jsp_partnerId" scope="session">
    		<html:hidden property="partnerId" value='<%=(String)session.getAttribute("jsp_partnerId")%>' />
	 	</logic:present>
	 	
	 	<tr class="form">
			<td>
				 <jbilling:help page="users" anchor="parent">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
	 		<td class="form_prompt"><bean:message key="user.prompt.parent"/></td>
	 		<td ><html:text property="parentId" size="5" /></td>
	 	</tr>
	 	<tr class="form">
			<td>
				 <jbilling:help page="users" anchor="allow">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
	 		<td class="form_prompt"><bean:message key="user.prompt.isParent"/></td>
	 		<td ><html:checkbox property="chbx_is_parent"/></td>
	 	</tr>
	 	
	 	</logic:present>

	 	<tr class="form">
	 		<td></td>
	 		<td class="form_button" colspan="2">
              	<html:submit styleClass="form_button">
              		<bean:message key="user.create.submit"/>
              	</html:submit>
            </td>
		</tr>	 
	</table>
</html:form>