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


<%-- now let know the invoice list the forward values --%>
<bean:define id="forward_from" 
	         value="paymentApply"
	         toScope="session"/>

<bean:define id="forward_to" 
	         value="paymentApply"
             toScope="session"/>
             
<%-- clean-up any previous invoice selection --%>             
<logic:present parameter="reset">
	<sess:removeAttribute name="invoiceDto"/>
	<sess:removeAttribute name="listinvoice"/>
</logic:present>

<p class="title">
     <bean:message key="payment.apply.title"/>
</p>

<p class="instr"><bean:message key="payment.apply.instr"/></p>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>


<jbilling:genericList setup="true" type='<%=Constants.LIST_TYPE_INVOICE%>'/>