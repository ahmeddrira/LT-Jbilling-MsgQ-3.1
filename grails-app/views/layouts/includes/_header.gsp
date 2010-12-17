<%--
  Page header for all common jBilling layouts.

  This contains the jBilling top-navigation bar, search form and main navigation menu.

  @author Brian Cowdery
  @since  23-11-2010
--%>

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
        <li><g:message code="topnav.greeting"/> <sec:loggedInUserInfo field="plainUsername"/></li>
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
                <g:link controller="user" breadcrumb="controller"><span><g:message code="menu.link.customers"/></span><em></em></g:link>
            </li>
            <li <g:if test="${controllerName == 'invoice'}">class="active"</g:if>>
                <g:link controller="invoice" breadcrumb="controller"><span><g:message code="menu.link.invoices"/></span><em></em></g:link>
            </li>
            <li <g:if test="${controllerName == 'payment'}">class="active"</g:if>>
                <g:link controller="payment" breadcrumb="controller"><span><g:message code="menu.link.payments.refunds"/></span><em></em></g:link>
            </li>
            <li <g:if test="${controllerName == 'order'}">class="active"</g:if>>
                <g:link controller="order" breadcrumb="controller"><span><g:message code="menu.link.orders"/></span><em></em></g:link>
            </li>
            <li <g:if test="${controllerName == 'billing'}">class="active"</g:if>>
                <g:link controller="billing" breadcrumb="controller"><span><g:message code="menu.link.billing"/></span><em></em></g:link>
            </li>
            <li <g:if test="${controllerName == 'mediation'}">class="active"</g:if>>
                <g:link controller="mediation" breadcrumb="controller"><span><g:message code="menu.link.mediation"/></span><em></em></g:link>
            </li>
            <li <g:if test="${controllerName == 'reports'}">class="active"</g:if>>
                <g:link controller="reports" breadcrumb="controller"><span><g:message code="menu.link.reports"/></span><em></em></g:link>
            </li>
            <li <g:if test="${controllerName == 'product'}">class="active"</g:if>>
                <g:link controller="product" breadcrumb="controller"><span><g:message code="menu.link.products"/></span><em></em></g:link>
            </li>
            <li <g:if test="${controllerName == 'configuration'}">class="active"</g:if>>
                <g:link controller="plugin" breadcrumb="controller"><span><g:message code="menu.link.configuration"/></span><em></em></g:link>
            </li>
        </ul>
    </div>
</div>
