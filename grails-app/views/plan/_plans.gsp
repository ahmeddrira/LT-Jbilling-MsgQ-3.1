
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

<%--
  Plans list table.

  @author Brian Cowdery
  @since  01-Feb-2011
--%>

<div class="table-box">
    <div class="table-scroll">
        <table id="plans" cellspacing="0" cellpadding="0">
            <thead>
                <tr>
                    <th>
                        <g:remoteSort action="list" sort="id" update="column1">
                            <g:message code="plan.th.name"/>
                        </g:remoteSort>
                    </th>
                    <th class="medium">
                        <g:remoteSort action="list" sort="item.internalNumber" update="column1">
                            <g:message code="plan.th.item.number"/>
                        </g:remoteSort>
                    </th>
                    <th class="small">
                        <g:message code="plan.th.products"/>
                    </th>
                </tr>
            </thead>

            <tbody>
            <g:each var="plan" in="${plans}">
                <tr id="plan-${plan.id}" class="${selected?.id == plan.id ? 'active' : ''}">

                    <td>
                        <g:remoteLink class="cell double" action="show" id="${plan.id}" before="register(this);" onSuccess="render(data, next);">
                            <strong>${plan.item.description}</strong>
                            <em><g:message code="table.id.format" args="[plan.id]"/></em>
                        </g:remoteLink>
                    </td>
                    <td>
                        <g:remoteLink class="cell" action="show" id="${plan.id}" before="register(this);" onSuccess="render(data, next);">
                            <strong>${plan.item.internalNumber}</strong>
                        </g:remoteLink>
                    </td>
                    <td>
                        <g:remoteLink class="cell" action="show" id="${plan.id}" before="register(this);" onSuccess="render(data, next);">
                            <span>${plan.planItems?.size()}</span>
                        </g:remoteLink>
                    </td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
</div>

<g:if test="${plans?.totalCount > params.max}">
    <div class="pager-box">
        <div class="row left">
            <g:render template="/layouts/includes/pagerShowResults" model="[steps: [10, 20, 50], update: 'column1']"/>
        </div>
        <div class="row">
            <util:remotePaginate controller="plan" action="list" params="${sortableParams(params: [partial: true])}" total="${plans.totalCount}" update="column1"/>
        </div>
    </div>
</g:if>

<div class="btn-box">
    <g:link controller="planBuilder" action="edit" class="submit add"><span><g:message code="button.create"/></span></g:link>
</div>