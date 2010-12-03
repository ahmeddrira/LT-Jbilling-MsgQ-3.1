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
								<p><g:message code="product.category.id"/></p>
								<span><g:textField readonly="readonly"
							name="id" value="${dto?.id}"/></span>
							</div>
							<div class="row">
								<p><g:message code="product.category.name"/></p>
								<span><g:textField name="description" 
							value="${dto?.description}"/></span>
							</div>
							<div class="row">
								<p><g:message code="product.category.type"/></p>
								<span><g:select name="orderLineTypeId"
							from="${com.sapienter.jbilling.server.order.db.OrderLineTypeDTO.list()}"
							optionKey="id" optionValue="description"
							value="${dto?.orderLineTypeId}" /></span>
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