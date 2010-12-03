<div class="heading table-heading">
    <strong class="first"><g:message code="title.notification"/></strong>
    <strong style="width:20%"><g:message code="title.notification.active"/></strong>
</div>
	
<div class="table-box">
	<ul>
		<g:each in="${lst}" status="idx" var="dto">
			<li>
			<g:remoteLink action="view" id="${dto.id}" 
		     			before="register(this);" onSuccess="render(data, next);">
				<strong>${dto.getDescription(languageId)}</strong>
				
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
			</g:remoteLink>
			</li>
		</g:each>
	</ul>
</div>
<div class="btn-box">
</div>
