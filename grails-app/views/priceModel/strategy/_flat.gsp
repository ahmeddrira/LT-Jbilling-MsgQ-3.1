<%@ page import="com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Flat pricing form.

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

<g:hiddenField name="model.currencyId" value="${currencies?.asList()?.first()?.id}"/>
<g:hiddenField name="model.rate" value="${BigDecimal.ZERO}"/>
