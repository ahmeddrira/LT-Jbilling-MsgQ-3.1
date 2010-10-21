<html>
<head>
<title>
${title}
</title>
</head>
<script language="javascript">
	$(function() {
		$("#dragCategories").draggable();
	});
</script>
<body>

<h2><g:message code="prompt.add.edit.products" /></h2>
<g:form>
	<g:hiddenField name="id" value="${item?.id}" />
	<div id="outerTbl">
	<table id="productDetails" cellspacing='4' class="product-table">
		<tr>
			<td><g:message code="product.internal.number" />:</td>
			<td><g:textField size="40" name="internalNumber"
				value="${item?.internalNumber}" /></td>
			<td rowspan="6" valign="top">
			<table cellspacing="2">
				<tr>
					<td><g:message code="prompt.category.list"/>:<br />
					<g:select id="dragCategories" name="allCategories" multiple="true"
						from="${com.sapienter.jbilling.server.item.db.ItemTypeDTO.findAll()}"
						optionKey="id" optionValue="description" value="" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.description"/>:</td>
			<td><g:textField size="40" name="description"
				value="${item?.description}" /></td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.categories"/>:</td>
			<td><g:select name="itemTypes" multiple="true"
				from="${item?.itemTypes}" optionKey="id" optionValue="description"
				value="${item?.itemTypes}" /></td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.language"/>:</td>
			<td><g:select name="languageId" from="${languageId}"
				optionKey="id" optionValue="description" value="${languageId}" /></td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.percentage"/>:</td>
			<td><g:textField size="5" name="percentage"
				value="${item?.percentage}" /></td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.allow.decimal"/>:</td>
			<td><g:checkBox name="hasDecimals"
				checked="${item?.hasDecimals}" /></td>
		</tr>
		<tr>
			<td>
			<table border="1">
				<thead>
					<tr>
						<th><g:message code="prompt.product.currency"/></th>
						<th><g:message code="prompt.product.price"/></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td></td>
						<td></td>
					</tr>
				</tbody>
			</table>
			</td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.allow.manual.pricing"/>:</td>
			<td><g:checkBox name="priceManual"
				checked="${item?.priceManual}" /></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
		</tr>
	</table>
	<table>
		<tr>
			<td><g:actionSubmit value="Save" action="updateOrCreate"
				class="form_button" /></td>
			<td><input type="button" value="Cancel"
				onClick="javascript: history.back()" /></td>
		</tr>
	</table>
	</div>
</g:form>
</body>
</html>