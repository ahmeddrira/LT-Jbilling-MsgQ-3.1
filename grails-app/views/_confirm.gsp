
<div id="confirm-dialog" class="bg-lightbox" title=<g:message code="popup.confirm.title"/>>
    <g:message code="${confirmMessage}"/>
</div>

<script type="text/javascript">
    $('#confirm-dialog').dialog({
        autoOpen: false,
        height: 200,
        width: 400,
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