%{--
  jBilling - The Enterprise Open Source Billing System
  Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

  This file is part of jbilling.

  jbilling is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  jbilling is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
  --}%

<%@ page import="com.sapienter.jbilling.server.item.db.PlanItemBundleDTO; com.sapienter.jbilling.server.item.db.ItemDTO; com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>


<%--
  Renders an PlanItemWS as an editable row for the plan builder preview pane.

  @author Brian Cowdery
  @since 24-Jan-2011
--%>
<g:set var="product" value="${ItemDTO.get(planItem.itemId)}"/>
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
            <g:set var="currency" value="${currencies.find{ it.id == planItem.model.currencyId}}"/>
            <span class="rate">
                <g:formatNumber number="${planItem.model.getRateAsDecimal()}" type="currency"
                                currencySymbol="${currency?.symbol}"/>
            </span>
            <span class="strategy">
                <g:message code="price.strategy.${planItem.model?.type}"/>
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
                    <g:select from="${orderPeriods}"
                              optionKey="id" optionValue="${{it.getDescription(session['language_id'])}}"
                              name="bundle.periodId"
                              value="${planItem?.bundle?.periodId}"/>
                </g:applyLayout>

                <g:applyLayout name="form/select">
                    <content tag="label"><g:message code="plan.bundle.target.customer"/></content>
                    <content tag="label.for">bundle.targetCustomer</content>
                    <g:select from="${PlanItemBundleDTO.Customer.values()}"
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

                <g:if test="${planItem.model?.type}">
                    <g:render template="/priceModel/builderModel" model="[model: planItem.model]"/>
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
