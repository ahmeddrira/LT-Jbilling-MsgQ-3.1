
<div class="table-box">
	<div class="table-scroll">
    	<table id="processes" cellspacing="0" cellpadding="0">
			<thead>
				<tr>
					<th class="large"><g:message code="mediation.th.id" /></th>
					<th class="small2"><g:message code="mediation.th.start.date" /></th>
					<th class="small2"><g:message code="mediation.th.end.date" /></th>
					<th class="small"><g:message code="mediation.th.total.records" /></th>
                    <th class="small"><g:message code="mediation.th.orders.affected"/></th>
				</tr>
			</thead>
	
			<tbody>
				<g:each var="proc" in="${processes}">
					<tr id="mediation-${proc.id}" class="${proc?.id == processId ? 'active' : ''}">
						<td>
                            <g:remoteLink breadcrumb="id" class="cell double" action="show" id="${proc.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">

                                <strong>${proc.id}</strong>
                                <em>${proc.configuration.name}</em>
                            </g:remoteLink>
                        </td>
						<td>
							<g:remoteLink breadcrumb="id" class="cell" action="show" id="${proc.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                                <g:formatDate date="${proc.startDatetime}" formatName="date.timeSecs.format"/>
                            </g:remoteLink>
						</td>
                        <td>
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${proc.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                                <g:formatDate date="${proc.endDatetime}" formatName="date.timeSecs.format"/>
                            </g:remoteLink>
                        </td>
						<td>
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${proc.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                                ${proc.records?.size()}
                            </g:remoteLink>
                        </td>
                        <td>
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${proc.id}" params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                                ${proc.ordersAffected}
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
    </div>

    <div class="row">
        <util:remotePaginate controller="mediation" action="index" params="[applyFilter: true]" total="${processes?.totalCount ?: 0}" update="column1"/>
    </div>
</div>

<div class="btn-box">
    <div class="row"></div>
</div>
