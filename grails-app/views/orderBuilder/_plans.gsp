%{--
  jBilling - The Enterprise Open Source Billing System
  Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

  This file is part of jbilling.

  jbilling is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  jbilling is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
  --}%

<%@ page import="com.sapienter.jbilling.server.user.db.CompanyDTO; com.sapienter.jbilling.server.item.db.ItemTypeDTO" %>

<%--
  Shows the plans list and provides some basic filtering capabilities.

  @author Brian Cowdery
  @since 02-Feb-2011
--%>

<div id="product-box">

    <!-- filter -->
    <div class="form-columns">
        <g:formRemote name="plans-filter-form" url="[action: 'edit']" update="ui-tabs-3" method="GET">
            <g:hiddenField name="_eventId" value="plans"/>
            <g:hiddenField name="execution" value="${flowExecutionKey}"/>

            <g:applyLayout name="form/input">
                <content tag="label"><g:message code="filters.title"/></content>
                <content tag="label.for">filterBy</content>
                <g:textField name="filterBy" class="field default" placeholder="${message(code: 'products.filter.by.default')}" value="${params.filterBy}"/>
            </g:applyLayout>
        </g:formRemote>

        <script type="text/javascript">
            $(function() {
                $('#plans-filter-form :input[name=filterBy]').blur(function() { $('#plans-filter-form').submit(); });
                placeholder();
            });
        </script>
    </div>

    <!-- product list -->
    <div class="table-box tab-table">
        <div class="table-scroll">
            <table id="plans" cellspacing="0" cellpadding="0">
                <tbody>

                <g:each var="plan" in="${plans}">
                    <tr>
                        <td>
                            <g:remoteLink class="cell double" action="edit" id="${plan.id}" params="[_eventId: 'addPlan']" update="column2" method="GET">
                                <strong>${plan.getDescription(session['language_id'])}</strong>
                                <em><g:message code="table.id.format" args="[plan.id]"/></em>
                            </g:remoteLink>
                        </td>
                        <td class="small">
                            <g:remoteLink class="cell double" action="edit" id="${plan.id}" params="[_eventId: 'addPlan']" update="column2" method="GET">
                                <span>${plan.internalNumber}</span>
                            </g:remoteLink>
                        </td>
                        <td class="medium">
                            <g:remoteLink class="cell double" action="edit" id="${plan.id}" params="[_eventId: 'addPlan']" update="column2" method="GET">
                                <g:if test="${plan.percentage}">
                                    %<g:formatNumber number="${plan.percentage}" formatName="money.format"/>
                                </g:if>
                                <g:else>
                                    <g:formatNumber number="${plan.defaultPrice?.rate}" type="currency" currencySymbol="${plan.defaultPrice?.currency?.symbol}"/>
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