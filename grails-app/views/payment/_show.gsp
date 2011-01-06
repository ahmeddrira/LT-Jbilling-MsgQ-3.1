<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>

<%--
  Shows details of a selected payment.

  @author Brian Cowdery
  @since 04-Jan-2011
--%>

<g:set var="customer" value="${selected.baseUser.customer}"/>
<g:set var="contact" value="${ContactDTO.findByUserId(selected.baseUser.id)}"/>

<div class="column-hold">
    <div class="heading">
        <strong>
            <g:if test="${selected.isRefund > 0}">
                <g:message code="payment.refund.title"/>
            </g:if>
            <g:else>
                <g:message code="payment.payment.title"/>
            </g:else>
            <em>${selected.id}</em>
        </strong>
    </div>

    <div class="box">
        <!-- payment details -->
        <dl class="other">
            <dt><g:message code="payment.id"/></dt>
            <dd>${selected.id}</dd>
            <dt><g:message code="payment.attempt"/></dt>
            <dd>${selected.attempt}</dd>
            <dt><g:message code="payment.date"/></dt>
            <dd><g:formatDate date="${selected.paymentDate}" formatName="date.format"/></dd>
        </dl>
        <br/>

        <dl class="other">
            <dt><g:message code="payment.user.id"/></dt>
            <dd>${selected.baseUser.id}</dd>

            <g:if test="${contact?.firstName || contact?.lastName}">
                <dt>Customer Name</dt>
                <dd>${contact.firstName} ${contact.lastName} &nbsp;</dd>
            </g:if>
            <g:if test="${contact?.organizationName}">
                <dt>Organization</dt>
                <dd>${contact.organizationName} &nbsp;</dd>
            </g:if>

            <dt>Login Name</dt>
            <dd>${selected.baseUser.userName}</dd>
        </dl>
        <br/>

        <dl class="other">
            <dt class="long"><g:message code="payment.is.refund"/></dt>
            <dd><em><g:formatBoolean boolean="${selected.isRefund > 0}"/> &nbsp;</em></dd>
            <dt class="long"><g:message code="payment.is.preauth"/></dt>
            <dd><em><g:formatBoolean boolean="${selected.isPreauth > 0}"/> &nbsp;</em></dd>
            <dt class="long"><g:message code="payment.result"/></dt>
            <dd>${selected.paymentResult.getDescription(session['language_id'])}</dd>
        </dl>
        <br/>

        <dl class="other">
            <dt class="long"><g:message code="payment.amount"/></dt>
            <dd><g:formatNumber number="${selected.amount}" type="currency" currencyCode="${selected.currencyDTO.code}"/> &nbsp;</dd>
            <dt class="long"><g:message code="payment.balance"/></dt>
            <dd><g:formatNumber number="${selected.balance}" type="currency" currencyCode="${selected.currencyDTO.code}"/> &nbsp;</dd>
        </dl>
        <br/>

        <!-- list of linked invoices -->
        <g:if test="${selected.invoicesMap}">
            <table cellpadding="0" cellspacing="0" class="innerTable">
                <thead class="innerHeader">
                    <tr>
                        <th><g:message code="payment.invoice.payment"/></th>
                        <th><g:message code="payment.invoice.payment.amount"/></th>
                        <th><g:message code="payment.invoice.payment.date"/></th>
                        <th><!-- action --> &nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="invoicePayment" in="${selected.invoicesMap}">
                    <tr>
                        <td class="innerContent">
                            <g:link controller="invoice" action="list" id="${invoicePayment.invoiceEntity.id}">
                                <g:message code="payment.link.invoice" args="[invoicePayment.invoiceEntity.id]"/>
                            </g:link>
                        </td>
                        <td class="innerContent">
                            <g:formatNumber number="${invoicePayment.amount}" type="currency" currencyCode="${selected.currencyDTO.code}"/>
                        </td>
                        <td class="innerContent">
                            <g:formatDate date="${invoicePayment.createDatetime}"/>
                        </td>
                        <td class="innerContent">
                            <g:remoteLink action="unlink" id="${selected.id}">
                                <g:message code="payment.link.unlink"/>
                            </g:remoteLink>
                        </td>
                    </tr>
                    </g:each>
                </tbody>
            </table>
        </g:if>
    </div>

    <!-- credit card details -->
    <g:if test="${selected.creditCard}">
        <g:set var="creditCard" value="${selected.creditCard}"/>

        <div class="heading">
            <strong><g:message code="payment.credit.card"/></strong>
        </div>
        <div class="box">
            <dl class="other">
                <dt><g:message code="payment.credit.card.name"/></dt>
                <dd>${creditCard.rawName}</dd>
                <dt><g:message code="payment.credit.card.type"/></dt>
                <dd>${selected.paymentMethod.getDescription(session['language_id'])}</dd>
                <dt><g:message code="payment.credit.card.number"/></dt>
                <!-- todo: Check preference for credit card hiding. Mask CC number if preference set -->
                <dd>${creditCard.rawNumber}</dd>
                <dt><g:message code="payment.credit.card.expiry"/></dt>
                <dd><g:formatDate date="${creditCard.ccExpiry}" formatName="credit.card.date.format"/></dd>
            </dl>
        </div>
    </g:if>

    <!-- ACH banking details -->
    <g:if test="${selected.ach}">
        <g:set var="ach" value="${selected.ach}"/>

        <div class="heading">
            <strong><g:message code="payment.ach"/></strong>
        </div>
        <div class="box">
            <dt><g:message code="payment.ach.account.name"/></dt>
            <dd>${ach.accountName}</dd>
            <dt><g:message code="payment.ach.bank.name"/></dt>
            <dd>${ach.bankName}</dd>
            <dt><g:message code="payment.ach.routing.number"/></dt>
            <dd>${ach.adbRouting}</dd>
            <dt><g:message code="payment.ach.account.number"/></dt>
            <dd>${ach.bankAccount}</dd>
            <dt><g:message code="payment.ach.account.type"/></dt>
            <dd>
                <g:if test="${ach.accountType == 1}">
                    <g:message code="label.account.checking"/>
                </g:if>
                <g:else>
                    <g:message code="label.account.savings"/>
                </g:else>
            </dd>
            </dd>
        </div>
    </g:if>

    <!-- cheque details -->
    <g:if test="${selected.paymentInfoCheque}">
        <g:set var="cheque" value="${selected.paymentInfoCheque}"/>

        <div class="heading">
            <strong><g:message code="payment.cheque"/></strong>
        </div>
        <div class="box">
            <dl class="other">
                <dt><g:message code="payment.cheque.bank"/></dt>
                <dd>${cheque.bank}</dd>
                <dt><g:message code="payment.cheque.number"/></dt>
                <dd>${cheque.chequeNumber}</dd>
                <dt><g:message code="payment.cheque.date"/></dt>
                <dd><g:formatDate date="${cheque.date}" formatName="date.format"/></dd>
            </dl>
        </div>
    </g:if>


    <div class="btn-box">
        <!-- edit or delete unlinked payments -->
        <div class="row">
            <g:if test="${!selected.invoicesMap}">
                <g:link action="edit" id="${selected.id}" class="submit edit"><span><g:message code="button.edit"/></span></g:link>
                <a onclick="showConfirm('delete-${selected.id}');" class="submit delete"><span><g:message code="button.delete"/></span></a>
            </g:if>
            <g:else>
                <em>Cannot edit a payment linked to invoices.</em>
            </g:else>
        </div>
    </div>

    <g:render template="/confirm"
              model="['message': 'payment.delete.confirm',
                      'controller': 'payment',
                      'action': 'delete',
                      'id': selected.id,
                      'ajax': true,
                      'update': 'column1',
                      'onYes': 'closePanel(\'#column2\')'
                     ]"/>
</div>