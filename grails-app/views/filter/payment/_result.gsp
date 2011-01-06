<%@ page import="com.sapienter.jbilling.server.payment.db.PaymentResultDAS" %>

<%--
  Payment result filter.

  @author Brian Cowdery
  @since  06-Jan-2011
--%>

<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.${filter.field}.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" params="[name: filter.name]" update="filters"/>

    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="select-bg">
                    <g:select name="filters.${filter.name}.integerValue"
                            value="${filter.integerValue}"
                            from="${new PaymentResultDAS().findAll()}"
                            optionKey="id" optionValue="description"
                            noSelection="['': message(code: 'filters.payment.result.empty')]" />

                </div>
                <label for="filters.${filter.name}.stringValue"><g:message code="filters.payment.result.label"/></label>
            </div>
        </fieldset>
    </div>
</div>

