<div class="heading">
    <strong>Edit Notes</strong>
</div>
<g:formRemote id="notes-form" name="notes-form" url="[action: 'saveNotes']" before="register(this);" onSuccess="render(data, prev); closePanel(this);">
    <g:hiddenField name="id" value="${selected.id}"/>

    <div class="box">
        <div class="form-columns">
            <div class="row">
                <label class="lb">Notes:</label>
                <g:textArea name="notes" value="${selected.customer.notes}"/>
            </div>
        </div>
    </div>
    <div class="btn-box buttons">
        <ul>
            <li><a class="submit save" onclick="$('#notes-form').submit();"><span>Save Changes</span></a></li>
            <li><a class="submit cancel" onclick="closePanel(this);"><span>Cancel</span></a></li>
        </ul>
    </div>
</g:formRemote>