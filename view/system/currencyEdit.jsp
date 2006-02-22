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

<p class="title"><bean:message key="system.currency.title"/></p>
<p class="instr">
   <bean:message key="all.prompt.help" />
   <jbilling:help page="system" anchor="currencies">
		 <bean:message key="all.prompt.here" />
   </jbilling:help>
</p>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<html:form action="/currencyMaintain?action=edit" >
  <table class="form">
  	<tr class="form">
  		<jbilling:getOptions currencies="true"/>
  		<td class="form_prompt"><bean:message key="system.currency.default"/></td>
		<td colspan="4">
		   <html:select property="defaultCurrencyId">
			   <html:options collection='<%=Constants.PAGE_CURRENCIES%>' 
				 property="code"
				 labelProperty="description"	/>
		  </html:select>
 	    </td>
  		
	</tr>
  	<tr class="form">
  		<td class="form_prompt"><bean:message key="system.currency.prompt.name"/></td>
  		<td class="form_prompt"><bean:message key="system.currency.prompt.symbol"/></td>
  		<td class="form_prompt"><bean:message key="system.currency.prompt.inUse"/></td>
  		<td class="form_prompt"><bean:message key="system.currency.prompt.rate"/></td>
  		<td class="form_prompt"><bean:message key="system.currency.prompt.sysRate"/></td>
  	</tr>
  	
  	<logic:iterate id="line" name="currency" property="lines" indexId="index">
	  <tr class="form">
		  <td><bean:write name="currency" property='<%= "lines[" + index+ "].name" %>' /></td>
		  <td align="center">
 			 <bean:define id="cId" name="currency"
			       property='<%= "lines[" + index+ "].id" %>'/>
			<bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				      property='<%= "symbols[" + cId + "].symbol" %>'
				      scope="application"
				      filter="false"/>
		  </td>
		  <td><html:checkbox property='<%= "lines[" + index+ "].inUse" %>' /></td>
		  <td>
		  	<logic:notEqual name="currency" 
		  		            property='<%= "lines[" + index+ "].id" %>'
		  		            value="1">
 		  	    <html:text property='<%= "lines[" + index+ "].rate" %>' />
 		  	</logic:notEqual>
		  </td>
		  <td align="center">
		  	 <bean:write name="currency" property='<%= "lines[" + index+ "].sysRate" %>' formatKey="format.money"/>
		  </td>
	  </tr>
    </logic:iterate>
    
	<tr class="form">
		<td colspan="5" class="form_button">
			<html:submit styleClass="form_button">
                <bean:message key="all.prompt.submit"/>                
            </html:submit>
        </td>
	</tr>

  </table>
</html:form>
