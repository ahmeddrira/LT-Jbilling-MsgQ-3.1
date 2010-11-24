<html>
<head>
</head>
<body>
<h2 align="center"><g:message code="prompt.customer.inspector" /></h2>
<p><jB:renderErrorMessages /></p>
<g:form>
<g:hiddenField name="_id" value="${_id}"/>
${user?.getContact()?.firstName}&nbsp;${user?.getContact()?.lastName} <br/> 
${user?.companyName }<br/><br/>
<table border=1 cellpadding="0">
<tr><td><g:message code="prompt.login.name"/>: ${user?.userName}<td>Number: ${user?.userId }<td>&nbsp;
<tr><td><g:message code="prompt.phone.number"/>: ${user?.contact?.phoneCountryCode}<td>Type: ${user?.role }<td>&nbsp;
<tr><td><g:message code="prompt.email.address"/>: ${user?.contact?.email}<td>Balance: ${user?.owingBalance }<td>&nbsp;
<tr><td><g:message code="prompt.user.status"/>: ${user?.status}<td>&nbsp;<td>&nbsp;
<tr><td><g:message code="prompt.user.subscriber.status"/>: <td>${subscribStatus}<td>&nbsp;
<tr><td><g:message code="prompt.next.invoice.date"/>: <g:if test='${null != user?.nextInvoiceDate}'>
								${user?.nextInvoiceDate}
						   </g:if>
						   <g:else>
						   		<g:message code="prompt.no.active.orders"/>
						   </g:else>
							<td>&nbsp;<td>&nbsp;
<tr><td colspan="3">
<g:actionSubmit  action="invoices"  value="View Invoices" class=""/>
<g:actionSubmit  action="payments"  value="View Payments" class=""/>
<g:actionSubmit  action="orders"  value="View Orders" class=""/>
<tr><td colspan="3"><g:actionSubmit  action="editCustomer"  value="Edit Customer" class=""/>
<g:actionSubmit  action="makePayment"  value="Make Payment" class=""/>
<g:actionSubmit  action="createOrder"  value="Create Order" class=""/>
<tr><td colspan="3"><g:message code="prompt.address"/>
<tr><td colspan="3">&nbsp;
<tr><td colspan="3"><g:message code="prompt.address"/>: ${user?.contact?.address1}&nbsp;${user?.contact?.address2}
<tr><td><g:message code="prompt.city"/>: ${user?.contact?.city}
	<td><g:message code="prompt.state"/>: ${user?.contact?.stateProvince}<td>Postal Code: ${user?.contact?.postalCode}
<tr><td colspan="3"><g:message code="prompt.country"/>: ${user?.contact?.countryCode} 
<g:each var="ccf" status="idx" in="${user?.contact?.fieldNames}">
	<tr><td colspan="3"><g:message code="${user?.contact?.fieldNames[idx]}"/>: ${user?.contact?.fieldValues[idx]} 
</g:each>
<tr><td colspan="3">&nbsp;
<g:each var="contact" in="${contacts}">
		<tr><td colspan="3">${contact?.contactTypeDescr}
		<tr><td colspan="3">&nbsp;
		<tr><td colspan="3"><g:message code="prompt.address"/>: ${contact?.address1}&nbsp;${contact?.address2}
		<tr><td><g:message code="prompt.city"/>: ${contact?.city}
			<td><g:message code="prompt.state"/>: ${contact?.stateProvince}
			<td><g:message code="prompt.zip"/>: ${contact?.postalCode}
		<tr><td colspan="3"><g:message code="prompt.country"/>: ${contact?.countryCode} 
		<g:each var="ccf" status="idx2" in="${contact?.fieldIDs}">
			<tr><td colspan="3"><g:message code="${contact?.fieldNames[idx2]}"/>: ${contact?.fieldValues[idx2]}
		</g:each>
</g:each>

<tr><td colspan="3">&nbsp;
<tr><td colspan="3"><g:message code="prompt.notes"/>
<tr><td colspan="3">&nbsp;
<tr><td colspan="3"><g:textArea cols="20" rows="10" name="notes" value="${user?.notes}" /> 

<tr><td colspan="3"><g:message code="prompt.subscriptions"/>
<tr><td colspan="3">&nbsp;
<tr><td colspan="3">
<table border=1>
<thead><tr><th><g:message code="prompt.order.id"/></th><th><g:message code="prompt.order.description"/></th>
			<th><g:message code="prompt.order.period"/></th><th><g:message code="prompt.order.qty"/></th>
			<th><g:message code="prompt.order.price"/></th><th><g:message code="prompt.order.total"/></th>
		</tr></thead>
<tbody>
<g:each var="ordr" in="${orders}">
	<g:each var="ln" in="${ordr?.orderLines}">
		<tr><td>${ordr?.id}</td><td>${ln?.description}</td>
			<td>${ordr?.periodStr}</td><td>${ln?.quantity}</td><td>${ln?.price}</td>
			<td>${ordr?.total}</td></tr>
	</g:each>
</g:each>
</tbody>
</table>
&nbsp;
<tr><td colspan="3"><g:message code="prompt.credit.card"/>
<tr><td colspan="3">&nbsp;
<tr><td colspan="3"><g:message code="prompt.credit.card.number"/>: ${user?.creditCard?.number}
<tr><td><g:message code="prompt.name.on.card"/>: <td>${user?.creditCard?.name}<td>&nbsp;
<tr><td><g:message code="prompt.expiry.date"/>: <td>${expDate }<td>&nbsp;
<tr><td><g:message code="prompt.cc.used.auto.payment"/>: <td>${isAutoCC?"Yes":"No"}<td>&nbsp;
<tr><td colspan="3">&nbsp;
<tr><td colspan="3"><g:message code="prompt.ach"/>
<tr><td colspan="3">&nbsp;
<tr><td colspan="3"><g:message code="prompt.aba.routing.num"/>: ${user?.ach?.abaRouting}
<tr><td><g:message code="prompt.bank.acc.num"/>: <td>${user?.ach?.bankAccount}<td>&nbsp;
<tr><td><g:message code="prompt.name.customer.account"/>: <td>${user?.ach?.accountName}<td>&nbsp;
<tr><td><g:message code="prompt.account.type"/>: 
	<td><g:radioGroup name="ach.accountType"
					value="${user?.ach?.accountType}"
					labels="['Checking','Savings']" values="[1,2]">
						${it.label}
						${it.radio}
		</g:radioGroup><td>&nbsp;
<tr><td><g:message code="prompt.ach.used.auto.payment"/>: <td>${isAutoAch?"Yes":"No"}<td>&nbsp;

</table>
</g:form>
</body>
</html>