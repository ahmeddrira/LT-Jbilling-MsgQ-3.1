<%--
  _invoices - has invoices

  @author Brian Cowdery
  @since  11-Apr-2011
--%>

<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.${filter.field}.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" params="[name: filter.name]" update="filters"/>
    
    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="checkbox-row">
                    <label for="filters.${filter.name}.booleanValue"><g:message code="filters.${filter.field}.title"/></label>
                    <g:checkBox name="filters.${filter.name}.booleanValue" class="cb check" checked="${filter.booleanValue}"/>
                </div>
            </div>
        </fieldset>
    </div>
</div>

