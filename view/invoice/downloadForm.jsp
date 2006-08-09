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

<%@ page language="java"
	import="com.sapienter.jbilling.client.util.Constants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling"%>

<p class="title"><bean:message key="invoice.download.title" /></p>
<p class="instr"><bean:message key="invoice.download.instr" /></p>


<html:errors />
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage" /></p>
</html:messages>

<html:form action="/download">
	<html:hidden property="operationType" />
	<table class="form">
		<tr class="form">
			<td class="form_prompt"><bean:message
				key="invoice.download.prompt.customer" /></td>
			<td><html:text property="customer" size="6" /></td>
			<td class="form_button"><html:submit styleClass="form_button"
				onclick="setOperationType(1);">
				<bean:message key="invoice.download.label.generate" />
			</html:submit></td>
		</tr>
		<tr class="form">
			<td class="form_prompt"><bean:message
				key="invoice.download.prompt.process" /></td>
			<td><html:text property="process" size="6" /></td>
			<td class="form_button"><html:submit styleClass="form_button"
				onclick="setOperationType(3);">
				<bean:message key="invoice.download.label.generate" />
			</html:submit></td>
		</tr>
		<tr class="form">
			<td class="form_prompt"><bean:message
				key="invoice.download.prompt.range" /></td>
			<td>
			<table class="form">
				<tr class="form">
					<td class="form_prompt"><bean:message
						key="invoice.download.label.start" /></td>
					<td><html:text property="rangeStart" size="6" /></td>
					<td class="form_prompt"><bean:message
						key="invoice.download.label.end" /></td>
					<td><html:text property="rangeEnd" size="6" /></td>
				</tr>
			</table>
			</td>
			<td class="form_button"><html:submit styleClass="form_button"
				onclick="setOperationType(2);">
				<bean:message key="invoice.download.label.generate" />
			</html:submit></td>
		</tr>
		<tr class="form">
			<td class="form_prompt"><bean:message
				key="invoice.download.prompt.date" /></td>
			<td>
			<table>
				<tr class="form">
					<td class="form_prompt"><bean:message
						key="invoice.download.label.start" /></td>
					<jbilling:dateFormat format="mm-dd">
						<td><html:text property="date_from_month" size="2" maxlength="2" /></td>
						<td><html:text property="date_from_day" size="2" maxlength="2" /></td>
					</jbilling:dateFormat>
					<jbilling:dateFormat format="dd-mm">
						<td><html:text property="date_from_day" size="2" maxlength="2" /></td>
						<td><html:text property="date_from_month" size="2" maxlength="2" /></td>
					</jbilling:dateFormat>
					<td><html:text property="date_from_year" size="4" maxlength="4" /></td>
					<td><bean:message key="all.prompt.dateFormat" /></td>
				</tr>
				<tr class="form">
					<td class="form_prompt"><bean:message
						key="invoice.download.label.end" /></td>
					<jbilling:dateFormat format="mm-dd">
						<td><html:text property="date_to_month" size="2" maxlength="2" /></td>
						<td><html:text property="date_to_day" size="2" maxlength="2" /></td>
					</jbilling:dateFormat>
					<jbilling:dateFormat format="dd-mm">
						<td><html:text property="date_to_day" size="2" maxlength="2" /></td>
						<td><html:text property="date_to_month" size="2" maxlength="2" /></td>
					</jbilling:dateFormat>
					<td><html:text property="date_to_year" size="4" maxlength="4" /></td>
					<td><bean:message key="all.prompt.dateFormat" /></td>
				</tr>
			</table>
			</td>
			<td class="form_button"><html:submit styleClass="form_button"
				onclick="setOperationType(4);">
				<bean:message key="invoice.download.label.generate" />
			</html:submit></td>
		</tr>
		<tr class="form">
			<td class="form_prompt"><bean:message
				key="invoice.download.prompt.number" /></td>
			<td>
			<table class="form">
				<tr class="form">
					<td class="form_prompt"><bean:message
						key="invoice.download.label.start" /></td>
					<td><html:text property="number_from" size="15" /></td>
                </tr>
				<tr class="form">
					<td class="form_prompt"><bean:message
						key="invoice.download.label.end" /></td>
					<td><html:text property="number_to" size="15" /></td>
				</tr>
			</table>
			</td>
			<td class="form_button"><html:submit styleClass="form_button"
				onclick="setOperationType(5);">
				<bean:message key="invoice.download.label.generate" />
			</html:submit></td>
		</tr>

	</table>
</html:form>

<script type="text/javascript">
	function setOperationType(val) {		
		document.downloadInvoices.operationType.value = val;
	}
</script>
