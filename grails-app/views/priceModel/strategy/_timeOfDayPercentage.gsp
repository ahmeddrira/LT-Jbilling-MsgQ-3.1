<%@ page import="com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Time-of-day percentage pricing form.

  @author Brian Cowdery
  @since  08-Feb-2011
--%>

<g:hiddenField name="model.id" value="${model?.id}"/>

<g:applyLayout name="form/select">
    <content tag="label"><g:message code="plan.model.type"/></content>
    <content tag="label.for">model.type</content>
    <g:select from="${PriceModelStrategy.values()}"
            name="model.type"
            valueMessagePrefix="price.strategy"
            value="${model?.type ?: type.name()}"/>
</g:applyLayout>

<g:applyLayout name="form/select">
    <content tag="label"><g:message code="prompt.user.currency"/></content>
    <content tag="label.for">model.currencyId</content>
    <g:select from="${currencies}"
            name="model.currencyId"
            optionKey="id" optionValue="${{it.getDescription(session['language_id'])}}"
            value="${model?.currencyId}" />
</g:applyLayout>

<g:hiddenField name="model.rate" value="${BigDecimal.ZERO}"/>
