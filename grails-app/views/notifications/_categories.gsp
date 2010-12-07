<div class="heading table-heading">
	<strong style="width: 100%">
		<g:message code="title.notification.category"/>
	</strong>
</div>

<div class="table-box">
	<ul>
		<g:each in="${lst}" status="idx" var="dto">
			<li>
				<g:remoteLink action="lists" id="${dto.id}" 
			     		before="register(this);" onSuccess="render(data, next);">
					<strong>${dto.getDescription(languageId)}</strong>
				</g:remoteLink>
			</li>
		</g:each>
	</ul>
</div>
<div class="btn-box">	
	<g:remoteLink action="preferences" class="submit"  
		before="register(this);" onSuccess="render(data, next);">
		<span><g:message code="button.preferences"/></span>
	</g:remoteLink>
</div>