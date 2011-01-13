<%@ page import="com.sapienter.jbilling.server.util.Util"%>

<%-- 
	Invoice list template. 
	
	@author Vikas Bodani
	@since 24-Dec-2010
 --%>
 
<div class="table-box">
	<div class="table-scroll">
		<table id="invoices" cellspacing="0" cellpadding="0">
			<thead>
	            <th><g:message code="label.gui.date"/></th>
	            <th class="small"><g:message code="invoice.label.duedate"/></th>
	            <th class="small"><g:message code="invoice.label.id"/></th>
	            <th class="small">#</th> <!-- # for Invoice Number -->
	            <th><g:message code="invoice.label.status"/></th>
	            <th class="small"><g:message code="invoice.label.amount"/></th>
	            <th class="small"><g:message code="invoice.label.balance"/></th>
	        </thead>
	        
	        <tbody>
			<g:each var="inv" in="${invoices}">
				<tr id="product-${inv.id}"  class="${invoice?.id == inv.id ? 'active' : ''}">
	            	<td class="medium">
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
			     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								${Util.formatDate(inv?.getCreateDatetime(), session["user_id"]) }
							</strong>
						</g:remoteLink>
					</td>
					<td class="medium">
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
			     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								${Util.formatDate(inv?.dueDate, session["user_id"]) }
							</strong>
						</g:remoteLink>
					</td>
					<td class="small">
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
				     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								${inv.id }
							</strong>
						</g:remoteLink>
					</td>
					<td class="small">
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
				     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								${inv.number }
							</strong>
						</g:remoteLink>
					</td>
					<td class="large">
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']"
				     			before="register(this);" onSuccess="render(data, next);">
							<strong>
								${inv.getInvoiceStatus().getDescription(session['language_id']) }
							</strong>
						</g:remoteLink>
					</td>
					<td class="medium">
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
					<td class="medium">
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
</div>

<g:if test="${invoices?.totalCount > params.max}">
    <div class="pager-box">
        <util:remotePaginate controller="invoice" action="list" params="[applyFilter: true]" total="${invoces.totalCount}" update="column2"/>
    </div>
</g:if>
