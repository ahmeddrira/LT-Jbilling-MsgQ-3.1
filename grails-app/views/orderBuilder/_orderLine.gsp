
<%--
  Renders an OrderLineWS as an editable row for the order builder preview pane.

  @author Brian Cowdery
  @since 24-Jan-2011
--%>

<g:set var="product" value="${products.find{ it.id == line.itemId}}"/>
<g:set var="quantityNumberFormat" value="${product.hasDecimals ? 'money.format' : 'default.number.format'}"/>

<li id="line-${index}" class="line active">
    <span class="description">
        ${line.description}
    </span>
    <span class="sub-total">
        <g:formatNumber number="${line.getAmountAsDecimal()}" type="currency" currencyCode="${user.currency.code}"/>
    </span>
    <span class="qty-price">
        <g:set var="quantity" value="${formatNumber(number: line.getQuantityAsDecimal(), formatName: quantityNumberFormat)}"/>
        <g:set var="price" value="${formatNumber(number: line.getPriceAsDecimal(), type: 'currency', currencyCode: user.currency.code)}"/>
        <g:message code="order.quantity.by.price" args="[quantity, price]"/>
    </span>
</li>

<li id="line-${index}-editor" class="editor">
    <div class="box">
        <div class="form-columns">
            <g:applyLayout name="form/input">
                <content tag="label"><g:message code="order.label.quantity"/></content>
                <content tag="label.for">line-${index}.quantity</content>
                <g:textField name="line-${index}.quantity" class="field" value="${formatNumber(number: line.getQuantityAsDecimal() ?: BigDecimal.ONE, formatName: quantityNumberFormat)}"/>
            </g:applyLayout>

            <g:if test="${product.priceManual > 0}">
                <g:applyLayout name="form/input">
                    <content tag="label"><g:message code="order.label.price.with.currency" args="[user.currency.code]"/></content>
                    <content tag="label.for">line-${index}.price</content>
                    <g:textField name="line-${index}.price" class="field" value="${formatNumber(number: line.getPriceAsDecimal(), formatName: 'money.format')}"/>
                </g:applyLayout>
            </g:if>

            <g:hiddenField name="line-${index}.index" value="${index}"/>
        </div>
    </div>
    <div class="btn-box">
        <a class="submit save"><span><g:message code="button.add"/></span></a>
        <a class="submit cancel"><span><g:message code="button.remove"/></span></a>
    </div>
</li>