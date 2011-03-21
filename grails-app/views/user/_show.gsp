<%@ page import="com.sapienter.jbilling.server.customer.CustomerBL; com.sapienter.jbilling.common.Constants; com.sapienter.jbilling.server.user.UserBL; com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>

<%--
  Shows details of a selected user.

  @author Brian Cowdery
  @since  23-Nov-2010
--%>

<g:set var="customer" value="${selected.customer}"/>
<g:set var="contact" value="${ContactDTO.findByUserId(selected.id)}"/>

<div class="column-hold">
    <!-- user notes -->
    <div class="heading">
        <strong>
            <g:if test="${contact?.firstName || contact?.lastName}">
                ${contact.firstName} ${contact.lastName}
            </g:if>
            <g:else>
                ${selected.userName}
            </g:else>
            <em><g:if test="${contact}">${contact.organizationName}</g:if></em>
        </strong>
    </div>
    <div class="box edit">
        <g:remoteLink action="show" id="${selected.id}" params="[template: 'notes']" before="register(this);" onSuccess="render(data, next);" class="edit"/>
        <g:if test="${customer?.notes}">
            <p>${customer.notes}</p>
        </g:if>
        <g:else>
            <p><em><g:message code="customer.detail.note.empty.message"/></em></p>
        </g:else>
    </div>

    <!-- user details -->
    <div class="heading">
        <strong><g:message code="customer.detail.user.title"/></strong>
    </div>
    <div class="box">
        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
                <tr>
                    <td><g:message code="customer.detail.user.user.id"/></td>
                    <td class="value"><g:link controller="customerInspector" action="inspect" id="${selected.id}" title="${message(code: 'customer.inspect.link')}">${selected.id}</g:link></td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.user.username"/></td>
                    <td class="value">${selected.userName}</td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.user.status"/></td>
                    <td class="value">${selected.userStatus.description}</td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.user.created.date"/></td>
                    <td class="value"><g:formatDate date="${selected.createDatetime}"/></td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.user.email"/></td>
                    <td class="value"><a href="mailto:${contact?.email}">${contact?.email}</a></td>
                </tr>

                <g:if test="${customer?.parent}">
                    <!-- empty spacer row --> 
                    <tr>
                        <td colspan="2"><br/></td>
                    </tr>
                    <tr>
                        <td><g:message code="prompt.parent.id"/></td>
                        <td class="value">
                            <g:remoteLink action="show" id="${customer.parent.baseUser.id}" before="register(this);" onSuccess="render(data, next);">
                                ${customer.parent.baseUser.id} - ${customer.parent.baseUser.userName}
                            </g:remoteLink>
                        </td>
                    </tr>
                    <tr>
                        <td><g:message code="customer.invoice.if.child.label"/></td>
                        <td class="value">
                            <g:if test="${customer.invoiceChild > 0}">
                                <g:message code="customer.invoice.if.child.true"/>
                            </g:if>
                            <g:else>
                                <g:set var="parent" value="${new CustomerBL(customer.id).getInvoicableParent()}"/>
                                <g:remoteLink action="show" id="${parent.baseUser.id}" before="register(this);" onSuccess="render(data, next);">
                                    <g:message code="customer.invoice.if.child.false" args="[ parent.baseUser.id ]"/>
                                </g:remoteLink>
                            </g:else>
                        </td>
                    </tr>
                </g:if>

                <g:if test="${customer?.children}">
                    <!-- empty spacer row --> 
                    <tr>
                        <td colspan="2"><br/></td>
                    </tr>
                    
                    <!-- direct sub-accounts -->
                    <g:each var="account" in="${customer.children}">
                        <tr>
                            <td><g:message code="customer.subaccount.title" args="[ account.baseUser.id ]"/></td>
                            <td class="value">
                                <g:remoteLink action="show" id="${account.baseUser.id}" before="register(this);" onSuccess="render(data, next);">
                                    ${account.baseUser.userName}
                                </g:remoteLink>
                            </td>
                        </tr>
                    </g:each>
                </g:if>
            </tbody>
        </table>
    </div>

    <!-- user payment details -->
    <div class="heading">
        <strong><g:message code="customer.detail.payment.title"/></strong>
    </div>
    <div class="box">
        <g:set var="invoice" value="${selected.invoices ? selected.invoices.asList().first() : null}"/>
        <g:set var="payment" value="${selected.payments ? selected.payments.asList().first() : null}"/>

        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
                <tr>
                    <td><g:message code="customer.detail.payment.invoiced.date"/></td>
                    <td class="value">
                        <g:remoteLink controller="invoice" action="show" id="${invoice?.id}" before="register(this);" onSuccess="render(data, next);">
                            <g:formatDate date="${invoice?.createDatetime}"/>
                        </g:remoteLink>
                    </td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.payment.paid.date"/></td>
                    <td class="value">
                        <g:remoteLink controller="payment" action="show" id="${payment?.id}" before="register(this);" onSuccess="render(data, next);">
                            <g:formatDate date="${payment?.paymentDate ?: payment?.createDatetime}"/>
                        </g:remoteLink>

                    </td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.payment.due.date"/></td>
                    <td class="value"><g:formatDate date="${invoice?.dueDate}"/></td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.payment.invoiced.amount"/></td>
                    <td class="value"><g:formatNumber number="${invoice?.total}" type="currency" currencySymbol="${selected.currency.symbol}"/></td>
                </tr>
                <tr>
                    <td><g:message code="invoice.label.status"/></td>
                    <td class="value">
                        <g:if test="${invoice?.invoiceStatus?.id == Constants.INVOICE_STATUS_UNPAID}">
                            <g:link controller="payment" action="edit" params="[userId: selected.id, invoiceId: invoice.id]" title="${message(code: 'invoice.pay.link')}">
                                ${invoice.invoiceStatus.getDescription(session['language_id'])}
                            </g:link>
                        </g:if>
                        <g:else>
                            ${invoice?.invoiceStatus?.getDescription(session['language_id'])}
                        </g:else>
                    </td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.payment.amount.owed"/></td>
                    <td class="value"><g:formatNumber number="${new UserBL().getTotalOwed(selected.id)}" type="currency"  currencySymbol="${selected.currency.symbol}"/></td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.payment.lifetime.revenue"/></td>
                    <td class="value"><g:formatNumber number="${revenue}" type="currency"  currencySymbol="${selected.currency.symbol}"/></td>
                </tr>
            </tbody>
        </table>

        <hr/>

        <g:set var="card" value="${selected.creditCards ? selected.creditCards.asList().first() : null}"/>
        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
                <tr>
                    <td><g:message code="customer.detail.payment.credit.card"/></td>
                    <td class="value">
                        %{-- obscure credit card by default, or if the preference is explicitly set --}%
                        <g:if test="${card?.number && preferenceIsNullOrEquals(preferenceId: Constants.PREFERENCE_HIDE_CC_NUMBERS, value: 1, true)}">
                            <g:set var="creditCardNumber" value="${card.number.replaceAll('^\\d{12}','************')}"/>
                            ${creditCardNumber}
                        </g:if>
                        <g:else>
                            ${card?.number}
                        </g:else>
                    </td>
                </tr>

                <tr>
                    <td><g:message code="customer.detail.payment.credit.card.expiry"/></td>
                    <td class="value"><g:formatDate date="${card?.ccExpiry}"/></td>
                </tr>
            </tbody>
        </table>
    </div>

    <!-- contact details -->    
    <div class="heading">
        <strong><g:message code="customer.detail.contact.title"/></strong>
    </div>
    <g:if test="${contact}">
    <div class="box">

        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
                <tr>
                    <td><g:message code="customer.detail.contact.telephone"/></td>
                    <td class="value">
                        <g:if test="${contact.phoneCountryCode}">${contact.phoneCountryCode}.</g:if>
                        <g:if test="${contact.phoneAreaCode}">${contact.phoneAreaCode}.</g:if>
                        ${contact.phoneNumber}
                    </td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.contact.address"/></td>
                    <td class="value">${contact.address1} ${contact.address2}</td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.contact.city"/></td>
                    <td class="value">${contact.city}</td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.contact.state"/></td>
                    <td class="value">${contact.stateProvince}</td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.contact.country"/></td>
                    <td class="value">${contact.countryCode}</td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.contact.zip"/></td>
                    <td class="value">${contact.postalCode}</td>
                </tr>
            </tbody>
        </table>
    </div>
    </g:if>

    <div class="btn-box">
        <div class="row">
            <g:link controller="orderBuilder" action="edit" params="[userId: selected.id]" class="submit order"><span><g:message code="button.create.order"/></span></g:link>
            <g:link controller="payment" action="edit" params="[userId: selected.id]" class="submit payment"><span><g:message code="button.make.payment"/></span></g:link>
        </div>
        <div class="row">
            <g:link action="edit" id="${selected.id}" class="submit edit"><span><g:message code="button.edit"/></span></g:link>
            <a onclick="showConfirm('delete-${selected.id}');" class="submit delete"><span><g:message code="button.delete"/></span></a>
            <g:if test="${customer?.isParent > 0}">
                <g:link action="edit" params="[parentId: selected.id]" class="submit add"><span><g:message code="customer.add.subaccount.button"/></span></g:link>
            </g:if>
        </div>
    </div>

    <g:render template="/confirm"
              model="['message': 'customer.delete.confirm',
                      'controller': 'user',
                      'action': 'delete',
                      'id': selected.id,
                      'ajax': true,
                      'update': 'column1',
                      'onYes': 'closePanel(\'#column2\')'
                     ]"/>

</div>