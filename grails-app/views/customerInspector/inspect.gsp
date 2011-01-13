<%@ page import="com.sapienter.jbilling.server.user.UserBL; com.sapienter.jbilling.common.Constants; com.sapienter.jbilling.server.user.contact.db.ContactDTO; com.sapienter.jbilling.server.util.Util"%>

<html>
<head>
    <meta name="layout" content="main" />
</head>
<body>
<div class="form-edit">

    <g:set var="customer" value="${user.customer}"/>
    <g:set var="contact" value="${ContactDTO.findByUserId(user.id)}"/>

    <div class="heading">
        <strong>
            <g:if test="${contact && (contact.firstName || contact.lastName)}">
                ${contact.firstName} ${contact.lastName}
            </g:if>
            <g:else>
                ${user.userName}
            </g:else>
            <em><g:if test="${contact}">${contact.organizationName}</g:if></em>
        </strong>
    </div>

    <div class="form-hold">
        <fieldset>
            <!-- user details -->
            <div class="form-columns">
                <div class="column">
                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="customer.detail.user.username"/></content>
                        <span>${user.userName}</span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="customer.detail.contact.telephone"/></content>
                        <span>
                            <g:if test="${contact?.phoneCountryCode}">${contact?.phoneCountryCode}.</g:if>
                            <g:if test="${contact?.phoneAreaCode}">${contact?.phoneAreaCode}.</g:if>
                            ${contact?.phoneNumber}
                        </span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="customer.detail.contact.fax"/></content>
                        <span>
                            <g:if test="${contact?.faxCountryCode}">${contact?.faxCountryCode}.</g:if>
                            <g:if test="${contact?.faxAreaCode}">${contact?.faxAreaCode}.</g:if>
                            ${contact?.faxNumber}
                        </span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="customer.detail.user.email"/></content>
                        <span><a href="mailto:${contact?.email}">${contact?.email}</a></span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="customer.detail.user.status"/></content>
                        <span>${user.userStatus.getDescription(session['language_id'])}</span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="customer.detail.user.subscriber.status"/></content>
                        <span>${user.subscriberStatus.getDescription(session['language_id'])}</span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="customer.detail.user.next.invoice.date"/></content>

                        <g:if test="${cycle}">
                            <g:set var="nextInvoiceDate" value="${cycle?.getNextBillableDay() ?: cycle?.getActiveSince() ?: cycle?.getCreateDate()}"/>
                            <span><g:formatDate date="${nextInvoiceDate}"/></span>
                        </g:if>
                        <g:else>
                            <g:message code="prompt.no.active.orders"/>
                        </g:else>
                    </g:applyLayout>
                </div>

                <div class="column">
                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="customer.detail.user.user.id"/></content>
                        <span>${user.id}</span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="customer.detail.user.type"/></content>
                        <g:set var="mainRole" value="${user.roles.asList()?.min{ it.id }}"/>
                        <span title="${mainRole.getDescription(session['language_id'])}">${mainRole.getTitle(session['language_id'])}</span>
                    </g:applyLayout>

                    <g:each var="ccf" in="${contact?.fields?.sort{ it.id }}">
                        <g:applyLayout name="form/text">
                            <content tag="label"><g:message code="${ccf.type.promptKey}"/></content>
                            <span>${ccf.content}</span>
                        </g:applyLayout>
                    </g:each>

                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="customer.detail.payment.lifetime.revenue"/></content>
                        <span><g:formatNumber number="${revenue}" type="currency" currencyCode="${user.currency.code}"/></span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="customer.detail.payment.amount.owed"/></content>
                        <span><g:formatNumber number="${new UserBL().getBalance(user.id)}" type="currency" currencyCode="${user.currency.code}"/></span>
                    </g:applyLayout>
                </div>
            </div>

            <div>
                <div class="btn-row">
                    <g:link controller="user" action="list" id="${user.id}" class="submit user"><span>View Customer</span></g:link>
                    <g:link controller="invoice" action="user" id="${user.id}" class="submit invoice"><span>View Invoices</span></g:link>
                    <g:link controller="payment" action="user" id="${user.id}" class="submit payment"><span>View Payments</span></g:link>
                    <g:link controller="order" action="user" id="${user.id}" class="submit order"><span>View Prders</span></g:link>
                </div>
                <div class="btn-row">
                    <g:link controller="user" action="edit" id="${user.id}" class="submit edit"><span>Edit Customer</span></g:link>
                    <g:link controller="payment" action="edit" params="[userId: user.id]" class="submit payment"><span><g:message code="button.create.payment"/></span></g:link>
                    <g:link controller="order" action="edit" params="[userId: user.id]" class="submit order"><span><g:message code="button.create.order"/></span></g:link>
                </div>
            </div>

            <!-- address -->
            <div id="address" class="box-cards">
                <div class="box-cards-title">
                    <a class="btn-open"><span><g:message code="customer.inspect.address.title"/></span></a>
                </div>
                <div class="box-card-hold">
                    <div class="form-columns">
                        <g:render template="address" model="[contact: contact]"/>
                    </div>
                </div>
            </div>

            <!-- extra contacts -->
            <g:set var="contacts" value="${contacts.findAll { it.id != contact?.id }}"/>
            <g:if test="${contacts}">
                <div id="extra-contacts" class="box-cards">
                    <div class="box-cards-title">
                        <a class="btn-open"><span><g:message code="customer.inspect.extra.contact.title"/></span></a>
                    </div>
                    <div class="box-card-hold">
                        <div class="form-columns">
                            <g:each var="extraContact" in="${contacts}">
                                <g:render template="address" model="[contact: extraContact]"/>
                            </g:each>
                        </div>
                    </div>
                </div>
            </g:if>

            <!-- notes -->
            <div id="notes" class="box-cards">
                <div class="box-cards-title">
                    <a class="btn-open"><span><g:message code="customer.inspect.notes.title"/></span></a>
                </div>
                <div class="box-card-hold">
                    <div class="box-text">
                        <label><g:message code="customer.detail.note.title"/></label>
                        <ul>
                            <li><p>${customer?.notes}</p></li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- last payment -->
            <g:if test="${payment}">
                <div id="payment" class="box-cards">
                    <div class="box-cards-title">
                        <a class="btn-open"><span><g:message code="customer.inspect.last.payment.title"/></span></a>
                    </div>
                    <div class="box-card-hold">
                        <div class="form-columns">
                            <g:render template="payment" model="[payment: payment]"/>
                        </div>
                    </div>
                </div>
            </g:if>

            <!-- last invoice -->
            <g:if test="${invoice}">
                <div id="invoice" class="box-cards">
                    <div class="box-cards-title">
                        <a class="btn-open"><span><g:message code="customer.inspect.last.invoice.title"/></span></a>
                    </div>
                    <div class="box-card-hold">

                        <div class="form-columns">
                            <table cellpadding="0" cellspacing="0" class="dataTable">
                                <tbody>
                                    <tr>
                                        <td><g:message code="invoice.label.id"/></td>
                                        <td class="value"><g:link controller="invoice" action="list" id="${invoice.id}">${invoice.id}</g:link></td>

                                        <td><g:message code="invoice.label.date"/></td>
                                        <td class="value"><g:formatDate date="${invoice.createDatetime}"/></td>

                                        <td><g:message code="invoice.amount.date"/></td>
                                        <td class="value"><g:formatNumber number="${invoice.total}" type="currency" currencyCode="${invoice.currency.code}"/></td>

                                        <td><g:message code="invoice.label.delegation"/></td>
                                        <td class="value">
                                            <g:each var="delegated" in="${invoice.invoices}" status="i">
                                                <g:link controller="invoice" action="list" id="${delegated.id}">${delgated.id}</g:link>
                                                <g:if test="${i < invoice.invoices.size()-1}">, </g:if>
                                            </g:each>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td><g:message code="invoice.label.status"/></td>
                                        <td class="value">
                                            <g:if test="${invoice.invoiceStatus.id == Constants.INVOICE_STATUS_UNPAID}">
                                                <g:link controller="payment" action="edit" params="[userId: user.id, invoiceId: invoice.id]" title="${message(code: 'invoice.pay.link')}">
                                                    ${invoice.invoiceStatus.getDescription(session['language_id'])}
                                                </g:link>
                                            </g:if>
                                            <g:else>
                                                ${invoice.invoiceStatus.getDescription(session['language_id'])}
                                            </g:else>
                                        </td>

                                        <td><g:message code="invoice.label.duedate"/></td>
                                        <td class="value"><g:formatDate date="${invoice.dueDate}"/></td>

                                        <td><g:message code="invoice.label.balance"/></td>
                                        <td class="value"><g:formatNumber number="${invoice.balance}" type="currency" currencyCode="${invoice.currency.code}"/></td>

                                        <td><g:message code="invoice.label.orders"/></td>
                                        <td class="value">
                                            <g:each var="process" in="${invoice.orderProcesses}" status="i">
                                                <g:link controller="order" action="list" id="${process.purchaseOrder.id}">${process.purchaseOrder.id}</g:link>
                                                <g:if test="${i < invoice.orderProcesses.size()-1}">, </g:if>
                                            </g:each>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td><g:message code="invoice.label.payment.attempts"/></td>
                                        <td class="value">${invoice.paymentAttempts}</td>

                                        <td><g:message code="invoice.label.gen.date"/></td>
                                        <td class="value"><g:formatDate date="${invoice.createTimestamp}"/></td>

                                        <td><g:message code="invoice.label.carried.bal"/></td>
                                        <td class="value"><g:formatNumber number="${invoice.carriedBalance}" type="currency" currencyCode="${invoice.currency.code}"/></td>

                                        <!-- spacer -->
                                        <td></td><td></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <table cellpadding="0" cellspacing="0" class="innerTable">
                            <thead class="innerHeader">
                            <tr>
                                <th><g:message code="invoice.label.id"/></th>
                                <th><g:message code="label.gui.description"/></th>
                                <th><g:message code="label.gui.quantity"/></th>
                                <th><g:message code="label.gui.price"/></th>
                                <th><g:message code="label.gui.amount"/></th>
                            </tr>
                            </thead>
                            <tbody>
                                <g:each var="invoiceLine" in="${invoice.invoiceLines}">
                                    <tr>
                                        <td class="innerContent">
                                            <g:link controller="invoice" action="list" id="${invoice.id}">${invoice.id}</g:link>
                                        </td>
                                        <td class="innerContent">
                                            ${invoiceLine.description}
                                        </td>

                                        <td class="innerContent">
                                            <g:formatNumber number="${invoiceLine.quantity}"/>
                                        </td>
                                        <td class="innerContent">
                                            <g:formatNumber number="${invoiceLine.price}" type="currency" currencyCode="${invoice.currency.code}"/>
                                        </td>
                                        <td class="innerContent">
                                            <g:formatNumber number="${invoiceLine.amount}" type="currency" currencyCode="${invoice.currency.code}"/>
                                        </td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>

                        <g:if test="${invoice.paymentMap}">
                            <div class="box-cards">
                                <div class="box-cards-title">
                                    <span><g:message code="invoice.label.payment.refunds"/></span>
                                </div>
                                <div class="box-card-hold">

                                    <g:each var="invoicePayment" in="${invoice.paymentMap}" status="i">
                                        <g:render template="payment" model="[payment: invoicePayment.payment"/>
                                        <g:if test="${i < invoice.paymentMap.size()-1}"><hr/></g:if>
                                    </g:each>
                                </div>
                            </div>
                        </g:if>

                    </div>
                </div>
            </g:if>

            <!-- subscriptions -->
            <g:if test="${subscriptions}">
                <div id="subscriptions" class="box-cards">
                    <div class="box-cards-title">
                        <a class="btn-open"><span><g:message code="customer.inspect.subscriptions.title"/></span></a>
                    </div>
                    <div class="box-card-hold">

                        <table cellpadding="0" cellspacing="0" class="innerTable">
                            <thead class="innerHeader">
                            <tr>
                                <th><g:message code="order.label.id"/></th>
                                <th><g:message code="label.gui.description"/></th>
                                <th><g:message code="label.gui.period"/></th>
                                <th><g:message code="label.gui.quantity"/></th>
                                <th><g:message code="label.gui.price"/></th>
                                <th><g:message code="label.gui.amount"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each var="order" in="${subscriptions}">
                                <g:set var="currency" value="${currencies.find { it.id == order.currencyId }}"/>

                                <g:each var="orderLine" in="${order.orderLines}">
                                    <tr>
                                        <td class="innerContent">
                                            <g:link controller="order" action="list" id="${order.id}">${order.id}</g:link>
                                        </td>
                                        <td class="innerContent">
                                            ${orderLine.description}
                                        </td>
                                        <td class="innerContent">
                                            ${order.periodStr}
                                        </td>
                                        <td class="innerContent">
                                            <g:formatNumber number="${orderLine.quantity}"/>
                                        </td>
                                        <td class="innerContent">
                                            <g:formatNumber number="${orderLine.getPriceAsDecimal()}" type="currency" currencyCode="${currency.code}"/>
                                        </td>
                                        <td class="innerContent">
                                            <g:formatNumber number="${orderLine.getAmountAsDecimal()}" type="currency" currencyCode="${currency.code}"/>
                                        </td>
                                    </tr>
                                </g:each>
                            </g:each>
                            </tbody>
                        </table>

                    </div>
                </div>
            </g:if>

            <!-- credit card -->
            <g:if test="${user?.creditCards}">
                <g:set var="creditCard" value="${user.creditCards.asList().get(0)}"/>

                <div id="creditCard" class="box-cards">
                    <div class="box-cards-title">
                        <a class="btn-open"><span><g:message code="prompt.credit.card"/></span></a>
                    </div>
                    <div class="box-card-hold">
                        <div class="form-columns">
                            <div class="column">
                                <g:applyLayout name="form/text">
                                    <content tag="label"><g:message code="prompt.credit.card"/></content>

                                    %{-- obscure credit card by default, or if the preference is explicitly set --}%
                                    <g:if test="${preferenceIsNullOrEquals(preferenceId: Constants.PREFERENCE_HIDE_CC_NUMBERS, value: 1, true)}">
                                        <g:set var="creditCardNumber" value="${creditCard.number.replaceAll('^\\d{12}','************')}"/>
                                        ${creditCardNumber}
                                    </g:if>
                                    <g:else>
                                        ${creditCard.number}
                                    </g:else>
                                </g:applyLayout>

                                <g:applyLayout name="form/text">
                                    <content tag="label"><g:message code="prompt.name.on.card"/></content>
                                    <span>${creditCard.name}</span>
                                </g:applyLayout>

                                <g:applyLayout name="form/text">
                                    <content tag="label"><g:message code="prompt.expiry.date"/></content>
                                    <span>
                                        <g:formatDate date="${creditCard.expiry}" format="MM"/>
                                        /
                                        <g:formatDate date="${creditCard.expiry}" format="yyyy"/>
                                    </span>
                                </g:applyLayout>
                            </div>

                            <div class="column">
                                <g:applyLayout name="form/text">
                                    <content tag="label"><g:message code="prompt.preferred.auto.payment"/></content>
                                    <g:formatBoolean boolean="${customer.autoPaymentType == Constants.AUTO_PAYMENT_TYPE_CC}"/>
                                </g:applyLayout>
                            </div>
                        </div>
                    </div>
                </div>
            </g:if>

            <!-- ach -->
            <g:if test="${user?.achs}">
                <g:set var="ach" value="${user.achs.asList().get(0)}"/>

                <div id="ach" class="box-cards">
                    <div class="box-cards-title">
                        <a class="btn-open" href="#"><span><g:message code="prompt.ach"/></span></a>
                    </div>
                    <div class="box-card-hold">
                        <div class="form-columns">
                            <div class="column">
                                <g:applyLayout name="form/text">
                                    <content tag="label"><g:message code="prompt.aba.routing.num"/></content>
                                    <span>${ach.abaRouting}</span>
                                </g:applyLayout>

                                <g:applyLayout name="form/text">
                                    <content tag="label"><g:message code="prompt.bank.acc.num"/></content>
                                    <span>${ach.bankAccount}</span>
                                </g:applyLayout>

                                <g:applyLayout name="form/text">
                                    <content tag="label"><g:message code="prompt.bank.name"/></content>
                                    <span>${ach.bankName}</span>
                                </g:applyLayout>

                                <g:applyLayout name="form/text">
                                    <content tag="label"><g:message code="prompt.name.customer.account"/></content>
                                    <span>${ach.accountName}</span>
                                </g:applyLayout>

                                <g:applyLayout name="form/text">
                                    <content tag="label"><g:message code="prompt.account.type" /></content>

                                    <g:if test="${ach.accountType == 1}">
                                        <span><g:message code="label.account.checking"/></span>
                                    </g:if>
                                    <g:elseif test="${ach.accountType == 2}">
                                        <span><g:message code="label.account.savings"/></span>
                                    </g:elseif>
                                </g:applyLayout>
                            </div>

                            <div class="column">
                                <g:applyLayout name="form/text">
                                    <content tag="label"><g:message code="prompt.preferred.auto.payment"/></content>
                                    <g:formatBoolean boolean="${customer.autoPaymentType == Constants.AUTO_PAYMENT_TYPE_ACH}"/>
                                </g:applyLayout>
                            </div>
                        </div>
                    </div>
                </div>
            </g:if>

            <!-- spacer -->
            <div>
                <br/>&nbsp;
            </div>

        </fieldset>
    </div> <!-- end form-hold -->

</div> <!-- end form-edit -->

</body>
</html>