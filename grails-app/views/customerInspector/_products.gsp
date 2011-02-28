<%@ page import="com.sapienter.jbilling.server.user.db.CompanyDTO; com.sapienter.jbilling.server.item.db.ItemTypeDTO" %>

<%--
  Shows the product list and provides some basic filtering capabilities.

  @author Brian Cowdery
  @since 28-Feb-2011
--%>

<div id="product-box">

    <!-- filter -->
    <div class="form-columns">
        <g:formRemote name="products-filter-form" url="[action: 'filterProducts']" update="product-box">
            <g:applyLayout name="form/input">
                <content tag="label"><g:message code="filters.title"/></content>
                <content tag="label.for">filterBy</content>
                <g:textField name="filterBy" class="field default" placeholder="${message(code: 'products.filter.by.default')}" value="${params.filterBy}"/>
            </g:applyLayout>

            <g:applyLayout name="form/select">
                <content tag="label"><g:message code="order.label.products.category"/></content>
                <content tag="label.for">typeId</content>
                <g:select name="typeId" from="${itemTypes}"
                          noSelection="['': message(code: 'filters.item.type.empty')]"
                          optionKey="id" optionValue="description"
                          value="${params.typeId}"/>
            </g:applyLayout>
        </g:formRemote>

        <script type="text/javascript">
            $(function() {
                $('#filterBy').blur(function() { $('#products-filter-form').submit(); });
                $('#typeId').change(function() { $('#products-filter-form').submit(); });
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
                            <g:remoteLink class="cell double" action="productPrices" id="${product.id}" params="[userId: user?.id]" update="prices-column">
                                <strong>${product.getDescription(session['language_id'])}</strong>
                            </g:remoteLink>
                        </td>
                        <td class="small">
                            <g:remoteLink class="cell" action="productPrices" id="${product.id}" params="[userId: user?.id]" update="prices-column">
                                <span>${product.internalNumber}</span>
                            </g:remoteLink>
                        </td>
                        <td class="medium">
                            <g:remoteLink class="cell" action="productPrices" id="${product.id}" params="[userId: user?.id]" update="prices-column">
                                <g:if test="${product.percentage}">
                                    %<g:formatNumber number="${product.percentage}" formatName="money.format"/>
                                </g:if>
                                <g:else>
                                    <g:formatNumber number="${product.defaultPrice?.rate}" type="currency" currencySymbol="${product.defaultPrice?.currency?.symbol}"/>
                                </g:else>
                            </g:remoteLink>
                        </td>
                    </tr>
                </g:each>

                </tbody>
            </table>
        </div>
    </div>

</div>