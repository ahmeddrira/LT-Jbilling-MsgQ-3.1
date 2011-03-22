
<%--
  Payment list table.

  @author Brian Cowdery
  @since  04-Jan-2011
--%>

<div class="table-box">
    <div class="table-scroll">
        <table id="payments" cellspacing="0" cellpadding="0">
            <thead>
                <tr>
                    <th><g:message code="payment.th.id"/></th>
                    <th class="medium"><g:message code="payment.th.date"/></th>
                    <th class="tiny"><g:message code="payment.th.payment.or.refund"/></th>
                    <th class="small"><g:message code="payment.th.amount"/></th>
                    <th class="small"><g:message code="payment.th.method"/></th>
                    <th class="small"><g:message code="payment.th.result"/></th>
                </tr>
            </thead>

            <tbody>
            <g:each var="payment" in="${payments}">
                <tr id="payment-${payment.id}" class="${selected?.id == payment.id ? 'active' : ''}">

                    <td>
                        <g:remoteLink class="cell" action="show" id="${payment.id}" before="register(this);" onSuccess="render(data, next);">
                            <span>${payment.id}</span>
                        </g:remoteLink>
                    </td>
                    <td class="medium">
                        <g:remoteLink class="cell" action="show" id="${payment.id}" before="register(this);" onSuccess="render(data, next);">
                            <span><g:formatDate date="${payment.paymentDate}" formatName="date.pretty.format"/></span>
                        </g:remoteLink>
                    </td>
                    <td class="tiny">
                        <g:remoteLink class="cell" action="show" id="${payment.id}" before="register(this);" onSuccess="render(data, next);">
                            <g:if test="${payment.isRefund > 0}">
                                <span>R</span>
                            </g:if>
                            <g:else>
                                <span>P</span>
                            </g:else>
                        </g:remoteLink>
                    </td>
                    <td class="small">
                        <g:remoteLink class="cell" action="show" id="${payment.id}" before="register(this);" onSuccess="render(data, next);">
                            <span><g:formatNumber number="${payment.amount}" type="currency" currencySymbol="${payment.currencyDTO.symbol}"/></span>
                        </g:remoteLink>
                    </td>
                    <td class="small">
                        <g:remoteLink class="cell" action="show" id="${payment.id}" before="register(this);" onSuccess="render(data, next);">
                            <span>${payment.paymentMethod.getDescription(session['language_id'])}</span>
                        </g:remoteLink>
                    </td>
                    <td class="small">
                        <g:remoteLink class="cell" action="show" id="${payment.id}" before="register(this);" onSuccess="render(data, next);">
                            <span>${payment.paymentResult.getDescription(session['language_id'])}</span>
                        </g:remoteLink>
                    </td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
</div>

<div class="pager-box">
    <div class="row">
        <div class="results">
            <g:render template="/layouts/includes/pagerShowResults" model="[steps: [10, 20, 50], update: 'column1']"/>
        </div>
        <div class="download">
            <g:link action="csv" id="${selected?.id}">
                <g:message code="download.csv.link"/>
            </g:link>
        </div>
    </div>

    <div class="row">
        <util:remotePaginate controller="payment" action="list" params="[applyFilter: true]" total="${payments?.totalCount ?: 0}" update="column1"/>
    </div>
</div>

<div class="btn-box">
    <g:link action="create" class="submit payment"><span><g:message code="button.create.payment"/></span></g:link>
</div>