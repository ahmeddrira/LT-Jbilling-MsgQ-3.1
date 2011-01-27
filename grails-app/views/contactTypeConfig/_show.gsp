<%@ page contentType="text/html;charset=UTF-8" %>

<%--
  Shows a contact type.

  @author Brian Cowdery
  @since  27-Jan-2011
--%>

<div class="column-hold">
    <div class="heading">
        <strong>
            ${selected.getDescription(session['language_id'])}
            <em>${selected.id}</em>
        </strong>
    </div>

    <div class="box">
        <fieldset>
            <div class="form-columns">
                <g:applyLayout name="form/text">
                    <content tag="label">Primary Contact</content>
                    <g:formatBoolean boolean="${selected?.isPrimary > 0}"/>
                </g:applyLayout>

                <g:each var="language" in="${languages}">
                    <g:applyLayout name="form/text">
                        <content tag="label">${language.description}</content>
                        ${selected?.getDescription(language.id)}
                    </g:applyLayout>
                </g:each>
            </div>
        </fieldset>
    </div>

    <div class="btn-box buttons">
        <div class="row"></div>
    </div>
</div>