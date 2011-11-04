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

<%@ page import="com.sapienter.jbilling.server.pricing.PriceModelBL; com.sapienter.jbilling.server.user.db.CompanyDTO; com.sapienter.jbilling.server.util.Constants" %>
<%--
  Renders a PlanWS as a quick preview of the plan being built. This view also allows
  individual plan prices to be edited and removed from the order.

  @author Brian Cowdery
  @since 01-Feb-2011
--%>

<div id="review-box">

    <!-- error messages -->
    <div id="messages">
        <g:if test="${errorMessages}">
            <div class="msg-box error">
                <ul>
                    <g:each var="message" in="${errorMessages}">
                        <li>${message}</li>
                    </g:each>
                </ul>
            </div>

            <g:set var="errorMessages" value=""/>
        </g:if>
    </div>

    <!-- review -->
    <div class="box no-heading">
        <!-- plan review header -->
        <div class="header">
            <div class="column">
                <h2>${product.description} &nbsp;</h2>
            </div>
            <div class="column">
                <h2 class="right">
                    <g:set var="defaultProductPrice" value="${PriceModelBL.getWsPriceForDate(product.defaultPrices, startDate)}"/>

                    <g:if test="${defaultProductPrice}">
                        <g:set var="currency" value="${currencies.find{ it.id == defaultProductPrice.currencyId }}"/>
                        <g:set var="price" value="${formatNumber(number: defaultProductPrice.getRateAsDecimal(), type: 'currency', currencySymbol: currency.symbol)}"/>
                    </g:if>
                    <g:else>
                        <g:set var="currency" value="${CompanyDTO.get(session['company_id']).currency}"/>
                        <g:set var="price" value="${formatNumber(number: BigDecimal.ZERO, type: 'currency', currencySymbol: currency.symbol)}"/>
                    </g:else>

                    <g:if test="${plan.periodId == Constants.ORDER_PERIOD_ONCE}">
                        <g:message code="plan.review.onetime.price" args="[price]"/>
                    </g:if>
                    <g:else>
                        <g:set var="orderPeriod" value="${orderPeriods.find{ it.id == plan.periodId }}"/>
                        <g:message code="plan.review.period.price" args="[price, orderPeriod.getDescription(session['language_id'])]"/>
                    </g:else>
                </h2>
                <h3 class="right"><g:message code="plan.review.view.on.date" args="[formatDate(date: startDate)]"/></h3>
            </div>

            <div style="clear: both;"></div>
        </div>

        <hr/>

        <!-- list of item prices (ordered by precedence) -->
        <ul id="review-lines">
            <g:each var="planItem" status="index" in="${plan.planItems}">
                <g:render template="priceLine" model="[ planItem: planItem, index: index, startDate: startDate ]"/>
            </g:each>

            <g:if test="${!plan.planItems}">
                <li><em><g:message code="plan.review.no.prices"/></em></li>
            </g:if>
        </ul>

        <!-- plan notes -->
        <g:if test="${plan.description}">
            <hr/>
            <div class="box-text">
                <ul>
                    <li><p>${plan.description}</p></li>
                </ul>
            </div>
        </g:if>
    </div>

    <!-- buttons -->
    <div class="btn-box">
        <g:link class="submit save" action="edit" params="[_eventId: 'save']">
            <span><g:message code="button.save"/></span>
        </g:link>

        <g:link class="submit cancel" action="edit" params="[_eventId: 'cancel']">
            <span><g:message code="button.cancel"/></span>
        </g:link>
    </div>

    <script type="text/javascript">
        $(function() {
            $('#review-lines li.line').click(function() {
                var id = $(this).attr('id');
                $('#' + id).toggleClass('active');
                $('#' + id + '-editor').toggle('blind');
            });
        });

        $(function() {
            $('.model-type').change(function() {
                var form = $(this).parents('form');
                form.find('[name=_eventId]').val('updateStrategy');
                form.submit();
            });
        });

        function addChainModel(element) {
            var form = $(element).parents('form');
            form.find('[name=_eventId]').val('addChainModel');
            form.submit();
        }

        function removeChainModel(element, modelIndex) {
            var form = $(element).parents('form');
            form.find('[name=_eventId]').val('removeChainModel');
            form.find('[name=modelIndex]').val(modelIndex);
            form.submit();
        }

        function addModelAttribute(element, modelIndex, attributeIndex) {
            var form = $(element).parents('form');
            form.find('[name=_eventId]').val('addAttribute');
            form.find('[name=modelIndex]').val(modelIndex);
            form.find('[name=attributeIndex]').val(attributeIndex);
            form.submit();
        }

        function removeModelAttribute(element, modelIndex, attributeIndex) {
            var form = $(element).parents('form');
            form.find('[name=_eventId]').val('removeAttribute');
            form.find('[name=modelIndex]').val(modelIndex);
            form.find('[name=attributeIndex]').val(attributeIndex);
            form.submit();
        }
    </script>
</div>