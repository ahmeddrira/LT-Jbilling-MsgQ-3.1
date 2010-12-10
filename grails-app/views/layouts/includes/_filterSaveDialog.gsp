
<%--
  _filterSaveDialog

  @author Brian Cowdery
  @since  09-12-2010
--%>

<div id="filter-save-dialog" class="dialog" title="Save Filters">
    <g:formRemote name="filter-save-form" url="[controller: 'filter', action: 'save']" update="filters">
        <fieldset>
            <div class="form-columns">
                <g:applyLayout name="form/input">
                    <content tag="label">Name</content>
                    <content tag="label.for">name</content>
                    <g:textField name="name" class="field"/>
                </g:applyLayout>
            </div>
        </fieldset>
    </g:formRemote>
</div>

<script type="text/javascript">
    $('#filter-save-dialog').dialog({
        autoOpen: false,
        height: 200,
        width: 400,
        modal: true,
        buttons: {
            Save: function() {
                $('#filter-save-form').submit();
                $('#filter-save-form input:visible').val("");
                $(this).dialog("close");
            },
            Cancel: function() {
                $(this).dialog("close");
            }
        }
    });
</script>