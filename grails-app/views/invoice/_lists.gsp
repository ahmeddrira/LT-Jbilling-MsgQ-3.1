<%@ page import="com.sapienter.jbilling.server.util.Util"%>

<div class="table-box">
	<table id="invoices" cellspacing="0" cellpadding="0">
		<thead>
            <th><g:message code="label.gui.date"/></th>
            <th class="medium"><g:message code="invoice.label.duedate"/></th>
            <th class="medium"><g:message code="invoice.label.id"/></th>
            <th class="medium">#</th> <!-- # for Invoice Number -->
            <th class="medium"><g:message code="invoice.label.status"/></th>
            <th class="medium"><g:message code="invoice.label.amount"/></th>
            <th class="medium"><g:message code="invoice.label.balance"/></th>
        </thead>
        <tbody>
			<g:each var="inv" in="${invoices}">
				<tr id="product-${inv.id}"  class="${invoice?.id == inv.id ? 'active' : ''}">
	            	<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
			     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								${Util.formatDate(inv?.getCreateDatetime(), session["user_id"]) }
							</strong>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
			     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								${Util.formatDate(inv?.dueDate, session["user_id"]) }
							</strong>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
				     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								${inv.id }
							</strong>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
				     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								${inv.number }
							</strong>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
				     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								${inv.getInvoiceStatus().getDescription(session['language_id']) }
							</strong>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
				     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								<g:if test="${null == inv.total }">&nbsp;</g:if>
								<g:else>
								${Util.formatMoney(new BigDecimal(inv.total),
									session["user_id"],
									inv.currencyId, 
									false)}
								</g:else>
							</strong>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
				     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								<g:if test="${null == inv.balance }">&nbsp;</g:if>
								<g:else>
								${Util.formatMoney(new BigDecimal( inv.balance ),
									session["user_id"],
									inv.currencyId, 
									false)}
								</g:else>
							</strong>
						</g:remoteLink>
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</div>


<g:if test="${invoices?.totalCount > params.max}">
    <div class="pager-box">
        <util:remotePaginate controller="invoice" action="list" total="${invoces.totalCount}" update="column2"/>
    </div>
</g:if>

<div class="btn-box">
</div>