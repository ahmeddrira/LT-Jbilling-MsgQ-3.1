
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
                    <g:select name="filters.${filter.name}.integerValue"
                              value="${filter.integerValue}"
                              from="${[0, 1]}"
                              valueMessagePrefix='filters.isReview'/>
                </div>
                <label for="filters.${filter.name}.integerValue"><g:message code="filters.isReview.label"/></label>
            </div>
        </fieldset>
    </div>
</div>