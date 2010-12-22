<%--
  details

  @author E. Conde
  @since  26-11-2010
--%>


<div class="column-hold">

    <!-- the plug-in details -->
    <div class="heading">
        <strong>${plugin.type.getDescription(session['language_id'], "title")}</strong>
    </div>
    <div class="box">
        <strong><g:message code="plugins.plugin.description"/></strong>
        <p>${plugin.type.getDescription(session['language_id'])}</p>
        <br/>
        
        <table class="dataTable">
           <tr>
            <td><g:message code="plugins.plugin.id-long"/></td>
            <td class="value">${plugin.getId()}</td>
           </tr>
           <tr>
            <td><g:message code="plugins.plugin.notes"/></td>
            <td class="value">
                <g:if test="${plugin.getNotes() != null}">
                    ${plugin.getNotes()}
                </g:if>
                <g:else>
                    <g:message code="plugins.plugin.noNotes"/>
                </g:else>
            </td>
           </tr>
           <tr>
            <td><g:message code="plugins.plugin.order"/></td>
            <td class="value">${plugin.getProcessingOrder()}</td>
           </tr>
        </table>
        
        <table class="innerTable">
             <thead class="innerHeader">
             <tr>
                <th><g:message code="plugins.plugin.parameter"/></th>
                <th><g:message code="plugins.plugin.value"/></th>
             </tr>
             </thead>
             <tbody>
             <g:each in="${plugin.parameters}">
             <tr>
                <td class="innerContent">${it.name}</td>
                <td class="innerContent">${it.value}</td>
             </tr>         
             </g:each>
             </tbody>
        </table>
    </div>

    <g:render template="/confirm" 
              model="['message':'plugins.delete.confirm','controller':'plugin','action':'delete','id':plugin.getId()]"/>

    <div class="btn-box">
        <a href="${createLink(controller:'plugin', action:'edit', id:plugin.getId()) }" class="submit">
            <span><g:message code="plugins.plugin.edit"/></span>
        </a>
        <a onclick="$('#confirm-dialog-${plugin.id}').dialog('open');" class="submit delete">
            <span><g:message code="plugins.plugin.delete"/></span>
        </a>
    </div>
</div>


