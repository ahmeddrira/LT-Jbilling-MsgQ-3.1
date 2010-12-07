<%--
  _login

  @author Brian Cowdery
  @since  30-11-2010
--%>

<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.${filter.field}.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" id="${filter.name}" update="filters"/>
    
    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="input-bg">
                    <g:textField name="filters.${filter.name}.stringValue" value="${filter.stringValue}"/>
                </div>
                <label for="filters.${filter.name}.stringValue"><g:message code="filters.userName.label"/></label>               
            </div>
        </fieldset>
    </div>
</div>