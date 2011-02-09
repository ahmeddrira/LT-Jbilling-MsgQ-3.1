<%@ page import="com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Percentage pricing form.

  @author Brian Cowdery
  @since  08-Feb-2011
--%>

<g:hiddenField name="model.${modelIndex}.id" value="${model?.id}"/>

<g:applyLayout name="form/select">
    <content tag="label"><g:message code="plan.model.type"/></content>
    <content tag="label.for">model.${modelIndex}.type</content>
    <g:select name="model.${modelIndex}.type" class="model-type"
              from="${PriceModelStrategy.values()}"
              valueMessagePrefix="price.strategy"
              value="${model?.type ?: type.name()}"/>
</g:applyLayout>

<g:applyLayout name="form/input">
    <content tag="label"><g:message code="plan.model.percentage"/></content>
    <content tag="label.for">model.${modelIndex}.rate</content>
    <g:textField class="field" name="model.${modelIndex}.rate" value="${formatNumber(number: model?.rate ?: BigDecimal.ZERO, formatName: 'money.format')}"/>
</g:applyLayout>

<g:applyLayout name="form/select">
    <content tag="label"><g:message code="prompt.user.currency"/></content>
    <content tag="label.for">model.${modelIndex}.currencyId</content>
    <g:select name="model.${modelIndex}.currencyId"
              from="${currencies}"
              optionKey="id" optionValue="${{it.getDescription(session['language_id'])}}"
              value="${model?.currencyId}" />
</g:applyLayout>
