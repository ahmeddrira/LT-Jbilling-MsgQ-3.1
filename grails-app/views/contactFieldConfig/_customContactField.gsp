<%@ page contentType="text/html;charset=UTF-8" %>


<div class="form-edit" style="width:650px">

    <div class="heading">
        <strong><g:message code="configuration.title.contact.fields"/></strong>
    </div>

    <div class="form-hold">
        <g:form name="save-customcontactfields-form" action="save">
            <g:hiddenField name="recCnt" value="${ageingSteps?.length}"/>
            <fieldset>
                <div class="form-columns" style="width:650px">
                    <div class="one_column" style="padding-right: 0px;">
                    <table class="innerTable" style="width: 100%;">
                        <thead class="innerHeader">
                             <tr>
                                <th><g:message code="contact.field.name"/></th>
                                <th class="medium"><g:message code="contact.field.datatype"/></th>
                                <th class="medium"><g:message code="contact.field.isReadOnly"/></th>
                             </tr>
                         </thead>
                         <tbody>
                            <g:each status="iter" var="type" in="${types}">
                                <tr>
                                    <td><label>${type.getDescriptionDTO(session['language_id'])?.content}</label></td>
                                    <td class="medium">
                                        <g:textField class="inp-bg inp4" name="dataType" value="${type.dataType}"/>
                                    </td>
                                    <td class="medium">
                                        <g:formatBoolean boolean="${type.readOnly > 0}"/>
                                    </td>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                    <div class="row">&nbsp;</div>
                    </div>
                </div>
            </fieldset>
             <div class="btn-box">
                <a onclick="$('#save-aging-form').submit();" class="submit save"><span><g:message code="button.save"/></span></a>
                <g:link controller="config" action="index" class="submit cancel"><span><g:message code="button.cancel"/></span></g:link>
            </div>
        </g:form>
    </div>
</div>