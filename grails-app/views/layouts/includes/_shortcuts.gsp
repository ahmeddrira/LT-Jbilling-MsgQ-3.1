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
                <g:each var="shortcut" in="${session['shortcuts']}">
                    <li>
                        <g:link controller="${shortcut.controller}" action="${shortcut.action}" id="${shortcut.objectId}">
                            <g:message code="${shortcut.messageCode}" args="[shortcut.objectId]"/>
                        </g:link>
                        <g:remoteLink class="delete" controller="shortcut" action="remove" params="[id: shortcut.id]" update="shortcuts"
                            style=" width:9px;
                            height:9px;
                            text-indent:-9999px;
                            overflow:hidden;
                            background:url(../images/icon03.gif) no-repeat;
                            float:right;
                            margin:11px 8px 0 0;
                            padding:0;"/>
                    </li>
                </g:each>
            </ul>
        </div>
    </div>
</div>