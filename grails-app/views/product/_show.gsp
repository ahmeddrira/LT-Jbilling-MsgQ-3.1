<%@ page import="com.sapienter.jbilling.server.pricing.db.PriceModelStrategy; com.sapienter.jbilling.server.util.Util"%>

<%--
  Product details template. This template shows a product and all the relevant product details.

  @author Brian Cowdery
  @since  16-Dec-2010
--%>


<div class="column-hold">
    <div class="heading">
	    <strong>${selectedProduct.internalNumber}</strong>
	</div>

	<div class="box">
        <!-- product info -->
        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
                <tr>
                    <td><g:message code="product.detail.id"/></td>
                    <td class="value">${selectedProduct.id}</td>
                </tr>
                <tr>
                    <td><g:message code="product.detail.internal.number"/></td>
                    <td class="value">${selectedProduct.internalNumber}</td>
                </tr>
                <tr>
                    <td><g:message code="product.detail.percentage"/></td>
                    <td class="value">${selectedProduct.percentage ?: '-'}</td>
                </tr>
            </tbody>
        </table>

        <!-- pricing -->
        <table class="dataTable" cellspacing="0" cellpadding="0" width="100%">
            <tbody>
            <g:set var="next" value="${selectedProduct.defaultPrice}"/>
                <g:while test="${next}">
                    <tr>
                        <td><g:message code="plan.model.type"/></td>
                        <td class="value"><g:message code="price.strategy.${next.type.name()}"/></td>
                        <td><g:message code="plan.model.rate"/></td>
                        <td class="value">
                            <g:if test="${next.rate}">
                                <g:formatNumber number="${next.rate}" type="currency" currencyCode="${next.currency.code}"/>
                            </g:if>
                            <g:else>
                                -
                            </g:else>
                        </td>
                    </tr>
                    <g:each var="attribute" in="${next.attributes.entrySet()}">
                        <g:if test="${attribute.value}">
                            <tr>
                                <td></td><td></td>
                                <td><g:message code="${attribute.key}"/></td>
                                <td class="value">${attribute.value}</td>
                            </tr>
                        </g:if>
                    </g:each>

                    <g:set var="next" value="${next.next}"/>
                </g:while>
            </tbody>
        </table>

        <!-- flags -->
        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
                <tr>
                    <td><em><g:message code="product.detail.manual.pricing"/></em></td>
                    <td class="value"><em><g:formatBoolean boolean="${selectedProduct.priceManual > 0}"/></em></td>
                </tr>
                <tr>
                    <td><em><g:message code="product.detail.decimal"/></em></td>
                    <td class="value"><em><g:formatBoolean boolean="${selectedProduct.hasDecimals > 0}"/></em></td>
                </tr>
            </tbody>
        </table>

        <p class="description">
            ${selectedProduct.description}
        </p>

        <!-- product categories cloud -->
        <div class="box-cards box-cards-open">
            <div class="box-cards-title">
                <span><g:message code="product.detail.categories.title"/></span>
            </div>
            <div class="box-card-hold">
                <div class="content">
                    <ul class="cloud">
                        <g:each var="category" in="${selectedProduct.itemTypes}">
                            <li>
                                <g:link action="list" id="${category.id}">${category.description}</g:link>
                            </li>
                        </g:each>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="btn-box">
        <g:link action="editProduct" id="${selectedProduct.id}" class="submit edit"><span><g:message code="button.edit"/></span></g:link>
        <a onclick="showConfirm('deleteProduct-${selectedProduct.id}');" class="submit delete"><span><g:message code="button.delete"/></span></a>
    </div>

    <g:render template="/confirm"
              model="['message': 'product.delete.confirm',
                      'controller': 'product',
                      'action': 'deleteProduct',
                      'id': selectedProduct.id,
                      'formParams': ['category': selectedCategoryId],
                      'ajax': true,
                      'update': 'column2',
                      'onYes': 'closePanel($(\'column3\'))'
                     ]"/>
</div>

