%{--
  JBILLING CONFIDENTIAL
  _____________________

  [2003] - [2012] Enterprise jBilling Software Ltd.
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Enterprise jBilling Software.
  The intellectual and technical concepts contained
  herein are proprietary to Enterprise jBilling Software
  and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden.
  --}%

<%@ page import="com.sapienter.jbilling.server.pricing.PriceModelBL; com.sapienter.jbilling.server.item.db.ItemDTO; com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>
<%@ page import="com.sapienter.jbilling.server.item.db.PlanItemBundleDTO.Customer" %>


<%--
  Renders an PlanItemWS as an editable row for the plan builder preview pane.

  @author Brian Cowdery
  @since 24-Jan-2011
--%>
<g:set var="product" value="${ItemDTO.get(planItem.itemId)}"/>
<g:set var="model" value="${PriceModelBL.getWsPriceForDate(planItem.models, startDate)}"/>

<g:set var="editable" value="${index == params.int('newLineIndex')}"/>

<g:formRemote name="price-${index}-update-form" url="[action: 'edit']" update="column2" method="GET">
    <g:hiddenField name="_eventId" value="updatePrice"/>
    <g:hiddenField name="execution" value="${flowExecutionKey}"/>
    <g:hiddenField name="index" value="${index}"/>

    <!-- review line ${index} -->
    <li id="line-${index}" class="line ${editable ? 'active' : ''}">
        <span class="description">
            ${planItem.precedence} &nbsp; ${product.description}
        </span>
        <g:if test="${planItem?.model?.type}">
            <g:set var="currency" value="${currencies.find{ it.id == model.currencyId}}"/>
            <span class="rate">
                <g:formatNumber number="${model.getRateAsDecimal()}" type="currency" currencySymbol="${currency?.symbol}"/>
            </span>
            <span class="strategy">
                <g:message code="price.strategy.${model.type}"/>
            </span>
        </g:if>
        <g:else>
            <span class="rate">
                <g:formatNumber number="${product.percentage}" formatName="percentage.format"/>
            </span>
            <span class="strategy">
                <g:message code="product.percentage"/>
            </span>
        </g:else>
        <div style="clear: both;"></div>
    </li>

    <!-- line ${index} editor -->
    <li id="line-${index}-editor" class="editor ${editable ? 'open' : ''}">
        <div class="box">
            <div class="form-columns">
                <g:applyLayout name="form/input">
                    <content tag="label"><g:message code="plan.item.precedence"/></content>
                    <content tag="label.for">price.precedence</content>
                    <g:textField class="field" name="price.precedence" value="${planItem.precedence}"/>
                </g:applyLayout>

                <br/>

                <g:applyLayout name="form/input">
                    <content tag="label"><g:message code="plan.item.bundled.quantity"/></content>
                    <content tag="label.for">bundle.quantityAsDecimal</content>
                    <g:textField class="field" name="bundle.quantityAsDecimal" value="${formatNumber(number: planItem.bundle?.quantityAsDecimal ?: BigDecimal.ZERO)}"/>
                </g:applyLayout>

                <g:applyLayout name="form/select">
                    <content tag="label"><g:message code="plan.bundle.period"/></content>
                    <content tag="label.for">bundle.periodId</content>
                    <g:select from="${itemOrderPeriods}"
                              optionKey="id" optionValue="${{it.getDescription(session['language_id'])}}"
                              name="bundle.periodId"
                              value="${planItem?.bundle?.periodId}"/>
                </g:applyLayout>

                <g:applyLayout name="form/select">
                    <content tag="label"><g:message code="plan.bundle.target.customer"/></content>
                    <content tag="label.for">bundle.targetCustomer</content>
                    <g:select from="${Customer.values()}"
                              name="bundle.targetCustomer"
                                valueMessagePrefix="bundle.target.customer"
                              value="${planItem.bundle?.targetCustomer}"/>
                </g:applyLayout>

                <g:applyLayout name="form/checkbox">
                    <content tag="label"><g:message code="plan.bundle.add.if.exists"/></content>
                    <content tag="label.for">bundle.addIfExists</content>
                    <g:checkBox name="bundle.addIfExists" class="cb check" checked="${planItem.bundle?.addIfExists}"/>
                </g:applyLayout>

                <br/>

                <g:if test="${model?.type}">
                    <g:render template="/priceModel/builderModel" model="[model: model]"/>
                </g:if>
            </div>
        </div>

        <div class="btn-box">
            <a class="submit save" onclick="$('#price-${index}-update-form').submit();"><span><g:message code="button.update"/></span></a>
            <g:remoteLink class="submit cancel" action="edit" params="[_eventId: 'removePrice', index: index]" update="column2" method="GET">
                <span><g:message code="button.remove"/></span>
            </g:remoteLink>
        </div>
    </li>
</g:formRemote>
