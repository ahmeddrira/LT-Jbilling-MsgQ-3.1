<%@page import="com.sapienter.jbilling.server.order.db.OrderLineTypeDTO"%>
<script>
function editCategory() {
	if (selected)
	{
		var selectedId= $('.active').find('#id').text();
		window.location = ('/jbilling/product/addEditCategory/'+ selectedId);
		return true;
	} else {
		alert ('<g:message code="error.edit.record.not.selected"/>');
	}
	return false;
}
</script>
<div class="heading table-heading">
    <strong class="first"><g:message code="product.category.id"/></strong>
    <strong style="width:25%"><g:message code="product.category.type"/></strong>
    <strong style="width:50%"><g:message code="product.category.name"/></strong>
</div>

<div class="table-box">
    <ul>
		<g:each in="${categories}" status="idx" var="cat">
		   <li>
		     <g:remoteLink controller="product" action="type" id="${cat.id}" 
		     	before="register(this);" onSuccess="render(data, next);">
                 <span class="block">
                 	<span>
  ${new OrderLineTypeDTO(cat.orderLineTypeId, 0).getDescription(session['language_id'])}
                 	</span>
                 </span>
                 <strong id="id">${cat.id}</strong>
                 <strong>${cat.description}</strong>
			 </g:remoteLink>
		   </li>
        </g:each>
    </ul>
</div>

<div class="btn-box">
    <a href="${createLink(controller: 'product', action: 'addEditCategory')}" class="submit add">
    	<span><g:message code="button.create"/></span></a>
    <a href="javascript: void(0)" onclick="editCategory();" class="submit edit">
    	<span><g:message code="button.edit"/></span></a>
    <a href="${createLink(action: 'del')}" class="submit delete">
    	<span><g:message code="button.delete"/></span></a>    	
</div>
