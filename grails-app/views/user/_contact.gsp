<div id="contact-${contactType.id}" class="contact" style="${contactType.isPrimary > 0 ? '' : 'display: none;'}">

    <g:hiddenField name="contact-${contactType?.id}.type" value="${contactType?.id}"/>
    <g:hiddenField name="contact-${contactType?.id}.id" value="${contact?.id}"/>

    <g:applyLayout name="form/input">
        <content tag="label"><g:message code="prompt.organization.name"/></content>
        <content tag="label.for">contact.organizationName</content>
        <g:textField class="field" name="contact-${contactType?.id}.organizationName" value="${contact?.organizationName}" />
    </g:applyLayout>

    <g:applyLayout name="form/input">
        <content tag="label"><g:message code="prompt.first.name"/></content>
        <content tag="label.for">contact.firstName</content>
        <g:textField class="field" name="contact-${contactType?.id}.firstName" value="${contact?.firstName}" />
    </g:applyLayout>

    <g:applyLayout name="form/input">
        <content tag="label"><g:message code="prompt.last.name"/></content>
        <content tag="label.for">contact.lastName</content>
        <g:textField class="field" name="contact-${contactType?.id}.lastName" value="${contact?.lastName}" />
    </g:applyLayout>

    <g:applyLayout name="form/text">
        <content tag="label"><g:message code="prompt.phone.number"/></content>
        <content tag="label.for">contact.phoneCountryCode</content>
        <span>
            <g:textField class="field" name="contact-${contactType?.id}.phoneCountryCode" value="${contact?.phoneCountryCode}" maxlength="3" size="2"/>
            -
            <g:textField class="field" name="contact-${contactType?.id}.phoneAreaCode" value="${contact?.phoneAreaCode}" maxlength="5" size="3"/>
            -
            <g:textField class="field" name="contact-${contactType?.id}.phoneNumber" value="${contact?.phoneNumber}" maxlength="10" size="8"/>
        </span>
    </g:applyLayout>

    <g:applyLayout name="form/input">
        <content tag="label"><g:message code="prompt.email"/></content>
        <content tag="label.for">contact.email</content>
        <g:textField class="field" name="contact-${contactType?.id}.email" value="${contact?.email}" />
    </g:applyLayout>

    <g:applyLayout name="form/input">
        <content tag="label"><g:message code="prompt.address1"/></content>
        <content tag="label.for">contact.address1</content>
        <g:textField class="field" name="contact-${contactType?.id}.address1" value="${contact?.address1}" />
    </g:applyLayout>

    <g:applyLayout name="form/input">
        <content tag="label"><g:message code="prompt.address2"/></content>
        <content tag="label.for">contact.address2</content>
        <g:textField class="field" name="contact-${contactType?.id}.address2" value="${contact?.address2}" />
    </g:applyLayout>

    <g:applyLayout name="form/input">
        <content tag="label"><g:message code="prompt.city"/></content>
        <content tag="label.for">contact.city</content>
        <g:textField class="field" name="contact-${contactType?.id}.city" value="${contact?.city}" />
    </g:applyLayout>

    <g:applyLayout name="form/input">
        <content tag="label"><g:message code="prompt.state"/></content>
        <content tag="label.for">contact.stateProvince</content>
        <g:textField class="field" name="contact-${contactType?.id}.stateProvince" value="${contact?.stateProvince}" />
    </g:applyLayout>

    <g:applyLayout name="form/input">
        <content tag="label"><g:message code="prompt.zip"/></content>
        <content tag="label.for">contact.postalCode</content>
        <g:textField class="field" name="contact-${contactType?.id}.postalCode" value="${contact?.postalCode}" />
    </g:applyLayout>

    <g:applyLayout name="form/input">
        <content tag="label"><g:message code="prompt.country"/></content>
        <content tag="label.for">contact.countryCode</content>
        <g:textField class="field" name="contact-${contactType?.id}.countryCode" value="${contact?.countryCode}" />
    </g:applyLayout>

    <g:applyLayout name="form/checkbox">
        <content tag="label"><g:message code="prompt.include.in.notifications"/></content>
        <content tag="label.for">contact.include</content>
        <g:checkBox class="cb checkbox" name="contact-${contactType?.id}.include" checked="${contact?.include > 0}"/>
    </g:applyLayout>
</div>