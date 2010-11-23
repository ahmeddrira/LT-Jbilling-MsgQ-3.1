<html>
<head>
<meta name="layout" content="main" />
</head>
<body>

<table>
	<thead>
		<tr>
			<td>Date</td>
			<td>Due Date</td>
			<td>ID</td>
			<td>#</td>
			<td>Stat.</td>
			<td>Amount</td>
			<td>Balance</td>
		</tr>
	</thead>
	<tbody>
		<g:each var="inv" in="${invoices}">
			<tr>
				<td>
				${inv.id }
				</td>
				<td>
				${inv.createDateTime }
				</td>
				<td>
				${inv.total }
				</td>
				<td>
				${inv.dueDate }
				</td>
			</tr>
		</g:each>
	</tbody>
</table>

</body>
</html>