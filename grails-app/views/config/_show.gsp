<%@ page import="com.sapienter.jbilling.server.util.Constants" %>

<div class="column-hold">
    <div class="heading">
        <strong>
            <g:message code="preference.title"/>
            <em>${selected.id}</em>
        </strong>
    </div>

    <g:form name="save-preference-form" url="[action: 'save']">

    <div class="box">
        <p class="description">
            ${selected.getDescription(session['language_id'])}
        </p>

        <p>
            <em>${selected.getInstructions(session['language_id'])}</em>
        </p>

        <fieldset>
            <div class="form-columns">

                <g:set var="hasPreference" value="${false}"/>

                <g:each var="preference" status="index" in="${selected.preferences}">
                    <g:if test="${preference.jbillingTable.name == Constants.TABLE_ENTITY}">
                        <g:if test="${preference.foreignId == session['company_id']}">
                            <g:set var="hasPreference" value="${true}"/>
                            <g:render template="preference" model="[ preference: preference, type: selected]"/>
                        </g:if>
                    </g:if>
                </g:each>

                <g:if test="${!hasPreference}">
                    <g:render template="preference" model="[type: selected]"/>
                </g:if>

            </div>
        </fieldset>
    </div>

    </g:form>

    <div class="btn-box buttons">
        <ul>
            <li><a class="submit save" onclick="$('#save-preference-form').submit();"><span><g:message code="button.save"/></span></a></li>
            <li><a class="submit cancel" onclick="closePanel(this);"><span><g:message code="button.cancel"/></span></a></li>
        </ul>
    </div>

</div>
