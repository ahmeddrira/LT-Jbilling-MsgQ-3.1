<%@ page import="org.apache.commons.lang.WordUtils; com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Editor form for price models.

  @author Brian Cowdery
  @since  02-Feb-2011
--%>

<g:set var="type" value="${(model ? PriceModelStrategy.valueOf(model?.type) : PriceModelStrategy.METERED)}"/>
<g:set var="templateName" value="${WordUtils.uncapitalize(WordUtils.capitalizeFully(type.name(), ['_'] as char[]).replaceAll('_',''))}"/>
<g:set var="modelIndex" value="${0}"/>

<div id="priceModel">
    <!-- root price model -->
    <div class="form-columns">
        <div class="column">
            <g:render template="/priceModel/strategy/${templateName}" model="[model: model, type: type, modelIndex: modelIndex, currencies: currencies]"/>
        </div>
        <div class="column">
            <g:render template="/priceModel/attributes" model="[model: model, type: type, modelIndex: modelIndex]"/>
        </div>
    </div>

    <!-- price models in chain -->
    <g:set var="next" value="${model.next}"/>
    <g:while test="${next}">
        <g:set var="type" value="${PriceModelStrategy.valueOf(next?.type)}"/>
        <g:set var="templateName" value="${WordUtils.uncapitalize(WordUtils.capitalizeFully(type.name(), ['_'] as char[]).replaceAll('_',''))}"/>
        <g:set var="modelIndex" value="${modelIndex + 1}"/>

        <div class="form-columns">
            <hr/>
            <div class="column">
                <g:render template="/priceModel/strategy/${templateName}" model="[model: next, type: type, modelIndex: modelIndex, currencies: currencies]"/>
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
                <a class="submit add" onclick="chain()"><span>Add Chain</span></a>
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

        function chain() {
            $.ajax({
               type: 'POST',
               url: '${createLink(action: 'addChainModel')}',
               data: $('#priceModel').parents('form').serialize(),
               success: function(data) { $('#priceModel').replaceWith(data); }
            });
        }

        function add(modelIndex, attributeIndex) {
            $('#modelIndex').val(modelIndex);
            $('#attributeIndex').val(attributeIndex);

            $.ajax({
               type: 'POST',
               url: '${createLink(action: 'addAttribute')}',
               data: $('#priceModel').parents('form').serialize(),
               success: function(data) { $('#priceModel').replaceWith(data); }
            });
        }

        function remove(modelIndex, attributeIndex) {
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
