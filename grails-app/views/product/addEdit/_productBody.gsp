<script type="text/javascript">
	var lang=${languageId};
	
	function lChange() {
		if (lang != $('#languageId').val()) {
			$('#product').attr('action', '/jbilling/product/changeLanguage');
			//alert($('#product').attr('action'));
			$('#product').submit();
		}
		return false;
	}
</script>
<div class="form-edit">
	<div class="heading">
		<strong><g:message code="prompt.add.edit.products"/> - 
			<span>${item?.description}</span>
		</strong>
	</div>

	<div class="form-hold">
		<g:form action="updateOrCreate" name="product">
			<g:hiddenField name="id" value="${item?.id}" />
			<fieldset>
				<div class="form-columns">
					<div class="column">
						<div class="row">
							<label><g:message code="product.internal.number"/>:</label>
							<div class="inp-bg"><g:textField size="40" name="number"
						value="${item?.internalNumber}" class="field"/></div>
						</div>
						<div class="row">
							<label><g:message code="prompt.product.description" />:</label>
							<div class="inp-bg"><g:textField size="40" name="description"
				value="${item?.description}" class="field"/></div>
						</div>
						<div class="row">
							<label><g:message code="prompt.product.categories" />:</label>
							<div class="box">
							<g:select name="types" multiple="true"
								from="${com.sapienter.jbilling.server.item.db.ItemTypeDTO.findAll()}"
								optionKey="id" optionValue="description" value="${item?.itemTypes}" class="selectArea"/>
							</div>
						</div>
						
						<div class="row">
							<label><g:message code="prompt.product.language"/>:</label>
							<div class="select">
							<g:select name="languageId" 
								from="${com.sapienter.jbilling.server.util.db.LanguageDTO.list()}"
								optionKey="id" optionValue="description" value="${languageId}"
								onchange="lChange();"/>
							</div>
						</div>
						<div class="row">
							<label><g:message code="prompt.product.percentage"/>:</label>
							<div class="inp-bg"><g:textField size="5" name="percentage"
				value="${item?.percentage}" class="field"/></div>
						</div>
						<div class="row">
							<label><g:message code="prompt.product.decimal"/>:</label>
							<div class="checkboxArea">
								<g:checkBox name="hasDecimals"
									checked="${(item?.hasDecimals > 0 ? true:false)}" class="cb outtaHere"/>
							</div>
						</div>
						<div class="row">
							<div class="box-cards box-cards-open">
								<div class="box-cards-title">
									<span><g:message code="prompt.product.currency"/></span>
									<span style="width:20%"><g:message code="prompt.product.price"/></span>										
								</div>
								<div class="box-card-hold">
									<%-- Counting currencies to bindData in controller --%>
									<g:set var="counter" value="${-1}"/>
									<%--Iterate all currencies and set/use if applicable --%>
									<g:each in="${currencies}" var="curr">
										<g:if test="${curr.inUse}">
											<g:set var="counter" value="${(counter+1)}"/>
											<div class="form-columns">
											<label>${curr.getDescription(languageId)}</label>
											<label><g:hiddenField name="prices[${counter}].currencyId"
													value="${curr.getId()}" /> 
												<g:if test="${(item?.itemPrices)}">
													<g:set var="priceFound" value="${false}"/>
													<g:each in="${item?.itemPrices}" var="obj">
														<g:if test="${curr?.id == obj.currencyDTO?.id}">
															<g:set var="priceFound" value="${true}"/>
															<g:textField size="5" name="prices[${counter}].price"
																value="${obj?.price}" class="field"/>
														</g:if>
													</g:each>
													<g:if test="${!priceFound}">
														<g:textField size="5" name="prices[${counter}].price" value=""  class="field"/>	
													</g:if>
												</g:if> <g:else>
													<g:textField size="5" name="prices[${counter}].price" value=""  class="field"/>
												</g:else>
											</label>
											</div>
										</g:if>
									</g:each>
									<!-- counter starts from zero, therefore increment by 1 to get the count -->
									<g:hiddenField name="pricesCnt" value="${(counter+1)}" />										
								</div>
							</div>
							<div class="row">
								<label><g:message code="prompt.product.manual.pricing" />:</label>
								<div class="checkboxArea">
									<g:checkBox name="priceManual"
										checked="${(item?.priceManual > 0 ? true:false)}" class="cb outtaHere"/>
								</div>
							</div>
						</div>
					</div>
				</div>
			</fieldset>
		</g:form>
		<div class="btn-box">
			<a href="javascript:void(0)" onclick="$('#product').submit();" class="submit save">
				<span><g:message code="button.save"/></span></a>
			<a href="${createLink(action: 'index')}" class="submit cancel">
				<span><g:message code="button.cancel"/></span></a>
		</div>
	</div>
</div>