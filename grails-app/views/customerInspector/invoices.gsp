<table>
<thead>
<tr>
	<td>Id</td><td>Date</td><td>Total</td><td>Due Date</td>
</tr>
</thead>
<tbody>
<g:each var="inv" in="${invoices}">
	<tr>
		<td>${inv.id }</td>
		<td>${inv.createDateTime }</td>
		<td>${inv.total }</td>
		<td>${inv.dueDate }</td>
	</tr>
</g:each>
</tbody></table>