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

Contributor(s): Lucas Pickstone_______________________.
--%>

<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<p class="title"><bean:message key="invoice.logo.title" /></p>
<p class="instr">
   <bean:message key="invoice.logo.instr" />
</p>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage" /></p>
</html:messages>

<p>
<jbilling:invoiceLogoExists id="logoExists" />
<c:choose>
  <c:when test="${logoExists}">
    <img src="logoImage.jsp" />
  </c:when>
  <c:otherwise>
    <bean:message key="invoice.logo.noFile" />
  </c:otherwise>
</c:choose>
</p>
<p>
<html:form action="/invoice/logoUpload" enctype="multipart/form-data">
    Select File: <html:file property="logoFile"/> <br/>
    <html:submit value="Upload File"/>
</html:form>
</p>
