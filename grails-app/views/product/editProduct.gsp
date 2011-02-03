<%@ page import="com.sapienter.jbilling.server.pricing.strategy.PriceModelStrategy; com.sapienter.jbilling.server.util.db.CurrencyDTO; com.sapienter.jbilling.server.util.db.LanguageDTO; com.sapienter.jbilling.server.item.db.ItemTypeDTO" %>

<html>
<head>
    <meta name="layout" content="main" />

    <script type="text/javascript">
        var graduatedStrategies = [
        <g:each var="strategy" status="i" in="${PriceModelStrategy.values()}">
             <g:if test="${strategy.getStrategy().isGraduated()}">'${strategy.name()}',</g:if>
        </g:each>
        ];

        $(document).ready(function() {
            $('#price\\.type').change(function() {
                var included = $('#price\\.includedQuantity');

                if ($.inArray($(this).val(), graduatedStrategies) == -1) {
                    included.attr('disabled', 'true');
                    included.val('${formatNumber(number: BigDecimal.ZERO, formatName: 'money.format')}');
                } else {
                    included.attr('disabled', '');
                }
            }).change();
        });
    </script>
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

                            <g:select name="product.types" multiple="true" from="${ItemTypeDTO.list()}"
                                      optionKey="id" optionValue="description" value="${product?.types ?: categoryId}" />
                        </g:applyLayout>
                    </div>
                </div>

                <!-- spacer -->
                <div>
                    <br/>&nbsp;
                </div>

                <!-- pricing controls -->
                <div class="box-cards box-cards-open">
                    <div class="box-cards-title">
                        <a class="btn-open" href="#"><span><g:message code="product.prices"/></span></a>
                    </div>
                    <div class="box-card-hold">
                        <div class="form-columns">
                            <div class="column">
                                <g:hiddenField name="price.id" value="${product?.defaultPrice?.id}"/>

                                <g:applyLayout name="form/input">
                                    <content tag="label"><g:message code="plan.model.rate"/></content>
                                    <content tag="label.for">price.rate</content>
                                    <g:textField class="field" name="price.rate" value="${formatNumber(number: product?.defaultPrice?.rate, formatName: 'money.format')}"/>
                                </g:applyLayout>

                                <g:applyLayout name="form/select">
                                    <content tag="label"><g:message code="plan.model.type"/></content>
                                    <content tag="label.for">price.type</content>
                                    <g:select name="price.type" from="${PriceModelStrategy.values()}"
                                              valueMessagePrefix="price.strategy"
                                              value="${product?.defaultPrice?.type}"/>
                                </g:applyLayout>

                                <g:applyLayout name="form/input">
                                    <content tag="label">Included Quantity</content>
                                    <content tag="label.for">price.includedQuantity</content>
                                    <g:textField class="field" name="price.includedQuantity" value="${formatNumber(number: product?.defaultPrice?.includedQuantity, formatName: 'money.format')}"/>
                                </g:applyLayout>
                            </div>

                            <div class="column">
                                <g:applyLayout name="form/select">
                                    <content tag="label"><g:message code="prompt.user.currency"/></content>
                                    <content tag="label.for">price.currencyId</content>
                                    <g:select name="price.currencyId" from="${currencies}"
                                              optionKey="id" optionValue="description"
                                              value="${product?.defaultPrice?.currencyId}" />
                                </g:applyLayout>

                                <g:applyLayout name="form/input">
                                    <content tag="label"><g:message code="product.percentage"/></content>
                                    <content tag="label.for">product.percentage</content>
                                    <g:textField class="field" name="product.percentage" value="${formatNumber(number: product?.percentage, formatName: 'money.format')}" size="5"/>
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