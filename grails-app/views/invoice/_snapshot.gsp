<%@ page import="com.sapienter.jbilling.server.util.Util" %>

<div id="invoice-details">
    <!--  Invoice Details snapshot -->
    <div class="form-columns">
        <div class="column">
            <div class="row"><label><g:message code="invoice.label.id"/>:</label><span>${invoice.id}</span></div>
            <div class="row"><label><g:message code="invoice.label.number"/>:</label><span>${invoice.number}</span></div>
            <div class="row"><label><g:message code="invoice.label.status"/>:</label><span>${invoice.statusDescr}</span></div>
            <div class="row"><label><g:message code="invoice.label.date"/>:</label><span>${Util.formatDate(invoice.createDateTime, session["user_id"])}</span></div>
            <div class="row"><label><g:message code="invoice.label.duedate"/>:</label><span>${Util.formatDate(invoice.dueDate, session["user_id"])}</span></div>
        </div>
    
        <div class="column">
            <div class="row"><label><g:message code="invoice.label.gen.date"/>:</label><span>${Util.formatDate(invoice.createTimeStamp, session["user_id"])}</span></div>
            <div class="row"><label><g:message code="invoice.label.amount"/>:</label><span>${Util.formatMoney(new BigDecimal(invoice.total?: "0.0"),
                session["user_id"],invoice?.currencyId, false)}</span></div>
            <div class="row"><label><g:message code="invoice.label.balance"/>:</label><span>${Util.formatMoney(new BigDecimal(invoice.balance ?: "0.0"),
                session["user_id"],invoice?.currencyId, false)}</span></div>
            <div class="row"><label><g:message code="invoice.label.carried.bal"/>:</label><span>${Util.formatMoney(new BigDecimal(invoice.balance ?: "0.0"),
                session["user_id"],invoice?.currencyId, false)}</span></div>
            
            <div class="row"><label><g:message code="invoice.label.payment.attempts"/>:</label><span>${invoice.paymentAttempts}</span></div>
            <div class="row"><label><g:message code="invoice.label.orders"/>:</label><span>
                <g:each var="order" in="${invoice.orders}">${order.toString()}&nbsp;</g:each></span>
            </div>
        </div>
    </div>
    
    <div class="btn-row">
        <a href="${createLink (controller: 'order', action: 'apply', params: [id: session['applyToInvoiceOrderId'], invoiceId: invoice.id])}" class="submit payment">
            <span><g:message code="order.button.apply"/></span></a>
    </div>
</div>