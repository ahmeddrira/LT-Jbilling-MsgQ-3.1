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

<logic:notPresent name='<%=Constants.SESSION_CUSTOMER_DTO%>' 
		property="customerDto.parentId">
<table class="info">
	<tr>
		<th class="info" colspan="2"><bean:message key="creditcard.info.title"/></th>
	</tr>

<logic:present name='<%=Constants.SESSION_CUSTOMER_DTO%>' property="creditCard" scope="session">
   <tr class="infoA">
	   <td class="infoprompt"><bean:message key="payment.cc.number"/></td>
	   <td class="infodata">
	 	  <jbilling:permission permission='<%=Constants.P_USER_EDIT_VIEW_CC%>'>
            <bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>' property="creditCard.number" />
          </jbilling:permission>
	 	  <jbilling:permission permission='<%=Constants.P_USER_EDIT_VIEW_CC%>'
                            negative="true">
                ****************
          </jbilling:permission>
       </td>
   </tr>
   <tr class="infoB">
	   <td class="infoprompt"><bean:message key="payment.cc.name"/></td>
	   <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>' property="creditCard.name" /></td>
   </tr>
   <tr class="infoA">
 	   <td class="infoprompt"><bean:message key="payment.cc.date"/></td>
	   <td class="infodata"><bean:write name='<%=Constants.SESSION_CUSTOMER_DTO%>' 
	   		           property="creditCard.expiry"
	   		           formatKey="format.date"/>
	   </td>
   </tr>
   <jbilling:permission permission='<%=Constants.P_USER_EDIT_LINKS%>'>
   <tr>
	  <td class="infocommands" colspan="2">
		  <html:link action="/creditCardMaintain.do?action=setup&mode=creditCard"
		  	         paramId="userId"
				     paramName='<%=Constants.SESSION_CUSTOMER_DTO%>'
				     paramProperty="userId">
			  <bean:message key="creditcard.edit.link"/>
	      </html:link>
	  </td>
   </tr>
   </jbilling:permission>
   
</logic:present>

<logic:notPresent name='<%=Constants.SESSION_CUSTOMER_DTO%>' property="creditCard" scope="session">
	<tr class="infoA">
		<td class="infoprompt" colspan="2"><bean:message key="creditcard.notPresent"/></td>
	</tr>
</logic:notPresent>

</table>
</logic:notPresent>
