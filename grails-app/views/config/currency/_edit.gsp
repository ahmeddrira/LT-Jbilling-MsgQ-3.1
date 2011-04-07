<%@ page contentType="text/html;charset=UTF-8" %>

<%--
  Shows an edit form for a currency (used to create new currencies).

  @author Brian Cowdery
  @since  07-Apr-2011
--%>

<div class="column-hold">
    <div class="heading">
        <strong>New Currency</strong>
    </div>

    <g:form name="save-currency-form" url="[action: 'saveCurrency']">

    <div class="box">
        <fieldset>
            <div class="form-columns">
                <g:hiddenField name="id" value="${currency?.id}"/>
%{--
                <g:applyLayout name="form/input">
                    <content tag="label">Name</content>
                    <content tag="label.for">name</content>
                    <g:textField name="name" class="field" value="${currency?.name}"/>
                </g:applyLayout>
--}%

                <g:applyLayout name="form/input">
                    <content tag="label">Currency Code</content>
                    <content tag="label.for">code</content>
                    <g:textField name="code" class="field" value="${currency?.code}" maxlenght="3"/>
                </g:applyLayout>

                <g:applyLayout name="form/input">
                    <content tag="label">Currency Symbol</content>
                    <content tag="label.for">symbol</content>
                    <g:textField name="symbol" class="field" value="${currency?.symbol}" maxlenght="10"/>
                </g:applyLayout>

                <g:applyLayout name="form/input">
                    <content tag="label">Country Code</content>
                    <content tag="label.for">countryCode</content>
                    <g:textField name="countryCode" class="field" value="${currency?.countryCode}" maxlength="2"/>
                </g:applyLayout>

                <g:applyLayout name="form/input">
                    <content tag="label">Exchange Rate</content>
                    <content tag="label.for">rate</content>
                    <g:textField name="rate" class="field" value="${currency?.rate}"/>
                </g:applyLayout>

                <g:applyLayout name="form/input">
                    <content tag="label">System Rate</content>
                    <content tag="label.for">sysRate</content>
                    <g:textField name="sysRate" class="field" value="${formatNumber(number: currency?.sysRate ?: BigDecimal.ONE, formatName: 'decimal.format')}"/>
                </g:applyLayout>

                <g:applyLayout name="form/checkbox">
                    <content tag="label">Active</content>
                    <content tag="label.for">inUse</content>
                    <g:checkBox name="inUse" class="cb" value="${currency?.inUse}"/>
                </g:applyLayout>

            </div>
        </fieldset>
    </div>

    </g:form>

    <div class="btn-box buttons">
        <ul>
            <li><a class="submit save" onclick="$('#save-currency-form').submit();"><span><g:message code="button.save"/></span></a></li>
            <li><a class="submit cancel" onclick="closePanel(this);"><span><g:message code="button.cancel"/></span></a></li>
        </ul>
    </div>
</div>