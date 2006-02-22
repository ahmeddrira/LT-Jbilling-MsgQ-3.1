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
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt" %>

<p class="title"><bean:message key="process.review.title"/></p>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<logic:present name='<%=Constants.SESSION_PROCESS_DTO%>' 
	           scope="session">

<p class="instr"><bean:message key="process.review.instr"/></p>
<p class="instr">
	 <bean:message key="all.prompt.help" />
	 <jbilling:help page="process" anchor="review">
			 <bean:message key="all.prompt.here" />
	 </jbilling:help>
</p>

<table class="info">
  <tr>
      <th class="info" colspan="2">
          <bean:message key="process.review.info"/>
	  </th>
  </tr>
  <tr class="infoA">
  	<td class="infoprompt">
	<bean:message key="process.configuration.prompt.status"/>
	</td>
	<td class="infodata">
	<logic:equal name='<%=Constants.SESSION_PROCESS_CONFIGURATION_DTO%>' 
				 property="reviewStatus"
				 scope="session"
				 value='<%=Constants.REVIEW_STATUS_GENERATED.toString()%>' >
		<bean:message key="process.approval.generated"/>
	</logic:equal>
	
	<logic:equal name='<%=Constants.SESSION_PROCESS_CONFIGURATION_DTO%>' 
				 property="reviewStatus"
				 scope="session"
				 value='<%=Constants.REVIEW_STATUS_APPROVED.toString()%>' >
		<bean:message key="process.approval.yes"/>
	</logic:equal>
	
	<logic:equal name='<%=Constants.SESSION_PROCESS_CONFIGURATION_DTO%>' 
				 property="reviewStatus"
				 scope="session"
				 value='<%=Constants.REVIEW_STATUS_DISAPPROVED.toString()%>' >
		<bean:message key="process.approval.no"/>
	</logic:equal>
	</td>
  </tr>	
  <tr class="infoB">
  	<td class="infoprompt">
  	<bean:message key="process.configuration.prompt.nextRunDate"/>
  	</td>
  	<td class="infodata">
	<bean:write name='<%=Constants.SESSION_PROCESS_CONFIGURATION_DTO%>' 
				 property="nextRunDate"
				 scope="session"
				 formatKey="format.date"/>
  	</td>
  </tr>
  
  <logic:equal name='<%=Constants.SESSION_USER_DTO%>'
						 property="mainRoleId"
						 scope="session"
						 value='<%=Constants.TYPE_INTERNAL.toString()%>'>	 	  
	<bean:define id="jsp_hasApproval" scope="page" value="y"/>
  </logic:equal>
  <logic:equal name='<%=Constants.SESSION_USER_DTO%>'
						 property="mainRoleId"
						 scope="session"
						 value='<%=Constants.TYPE_ROOT.toString()%>'>	 	  
	<bean:define id="jsp_hasApproval" scope="page" value="y"/>
  </logic:equal>

  <logic:present name="jsp_hasApproval" scope="page">
  <tr>
  	<td class="infocommands">
  	<html:link page="/invoice/listProcess.jsp?toApprove=yes">
  		<bean:message key="process.approval.approve"/>
	</html:link>
  	</td>
  	<td class="infocommands">
  	<html:link action="processMaintain?action=approval">
  		<bean:message key="process.approval.disapprove"/>
	</html:link>
  	</td>
  </tr>
  </logic:present>
</table>

</logic:present>

  
