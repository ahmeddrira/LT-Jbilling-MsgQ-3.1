
<%--
  Renders an OrderLineWS as an editable row for the order builder preview pane.

  @author Brian Cowdery
  @since 24-Jan-2011
--%>

<g:set var="product" value="${products?.find{ it.id == line.itemId}}"/>
<g:if test="${!product}">
    <g:set var="product" value="${plans?.find{ it.id == line.itemId}}"/>
</g:if>

<g:set var="quantityNumberFormat" value="${product?.hasDecimals ? 'money.format' : 'default.number.format'}"/>
<g:set var="editable" value="${index == params.int('newLineIndex')}"/>

<g:formRemote name="line-${index}-update-form" url="[action: 'edit']" update="column2" method="GET">
    <g:hiddenField name="_eventId" value="updateLine"/>
    <g:hiddenField name="execution" value="${flowExecutionKey}"/>

    <li id="line-${index}" class="line ${editable ? 'active' : ''}">
        <span class="description">
            ${line.description}
        </span>
        <span class="sub-total">
            <g:set var="subTotal" value="${formatNumber(number: line.getAmountAsDecimal(), type: 'currency', currencyCode: user.currency.code)}"/>
            <g:message code="order.review.line.total" args="[subTotal]"/>
        </span>
        <span class="qty-price">
            <g:set var="quantity" value="${formatNumber(number: line.getQuantityAsDecimal(), formatName: quantityNumberFormat)}"/>
            <g:if test="${product?.percentage}">
                <g:set var="percentage" value="%${formatNumber(number: product.percentage)}"/>
                <g:message code="order.review.quantity.by.price" args="[quantity, percentage]"/>
            </g:if>
            <g:else>
                <g:set var="price" value="${formatNumber(number: line.getPriceAsDecimal(), type: 'currency', currencyCode: user.currency.code)}"/>
                <g:message code="order.review.quantity.by.price" args="[quantity, price]"/>
            </g:else>
        </span>
    </li>

    <li id="line-${index}-editor" class="editor ${editable ? 'open' : ''}">
        <div class="box">
            <div class="form-columns">
                <g:applyLayout name="form/input">
                    <content tag="label"><g:message code="order.label.quantity"/></content>
                    <content tag="label.for">line-${index}.quantity</content>
                    <g:textField name="line-${index}.quantity" class="field" value="${formatNumber(number: line.getQuantityAsDecimal() ?: BigDecimal.ONE, formatName: quantityNumberFormat)}"/>
                </g:applyLayout>

                <g:if test="${product?.priceManual > 0}">
                    <g:applyLayout name="form/input">
                        <content tag="label"><g:message code="order.label.price.with.currency" args="[user.currency.code]"/></content>
                        <content tag="label.for">line-${index}.price</content>
                        <g:textField name="line-${index}.price" class="field" value="${formatNumber(number: line.getPriceAsDecimal(), formatName: 'money.format')}"/>
                    </g:applyLayout>
                </g:if>

                <g:hiddenField name="index" value="${index}"/>
            </div>
        </div>

        <div class="btn-box">
            <a class="submit save" onclick="$('#line-${index}-update-form').submit();"><span><g:message code="button.update"/></span></a>
            <g:remoteLink class="submit cancel" action="edit" params="[_eventId: 'removeLine', index: index]" update="column2" method="GET">
                <span><g:message code="button.remove"/></span>
            </g:remoteLink>
        </div>
    </li>

    <g:if test="${product.plans}">
        <g:each var="plan" in="${product.plans}">
            <g:each var="planItem" in="${plan.planItems}">
                <g:if test="${planItem.bundledQuantity}">
                    <li class="bundled">
                        <span class="description">
                            ${planItem.item.description}
                        </span>
                        <span class="included-qty">
                            + <g:formatNumber number="${planItem.bundledQuantity}"/>
                            <g:if test="${planItem.period}">
                                ${planItem.period.getDescription(session['language_id']).toLowerCase()}
                            </g:if>
                        </span>
                    </li>

                    <li class="bundled-price">
                        <table class="dataTable" cellspacing="0" cellpadding="0" width="100%">
                            <tbody>
                               <g:render template="/plan/priceModel" model="[model: planItem.model]"/>
                            </tbody>
                        </table>
                    </li>
                </g:if>
            </g:each>
        </g:each>
    </g:if>

</g:formRemote>