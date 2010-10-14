<html>
	<body>
		<h2><g:message code="prompt.products"/></h2>
		<div>
			<table id="catTbl" cellspacing='4' class="link-table" >
		<thead>
			<tr>
				
				<th><g:message code="product.name" /></th>
				<th><g:message code="product.id" /></th>
				<th><g:message code="product.internal.number" /></th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${list}" status="idx" var="item">
				<tr>
					
					<td><g:textField size="40" readonly="readonly" name="item[${idx}].description"
						value="${item.description}" /></td>
					<td><g:textField readonly="readonly" name="item[${idx}].id"
						value="${item.id}" /></td>
					<td><g:textField readonly="readonly" name="item[${idx}].internalNumber"
						value="${item.internalNumber}" /></td>
				</tr>
			</g:each>
		</tbody>
	</table>
		</div>
	</body>
</html>