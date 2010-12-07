<%@ page import="com.sapienter.jbilling.server.util.db.LanguageDTO"%>
<div class="column-hold">
	
	<div class="heading">
		<strong style="width: 100%">
			${dto?.notificationMessageType?.getDescription(languageDto.id)}
			<!-- <g:message code="prompt.edit.notification"/> -->
		</strong>
	</div>
	
	<div class="box">
		<dl>
		<dt><g:message code="title.notification.active" />:</dt>
		<dd><g:if test="${(dto?.getUseFlag() > 0)}">
				<g:message code="prompt.yes"/>
			</g:if>
			<g:else>
				<g:message code="prompt.no"/>
			</g:else>
		</dd>

		<dt><g:message code="prompt.product.language" />:</dt>
		<dd>${languageDto.getDescription()}</dd>
		
		<g:set var="flag" value="${true}" />
		<dt><g:message code="prompt.edit.notification.subject" />:</dt>
		<dd>
			<g:each in="${dto?.getNotificationMessageSections()}"
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
					${tempContent}
					<g:set var="flag" value="${false}" />
				</g:if>
			</g:each>
		</dd>
	
		<g:set var="flag" value="${true}" />
		<dt><g:message code="prompt.edit.notification.bodytext" />:</dt>
		<dd><g:each in="${dto?.getNotificationMessageSections()}"
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
					${tempContent}
					<g:set var="flag" value="${false}" />
				</g:if>
			</g:each>
		</dd>
	
		<g:set var="flag" value="${true}" />
		<dt><g:message code="prompt.edit.notification.bodyhtml" />:</dt>
		<dd><g:each in="${dto?.getNotificationMessageSections()}"
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
					${tempContent}
					<g:set var="flag" value="${false}" />
				</g:if>
			</g:each>
		</dd>
		</dl>
	</div>
	
	<div class="btn-box">
	    <a href="${createLink(action: 'edit', params: [id:messageTypeId])}" class="submit edit">
	    	<span><g:message code="button.edit"/></span></a>
	</div>
</div>