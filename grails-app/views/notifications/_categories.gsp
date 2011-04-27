<div class="table-box">
    <table cellpadding="0" cellspacing="0">
        <thead>
            <th><g:message code="title.notification.category" /></th>
        </thead>
        <tbody>
            <g:each in="${lst}" status="idx" var="dto">
                <tr class="${dto.id == (selected)? 'active' : '' }">
                    <td><g:remoteLink breadcrumb="id" class="cell" action="list" id="${dto.id}" params="['template': 'list']"
                            before="register(this);" onSuccess="render(data, next);">
                        <strong> ${dto.getDescription(session['language_id'])}</strong></g:remoteLink>
                    </td>
                </tr>
            </g:each>
        </tbody>
    </table>
</div>
<div class="btn-box">
    <div class="row"></div>
</div>