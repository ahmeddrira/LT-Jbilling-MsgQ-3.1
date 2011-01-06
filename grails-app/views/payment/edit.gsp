<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
</head>
<body>
<div class="form-edit">

    <div class="heading">
        <strong>
            Edit Payment
        </strong>
    </div>

    <div class="form-hold">
        <g:form name="payment-edit-form" action="save">
            <fieldset>

                <!-- payment details  -->
                <div class="form-columns">
                    <div class="column">
                        <g:applyLayout name="form/text">
                            <content tag="label">Payment ID</content>
                            <em>${payment.id}</em>
                            <g:hiddenField name="payment.id" value="${payment?.id}"/>
                        </g:applyLayout>

                        <g:applyLayout name="form/text">
                            <content tag="label">Currency</content>
                            <em>${payment.currencyId}</em>
                            <g:hiddenField name="payment.currencyId" value="${payment?.currencyId}"/>
                        </g:applyLayout>

                        <g:applyLayout name="form/input">
                            <content tag="label">Payment Amount</content>
                            <content tag="label.for">payment.amount</content>
                            <g:textField class="field" name="payment.amount" value="${formatNumber(number: payment?.amount, formatName: 'money.format')}"/>
                        </g:applyLayout>


                        <g:applyLayout name="form/date">
                            <content tag="label">Payment Date</content>
                            <content tag="label.for">payment.date</content>
                            <g:textField class="field" name="payment.date" value="${formatDate(date: payment?.paymentDate, formatName:'datepicker.format')}"/>
                        </g:applyLayout>
                    </div>

                    <div class="column">
                        <g:applyLayout name="form/text">
                            <content tag="label">User ID</content>
                            <em>${user.userId}</em>
                            <g:hiddenField name="payment.userId" value="${user.userId}"/>
                        </g:applyLayout>

                        <g:applyLayout name="form/text">
                            <content tag="label">Login Name</content>
                            <em>${user.userName}</em>
                        </g:applyLayout>

                        <g:if test="${user.contact?.firstName || user.contact?.lastName}">
                            <g:applyLayout name="form/text">
                                <content tag="label">Name</content>
                                <em>${user.contact.firstName} ${user.contact.lastName}</em>
                            </g:applyLayout>
                        </g:if>

                        <g:if test="${user.contact?.organizationName}">
                            <g:applyLayout name="form/text">
                                <content tag="label">Organization Name</content>
                                <em>${user.contact.organizationName}</em>
                            </g:applyLayout>
                        </g:if>
                    </div>
                </div>

                <!-- spacer -->
                <div>
                    <br/>&nbsp;
                </div>

                <!-- credit card -->
                <g:if test="${payment?.creditCard}">
                    <g:set var="creditCard" value="${payment.creditCard}"/>
                    <g:hiddenField name="creditCard.id" value="${creditCard.id}"/>

                    <div class="box-cards box-cards-open">
                        <div class="box-cards-title">
                            <a class="btn-open"><span><g:message code="prompt.credit.card"/></span></a>
                        </div>
                        <div class="box-card-hold">
                            <div class="form-columns">
                                <div class="column">
                                    <g:applyLayout name="form/input">
                                        <content tag="label"><g:message code="prompt.credit.card"/></content>
                                        <content tag="label.for">creditCard.number</content>
                                        <g:textField class="field" name="creditCard.number" value="${creditCard.number}" />
                                    </g:applyLayout>

                                    <g:applyLayout name="form/input">
                                        <content tag="label"><g:message code="prompt.name.on.card"/></content>
                                        <content tag="label.for">creditCard.name</content>
                                        <g:textField class="field" name="creditCard.name" value="${creditCard.name}" />
                                    </g:applyLayout>

                                    <g:applyLayout name="form/text">
                                        <content tag="label"><g:message code="prompt.expiry.date"/></content>
                                        <content tag="label.for">expiryMonth</content>
                                        <span>
                                            <g:textField class="text" name="expiryMonth" maxlength="2" size="2" value="${formatDate(date: creditCard.expiry, format:'MM')}" />
                                            -
                                            <g:textField class="text" name="expiryYear" maxlength="4" size="4" value="${formatDate(date: creditCard.expiry, format:'yyyy')}"/>
                                            mm/yyyy
                                        </span>
                                    </g:applyLayout>
                                </div>
                            </div>
                        </div>
                    </div>
                </g:if>

                <!-- ach -->
                <g:if test="${payment?.ach}">
                    <g:set var="ach" value="${payment.ach}"/>
                    <g:hiddenField name="ach.id" value="${ach.id}"/>

                    <div class="box-cards box-cards-open">
                        <div class="box-cards-title">
                            <a class="btn-open" href="#"><span><g:message code="prompt.ach"/></span></a>
                        </div>
                        <div class="box-card-hold">
                            <div class="form-columns">
                                <div class="column">
                                    <g:applyLayout name="form/input">
                                        <content tag="label"><g:message code="prompt.aba.routing.num"/></content>
                                        <content tag="label.for">ach.abaRouting</content>
                                        <g:textField class="field" name="ach.abaRouting" value="${ach.abaRouting}" />
                                    </g:applyLayout>

                                    <g:applyLayout name="form/input">
                                        <content tag="label"><g:message code="prompt.bank.acc.num"/></content>
                                        <content tag="label.for">ach.bankAccount</content>
                                        <g:textField class="field" name="ach.bankAccount" value="${ach.bankAccount}" />
                                    </g:applyLayout>

                                    <g:applyLayout name="form/input">
                                        <content tag="label"><g:message code="prompt.bank.name"/></content>
                                        <content tag="label.for">ach.bankName</content>
                                        <g:textField class="field" name="ach.bankName" value="${ach.bankName}" />
                                    </g:applyLayout>

                                    <g:applyLayout name="form/input">
                                        <content tag="label"><g:message code="prompt.name.customer.account"/></content>
                                        <content tag="label.for">ach.accountName</content>
                                        <g:textField class="field" name="ach.accountName" value="${ach.accountName}" />
                                    </g:applyLayout>

                                    <g:applyLayout name="form/radio">
                                        <content tag="label"><g:message code="prompt.account.type" /></content>

                                        <g:radio class="rb" id="ach.accountType.checking" name="ach.accountType" value="1" checked="${ach.accountType == 1}"/>
                                        <label class="rb" for="ach.accountType.checking"><g:message code="label.account.checking"/></label>

                                        <g:radio class="rb" id="ach.accountType.savings" name="ach.accountType" value="2" checked="${ach.accountType == 2}"/>
                                        <label class="rb" for="ach.accountType.savings"><g:message code="label.account.savings"/></label>
                                    </g:applyLayout>
                                </div>
                            </div>
                        </div>
                    </div>
                </g:if>

                <!-- cheque -->
                <g:if test="${payment?.cheque}">
                    <g:set var="cheque" value="${payment.cheque}"/>
                    <g:hiddenField name="cheque.id" value="${cheque.id}"/>

                    <div class="box-cards box-cards-open">
                        <div class="box-cards-title">
                            <a class="btn-open"><span><g:message code="prompt.cheque"/></span></a>
                        </div>
                        <div class="box-card-hold">
                            <div class="form-columns">
                                <div class="column">
                                    <g:applyLayout name="form/input">
                                        <content tag="label"><g:message code="prompt.cheque.bank"/></content>
                                        <content tag="label.for">cheque.bank</content>
                                        <g:textField class="field" name="cheque.bank" value="${cheque.bank}"/>
                                    </g:applyLayout>

                                    <g:applyLayout name="form/input">
                                        <content tag="label"><g:message code="prompt.cheque.number"/></content>
                                        <content tag="label.for">cheque.number</content>
                                        <g:textField class="field" name="cheque.number" value="${cheque.number}"/>
                                    </g:applyLayout>

                                    <g:applyLayout name="form/date">
                                        <content tag="label"><g:message code="prompt.cheque.date"/></content>
                                        <content tag="label.for">cheque.date</content>
                                        <g:textField class="field" name="cheque.date" value="${formatDate(date: cheque.date, formatName:'datepicker.format')}"/>
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

                <div class="buttons">
                    <ul>
                        <li>
                            <a onclick="$('#payment-edit-form').submit()" class="submit save"><span><g:message code="button.save"/></span></a>
                        </li>
                        <li>
                            <g:link action="list" class="submit cancel"><span><g:message code="button.cancel"/></span></g:link>
                        </li>
                    </ul>
                </div>

            </fieldset>
        </g:form>
    </div>

</div>
</body>
</html>