<%--
  General configuration options

  @author Brian Cowdery
  @since  03-Jan-2011
--%>

<!-- selected configuration menu item -->
<content tag="menu.item">general</content>

<!-- configuration panel -->
<div class="heading">
    <strong>General</strong>
</div>
<div class="box">
    <div class="form-columns">
        <div class="row">

            <g:applyLayout name="form/input">
                <content tag="label">Session Timeout</content>
                <content tag="label.for">timeout</content>
                <g:textField class="field" name="timeout" value="${timeout}"/>
            </g:applyLayout>

        </div>
    </div>
</div>
<div class="btn-box buttons">
    <ul>
        <li><a class="submit save" onclick="$('#form').submit();"><span>Save Changes</span></a></li>
        <li><a class="submit cancel" onclick="closePanel(this);"><span>Cancel</span></a></li>
    </ul>
</div>