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

<%@ page language="java"  import="com.sapienter.jbilling.client.util.Constants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>

<logic:present name='<%=Constants.SESSION_INVOICE_DTO%>'
	           scope="session">
	 <table class="info">
	 <tr><th class="info" colspan="2"><bean:message key="invoice.selected"/></th></tr>
	 <tr class="infoA">
	 		<td class="infoprompt"><bean:message key="invoice.id.prompt"/></td>
	 	    <td class="infodata">
	 	    	<html:link page="/invoiceMaintain.do?action=view" paramId="id" 
						   paramName='<%=Constants.SESSION_INVOICE_DTO%>'
						   paramProperty="id">
					 <bean:write name='<%=Constants.SESSION_INVOICE_DTO%>'
								  property="id"/>
				</html:link>
	  	    </td>
	 </tr>
	 <tr class="infoB">
	 		<td class="infoprompt"><bean:message key="invoice.number.prompt"/></td>
	 	    <td class="infodata">
	 	    	<bean:write name='<%=Constants.SESSION_INVOICE_DTO%>'
	  	                    property="number"/>
	  	    </td>
	 </tr>
	 <tr class="infoA">
	 		<td class="infoprompt"><bean:message key="invoice.total"/></td>
	 	    <td class="infodata">
	 	    	<bean:define id="index" name='<%=Constants.SESSION_INVOICE_DTO%>'
	 	    		 property="currencyId"/>
	 	    	<bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
	 	    		  property='<%= "symbols[" + index + "].symbol" %>'
	 	    		  scope="application"
	 	    		  filter="false"/>
	 	    	<bean:write name='<%=Constants.SESSION_INVOICE_DTO%>'
	  	                    property="total" formatKey="format.money"/>
	  	    </td>
	 </tr>
	 </table>
</logic:present>
<%-- let the user know if an invoice has been selected or not--%>
<logic:notPresent name='<%=Constants.SESSION_INVOICE_DTO%>'
	                         scope="session">
	 <table class="info">
	 <tr><th class="info"><bean:message key="invoice.selected"/></th></tr>
	 <tr class="infoA">
         <td class="infoprompt"><bean:message key="invoice.notSelected"/></td>
	 </tr>
	 </table>
</logic:notPresent>
