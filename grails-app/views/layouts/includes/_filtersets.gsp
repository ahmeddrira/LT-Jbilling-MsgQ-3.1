
<g:if test="${filtersets}">
    <table cellspacing="0" cellpadding="0" class="innerTable">
        <thead class="innerHeader">
        <tr>
            <th>Saved Filter Sets</th>
            <th>Action</th>
        </tr>
        </thead>

        <tbody>
        <g:each var="filterset" in="${filtersets}">
            <tr>
                <td class="innerContent">
                    ${filterset.name}
                </td>
                <td class="innerContent">
                    <g:remoteLink controller="filter" action="delete" id="${filterset.id}" update="filter-save-dialog">
                        <img src="${resource(dir:'images', file:'cross.png')}" alt="remove"/>
                    </g:remoteLink>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>

    <br/>&nbsp;
</g:if>

<g:applyLayout name="form/input">
    <content tag="label">Name</content>
    <content tag="label.for">filterset.name</content>
    <g:textField name="filterset.name" class="field"/>
</g:applyLayout>