<html>
<head>
<meta name="layout" content="main" />
</head>
<body>
<p><jB:renderErrorMessages /></p>

<h2>${user?.getContact()?.firstName}&nbsp;${user?.getContact()?.lastName}</h2>
${user?.companyName }
<br/><br/>
<table border=1 cellpadding="0">
<tr><td><g:message code="invoice.label.user.id"/>: ${user?.userId }
<tr><td><g:message code="invoice.label.login.name"/>: ${user?.userName }
<tr><td><g:message code="invoice.label.lifetime.revenue"/>: ${}
<tr><td><g:message code="prompt.customer.note"/>:<BR>
<g:textArea cols="40" rows="5" name="notes" value="${user?.notes}" />
<input type="submit" value="Edit Note" onclick="javascript: window.location='/jbilling/customerInspector/editNote/${user?.userId }';"/>

<hr>

<tr><td><g:message code="invoice.label.id"/></td><td>${invoice.id}</td>
<tr><td><g:message code="invoice.label.number"/></td><td>${invoice.number}</td>
<tr><td><g:message code="invoice.label.status"/></td><td>${invoice.statusDescr}</td>
<tr><td><g:message code="invoice.label.date"/></td><td>${com.sapienter.jbilling.server.util.Util.formatDate(invoice.createDateTime, session["user_id"]) }</td>
<tr><td><g:message code="invoice.label.duedate"/></td><td>${com.sapienter.jbilling.server.util.Util.formatDate(invoice.dueDate, session["user_id"]) }</td>
<tr><td><g:message code="invoice.label.gen.date"/></td><td>${com.sapienter.jbilling.server.util.Util.formatDate(invoice.createTimeStamp, session["user_id"]) }</td>
<tr><td><g:message code="invoice.label.amount"/></td><td>${com.sapienter.jbilling.server.util.Util.formatMoney(new BigDecimal(invoice.total),
					session["user_id"],invoice.currencyId, false)}</td>
<tr><td><g:message code="invoice.label.balance"/></td><td>${com.sapienter.jbilling.server.util.Util.formatMoney(new BigDecimal(invoice.balance),
					session["user_id"],invoice.currencyId, false)}</td>
<tr><td><g:message code="invoice.label.carried.bal"/></td><td>${com.sapienter.jbilling.server.util.Util.formatMoney(new BigDecimal(invoice.balance),
					session["user_id"],invoice.currencyId, false)}</td>
<tr><td><g:message code="invoice.label.currency"/></td><td>${invoice.currencyId}</td>
<tr><td><g:message code="invoice.label.payment.attempts"/></td><td>${invoice.paymentAttempts}</td>
<tr><td><g:message code="invoice.label.orders"/></td><td>${invoice.orders}</td>
<tr><td><g:message code="invoice.label.delegation"/></td><td>${delegatedInvoices}</td>
<tr><td><g:message code="invoice.label.lines"/></td><td>${invoice.invoiceLines}</td>


<tr><td><g:message code="invoice.label.payment.refunds"/></td><td>${invoice.payments}</td>



</body>
</html>