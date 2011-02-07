<%@ page import="com.sapienter.jbilling.server.pricing.db.PriceModelStrategy; com.sapienter.jbilling.server.pricing.db.PriceModelStrategy; com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Editor form for price model attributes.

  This template is not the same as the attribute UI in the plan builder. The plan builder
  uses remote AJAX calls that can only be used in a web-flow. This template is to be used
  for standard .gsp pages.

  @author Brian Cowdery
  @since  02-Feb-2011
--%>

<g:set var="strategy" value="${(model ? com.sapienter.jbilling.server.pricing.db.PriceModelStrategy.valueOf(model?.type) : com.sapienter.jbilling.server.pricing.db.PriceModelStrategy.METERED).getStrategy()}"/>
<g:set var="attributeIndex" value="${0}"/>

<div id="attributes">
    <!-- all attribute definitions -->
    <g:each var="definition" in="${strategy?.attributeDefinitions}">
        <g:set var="attributeIndex" value="${attributeIndex + 1}"/>
        <g:set var="attribute" value="${model.attributes.remove(definition.name)}"/>

        <g:applyLayout name="form/attribute">
            <g:if test="${attributeIndex == 1}">
                <content tag="label"><g:message code="plan.mode.attributes"/></content>
            </g:if>
            <content tag="name">
                <g:textField class="field" name="attribute.${attributeIndex}.name" value="${definition.name}" readonly="true"/>
            </content>
            <content tag="value">
                <g:textField class="field" name="attribute.${attributeIndex}.value" value="${attribute}"/>
            </content>
            <g:if test="${definition.required}">
                <span><g:message code="plan.model.attribute.required"/></span>
            </g:if>
        </g:applyLayout>
    </g:each>

    <!-- remaining user-defined attributes -->
    <g:each var="attribute" in="${model?.attributes?.entrySet()}">
        <g:set var="attributeIndex" value="${attributeIndex + 1}"/>

        <g:applyLayout name="form/attribute">
            <g:if test="${attributeIndex == 1}">
                <content tag="label"><g:message code="plan.mode.attributes"/></content>
            </g:if>
            <content tag="name">
                <g:textField class="field" name="attribute.${attributeIndex}.name" value="${attribute.key}"/>
            </content>
            <content tag="value">
                <g:textField class="field" name="attribute.${attributeIndex}.value" value="${attribute.value}"/>
            </content>

            <a href="#" onclick="remove(${attributeIndex})">
                <img src="${resource(dir:'images', file:'cross.png')}" alt="remove"/>
            </a>
        </g:applyLayout>
    </g:each>

    <!-- one empty row -->
    <g:set var="attributeIndex" value="${attributeIndex + 1}"/>
    <g:applyLayout name="form/attribute">
        <g:if test="${attributeIndex == 1}">
            <content tag="label"><g:message code="plan.mode.attributes"/></content>
        </g:if>
        <content tag="name">
            <g:textField class="field" name="attribute.${attributeIndex}.name"/>
        </content>
        <content tag="value">
            <g:textField class="field" name="attribute.${attributeIndex}.value"/>
        </content>

        <a href="#" onclick="add(${attributeIndex})">
            <img src="${resource(dir:'images', file:'add.png')}" alt="remove"/>
        </a>
    </g:applyLayout>

    <g:hiddenField name="attributeIndex" value="${attributeIndex}"/>

    <script type="text/javascript">
        function add(index) {
            $.ajax({
               type: 'POST',
               url:'${createLink(action: 'addAttribute')}',
               data: $('#attributes').parents('form').serialize(),
               success: function(data){ $('#attributes').replaceWith(data); },
            });
        }

        function remove(index) {
            $('#attributeIndex').val(index);

            $.ajax({
               type: 'POST',
               url:'${createLink(action: 'removeAttribute')}',
               data: $('#attributes').parents('form').serialize(),
               success: function(data){ $('#attributes').replaceWith(data); },
            });
        }
    </script>
</div>


