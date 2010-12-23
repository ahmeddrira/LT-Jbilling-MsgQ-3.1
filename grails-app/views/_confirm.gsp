<%--
    Popup confirmation dialog.

    This dialog can operate in both AJAX and non-AJAX modes. In AJAX mode a target can be re-rendered
    using the return value from the AJAX call (see g:formRemote #update).

    Parameters:
        controller  Name of the controller
        action      Name of the controller action
        id          ID value for the action

    Optional parameters:
        formParams  A map of key value parameters to pass to the action

        ajax        If true, use an AJAX remote form to call the defined controller/action/id
        update      Element to update with AJAX return value

        onYes       Javascript to execute when "Yes" is clicked
        onNo        Javascript to execute when "No" is clicked


    The rendered confirmation dialog can be shown hidden using the element ID "confirm-dialog-${action}-${name}" where the
    ID value is that of the passed ID value of the action.

    To open, use "$('#confirm-dialog-action-123'}').dialog('open');" or the shorthand "showConfirm('action-123');"
 --%>

<g:set var="name" value="${[action, id].collect{ it }.join('-')}"/>

<div id="confirm-dialog-${name}" class="bg-lightbox" title="${message(code: 'popup.confirm.title')}">
    <!-- command form -->
    <g:if test="${ajax}">
        <g:formRemote name="confirm-command-form-${name}" url="[controller: controller, action: action, id: id]" update="${update}">
            <g:hiddenField name="id" value="${id}"/>
            <g:each var="param" in="${formParams?.entrySet()}">
                <g:if test="${param.value != null && !param.value == 'null' }">
                    <g:hiddenField name="${param.key}" value="${param.value}"/>
                </g:if>
            </g:each>
        </g:formRemote>
    </g:if>
    <g:else>
        <g:form name="confirm-command-form-${name}" url="[controller: controller, action: action, id: id]">
            <g:hiddenField name="id" value="${id}"/>
            <g:each var="param" in="${formParams?.entrySet()}">
                <g:if test="${param.value != null && !param.value == 'null' }">
                    <g:hiddenField name="${param.key}" value="${param.value}"/>
                </g:if>
            </g:each>
        </g:form>
    </g:else>

    <!-- confirm dialog content body -->
    <table style="margin: 3px 0 0 10px">
        <tbody><tr>
            <td>
                <img src="/jbilling/images/icon34.gif" alt="">
            </td>
            <td class="col2" style="padding-left: 7px">
                <g:message code="${message}" args="[id]"/>
            </td>
        </tr></tbody>
    </table>
</div>

<script type="text/javascript">
    $('#confirm-dialog-${name}').dialog({
        autoOpen: false,
        height: 200,
        width: 375,
        modal: true,
        buttons: {
            "${message(code: 'prompt.yes')}": function() {
                ${onYes};
                $("#confirm-command-form-${name}").submit();
                $(this).dialog('close');
            },
            "${message(code: 'prompt.no')}": function() {
                ${onNo};
                $(this).dialog('close');
            }
        }
    });

    function showConfirm(name) {
        $('#confirm-dialog-' + name).dialog('open');
    }
</script>