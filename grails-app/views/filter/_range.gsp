<%--
  _range

  @author Brian Cowdery
  @since  11-Apr-2011
--%>

<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.${filter.field}.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" params="[name: filter.name]" update="filters"/>
    
    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="input-bg">
                    <g:textField name="filters.${filter.name}.decimalValue" value="${filter.decimalValue}"/>
                </div>
                <label for="filters.${filter.name}.decimalValue"><g:message code="filters.${filter.field}.low.label"/></label>
            </div>

            <div class="input-row">
                <div class="input-bg">
                    <g:textField name="filters.${filter.name}.decimalHighValue" value="${filter.decimalHighValue}"/>
                </div>
                <label for="filters.${filter.name}.decimalHighValue"><g:message code="filters.${filter.field}.high.label"/></label>
            </div>
        </fieldset>

    </div>
</div>