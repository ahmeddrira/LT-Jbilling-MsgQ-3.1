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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/session-1.0" prefix="sess" %>


<%-- removal of the form bean from the session is necessary to
     avoid old data to show up later in creation/edition --%>
<%-- when a customer is selected, the list of invoices has to be reset
	 otherwise it won't look for it again, even though this is a 
	 different customer. 
	 This is here AND in the customerSelectionTop because the customer
	 doesn't use the customer selection, but it has to be there also because
	 of the 'back' browser button --%>
<%
  session.removeAttribute("payment");  
  session.removeAttribute(Constants.SESSION_PAYMENT_DTO);
%>

<%-- let know the body if this is a cheque or a cc --%>
<logic:present parameter="cc">                
   <sess:setAttribute name="jsp_payment_method">cc</sess:setAttribute>
</logic:present>
<logic:present parameter="cheque">            
   <sess:setAttribute name="jsp_payment_method">cheque</sess:setAttribute>
</logic:present>
<logic:present parameter="ach">            
   <sess:setAttribute name="jsp_payment_method">ach</sess:setAttribute>
</logic:present>
<logic:present parameter="paypal">
   <sess:setAttribute name="jsp_payment_method">paypal</sess:setAttribute>
</logic:present>

<%-- let know the body if this is a refund --%>
<logic:present parameter="refund">                
	<sess:setAttribute name="jsp_is_refund">yes</sess:setAttribute>
	<%
	  session.removeAttribute(Constants.SESSION_LIST_KEY + Constants.LIST_TYPE_PAYMENT_USER);  
	%>
</logic:present>
<logic:notPresent parameter="refund">                
	<%
	  session.removeAttribute(Constants.SESSION_PAYMENT_DTO);
	  session.removeAttribute(Constants.SESSION_INVOICE_DTO);
	  session.removeAttribute(Constants.SESSION_LIST_KEY + Constants.LIST_TYPE_INVOICE);  
      session.removeAttribute("jsp_linked_invoices");
	%>
</logic:notPresent>


<%-- now depending who's logged, show up the list of customers or not --%>
<logic:notEqual name='<%=Constants.SESSION_USER_DTO%>'
	                     property="mainRoleId"
	                     scope="session"
	                     value='<%=Constants.TYPE_CUSTOMER.toString()%>'>
     <tiles:insert definition="payment.enter.customerSelection" flush="true" />
</logic:notEqual>

<%-- Customers shouldn't select a user, they are the user
          This basically does just like if a user was selected from the list --%>
<logic:equal name='<%=Constants.SESSION_USER_DTO%>'
	                     property="mainRoleId"
	                     scope="session"
	                     value='<%=Constants.TYPE_CUSTOMER.toString()%>'>
	    <%
	         session.setAttribute(Constants.SESSION_USER_ID, session.getAttribute(
	                    Constants.SESSION_LOGGED_USER_ID));
	    %>     
		<logic:redirect forward='<%=Constants.FORWARD_PAYMENT_CREATE%>' />
</logic:equal>