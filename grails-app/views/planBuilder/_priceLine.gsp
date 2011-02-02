<%@ page import="com.sapienter.jbilling.server.pricing.strategy.PriceModelStrategy" %>

<%--
  Renders an PlanItemWS as an editable row for the plan builder preview pane.

  @author Brian Cowdery
  @since 24-Jan-2011
--%>
<g:set var="product" value="${products?.find{ it.id == planItem.itemId }}"/>
<g:set var="editable" value="${index == params.int('newLineIndex')}"/>

<g:formRemote name="price-${index}-update-form" url="[action: 'edit']" update="column2" method="GET">
    <g:hiddenField name="_eventId" value="updatePrice"/>
    <g:hiddenField name="execution" value="${flowExecutionKey}"/>

    <g:hiddenField name="index" value="${index}"/>
    <g:hiddenField name="update" value=""/>


    <!-- review line ${index} -->
    <li id="line-${index}" class="line ${editable ? 'active' : ''}">
        <g:set var="currency" value="${currencies.find{ it.id == planItem.model.currencyId}}"/>

        <span class="description">
            ${planItem.precedence} &nbsp; ${product.description}
        </span>
        <span class="rate">
            <g:formatNumber number="${planItem.model.getRateAsDecimal()}" type="currency" currencyCode="${currency.code}"/>
        </span>
        <span class="strategy">
            <g:message code="price.strategy.${planItem.model.type}"/>
        </span>
    </li>

    <!-- line ${index} editor -->
    <li id="line-${index}-editor" class="editor ${editable ? 'open' : ''}">
        <div class="box">
            <div class="form-columns">
                <g:applyLayout name="form/select">
                    <content tag="label"><g:message code="plan.model.type"/></content>
                    <content tag="label.for">model-${index}.type</content>
                    <g:select from="${PriceModelStrategy.values()}"
                              name="model-${index}.type"
                              valueMessagePrefix="price.strategy"
                              value="${planItem.model.type}"/>
                </g:applyLayout>

                <g:set var="strategy" value="${Enum.valueOf(PriceModelStrategy.class, planItem.model.type)?.getStrategy()}"/>

                <g:if test="${!strategy?.hasRate()}">
                    <!-- user defined rate -->
                    <g:applyLayout name="form/input">
                        <content tag="label"><g:message code="plan.model.rate"/></content>
                        <content tag="label.for">model-${index}.rate</content>
                        <g:textField class="field" name="model-${index}.rate" value="${formatNumber(number: planItem.model.rate, formatName: 'money.format')}"/>
                    </g:applyLayout>
                </g:if>
                <g:else>
                    <!-- strategy has a fixed rate -->
                    <g:applyLayout name="form/text">
                        <content tag="label"><g:message code="plan.model.rate"/></content>
                        <g:formatNumber number="${strategy.rate}" formatName="money.format"/>
                        <g:hiddenField name="model-${index}.rate" value="${formatNumber(number: planItem.model.rate, formatName: 'money.format')}"/>
                    </g:applyLayout>
                </g:else>

                <g:applyLayout name="form/select">
                    <content tag="label"><g:message code="prompt.user.currency"/></content>
                    <content tag="label.for">model-${index}.currencyId</content>
                    <g:select from="${currencies}"
                              name="model-${index}.currencyId"
                              optionKey="id" optionValue="code"
                              value="${planItem.model.currencyId}" />
                </g:applyLayout>

                <g:applyLayout name="form/input">
                    <content tag="label"><g:message code="plan.item.precedence"/></content>
                    <content tag="label.for">price-${index}.precedence</content>
                    <g:textField class="field" name="price-${index}.precedence" value="${planItem.precedence}"/>
                </g:applyLayout>

                <!-- attributes -->
                <g:each var="attribute" status="attributeIndex" in="${strategy?.attributeDefinitions}">
                    <g:applyLayout name="form/input">
                        <content tag="label">${attribute.name}</content>
                        <g:textField class="field" name="attribute-${attributeIndex}.value"/>
                    </g:applyLayout>
                </g:each>
            </div>
        </div>

        <div class="btn-box">
            <a class="submit save" onclick="$('#price-${index}-update-form').submit();"><span><g:message code="button.update"/></span></a>
            <g:remoteLink class="submit cancel" action="edit" params="[_eventId: 'removePrice', index: index]" update="column2" method="GET">
                <span><g:message code="button.remove"/></span>
            </g:remoteLink>
        </div>
    </li>
</g:formRemote>

<script type="text/javascript">
    $(function() {
        $('#model-${index}\\.type').change(function() {
            $('#price-${index}-update-form :input#update').val('strategy');
            $('#price-${index}-update-form').submit();
        });
    });
</script>