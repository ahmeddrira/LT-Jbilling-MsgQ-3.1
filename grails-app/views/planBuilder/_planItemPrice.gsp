<%--
  Renders an PlanItemWS as an editable row for the plan builder preview pane.

  @author Brian Cowdery
  @since 24-Jan-2011
--%>
<g:set var="product" value="${products?.find{ it.id == planItem.itemId }}"/>
<g:set var="editable" value="${index == params.int('newLineIndex')}"/>

<g:formRemote name="price-${index}-update-form" url="[action: 'edit']" update="column2" method="GET">
    <g:hiddenField name="_eventId" value="updateLine"/>
    <g:hiddenField name="execution" value="${flowExecutionKey}"/>

    <!-- review line ${index} -->
    <li id="line-${index}" class="line ${editable ? 'active' : ''}">
        <span class="description">
            ${product.description}
        </span>
        <span class="rate">
            <g:formatNumber number="${planItem.model.getRateAsDecimal()}" formatName="money.format"/>
        </span>
        <span class="strategy">
            <g:message code="plan.price.model.type.${planItem.model.type}"/>
        </span>
    </li>

    <!-- line ${index} editor -->
    <li id="line-${index}-editor" class="editor ${editable ? 'open' : ''}">
        <div class="box">
            <div class="form-columns">
                <!-- strategy -->
                <!-- precedence -->
                <!-- rate -->
                <!-- currency -->

                <!-- attributes -->

                <g:hiddenField name="index" value="${index}"/>
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