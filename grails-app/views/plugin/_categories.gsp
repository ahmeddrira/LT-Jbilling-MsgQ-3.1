<div class="heading table-heading">
    <strong style="width:100%"><g:message code="plugins.category.list.title"/></strong>
</div>

<div class="table-box">
    <ul>
		<g:each in="${categories}" status="idx" var="dto">
		   <li>
		     <g:remoteLink action="plugins" id="${dto.id}" before="register(this);" 
		                   onSuccess="render(data, next);" breadcrumb="id">
                 <strong>
                    ${dto.getDescription(session['language_id'])}
		         </strong>
		         <em>
                    ${dto.getInterfaceName()}
		         </em>
			 </g:remoteLink>
		   </li>
        </g:each>
    </ul>
</div>
