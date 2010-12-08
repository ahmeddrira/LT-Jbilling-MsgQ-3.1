<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <g:render template="/layouts/includes/head"/>
    <g:javascript library="panels"/>

    <script type="text/javascript">
        function applyFilters() {
            $('#filters-form input:visible, #filters-form select:visible').each(function() {
                var title = $(this).parents('li').find('.title');
                if ($(this).val()) {
                    title.addClass('active');
                } else {
                    title.removeClass('active');
                }
            });

            $('#filters-form').submit();
        }
    </script>

    <g:layoutHead/>
</head>
<body>
<div id="wrapper">
    <g:render template="/layouts/includes/header"/>

    <div id="main">
        <g:render template="/layouts/includes/breadcrumbs"/>

        <div id="left-column">
            <!-- filters -->
            <g:formRemote id="filters-form" name="filters-form" url="[action: list]" onSuccess="render(data, first);">
                <g:hiddenField name="applyFilter" value="true"/>
                
                <div id="filters">
                    <g:if test="${filters}">
                        <g:render template="/filter/filters"/>
                    </g:if>
                </div>
            </g:formRemote>

            <!-- shortcuts -->
            <div id="shortcuts">
                <div class="heading">
                    <a href="#" class="arrow open"><strong><g:message code="shortcut.title"/></strong></a>
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

            <!-- recently viewed items -->
            <div id="recent-items">
                <div class="heading">
                    <strong><g:message code="recent.items.title"/></strong>
                </div>
                <ul class="list">
                    <g:each var="item" in="${recent.reverse()}">
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
        </div>


        <!-- content columns -->
        <div class="columns-holder">
            <g:render template="/layouts/includes/messages"/>

            <div id="viewport">
                <div class="column panel" index="1">
                    <div class="column-hold">
                        <g:pageProperty name="page.column1"/>
                    </div>
                </div>

                <div class="column panel" index="2">
                    <div class="column-hold">
                        <g:pageProperty name="page.column2"/>
                    </div>
                </div>

                <div class="column panel" index="3">
                    <div class="column-hold">
                        <g:pageProperty name="page.column3"/>
                    </div>
                </div>

                <div class="column panel" index="4">
                    <div class="column-hold">
                        <g:pageProperty name="page.column4"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
