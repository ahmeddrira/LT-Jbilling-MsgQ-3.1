<div class="table-box">
<table cellpadding="0" cellspacing="0">
    <thead>
        <th><g:message code="title.notification.category" /></th>
    </thead>
    <tbody>
        <g:each in="${lst}" status="idx" var="dto">
            <tr>
                <td><g:remoteLink action="lists" id="${dto.id}"
                    before="register(this);"
                    onSuccess="render(data, next);"
                    params="[template:'show']"
                >
                    <strong> ${dto.getDescription(session['language_id'])}
                    </strong>
                </g:remoteLink></td>
            </tr>
        </g:each>
    </tbody>
</table>
</div>
<div class="btn-box">
    <g:remoteLink action="preferences"
        class="submit" before="register(this);"
        onSuccess="render(data, next);">
        <span><g:message code="button.preferences" /></span>
    </g:remoteLink>
</div>