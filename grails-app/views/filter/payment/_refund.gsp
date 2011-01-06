
<%--
  Payment "is refund" filter.

  @author Brian Cowdery
  @since  05-Jan-2011
--%>

<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.isRefund.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" params="[name: filter.name]" update="filters"/>

    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="select-bg">
                    <g:select name="filters.${filter.name}.integerValue"
                              from="${[0, 1]}"
                              valueMessagePrefix='filters.isRefund'
                              noSelection="['': message(code: 'filters.isRefund.empty')]"/>
                </div>
                <label for="filters.${filter.name}.stringValue"><g:message code="filters.isRefund.label"/></label>
            </div>
        </fieldset>
    </div>
</div>