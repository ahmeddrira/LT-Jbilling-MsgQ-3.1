<%@ page import="com.sapienter.jbilling.server.util.Util" %>
<%@ page import="com.sapienter.jbilling.server.payment.db.PaymentResultDTO" %>
<%@ page import="com.sapienter.jbilling.server.payment.db.PaymentMethodDTO" %>

<div class="column-hold">

	<div class="heading">
		<strong><g:message code="invoice.label.details"/> <em>${invoice?.id}</em>
		</strong>
	</div>

	<!-- Invoice details -->
	<div class="box">
		<table class="dataTable">
            <tr><td><strong>
                    <g:if test="${user?.contact?.firstName || user?.contact?.lastName}">
                        ${user?.contact?.firstName}&nbsp;${user?.contact?.lastName}
                    </g:if>
                    <g:else>
                        ${user?.userName}
                    </g:else>
                </strong><br>
                <em>${user?.contact?.organizationName}</em>
            </td></tr>
            <tr><td><g:message code="invoice.label.user.id"/>:</td><td class="value">
                <g:link controller="user" action="list" id="${user?.id}">
                    ${user?.id}
                </g:link>
            </td></tr>
            <tr><td><g:message code="invoice.label.user.name"/>:</td>
                <td class="value">${user?.userName}</td>
            </tr>
        </table>
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
	
			<tr><td><g:message code="invoice.label.payment.attempts"/>:</td><td class="value">${invoice.paymentAttempts}</td></tr>
			<tr><td><g:message code="invoice.label.orders"/>:</td><td class="value">
                <g:each var="order" in="${invoice.orders}">
                    <g:remoteLink breadcrumb="id" controller="order" action="show" id="${order}" params="['template': 'order']"
                                before="register(this);" onSuccess="render(data, next);">
                        ${order.toString()}
                     </g:remoteLink>&nbsp;
			    </g:each></td></tr>
			<tr><td><g:message code="invoice.label.delegation"/>:</td><td class="value">${delegatedInvoices}</td></tr>
		</table>
	</div>
	
	<!-- Invoice Lines Info -->
	<div class="heading">
		<strong><g:message code="invoice.label.lines"/></strong>
	</div>
	
	<div class="box">
		<table class="innerTable" >
			<thead class="innerHeader">
		         <tr>
		            <th><g:message code="label.gui.description"/></th>
		            <th><g:message code="label.gui.quantity"/></th>
		            <th><g:message code="label.gui.price"/></th>
		            <th><g:message code="label.gui.amount"/></th>
		         </tr>
	         </thead>
	         <tbody>
			     <g:each var="line" in="${invoice.invoiceLines}" status="idx">
			         <tr>
			            <td style="text-align: left;" class="innerContent">${line.description}</td>
			            <td class="innerContent">${(int)line.quantity}</td>
			            <td class="innerContent">${Util.formatMoney(new BigDecimal(line.price?:"0.0"),
								session["user_id"],invoice?.currencyId, false)}</td>
			            <td class="innerContent">${Util.formatMoney(new BigDecimal(line.amount?: "0.0"),
								session["user_id"],invoice?.currencyId, false)}</td>
			         </tr>
		         </g:each>
	         </tbody>
	    </table>
	</div>

	<div class="btn-box">
        <div class="row">
    		<a href="${createLink (controller: 'payment', action: 'edit', params: [userId: user?.id, invoiceId: invoice.id])}" class="submit payment">
                <span><g:message code="button.invoice.pay"/></span></a>
    		<a href="${createLink (action: 'downloadPdf', id: invoice.id)}" class="submit save">
    			<span><g:message code="button.invoice.downloadPdf"/></span></a>
        </div>
        <div class="row">
            <a href="${createLink (action: 'notifyInvoiceByEmail', id: invoice.id)}" class="submit emailinvoice">
                <span><g:message code="button.invoice.sendEmail"/></span></a>
        </div>
	</div>

	<!-- Payments & Refunds Info -->
	<div class="heading">
		<strong><g:message code="invoice.label.payment.refunds"/></strong>
	</div>
	
	<div class="box">
		<g:if test="${payments}">
			<g:hiddenField name="unlink_payment_id" value="-1"/>
			<table class="innerTable" >
				<thead class="innerHeader">
			         <tr>
                        <th><g:message code="label.gui.payment.id"/></th>
			            <th><g:message code="label.gui.date"/></th>
						<th><g:message code="label.gui.payment.refunds"/></th>
						<th><g:message code="label.gui.amount"/></th>
						<th><g:message code="label.gui.method"/></th>
						<th><g:message code="label.gui.result"/></th>
						<th></th>
			         </tr>
		         </thead>
		         <tbody>
				     <g:each var="payment" in="${payments}" status="idx">
				         <tr>
                            <td class="innerContent">
                                <g:remoteLink breadcrumb="id" controller="payment" action="show" id="${payment.id}" params="['template': 'show']"
                                    before="register(this);" onSuccess="render(data, next);">${payment.id}
                                </g:remoteLink>
                            </td>
				         	<td class="innerContent">${Util.formatDate(payment.paymentDate, session["user_id"])}</td>
							<td class="innerContent">${payment.isRefund?"R":"P"}</td>
							<td class="innerContent">${Util.formatMoney(new BigDecimal(payment.amount?:"0.0"),
								session["user_id"],invoice?.currencyId, false)}</td>
							<td class="innerContent">${new PaymentMethodDTO(payment?.paymentMethodId).getDescription(session['language_id'])}</td>
							<td class="innerContent">${new PaymentResultDTO(payment?.resultId).getDescription(session['language_id'])}</td>
							<td class="innerContent">
								<a href="javascript:void(0);" onclick="setUnlinkPaymentId(${invoice.id}, ${payment.id});">
								<span><g:message code="invoice.prompt.unlink.payment"/></span></a>
							</td>
				         </tr>
			         </g:each>
		         </tbody>
		    </table>
		</g:if>
		<g:else>
			<em><g:message code="invoice.prompt.no.payments.refunds"/></em>
		</g:else>
	</div>

	<!-- Invoice Notes -->
	<div class="heading">
		<strong><g:message code="invoice.label.note"/></strong>
	</div>
	<div class="box">
		<table class="dataTable">
			<tr><td>${invoice.customerNotes}</td></tr>
		</table>
	</div>
	
	<div class="btn-box">
	    <g:if test="${invoice.id}">
	        <a onclick="showConfirm('delete-'+${invoice.id});" class="submit delete">
	        	<span><g:message code="button.delete.invoice"/></span>
	        </a>
	    </g:if>
	</div>
</div>

<script type="text/javascript">
function setUnlinkPaymentId(invId, pymId) {
	$('#unlink_payment_id').val(pymId);
	showConfirm("removePaymentLink-" + invId);
	return true;
}
function setPaymentId() {
    $('#confirm-command-form-removePaymentLink-${invoice.id} [name=paymentId]').val($('#unlink_payment_id').val());
}
</script>
<g:render template="/confirm"
        model="['message':'invoice.prompt.confirm.remove.payment.link',
                'controller':'invoice',
                'action':'removePaymentLink',
                'id':invoice.id,
                'formParams': ['paymentId': '-1'],
                'onYes': 'setPaymentId()',
               ]"/>

<g:render template="/confirm"
         model="['message':'invoice.prompt.are.you.sure',
                 'controller':'invoice',
                 'action':'delete',
                 'id':invoice.id,
                ]"/>
