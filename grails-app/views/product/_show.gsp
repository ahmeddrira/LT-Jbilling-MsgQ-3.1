<%@ page import="com.sapienter.jbilling.server.util.Util"%>
<script>
function editProduct() {
	window.location = ('/jbilling/product/addEditProduct/'+ ${item.id});
}
</script>
<div class="column-hold">

	<div class="heading">
	    <strong>${item.getDescription(languageId)}</strong>
	</div>

	<div class="box">

		<dl class="other">
			<dt><g:message code="product.id" />:</dt>
			<dd>${item.id}</dd>
			
			<dt><g:message code="product.internal.number" />:</dt>
			<dd>${item.internalNumber}&nbsp;</dd>
			
			<dt><g:message code="prompt.product.language" />:</dt>
			<dd>${language}&nbsp;</dd>
	
			<dt><g:message code="prompt.product.promo.code" />:</dt>
			<dd>${item.promoCode}&nbsp;</dd>
	
			<dt><g:message code="prompt.product.percentage" />:</dt>
			<dd>${item.percentage}&nbsp;</dd>
<%-- --%>
			<g:each in="${item.itemPrices}">
				<dt>${it?.currencyDTO?.code}:&nbsp;</dt>
				<dd>
${Util.formatMoney(new BigDecimal(it?.price), session["user_id"],it?.currencyDTO?.id, false)}&nbsp;
				</dd>
			</g:each>

			<dt><em><g:message code="prompt.product.manual.pricing"/>:&nbsp;</em></dt>
			<dd><em>${item.priceManual?"Yes":"No"}&nbsp;</em></dd>

			<dt><em><g:message code="prompt.product.decimal"/>:&nbsp;</em></dt>
			<dd><em>${item.hasDecimals?"Yes":"No"}&nbsp;</em></dd>

			<dt><g:message code="prompt.product.categories" />:</dt>
			<dd><g:each in="${item.itemTypes}">${it.description}&nbsp;</g:each></dd>
		</dl>
	</div>

    <div class="btn-box">
        <a href="javascript: void(0)" onclick="editProduct();" class="submit edit">
    		<span><g:message code="button.edit"/></span></a>    	
    </div>
	
</div>
