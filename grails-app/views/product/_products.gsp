
<%--
  Products list

  @author Brian Cowdery
  @since  16-Dec-2010
--%>

<div class="heading table-heading">
    <strong class="name"><g:message code="product.th.name"/></strong>
    <strong style="width: 120px"><g:message code="product.th.internal.number"/></strong>
</div>

<div id="products" class="table-box">
    <ul>
        <g:each var="product" in="${products}">
            <li id="product-${product.id}" <g:if test="${selectedProduct?.id == product.id}">class="active"</g:if>>
                <g:remoteLink breadcrumb="id" action="show" id="${product.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                    <span class="block last left" style="width: 120px">
                        <span>${product.internalNumber}</span>
                    </span>
                    <strong>${product.getDescription(session['language_id'])}</strong>
                    <em><g:message code="product.id.label" args="[product.id]"/></em>
                </g:remoteLink>
            </li>
        </g:each>
    </ul>
</div>

<g:if test="${products.totalCount > params.max}">
    <div class="pager-box">
        <g:set var="paginateAction" value="${actionName == 'products' ? 'products' : 'allProducts'}"/>
        <util:remotePaginate controller="product" action="${paginateAction}" id="${selectedCategoryId}" total="${products.totalCount}" update="column2"/>
    </div>
</g:if>

<div class="btn-box">
    <g:if test="${selectedCategoryId}">
        <g:link action="createProduct" params="['category': selectedCategoryId]" class="submit add"><span><g:message code="button.create.product"/></span></g:link>
    </g:if>
    <a href="#" onclick="return deleteProduct();" class="submit delete"><span><g:message code="button.delete"/></span></a>
    <g:remoteLink action="allProducts" update="column2" class="submit show"><span><g:message code="button.show.all"/></span></g:remoteLink>
</div>


<%-- delete item dialog --%>
<div id="product-delete-confirm" title="${message(code: 'popup.confirm.title')}">
    <g:formRemote name="product-delete-form" url="[action: 'deleteProduct']" update="column2">
        <g:hiddenField name="id" value="${selectedProduct?.id}"/>
        <g:hiddenField name="category" value="${selectedCategoryId}"/>
    </g:formRemote>

    <p class="dialog-text">
        <img src="/jbilling/images/icon34.gif" alt="">
        <g:message code="product.delete.confirm"/>
    </p>
</div>

<script type="text/javascript">
    $('#product-delete-confirm').dialog({
        autoOpen: false,
        height: 200,
        width: 400,
        modal: true,
        buttons: {
            "${message(code: 'prompt.yes')}": function() {
                $('#product-delete-form input#id').val(getSelectedId($('#products')));
                $('#product-delete-form').submit();
                closePanel($('#column3'));
                $(this).dialog("close")
            },
            "${message(code: 'prompt.no')}": function() {
                $(this).dialog("close");
            }
        }
    });

    function deleteProduct() {
        $('#product-delete-confirm').dialog('open');
        return false;
    }
</script>