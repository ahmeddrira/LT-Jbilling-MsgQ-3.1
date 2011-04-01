<%@ page import="com.sapienter.jbilling.server.util.Constants" %>


<g:hiddenField name="preference.typeId" value="${type.id}"/>

<g:applyLayout name="form/text">
    <content tag="label">Preference For</content>
    <content tag="label.for">preference.table</content>

    <div class="select5">
        <g:select name="preference.table"
                from="[Constants.TABLE_ENTITY, Constants.TABLE_BASE_USER]"
                valueMessagePrefix="preference.table"
                value="${preference?.jbillingTable?.name}"/>
    </div>

    <div class="inp-bg inp4" id="foreign-id">
        <g:textField class="field" name="preference.foreignId" value="${preference?.foreignId}"/>
    </div>
</g:applyLayout>

<g:applyLayout name="form/input">
    <content tag="label">String Value</content>
    <content tag="label.for">preference.strValue</content>
    <g:textField class="field" name="preference.strValue" value="${preference?.strValue ?: type.strDefValue}"/>
</g:applyLayout>

<g:applyLayout name="form/input">
    <content tag="label">Integer Value</content>
    <content tag="label.for">preference.intValue</content>
    <g:textField class="field" name="preference.intValue" value="${preference?.intValue ?: type.intDefValue}"/>
</g:applyLayout>

<g:applyLayout name="form/input">
    <content tag="label">Decimal Value</content>
    <content tag="label.for">preference.floatValue</content>
    <g:textField class="field" name="preference.floatValue" value="${formatNumber(number: preference?.floatValue ?: type.floatDefValue, formatName: 'decimal.format')}"/>
</g:applyLayout>

<script text="text/javascript">
    $(function() {
        $('#preference\\.table').change(function() {
            if ($(this).val() == '${Constants.TABLE_ENTITY}') {
                $('#foreign-id').hide();
            } else {

                $('#foreign-id').show();
            }
        }).change();
    })
</script>