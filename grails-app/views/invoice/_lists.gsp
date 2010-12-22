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
				<tr id="product-${inv.id}">
	            	<td>
						<g:remoteLink breadcrumb="id" class="cell double" action="show" id="${inv.id}" params="[userId: userId]"
			     			before="register(this);" onSuccess="render(data, next);">
							<span>
								${Util.formatDate(inv?.getCreateDatetime(), session["user_id"]) }
							</span>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="[userId: userId]"
			     			before="register(this);" onSuccess="render(data, next);">
							<span>
								${Util.formatDate(inv?.dueDate, session["user_id"]) }
							</span>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="[userId: userId]"
				     			before="register(this);" onSuccess="render(data, next);">
							<span>
								${inv.id }
							</span>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="[userId: userId]"
				     			before="register(this);" onSuccess="render(data, next);">
							<span>
								${inv.number }
							</span>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="[userId: userId]"
				     			before="register(this);" onSuccess="render(data, next);">
							<span>
								${inv.getInvoiceStatus().getDescription(session['language_id']) }
							</span>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="[userId: userId]"
				     			before="register(this);" onSuccess="render(data, next);">
							<span>
								<g:if test="${null == inv.total }">&nbsp;</g:if>
								<g:else>
								${Util.formatMoney(new BigDecimal(inv.total),
									session["user_id"],
									inv.currencyId, 
									false)}
								</g:else>
							</span>
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="[userId: userId]"
				     			before="register(this);" onSuccess="render(data, next);">
							<span>
								<g:if test="${null == inv.balance }">&nbsp;</g:if>
								<g:else>
								${Util.formatMoney(new BigDecimal( inv.balance ),
									session["user_id"],
									inv.currencyId, 
									false)}
								</g:else>
							</span>
						</g:remoteLink>
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</div>


<g:if test="${invoices?.totalCount > params.max}">
    <div class="pager-box">
        <util:remotePaginate controller="invoice" action="list" id="${userId == null ? '' : userId}" total="${invoces.totalCount}" update="column2"/>
    </div>
</g:if>

<div class="btn-box">
</div>