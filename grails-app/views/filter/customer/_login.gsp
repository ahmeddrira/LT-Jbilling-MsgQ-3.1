<%--
  _login

  @author Brian Cowdery
  @since  30-11-2010
--%>

<a href="#"><g:message code="filters.login.title"/></a>
<div class="slide">
    <fieldset>
        <div class="input-row">
            <div class="input-bg">
                <g:textField name="filters.${filter.id}.stringValue" value="${filter.stringValue}"/>
            </div>
            <label for="filters.${filter.id}.stringValue"><g:message code="filters.login.label"/></label>
            <g:hiddenField name="filters.${filter.id}.id" value="${filter.id}"/>
        </div>
    </fieldset>
</div>