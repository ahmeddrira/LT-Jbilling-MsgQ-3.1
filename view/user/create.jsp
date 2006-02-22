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

<%-- remove the user id, so the left menu doesn't show up the options until 
          the user has been created --%>
<logic:present parameter="create">
    <%-- removal of the form bean from the session is necessary to
         avoid old data to show up later in creation/edition --%>
   <% session.removeAttribute("userCreate"); %>
   <% session.removeAttribute("jsp_is_customer"); %>
   <% session.removeAttribute("jsp_is_partner"); %>
   <% session.removeAttribute("jsp_partnerId"); %>
   <% session.removeAttribute(Constants.SESSION_USER_ID); %>
   <logic:present parameter="partner">
	   <% session.removeAttribute(Constants.SESSION_PARTNER_ID); %>
	   <% session.removeAttribute(Constants.SESSION_PARTNER_DTO); %>
	   <% session.removeAttribute(Constants.SESSION_PAYOUT_DTO); %>
   </logic:present>
</logic:present>
<tiles:insert definition="user.create" flush="true" />
