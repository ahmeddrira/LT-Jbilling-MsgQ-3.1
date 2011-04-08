
<div class="column-hold">
    <div class="heading">
        <strong>
            <g:if test="${selected}">
                <g:message code="filters.save.edit.title" args="[selected.name]"/>
            </g:if>
            <g:else>
                <g:message code="filters.save.new.title"/>
            </g:else>
        </strong>
    </div>

    <div class="box">
        <fieldset>
            <div class="form-columns">

                <g:applyLayout name="form/text">
                    <content tag="label"><g:message code="filters.save.label.id"/></content>

                    <g:if test="${selected}">
                        ${selected.id}
                    </g:if>
                    <g:else>
                        <em><g:message code="prompt.id.new"/></em>
                    </g:else>

                    <g:hiddenField name="id" value="${selected?.id}"/>
                </g:applyLayout>

                <g:applyLayout name="form/input">
                    <content tag="label"><g:message code="filters.save.label.name"/></content>
                    <content tag="label.for">name</content>
                    <g:textField class="field" name="name" value="${selected?.name}"/>
                </g:applyLayout>

            </div>
        </fieldset>

        <g:if test="${selected}">
            <br/>&nbsp;
            <p>
                <em><g:message code="filters.save.update.message"/></em>
            </p>
        </g:if>
    </div>

    <div class="btn-box">
        <a class="submit save" onclick="$('#filter-save-form').submit();">
            <span><g:message code="button.save"/></span>
        </a>
        <g:if test="${selected}">
            <g:remoteLink class="submit delete" controller="filter" action="delete" id="${selected.id}" update="filtersets">
                <span><g:message code="button.delete"/></span>
            </g:remoteLink>
        </g:if>
    </div>
</div>