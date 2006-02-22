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

<logic:present name='<%=Constants.SESSION_INVOICE_DTO%>' scope="session">
	<table>
	<tr>
		<td class="leftMenuOption">
			<html:link styleClass="leftMenu" page="/invoice/download.jsp">
				<bean:message key="invoice.downloadPDF.link"/>
			</html:link>
		</td>
	</tr>
	<%-- the delete link. Only for those with the permission --%>
	<jbilling:permission permission='<%=Constants.P_INVOICE_DELETE%>'>

		<jbilling:getPreference preferenceId='<%=Constants.PREFERENCE_INVOICE_DELETE%>'
			beanName="preference"/> 
		<logic:equal name="preference" value="1">
	  	<tr>
		  <td class="leftMenuOption">
			  <html:link styleClass="leftMenu" page="/invoice/view.jsp?confirm=do&noTitle=yes">
				  <bean:message key="invoice.delete.link"/>
			  </html:link>
 		  </td>
	    </tr>
	    </logic:equal>
	    
	</jbilling:permission>
	
	</table>
</logic:present>


