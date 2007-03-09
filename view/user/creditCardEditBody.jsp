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

<logic:notPresent parameter="confirm">
   <p class="title">
	   <bean:message key="creditcard.edit.title" />
   </p>
   <p class="instr">
	   <bean:message key="creditcard.edit.instr" />
   </p>
   
   <html:link page="/user/creditCardEdit.jsp?confirm=do">
   	  <bean:message key="all.prompt.delete" />
   	</html:link>
</logic:notPresent>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<logic:present parameter="confirm">
	<logic:equal parameter="confirm" value="do">
		  <p>
		      <bean:message key="creditcard.delete.confirm"/> <br/>
              <html:link page="/creditCardMaintain.do?action=delete&mode=creditCard">
              	   <bean:message key="all.prompt.yes"/>
              </html:link><br/>
              <html:link page="/user/creditCardEdit.jsp?confirm=no">
              	   <bean:message key="all.prompt.no"/>
              </html:link><br/>
   	    </p>
	</logic:equal>
	
	<logic:equal parameter="confirm" value="no">
          <bean:message key="creditcard.delete.notDone"/>
	</logic:equal>	
</logic:present>

<html:form action="/creditCardMaintain?action=edit&mode=creditCard" >
     <table class="form">
      	  <tr class="form">
	 		  <td class="form_prompt"><bean:message key="payment.cc.number"/></td>
	 		  <td colspan="4"><html:text property="number" size="20" /></td>
	 	  </tr>
      	  <tr class="form">
	 		  <td class="form_prompt"><bean:message key="payment.cc.name"/></td>
	 		  <td colspan="4"><html:text property="name" size="20" /></td>
	 	  </tr>
 	 	  <tr class="form">
	 	  	  <td class="form_prompt"><bean:message key="payment.cc.date"/></td>
	 		  <td><html:text property="expiry_month" size="2" maxlength="2"/></td>
	 		  <td><html:text property="expiry_year" size="4" maxlength="4"/></td>
	 		  <td><bean:message key="all.prompt.ccDateFormat"/></td>
	 	  </tr>
      	  <tr class="form">
	 		  <td class="form_prompt"><bean:message key="creditcard.usethis.prompt"/></td>
	 		  <td colspan="4"><html:checkbox property="chbx_use_this"/></td>
	 	  </tr>
	 	  <tr class="form">
	 	  	 <td class="form_button" colspan="5">
	 	         <html:submit styleClass="form_button">
	 	         	<bean:message key="all.prompt.submit"/>
	 	         </html:submit>
	 	     </td>
	 	  </tr>
	</table>
</html:form>
