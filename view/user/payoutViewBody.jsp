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

<logic:present parameter="result">
	<p class="title"><bean:message key="payout.manual.result"/></p>
	
	<html:errors/>
	<html:messages message="true" id="myMessage">
		<p><bean:write name="myMessage"/></p>
	</html:messages>
</logic:present>

<table class="info" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<th class="info" colspan="2"><bean:message key="payout.info.title"/></th>
	</tr>
	
	<logic:present name='<%=Constants.SESSION_PAYOUT_DTO%>' scope="session">
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="payout.prompt.id"/></td>
		<td class="infodata"><bean:write name='<%=Constants.SESSION_PAYOUT_DTO%>'  
			property="id" scope="session"/></td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="payout.prompt.startDate"/></td>
		<td  class="infodata"><bean:write name='<%=Constants.SESSION_PAYOUT_DTO%>'  
			property="startingDate" scope="session" formatKey="format.date"/></td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="payout.prompt.endDate"/></td>
		<td  class="infodata"><bean:write name='<%=Constants.SESSION_PAYOUT_DTO%>'  
			property="endingDate" scope="session" formatKey="format.date"/></td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="payout.prompt.payments"/></td>
		<td  class="infodata">
			 <bean:define id="index" name='<%=Constants.SESSION_PAYOUT_DTO%>'
				  property="payment.currencyId"/>
			 <bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				   property='<%= "symbols[" + index + "].symbol" %>'
				   scope="application"
				   filter="false"/>
			<bean:write name='<%=Constants.SESSION_PAYOUT_DTO%>'  
			       property="paymentsAmount" scope="session" formatKey="format.money"/>
	    </td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="payout.prompt.refunds"/></td>
		<td  class="infodata">
			 <bean:define id="index" name='<%=Constants.SESSION_PAYOUT_DTO%>'
				  property="payment.currencyId"/>
			 <bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				   property='<%= "symbols[" + index + "].symbol" %>'
				   scope="application"
				   filter="false"/>
			<bean:write name='<%=Constants.SESSION_PAYOUT_DTO%>'  
			       property="refundsAmount" scope="session" formatKey="format.money"/>
	    </td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="payout.prompt.total"/></td>
		<td  class="infodata">
			 <bean:define id="index" name='<%=Constants.SESSION_PAYOUT_DTO%>'
				  property="payment.currencyId"/>
			 <bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				   property='<%= "symbols[" + index + "].symbol" %>'
				   scope="application"
				   filter="false"/>
			<bean:write name='<%=Constants.SESSION_PAYOUT_DTO%>'  
			       property="total" scope="session" formatKey="format.money"/>
	    </td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="payout.prompt.balance"/></td>
		<td  class="infodata">
			 <bean:define id="index" name='<%=Constants.SESSION_PAYOUT_DTO%>'
				  property="payment.currencyId"/>
			 <bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				   property='<%= "symbols[" + index + "].symbol" %>'
				   scope="application"
				   filter="false"/>
			<bean:write name='<%=Constants.SESSION_PAYOUT_DTO%>'  
			       property="balanceLeft" scope="session" formatKey="format.money"/>
	    </td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="payout.prompt.paid"/></td>
		<td  class="infodata">
			 <bean:define id="index" name='<%=Constants.SESSION_PAYOUT_DTO%>'
				  property="payment.currencyId"/>
			 <bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				   property='<%= "symbols[" + index + "].symbol" %>'
				   scope="application"
				   filter="false"/>
			<bean:write name='<%=Constants.SESSION_PAYOUT_DTO%>'  
			       property="payment.amount" scope="session" formatKey="format.money"/>
	    </td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="payout.prompt.date"/></td>
		<td  class="infodata"><bean:write name='<%=Constants.SESSION_PAYOUT_DTO%>'  
			property="payment.createDateTime" scope="session" formatKey="format.timestamp"/></td>
	</tr>
	</logic:present>
	<logic:notPresent name='<%=Constants.SESSION_PAYOUT_DTO%>' scope="session">
		<tr class="infoA">
		<td class="infoprompt" colspan="2"><bean:message key="payout.info.void"/></td>
		</tr>
	</logic:notPresent>
</table>