<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactDTO" contentType="text/html;charset=UTF-8" %>

<%--
  Shows a list of internal users.

  @author Brian Cowdery
  @since  04-Apr-2011
--%>

<div class="table-box">
    <table id="users" cellspacing="0" cellpadding="0">
        <thead>
            <tr>
                <th><g:message code="users.th.login"/></th>
                <th><g:message code="users.th.name"/></th>
                <th><g:message code="users.th.organization"/></th>
                <th class="small"><g:message code="users.th.role"/></th>
            </tr>
        </thead>

        <tbody>
            <g:each var="user" in="${users}">
                <g:set var="contact" value="${ContactDTO.findByUserId(user.id)}"/>

                <tr id="user-${user.id}" class="${selected?.id == user.id ? 'active' : ''}">
                    <td>
                        <g:remoteLink class="cell double" action="show" id="${user.id}" before="register(this);" onSuccess="render(data, next);">
                            <strong>${user.userName}</strong>
                            <em>Id: ${user.id}</em>
                        </g:remoteLink>
                    </td>

                    <td>
                        <g:remoteLink class="cell" action="show" id="${user.id}" before="register(this);" onSuccess="render(data, next);">
                            ${contact.firstName} ${contact.lastName}
                        </g:remoteLink>
                    </td>

                    <td>
                        <g:remoteLink class="cell" action="show" id="${user.id}" before="register(this);" onSuccess="render(data, next);">
                            ${contact.organizationName}
                        </g:remoteLink>
                    </td>

                    <td class="small">
                        <g:remoteLink class="cell" action="show" id="${user.id}" before="register(this);" onSuccess="render(data, next);">
                            ${user.roles?.asList()?.first()?.getTitle(session['language_id'])}
                        </g:remoteLink>
                    </td>
                </tr>

            </g:each>
        </tbody>
    </table>
</div>

<div class="pager-box">
    <div class="row">
        <div class="results">
            <g:render template="/layouts/includes/pagerShowResults" model="[steps: [10, 20, 50], update: 'column1']"/>
        </div>
    </div>

    <div class="row">
        <util:remotePaginate controller="user" action="list" params="[applyFilter: true]" total="${users?.totalCount ?: 0}" update="column1"/>
    </div>
</div>

<div class="btn-box">
    <g:link action="edit" class="submit add">
        <span><g:message code="button.create"/></span>
    </g:link>
</div>