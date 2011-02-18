<%@ page import="com.sapienter.jbilling.server.util.Util" %>
	
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
                                <strong>${new java.text.SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(proc.startDatetime)}
                                </strong></g:remoteLink>
						</td>
                        <td class="medium">
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${proc.id}" params="['template': 'show']"
                                before="register(this);" onSuccess="render(data, next);">
                                <strong>${new java.text.SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(proc.endDatetime)}
                                </strong></g:remoteLink>
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

<g:if test="${processes?.totalCount > params.max}">
    <div class="pager-box">
        <util:remotePaginate controller="mediation" action="index" params="[applyFilter: true]" total="${processes?.totalCount}" update="column1"/>
    </div>
</g:if>
