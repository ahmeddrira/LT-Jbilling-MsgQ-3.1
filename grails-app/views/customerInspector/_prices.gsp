
<%--
  Shows a list of item pricing strategies and attributes.

  @author Brian Cowdery
  @since 28-Feb-2011
--%>
<div class="heading">
    <g:if test="${itemId}">
        <strong>
            <g:set var="itemDescription" value="${product.getDescription(session['language_id'])}"/>
            <g:message code="customer.inspect.prices.for.title" args="[itemDescription]"/>
        </strong>
    </g:if>
    <g:else>
        <strong><g:message code="customer.inspect.prices.all.title"/></strong>
    </g:else>
</div>

<div class="box">
    <g:if test="${prices}">

        <table class="dataTable" cellspacing="0" cellpadding="0" width="100%">
            <tbody>

            <g:each var="price" status="index" in="${prices?.sort{ it.precedence }}">
                <tr>
                    <td><g:message code="product.internal.number"/></td>
                    <td class="value">
                        <g:link controller="product" action="list" id="${price.item.id}">
                            ${price.item.internalNumber}
                        </g:link>
                    </td>

                    <td><g:message code="plan.item.precedence"/></td>
                    <td class="value">${price.precedence}</td>
                </tr>

                <tr>
                    <td><g:message code="product.description"/></td>
                    <td class="value" colspan="3">
                        ${price.item.getDescription(session['language_id'])}
                    </td>
                </tr>

                <!-- price model -->
                <tr><td colspan="4">&nbsp;</td></tr>
                <g:render template="/plan/priceModel" model="[model: price.model]"/>

                <!-- separator line -->
                <g:if test="${index < prices.size()-1}">
                    <tr><td colspan="4"><hr/></td></tr>
                </g:if>
            </g:each>

            </tbody>
        </table>

    </g:if>
    <g:else>
        <em><g:message code="customer.inspect.no.prices"/></em>
    </g:else>
</div>

<div class="btn-box">
    <g:if test="${itemId}">
        <g:link class="submit add" controller="plan" action="editCustomerPrice" params="[userId: userId, itemId: itemId]">
            <span><g:message code="button.add.customer.price"/></span>
        </g:link>
    </g:if>
</div>
