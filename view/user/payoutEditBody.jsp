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

<p class="instr"><bean:message key="payout.manual.instr"/></p>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<html:form action="/payout?action=edit" >
  <table class="form">
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="payout.prompt.startDate"/></td>
	 		<jbilling:dateFormat format="mm-dd">
				 <td><html:text property="start_month" size="2" maxlength="2"/></td>
				 <td><html:text property="start_day" size="2" maxlength="2"/></td>
			</jbilling:dateFormat>
	 		<jbilling:dateFormat format="dd-mm">
				 <td><html:text property="start_day" size="2" maxlength="2"/></td>
				 <td><html:text property="start_month" size="2" maxlength="2"/></td>
			</jbilling:dateFormat>
	 		<td><html:text property="start_year" size="4" maxlength="4"/></td>
	 		<td><bean:message key="all.prompt.dateFormat"/></td>
	 	</tr>
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="payout.prompt.endDate"/></td>
	 		<jbilling:dateFormat format="mm-dd">
				 <td><html:text property="end_month" size="2" maxlength="2"/></td>
				 <td><html:text property="end_day" size="2" maxlength="2"/></td>
			</jbilling:dateFormat>
	 		<jbilling:dateFormat format="dd-mm">
				 <td><html:text property="end_day" size="2" maxlength="2"/></td>
				 <td><html:text property="end_month" size="2" maxlength="2"/></td>
			</jbilling:dateFormat>
	 		<td><html:text property="end_year" size="4" maxlength="4"/></td>
	 		<td><bean:message key="all.prompt.dateFormat"/></td>
	 	</tr>
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="payout.prompt.method"/></td>
	 		<td colspan="4">
	 			<html:radio property="method" value="cheque"> 
	 				<bean:message key="payment.type.cheque"/>
 				</html:radio>
	 			<html:radio property="method" value="cc"> 
	 				<bean:message key="payment.type.cc"/>
 				</html:radio>
	 		</td>
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