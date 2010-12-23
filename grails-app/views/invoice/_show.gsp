<%@ page import="com.sapienter.jbilling.server.util.Util" %>
<%@ page import="com.sapienter.jbilling.server.payment.db.PaymentResultDTO" %>
<%@ page import="com.sapienter.jbilling.server.payment.db.PaymentMethodDTO" %>

<div class="column-hold">

<div class="heading">
<strong><g:message code="invoice.label.details"/></strong>
</div>

<!-- Invoice details -->
<div class="box">

	<strong>${user?.contact?.firstName?:'<firstName>'}
		&nbsp;${user?.contact?.lastName?:'<lastName>'}
		<br/>
		<em>${user?.companyName}</em>
	</strong>

	<table class="dataTable">
		<tr><td><g:message code="invoice.label.user.id"/>:</td><td class="value">${user?.userId }</td></tr>
		<tr><td><g:message code="invoice.label.login.name"/>:</td><td class="value">${user?.userName}</td></tr>
		<tr><td><g:message code="invoice.label.lifetime.revenue"/>:</td>
		<td class="value">
			${Util.formatMoney((totalRevenue?: new BigDecimal("0.0")),session["user_id"],invoice?.currencyId, false)}
		</td></tr>
	</table>
	<table class="dataTable"  width="50%">
		<tr><td><g:message code="prompt.customer.note"/>:</td></tr>
		<tr><td>${user?.notes}</td></tr>
	</table>
	<hr>
	<table class="dataTable">
		<tr><td><g:message code="invoice.label.id"/>:</td><td class="value">${invoice.id}</td></tr>
		<tr><td><g:message code="invoice.label.number"/>:</td><td class="value">${invoice.number}</td></tr>
		<tr><td><g:message code="invoice.label.status"/>:</td><td class="value">${invoice.statusDescr}</td></tr>
		<tr><td><g:message code="invoice.label.date"/>:</td><td class="value">${Util.formatDate(invoice.createDateTime, session["user_id"])}</td></tr>
		<tr><td><g:message code="invoice.label.duedate"/>:</td><td class="value">${Util.formatDate(invoice.dueDate, session["user_id"])}</td></tr>
		<tr><td><g:message code="invoice.label.gen.date"/>:</td><td class="value">${Util.formatDate(invoice.createTimeStamp, session["user_id"])}</td></tr>
		<tr><td><g:message code="invoice.label.amount"/>:</td><td class="value">${Util.formatMoney(new BigDecimal(invoice.total?: "0.0"),
							session["user_id"],invoice?.currencyId, false)}</td></tr>
		<tr><td><g:message code="invoice.label.balance"/>:</td><td class="value">${Util.formatMoney(new BigDecimal(invoice.balance ?: "0.0"),
							session["user_id"],invoice?.currencyId, false)}</td></tr>
		<tr><td><g:message code="invoice.label.carried.bal"/>:</td><td class="value">${Util.formatMoney(new BigDecimal(invoice.balance ?: "0.0"),
							session["user_id"],invoice?.currencyId, false)}</td></tr>
		<tr><td><g:message code="invoice.label.currency"/>:</td><td class="value">${invoice?.currencyId}</td></tr>
		<tr><td><g:message code="invoice.label.payment.attempts"/>:</td><td class="value">${invoice.paymentAttempts}</td></tr>
		<tr><td><g:message code="invoice.label.orders"/>:</td><td class="value"><g:each var="order" in="${invoice.orders}">
			${order.toString()}&nbsp;
		</g:each></td></tr>
		<tr><td><g:message code="invoice.label.delegation"/>:</td><td class="value">${delegatedInvoices}</td></tr>
	</table>
</div>

	<div class="heading">
		<strong><g:message code="invoice.label.lines"/></strong>
	</div>
	
	<table class="innerTable" >
		<thead class="innerHeader">
	         <tr>
	            <th align="left"><g:message code="label.gui.description"/></th>
	            <th align="left"><g:message code="label.gui.quantity"/></th>
	            <th align="left"><g:message code="label.gui.price"/></th>
	            <th align="left"><g:message code="label.gui.amount"/></th>
	         </tr>
         </thead>
         <tbody>
		     <g:each var="line" in="${invoice.invoiceLines}" status="idx">
		         <tr>
		            <td align="left" class="innerContent">${line.description}</td>
		            <td align="left" class="innerContent">${(int)line.quantity}</td>
		            <td align="left" class="innerContent">${Util.formatMoney(new BigDecimal(line.price?:"0.0"),
							session["user_id"],invoice?.currencyId, false)}</td>
		            <td align="left" class="innerContent">${Util.formatMoney(new BigDecimal(line.amount?: "0.0"),
							session["user_id"],invoice?.currencyId, false)}</td>
		         </tr>
	         </g:each>
         </tbody>
    </table>

	<br/>

	<div class="btn-box">
		<a onclick="" class="submit"><span><g:message code="button.invoice.pay"/></span></a>
		<!-- <a onclick="" class="submit"><span><g:message code="button.invoice.sendEmail"/></span></a> --> 
		<a href="${createLink (action: 'downloadPdf', id: invoice.id)}" class="submit">
			<span><g:message code="button.invoice.downloadPdf"/></span>
		</a>
	</div>
	<br/>
	
	<div class="heading">
		<strong><g:message code="invoice.label.payment.refunds"/></strong>
	</div>
	
	<table class="innerTable" >
		<thead class="innerHeader">
	         <tr>
	            <th align="left"><g:message code="label.gui.date"/></th>
				<th align="left"><g:message code="label.gui.payment.refunds"/></th>
				<th align="left"><g:message code="label.gui.amount"/></th>
				<th align="left"><g:message code="label.gui.method"/></th>
				<th align="left"><g:message code="label.gui.result"/></th>
				<th align="left">.</th>
	         </tr>
         </thead>
         <tbody>
		     <g:each var="payment" in="${payments}" status="idx">
		         <tr>
		         	<td align="left" class="innerContent">${Util.formatDate(payment.paymentDate, session["user_id"])}</td>
					<td align="left" class="innerContent">${payment.isRefund?"R":"P"}</td>
					<td align="left" class="innerContent">${Util.formatMoney(new BigDecimal(payment.amount?:"0.0"),
									session["user_id"],invoice?.currencyId, false)}</td>
					<td align="left" class="innerContent">${new PaymentMethodDTO(payment?.paymentMethodId).getDescription(languageId)}</td>
					<td align="left" class="innerContent">${new PaymentResultDTO(payment?.resultId).getDescription(languageId)}</td>
					<td align="left" class="innerContent">
						<a href="${createLink (action: 'removePaymentLink', id: invoice.id, params:['paymentId': payment.id])}" class="submit">
							<span>*</span>
						</a>
					</td>
		         </tr>
	         </g:each>
         </tbody>
    </table>
	
	<hr/>
	
	<div class="heading">
		<strong><g:message code="invoice.label.note"/></strong>
	</div>
	<br/>
	${invoice.customerNotes }

<div class="btn-box">
    <g:if test="${invoice.id}">
        <a onclick="showConfirm(${invoice.id});" class="submit delete"><span><g:message code="button.delete.invoice"/></span></a>
    </g:if>
</div>

<g:render template="/confirm"
          model="['message':'invoice.prompt.are.you.sure',
                  'controller':'invoice',
                  'action':'delete',
                  'id':invoice.id,
                 ]"/>
</div>
