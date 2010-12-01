
<script language="javascript">
function lchange() {
	//alert("onchange called=" + document.getElementById('languageId').value);
	document.forms[0].action='/jbilling/product/changeLanguage';
    document.forms[0].submit();
}
</script>

<div class="heading table-heading">
	<strong style="width: 100%">
		<g:message code="prompt.add.edit.products"/>
	</strong>
</div>

<g:form action="updateOrCreate" name="addEdit">
	<g:hiddenField name="id" value="${item?.id}" />
	<div class="table-box">
		<ul>
			<li>
				<strong><g:message code="product.internal.number" />:</strong>
				<strong><g:textField size="40" name="number"
					value="${item?.internalNumber}" /></strong>			
			</li>
			<li>
				<strong><g:message code="prompt.product.description" />:</strong>
				<strong><g:textField size="40" name="description"
					value="${item?.description}" /></strong>
			</li>
			<li>
				<strong><g:message code="prompt.product.categories" />:</strong>
				<strong><g:select id="droppable" name="types" multiple="true"
					from="${com.sapienter.jbilling.server.item.db.ItemTypeDTO.findAll()}"
					optionKey="id" optionValue="description" value="${item?.itemTypes}" /></strong>
			</li>
			<li>
				<strong><g:message code="prompt.product.language" />:</strong>
				<strong><g:select name="languageId"
					from="${com.sapienter.jbilling.server.util.db.LanguageDTO.list()}"
					optionKey="id" optionValue="description" value="${languageId}"
					onchange="lchange()" /></strong>
			</li>
			<li>
				<strong><g:message code="prompt.product.percentage" />:</strong>
				<strong><g:textField size="5" name="percentage"
					value="${item?.percentage}" /></strong>
			</li>
			<li>
				<strong><g:message code="prompt.product.allow.decimal" />:</strong>
				<strong><g:checkBox name="hasDecimals"
					checked="${(item?.hasDecimals > 0 ? true:false)}" /></strong>
			</li>
			<li>
				<strong>				
					<div class="heading table-heading">
					    <strong class="first"><g:message code="prompt.product.currency"/></strong>
					    <strong style="width:70%"><g:message code="prompt.product.price"/></strong>
					</div>

					 <ul>				
						<g:set var="counter" value="${-1}" />
						<g:each in="${currencies}" var="curr">
							<g:if test="${curr.inUse}">
								<g:set var="counter" value="${counter+1}" />
								<li>
									<strong>${curr.getDescription(languageId)}</strong>
									
									<strong><g:hiddenField name="prices[${counter}].currencyId"
										value="${curr.getId()}" /> 
									<g:if test="${(item?.itemPrices)}">
										<g:set var="priceFound" value="${false}"/>
										<g:each in="${item?.itemPrices}" var="obj">
											<g:if test="${curr?.id == obj.currencyDTO?.id}">
												<g:set var="priceFound" value="${true}"/>
												<g:textField size="5" name="prices[${counter}].price"
													value="${obj?.price}" />
											</g:if>
										</g:each>
										<g:if test="${!priceFound}">
											<g:textField size="5" name="prices[${counter}].price" value="" />	
										</g:if>
									</g:if> <g:else>
										<g:textField size="5" name="prices[${counter}].price" value="" />
									</g:else></strong>
								</li>
							</g:if>
						</g:each>
						<g:hiddenField name="pricesCnt" value="${counter}" />					
						</ul>
				</strong>
			</li>
			<li>
				<strong><g:message code="prompt.product.allow.manual.pricing" />:</strong>
				<strong><g:checkBox name="priceManual"
					checked="${(item?.priceManual > 0 ? true:false)}" /></strong>
			</li>
		</ul>
		<div class="btn-box">
		    <a href="${createLink(action: 'updateOrCreate')}" class="submit add"><span><g:message code="button.save"/></span></a>
		    <a href="#" class="submit cancel" onclick="javascript: history.back()"><span><g:message code="button.cancel"/></span></a>
		</div>
		<!-- 
		<table>
			<tr>
				<td><g:actionSubmit value="Save" action="updateOrCreate"
					class="form_button" /></td>
				<td><input type="button" value="Cancel"
					onClick="javascript: history.back()" /></td>
			</tr>
		</table>
		 -->
	</div>
</g:form>