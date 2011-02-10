<%@ page import="com.sapienter.jbilling.server.pricing.db.ChainPosition; org.apache.commons.lang.WordUtils; com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Plan-builder editor form for price models.

  Necessary javascript functions are present in the main plan builder "_review.gsp" template.

  @author Brian Cowdery
  @since  09-Feb-2011
--%>

<!-- root price model -->
<g:set var="types" value="${PriceModelStrategy.getStrategyByChainPosition(ChainPosition.START)}"/>
<g:set var="type" value="${(model ? PriceModelStrategy.valueOf(model?.type) : PriceModelStrategy.METERED)}"/>
<g:set var="templateName" value="${WordUtils.uncapitalize(WordUtils.capitalizeFully(type.name(), ['_'] as char[]).replaceAll('_',''))}"/>
<g:set var="modelIndex" value="${0}"/>

<g:render template="/priceModel/strategy/${templateName}" model="[model: model, type: type, modelIndex: modelIndex, types: types, currencies: currencies]"/>
<g:render template="/priceModel/attributes" model="[model: model, type: type, modelIndex: modelIndex]"/>

<!-- price models in chain -->
<g:set var="types" value="${PriceModelStrategy.getStrategyByChainPosition(ChainPosition.END, ChainPosition.MIDDLE)}"/>
<g:set var="next" value="${model.next}"/>
<g:while test="${next}">
    <g:set var="type" value="${PriceModelStrategy.valueOf(next?.type)}"/>
    <g:set var="templateName" value="${WordUtils.uncapitalize(WordUtils.capitalizeFully(type.name(), ['_'] as char[]).replaceAll('_',''))}"/>
    <g:set var="modelIndex" value="${modelIndex + 1}"/>

    <hr/>
    <g:render template="/priceModel/strategy/${templateName}" model="[model: next, type: type, modelIndex: modelIndex, types: types, currencies: currencies]"/>
    <g:render template="/priceModel/attributes" model="[model: next, type: type, modelIndex: modelIndex]"/>

    <g:set var="next" value="${next.next}"/>
</g:while>

<!-- controls -->
<br/>
<g:applyLayout name="form/text">
    <content tag="label">&nbsp;</content>
    <a class="submit add" onclick="addChainModel(this)"><span><g:message code="button.add.chain"/></span></a>
</g:applyLayout>

<g:hiddenField name="attributeIndex"/>
<g:hiddenField name="modelIndex"/>

