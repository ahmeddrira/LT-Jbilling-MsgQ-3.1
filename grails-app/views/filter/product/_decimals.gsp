<%--
  Product "allows decimals" filter

  @author Brian Cowdery
  @since  30-11-2010
--%>

<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.hasDecimals.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" params="[name: filter.name]" update="filters"/>

    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="select-bg">
                    <g:select name="filters.${filter.name}.integerValue"
                              from="${[0, 1]}"
                              valueMessagePrefix='filters.hasDecimals'
                              noSelection="['': message(code: 'filters.hasDecimals.empty')]"/>
                </div>
                <label for="filters.${filter.name}.integerValue"><g:message code="filters.hasDecimals.label"/></label>
            </div>
        </fieldset>
    </div>
</div>