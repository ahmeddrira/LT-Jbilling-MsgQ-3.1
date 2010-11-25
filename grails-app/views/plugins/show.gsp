<html>
<body>
<em>${dto.type.getDescription(languageId, "title")}</em>
<p><g:message code="plugins.plugin.description" /></p>
<p>
		${dto.type.getDescription(languageId)}
</p>
<p>
        <g:message code="plugins.plugin.id" /> ${dto.getId()}
</p>
<p>
        <g:message code="plugins.plugin.notes" />
</p>
<p>
${dto.getNotes()}
</p>
<p><g:message code="plugins.plugin.order" /> ${dto.getProcessingOrder()}</p>
        
<table>
<thead><tr>
         <th><g:message code="plugins.plugin.parameter"/></th>
         <th><g:message code="plugins.plugin.value"/></th>
         <th></th>
</tr></thead>
<tbody>
            <g:each in="${dto.parameters}">
                <tr>
                    <td>
                       ${it.name}
                    </td>
                    <td> 
                       ${it.value}
                    </td>
                </tr>
            </g:each>
</table>
</body>
</html>