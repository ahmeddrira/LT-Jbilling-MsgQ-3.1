<div class="heading table-heading">
	<strong style="width: 100%">
		<g:message code="prompt.product.category"/>
	</strong>
</div>

<g:form name="item" controller="item" action="save">

	<g:hiddenField name="recCnt" value="0" />
	<g:hiddenField name="deleteItemId" value="0" />
	<!-- g:hiddenField name="delOrderTypeId" value="0"/-->

	<div class="heading table-heading">
	    <strong class="first"><g:message code="product.category.id"/></strong>
	    <strong style="width:30%"><g:message code="product.category.type"/></strong>
	    <strong style="width:35%"><g:message code="product.category.name"/></strong>
	</div>

	<div id="catTbl" class="table-box">
	    <ul>
			<g:each in="${categories}" status="idx" var="cat">
			   <li id="row${idx}">
			     <g:remoteLink controller="product" action="type" id="${cat.id}" 
			     	before="register(this);" onSuccess="render(data, next);">
	                 <strong>
	                    <g:textField readonly="readonly"
							name="categories[${idx}].id" value="${cat.id}" />
			         </strong>
	                 <strong>
			            <g:textField name="categories[${idx}].description"
							value="${cat.description}" />
			         </strong>
	                 <strong>
	                 	<g:select name="categories[${idx}].orderLineTypeId"
							from="${com.sapienter.jbilling.server.order.db.OrderLineTypeDTO.list()}"
							optionKey="id" optionValue="description"
							value="${cat.orderLineTypeId}" />
	                 </strong>
				 </g:remoteLink>
			   </li>
	        </g:each>
	    </ul>
	</div>

	<div class="btn-box">
	    <a href="javascript:void(0)" onclick="add();" class="submit add">
	    	<span><g:message code="button.create"/></span></a>
	    <a href="javascript:void(0)" onclick="return del();" class="submit delete">
	    	<span><g:message code="button.delete"/></span></a>
	</div>

	<div class="btn-box">
	    <a href="javascript:void(0)" onclick="$('#item').submit();" class="submit save">
	    	<span><g:message code="button.save"/></span></a>
	    <a href="javascript:void(0)" class="submit cancel" onclick="javascript: closePanel(this);">
	    	<span><g:message code="button.cancel"/></span></a>
	</div>

</g:form>