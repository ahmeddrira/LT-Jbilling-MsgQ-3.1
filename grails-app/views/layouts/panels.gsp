<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <g:render template="/layouts/includes/head"/>

    <jqui:resources/>

    <g:javascript library="panels"/>

    <g:layoutHead/>
</head>
<body>
<div id="wrapper">
    <g:render template="/layouts/includes/header"/>

    <div id="main">
        <g:render template="/layouts/includes/breadcrumbs"/>

        <!-- left column -->
        <div id="left-column">
            <div class="heading">
                <strong><g:message code="filters.title"/></strong>
            </div>

            <g:pageProperty name="page.filters"/>

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

            <%-- todo: recently viewed items functionality --%>
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
