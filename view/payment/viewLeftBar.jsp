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

<%-- this menu is only dispalyed when a payment is present, and it is
     not the review screen --%>
<logic:present name='<%=Constants.SESSION_PAYMENT_DTO%>' scope="session">
<logic:notPresent parameter="review">

<table>
<%-- these modify links are only for those with the permission --%>
<jbilling:permission permission='<%=Constants.P_PAYMENT_MODIFY%>'>
   <%-- we can only edit or delete payments NOT linked to any invoice--%>
   <logic:empty name='<%=Constants.SESSION_PAYMENT_DTO%>' property="invoiceIds" 
                scope="session">
	    <tr>
		  <td class="leftMenuOption">
			  <html:link styleClass="leftMenu" page="/payment/review.jsp?confirm=do&noTitle=yes">
				  <bean:message key="payment.delete.link"/>
			  </html:link>
 		  </td>
	    </tr>
   <%-- only ENTERED payments can be edited --%>
   <logic:equal name='<%=Constants.SESSION_PAYMENT_DTO%>' property="resultId" 
                value='<%=Constants.RESULT_ENTERED.toString()%>'
                scope="session">
	  	<tr>
		  <td class="leftMenuOption">
			  <html:link styleClass="leftMenu" page="/paymentMaintain.do?action=setup&mode=payment&submode=edit">
				  <bean:message key="payment.edit.link"/>
			  </html:link>
			  </td>
	    </tr>
    </logic:equal>
    </logic:empty>
</jbilling:permission>
</table>

</logic:notPresent>
</logic:present>

