

<%-- 
	Invoice list template. 
	
	@author Vikas Bodani
	@since 24-Dec-2010
 --%>
 
<div class="table-box">
	<div class="table-scroll">
		<table id="invoices" cellspacing="0" cellpadding="0">
			<thead>
                <tr>
                    <th><g:message code="label.gui.date"/></th>
                    <th class="small"><g:message code="invoice.label.duedate"/></th>
                    <th class="small"><g:message code="invoice.label.id"/></th>
                    <th><g:message code="invoice.label.status"/></th>
                    <th class="small"><g:message code="invoice.label.amount"/></th>
                    <th class="small"><g:message code="invoice.label.balance"/></th>
                </tr>
	        </thead>
	        
	        <tbody>
			<g:each var="inv" in="${invoices}">
            
                <g:set var="currency" value="${currencies.find{ it.id == inv?.currencyId}}"/>
                
				<tr id="invoice-${inv.id}" class="${invoice?.id == inv.id ? 'active' : ''}">
	            	<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                            <g:formatDate date="${inv?.getCreateDatetime()}" dateFormat="date.pretty.format"/>
						</g:remoteLink>
					</td>
					<td class="small">
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                            <g:formatDate date="${inv?.dueDate}" dateFormat="date.pretty.format"/>
						</g:remoteLink>
					</td>
					<td class="small">
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                            ${inv.id }
						</g:remoteLink>
					</td>
					<td>
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                            ${inv.getInvoiceStatus().getDescription(session['language_id']) }
						</g:remoteLink>
					</td>
					<td class="small">
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                            <g:formatNumber number="${inv.total}"  type="currency" currencySymbol="${currency?.symbol}"/>
						</g:remoteLink>
					</td>
					<td class="small">
						<g:remoteLink breadcrumb="id" class="cell" action="show" id="${inv.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                            <g:formatNumber number="${inv.balance}" type="currency" currencySymbol="${currency?.symbol}"/>
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
            <g:link action="csv" id="${invoice?.id}">
                <g:message code="download.csv.link"/>
            </g:link>
        </div>
    </div>

    <div class="row">
        <util:remotePaginate controller="invoice" action="list" params="[applyFilter: true]" total="${invoices?.totalCount ?: 0}" update="column1"/>
    </div>
</div>

<div class="btn-box">
    <div class="row"></div>
</div>
