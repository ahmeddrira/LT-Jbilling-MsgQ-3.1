<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactDTO" contentType="text/html;charset=UTF-8" %>

<%--
  Shows an internal user.

  @author Brian Cowdery
  @since  04-Apr-2011
--%>

<g:set var="customer" value="${selected.customer}"/>
<g:set var="contact" value="${ContactDTO.findByUserId(selected.id)}"/>

<div class="column-hold">
    <div class="heading">
        <strong>
            <g:if test="${contact?.firstName || contact?.lastName}">
                ${contact.firstName} ${contact.lastName}
            </g:if>
            <g:else>
                ${selected.userName}
            </g:else>
            <em><g:if test="${contact}">${contact.organizationName}</g:if></em>
        </strong>
    </div>

    <!-- user details -->
    <div class="box">
        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
            <tr>
                <td><g:message code="customer.detail.user.user.id"/></td>
                <td class="value">${selected.id}</td>
            </tr>
            <tr>
                <td><g:message code="customer.detail.user.username"/></td>
                <td class="value">${selected.userName}</td>
            </tr>
            <tr>
                <td><g:message code="customer.detail.user.status"/></td>
                <td class="value">${selected.userStatus.description}</td>
            </tr>
            <tr>
                <td><g:message code="user.language"/></td>
                <td class="value">${selected.language.getDescription()}</td>
            </tr>

            <tr>
                <td><g:message code="customer.detail.user.created.date"/></td>
                <td class="value"><g:formatDate date="${selected.createDatetime}" formatName="date.pretty.format"/></td>
            </tr>
            <tr>
                <td><g:message code="user.last.login"/></td>
                <td class="value"><g:formatDate date="${selected.lastLogin}" formatName="date.pretty.format"/></td>
            </tr>
            <tr>
                <td><g:message code="customer.detail.user.email"/></td>
                <td class="value"><a href="mailto:${contact?.email}">${contact?.email}</a></td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="btn-box">
        <div class="row">
            <g:link action="edit" id="${selected.id}" class="submit edit"><span><g:message code="button.edit"/></span></g:link>
            <a onclick="showConfirm('delete-${selected.id}');" class="submit delete"><span><g:message code="button.delete"/></span></a>
        </div>
    </div>

    <g:render template="/confirm"
              model="['message': 'user.delete.confirm',
                      'controller': 'user',
                      'action': 'delete',
                      'id': selected.id,
                      'ajax': true,
                      'update': 'column1',
                      'onYes': 'closePanel(\'#column2\')'
                     ]"/>

</div>