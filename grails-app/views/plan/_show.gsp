<%@ page import="com.sapienter.jbilling.server.pricing.strategy.PriceModelStrategy; com.sapienter.jbilling.server.util.Util"%>

<%--
  Plan details template.

  @author Brian Cowdery
  @since  1-Feb-2010
--%>

<div class="column-hold">
    <div class="heading">
	    <strong>${plan.item.internalNumber}</strong>
	</div>

    <!-- plan details -->
	<div class="box">
        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
                <tr>
                    <td><g:message code="plan.id"/></td>
                    <td class="value">${plan.id}</td>
                </tr>
                <tr>
                    <td><g:message code="plan.item.internal.number"/></td>
                    <td class="value">${plan.item.internalNumber}</td>
                </tr>
                <tr>
                    <td><g:message code="plan.item.description"/></td>
                    <td class="value">${plan.item.description}</td>
                </tr>

                <g:if test="${plan.item.defaultPrice}">
                    <tr>
                        <td>${plan.item.defaultPrice.currency.code}</td>
                        <td class="value">
                            <g:formatNumber number="${plan.item.defaultPrice.rate}" type="currency" currencyCode="${plan.item.defaultPrice.currency.code}"/>
                        </td>
                    </tr>
                </g:if>
            </tbody>
        </table>

        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tr>
                <td><em><g:message code="product.detail.manual.pricing"/></em></td>
                <td class="value"><em><g:formatBoolean boolean="${plan.item.priceManual > 0}"/></em></td>
            </tr>
        </table>

        <p class="description">
            ${plan.description}
        </p>
    </div>

    <!-- plan prices -->
    <g:if test="${plan.planItems}">
    <div class="heading">
        <strong><g:message code="plan.prices.title"/></strong>
    </div>
    <div class="box">
        <table class="dataTable" cellspacing="0" cellpadding="0" width="100%">
            <tbody>

            <g:each var="planItem" status="index" in="${plan.planItems.sort{ it.precedence }}">

                <tr>
                    <td><g:message code="product.id"/></td>
                    <td class="value">
                        <g:link controller="product" action="show" id="${planItem.item.id}">
                            ${planItem.item.id}
                        </g:link>
                    </td>
                    <td><g:message code="product.internal.number"/></td>
                    <td class="value">${planItem.item.internalNumber}</td>
                </tr>
                <tr>
                    <td><g:message code="plan.model.type"/></td>
                    <td class="value">${planItem.model.type}</td>
                    <td><g:message code="plan.model.rate"/></td>
                    <td class="value"><g:formatNumber number="${planItem.model.rate}" type="currency" currencyCode="${planItem.model.currency.code}"/></td>
                </tr>

                <g:if test="${index < plan.planItems.size()-1}">
                    <tr><td colspan="4"><hr/></td></tr>
                </g:if>

            </g:each>

            </tbody>
        </table>
    </div>
    </g:if>

    <div class="btn-box">
        <g:link controller="planBuilder" action="edit" id="${plan.id}" class="submit edit"><span><g:message code="button.edit"/></span></g:link>
        <a onclick="showConfirm('delete-${plan.id}');" class="submit delete"><span><g:message code="button.delete"/></span></a>
    </div>

    <g:render template="/confirm"
              model="['message': 'plan.delete.confirm',
                      'controller': 'plan',
                      'action': 'delete',
                      'id': plan.id,
                      'ajax': true,
                      'update': 'column1',
                      'onYes': 'closePanel($(\'#column2\'))'
                     ]"/>
</div>

