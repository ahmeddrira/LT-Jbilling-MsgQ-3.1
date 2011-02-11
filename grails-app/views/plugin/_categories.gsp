<div class="table-box">
<table cellpadding="0" cellspacing="0">
    <thead>
       <tr class="active">
        <th><g:message code="plugins.category.list.title"/></th>
       </tr>
    </thead>
    <tbody>
    <g:each in="${categories}" status="idx" var="dto">
    <tr>
        <td>
        <g:remoteLink action="plugins" id="${dto.id}" before="register(this);" 
                                   onSuccess="render(data, next);"
                                   params="[template:'show']">
             <strong>
                ${dto.getDescription(session['language_id'])}
		     </strong>
		     <em>
                ${dto.getInterfaceName()}
		     </em>
        </g:remoteLink>
        </td>
    </tr>
    </g:each>
    </tbody>
</table>
</div>