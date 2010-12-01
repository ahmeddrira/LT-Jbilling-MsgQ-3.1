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
        <strong><g:message code="plugins.plugin.description"/></strong>
        <p>${plugin.type.getDescription(languageId)}</p>
        <br/>
        <dl class="other">
            <dt><g:message code="plugins.plugin.id-long"/></dt>
            <dd>${plugin.getId()}</dd>
            <dt><g:message code="plugins.plugin.notes"/></dt>
            <dd>
                <g:if test="${plugin.getNotes() != null}">
                    ${plugin.getNotes()}
                </g:if>
                <g:else>
                    <g:message code="plugins.plugin.noNotes"/>
                </g:else>
            </dd>
            <dt><g:message code="plugins.plugin.order"/></dt>
            <dd>${plugin.getProcessingOrder()}</dd>
        </dl>
        
        <div class="box-cards box-cards-open">
             <div class="box-cards-title">
                   <span><g:message code="plugins.plugin.value"/></span>
                   <span style="width:50%"><g:message code="plugins.plugin.parameter"/></span>
             </div>
             <div class="box-card-hold">
                       <g:each in="${plugin.parameters}">
                           <div class="form-columns">
                              <label>${it.name}</label>
                              <label>${it.value}</label>
                           </div>
                       </g:each>
             </div>
        </div>
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
