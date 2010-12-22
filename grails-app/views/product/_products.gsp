
<%--
  Products list

  @author Brian Cowdery
  @since  16-Dec-2010
--%>

<div class="table-box">
    <table id="products" cellspacing="0" cellpadding="0">
        <thead>
            <th><g:message code="product.th.name"/></th>
            <th class="medium"><g:message code="product.th.internal.number"/></th>
        </thead>
        <tbody>
            <g:each var="product" in="${products}">

                <tr id="product-${product.id}" class="${selectedProduct?.id == product.id ? 'active' : ''}">
                    <td>
                        <g:remoteLink class="cell double" action="show" id="${product.id}" params="['template': 'show', 'category': selectedCategoryId]" before="register(this);" onSuccess="render(data, next);">
                            <strong>${product.getDescription(session['language_id'])}</strong>
                            <em><g:message code="product.id.label" args="[product.id]"/></em>
                        </g:remoteLink>
                    </td>
                    <td>
                        <g:remoteLink class="cell" action="show" id="${product.id}" params="['template': 'show', 'category': selectedCategoryId]" before="register(this);" onSuccess="render(data, next);">
                            <span>${product.internalNumber}</span>
                        </g:remoteLink>
                    </td>
                </tr>

            </g:each>
        </tbody>
    </table>
</div>

<g:if test="${products?.totalCount > params.max}">
    <div class="pager-box">
        <g:set var="paginateAction" value="${actionName == 'products' ? 'products' : 'allProducts'}"/>
        <util:remotePaginate controller="product" action="${paginateAction}" id="${selectedCategoryId}" total="${products.totalCount}" update="column2"/>
    </div>
</g:if>

<div class="btn-box">
    <g:if test="${selectedCategoryId}">
        <g:link action="editProduct" params="['category': selectedCategoryId]" class="submit add"><span><g:message code="button.create.product"/></span></g:link>
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