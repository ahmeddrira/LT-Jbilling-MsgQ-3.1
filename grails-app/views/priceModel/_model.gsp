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

<%@ page import="com.sapienter.jbilling.server.pricing.db.ChainPosition; org.apache.commons.lang.WordUtils; com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Editor form for price models.

  @author Brian Cowdery
  @since  02-Feb-2011
--%>

<g:set var="types" value="${PriceModelStrategy.getStrategyByChainPosition(ChainPosition.START)}"/>
<g:set var="type" value="${model?.type ? PriceModelStrategy.valueOf(model.type) : types?.asList()?.first()}"/>
<g:set var="templateName" value="${WordUtils.uncapitalize(WordUtils.capitalizeFully(type.name(), ['_'] as char[]).replaceAll('_',''))}"/>
<g:set var="modelIndex" value="${0}"/>

<div id="priceModel">
    <!-- root price model -->
    <div class="form-columns">
        <div class="column">
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

    <!-- controls -->
    <div class="form-columns">
        <div class="column">
            <g:applyLayout name="form/text">
                <content tag="label">&nbsp;</content>
                <a class="submit add" onclick="addChainModel()"><span><g:message code="button.add.chain"/></span></a>
            </g:applyLayout>

            <g:hiddenField name="attributeIndex"/>
            <g:hiddenField name="modelIndex"/>
        </div>
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
