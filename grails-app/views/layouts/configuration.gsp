<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <g:render template="/layouts/includes/head"/>
    <g:javascript library="panels"/>

    <script type="text/javascript">
        function renderRecentItems() {
            $.ajax({
                url: "${resource(dir:'')}/recentItem",
                global: false,
                success: function(data) { $("#recent-items").replaceWith(data) }
            });
        }

        $(document).ajaxSuccess(function(e, xhr, settings) {
            renderRecentItems();
        });

        /*
            Highlight clicked rows in the configuration side menu
         */
        $('#left-column ul.list li').click(function() {
            $(this).parents('ul.list').find('li.active').removeClass('active');
            $(this).addClass('active');
        });
    </script>

    <g:layoutHead/>
</head>
<body>
<div id="wrapper">
    <g:render template="/layouts/includes/header"/>

    <div id="main">
        <g:render template="/layouts/includes/breadcrumbs"/>

        <div id="left-column">
            <!-- configuration menu -->
            <div class="menu-items">
                <ul class="list">
                    <li class="${pageProperty(name: 'page.menu.item') == 'aging' ? 'active' : ''}">
                        <g:remoteLink action="aging" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.aging"/>
                        </g:remoteLink>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'billing' ? 'active' : ''}">
                        <g:remoteLink action="billing" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.billing"/>
                        </g:remoteLink>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'blacklist' ? 'active' : ''}">
                        <g:remoteLink action="blacklist" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.blacklist"/>
                        </g:remoteLink>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'company' ? 'active' : ''}">
                        <g:remoteLink action="company" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.company"/>
                        </g:remoteLink>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'currencies' ? 'active' : ''}">
                        <g:remoteLink action="currencies" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.currencies"/>
                        </g:remoteLink>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'email' ? 'active' : ''}">
                        <g:remoteLink action="email" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.email"/>
                        </g:remoteLink>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'general' ? 'active' : ''}">
                        <g:remoteLink action="general" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.general"/>
                        </g:remoteLink>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'invoices' ? 'active' : ''}">
                        <g:remoteLink action="invoices" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.invoices"/>
                        </g:remoteLink>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'mediation' ? 'active' : ''}">
                        <g:remoteLink action="mediation" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.mediation"/>
                        </g:remoteLink>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'notification' ? 'active' : ''}">
                        <g:link controller="notifications" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.notification"/>
                        </g:link>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'periods' ? 'active' : ''}">
                        <g:remoteLink action="periods" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.order.periods"/>
                        </g:remoteLink>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'plugins' ? 'active' : ''}">
                        <g:link controller="plugin" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.plugins"/>
                        </g:link>
                    </li>
                    <li class="${pageProperty(name: 'page.menu.item') == 'users' ? 'active' : ''}">
                        <g:remoteLink action="users" before="register(this);" onSuccess="render(data, first);">
                            <g:message code="configuration.menu.users"/>
                        </g:remoteLink>
                    </li>
                </ul>
            </div>

            <!-- shortcuts -->
            <g:render template="/layouts/includes/shortcuts"/>

            <!-- recently viewed items -->
            <g:render template="/layouts/includes/recent"/>
        </div>

        <!-- content columns -->
        <div class="columns-holder">
            <g:render template="/layouts/includes/messages"/>

            <div id="viewport">
                <div class="column panel" index="1">
                    <div id="column1" class="column-hold">
                        <g:pageProperty name="page.column1"/>
                    </div>
                </div>

                <div class="column panel" index="2">
                    <div id="column2" class="column-hold">
                        <g:pageProperty name="page.column2"/>
                    </div>
                </div>

                <div class="column panel" index="3">
                    <div id="column3" class="column-hold">
                        <g:pageProperty name="page.column3"/>
                    </div>
                </div>

                <div class="column panel" index="4">
                    <div id="column4" class="column-hold">
                        <g:pageProperty name="page.column4"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
