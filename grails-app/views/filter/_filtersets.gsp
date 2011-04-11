
<div id="filterset-list" class="column">
    <div class="column-hold">
        <div class="table-box">
            <table id="users" cellspacing="0" cellpadding="0">
                <thead>
                <tr>
                    <th><g:message code="filters.save.th.name"/></th>
                    <th class="medium"><g:message code="filters.save.th.filters"/></th>
                </tr>
                </thead>
                <tbody>

                <g:each var="filterset" in="${filtersets}">
                    <tr id="filterset-${filterset.id}" class="${selected?.id == filterset.id ? 'active' : ''}">
                        <td>
                            <g:remoteLink class="cell double" controller="filter" action="edit" id="${filterset.id}" update="filterset-edit">
                                <strong>${filterset.name}</strong>
                                <em><g:message code="table.id.format" args="[filterset.id]"/></em>
                            </g:remoteLink>
                        </td>
                        <td>
                            <g:set var="count" value="${filterset.filters.findAll { it.value }?.size()}"/>
                            <g:remoteLink class="cell" controller="filter" action="edit" id="${filterset.id}" update="filterset-edit">
                                ${count}
                            </g:remoteLink>
                        </td>
                    </tr>
                </g:each>

                </tbody>
            </table>
        </div>
        <div class="btn-box">
            <div class="row"></div>
        </div>
    </div>
</div>

<div id="filterset-edit" class="column">
    <g:render template="edit" model="[selected: selected, filters: filters]"/>
</div>