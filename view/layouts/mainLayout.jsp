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

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>


<%-- Layout component 
  parameters : mainMenuBar, secondMenuBar, leftMenuBar, body, featureTitle,  footer 
--%>


<html:html>
  <HEAD>
    <title><bean:message key="all.title"/></title>
  	<link rel="stylesheet" type="text/css" 
  		href="<jbilling:getPreference preferenceId='<%=Constants.PREFERENCE_CSS_LOCATION%>'/>" /> 
  </HEAD>
  <body>

   	<table width="760" cellpadding="0" cellspacing="0" border="0">

  	<!-- This is the top bar -->
  	<tr class="topBar">

  	<!-- This is the coporate graphic -->
  	<td class="logo"><center><img src="<jbilling:getPreference preferenceId='<%=Constants.PREFERENCE_LOGO_LOCATION%>'/>" /></center></td>

	<!-- This is the menu and feature title bar -->  	
	<td class="threeBars">
		<table  class="threeBars" cellpadding="0" cellspacing="0" border="0">
			<!-- The main menu, to be rendered by a custom tag -->
			<tr class="bar"><td>
				<table class="menu" align="center" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<tiles:insert attribute="mainMenuBar" />
					</tr>
				</table>
			</td></tr>
			<!-- The secondary menu to be rendered by a custom tag -->
			<tr class="bar"><td>
				<table  class="submenu" align="center" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<tiles:insert attribute="secondMenuBar" />
					</tr>
				</table>
			</td></tr>
			<!-- The Feature Title & Logout -->
			<tr class="bar"> <td>
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
				     <td class="title">
				     	<tiles:useAttribute id="barTitle" name="featureTitle"/>
				     	<bean:message name="barTitle"/>
				     </td>
				     
				     <!-- No help for end customers -->
				     <logic:notEqual name='<%=Constants.SESSION_USER_DTO%>'
					 	property="mainRoleId"
					 	scope="session"
					 	value='<%=Constants.TYPE_CUSTOMER.toString()%>'>
				     <td class="logout">
						<a target="jbsite" href="http://www.jbilling.com/?q=node/304">
						   <bean:message key="all.training"/>
					    </a>				     
					 </td>
				     <td class="logout">
					    <jbilling:help page="index">
						   <bean:message key="all.help"/>
					    </jbilling:help>
				     </td>
				     </logic:notEqual>
				     
				     <td class="logout">
					    <html:link page="/logout.do">
						   <bean:message key="all.logout"/>
					    </html:link>
				     </td>
					</tr>
				</table>
			</td></tr>
		</table>
	</td>

  	</tr>

	<!-- The Main Body-->
	<tr>
		<td class="leftMenu"><tiles:insert attribute="leftMenuBar"/></td>
		<td class="body"><tiles:insert attribute="body"/></td>
	</tr>
	
	<!-- The Footer -->
	<tr>
		<td colspan="2"><tiles:insert attribute="footer" /></td>
	</tr>


  	</table>
  </body>
</html:html>