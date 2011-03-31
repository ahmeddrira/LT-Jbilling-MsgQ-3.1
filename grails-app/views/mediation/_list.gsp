
<div class="table-box">
	<div class="table-scroll">
    	<table id="processes" cellspacing="0" cellpadding="0">
			<thead>
				<tr>
					<th class="small"><g:message code="label.mediation.id" /></th>
                    <th class="small"><g:message code="label.mediation.config.id" /></th>
					<th class="medium"><g:message code="label.mediation.start.date" /></th>
					<th class="medium"><g:message code="label.mediation.end.date" /></th>
					<th class="large"><g:message code="label.mediation.total.records" /></th>
				</tr>
			</thead>
	
			<tbody>
				<g:each var="proc" in="${processes}">
					<tr id="mediation-${proc.id}" class="${proc?.id == processId ? 'active' : ''}">
						<td class="small">
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${proc.id}" params="['template': 'show']"
                                before="register(this);" onSuccess="render(data, next);">
                                <strong>${proc.id}</strong></g:remoteLink>
                        </td>
                        <td class="small">
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${proc.id}" params="['template': 'show']"
                                before="register(this);" onSuccess="render(data, next);">
                                <strong>${proc.configuration?.id}</strong></g:remoteLink>
                        </td>
						<td class="medium">
							<g:remoteLink breadcrumb="id" class="cell" action="show" id="${proc.id}" params="['template': 'show']"
                                before="register(this);" onSuccess="render(data, next);">
                                <strong>
                                    <g:formatDate date="${proc.startDatetime}" formatName="date.timeSecs.format"/>
                                </strong></g:remoteLink>
						</td>
                        <td class="medium">
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${proc.id}" params="['template': 'show']"
                                before="register(this);" onSuccess="render(data, next);">
                                <strong>
                                <g:formatDate date="${proc.endDatetime}" formatName="date.timeSecs.format"/>
                                </strong>
                            </g:remoteLink>
                        </td>
						<td class="large">
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${proc.id}" params="['template': 'show']"
                                before="register(this);" onSuccess="render(data, next);">
                                <strong>${proc.records?.size()}</strong></g:remoteLink>
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
