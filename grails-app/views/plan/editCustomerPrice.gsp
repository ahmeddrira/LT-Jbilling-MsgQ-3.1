<%@ page import="com.sapienter.jbilling.server.util.db.CurrencyDTO; com.sapienter.jbilling.server.util.db.LanguageDTO; com.sapienter.jbilling.server.item.db.ItemTypeDTO" %>

<html>
<head>
    <meta name="layout" content="main" />
</head>
<body>
<div class="form-edit">

    <div class="heading">
        <strong>
            <g:if test="${!price.id}">
                <g:message code="customer.price.new.title"/>
            </g:if>
            <g:else>
                <g:message code="customer.price.update.title"/>
            </g:else>
        </strong>
    </div>

    <div class="form-hold">
        <g:form name="save-price-form" action="saveCustomerPrice">
            <fieldset>
                <div class="form-columns">
                    <div class="column">
                        <g:applyLayout name="form/select">
                            <content tag="label"><g:message code="customer.price.product"/></content>
                            <content tag="label.for">price.itemId</content>
                            <g:select name="price.itemId" from="${products}"
                                      optionKey="id" optionValue="description"
                                      value="${price.itemId}"/>
                        </g:applyLayout>

                        <g:applyLayout name="form/input">
                            <content tag="label"><g:message code="plan.item.precedence"/></content>
                            <content tag="label.for">price.precedence</content>
                            <g:textField class="field" name="price.precedence" value="${price.precedence}"/>
                        </g:applyLayout>
                    </div>

                    <div class="column">
                        <g:applyLayout name="form/text">
                            <content tag="label"><g:message code="payment.user.id"/></content>
                            <span><g:link controller="user" action="list" id="${user.userId}">${user.userId}</g:link></span>
                        </g:applyLayout>

                        <g:applyLayout name="form/text">
                            <content tag="label"><g:message code="prompt.login.name"/></content>
                            <span>${user.userName}</span>
                            <g:hiddenField name="userId" value="${user.userId}"/>
                        </g:applyLayout>

                        <g:if test="${user.contact?.firstName || user.contact?.lastName}">
                            <g:applyLayout name="form/text">
                                <content tag="label"><g:message code="prompt.customer.name"/></content>
                                <em>${user.contact.firstName} ${user.contact.lastName}</em>
                            </g:applyLayout>
                        </g:if>

                        <g:if test="${user.contact?.organizationName}">
                            <g:applyLayout name="form/text">
                                <content tag="label"><g:message code="prompt.organization.name"/></content>
                                <em>${user.contact.organizationName}</em>
                            </g:applyLayout>
                        </g:if>
                    </div>
                </div>

                <!-- spacer -->
                <div>
                    <br/>&nbsp;
                </div>

                <!-- pricing controls -->
                <div class="box-cards box-cards-open">
                    <div class="box-cards-title">
                        <span><g:message code="customer.price.title"/></span>
                    </div>
                    <div class="box-card-hold">
                        <g:render template="/priceModel/model" model="[model: price.model]"/>
                    </div>
                </div>

                <!-- spacer -->
                <div>
                    <br/>&nbsp;
                </div>

                <div class="buttons">
                    <ul>
                        <li><a onclick="$('#save-price-form').submit();" class="submit save"><span><g:message code="button.save"/></span></a></li>
                        <li><g:link action="list" class="submit cancel"><span><g:message code="button.cancel"/></span></g:link></li>
                    </ul>
                </div>

            </fieldset>
        </g:form>
    </div>

</div>
</body>
</html>