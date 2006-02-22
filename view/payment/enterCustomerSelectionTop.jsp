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
<%@ taglib uri="http://jakarta.apache.org/taglibs/session-1.0" prefix="sess" %>

<%-- removal of the form bean from the session is necessary to
     avoid old data to show up later in creation/edition 
    When a customer is selected, the list of invoices has to be reset
	 otherwise it won't look for it again, even though this is a 
	 different customer. --%>
<%
  session.removeAttribute("payment");  
  session.removeAttribute(Constants.SESSION_PAYMENT_DTO);
  session.removeAttribute(Constants.SESSION_LIST_KEY + Constants.LIST_TYPE_PAYMENT_USER);  
  session.removeAttribute(Constants.SESSION_INVOICE_DTO);
  session.removeAttribute(Constants.SESSION_LIST_KEY + Constants.LIST_TYPE_INVOICE);  
%>


<p class="title">
	<sess:existsAttribute name="jsp_is_refund" value="false">
        <bean:message key="payment.customer.title"/>
    </sess:existsAttribute>
	<sess:existsAttribute name="jsp_is_refund" >
        <bean:message key="refund.customer.title"/>
    </sess:existsAttribute>
</p>

<p class="instr">
	<sess:existsAttribute name="jsp_is_refund" value="false">
        <bean:message key="payment.customer.select"/><br/>
        <bean:message key="all.prompt.help" />
	    <jbilling:help page="payments" anchor="new">
			<bean:message key="all.prompt.here" />
	    </jbilling:help>
    </sess:existsAttribute>
	<sess:existsAttribute name="jsp_is_refund" >
        <bean:message key="refund.customer.select"/>
    </sess:existsAttribute>
</p>

<%-- now let know the customer list the forward values 
     can't use the Constants :( --%>
<bean:define id="forward_from" 
	         value='<%=Constants.FORWARD_PAYMENT_CREATE%>' 
	         toScope="session"/>

<bean:define id="forward_to" 
	         value='<%=Constants.FORWARD_PAYMENT_CREATE%>' 
             toScope="session"/>
 
<jbilling:genericList setup="true" type='<%=Constants.LIST_TYPE_CUSTOMER_SIMPLE%>'/>
	            
