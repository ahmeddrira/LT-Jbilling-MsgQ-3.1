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
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<jbilling:getOptions orderLineType="true"/>

<logic:present parameter="create">
    <%-- removal of the form bean from the session is necessary to
         avoid old data to show up later in creation/edition --%>
    <% session.removeAttribute("item"); %>
</logic:present>

<html:form action="/itemTypeMaintain?action=edit&mode=type">
      <logic:present parameter="create">                
         <html:hidden property="create" value="yes"/>
      </logic:present>

	  <table class="form">
	    <tr class="form">
	      <td class="form_prompt"><bean:message key="item.type.prompt.description"/></td>
	      <td><html:text property="name" /></td>
    	</tr>

	    <tr class="form">
	      <td class="form_prompt"><bean:message key="order.line.prompt.type"/></td>
	      <td>
	      	<html:select property="order_line_type">
		          <html:options collection='<%=Constants.PAGE_ORDER_LINE_TYPES%>' 
				            property="code"
				            labelProperty="description"	/>
		    </html:select>
	      </td>
    	</tr>

	    <tr class="form">
    	  <td colspan="2" class="form_button">
              <html:submit styleClass="form_button">
                <logic:present parameter="create">                
                    <bean:message key="item.type.prompt.create"/>
                </logic:present>
                <logic:notPresent parameter="create">
                    <bean:message key="all.prompt.submit"/>                
                </logic:notPresent>
              </html:submit>
          </td>
    	</tr>
	  </table>
</html:form>
