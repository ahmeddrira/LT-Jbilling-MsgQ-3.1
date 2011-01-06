
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
                            <span><g:formatDate date="${payment.paymentDate}" formatName="date.format"/></span>
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
                            <span><g:formatNumber number="${payment.amount}" type="currency" currencyCode="${payment.currencyDTO.code}"/></span>
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

<g:if test="${payments?.totalCount > params.max}">
    <div class="pager-box">
        <util:remotePaginate controller="payment" action="list" params="[applyFilter: true]" total="${payments.totalCount}" update="column1"/>
    </div>
</g:if>

<div class="btn-box">
    <g:link action='create' class="submit payment"><span><g:message code="button.create.payment"/></span></g:link>
</div>