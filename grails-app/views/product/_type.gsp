<script>
var selected2;
// todo: should be attached to the ajax "success" event.
// row should only be highlighted when it really is selected.
$(document).ready(function() {
    $('.table-box li#type').bind('click', function() {
        if (selected2) selected2.attr("class", "");
        selected2 = $(this);
        selected2.attr("class", "active");
    })
});
function getSelectedId2() {
	return $('.active').find('#id2').text();
}

function delProduct() {
	var selectedId= getSelectedId2();
	if ( null == selectedId || "" == selectedId) {
		alert ('<g:message code="error.record.not.selected"/>');
	} else {
		if (!confirm('Are you sure you want to delete?')){
			return false;
		}
		window.location = ('/jbilling/product/deleteProduct/'+ selectedId);
	}
	return true;
}
</script>
<div class="column-hold">
	<div class="heading table-heading">
	    <strong class="first"><g:message code="product.name"/></strong>
	    <strong style="width:15%"><g:message code="product.internal.number"/></strong>
	    <strong style="width:15%"><g:message code="product.id"/></strong>
	</div>

	<div class="table-box">
		<ul>
			<g:each in="${list}" status="idx" var="item">
				<!-- TODO The below condition must be superflous -->
				<g:if test="${item.deleted == 0 }">
					<li id="type">
						<g:remoteLink controller="product" action="show" id="${item.id}" 
									before="register(this);" onSuccess="render(data, next);">
							<strong>${item.getDescription(languageId)}</strong>
							<span class="block">
								<span>${item.internalNumber}</span>
							</span>
							<span class="block" id="id2">
								${item.id}
							</span>
						</g:remoteLink>
					</li>
				</g:if>
			</g:each>
		</ul>
	</div>

	<div class="btn-box">
	    <a href="${createLink(action: 'addEditProduct', params: ['itemTypeId':itemTypeId])}" class="submit add">
	    	<span><g:message code="button.add.item"/></span></a>
	   	<a href="javascript: void(0)" onclick="delProduct();" class="submit delete">
    		<span><g:message code="button.delete"/></span></a>
	    <a href="${createLink(action: 'showAll')}" class="submit">
	    	<span><g:message code="button.show.all"/></span></a>
	</div>

</div>