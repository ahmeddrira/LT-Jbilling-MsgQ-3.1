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

<html:errors/>
<html:form action="/itemPriceMaintain?action=edit&mode=price" >
      <logic:present parameter="create">                
         <html:hidden property="create" value="yes"/>
      </logic:present>
	
	  <table class="form">
	  	<tr class="form">
	  		<td class="form_prompt"><bean:message key="item.prompt.id"/></td>
	  		<td><bean:write name='<%=Constants.SESSION_ITEM_DTO%>' scope="session" property="id"/></td>
	  	</tr>
	    <tr class="form">
	      <td class="form_prompt"><bean:message key="item.prompt.internalNumber"/></td>
	      <td><bean:write name='<%=Constants.SESSION_ITEM_DTO%>' scope="session" property="number" /></td>
    	</tr>
	    <tr class="form">
	      <td class="form_prompt"><bean:message key="item.prompt.description"/></td>
	      <td><bean:write name='<%=Constants.SESSION_ITEM_DTO%>' scope="session" property="description" /></td>
    	</tr>
	    <tr class="form">
	      <td class="form_prompt">
	      	  <bean:message key="item.prompt.price"/>
	      	  <bean:define id="index" name="userPrice"
				  property="currencyId"/>
			  <bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				   property='<%= "symbols[" + index + "].symbol" %>'
				   scope="application"
				   filter="false"/>
	      
	      </td>
	      <td><html:text property="price" /></td>
    	</tr>

	    <tr class="form">
    	  <td colspan="2" class="form_button">
            <logic:present parameter="create">
                <html:submit styleClass="form_button" property="create">
                    <bean:message key="item.user.price.prompt.create"/>
                </html:submit>
            </logic:present>
           <logic:notPresent parameter="create">
                <html:submit styleClass="form_button">
                    <bean:message key="all.prompt.submit"/>                
                </html:submit>
            </logic:notPresent>
         </td>
    	</tr>
	  </table>
</html:form>
