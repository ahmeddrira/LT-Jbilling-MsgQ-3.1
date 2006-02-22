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

<table class="info">
<tr><th class="info" colspan="2"><bean:message key="payment.selected"/></th></tr>

<logic:present name='<%=Constants.SESSION_PAYMENT_DTO%>'
	           scope="session">
	 <tr class="infoA">
	 		<td class="infoprompt"><bean:message key="payment.id"/></td>
	 	    <td class="infodata">
	 	    	<bean:write name='<%=Constants.SESSION_PAYMENT_DTO%>'
	  	                              property="id"/>
	  	    </td>
	 </tr>
	 <tr class="infoB">
	 		<td class="infoprompt"><bean:message key="payment.amount"/></td>
	 	    <td class="infodata align="right">
			    <bean:define id="index" name='<%=Constants.SESSION_PAYMENT_DTO%>'
				      property="currencyId"/>
			    <bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				      property='<%= "symbols[" + index + "].symbol" %>'
				      scope="application"
				      filter="false"/>
	 	    	
	 	    	<bean:write name='<%=Constants.SESSION_PAYMENT_DTO%>'
	  	                    property="amount" formatKey="format.money"/>
	  	    </td>
	 </tr>
	 <tr class="infoA">
	 		<td class="infoprompt"><bean:message key="payment.date"/></td>
	 	    <td class="infodata align="right">
	 	    	<bean:write name='<%=Constants.SESSION_PAYMENT_DTO%>'
	  	                    property="createDateTime" formatKey="format.timestamp"/>
	  	     </td>
	 </tr>
</logic:present>

<%-- let the user know if a payment has been selected or not--%>
<logic:notPresent name='<%=Constants.SESSION_PAYMENT_DTO%>'
	                         scope="session">
	 <tr class="infoA">
        <td class="infoprompt"><bean:message key="payment.notSelected"/></td>
	 </tr>
</logic:notPresent>

</table>
