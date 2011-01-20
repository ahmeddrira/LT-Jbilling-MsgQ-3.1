<%@ page import="com.sapienter.jbilling.server.util.Util" %>

<div class="column-hold">

    <div class="heading">
        <strong><g:message code="order.label.details"/>
    </div>
    
    <!-- Order Details -->
    <div class="box">
        <table class="dataTable">
            <tr><td><g:message code="order.label.id"/>:</td><td class="value">${order.id}</td></tr>
            <tr><td><g:message code="order.label.active.since"/>:</td>
                <td class="value"><g:if test="${order?.activeSince}">${Util.formatDate(order.activeSince, session["locale"])}</g:if></td>
            </tr>
            <tr><td><g:message code="order.label.active.until"/>:</td>
                <td class="value"><g:if test="${order?.activeUntil}">${Util.formatDate(order.activeUntil, session["locale"])}</g:if></td>
            </tr>
            <tr><td><g:message code="order.label.next.invoice"/>:</td>
                <td class="value"><g:if test="${order?.nextBillableDay}">${Util.formatDate(order.nextBillableDay, session["locale"])}</g:if></td></tr>
            <tr><td><g:message code="order.label.period"/>:</td><td class="value">${order.period}</td></tr>
            <tr><td><g:message code="order.label.total"/>:</td>
                    <td class="value">${Util.formatMoney(order.total as BigDecimal,
                        session["user_id"],
                        order.currencyId, 
                        false)?.substring(3)}</td></tr>
        </table>
    </div>
    <div class="btn-box">
    </div>
</div>