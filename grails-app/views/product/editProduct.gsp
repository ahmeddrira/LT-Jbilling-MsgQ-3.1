<%@ page import="com.sapienter.jbilling.server.util.db.CurrencyDTO; com.sapienter.jbilling.server.util.db.LanguageDTO; com.sapienter.jbilling.server.item.db.ItemTypeDTO" %>

<html>
<head>
    <meta name="layout" content="main" />
</head>
<body>
<div class="form-edit">

    <div class="heading">
        <strong>
            <g:if test="${product}">
                <g:message code="product.edit.title"/>
            </g:if>
            <g:else>
                <g:message code="product.add.title"/>
            </g:else>
        </strong>
    </div>

    <div class="form-hold">
        <g:form name="save-product-form" action="saveProduct">
            <fieldset>
                <!-- product info -->
                <div class="form-columns">
                    <div class="column">
                        <g:applyLayout name="form/text">
                            <content tag="label"><g:message code="product.id"/></content>

                            <g:if test="${product}">${product?.id}</g:if>
                            <g:else><em><g:message code="prompt.id.new"/></em></g:else>

                            <g:hiddenField name="product.id" value="${product?.id}"/>
                        </g:applyLayout>

                        <g:applyLayout name="form/input">
                            <content tag="label"><g:message code="product.description"/></content>
                            <content tag="label.for">product.description</content>
                            <g:textField class="field" name="product.description" value="${product?.description}" size="40"/>
                        </g:applyLayout>

                        <g:applyLayout name="form/checkbox">
                            <content tag="label"><g:message code="product.allow.decimal.quantity"/></content>
                            <content tag="label.for">product.hasDecimals</content>
                            <g:checkBox class="cb checkbox" name="product.hasDecimals" checked="${product?.hasDecimals > 0}"/>
                        </g:applyLayout>
                    </div>

                    <div class="column">
                        <g:applyLayout name="form/input">
                            <content tag="label"><g:message code="product.internal.number"/></content>
                            <content tag="label.for">product.number</content>
                            <g:textField class="field" name="product.number" value="${product?.number}" size="40"/>
                        </g:applyLayout>

                        <g:applyLayout name="form/select">
                            <content tag="label"><g:message code="product.categories"/></content>
                            <content tag="label.for">product.types</content>

                            <g:set var="itemTypes" value="${product?.itemTypes?.collect { it.id } }"/>
                            <g:select name="product.types" multiple="true" from="${ItemTypeDTO.list()}"
                                      optionKey="id" optionValue="description" value="${itemTypes ?: categoryId}" />
                        </g:applyLayout>
                    </div>
                </div>

                <!-- spacer -->
                <div>
                    <br/>&nbsp;
                </div>

                <!-- pricing controls -->
                <div class="box-cards ${product?.itemPrices ? 'box-cards-open' : ''}">
                    <div class="box-cards-title">
                        <a class="btn-open" href="#"><span><g:message code="product.prices"/></span></a>
                    </div>
                    <div class="box-card-hold">
                        <div class="form-columns">
                            <div class="column">
                                <g:each var="currency" in="${currencies}">
                                    <g:applyLayout name="form/input">
                                        <content tag="label">${currency.getDescription(session['language_id'])} <strong>${currency.symbol}</strong></content>
                                        <content tag="label.for">prices.${currency.id}</content>

                                        <g:set var="itemPrice" value="${product?.itemPrices?.find { it.currencyDTO.id == currency.id }}"/>
                                        <g:textField class="field" name="prices.${currency.id}" value="${formatNumber(number: itemPrice?.price, format:'#.00')}"/>
                                    </g:applyLayout>
                                </g:each>
                            </div>

                            <div class="column">
                                <g:applyLayout name="form/input">
                                    <content tag="label"><g:message code="product.percentage"/></content>
                                    <content tag="label.for">product.percentage</content>
                                    <g:textField class="field" name="product.percentage" value="${formatNumber(number: product?.percentage, format:'#.00')}" size="5"/>
                                </g:applyLayout>

                                <g:applyLayout name="form/checkbox">
                                    <content tag="label"><g:message code="product.allow.manual.pricing"/></content>
                                    <content tag="label.for">product.priceManual</content>
                                    <g:checkBox class="cb checkbox" name="product.priceManual" checked="${product?.priceManual > 0}"/>
                                </g:applyLayout>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- spacer -->
                <div>
                    <br/>&nbsp;
                </div>

                <div class="buttons">
                    <ul>
                        <li><a onclick="$('#save-product-form').submit();" class="submit save"><span><g:message code="button.save"/></span></a></li>
                        <li><g:link action="list" class="submit cancel"><span><g:message code="button.cancel"/></span></g:link></li>
                    </ul>
                </div>

            </fieldset>
        </g:form>
    </div>

</div>
</body>
</html>