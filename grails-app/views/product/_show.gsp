<%@ page import="org.apache.commons.lang.StringUtils; com.sapienter.jbilling.server.pricing.strategy.PriceModelStrategy; com.sapienter.jbilling.server.util.Util"%>

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
		<dl class="other">
			<dt><g:message code="product.detail.id"/></dt>
			<dd>${selectedProduct.id}</dd>
			<dt><g:message code="product.detail.internal.number"/></dt>
			<dd>${selectedProduct.internalNumber} &nbsp;</dd>
			<dt><g:message code="product.detail.percentage"/></dt>
			<dd>${selectedProduct.percentage ?: '-'} &nbsp;</dd>

            <g:if test="${selectedProduct.defaultPrice}">
                <dt>${selectedProduct.defaultPrice.currency.code}</dt>
                <dd><g:formatNumber number="${selectedProduct.defaultPrice.rate}" type="currency" currencyCode="${selectedProduct.defaultPrice.currency.code}"/></dd>

                <g:if test="${selectedProduct.defaultPrice.type != PriceModelStrategy.METERED}">
                    <dt>Pricing Strategy</dt>
                    <dd><g:message code="price.strategy.${selectedProduct.defaultPrice.type}"/></dd>
                </g:if>
            </g:if>

            <br/>

			<dt class="long"><em><g:message code="product.detail.manual.pricing"/>:&nbsp;</em></dt>
			<dd><em><g:formatBoolean boolean="${selectedProduct.priceManual > 0}"/> &nbsp;</em></dd>
			<dt class="long"><em><g:message code="product.detail.decimal"/>:&nbsp;</em></dt>
			<dd><em><g:formatBoolean boolean="${selectedProduct.hasDecimals > 0}"/> &nbsp;</em></dd>

            <p class="description">
                ${selectedProduct.description}
            </p>
		</dl>

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

