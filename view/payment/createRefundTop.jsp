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


<%-- now let know the invoice list the forward values 
     can't use the Constants :( . The payment info form does the paging, so 
     here both from/to are the same --%>
<bean:define id="forward_from" 
	         value='<%=Constants.FORWARD_PAYMENT_CREATE%>' 
	         toScope="session"/>

<bean:define id="forward_to" 
	         value='<%=Constants.FORWARD_PAYMENT_CREATE%>' 
             toScope="session"/>

<p class="title">
<sess:equalsAttribute name="jsp_payment_method" match="cc">                	
     <bean:message key="refund.cc.title"/>
</sess:equalsAttribute>
<sess:equalsAttribute name="jsp_payment_method" match="cheque">                
     <bean:message key="refund.cheque.title"/>
</sess:equalsAttribute>
<sess:equalsAttribute name="jsp_payment_method" match="ach">                
     <bean:message key="refund.ach.title"/>
</sess:equalsAttribute>
</p>

<p class="instr">
     <bean:message key="refund.enter.instr"/>
</p>

<jbilling:genericList setup="true" type='<%=Constants.LIST_TYPE_PAYMENT_USER%>'/>


<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>


<p class="instr">
     <bean:message key="refund.enter.payment.list"/>
</p>
