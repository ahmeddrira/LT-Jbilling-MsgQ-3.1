<%@ page import="com.sapienter.jbilling.server.user.db.UserStatusDAS" %>

<%--
  _status

  @author Brian Cowdery
  @since  30-11-2010
--%>

<div id="filter-${filter.id}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.status.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" id="${filter.id}" update="filters"/>
    
    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="select-bg">
                    <g:select name="filters.${filter.id}.integerValue"
                            value="${filter.integerValue}"
                            from="${new UserStatusDAS().findAll()}"
                            optionKey="id" optionValue="description"
                            noSelection="['': message(code: 'filters.status.empty')]" />

                </div>

                <label for="filters.${filter.id}.stringValue"><g:message code="filters.status.label"/></label>
                <g:hiddenField name="filters.${filter.id}.id" value="${filter.id}"/>
            </div>
        </fieldset>
    </div>
</div>

