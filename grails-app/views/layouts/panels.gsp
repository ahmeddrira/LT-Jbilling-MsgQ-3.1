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
            <g:if test="${filters}">
                <div id="filters">
                    <div class="heading">
                        <strong><g:message code="filters.title"/></strong>
                    </div>

                    <g:formRemote id="filters-form" name="filters-form" url="[action: list]" onSuccess="render(data, first);">
                        <g:hiddenField name="applyFilter" value="true"/>

                        <ul class="accordion">
                            <g:each var="filter" in="${filters}">
                                <g:if test="${filter.visible}">
                                    <li>
                                        <g:render template="/filter/${filter.template}" model="[filter: filter]"/>
                                    </li>
                                </g:if>
                            </g:each>
                        </ul>
                    </g:formRemote>
                    
                    <div class="btn-hold">
                        <a href="#" class="submit apply" onclick="applyFilters()"><span><g:message code="filters.apply.button"/></span></a>

                        <g:if test="${filters.find { !it.visible }}">
                            <div class="dropdown">
                                <a href="#" class="submit add open"><span><g:message code="filters.add.button"/></span></a>
                                <div class="drop">
                                    <ul>
                                        <g:each var="filter" in="${filters}">
                                            <g:if test="${!filter.visible}">
                                                <li>
                                                    <a href="#">${filter.field}</a>
                                                </li>
                                            </g:if>
                                        </g:each>
                                    </ul>
                                </div>
                            </div>
                        </g:if>

                        <a href="#" class="submit2 save"><span><g:message code="filters.save.button"/></span></a>
                        <a href="#" class="submit2 load"><span><g:message code="filters.load.button"/></span></a>
                    </div>
                </div>
            </g:if>

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
                    <li><a href="#"><img src="${resource(dir:'images', file:'icon09.gif')}" alt="invoice" />Invoice</a></li>
                    <li><a href="#"><img src="${resource(dir:'images', file:'icon10.gif')}" alt="payment" />Payment</a></li>
                    <li><a href="#"><img src="${resource(dir:'images', file:'icon11.gif')}" alt="customer" />Customer</a></li>
                    <li><a href="#"><img src="${resource(dir:'images', file:'icon12.gif')}" alt="order" />Order</a></li>
                    <li><a href="#"><img src="${resource(dir:'images', file:'icon13.gif')}" alt="other" />Others</a></li>
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
