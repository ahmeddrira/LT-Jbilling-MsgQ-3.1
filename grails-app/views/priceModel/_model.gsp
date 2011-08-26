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

<%@ page import="com.sapienter.jbilling.server.pricing.db.PriceModelDTO; com.sapienter.jbilling.server.pricing.PriceModelBL; com.sapienter.jbilling.server.pricing.db.ChainPosition; org.apache.commons.lang.WordUtils; com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Editor form for price models.

  @author Brian Cowdery
  @since  02-Feb-2011
--%>

<!-- model and date to display -->
<g:set var="startDate" value="${startDate ?: new Date()}"/>
<g:set var="model" value="${model ?: PriceModelBL.getWsPriceForDate(models, startDate)}"/>

<!-- local variables -->
<g:set var="types" value="${PriceModelStrategy.getStrategyByChainPosition(ChainPosition.START)}"/>
<g:set var="type" value="${model?.type ? PriceModelStrategy.valueOf(model.type) : types?.asList()?.first()}"/>
<g:set var="templateName" value="${WordUtils.uncapitalize(WordUtils.capitalizeFully(type.name(), ['_'] as char[]).replaceAll('_',''))}"/>
<g:set var="modelIndex" value="${0}"/>

<div id="priceModel">
    <div id="timeline">
        <div class="form-columns">
            <ul>
                <g:each var="modelEntry" in="${models.entrySet()}">
                    <g:if test="${model.equals(modelEntry.getValue()) || startDate.equals(modelEntry.getKey())}">
                        <li class="current">
                            <g:set var="startDate" value="${modelEntry.getKey()}"/>
                            <g:set var="date" value="${formatDate(date: startDate)}"/>
                            <a onclick="editDate('${date}')">${date}</a>
                        </li>
                    </g:if>
                    <g:else>
                        <li>
                            <g:set var="date" value="${formatDate(date: modelEntry.getKey())}"/>
                            <a onclick="editDate('${date}')">${date}</a>
                        </li>
                    </g:else>
                </g:each>

                <g:if test="${!models.containsKey(startDate)}">
                    <li class="current">
                        <g:set var="date" value="${formatDate(date: startDate)}"/>
                        <a onclick="editDate('${date}')">${date}</a>
                    </li>
                </g:if>

                <li class="new">
                    <a onclick="addDate()"><g:message code="button.add.price.date"/></a>
                </li>
            </ul>
        </div>

        %{-- button for "Add Date" instead of link in timeline --}%
        %{--
        <div class="form-columns">
            <div class="column">
                <g:applyLayout name="form/text">
                    <content tag="label">&nbsp;</content>
                    <a class="submit add" onclick="addDate()"><span><g:message code="button.add.price.date"/></span></a>
                </g:applyLayout>
            </div>
        </div>
        --}%
    </div>

    <!-- root price model -->
    <div class="form-columns">
        <div class="column">
            <g:applyLayout name="form/date">
                <content tag="label">Start Date</content>
                <content tag="label.for">startDate</content>
                <g:textField class="field" name="startDate" value="${formatDate(date: startDate, formatName: 'datepicker.format')}"/>
                <g:hiddenField name="originalStartDate" value="${formatDate(date: startDate, formatName: 'date.format')}"/>
            </g:applyLayout>

            <g:render template="/priceModel/strategy/${templateName}" model="[model: model, type: type, modelIndex: modelIndex, types: types, currencies: currencies]"/>
        </div>
        <div class="column">
            <g:render template="/priceModel/attributes" model="[model: model, type: type, modelIndex: modelIndex]"/>
        </div>
    </div>

    <!-- price models in chain -->
    <g:set var="types" value="${PriceModelStrategy.getStrategyByChainPosition(ChainPosition.MIDDLE, ChainPosition.END)}"/>
    <g:set var="next" value="${model?.next}"/>
    <g:while test="${next}">
        <g:set var="type" value="${next?.type ? PriceModelStrategy.valueOf(next.type) : types?.asList()?.first()}"/>
        <g:set var="templateName" value="${WordUtils.uncapitalize(WordUtils.capitalizeFully(type.name(), ['_'] as char[]).replaceAll('_',''))}"/>
        <g:set var="modelIndex" value="${modelIndex + 1}"/>

        <div class="form-columns">
            <hr/>
            <div class="column">
                <g:render template="/priceModel/strategy/${templateName}" model="[model: next, type: type, modelIndex: modelIndex, types: types, currencies: currencies]"/>
            </div>
            <div class="column">
                <g:render template="/priceModel/attributes" model="[model: next, type: type, modelIndex: modelIndex]"/>
            </div>
        </div>

        <g:set var="next" value="${next.next}"/>
    </g:while>

    <!-- spacer -->
    <div>
        <br/>&nbsp;
    </div>

    <!-- controls -->
    <div class="btn-row">
        <a class="submit add" onclick="addChainModel()"><span><g:message code="button.add.chain"/></span></a>
        <a class="submit save" onclick="saveDate()"><span><g:message code="button.save"/></span></a>
        <a class="submit delete" onclick="removeDate()"><span><g:message code="button.delete"/></span></a>

        <g:hiddenField name="attributeIndex"/>
        <g:hiddenField name="modelIndex"/>
    </div>


    <script type="text/javascript">
        /**
         * Re-render the pricing model form when the strategy is changed
         */
        $(function() {
            $('.model-type').change(function() {
                $.ajax({
                           type: 'POST',
                           url: '${createLink(action: 'updateStrategy')}',
                           data: $('#priceModel').parents('form').serialize(),
                           success: function(data) { $('#priceModel').replaceWith(data); }
                       });
            });
        });

        function editDate(date) {
            $('#startDate').val(date);

            $.ajax({
                       type: 'POST',
                       url: '${createLink(action: 'editDate')}',
                       data: $('#priceModel').parents('form').serialize(),
                       success: function(data) { $('#priceModel').replaceWith(data); }
                   });
        }

        function addDate() {
            $.ajax({
                       type: 'POST',
                       url: '${createLink(action: 'addDate')}',
                       data: $('#priceModel').parents('form').serialize(),
                       success: function(data) { $('#priceModel').replaceWith(data); }
                   });
        }

        function removeDate() {
            $.ajax({
                       type: 'POST',
                       url: '${createLink(action: 'removeDate')}',
                       data: $('#priceModel').parents('form').serialize(),
                       success: function(data) { $('#priceModel').replaceWith(data); }
                   });
        }

        function saveDate() {
            $.ajax({
                       type: 'POST',
                       url: '${createLink(action: 'saveDate')}',
                       data: $('#priceModel').parents('form').serialize(),
                       success: function(data) { $('#priceModel').replaceWith(data); }
                   });
        }

        function addChainModel() {
            $.ajax({
                       type: 'POST',
                       url: '${createLink(action: 'addChainModel')}',
                       data: $('#priceModel').parents('form').serialize(),
                       success: function(data) { $('#priceModel').replaceWith(data); }
                   });
        }

        function removeChainModel(element, modelIndex) {
            $('#modelIndex').val(modelIndex);

            $.ajax({
                       type: 'POST',
                       url: '${createLink(action: 'removeChainModel')}',
                       data: $('#priceModel').parents('form').serialize(),
                       success: function(data) { $('#priceModel').replaceWith(data); }
                   });
        }

        function addModelAttribute(element, modelIndex, attributeIndex) {
            $('#modelIndex').val(modelIndex);
            $('#attributeIndex').val(attributeIndex);

            $.ajax({
                       type: 'POST',
                       url: '${createLink(action: 'addAttribute')}',
                       data: $('#priceModel').parents('form').serialize(),
                       success: function(data) { $('#priceModel').replaceWith(data); }
                   });
        }

        function removeModelAttribute(element, modelIndex, attributeIndex) {
            $('#modelIndex').val(modelIndex);
            $('#attributeIndex').val(attributeIndex);

            $.ajax({
                       type: 'POST',
                       url: '${createLink(action: 'removeAttribute')}',
                       data: $('#priceModel').parents('form').serialize(),
                       success: function(data) { $('#priceModel').replaceWith(data); }
                   });
        }
    </script>
</div>
