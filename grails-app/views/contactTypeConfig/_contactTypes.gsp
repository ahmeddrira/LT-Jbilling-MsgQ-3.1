<%@ page contentType="text/html;charset=UTF-8" %>

<%--
  Shows a list of contact types.

  @author Brian Cowdery
  @since  27-Jan-2011
--%>

<div class="table-box">
    <table id="users" cellspacing="0" cellpadding="0">
        <thead>
            <tr>
                <th><g:message code="contact.type.th.type"/></th>
                <th class="small"><g:message code="contact.type.th.primary"/></th>
            </tr>
        </thead>

        <tbody>
            <g:each var="type" in="${types}">
                <tr id="type-${type.id}" class="${selected?.id == type.id ? 'active' : ''}">
                    <td>
                        <g:remoteLink class="cell double" action="show" id="${type.id}" before="register(this);" onSuccess="render(data, next);">
                            <strong>${type.getDescription(session['language_id'])}</strong>
                            <em><g:message code="product.id.label" args="[type.id]"/></em>
                        </g:remoteLink>
                    </td>

                    <td class="small">
                        <g:remoteLink class="cell" action="show" id="${type.id}" before="register(this);" onSuccess="render(data, next);">
                            <g:formatBoolean boolean="${type.isPrimary > 0}"/>
                        </g:remoteLink>
                    </td>
                </tr>
            </g:each>
        </tbody>
    </table>
</div>

<div class="btn-box">
    <g:remoteLink action='edit' class="submit add" before="register(this);" onSuccess="render(data, next);">
        <span><g:message code="button.create"/></span>
    </g:remoteLink>
</div>