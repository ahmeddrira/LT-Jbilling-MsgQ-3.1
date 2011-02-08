<%@ page import="org.apache.commons.lang.WordUtils; com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Editor form for price models.

  @author Brian Cowdery
  @since  02-Feb-2011
--%>

<g:set var="type" value="${(model ? PriceModelStrategy.valueOf(model?.type) : PriceModelStrategy.METERED)}"/>
<g:set var="templateName" value="${WordUtils.uncapitalize(WordUtils.capitalizeFully(type.name(), ['_'] as char[]).replaceAll('_',''))}"/>

<div id="priceModel">
    <div class="column">
        <g:render template="/priceModel/strategy/${templateName}" model="[model: model, type: type, currencies: currencies]"/>
    </div>
    <div class="column">
        <g:render template="/priceModel/attributes" model="[model: model, type: type]"/>
    </div>

    <script type="text/javascript">
        /**
         * Re-render the pricing model form when the strategy is changed
         */
        $(function() {
            $('#model\\.type').change(function() {
                    $.ajax({
                       type: 'POST',
                       url:'${createLink(action: 'updateStrategy')}',
                       data: $('#priceModel').parents('form').serialize(),
                       success: function(data){ $('#priceModel').replaceWith(data); }
                    });
                }
            );
        });

        function add(index) {
            $.ajax({
               type: 'POST',
               url:'${createLink(action: 'addAttribute')}',
               data: $('#priceModel').parents('form').serialize(),
               success: function(data){ $('#priceModel').replaceWith(data); }
            });
        }

        function remove(index) {
            $('#attributeIndex').val(index);

            $.ajax({
               type: 'POST',
               url:'${createLink(action: 'removeAttribute')}',
               data: $('#priceModel').parents('form').serialize(),
               success: function(data){ $('#priceModel').replaceWith(data); }
            });
        }
    </script>
</div>

