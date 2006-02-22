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
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt" %>

<logic:present name="authorizationDto" scope="request">
<table class="info">
	<tr>
		<th class="info" colspan="2"><bean:message key="authorization.info.title"/></th>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="authorization.processor"/></td>
		<td class="infodata">	
            <bean:write name="authorizationDto" 
                        property="processor"
                        scope="request"/>
        </td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="authorization.code1"/></td>
		<td class="infodata">	
            <bean:write name="authorizationDto" 
                        property="code1"
                        scope="request"/>
        </td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="authorization.code2"/></td>
		<td class="infodata">	
            <bean:write name="authorizationDto" 
                        property="code2"
                        scope="request"/>
        </td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="authorization.code3"/></td>
		<td class="infodata">	
            <bean:write name="authorizationDto" 
                        property="code3"
                        scope="request"/>
        </td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="authorization.approvalCode"/></td>
		<td class="infodata">	
            <bean:write name="authorizationDto" 
                        property="approvalCode"
                        scope="request"/>
        </td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="authorization.transactionId"/></td>
		<td class="infodata">	
            <bean:write name="authorizationDto" 
                        property="transactionId"
                        scope="request"/>
        </td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="authorization.mD5"/></td>
		<td class="infodata">	
            <bean:write name="authorizationDto" 
                        property="MD5"
                        scope="request"/>
        </td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="authorization.cardCode"/></td>
		<td class="infodata">	
            <bean:write name="authorizationDto" 
                        property="cardCode"
                        scope="request"/>
        </td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="authorization.createDate"/></td>
		<td class="infodata">	
            <bean:write name="authorizationDto" 
                        property="createDate"
                        scope="request"
                        formatKey="format.timestamp"/>
        </td>
	</tr>
	
</table>	
</logic:present>