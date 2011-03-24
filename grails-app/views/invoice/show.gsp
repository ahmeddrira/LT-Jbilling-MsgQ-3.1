<%@ page import="com.sapienter.jbilling.server.payment.db.PaymentResultDTO" %>
<%@ page import="com.sapienter.jbilling.server.payment.db.PaymentMethodDTO" %>
<html>
<head>
<meta name="layout" content="main" />
</head>
<body>
<p><jB:renderErrorMessages /></p>

<h2>${user?.getContact()?.firstName}&nbsp;${user?.getContact()?.lastName}</h2>
${user?.companyName }
<br/><br/>

<g:set var="currency" value="${currencies.find{ it.id == invoice?.currencyId}}"/>

<table border=1 cellpadding="0">
<tr><td><g:message code="invoice.label.user.id"/>: ${user?.userId }
<tr><td><g:message code="invoice.label.login.name"/>: ${user?.userName }
<tr><td><g:message code="invoice.label.lifetime.revenue"/>: 
    <g:formatNumber number="${totalRevenue}" type="currency" currencySymbol="${currency?.symbol}"/>
    
<tr><td><g:message code="prompt.customer.note"/>:<BR>
<g:textArea cols="40" rows="5" name="notes" value="${user?.notes}" />
<input type="submit" value="Edit Note" onclick="javascript: window.location='/jbilling/customerInspector/editNote/${user?.userId }';"/>

<hr>
<table>
<tr><td><g:message code="invoice.label.id"/></td><td>${invoice.id}</td>
<tr><td><g:message code="invoice.label.number"/></td><td>${invoice.number}</td>
<tr><td><g:message code="invoice.label.status"/></td><td>${invoice.statusDescr}</td>
<tr><td><g:message code="invoice.label.date"/></td>
    <td><g:formatDate date="${invoice?.createDateTime}" formatName="date.pretty.format"/></td>
<tr><td><g:message code="invoice.label.duedate"/></td>
    <td><g:formatDate date="${invoice?.dueDate}" formatName="date.pretty.format"/></td>
<tr><td><g:message code="invoice.label.gen.date"/></td>
    <td><g:formatDate date="${invoice?.createTimeStamp}" formatName="date.pretty.format"/></td>
<tr><td><g:message code="invoice.label.amount"/></td>
    
    <td>
        <g:formatNumber number="${invoice?.total}" type="currency" currencySymbol="${currency?.symbol}"/>
    </td>
<tr><td><g:message code="invoice.label.balance"/></td>
    <td>
        <g:formatNumber number="${invoice?.balance}" type="currency" currencySymbol="${currency?.symbol}"/>
    </td>
<tr><td><g:message code="invoice.label.carried.bal"/></td>
    <td>
        <g:formatNumber number="${invoice?.carriedBalance}" type="currency" currencySymbol="${currency?.symbol}"/>
	</td>
<tr><td><g:message code="invoice.label.currency"/></td>
    <td>${currency?.symbol}</td>
<tr><td><g:message code="invoice.label.payment.attempts"/></td>
    <td>${invoice?.paymentAttempts}</td>
    
<tr><td><g:message code="invoice.label.orders"/></td>
<td><g:each var="order" in="${invoice?.orders}">
	${order.toString()}&nbsp;
</g:each>
</td>
<tr><td><g:message code="invoice.label.delegation"/></td><td>${delegatedInvoices}</td>
</table>

<tr><td><h3><g:message code="invoice.label.lines"/></h3>
<table><thead>
<tr><td><g:message code="label.gui.description"/></td>
	<td><g:message code="label.gui.quantity"/></td>
	<td><g:message code="label.gui.price"/></td>
	<td><g:message code="label.gui.amount"/></td>
</tr></thead><tbody>
<g:each var="line" in="${invoice.invoiceLines}" status="idx">
	<tr><td>${line.description}</td>
		<td>${(int)line.quantity}</td>
		<td>
            <g:formatNumber number="${line?.price}" type="currency" currencySymbol="${currency?.symbol}"/>
	    </td>
		<td>
            <g:formatNumber number="${line?.amount}" type="currency" currencySymbol="${currency?.symbol}"/>
        </td>
	</tr>
</g:each>
</tbody></table>

<tr><td><h3><g:message code="invoice.label.payment.refunds"/></h3>
<table><thead>
<tr><td><g:message code="label.gui.date"/></td>
	<td><g:message code="label.gui.payment.refunds"/></td>
	<td><g:message code="label.gui.amount"/></td>
	<td><g:message code="label.gui.method"/></td>
	<td><g:message code="label.gui.result"/></td>
	<td>.</td>
</tr></thead><tbody>
<g:each var="payment" in="${payments}" status="idx">
	<tr><td>
            <g:formatDate date="${payment?.paymentDate}" formatName="date.pretty.format"/>
        </td>
		<td>${payment.isRefund?"R":"P"}</td>
		<td>
            <g:formatNumber number="${payment?.amount}" type="currency" currencySymbol="${currency?.symbol}"/>
	    </td>
		<td>${new PaymentMethodDTO(payment?.paymentMethodId).getDescription(session['language_id'])}</td>
		<td>${new PaymentResultDTO(payment?.resultId).getDescription(session['language_id'])}</td>
		<td>*</td>
	</tr>
</g:each>
</tbody></table>

<hr></hr>

<h3><g:message code="invoice.label.note"/></h3>
${invoice?.customerNotes }
</body>
</html>