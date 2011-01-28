<g:set var="languageId" value="${session['language_id'] as Integer}"/>
<g:set var="entityId" value="${session['company_id'] as Integer}"/>

<div class="table-box">
    <table cellpadding="0" cellspacing="0">
        <thead>
            <th><g:message code="title.notification" /></th>
            <th><g:message code="title.notification.active" /></th>
        </thead>
        <tbody>
		<g:each in="${lstByCategory}" status="idx" var="dto">
			<tr>
    			<td><g:remoteLink breadcrumb="id" class="cell" action="show" id="${dto.id}" params="['template': 'show']"
    	                   before="register(this);" onSuccess="render(data, next);">
    				    <strong>${dto.getDescription(languageId)}</strong></g:remoteLink></td>
                <td>
    				<g:set var="flag" value="${true}"/> 
    				<g:each status="iter" var="var" in="${dto.getNotificationMessages()}">
    					<g:if test="${flag}">
    						<g:if test="${languageId == var.language.id 
    							&& var.entity.id == entityId && var.useFlag > 0}">
    								<g:set var="flag" value="${false}"/>
    						</g:if>
    					</g:if>
    				</g:each> 
    				<span class="block">
    					<span>
        					<g:if test="${flag}">
        						<g:message code="prompt.no"/>
        					</g:if>
        					<g:else>
        						<g:message code="prompt.yes"/>
        					</g:else>
    					</span>
    				</span>
    			</td>
            </tr>
		</g:each>
	</tbody>
    </table>
</div>
<div class="btn-box">
</div>
