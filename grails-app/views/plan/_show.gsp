<%@ page import="com.sapienter.jbilling.server.pricing.strategy.PriceModelStrategy; com.sapienter.jbilling.server.util.Util"%>

<%--
  Plan details template.

  @author Brian Cowdery
  @since  1-Feb-2010
--%>

<div class="column-hold">
    <div class="heading">
	    <strong>${plan.id}</strong>
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
                    <td><g:message code="product.id"/></td>
                    <td class="value">
                        <g:link controller="product" action="show" id="${plan.item.id}">
                            ${plan.item.id}
                        </g:link>
                    </td>
                </tr>
                <tr>
                    <td><g:message code="product.internal.number"/></td>
                    <td class="value">${plan.item.internalNumber}</td>
                </tr>
                <tr>
                    <td><g:message code="plan.item.description"/></td>
                    <td class="value">${plan.item.description}</td>
                </tr>
            </tbody>
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
        <g:each var="planItem" status="index" in="${plan.planItems.sort{ it.precedence }}">
            <table class="dataTable" cellspacing="0" cellpadding="0" width="100%">
                <tbody>
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
                </tbody>
            </table>

            <g:if test="${index < plan.planItems.size()-1}">
                <hr/>
            </g:if>
        </g:each>
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

