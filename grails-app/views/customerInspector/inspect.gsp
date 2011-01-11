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
                        <content tag="label">Login</content>
                        <span>${user.userName}</span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label">Phone</content>
                        <span>
                            <g:if test="${contact?.phoneCountryCode}">${contact?.phoneCountryCode}.</g:if>
                            <g:if test="${contact?.phoneAreaCode}">${contact?.phoneAreaCode}.</g:if>
                            ${contact?.phoneNumber}
                        </span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label">Fax</content>
                        <span>
                            <g:if test="${contact?.faxCountryCode}">${contact?.faxCountryCode}.</g:if>
                            <g:if test="${contact?.faxAreaCode}">${contact?.faxAreaCode}.</g:if>
                            ${contact?.faxNumber}
                        </span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label">Email</content>
                        <span>${contact?.email}</span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label">Status</content>
                        <span>${user.userStatus.getDescription(session['language_id'])}</span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label">Subscriber Status</content>
                        <span>${user.subscriberStatus.getDescription(session['language_id'])}</span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label">Next Invoice Date</content>

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
                        <content tag="label">Number</content>
                        <span>${user.id}</span>
                    </g:applyLayout>

                    <g:applyLayout name="form/text">
                        <content tag="label">Type</content>
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
                        <content tag="label">Balance</content>
                        <span><g:formatNumber number="${new UserBL().getBalance(user.id)}" type="currency" currencyCode="${user.currency.code}"/></span>
                    </g:applyLayout>
                </div>
            </div>

            <div class="form-columns">
                <div class="row">
                    <g:link controller="user" action="list" id="${user.id}" class="submit user"><span>View Customer</span></g:link>
                    <g:link controller="invoice" action="user" id="${user.id}" class="submit invoice"><span>View Invoices</span></g:link>
                    <g:link controller="payment" action="user" id="${user.id}" class="submit payment"><span>View Payments</span></g:link>
                    <g:link controller="order" action="user" id="${user.id}" class="submit order"><span>View Prders</span></g:link>
                </div>
                <div class="row">
                    <g:link controller="user" action="edit" id="${user.id}" class="submit edit"><span>Edit Customer</span></g:link>
                    <g:link controller="payment" action="edit" params="[userId: user.id]" class="submit payment"><span><g:message code="button.create.payment"/></span></g:link>
                    <g:link controller="order" action="edit" params="[userId: user.id]" class="submit order"><span><g:message code="button.create.order"/></span></g:link>
                </div>
            </div>

            <!-- address -->
            <div id="address" class="box-cards">
                <div class="box-cards-title">
                    <a class="btn-open"><span>Address</span></a>
                </div>
                <div class="box-card-hold">
                    <table class="dataTable" cellspacing="0" cellpadding="0">
                        <tbody>
                            <tr>
                                <td>Address</td>
                                <td class="value">${contact?.address1} ${contact?.address2}</td>
                            </tr>
                            <tr>
                                <td>City</td>
                                <td class="value">${contact?.city}</td>

                                <td>State/Province</td>
                                <td class="value">${contact?.stateProvince}</td>

                                <td>Postal Code</td>
                                <td class="value">${contact?.postalCode}</td>
                            </tr>
                            <tr>
                                <td>Country</td>
                                <td class="value">${contact?.countryCode}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- extra contacts -->
            <g:set var="contacts" value="${contacts.findAll { it.id != contact?.id }}"/>
            <g:if test="${contacts}">
                <div id="extra-contacts" class="box-cards">
                    <div class="box-cards-title">
                        <a class="btn-open"><span>Extra Contact</span></a>
                    </div>
                    <div class="box-card-hold">
                        <g:each var="extraContact" in="${contacts}">
                            <g:render template="address" model="[contact: extraContact]"/>
                        </g:each>
                    </div>
                </div>
            </g:if>

            <!-- notes -->
            <div id="notes" class="box-cards">
                <div class="box-cards-title">
                    <a class="btn-open"><span>Notes</span></a>
                </div>
                <div class="box-card-hold">
                    <div class="box-text">
                        <ul>
                            <li><p>${customer?.notes}</p></li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- subscriptions -->
            <g:if test="${subscriptions}">
                <div id="subscriptions" class="box-cards">
                    <div class="box-cards-title">
                        <a class="btn-open"><span>Subscriptions</span></a>
                    </div>
                    <div class="box-card-hold">

                        <table cellpadding="0" cellspacing="0" class="innerTable">
                            <thead class="innerHeader">
                            <tr>
                                <th>Order ID</th>
                                <th>Description</th>
                                <th>Period</th>
                                <th>Qty</th>
                                <th>Price</th>
                                <th>Total</th>
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
                                    <span>${creditCard.number}</span>
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