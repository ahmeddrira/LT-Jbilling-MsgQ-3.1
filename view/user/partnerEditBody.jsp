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

<p class="title">
<bean:message key="partner.edit.title"/>
</p>
<p class="instr">
<bean:message key="partner.edit.instr"/>
</p>

<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>
<html:errors/>


<jbilling:getOptions generalPeriod="true"/>
<jbilling:getOptions currencies="true"/>


<html:form action="/partnerMaintain?action=edit&mode=partner">
	 <logic:present parameter="create">
	 	<html:hidden property="create" value="yes"/>
	 </logic:present>
	 
	 <table class="form">
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="partner.prompt.balance"/></td>
	 		<td colspan="4"><html:text property="balance" size="10" /></td>
	 	</tr>
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="partner.prompt.rate"/></td>
	 		<td colspan="4"><html:text property="rate" size="5" /></td>
	 	</tr>
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="partner.prompt.fee"/></td>
	 		<td colspan="4"><html:text property="fee" size="5" /></td>
	 	</tr>
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="partner.prompt.feeCurrency"/></td>
	 		<td colspan="4">
	 			<html:select property="fee_currency">
					<html:options collection='<%=Constants.PAGE_CURRENCIES%>' 
								  property="code"
								  labelProperty="description"/>
				</html:select>
            </td>
	 	</tr>
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="partner.prompt.onetime"/></td>
	 		<td colspan="4"><html:checkbox property="chbx_one_time"/></td>
	 	</tr>
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="partner.prompt.period"/></td>
	 		<td ><html:text property="period_value" size="2" /></td>
	 		<td colspan="4">
	 			<html:select property="period_unit_id">
					<html:options collection='<%=Constants.PAGE_GENERAL_PERIODS%>' 
								  property="code"
								  labelProperty="description"/>
				</html:select>
            </td>
	 	</tr>
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="partner.prompt.nextPayout"/></td>
	 		<jbilling:dateFormat format="mm-dd">
				 <td><html:text property="payout_month" size="2" maxlength="2"/></td>
				 <td><html:text property="payout_day" size="2" maxlength="2"/></td>
			</jbilling:dateFormat>
	 		<jbilling:dateFormat format="dd-mm">
				 <td><html:text property="payout_day" size="2" maxlength="2"/></td>
				 <td><html:text property="payout_month" size="2" maxlength="2"/></td>
			</jbilling:dateFormat>
	 		<td><html:text property="payout_year" size="4" maxlength="4"/></td>
	 		<td><bean:message key="all.prompt.dateFormat"/></td>
	 	</tr>
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="partner.prompt.process"/></td>
	 		<td colspan="4"><html:checkbox property="chbx_process"/></td>
	 	</tr>
	 	<tr class="form">
	 		<td class="form_prompt"><bean:message key="partner.prompt.clerk"/></td>
	 		<td colspan="4"><html:text property="clerk" size="5" /></td>
	 	</tr>
	 	<tr class="form">
	 		<td class="form_button" colspan="5">
              	<html:submit property="ranges" styleClass="form_button">
              		<bean:message key="partner.button.ranges"/>
              	</html:submit>
            </td>
		</tr>	 
	 	<tr class="form">
	 		<td class="form_button" colspan="5">
              	<html:submit property="submit" styleClass="form_button">
              		<bean:message key="all.prompt.submit"/>
              	</html:submit>
            </td>
		</tr>	 
 	</table>
</html:form>
