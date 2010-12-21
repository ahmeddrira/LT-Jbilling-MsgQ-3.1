<%@page import="com.sapienter.jbilling.server.order.db.OrderLineTypeDTO"%>

<%--
  Categories list

  @author Brian Cowdery
  @since  16-Dec-2010
--%>

<div class="table-box">
    <table id="categories" cellspacing="0" cellpadding="0">
        <thead>
            <th><g:message code="product.category.th.name"/></th>
            <th class="small"><g:message code="product.category.th.type"/></th>
        </thead>
        <tbody>
        <g:each var="category" in="${categories}">
            <g:set var="lineType" value="${new OrderLineTypeDTO(category.orderLineTypeId, 0)}"/>

                <tr id="category-${category.id}" class="${selectedCategoryId == category.id ? 'active' : ''}">
                    <td>
                        <g:remoteLink breadcrumb="{'action':'list'}" class="cell double" action="products" id="${category.id}" before="register(this);" onSuccess="render(data, next);">
                            <strong>${category.description}</strong>
                            <em><g:message code="product.category.id.label" args="[category.id]"/></em>
                        </g:remoteLink>
                    </td>
                    <td>
                        <g:remoteLink breadcrumb="{'action':'list'}" class="cell" action="products" id="${category.id}" before="register(this);" onSuccess="render(data, next);">
                            <span>${lineType.description}</span>
                        </g:remoteLink>
                    </td>
                </tr>

            </g:each>
        </tbody>
    </table>
</div>

<g:if test="${categories?.totalCount > params.max}">
    <div class="pager-box">
        <util:remotePaginate controller="product" action="categories" total="${categories.totalCount}" update="column1"/>
    </div>
</g:if>

<div class="btn-box">
    <%-- breadcrumb for categories handled in the controller, cannot bind breadcrumb to the control form --%>
    <g:link action="editCategory" class="submit add"><span><g:message code="button.create.category"/></span></g:link>
    <a href="#" onclick="return editCategory();" class="submit edit"><span><g:message code="button.edit"/></span></a>
</div>


<!-- edit category control form -->
<g:form name="category-edit-form" url="[action: 'editCategory']">
    <g:hiddenField name="id" value="${selectedCategoryId}"/>
</g:form>

<script type="text/javascript">
    function editCategory() {
        $('#category-edit-form input#id').val(getSelectedId('#categories'));
        $('#category-edit-form').submit();
        return false;
    }
</script>