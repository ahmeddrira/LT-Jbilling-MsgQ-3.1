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

<p class="title"><bean:message key="system.branding.title"/></p>
<p class="instr">
   <bean:message key="all.prompt.help" />
   <jbilling:help page="system" anchor="branding">
		 <bean:message key="all.prompt.here" />
   </jbilling:help>
</p>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<html:form action="/brandingMaintain?action=edit&mode=branding" >
  <table class="form">
	  <tr class="form">
		  <td>
			 <jbilling:help page="system" anchor="branding.logo">
				 <img border="0" src="/billing/graphics/help.gif"/>
			 </jbilling:help>
		  </td>
		  <td class="form_prompt"><bean:message key="system.branding.prompt.logo"/></td>
		  <td><html:text property="logo" /></td>
	  </tr>
	  <tr class="form">
		  <td>
			 <jbilling:help page="system" anchor="branding.css">
				 <img border="0" src="/billing/graphics/help.gif"/>
			 </jbilling:help>
		  </td>
		  <td class="form_prompt"><bean:message key="system.branding.prompt.css"/></td>
		  <td><html:text property="css" /></td>
	  </tr>
	<tr class="form">
		<td></td>
		<td colspan="2" class="form_button">
			<html:submit styleClass="form_button">
                <bean:message key="all.prompt.submit"/>                
            </html:submit>
        </td>
	</tr>

  </table>
</html:form>
