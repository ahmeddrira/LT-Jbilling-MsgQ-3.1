<table>
	<tr>
		<td><g:message code="product.id" />:</td>
		<td>
		${item.id}
		</td>
	</tr>
	<tr>
		<td><g:message code="product.internal.number" />:</td>
		<td>
		${item.internalNumber}
		</td>
	</tr>
	<tr>
		<td><g:message code="prompt.product.language" />:</td>
		<td>
		${language}
		</td>
	</tr>
	<tr>
		<td><g:message code="prompt.product.promo.code" />:</td>
		<td>
		${item.promoCode}
		</td>
	</tr>
	<tr>
		<td><g:message code="prompt.product.percentage" />:</td>
		<td>
		${item.percentage}
		</td>
	</tr>

	<g:each in="${item.itemPrices}">
		<tr>
			<td>
			${it.currencyDTO.code}:</td>
			<td>
			${it.price}
			</td>
		</tr>
	</g:each>

	<tr>
		<td><g:message code="prompt.product.allow.manual.pricing" />:</td>
		<td>
		${item.priceManual?"Yes":"No"}
		</td>
	</tr>
	<tr>
		<td><g:message code="prompt.product.allow.decimal" />:</td>
		<td>
		${item.hasDecimals?"Yes":"No"}
		</td>
	</tr>
	<tr>
		<td><g:message code="prompt.product.categories" />:</td>
		<td><g:each in="${item.itemTypes}">
			${it.description}
		</g:each></td>
	</tr>
</table>