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

<%@ page language="java"  import="com.sapienter.jbilling.client.util.Constants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>

<logic:notPresent parameter="review">                
   <p class="title"><bean:message key="payment.view.title"/></p>
   <p class="instr">
	    <bean:message key="payment.view.instr" /> <br/>
   	    <bean:message key="all.prompt.help" />
	    <jbilling:help page="payments" anchor="details">
			<bean:message key="all.prompt.here" />
	    </jbilling:help>
   </p>
</logic:notPresent>

<logic:present parameter="review">                
   <p class="title"><bean:message key="payment.review.title"/></p>
   <p class="instr"><bean:message key="payment.review.instr"/></p>
</logic:present>

<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>


<%-- Confirm that the payment needs to be deleted --%>
<logic:present parameter="confirm">
	<logic:equal parameter="confirm" value="do">
		  <p>
		      <bean:message key="payment.delete.confirm"/> <br/>
              <html:link page="/paymentMaintain.do?action=delete&mode=payment">
              	   <bean:message key="all.prompt.yes"/>
              </html:link><br/>
              <html:link page="/payment/list.jsp?confirm=no">
              	   <bean:message key="all.prompt.no"/>
              </html:link><br/>
   	    </p>
	</logic:equal>
	<logic:equal parameter="confirm" value="no">
          <p><bean:message key="payment.delete.notDone"/></p>
	</logic:equal>	
</logic:present>

