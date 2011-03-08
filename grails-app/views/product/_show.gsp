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
                    <td><g:message code="product.detail.gl.code"/></td>
                    <td class="value">${selectedProduct.glCode}</td>
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
                <g:render template="/plan/priceModel" model="[model: selectedProduct.defaultPrice]"/>
            </tbody>
        </table>

        <!-- flags -->
        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
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
                      'update': 'column1',
                      'onYes': 'closePanel($(\'column2\'))'
                     ]"/>
</div>

