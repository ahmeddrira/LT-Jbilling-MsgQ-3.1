<%--
  Quick edit form for the selected customer's notes.

  @author Brian Cowdery
  @since  26-Nov-2010
--%>

<div class="heading">
    <strong><g:message code="customer.detail.edit.note.title"/></strong>
</div>

<g:formRemote id="notes-form" name="notes-form" url="[action: 'saveNotes']" before="register(this);" onSuccess="render(data, prev); closePanel(this);">
    <g:hiddenField name="id" value="${selected.id}"/>

    <div class="box">
        <div class="form-columns">
            <div class="row">
                <label class="lb"><g:message code="customer.detail.note.title"/></label>
                <g:textArea name="notes" value="${selected.customer.notes}" rows="5" cols="60"/>
            </div>
        </div>
    </div>

    <div class="btn-box buttons">
        <ul>
            <li><a class="submit save" onclick="$('#notes-form').submit();"><span><g:message code="button.save"/></span></a></li>
            <li><a class="submit cancel" onclick="closePanel(this);"><span><g:message code="button.cancel"/></span></a></li>
        </ul>
    </div>
</g:formRemote>