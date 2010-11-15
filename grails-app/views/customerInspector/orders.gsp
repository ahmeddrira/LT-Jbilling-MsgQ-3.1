<table border=1>
<thead><tr><th>Order Id</th><th>Description</th><th>Period</th><th>Qty</th><th>Price</th><th>Total</th></tr></thead>
<tbody>
<g:each var="ordr" in="${orders}">
	<g:each var="ln" in="${ordr.orderLines}">
		<tr><td>${ordr.id}</td><td>${ln?.description}</td>
			<td>${ordr.periodStr}</td><td>${ln.quantity}</td><td>${ln.price}</td>
			<td>${ordr.total}</td></tr>
	</g:each>
</g:each>
</tbody>
</table>