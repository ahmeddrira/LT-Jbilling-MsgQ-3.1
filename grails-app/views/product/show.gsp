<table>
<tr><td>Product Id:</td><td>${item.id}</td></tr>
<tr><td>Product Code:</td><td>${item.internalNumber}</td></tr>
<tr><td>Language:</td><td>${language}</td></tr>
<tr><td>Promo Code:</td><td>${item.promoCode}</td></tr>
<tr><td>Percentage:</td><td>${item.percentage}</td></tr>

<g:each in="${item.itemPrices}">
	<tr><td>${it.currencyDTO.code}:</td><td>${it.price}</td></tr>   
</g:each>

<tr><td>Allows manual pricing:</td><td>${item.priceManual?"Yes":"No"}</td></tr>
<tr><td>Allows decimal quantity:</td><td>${item.hasDecimals?"Yes":"No"}</td></tr>
<tr><td>Categories:</td><td>
	<g:each in="${item.itemTypes}">
    	${it.description}     
	</g:each>
</td></tr>
</table>