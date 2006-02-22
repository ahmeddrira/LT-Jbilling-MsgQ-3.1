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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:notPresent parameter="view">
<%-- the customer selection is always available --%>
<html:link page="/payment/customerSelect.jsp">
    <bean:message key="payment.enter.link.customerSelect"/>
</html:link>

<%-- the payment info is there if the customer is present--%>
<logic:present name='<%=Constants.SESSION_USER_ID%>' 
	                         scope="session">
	<html:link page="/payment/enterInfoCheque.jsp">
   		<bean:message key="payment.enter.link.chequeInfo"/>
   </html:link><br/>
</logic:present>

<%-- the payment review is present if the dto exists--%>
<logic:present name='<%=Constants.SESSION_PAYMENT_DTO%>' 
	                         scope="session">
	<html:link page="/payment/enterReview.jsp">
   		<bean:message key="payment.enter.link.review"/>
   </html:link><br/>
</logic:present>
</logic:notPresent>