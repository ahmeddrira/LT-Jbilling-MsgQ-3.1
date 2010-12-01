<html>
<head>
<meta name="layout" content="main" />
<script language="javascript">
function lchange() {
	//alert("onchange called=" + document.getElementById('languageId').value);
	document.forms[0].action='/jbilling/product/changeLanguage';
    document.forms[0].submit();
}
</script>
<title>
</title>
</head>
<body>
<h2><g:message code="prompt.add.edit.products" /></h2>
<g:form name="product" controller="product" action="updateOrCreate">
	<g:hiddenField name="id" value="${item?.id}" />
	<div id="outerTbl">
	<table id="productDetails" cellspacing='4' class="product-table">
		<tr>
			<td><g:message code="product.internal.number" />:</td>
			<td><g:textField size="40" name="number"
				value="${item?.internalNumber}" /></td>			
		</tr>
		<tr>
			<td><g:message code="prompt.product.description" />:</td>
			<td><g:textField size="40" name="description"
				value="${item?.description}" /></td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.categories" />:</td>
			<td><g:select id="droppable" name="types" multiple="true"
				from="${com.sapienter.jbilling.server.item.db.ItemTypeDTO.findAll()}"
				optionKey="id" optionValue="description" value="${item?.itemTypes}" /></td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.language" />:</td>
			<td><g:select name="languageId"
				from="${com.sapienter.jbilling.server.util.db.LanguageDTO.list()}"
				optionKey="id" optionValue="description" value="${languageId}"
				onchange="lchange()" /></td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.percentage" />:</td>
			<td><g:textField size="5" name="percentage"
				value="${item?.percentage}" /></td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.allow.decimal" />:</td>
			<td><g:checkBox name="hasDecimals"
				checked="${(item?.hasDecimals > 0 ? true:false)}" /></td>
		</tr>
		<tr>
			<td>
			<table border="1">
				<thead>
					<tr>
						<th><g:message code="prompt.product.currency" /></th>
						<th><g:message code="prompt.product.price" /></th>
					</tr>
				</thead>
				<tbody>
					<g:set var="counter" value="${-1}" />
					<g:each in="${currencies}" var="curr">
						<g:if test="${curr.inUse}">
							<g:set var="counter" value="${counter+1}" />
							<tr>
								<td>
								${curr.getDescription(languageId)}
								</td>
								<td><g:hiddenField name="prices[${counter}].currencyId"
									value="${curr.getId()}" /> 
								<g:if test="${(item?.itemPrices)}">
									<g:set var="priceFound" value="${false}"/>
									<g:each in="${item?.itemPrices}" var="obj">
										<g:if test="${curr?.id == obj.currencyDTO?.id}">
											<g:set var="priceFound" value="${true}"/>
											<g:textField size="6" name="prices[${counter}].price"
												value="${obj?.price}" />
										</g:if>
									</g:each>
									<g:if test="${!priceFound}">
										<g:textField size="6" name="prices[${counter}].price" value="" />	
									</g:if>
								</g:if> <g:else>
									<g:textField size="6" name="prices[${counter}].price" value="" />
								</g:else></td>
							</tr>
						</g:if>
					</g:each>
					<g:hiddenField name="pricesCnt" value="${counter}" />
				</tbody>
			</table>
			</td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.allow.manual.pricing" />:</td>
			<td><g:checkBox name="priceManual"
				checked="${(item?.priceManual > 0 ? true:false)}" /></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
		</tr>
	</table>
	
	<div class="btn-box">
	    <a href="javascript:void(0)" onclick="$('#product').submit();" class="submit save">
	    	<span><g:message code="button.save"/></span></a>
	    <a href="javascript:void(0)" class="submit cancel" onclick="closePanel(this);">
	    	<span><g:message code="button.cancel"/></span></a>
	</div>
	<%--
	<table>
		<tr>
			<td><g:actionSubmit value="Save" action="updateOrCreate"
				class="form_button" /></td>
			<td><input type="button" value="Cancel"
				onClick="closePanel(this);" /></td>
		</tr>
	</table>
	--%>
	</div>
</g:form>
</body>
</html>