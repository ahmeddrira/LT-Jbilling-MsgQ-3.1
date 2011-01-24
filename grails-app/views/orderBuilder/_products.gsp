<%@ page import="com.sapienter.jbilling.server.user.db.CompanyDTO; com.sapienter.jbilling.server.item.db.ItemTypeDTO" %>

<div id="product-box">

    <!-- filter -->
    <div class="form-columns">
        <g:formRemote name="filter-form" url="[action: 'edit']" update="ui-tabs-2" method="GET">
            <g:hiddenField name="_eventId" value="products"/>
            <g:hiddenField name="execution" value="${flowExecutionKey}"/>

            <g:applyLayout name="form/input">
                <content tag="label">Filter By</content>
                <content tag="label.for">filterBy</content>
                <g:textField name="filterBy" class="field default" placeholder="${message(code: 'products.filter.by.default')}" value="${params.filterBy}"/>
            </g:applyLayout>

            <g:applyLayout name="form/select">
                <content tag="label">Product Category</content>
                <content tag="label.for">typeId</content>
                <g:select name="typeId" from="${company.itemTypes.sort{ it.id }}"
                          noSelection="['': message(code: 'filters.item.type.empty')]"
                          optionKey="id" optionValue="description"
                          value="${params.typeId}"/>
            </g:applyLayout>
        </g:formRemote>

        <script type="text/javascript">
            $(function() {
                $('#filterBy').blur(function() { $('#filter-form').submit(); });
                $('#typeId').change(function() { $('#filter-form').submit(); });

                placeholder();
            });
        </script>
    </div>

    <!-- product list -->
    <div class="table-box tab-table">
        <div class="table-scroll">
            <table id="products" cellspacing="0" cellpadding="0">
                <tbody>

                <g:each var="product" in="${products}">
                    <tr>
                        <td>
                            <strong>${product.getDescription(session['language_id'])}</strong>
                            <em><g:message code="product.id.label" args="[product.id]"/></em>
                        </td>
                        <td class="small">
                            <span>${product.internalNumber}</span>
                        </td>
                        <td class="medium">
                            <g:if test="${product.percentage}">
                                %<g:formatNumber number="${product.percentage}" formatName="money.format"/>
                            </g:if>
                            <g:else>
                                <g:formatNumber number="${product.defaultPrice?.rate}" type="currency" currencyCode="${product.defaultPrice?.currency?.code}"/>
                            </g:else>
                        </td>
                    </tr>
                </g:each>

                </tbody>
            </table>
        </div>
    </div>

    <!-- pager -->
    <g:if test="${products?.totalCount > params.int('max')}">
        <div class="pager-box">
            <util:remotePaginate action="products" total="${products.totalCount}" update="columns1"/>
        </div>
    </g:if>

</div>