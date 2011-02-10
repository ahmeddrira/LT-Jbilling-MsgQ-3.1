<%@ page import="com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>


<%--
  Renders an PlanItemWS as an editable row for the plan builder preview pane.

  @author Brian Cowdery
  @since 24-Jan-2011
--%>
<g:set var="product" value="${products?.find{ it.id == planItem.itemId }}"/>
<g:set var="strategy" value="${PriceModelStrategy.valueOf(planItem.model.type)?.getStrategy()}"/>
<g:set var="editable" value="${index == params.int('newLineIndex')}"/>

<g:formRemote name="price-${index}-update-form" url="[action: 'edit']" update="column2" method="GET">
    <g:hiddenField name="_eventId" value="updatePrice"/>
    <g:hiddenField name="execution" value="${flowExecutionKey}"/>
    <g:hiddenField name="index" value="${index}"/>

    <!-- review line ${index} -->
    <li id="line-${index}" class="line ${editable ? 'active' : ''}">
        <g:set var="currency" value="${currencies.find{ it.id == planItem.model.currencyId}}"/>

        <span class="description">
            ${planItem.precedence} &nbsp; ${product.description}
        </span>
        <span class="rate">
            <g:if  test="${!strategy?.hasRate()}">
                <g:formatNumber number="${planItem.model.rateAsDecimal}" type="currency" currencyCode="${currency.code}"/>
            </g:if>
            <g:else>
                <g:formatNumber number="${strategy.rate}" type="currency" currencyCode="${currency.code}"/>
            </g:else>
        </span>
        <span class="strategy">
            <g:message code="price.strategy.${planItem.model.type}"/>
        </span>
    </li>

    <!-- line ${index} editor -->
    <li id="line-${index}-editor" class="editor ${editable ? 'open' : ''}">
        <div class="box">
            <div class="form-columns">
                <g:applyLayout name="form/input">
                    <content tag="label"><g:message code="plan.item.precedence"/></content>
                    <content tag="label.for">price.precedence</content>
                    <g:textField class="field" name="price.precedence" value="${planItem.precedence}"/>
                </g:applyLayout>

                <g:applyLayout name="form/input">
                    <content tag="label"><g:message code="plan.item.bundled.quantity"/></content>
                    <content tag="label.for">price.bundledQuantity</content>
                    <g:textField class="field" name="price.bundledQuantity" value="${formatNumber(number: planItem.bundledQuantity)}"/>
                </g:applyLayout>
                <br/>

                <g:render template="/priceModel/builderModel" model="[model: planItem.model]"/>
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
