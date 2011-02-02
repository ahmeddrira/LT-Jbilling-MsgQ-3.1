<%@ page import="jbilling.SearchType" %>

<%--
  Page header for all common jBilling layouts.

  This contains the jBilling top-navigation bar, search form and main navigation menu.

  @author Brian Cowdery
  @since  23-11-2010
--%>

<!-- header -->
<div id="header">
    <h1><a href="${resource(dir:'')}">jBilling</a></h1>
    <div class="search">        
        <g:form controller="search" name="search-form">
            <fieldset>
                <input type="image" class="btn" src="${resource(dir:'images', file:'icon-search.gif')}" onclick="$('#search-form').submit()"/>
                <div class="input-bg">                    
                    <g:textField name="id" value="${cmd?.id ?: message(code:'search.title')}" class="default"/>
                    <a href="#" class="open"></a>
                    <div class="popup">
                        <div class="top-bg">
                            <div class="btm-bg">
                                <div class="input-row">
                                    <g:radio id="customers" name="type" value="CUSTOMERS" checked="${!cmd || cmd?.type?.toString() == 'CUSTOMERS'}"/>
                                    <label for="customers"><g:message code="search.option.customers"/></label>
                                </div>
                                <div class="input-row">
                                    <g:radio id="orders" name="type" value="ORDERS" checked="${cmd?.type?.toString() == 'ORDERS'}"/>
                                    <label for="orders"><g:message code="search.option.orders"/></label>
                                </div>
                                <div class="input-row">
                                    <g:radio id="invoices" name="type" value="INVOICES" checked="${cmd?.type?.toString() == 'INVOICES'}"/>
                                    <label for="invoices"><g:message code="search.option.invoices"/></label>
                                </div>
                                <div class="input-row">
                                    <g:radio id="payments" name="type" value="PAYMENTS" checked="${cmd?.type?.toString() == 'PAYMENTS'}"/>
                                    <label for="payments"><g:message code="search.option.payments"/></label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
        </g:form>
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
        <ul>
            <li class="${controllerName == 'user' ? 'active' : ''}">
                <g:link controller="user"><span><g:message code="menu.link.customers"/></span><em></em></g:link>
            </li>
            <li class="${controllerName == 'invoice' ? 'active' : ''}">
                <g:link controller="invoice"><span><g:message code="menu.link.invoices"/></span><em></em></g:link>
            </li>
            <li class="${controllerName == 'payment' ? 'active' : ''}">
                <g:link controller="payment"><span><g:message code="menu.link.payments.refunds"/></span><em></em></g:link>
            </li>
            <li class="${controllerName == 'order' ? 'active' : ''}">
                <g:link controller="order"><span><g:message code="menu.link.orders"/></span><em></em></g:link>
            </li>
            <li class="${controllerName == 'billing' ? 'active' : ''}">
                <g:link controller="billing"><span><g:message code="menu.link.billing"/></span><em></em></g:link>
            </li>
            <li class="${controllerName == 'mediation' ? 'active' : ''}">
                <g:link controller="mediation"><span><g:message code="menu.link.mediation"/></span><em></em></g:link>
            </li>
            <li class="${controllerName == 'reports' ? 'active' : ''}">
                <g:link controller="reports"><span><g:message code="menu.link.reports"/></span><em></em></g:link>
            </li>
            <li class="${controllerName == 'product' ? 'active' : ''}">
                <g:link controller="product"><span><g:message code="menu.link.products"/></span><em></em></g:link>
            </li>
            <li class="${controllerName == 'plan' ? 'active' : ''}">
                <g:link controller="plan"><span><g:message code="menu.link.plans"/></span><em></em></g:link>
            </li>
            <li class="${controllerName == 'config' || controllerName == 'plugin' || controllerName == 'notifications' || controllerName == 'billingconfiguration' || controllerName == 'contactFieldConfig' ? 'active' : ''}">
                <g:link controller="config"><span><g:message code="menu.link.configuration"/></span><em></em></g:link>
            </li>
        </ul>
    </div>
</div>
