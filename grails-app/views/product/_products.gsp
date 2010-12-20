
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
                <g:remoteLink breadcrumb="id" action="show" id="${product.id}" params="['template': 'show', 'category': selectedCategoryId]" before="register(this);" onSuccess="render(data, next);">
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

<g:if test="${products?.totalCount > params.max}">
    <div class="pager-box">
        <g:set var="paginateAction" value="${actionName == 'products' ? 'products' : 'allProducts'}"/>
        <util:remotePaginate controller="product" action="${paginateAction}" id="${selectedCategoryId}" total="${products.totalCount}" update="column2"/>
    </div>
</g:if>

<div class="btn-box">
    <g:if test="${selectedCategoryId}">
        <g:link action="editProduct" params="['category': selectedCategoryId]" breadcrumb="{'name': 'create'}" class="submit add"><span><g:message code="button.create.product"/></span></g:link>
        <a onclick="showConfirm(${selectedCategoryId});" class="submit delete"><span><g:message code="button.delete.category"/></span></a>
        <g:remoteLink action="allProducts" update="column2" class="submit show"><span><g:message code="button.show.all"/></span></g:remoteLink>
    </g:if>
</div>

<g:render template="/confirm"
          model="['message':'product.category.delete.confirm',
                  'controller':'product',
                  'action':'deleteCategory',
                  'id':selectedCategoryId,
                  'ajax':true,
                  'update':'column1',
                 ]"/>