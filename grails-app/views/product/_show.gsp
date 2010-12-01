<div class="column-hold"><!-- Item Edit -->
<div class="heading"><a href="${createLink(action: 'edit', )}"
	class="edit"></a></div>

<div class="box edit"><g:remoteLink action="edit" id="${item.id}"
	params="[template: 'addEdit']" before="register(this);"
	onSuccess="render(data, next);" class="edit">
</g:remoteLink> <strong><g:message code="prompt.add.edit.products" /></strong></div>

<div class="table-box">
<ul>
	<li><strong><g:message code="product.id" />:</strong> <strong>
	${item.id} </strong></li>
	<li><strong><g:message code="product.internal.number" />:</strong>
	<strong> ${item.internalNumber} </strong></li>
	<li><strong><g:message code="prompt.product.language" />:</strong>
	<strong> ${language} </strong></li>
	<li><strong><g:message code="prompt.product.promo.code" />:</strong>
	<strong> ${item.promoCode} </strong></li>
	<li><strong><g:message code="prompt.product.percentage" />:</strong>
	<strong> ${item.percentage} </strong></li>

	<g:each in="${item.itemPrices}">
		<li><strong> ${it.currencyDTO.code}:</strong> <strong> ${it.price}
		</strong></li>
	</g:each>

	<li><strong><g:message
		code="prompt.product.allow.manual.pricing" />:</strong> <strong> ${item.priceManual?"Yes":"No"}
	</strong></li>

	<li><strong><g:message
		code="prompt.product.allow.decimal" />:</strong> <strong> ${item.hasDecimals?"Yes":"No"}
	</strong></li>

	<li><strong><g:message code="prompt.product.categories" />:</strong>
	<strong><g:each in="${item.itemTypes}">
		${it.description}
	</g:each></strong></li>
</ul>
</div>
</div>
