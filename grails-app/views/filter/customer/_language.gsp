<%@ page import="com.sapienter.jbilling.server.util.db.LanguageDTO" %>
<%--
  _language

  @author Brian Cowdery
  @since  02-12-2010
--%>

<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.${filter.field}.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" params="[name: filter.name]" update="filters"/>

    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="select-bg">
                    <g:select name="filters.${filter.name}.integerValue"
                              from="${LanguageDTO.list()}"
                              optionValue="${{ it.code }}"
                              optionKey="id"
                              noSelection="['': message(code: 'filters.' + filter.field + '.empty')]"/>
                </div>
                <label for="filters.${filter.name}.stringValue"><g:message code="filters.${filter.field}.label"/></label>
            </div>
        </fieldset>
    </div>
</div>