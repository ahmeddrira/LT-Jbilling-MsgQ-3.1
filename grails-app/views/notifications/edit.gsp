<html>
<head><script language="javascript"></script>
<title>${title}</title>
</head>
<script language="javascript">
</script>
<body>
<h2><g:message code="prompt.edit.notification" /></h2>
<g:form action="saveNotification">
	<g:hiddenField name="_id" value="${params.id}" />
	<g:hiddenField name="msgDTOId" value="${dto?.getId()}" />
	<g:hiddenField name="entity.id" value="${entityId}" />
	<table cellspacing="4">
		<tr>
			<td><g:message code="title.notification.active" />:</td>
			<td><g:checkBox name="useFlag"
				checked="${(dto?.getUseFlag() > 0)}" /></td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.language" />:</td>
			<td><g:select name="language.id"
				from="${com.sapienter.jbilling.server.util.db.LanguageDTO.list()}"
				optionKey="id" optionValue="description" value="${languageId}"
				onchange="javascript: changeLang()" /></td>
		</tr>

		<g:set var="flag" value="${true}" />
		<tr>
			<td><g:message code="prompt.edit.notification.subject" />:</td>
			<td><g:each in="${dto?.getNotificationMessageSections()}"
				var="section">
				<g:if test="${section.section == 1}">
					<g:hiddenField
						name="messageSections[${section.section}].id"
						value="${section.id}" />
					<g:hiddenField
						name="messageSections[${section.section}].section"
						value="${section.section}" />
					<g:set var="tempContent" value="" />
					<g:each in="${section.getNotificationMessageLines().sort{it.id}}"
						var="line">
						<g:set var="tempContent"
							value="${tempContent=tempContent + line?.getContent()}" />
					</g:each>
					<g:textField size="30"
						name="messageSections[${section.section}].notificationMessageLines.content"
						value="${tempContent}" />
					<g:set var="flag" value="${false}" />
				</g:if>
			</g:each> <g:if test="${flag}">
				<g:hiddenField
						name="messageSections[1].id" value="" />
				<g:hiddenField
						name="messageSections[1].section" value="1" />
				<g:textField size="30"
					name="messageSections[1].notificationMessageLines.content"
					value="" />
			</g:if></td>
		</tr>

		<g:set var="flag" value="${true}" />
		<tr>
			<td><g:message code="prompt.edit.notification.bodytext" />:</td>
			<td><g:each in="${dto?.getNotificationMessageSections()}"
				var="section">
				<g:if test="${section.section == 2}">
					<g:hiddenField
						name="messageSections[${section.section}].id"
						value="${section.id}" />
					<g:hiddenField
						name="messageSections[${section.section}].section"
						value="${section.section}" />
					<g:set var="tempContent" value="" />
					<g:each in="${section.getNotificationMessageLines().sort{it.id}}"
						var="line">
						<g:set var="tempContent"
							value="${tempContent=tempContent + line?.getContent()}" />
					</g:each>
					<g:textArea cols="20" rows="10"
						name="messageSections[${section.section}].notificationMessageLines.content"
						value="${tempContent}" />
					<g:set var="flag" value="${false}" />
				</g:if>
			</g:each> <g:if test="${flag}">
				<g:hiddenField
						name="messageSections[2].id" value="" />
				<g:hiddenField
						name="messageSections[2].section" value="2" />
				<g:textArea cols="20" rows="10"
					name="messageSections[2].notificationMessageLines.content"
					value="" />
			</g:if></td>
		</tr>

		<g:set var="flag" value="${true}" />
		<tr>
			<td><g:message code="prompt.edit.notification.bodyhtml" />:</td>
			<td><g:each in="${dto?.getNotificationMessageSections()}"
				var="section">
				<g:if test="${section?.section == 3}">
					<g:hiddenField
						name="messageSections[${section.section}].id"
						value="${section?.id}" />
					<g:hiddenField
						name="messageSections[${section.section}].section"
						value="${section?.section}" />
					<g:set var="tempContent" value="" />
					<g:each in="${section?.getNotificationMessageLines().sort{it.id}}"
						var="line">
						<g:set var="tempContent"
							value="${tempContent=tempContent + line?.getContent()}" />
					</g:each>
					<g:textArea cols="20" rows="10"
						name="messageSections[${section.section}].notificationMessageLines.content"
						value="${tempContent}" />
					<g:set var="flag" value="${false}" />
				</g:if>
			</g:each> <g:if test="${flag}">
				<g:hiddenField
						name="messageSections[3].id" value="" />
				<g:hiddenField
						name="messageSections[3].section" value="3" />
				<g:textArea cols="20" rows="10"
					name="messageSections[3].notificationMessageLines.content"
					value="" />
			</g:if></td>
		</tr>
	</table>
	<table>
		<tr>
			<td><g:actionSubmit value="Save Changes"
				action="saveNotification" class="form_button" /></td>
			<td><input type="button" value="Cancel"
				onClick="javascript: history.back()" /></td>
		</tr>
	</table>
</g:form>
</body>
</html>