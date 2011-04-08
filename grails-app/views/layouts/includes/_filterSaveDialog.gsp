
<div id="filter-save-dialog" class="dialog" title="Save Filters">
    <fieldset>
        <div class="form-columns">
            <g:formRemote name="filter-save-form" url="[controller: 'filter', action: 'save']" update="filters">
                <!-- content rendered using ajax -->
            </g:formRemote>
        </div>
    </fieldset>
</div>

<script type="text/javascript">
    $(function() {
        $('#filter-save-dialog').dialog({
            autoOpen: false,
            height: 400,
            width: 500,
            modal: true,
            buttons: {
                Save: function() {
                    $(this).dialog("close");
                    $('#filter-save-form').submit();
                    $('#filter-save-form input:visible').val("");
                },
                Cancel: function() {
                    $(this).dialog("close");
                }
            },
            open: function() {
                $('#filter-save-form').load("${createLink(controller: 'filter', action: 'filtersets')}");
            },
            close: function() {
                $('#filters').load("${createLink(controller: 'filter', action: 'filters')}");
            }
        });
    });
</script>