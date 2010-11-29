<%--
  details

  @author E. Conde
  @since  26-11-2010
--%>

<div class="column-hold">

    <!-- the plug-in details -->
    <div class="heading">
        <strong>${plugin.type.getDescription(languageId, "title")}</strong>
    </div>
    <div class="box">
        <dl>
            <dt><g:message code="plugins.plugin.description"/></dt>
            <dd>${plugin.type.getDescription(languageId)}</dd>
            <dt><g:message code="plugins.plugin.id-long"/></dt>
            <dd>${plugin.getId()}</dd>
            <dt><g:message code="plugins.plugin.notes"/></dt>
            <dd>${plugin.getNotes()}</dd>
            <dt><g:message code="plugins.plugin.order"/></dt>
            <dd>${plugin.getProcessingOrder()}</dd>
        </dl>
    </div>

    <div class="btn-box">
        <a href="#" class="submit"><span><g:message code="plugins.plugin.edit"/></span></a>
    </div>
</div>



<%--
<p><g:message code="plugins.plugin.description" /></p>
<p>
		${plugin.type.getDescription(languageId)}
</p>
<p>
        <g:message code="plugins.plugin.id" /> ${plugin.getId()}
</p>
<p>
        <g:message code="plugins.plugin.notes" />
</p>
<p>
${plugin.getNotes()}
</p>
<p><g:message code="plugins.plugin.order" /> ${plugin.getProcessingOrder()}</p>
        
<table>
<thead><tr>
         <th><g:message code="plugins.plugin.parameter"/></th>
         <th><g:message code="plugins.plugin.value"/></th>
         <th></th>
</tr></thead>
<tbody>
            <g:each in="${plugin.parameters}">
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
--%>
