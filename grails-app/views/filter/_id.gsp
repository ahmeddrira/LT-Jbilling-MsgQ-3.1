
<%--
  _id

  @author Brian Cowdery
  @since  30-11-2010
--%>

<a href="#"><g:message code="filters.${controllerName}.id.title"/></a>
<div class="slide">
    <fieldset>
        <div class="input-row">
            <div class="input-bg">
                <g:textField name="filters.${filter.id}.integerValue" value="${filter.integerValue}"/>
            </div>
            <label for="filters.${filter.id}.integerValue"><g:message code="filters.id.label"/></label>
            <g:hiddenField name="filters.${filter.id}.id" value="${filter.id}"/>
        </div>
    </fieldset>
</div>