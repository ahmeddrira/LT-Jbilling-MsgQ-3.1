<%@ page import="com.sapienter.jbilling.server.user.UserBL; com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>

<%--
  Customer table template. The customer table is used multiple times for rendering the
  main list and for rendering a separate list of sub-accounts. 

  @author Brian Cowdery
  @since  24-Nov-2010
--%>

<div class="heading table-heading">
    <strong class="name"><g:message code="customer.table.th.name"/></strong>
    <strong class="parent"><g:message code="customer.table.th.hierarchy"/></strong>
    <strong class="owed"><g:message code="customer.table.th.balance"/></strong>
    <strong class="status"><g:message code="customer.table.th.status"/></strong>
    <strong class="user"><g:message code="customer.table.th.user.id"/></strong>
</div>

<div class="table-box">
    <ul>
        <g:each in="${users}" var="user">
            <g:set var="customer" value="${user.customer}"/>
            <g:set var="contact" value="${ContactDTO.findByUserId(user.id)}"/>

            <li>
                <span class="block last">
                    <g:if test="${customer}">
                        <g:if test="${customer.isParent == 1 && customer.parent}">
                            <%-- is a parent, but also a child of another account --%>
                            <g:remoteLink action="subaccounts" id="${user.id}" before="register(this);" onSuccess="render(data, next);">
                                <img src="${resource(dir:'images', file:'icon17.gif')}" alt="parent and child" />
                                <span>${customer.children.size()}</span>
                            </g:remoteLink>
                        </g:if>
                        <g:elseif test="${customer.isParent == 1 && !customer.parent}">
                            <%-- is a top level parent --%>
                            <g:remoteLink action="subaccounts" id="${user.id}" before="register(this);" onSuccess="render(data, next);">
                                <img src="${resource(dir:'images', file:'icon18.gif')}" alt="parent" />
                                <span>${customer.children.size()}</span>
                            </g:remoteLink>
                        </g:elseif>
                        <g:elseif test="${customer.isParent == 0 && customer.parent}">
                            <%-- is a child account, but not a parent --%>
                            <img src="${resource(dir:'images', file:'icon19.gif')}" alt="child" />
                        </g:elseif>
                        <g:else>
                            <span>&nbsp;</span>
                        </g:else>
                    </g:if>
                    <g:else>
                        <span>&nbsp;</span>
                    </g:else>
                </span>
                <g:remoteLink action="select" id="${user.id}" before="register(this);" onSuccess="render(data, next);">
                    <span class="block">
                        <span><g:formatNumber number="${new UserBL().getBalance(user.id)}" type="currency" currencyCode="${user.currency.code}"/></span>
                    </span>
                    <span class="block">
                        <span>
                            <g:if test="${user.userStatus.id > 1 && user.userStatus.id < 5}">
                                <img src="${resource(dir:'images', file:'icon15.gif')}" alt="overdue" />
                            </g:if>
                            <g:elseif test="${user.userStatus.id >= 5}">
                                <img src="${resource(dir:'images', file:'icon16.gif')}" alt="suspended" />
                            </g:elseif>
                            <g:else>
                                <span>&nbsp;</span>
                            </g:else>
                        </span>
                    </span>
                    <span class="block">
                        <span>${user.id}</span>
                    </span>
                    <strong>
                        <g:if test="${contact && (contact.firstName || contact.lastName)}">
                            ${contact.firstName} ${contact.lastName}
                        </g:if>
                        <g:else>
                            ${user.userName}
                        </g:else>
                    </strong>
                    <em>${contact?.organizationName}</em>
                </g:remoteLink>
            </li>
        </g:each>
    </ul>
</div>

<div class="pager-box">
    <g:paginate controller="user" action="list" total="${users.totalCount}"/> 
</div>

<div class="btn-box">
    <a href="${createLink(action: 'create')}" class="submit add"><span><g:message code="button.create"/></span></a>
    <a href="${createLink(action: 'delete')}" class="submit delete"><span><g:message code="button.delete"/></span></a>
</div>