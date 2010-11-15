<table>
<thead>
<tr>
	<td>Id</td><td>Date</td><td>Method</td><td>Amount</td><td>Balance</td>
</tr>
</thead>
<tbody>
<g:each var="pym" in="${payments}">
	<tr>
		<td>${pym.id }</td>
		<td>${pym.paymentDate }</td>
		<td>${pym.paymentMethodId }</td>
		<td>${pym.amount}</td>
		<td>${pym.balance}</td>
	</tr>
</g:each>
</tbody></table>