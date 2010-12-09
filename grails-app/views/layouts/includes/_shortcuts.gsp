
<%--
  _shortcuts

  @author Brian Cowdery
  @since  09-12-2010
--%>

<div id="shortcuts">
    <div class="heading">
        <a class="arrow open"><strong><g:message code="shortcut.title"/></strong></a>
        <div class="drop">
            <ul>
                <li><g:link controller="user" action="create"><g:message code="shortcut.link.customer"/></g:link></li>
                <li><g:link controller="product" action="create"><g:message code="shortcut.link.product"/></g:link></li>
                <li><g:link controller="order" action="create"><g:message code="shortcut.link.order"/></g:link></li>
                <li><g:link controller="user" action="invoice"><g:message code="shortcut.link.invoice"/></g:link></li>
            </ul>
        </div>
    </div>
</div>