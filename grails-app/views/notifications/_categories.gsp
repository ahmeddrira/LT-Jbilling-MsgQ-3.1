<div class="heading table-heading">
	<strong style="width: 100%">
		<g:message code="title.notification.category"/>
	</strong>
</div>

<g:form controller="notifications" action="preferences">
<g:hiddenField name="selectedId" value="0" />

	<div id="catTbl" class="table-box">
		<ul>
		<g:each in="${lst}" status="idx" var="dto">
			<li>
				<g:remoteLink action="lists" id="${dto.id}" 
			     		before="register(this);" onSuccess="render(data, next);">
					<strong><g:hiddenField id="categId" name="categId${idx}"
						value="${dto?.getId()}" /> ${dto.getDescription(languageId)}
					</strong>
				</g:remoteLink>
			</li>
		</g:each>
		</ul>
	</div>
	
	<g:remoteLink action="preferences" class="submit"  
		before="register(this);" onSuccess="render(data, next);">
		<span><g:message code="button.preferences"/></span>
	</g:remoteLink>
	
<%--
	<div class="btn-box">
	    <a href="${createLink(action: 'preferences')}" class="submit save">
	    	<span><g:message code="button.preferences"/></span></a>
	</div>
	<table>
		<tr>
	<td><input type="submit" name="preferences" value="Preferences" class="form_button" />
			</td>			
		</tr>
	</table>
 --%>	
</g:form>