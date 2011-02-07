<%@ page import="com.sapienter.jbilling.server.pricing.db.PriceModelStrategy; com.sapienter.jbilling.server.pricing.db.PriceModelStrategy; com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Editor form for price models.

  @author Brian Cowdery
  @since  02-Feb-2011
--%>

<g:set var="type" value="${(model ? com.sapienter.jbilling.server.pricing.db.PriceModelStrategy.valueOf(model?.type) : com.sapienter.jbilling.server.pricing.db.PriceModelStrategy.METERED)}"/>

<div id="priceModel">
    <g:hiddenField name="model.id" value="${model?.id}"/>

    <g:applyLayout name="form/select">
        <content tag="label"><g:message code="plan.model.type"/></content>
        <content tag="label.for">model.type</content>
        <g:select from="${PriceModelStrategy.values()}"
                name="model.type"
                valueMessagePrefix="price.strategy"
                value="${model?.type ?: type.name()}"/>
    </g:applyLayout>

    <g:applyLayout name="form/input">
        <content tag="label"><g:message code="plan.model.rate"/></content>
        <content tag="label.for">model.rate</content>
        <g:textField class="field" name="model.rate" value="${formatNumber(number: model?.rate ?: BigDecimal.ZERO, formatName: 'money.format')}"/>
    </g:applyLayout>

    <g:applyLayout name="form/select">
        <content tag="label"><g:message code="prompt.user.currency"/></content>
        <content tag="label.for">model.currencyId</content>
        <g:select from="${currencies}"
                name="model.currencyId"
                optionKey="id" optionValue="code"
                value="${model?.currencyId}" />
    </g:applyLayout>


    %{-- Show price precendence when editing a plan --}%
    <g:if test="${planItem}">
        <g:applyLayout name="form/input">
            <content tag="label"><g:message code="plan.item.precedence"/></content>
            <content tag="label.for">price.precedence</content>
            <g:textField class="field" name="price.precedence" value="${planItem.precedence}"/>
        </g:applyLayout>
    </g:if>


    <script type="text/javascript">
        $(function() {
            $('#model\\.type').change(function() {
                    $.ajax({
                       type: 'POST',
                       url:'${createLink(action: 'updateStrategy')}',
                       data: $('#priceModel').parents('form').serialize(),
                       success: function(data){ $('#attributes').replaceWith(data); },
                    });
                }
            );
        });
    </script>
</div>