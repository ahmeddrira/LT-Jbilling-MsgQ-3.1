<%@ page import="com.sapienter.jbilling.server.util.Util" %>
<%@ page import="com.sapienter.jbilling.server.payment.db.PaymentResultDTO" %>
<%@ page import="com.sapienter.jbilling.server.payment.db.PaymentMethodDTO" %>

<div class="column-hold">

<!-- Invoice details -->
<div class="box">

	<strong>${user?.contact?.firstName?:'<firstName>'}
		&nbsp;${user?.contact?.lastName?:'<lastName>'}
		<br/>
		<em>${user?.companyName}</em>
	</strong>

	<dl class="other">
		<dt><g:message code="invoice.label.user.id"/>:</dt><dd>${user?.userId }</dd>
		<dt><g:message code="invoice.label.login.name"/>:</dt><dd>${user?.userName}</dd>
		<dt><g:message code="invoice.label.lifetime.revenue"/>:</dt>
		<dd>
			${Util.formatMoney((totalRevenue?: new BigDecimal("0.0")),session["user_id"],invoice?.currencyId, false)}
		</dd>
		<dt><g:message code="prompt.customer.note"/>:</dt>
		<dd><br/>
			${user?.notes}
		</dd>
		<hr>
		<dl>
		<dt><g:message code="invoice.label.id"/></dt><dd>${invoice.id}</dd>
		<dt><g:message code="invoice.label.number"/></dt><dd>${invoice.number}</dd>
		<dt><g:message code="invoice.label.status"/></dt><dd>${invoice.statusDescr}</dd>
		<dt><g:message code="invoice.label.date"/></dt><dd>${Util.formatDate(invoice.createDateTime, session["user_id"])}</dd>
		<dt><g:message code="invoice.label.duedate"/></dt><dd>${Util.formatDate(invoice.dueDate, session["user_id"])}</dd>
		<dt><g:message code="invoice.label.gen.date"/></dt><dd>${Util.formatDate(invoice.createTimeStamp, session["user_id"])}</dd>
		<dt><g:message code="invoice.label.amount"/></dt><dd>${Util.formatMoney(new BigDecimal(invoice.total?: "0.0"),
							session["user_id"],invoice?.currencyId, false)}</dd>
		<dt><g:message code="invoice.label.balance"/></dt><dd>${Util.formatMoney(new BigDecimal(invoice.balance ?: "0.0"),
							session["user_id"],invoice?.currencyId, false)}</dd>
		<dt><g:message code="invoice.label.carried.bal"/></dt><dd>${Util.formatMoney(new BigDecimal(invoice.balance ?: "0.0"),
							session["user_id"],invoice?.currencyId, false)}</dd>
		<dt><g:message code="invoice.label.currency"/></dt><dd>${invoice?.currencyId}</dd>
		<dt><g:message code="invoice.label.payment.attempts"/></dt><dd>${invoice.paymentAttempts}</dd>
		<dt><g:message code="invoice.label.orders"/></dt><dd><g:each var="order" in="${invoice.orders}">
			${order.toString()}&nbsp;
		</g:each></dd>
		<dt><g:message code="invoice.label.delegation"/></dt><dd>${delegatedInvoices}</dd>
	</dl>

<div class="heading">
	<strong><g:message code="invoice.label.lines"/></strong>
</div>
<div class="box-cards box-cards-open">
	<div class="box-cards-title">
		<span><g:message code="label.gui.amount"/></span>
		<span style="width:25%"><g:message code="label.gui.description"/></span>
		<span style="width:25%"><g:message code="label.gui.quantity"/></span>
		<span style="width:25%"><g:message code="label.gui.price"/></span>
	</div>
	<div class="box-card-hold">			   
		<div class="form-columns">
			<g:each var="line" in="${invoice.invoiceLines}" status="idx">
				<label>${line.description}</label>
				<label>${(int)line.quantity}</label>
				<label>${Util.formatMoney(new BigDecimal(line.price?:"0.0"),
								session["user_id"],invoice?.currencyId, false)}</label>
				<label>${Util.formatMoney(new BigDecimal(line.amount?: "0.0"),
								session["user_id"],invoice?.currencyId, false)}</label>
			</g:each>
		</div>			  
	</div>
</div>

<div class="heading">
	<strong><g:message code="invoice.label.payment.refunds"/></strong>
</div>

<div class="box-cards box-cards-open">
	<div class="box-cards-title">
		<span>.</span>
		<span style="width:20%"><g:message code="label.gui.date"/></span>
		<span style="width:20%"><g:message code="label.gui.payment.refunds"/></span>
		<span style="width:20%"><g:message code="label.gui.amount"/></span>
		<span style="width:20%"><g:message code="label.gui.method"/></span>
		<span style="width:20%"><g:message code="label.gui.result"/></span>
	</div>
	<div class="box-card-hold">
		<div class="form-columns">
			<g:each var="payment" in="${payments}" status="idx">
				<label>${Util.formatDate(payment.paymentDate, session["user_id"])}</label>
				<label>${payment.isRefund?"R":"P"}</label>
				<label>${Util.formatMoney(new BigDecimal(payment.amount?:"0.0"),
								session["user_id"],invoice?.currencyId, false)}</label>
				<label>${new PaymentMethodDTO(payment?.paymentMethodId).getDescription(languageId)}</label>
				<label>${new PaymentResultDTO(payment?.resultId).getDescription(languageId)}</label>
				<label>*</label>
			</g:each>			
		</div>			  
	</div>
</div>

<hr></hr>

<div class="heading">
	<strong><g:message code="invoice.label.note"/></strong>
</div>
<br/>
${invoice.customerNotes }
</div>


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