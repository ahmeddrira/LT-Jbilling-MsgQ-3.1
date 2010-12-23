
<%--
  Products list

  @author Brian Cowdery
  @since  16-Dec-2010
--%>

<%-- list of products --%>
<g:if test="${products}">
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
</g:if>

<%-- no products to show --%>
<g:if test="${!products}">
    <div class="heading"><strong><em><g:message code="product.category.no.products.title"/></em></strong></div>
    <div class="box">
        <g:if test="${selectedCategoryId}">
            <em><g:message code="product.category.no.products.warning"/></em>
        </g:if>
        <g:else>
            <em><g:message code="product.category.not.selected.message"/></em>
        </g:else>
    </div>
</g:if>

<g:if test="${products?.totalCount > params.max}">
    <div class="pager-box">
        <g:set var="paginateAction" value="${actionName == 'products' ? 'products' : 'allProducts'}"/>
        <util:remotePaginate controller="product" action="${paginateAction}" id="${selectedCategoryId}" total="${products.totalCount}" update="column2"/>
    </div>
</g:if>

<div class="btn-box">
    <g:if test="${selectedCategoryId}">
        <g:link action="editProduct" params="['category': selectedCategoryId]" class="submit add"><span><g:message code="button.create.product"/></span></g:link>
        <g:if test="${!products}">
            <a onclick="showConfirm('deleteCategory-${selectedCategoryId}');" class="submit delete"><span><g:message code="button.delete.category"/></span></a>
        </g:if>
    </g:if>
    <g:remoteLink action="allProducts" update="column2" class="submit show"><span><g:message code="button.show.all"/></span></g:remoteLink>
</div>

<g:render template="/confirm"
          model="['message':'product.category.delete.confirm',
                  'controller':'product',
                  'action':'deleteCategory',
                  'id':selectedCategoryId,
                  'ajax':true,
                  'update':'column1',
                 ]"/>