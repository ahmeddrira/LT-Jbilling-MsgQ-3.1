<%--
  _status

  @author Brian Cowdery
  @since  30-11-2010
--%>

<a href="#"><g:message code="filters.status.title"/></a>
<div class="slide">
    <fieldset>
        <div class="input-row">
            <div class="select-bg">
                <g:select name="filters.${filter.id}.integerValue"
                        value="${filter.integerValue}"
                        from="${statuses}"
                        optionKey="id" optionValue="description"
                        noSelection="['': message(code: 'filters.status.empty')]" />

            </div>

            <label for="filters.${filter.id}.stringValue"><g:message code="filters.status.label"/></label>
            <g:hiddenField name="filters.${filter.id}.id" value="${filter.id}"/>
        </div>
    </fieldset>
</div>