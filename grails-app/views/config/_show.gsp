<%@ page import="com.sapienter.jbilling.server.util.Constants" %>

<div class="column-hold">
    <div class="heading">
        <strong>
            Preference
            <em>${selected.id}</em>
        </strong>
    </div>

    <div class="box">
        <p class="description">
            ${selected.getDescription(session['language_id'])}
        </p>

        <fieldset>
            <div class="form-columns">


                <g:each var="preference" status="index" in="${selected.preferences}">
                    <g:if test="${preference.jbillingTable.name == Constants.TABLE_ENTITY}">
                        <g:if test="${preference.foreignId == session['company_id']}">
                            <g:render template="preference" model="[ preference: preference, type: selected]"/>
                        </g:if>
                    </g:if>
                    <g:else>
                        <g:render template="preference" model="[ preference: preference, type: selected]"/>
                    </g:else>
                </g:each>

                <g:if test="${!selected.preferences}">
                    <g:render template="preference" model="[type: selected]"/>
                </g:if>

            </div>
        </fieldset>
    </div>

    <div class="btn-box buttons">
        <ul>
            <li><a class="submit save" onclick="$('#save-type-form').submit();"><span><g:message code="button.save"/></span></a></li>
            <li><a class="submit cancel" onclick="closePanel(this);"><span><g:message code="button.cancel"/></span></a></li>
        </ul>
    </div>

</div>
