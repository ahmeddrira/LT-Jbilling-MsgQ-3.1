<%--
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
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