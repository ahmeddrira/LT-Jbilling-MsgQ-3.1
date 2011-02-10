<%@ page import="com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Flat pricing form.

  @author Brian Cowdery
  @since  08-Feb-2011
--%>

<g:hiddenField name="model.${modelIndex}.id" value="${model?.id}"/>

<g:applyLayout name="form/select">
    <content tag="label"><g:message code="plan.model.type"/></content>
    <content tag="label.for">model.${modelIndex}.type</content>
    <g:select name="model.${modelIndex}.type" class="model-type"
              from="${types}"
              valueMessagePrefix="price.strategy"
              value="${model?.type ?: type.name()}"/>

    <g:if test="${modelIndex > 0}">
        <a onclick="removeChainModel(this, ${modelIndex});">
            <img src="${resource(dir:'images', file:'cross.png')}" alt="remove"/>
        </a>
    </g:if>
</g:applyLayout>

<g:hiddenField name="model.${modelIndex}.currencyId" value="${currencies?.asList()?.first()?.id}"/>
<g:hiddenField name="model.${modelIndex}.rate" value="${BigDecimal.ZERO}"/>
