<div class="heading table-heading">
    <strong class="first"><g:message code="plugins.plugin.id"/></strong>
    <strong style="width:25%"><g:message code="plugins.plugin.order"/></strong>
    <strong style="width:50%"><g:message code="plugins.plugin.type"/></strong>
</div>

<div class="table-box">
    <ul>
		<g:each in="${plugins}" status="idx" var="dto">
		   <li>
		     <g:remoteLink action="show" id="${dto.id}" before="register(this);" onSuccess="render(data, next);">
                 <span class="block">
                    <span>${dto.getProcessingOrder()}</span>
		         </span>
                 <strong>
                    ${dto.getId()}
		         </strong>
                 <strong>
		            ${dto.type.getDescription(session['language_id'], "title")}
		         </strong>
		         <em>
		            ${dto.type.getClassName()}
		         </em>
			 </g:remoteLink>
		   </li>
        </g:each>
    </ul>
</div>

<div class="btn-box">
    <a href="${createLink(action: 'create')}" class="submit add"><span><g:message code="button.create"/></span></a>
    <a href="${createLink(action: 'delete')}" class="submit delete"><span><g:message code="button.delete"/></span></a>
</div>