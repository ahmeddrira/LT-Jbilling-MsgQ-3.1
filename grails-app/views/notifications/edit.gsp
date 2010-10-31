<html>
<head>
<script language="javascript">
</script>
<title>
${title}
</title>
</head>
<script language="javascript">
</script>
<body>
<h2><g:message code="prompt.edit.notification" /></h2>
<g:form>
	<g:hiddenField name="id" value="${dto?.getId()}" />
	<table cellspacing="4">
		<tr>
			<td><g:message code="title.notification.active" />:</td>
			<td><g:checkBox name="useFlag"
				checked="${(dto?.getUseFlag() > 0)}" /></td>
		</tr>
		<tr>
			<td><g:message code="prompt.product.language" />:</td>
			<td><g:select name="languageId"
				from="${com.sapienter.jbilling.server.util.db.LanguageDTO.list()}"
				optionKey="id" optionValue="description" value="${languageId}"
				onchange="javascript: changeLang()" /></td>
		</tr>
		
		<g:set var="flag" value="${true}" />
		<tr>
			<td><g:message code="prompt.edit.notification.subject" />:</td>
			<td><g:each in="${dto?.getNotificationMessageSections()}" var="section">
					<g:if test="${section.section == 1}">
							<g:hiddenField name="notificationMessageSections.id"
								value="${section.id}" /> <g:hiddenField
								name="notificationMessageSections.section"
								value="${section.section}" /> <g:set var="tempContent" value="" />
							<g:each in="${section.getNotificationMessageLines().sort{it.id}}" var="line">
								<g:set var="tempContent"
									value="${tempContent=tempContent + line?.getContent()}" />
							</g:each> <g:textField size="30" name="content${section.id}"
								value="${tempContent}" />
						<g:set var="flag" value="${false}"/>
					</g:if>
				</g:each>
				<g:if test="${flag}">
					<g:textField size="30" name="content1" value="" />
				</g:if>
			</td>
		</tr>
		
		<g:set var="flag" value="${true}" />
		<tr>
			<td><g:message code="prompt.edit.notification.bodytext" />:</td>
			<td><g:each in="${dto?.getNotificationMessageSections()}" var="section">
					<g:if test="${section.section == 2}">
						<g:hiddenField name="notificationMessageSections.id"
							value="${section.id}" /> <g:hiddenField
							name="notificationMessageSections.section"
							value="${section.section}" /> <g:set var="tempContent" value="" />
						<g:each in="${section.getNotificationMessageLines().sort{it.id}}" var="line">
							<g:set var="tempContent"
								value="${tempContent=tempContent + line?.getContent()}" />
						</g:each> <g:textArea cols="20" rows="10" name="content${section.id}"
							value="${tempContent}" />								
						<g:set var="flag" value="${false}"/>
					</g:if>
				</g:each>
				<g:if test="${flag}">
					<g:textArea cols="20" rows="10" name="content2" value="" />
				</g:if>
			</td>
		</tr>
		
		<g:set var="flag" value="${true}" />
		<tr>
			<td><g:message code="prompt.edit.notification.bodyhtml" />:</td>
			<td><g:each in="${dto?.getNotificationMessageSections()}" var="section">
					<g:if test="${section?.section == 3}">
						<g:hiddenField name="notificationMessageSections.id"
								value="${section?.id}" /> <g:hiddenField
								name="notificationMessageSections.section"
								value="${section?.section}" /> <g:set var="tempContent" value="" />
							<g:each in="${section?.getNotificationMessageLines().sort{it.id}}" var="line">
								<g:set var="tempContent"
									value="${tempContent=tempContent + line?.getContent()}" />
							</g:each> <g:textArea cols="20" rows="10" name="content${section?.id}"
								value="${tempContent}" />
						<g:set var="flag" value="${false}"/>
					</g:if>
				</g:each>
				<g:if test="${flag}">
					<g:textArea cols="20" rows="10" name="content3" value="" />
				</g:if>
			</td>
		</tr>
	</table>
</g:form>
</body>
</html>