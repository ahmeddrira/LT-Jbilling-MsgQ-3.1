<html>
<head>
    <meta name="layout" content="main" />
    <script type="text/javascript">

	</script>
</head>
<body>
	<div class="form-edit">
		<div class="heading">
			<strong><g:message code="prompt.product.category"/> - 
				<span>${dto?.description}</span>
			</strong>
		</div>

		<div class="form-hold">
			<g:form action="saveCategory" name="saveCategory">
				<fieldset>
					<div class="form-columns">
						<div class="column">
							<div class="row">
								<label><g:message code="product.category.id"/></label>
								<div class="inp-bg"><g:textField class="field" readonly="readonly"
									name="id" value="${dto?.id}"/></div>
							</div>
							<div class="row">
								<label><g:message code="product.category.name"/></label>
								<div class="inp-bg"><g:textField class="field" name="description" 
									value="${dto?.description}"/></div>
							</div>
							<div class="row">
								<label><g:message code="product.category.type"/></label>
								<div style="width: 220px; " class="selectArea">
									<g:select name="orderLineTypeId"
										from="${com.sapienter.jbilling.server.order.db.OrderLineTypeDTO.list()}"
										optionKey="id" optionValue="description"
										value="${dto?.orderLineTypeId}" />
								</div>
							</div>
						</div>
					</div>
				</fieldset>
			</g:form>
		</div>
		
		<div class="btn-box">
	    <a href="javascript:void(0)" onclick="$('#saveCategory').submit();" class="submit save">
	    	<span><g:message code="button.save"/></span></a>
	    <a href="${createLink(action: 'index')}" class="submit cancel">
	    	<span><g:message code="button.cancel"/></span></a>
	</div>
	
	</div>
</body>
</html>