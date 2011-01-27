<%@ page contentType="text/html;charset=UTF-8" %>

<%--
  Shows edit form for a contact type.

  @author Brian Cowdery
  @since  27-Jan-2011
--%>

<div class="column-hold">
    <div class="heading">
        <g:if test="${selected}">
        <strong>
            ${selected.getDescription(session['language_id'])}
            <em>${selected.id}</em>
        </strong>
        </g:if>
        <g:else>
            <strong>New Contact Type</strong>
        </g:else>
    </div>

    <g:form id="save-type-form" name="notes-form" url="[action: save]">

    <div class="box">
        <fieldset>
            <div class="form-columns">
                <g:hiddenField name="id" value="${selected?.id}"/>

                <g:applyLayout name="form/text">
                    <content tag="label">Primary Contact</content>
                    <g:formatBoolean boolean="${selected?.isPrimary > 0}"/>
                    <g:hiddenField name="isPrimary" value="${selected?.isPrimary ?: 0}"/>
                </g:applyLayout>

                <g:each var="language" in="${languages}">
                    <g:applyLayout name="form/input">
                        <content tag="label">${language.description}</content>
                        <content tag="label.for">language.${language.id}</content>
                        <g:textField class="field" name="language.${language.id}" value="${selected?.getDescription(language.id)}"/>
                    </g:applyLayout>
                </g:each>
            </div>
        </fieldset>
    </div>

    </g:form>

    <div class="btn-box buttons">
        <ul>
            <g:if test="${!selected}">
                <li><a class="submit save" onclick="$('#save-type-form').submit();"><span><g:message code="button.save"/></span></a></li>
            </g:if>
            <li><a class="submit cancel" onclick="closePanel(this);"><span><g:message code="button.cancel"/></span></a></li>
        </ul>
    </div>
</div>