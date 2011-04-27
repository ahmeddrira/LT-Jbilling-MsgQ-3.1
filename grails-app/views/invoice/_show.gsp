
%{--
  jBilling - The Enterprise Open Source Billing System
  Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

  This file is part of jbilling.

  jbilling is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  jbilling is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
  --}%

<%@ page import="com.sapienter.jbilling.server.payment.db.PaymentResultDTO" %>
<%@ page import="com.sapienter.jbilling.server.payment.db.PaymentMethodDTO" %>

<g:set var="currency" value="${currencies.find{ it.id == selected?.currencyId}}"/>

<div class="column-hold">

    <div class="heading">
        <strong><g:message code="invoice.label.details"/> <em>${selected?.id}</em>
        </strong>
    </div>

    <!-- Invoice details -->
    <div class="box">
        <table class="dataTable">
            <tr>
                <td>
                    <strong>
                        <g:if test="${user?.contact?.firstName || user?.contact?.lastName}">
                            ${user?.contact?.firstName}&nbsp;${user?.contact?.lastName}
                        </g:if>
                        <g:else>
                            ${user?.userName}
                        </g:else>
                    </strong><br>
                    <em>${user?.contact?.organizationName}</em>
                </td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.user.id"/></td>
                <td class="value">
                    <g:remoteLink controller="customer" action="show" id="${user?.id}" before="register(this);" onSuccess="render(data, next);">
                        ${user?.id}
                    </g:remoteLink>
                </td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.user.name"/>:</td>
                <td class="value">${user?.userName}</td>
            </tr>
        </table>

        <table class="dataTable">
            <tr>
                <td><g:message code="invoice.label.id"/></td>
                <td class="value">${selected.id}</td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.number"/></td>
                <td class="value">${selected.number}</td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.status"/></td>
                <td class="value">${selected.statusDescr}</td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.date"/></td>
                <td class="value">
                    <g:formatDate date="${selected?.createDateTime}" formatName="date.pretty.format"/>
                </td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.duedate"/></td>
                <td class="value">
                    <g:formatDate date="${selected?.dueDate}" formatName="date.pretty.format"/>
                </td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.gen.date"/></td>
                <td class="value">
                    <g:formatDate date="${selected?.createTimeStamp}" formatName="date.pretty.format"/>
                </td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.amount"/></td>
                <td class="value">
                    <g:formatNumber number="${selected?.totalAsDecimal ?: BigDecimal.ZERO}" type="currency" currencySymbol="${currency?.symbol}"/>
                </td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.balance"/></td>
                <td class="value">
                    <g:formatNumber number="${selected?.balanceAsDecimal ?: BigDecimal.ZERO}" type="currency" currencySymbol="${currency?.symbol}"/>
                </td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.carried.bal"/></td>
                <td class="value">
                    <g:formatNumber number="${selected?.carriedBalanceAsDecimal ?: BigDecimal.ZERO}" type="currency" currencySymbol="${currency?.symbol}"/>
                </td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.payment.attempts"/></td>
                <td class="value">${selected.paymentAttempts}</td></tr>
            <tr>
                <td><g:message code="invoice.label.orders"/></td>
                <td class="value">
                    <g:each var="order" in="${selected.orders}">
                        <g:remoteLink breadcrumb="id" controller="order" action="show" id="${order}" params="['template': 'order']" before="register(this);" onSuccess="render(data, next);">
                            ${order.toString()}
                        </g:remoteLink>
                    </g:each>
            </td>
            </tr>
            <tr>
                <td><g:message code="invoice.label.delegation"/></td>
                <td class="value">${delegatedInvoices}</td>
            </tr>
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
            <g:each var="line" in="${selected.invoiceLines}" status="idx">
                <tr>
                    <td class="innerContent">
                        ${line.description}
                    </td>
                    <td class="innerContent">
                        <g:formatNumber number="${line.quantity}" formatName="decimal.format"/>
                    </td>
                    <td class="innerContent">
                        <g:formatNumber number="${new BigDecimal(line.price ?: 0)}" type="currency" currencySymbol="${currency?.symbol}"/>
                    </td>
                    <td class="innerContent">
                        <g:formatNumber number="${new BigDecimal(line.amount ?: 0)}" type="currency" currencySymbol="${currency?.symbol}"/>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

    <div class="btn-box">
        <div class="row">
            <a href="${createLink (controller: 'payment', action: 'edit', params: [userId: user?.id, invoiceId: selected.id])}" class="submit payment">
                <span><g:message code="button.invoice.pay"/></span>
                </a>
            <a href="${createLink (action: 'downloadPdf', id: selected.id)}" class="submit save">
                <span><g:message code="button.invoice.downloadPdf"/></span>
            </a>
        </div>
        <div class="row">
            <a href="${createLink (action: 'notifyInvoiceByEmail', id: selected.id)}" class="submit emailinvoice">
                <span><g:message code="button.invoice.sendEmail"/></span>
            </a>
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
                            <g:remoteLink breadcrumb="id" controller="payment" action="show" id="${payment.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                                ${payment.id}
                            </g:remoteLink>
                        </td>
                        <td class="innerContent">
                            <g:formatDate date="${payment.paymentDate}" formatName="date.pretty.format"/>
                        </td>
                        <td class="innerContent">
                            ${payment.isRefund?"R":"P"}
                        </td>
                        <td class="innerContent">
                            <g:formatNumber number="${new BigDecimal(payment.amount ?: 0)}" type="currency" currencySymbol="${currency?.symbol}"/>
                        </td>
                        <td class="innerContent">
                            ${new PaymentMethodDTO(payment?.paymentMethodId).getDescription(session['language_id'])}
                        </td>
                        <td class="innerContent">
                            ${new PaymentResultDTO(payment?.resultId).getDescription(session['language_id'])}
                        </td>
                        <td class="innerContent">
                            <a href="javascript:void(0);" onclick="setUnlinkPaymentId(${selected.id}, ${payment.id});">
                                <span><g:message code="invoice.prompt.unlink.payment"/></span>
                            </a>
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
    <g:if test="${selected.customerNotes}">
        <div class="heading">
            <strong><g:message code="invoice.label.note"/></strong>
        </div>
        <div class="box">
            <p>${selected.customerNotes}</p>
        </div>
    </g:if>

    <div class="btn-box">
        <g:if test="${selected.id}">
            <a onclick="showConfirm('delete-'+${selected.id});" class="submit delete">
                <span><g:message code="button.delete.invoice"/></span>
            </a>
        </g:if>

        <g:link class="submit show" controller="mediation" action="invoice" id="${selected.id}">
            <span><g:message code="button.view.events" /></span>
        </g:link>
    </div>
</div>

<script type="text/javascript">
    function setUnlinkPaymentId(invId, pymId) {
        $('#unlink_payment_id').val(pymId);
        showConfirm("removePaymentLink-" + invId);
        return true;
    }
    function setPaymentId() {
        $('#confirm-command-form-removePaymentLink-${selected.id} [name=paymentId]').val($('#unlink_payment_id').val());
    }
</script>

<g:render template="/confirm"
          model="[message: 'invoice.prompt.confirm.remove.payment.link',
                  controller: 'invoice',
                  action: 'removePaymentLink',
                  id: selected.id,
                  formParams: [ 'paymentId': '-1' ],
                  onYes: 'setPaymentId()',
                 ]"/>

<g:render template="/confirm"
          model="[message: 'invoice.prompt.are.you.sure',
                  controller: 'invoice',
                  action: 'delete',
                  id: selected.id,
                 ]"/>
