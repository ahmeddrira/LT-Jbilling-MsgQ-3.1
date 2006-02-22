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

<logic:present parameter="create">
    <%-- removal of the form bean from the session is necessary to
         avoid old data to show up later in creation/edition --%>
    <% session.removeAttribute("promotion"); %>
</logic:present>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p> 
</html:messages>

<html:form action="/promotionMaintain?action=edit&mode=promotion">
      <logic:present parameter="create">                
               <html:hidden property="create" value="yes"/>
      </logic:present>
	
	  <table class="form">
	    <tr class="form">
	      <td class="form_prompt"><bean:message key="promotion.prompt.code"/></td>
	      <td colspan="4">
	         <jbilling:permission permission='<%=Constants.P_ITEM_EDIT%>'>
				 <html:text property="code" />
	         </jbilling:permission>
		     <jbilling:permission permission='<%=Constants.P_ITEM_EDIT%>' negative="true">
		         <bean:write name="promotion" property="code" />
	         </jbilling:permission>	      
	      </td>	      
    	</tr>
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="promotion.prompt.since"/></td>
	 		<jbilling:dateFormat format="mm-dd">
				 <td><html:text property="since_month" size="2" maxlength="2"/></td>
				 <td><html:text property="since_day" size="2" maxlength="2"/></td>
			</jbilling:dateFormat>
	 		<jbilling:dateFormat format="dd-mm">
				 <td><html:text property="since_day" size="2" maxlength="2"/></td>
				 <td><html:text property="since_month" size="2" maxlength="2"/></td>
			</jbilling:dateFormat>
	 		<td><html:text property="since_year" size="4" maxlength="4"/></td>
	 		<td><bean:message key="all.prompt.dateFormat"/></td>
	 	</tr>
 	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="promotion.prompt.until"/></td>
	 		<jbilling:dateFormat format="mm-dd">
				 <td><html:text property="until_month" size="2" maxlength="2"/></td>
				 <td><html:text property="until_day" size="2" maxlength="2"/></td>
			</jbilling:dateFormat>
	 		<jbilling:dateFormat format="dd-mm">
				 <td><html:text property="until_day" size="2" maxlength="2"/></td>
				 <td><html:text property="until_month" size="2" maxlength="2"/></td>
			</jbilling:dateFormat>
				 
	 		<td><html:text property="until_year" size="4" maxlength="4"/></td>
	 		<td><bean:message key="all.prompt.dateFormat"/></td>
	 	</tr>
    	
	    <tr class="form">
	      <td class="form_prompt"><bean:message key="promotion.prompt.once"/></td>
	      <td colspan="4">
	         <jbilling:permission permission='<%=Constants.P_ITEM_EDIT%>'>
				 <html:checkbox property="chbx_once" />
	         </jbilling:permission>
		     <jbilling:permission permission='<%=Constants.P_ITEM_EDIT%>' negative="true">
		         <bean:write name="promotion" property="chbx_once" />
	         </jbilling:permission>	      
	      </td>	      
    	</tr>
	    <tr class="form">
	      <td class="form_prompt"><bean:message key="promotion.prompt.notes"/></td>
	       <td colspan="4">
	           <jbilling:permission permission='<%=Constants.P_ITEM_EDIT%>'>	      
                    <html:text property="notes" />
	           </jbilling:permission>	      
	           <jbilling:permission permission='<%=Constants.P_ITEM_EDIT%>' negative="true">	      
	                 <bean:write name="promotion" property="notes" />
	           </jbilling:permission>	      
	       </td>
    	</tr>
    	
      <jbilling:permission permission='<%=Constants.P_ITEM_EDIT%>'>
	    <tr class="form">
    	  <td colspan="6" class="form_button">
            <logic:present parameter="create">
                <html:submit styleClass="form_button" property="create">
                    <bean:message key="promotion.prompt.create"/>
                </html:submit>
            </logic:present>
           <logic:notPresent parameter="create">
                <html:submit styleClass="form_button">
                    <bean:message key="all.prompt.submit"/>                
                </html:submit>
            </logic:notPresent>
         </td>
    	</tr>
    	 <logic:notPresent parameter="create">
    	 <tr class="form">
    	 	<td colspan="6" align="center">
    	 		<html:link forward='<%=Constants.FORWARD_ITEM_EDIT%>'>
    	 			<bean:message key="promotion.edit.item"/>
    	 		</html:link>
    	 	</td>
    	 </tr>
    	 </logic:notPresent>
    	
      </jbilling:permission>    	
	  </table>
</html:form>
