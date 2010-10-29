<div>
<table id="catTbl" cellspacing='4' class="link-table">
	<thead>
		<tr>
			<th><g:message code="title.notification" /></th>
			<th><g:message code="title.notification.active" /></th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${lst}" status="idx" var="dto">
			<tr>
				<td><g:hiddenField id="typeId" name="typeId${idx}"
					value="${dto?.getId()}"/> ${dto.getDescription(languageId)}
				</td>
				<td>
					<g:set var="flag" value="${true}"/>
					<g:each status="iter" var="var"
					in="${dto.getNotificationMessages()}">
					<g:if test="${iter == 0 && var.getUseFlag() > 0}">
						<g:checkBox name="active" checked="true" />
						<g:set var="flag" value="${false}"/>
					</g:if>
				</g:each>
					<g:if test="${flag}">
						<g:checkBox name="active" checked="false" />
					</g:if>
				</td>
			</tr>
		</g:each>
	</tbody>
</table>
</div>
