
<%--
  _recentitems

  @author Brian Cowdery
  @since  09-12-2010
--%>

<div id="recent-items">
    <div class="heading">
        <strong><g:message code="recent.items.title"/></strong>
    </div>
    <ul class="list">
        <g:each var="item" in="${session['recent_items']?.reverse()}">
            <li>
                <g:if test="${item.type.controller == controllerName}">
                    <g:remoteLink controller="${item.type.controller}" action="select" id="${item.objectId}" onSuccess="render(data, second);">
                        <img src="${resource(dir:'images', file:item.type.icon)}" alt="${item.type.messageCode}"/>
                        <g:message code="${item.type.messageCode}"/> ${item.objectId}
                    </g:remoteLink>
                </g:if>
                <g:else>
                    <g:link controller="${item.type.controller}" action="list" id="${item.objectId}">
                        <img src="${resource(dir:'images', file:item.type.icon)}" alt="${item.type.messageCode}"/>
                        <g:message code="${item.type.messageCode}"/> ${item.objectId}
                    </g:link>
                </g:else>
            </li>
        </g:each>
    </ul>
</div>