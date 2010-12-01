<%--
  _created

  @author Brian Cowdery
  @since  30-11-2010
--%>

<a href="#"><g:message code="filters.created.title"/></a>
<div class="slide">
    <fieldset>
        <div class="input-row">
            <div class="input-bg">
                <a href="#"></a>
                <g:textField name="filters.${filter.id}.startDateValue" value="${filter.startDateValue}"/>
            </div>
            <label for="filters.${filter.id}.startDateValue"><g:message code="filters.date.from.label"/></label>
        </div>
        
        <div class="input-row">
            <div class="input-bg">
                <a href="#"></a>
                <g:textField name="filters.${filter.id}.endDateValue" value="${filter.endDateValue}"/>
            </div>
            <label for="filters.${filter.id}.endDateValue"><g:message code="filters.date.to.label"/></label>
        </div>

        <g:hiddenField name="filters.${filter.id}.id" value="${filter.id}"/>
    </fieldset>
</div>