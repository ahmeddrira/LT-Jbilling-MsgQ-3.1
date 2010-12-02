<%--
  _language

  @author Brian Cowdery
  @since  02-12-2010
--%>

<div id="filter-${filter.id}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.language.title"/></span>
    <a href="#" class="delete"></a>
    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="input-bg">
                    <g:textField name="filters.${filter.id}.integerValue" value="${filter.integerValue}"/>
                </div>
                <label for="filters.${filter.id}.integerValue"><g:message code="filters.language.label"/></label>
                <g:hiddenField name="filters.${filter.id}.id" value="${filter.id}"/>
            </div>
        </fieldset>
    </div>
</div>