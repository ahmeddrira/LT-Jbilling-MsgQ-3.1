
<%--
  Payment "isReview" filter.

  @author Vikas Bodani
  @since  09-Feb-2011
--%>
<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.isReview.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" params="[name: filter.name]" update="filters"/>

    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="select-bg">
                    <g:select name="filters.${filter.name}.stringValue" 
                              value="${filter.stringValue}"
                              from="${['0']}"
                              valueMessagePrefix='filters.isReview'
                              noSelection="['': message(code: 'filters.isReview.empty')]"/>
                </div>
                <label for="filters.${filter.name}.stringValue"><g:message code="filters.isReview.label"/></label>
            </div>
        </fieldset>
    </div>
</div>