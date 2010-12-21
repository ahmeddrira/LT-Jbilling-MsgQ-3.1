<%@ page import="com.sapienter.jbilling.server.util.Util"%>

<%--
  Product details template. This template shows a product and all the relevant product details.

  @author Brian Cowdery
  @since  16-Dec-2010
--%>


<div class="column-hold">
    <!-- product info -->
    <div class="heading">
        <g:link action="editProduct" id="${selectedProduct.id}" breadcrumb="{'name': 'update'}" class="edit"/>
	    <strong>${selectedProduct.internalNumber}</strong>
	</div>
	<div class="box">
		<dl class="other">
			<dt><g:message code="product.detail.id"/></dt>
			<dd>${selectedProduct.id}</dd>
			<dt><g:message code="product.detail.internal.number"/></dt>
			<dd>${selectedProduct.internalNumber} &nbsp;</dd>
			<dt><g:message code="product.detail.percentage"/></dt>
			<dd>${selectedProduct.percentage ?: '-'} &nbsp;</dd>

			<g:each var="price" in="${selectedProduct.itemPrices}">
				<dt>${price.currencyDTO.code}:&nbsp;</dt>
                <dd><g:formatNumber number="${price.price}" type="currency" currencyCode="${price.currencyDTO.code}"/> &nbsp;</dd>
			</g:each>

            <br/>

			<dt class="long"><em><g:message code="product.detail.manual.pricing"/>:&nbsp;</em></dt>
			<dd><em><g:formatBoolean boolean="${selectedProduct.priceManual > 0}"/> &nbsp;</em></dd>
			<dt class="long"><em><g:message code="product.detail.decimal"/>:&nbsp;</em></dt>
			<dd><em><g:formatBoolean boolean="${selectedProduct.hasDecimals > 0}"/> &nbsp;</em></dd>

            <p><br/>${selectedProduct.description}</p>
		</dl>
	</div>

    <!-- product categories cloud -->
    <div class="heading">
        <strong><g:message code="product.detail.categories.title"/></strong>
    </div>
    <div class="box">
        <ul class="cloud">
            <g:each var="category" in="${selectedProduct.itemTypes}">
                <li>
                    <g:link breadcrumb="id" action="list" id="${category.id}">${category.description}</g:link>
                </li>
            </g:each>
        </ul>
    </div>

    <div class="btn-box">
        <a onclick="showConfirm(${selectedProduct.id});" class="submit delete"><span><g:message code="button.delete"/></span></a>
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

