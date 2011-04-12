
<%--
  Billing process review filter

  @author Brian Cowdery
  @since 12-Apr-2011
--%>
<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.billing.review.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" params="[name: filter.name]" update="filters"/>

    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="select-bg">
                    <g:select name="filters.${filter.name}.integerValue"
                              value="${filter.integerValue}"
                              from="${[0, 1]}"
                              valueMessagePrefix='filters.billing.review'
                              noSelection="['': message(code: 'filters.billing.review.empty')]"/>
                </div>
                <label for="filters.${filter.name}.integerValue"><g:message code="filters.billing.review.label"/></label>
            </div>
        </fieldset>
    </div>
</div>