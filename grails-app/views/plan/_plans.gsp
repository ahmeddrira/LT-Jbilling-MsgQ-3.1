<%@ page import="org.apache.commons.lang.StringUtils" %>

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
                    <th><g:message code="plan.th.id"/></th>
                    <th class="medium"><g:message code="plan.th.item"/></th>
                    <th class="small"><g:message code="plan.th.prices"/></th>
                </tr>
            </thead>

            <tbody>
            <g:each var="plan" in="${plans}">
                <tr id="plan-${plan.id}" class="${selected?.id == plan.id ? 'active' : ''}">

                    <td>
                        <g:remoteLink class="cell double" action="show" id="${plan.id}" before="register(this);" onSuccess="render(data, next);">
                            <strong>${StringUtils.abbreviate(plan.description, 50)}</strong>
                            <em><g:message code="product.id.label" args="[plan.id]"/></em>
                        </g:remoteLink>
                    </td>
                    <td>
                        <g:remoteLink class="cell double" action="show" id="${plan.id}" before="register(this);" onSuccess="render(data, next);">
                            <strong>${plan.item.internalNumber}</strong>
                            <em><g:message code="product.id.label" args="[plan.item.id]"/></em>
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
            <util:remotePaginate controller="plan" action="list" params="[applyFilter: true]" total="${plans.totalCount}" update="column1"/>
        </div>
    </div>
</g:if>

<div class="btn-box">
    <g:link controller="planBuilder" action="edit" class="submit add"><span><g:message code="button.create"/></span></g:link>
</div>