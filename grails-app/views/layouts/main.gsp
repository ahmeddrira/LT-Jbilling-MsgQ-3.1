<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <title><g:layoutTitle default="jBilling" /></title>

    <link rel="shortcut icon" href="${resource(dir:'images', file:'favicon.ico')}" type="image/x-icon" />

    <link media="all" rel="stylesheet" href="${resource(dir:'css', file:'default.css')}" type="text/css" />
    <!--[if lt IE 8]><link rel="stylesheet" href="${resource(dir:'css', file:'lt7.css')}" type="text/css" media="screen"/><![endif]-->

    <g:javascript library="jquery" plugin="jquery"/>
    <g:javascript library="ie-hover" />

    <g:layoutHead/>
</head>
<body>
<div id="wrapper">
    <!-- header -->
    <div id="header">
        <h1><a href="${resource(dir:'')}">jBilling</a></h1>
        <%-- todo: search action to support global searches. --%>
        <div class="search">
            <form action="#"> 
                <fieldset>
                    <input type="image" src="${resource(dir:'images', file:'icon-search.gif')}" value="" class="btn" />
                    <div class="input-bg">
                        <input type="text" class="text" value="${message(code:'topnav.search.title')}" />
                        <a href="#" class="open"></a>
                        <div class="popup">
                            <div class="top-bg">
                                <div class="btm-bg">
                                    <div class="input-row">
                                        <input type="radio" id="check01" name="search" checked="checked" />
                                        <label for="check01"><g:message code="topnav.search.option.anywhere"/></label>
                                    </div>
                                    <div class="input-row">
                                        <input type="radio" id="check02" name="search" />
                                        <label for="check02"><g:message code="topnav.search.option.users"/></label>
                                    </div>
                                    <div class="input-row">
                                        <input type="radio" id="check03" name="search" />
                                        <label for="check03"><g:message code="topnav.search.option.invoices"/></label>
                                    </div>
                                    <div class="input-row">
                                        <input type="radio" id="check04" name="search" />
                                        <label for="check04"><g:message code="topnav.search.option.emails"/></label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
        <ul class="top-nav">
            <%-- todo: great user using first name if available, add actions for account manipulation, training and help --%>
            <li><g:message code="topnav.greeting"/> <sec:loggedInUserInfo field="username"/></li>
            <li><a href="#"><img src="${resource(dir:'images', file:'icon25.gif')}" alt="account" /><g:message code="topnav.link.account"/></a></li>
            <li><a href="#"><img src="${resource(dir:'images', file:'icon26.gif')}" alt="training" /><g:message code="topnav.link.training"/></a></li>
            <li><a href="#"><img src="${resource(dir:'images', file:'icon27.gif')}" alt="help" /><g:message code="topnav.link.help"/></a></li>
            <li><g:link controller='logout'><img src="${resource(dir:'images', file:'icon28.gif')}" alt="logout" /><g:message code="topnav.link.logout"/></g:link></li>
        </ul>
        <div id="navigation">
            <%-- select the current menu item based on the controller name --%>
            <%-- todo: update as controllers are written. many of the names here are my best-guess at what the controller names will be --%>
            <ul>
                <li <g:if test="${controllerName == 'user'}">class="active"</g:if>>
                    <g:link controller="user"><span><g:message code="menu.link.customers"/></span><em></em></g:link>
                </li>
                <li <g:if test="${controllerName == 'invoice'}">class="active"</g:if>>
                    <g:link controller="invoice"><span><g:message code="menu.link.invoices"/></span><em></em></g:link>
                </li>
                <li <g:if test="${controllerName == 'payment'}">class="active"</g:if>>
                    <g:link controller="payment"><span><g:message code="menu.link.payments.refunds"/></span><em></em></g:link>
                </li>
                <li <g:if test="${controllerName == 'order'}">class="active"</g:if>>
                    <g:link controller="order"><span><g:message code="menu.link.orders"/></span><em></em></g:link>
                </li>
                <li <g:if test="${controllerName == 'billing'}">class="active"</g:if>>
                    <g:link controller="billing"><span><g:message code="menu.link.billing"/></span><em></em></g:link>
                </li>
                <li <g:if test="${controllerName == 'mediation'}">class="active"</g:if>>
                    <g:link controller="mediation"><span><g:message code="menu.link.mediation"/></span><em></em></g:link>
                </li>
                <li <g:if test="${controllerName == 'reports'}">class="active"</g:if>>
                    <g:link controller="reports"><span><g:message code="menu.link.reports"/></span><em></em></g:link>
                </li>
                <li <g:if test="${controllerName == 'product'}">class="active"</g:if>>
                    <g:link controller="product"><span><g:message code="menu.link.products"/></span><em></em></g:link>
                </li>
                <li <g:if test="${controllerName == 'configuration'}">class="active"</g:if>>
                    <g:link controller="configuration"><span><g:message code="menu.link.configuration"/></span><em></em></g:link>
                </li>
            </ul>
        </div>
    </div>

    <div id="main">
        <!-- breadcrumbs -->
        <div class="breadcrumbs">
            <ul>
                <%-- todo: add the grails breadcrumbs plugin for easy breadcrumb trails - http://www.grails.org/plugin/breadcrumbs --%>
                <%-- quick and dirty breadcrumbs --%>
                <li><a href="${resource(dir:'')}"><g:message code="breadcrumb.link.home"/></a></li>
                <g:if test="${controllerName != null}">
                    <li><g:link controller="${controllerName}"><g:message code="breadcumb.link.${controllerName}"/></g:link></li>
                </g:if>
                <g:if test="${actionName != null && actionName != 'index'}">
                    <li><g:link controller="${controllerName}"><g:message code="breadcumb.link.${controllerName}.${actionName}"/></g:link></li>
                </g:if>
            </ul>
        </div>

        <g:layoutBody />

    </div>
</div>
</body>
</html>
