<div class="table-box">
<table cellpadding="0" cellspacing="0">
    <thead>
        <tr>
            <th class="small"><g:message code="plugins.plugin.id"/></th>
    	    <th class="medium"><g:message code="plugins.plugin.type"/></th>
    	    <th class="tiny"><g:message code="plugins.plugin.order"/></th>
        </tr>
    </thead>

    <tbody>
		<g:each in="${plugins}" status="idx" var="dto">
		   <tr>
		     <td>
		     <g:remoteLink action="show" id="${dto.id}" before="register(this);" 
                           onSuccess="render(data, next);" params="[template:'show']">
                    ${dto.getId()}
			 </g:remoteLink>
             </td>
		     <td>
		     <g:remoteLink action="show" id="${dto.id}" before="register(this);" 
                           onSuccess="render(data, next);" params="[template:'show']">
                 <strong>
		            ${dto.type.getDescription(session['language_id'], "title")}
		         </strong>
		         <em>
		            ${dto.type.getClassName()}
		         </em>
			 </g:remoteLink>
             </td>
		     <td>
		     <g:remoteLink action="show" id="${dto.id}" before="register(this);" 
                           onSuccess="render(data, next);" params="[template:'show']">
                  ${dto.getProcessingOrder()}
			 </g:remoteLink>
             </td>
		   </tr>
        </g:each>
    </tbody>
</table>
</div>

<div class="btn-box">
    <a href="${createLink(action: 'showForm')}" class="submit add"><span><g:message code="button.create"/></span></a>
</div>