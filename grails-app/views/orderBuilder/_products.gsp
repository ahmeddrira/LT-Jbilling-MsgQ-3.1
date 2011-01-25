<%@ page import="com.sapienter.jbilling.server.user.db.CompanyDTO; com.sapienter.jbilling.server.item.db.ItemTypeDTO" %>

<%--
  Shows the product list and provides some basic filtering capabilities.

  @author Brian Cowdery
  @since 23-Jan-2011
--%>

<div id="product-box">

    <!-- filter -->
    <div class="form-columns">
        <g:formRemote name="filter-form" url="[action: 'edit']" update="ui-tabs-2" method="GET">
            <g:hiddenField name="_eventId" value="products"/>
            <g:hiddenField name="execution" value="${flowExecutionKey}"/>

            <g:applyLayout name="form/input">
                <content tag="label"><g:message code="filters.title"/></content>
                <content tag="label.for">filterBy</content>
                <g:textField name="filterBy" class="field default" placeholder="${message(code: 'products.filter.by.default')}" value="${params.filterBy}"/>
            </g:applyLayout>

            <g:applyLayout name="form/select">
                <content tag="label"><g:message code="order.label.products.category"/></content>
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
                            <g:remoteLink class="cell double" action="edit" id="${product.id}" params="[_eventId: 'add']" update="column2" method="GET">
                                <strong>${product.getDescription(session['language_id'])}</strong>
                                <em><g:message code="product.id.label" args="[product.id]"/></em>
                            </g:remoteLink>
                        </td>
                        <td class="small">
                            <g:remoteLink class="cell double" action="edit" id="${product.id}" params="[_eventId: 'add']" update="column2" method="GET">
                                <span>${product.internalNumber}</span>
                            </g:remoteLink>
                        </td>
                        <td class="medium">
                            <g:remoteLink class="cell double" action="edit" id="${product.id}" params="[_eventId: 'add']" update="column2" method="GET">
                                <g:if test="${product.percentage}">
                                    %<g:formatNumber number="${product.percentage}" formatName="money.format"/>
                                </g:if>
                                <g:else>
                                    <g:formatNumber number="${product.defaultPrice?.rate}" type="currency" currencyCode="${product.defaultPrice?.currency?.code}"/>
                                </g:else>
                            </g:remoteLink>
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