<g:set var="next" value="${model}"/>
<g:while test="${next}">
    <tr class="price">
        <td><g:message code="plan.model.type"/></td>
        <td class="value"><g:message code="price.strategy.${next.type.name()}"/></td>
        <td><g:message code="plan.model.rate"/></td>
        <td class="value">
            <g:if test="${next.rate}">
                <g:formatNumber number="${next.rate}" type="currency" currencySymbol="${next.currency?.symbol}"/>
            </g:if>
            <g:else>
                -
            </g:else>
        </td>
    </tr>
    <g:each var="attribute" in="${next.attributes.entrySet()}">
        <g:if test="${attribute.value}">
            <tr class="attribute">
                <td></td><td></td>
                <td><g:message code="${attribute.key}"/></td>
                <td class="value">${attribute.value}</td>
            </tr>
        </g:if>
    </g:each>

    <g:set var="next" value="${next.next}"/>
</g:while>