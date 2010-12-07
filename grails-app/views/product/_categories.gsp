<%@page import="com.sapienter.jbilling.server.order.db.OrderLineTypeDTO"%>
<script>
function getSelectedId() {
	return $('.active').find('#id').text();
}
function editCategory() {
	var selectedId= getSelectedId();
	if ( null == selectedId || "" == selectedId) {
		alert ('<g:message code="error.edit.record.not.selected"/>');
	} else {
		window.location = ('/jbilling/product/addEditCategory/'+ selectedId);
	}
	return true;
}
function delCategory() {
	var selectedId= getSelectedId();
	if ( null == selectedId || "" == selectedId) {
		alert ('<g:message code="error.record.not.selected"/>');
	} else {
		if (!confirm('Are you sure you want to delete?')){
			return false;
		}
		window.location = ('/jbilling/product/deleteCategory/'+ selectedId);
	}
	return true;
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
    <a href="${createLink(action: 'addEditCategory')}" class="submit add">
    	<span><g:message code="button.create"/></span></a>
    <a href="javascript: void(0)" onclick="editCategory();" class="submit edit">
    	<span><g:message code="button.edit"/></span></a>
    <a href="javascript: void(0)" onclick="delCategory();" class="submit delete">
    	<span><g:message code="button.delete"/></span></a>    	
</div>
