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

<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<p class="title">
	<bean:message key="order.statusChange.title" />
</p>
<p class="instr">
	<bean:message key="order.statusChange.instr" />
</p>

<p>
	
	<html:link page='<%="/orderMaintain.do?action=status&statusId=" + request.getParameter("statusId")%>'>
		
		   <bean:message key="all.prompt.yes"/>
	</html:link><br/>
	<html:link page="/orderMaintain.do?action=status">
		   <bean:message key="all.prompt.no"/>
	</html:link><br/>
</p>
