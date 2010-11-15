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
<tr><td>Login Name: ${user?.userName}<td>Number: ${user?.userId }<td>&nbsp;
<tr><td>Phone: ${user?.contact?.phoneCountryCode}<td>Type: ${user?.role }<td>&nbsp;
<tr><td>Email: ${user?.contact?.email}<td>Balance: ${user?.owingBalance }<td>&nbsp;
<tr><td>Status: ${user?.status}<td>&nbsp;<td>&nbsp;
<tr><td>Subscriber status: <td>
	<g:subscriberStatus name="subscriberStatusId" 
		value="${user?.subscriberStatusId}" languageId="${languageId?.toString()}"/>
	<td>&nbsp;
<tr><td>Next Invoice Date: ${}<td>&nbsp;<td>&nbsp;
<tr><td colspan="3">
<g:actionSubmit  action="invoices"  value="View Invoices" class=""/>
<g:actionSubmit  action="payments"  value="View Payments" class=""/>
<g:actionSubmit  action="orders"  value="View Orders" class=""/>
<tr><td colspan="3"><g:actionSubmit  action="editCustomer"  value="Edit Customer" class=""/>
<g:actionSubmit  action="makePayment"  value="Make Payment" class=""/>
<g:actionSubmit  action="createOrder"  value="Create Order" class=""/>
<tr><td colspan="3">Address
<tr><td colspan="3">&nbsp;
<tr><td colspan="3">Address: ${user?.contact?.address1}&nbsp;${user?.contact?.address2}
<tr><td>City: ${user?.contact?.city}<td>State/Province: ${user?.contact?.stateProvince}<td>Postal Code: ${user?.contact?.postalCode}
<tr><td colspan="3">Country: ${user?.contact?.countryCode} 
<tr><td colspan="3">Extra Contact
<tr><td colspan="3">&nbsp;
<tr><td colspan="3">Address:
<tr><td>City: <td>State/Province:<td>Postal Code:
<tr><td colspan="3">Country: 

<tr><td colspan="3">Notes
<tr><td colspan="3">&nbsp;
<tr><td colspan="3"><g:textArea cols="20" rows="10" name="notes" value="${user.notes}" /> 

<tr><td colspan="3">Subscriptions
<tr><td colspan="3">&nbsp;
<tr><td colspan="3">
<table border=1>
<thead><tr><th>Order Id</th><th>Description</th><th>Period</th><th>Qty</th><th>Price</th><th>Total</th></tr></thead>
<tbody>
<g:each var="ordr" in="${orders}">
	<g:each var="ln" in="${ordr.orderLines}">
		<tr><td>${ordr.id}</td><td>${ln?.description}</td>
			<td>${ordr.periodStr}</td><td>${ln.quantity}</td><td>${ln.price}</td>
			<td>${ordr.total}</td></tr>
	</g:each>
</g:each>
</tbody>
</table>
&nbsp;
<tr><td colspan="3">Credit Card
<tr><td colspan="3">&nbsp;
<tr><td colspan="3">Credit Card#: ${user?.creditCard?.number}
<tr><td>Name on Card: <td>${user?.creditCard?.name}<td>&nbsp;
<tr><td>Expiry Date: <td>${expDate }<td>&nbsp;
<tr><td>Used for Automatic Payment: <td>${isAutoCC?"Yes":"No"}<td>&nbsp;
<tr><td colspan="3">&nbsp;
<tr><td colspan="3">ACH
<tr><td colspan="3">&nbsp;
<tr><td colspan="3">ABA Routing Number#: ${user?.ach?.abaRouting}
<tr><td>Bank Account Number: <td>${user?.ach?.bankAccount}<td>&nbsp;
<tr><td>Name on Customer Account: <td>${user?.ach?.accountName}<td>&nbsp;
<tr><td>Account Type: <td><g:radioGroup name="ach.accountType"
								value="${user?.ach?.accountType}"
								labels="['Checking','Savings']" values="[1,2]">
								${it.label}
								${it.radio}
							</g:radioGroup><td>&nbsp;
<tr><td>Used for Automatic Payment: <td>${isAutoAch?"Yes":"No"}<td>&nbsp;

</table>
</g:form>
<script language="javascript">
document.getElementById('subscriberStatusId').disabled=true;
</script>
</body>
</html>