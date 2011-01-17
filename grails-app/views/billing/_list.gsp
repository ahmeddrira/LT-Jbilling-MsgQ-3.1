<%@ page import="com.sapienter.jbilling.server.util.Util" %>
<%@ page import="com.sapienter.jbilling.server.process.db.BillingProcessDTO"%>
	
<div class="table-box">
	<div class="table-scroll">
    	<table id="processes" cellspacing="0" cellpadding="0">
			<thead>
				<tr>
					<th class="small"><g:message code="label.billing.cycle.id" /></th>
					<th class="medium"><g:message code="label.billing.cycle.date" /></th>
					<th class="small"><g:message code="label.billing.invoice.count" /></th>
					<th class="small"><g:message code="label.billing.total.invoiced" /></th>
					<th class="small"><g:message code="label.billing.currency.code" /></th>
				</tr>
			</thead>
	
			<tbody>
				<g:each var="dto" in="${lstBillingProcesses}">
                    <g:link action="show" id="${dto.id}">
    					<tr id="process-${dto.id}" class="${selected?.id == dto.id ? 'active' : ''}">
							<td class="small">${dto.id}</td>
							<td class="medium">
								${new java.text.SimpleDateFormat("dd-MMM-yyyy").format(dto.billingDate)}
							</td>
							<td class="small">${dataHashMap[dto.id][0]}</td>
							<td class="small">
								${Util.formatMoney(new BigDecimal(dataHashMap[dto.id][1]?:"0.0"),
									session["user_id"],dataHashMap[dto.id][2].id, false)?.substring(2)}
							</td>
							<td class="small">${dataHashMap[dto.id][2].code}</td>
    					</tr>
                    </g:link>
				</g:each>
			</tbody>
		</table>
	</div>
</div>

<g:if test="${BillingProcessDTO.count() > params.max}">
    <div class="pager-box">
        <util:remotePaginate controller="billing" action="index" params="[applyFilter: true]" total="${BillingProcessDTO.count()}" update="column1"/>
    </div>
</g:if>
