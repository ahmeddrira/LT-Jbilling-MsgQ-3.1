<%--
  All configuration preferences.

  @author Brian Cowdery
  @since  03-Jan-2011
--%>

<!-- selected configuration menu item -->
<content tag="menu.item">all</content>

<!-- configuration panel -->
<div class="heading">
    <strong><g:message code="configuration.title.all"/></strong>
</div>
<div class="box">
    <div class="form-columns">
        <div class="row">

            <p>List of all preferences.</p>

        </div>
    </div>
</div>
<div class="btn-box buttons">
    <ul>
        <li><a class="submit save"><span><g:message code="button.save"/></span></a></li>
        <li><a class="submit cancel" onclick="closePanel(this);"><span><g:message code="button.cancel"/></span></a></li>
    </ul>
</div>