<%--
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
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
