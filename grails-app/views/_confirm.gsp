
<div id="confirm-dialog" class="bg-lightbox" title=<g:message code="popup.confirm.title"/>>
    <table style="margin: 3px 0px 0px 10px">
        <tbody><tr>
            <td><img src="/jbilling/images/icon34.gif" alt=""></td>
            <td class="col2" style="padding-left:7px"><g:message code="${confirmMessage}"/></td>
        </tr></tbody>
    </table>
</div>

<script type="text/javascript">
    $('#confirm-dialog').dialog({
        autoOpen: false,
        height: 155,
        width: 375,
        modal: true,
        buttons: {
    	<g:message code="prompt.yes"/>: function() {
        	    window.location = "<g:createLink controller="${popup_controller}" action="${popup_action}" id="${popup_id}"/>";
            },
        <g:message code="prompt.no"/>: function() {
                $(this).dialog("close");
            }
        }
    });
</script>