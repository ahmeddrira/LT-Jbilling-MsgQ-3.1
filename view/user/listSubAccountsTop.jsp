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

<%@ page language="java" import="com.sapienter.jbilling.client.util.Constants,com.sapienter.jbilling.server.order.NewOrderDTO;"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/session-1.0" prefix="sess" %>

<p class="title">
<bean:message key="user.subaccount.title"/>
</p>
<p class="instr">
<bean:message key="customer.list.instr"/>
</p>

<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<%-- now let know the customer list the forward values 
     can't use the Constants :( --%>
<bean:define id="forward_from" 
	         value='<%=Constants.FORWARD_USER_MAINTAIN%>' 
	         toScope="session"/>

<bean:define id="forward_to" 
	         value='<%=Constants.FORWARD_USER_VIEW%>' 
             toScope="session"/>

<jbilling:genericList setup="true" type='<%=Constants.LIST_TYPE_SUB_ACCOUNTS%>'/>
