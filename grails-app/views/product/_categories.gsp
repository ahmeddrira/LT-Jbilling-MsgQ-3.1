<%@page import="com.sapienter.jbilling.server.order.db.OrderLineTypeDTO"%>

<%--
  Categories list

  @author Brian Cowdery
  @since  16-Dec-2010
--%>

<div class="heading table-heading">
    <strong class="name"><g:message code="product.category.th.name"/></strong>
    <strong style="width: 60px;"><g:message code="product.category.th.type"/></strong>
</div>

<div id="categories" class="table-box">
    <ul>
        <g:each var="category" in="${categories}">
            <g:set var="lineType" value="${new OrderLineTypeDTO(category.orderLineTypeId, 0)}"/>

            <li id="category-${category.id}" <g:if test="${selectedCategoryId == category.id}">class="active"</g:if>>
                <g:remoteLink breadcrumb="{'action':'list'}" action="products" id="${category.id}" before="register(this);" onSuccess="render(data, next);">
                    <span class="block last left">
                        <span>${lineType.description}</span>
                    </span>
                    <strong>${category.description}</strong>
                    <em><g:message code="product.category.id.label" args="[category.id]"/></em>
                </g:remoteLink>
            </li>
        </g:each>
    </ul>
</div>

<g:if test="${categories?.totalCount > params.max}">
    <div class="pager-box">
        <util:remotePaginate controller="product" action="categories" total="${categories.totalCount}" update="column1"/>
    </div>
</g:if>

<div class="btn-box">
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