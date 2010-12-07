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

