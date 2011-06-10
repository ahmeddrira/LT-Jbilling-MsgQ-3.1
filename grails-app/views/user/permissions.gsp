%{--
  jBilling - The Enterprise Open Source Billing System
  Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

  This file is part of jbilling.

  jbilling is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  jbilling is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
  --}%

<html>
<head>
    <meta name="layout" content="main"/>
</head>
<body>
<div class="form-edit">

    <div class="heading">
        <strong>
            <g:message code="permissions.title"/>
        </strong>
    </div>

    <div class="form-hold">
        <g:form name="user-permission-edit-form" action="savePermissions">
            <fieldset>

                <!-- user information -->
                <div class="form-columns">
                    <div class="column">
                        <g:applyLayout name="form/text">
                            <content tag="label"><g:message code="prompt.customer.number"/></content>
                            ${user.userId}
                            <g:hiddenField name="id" value="${user.userId}"/>
                        </g:applyLayout>

                        <g:applyLayout name="form/text">
                            <content tag="label"><g:message code="prompt.login.name"/></content>
                            ${user?.userName}
                        </g:applyLayout>

                        <g:applyLayout name="form/text">
                            <content tag="label"><g:message code="prompt.user.role"/></content>
                            ${role.getTitle(session['language_id'])}
                        </g:applyLayout>
                    </div>

                    <div class="column">

                        <g:applyLayout name="form/text">
                            <content tag="label"><g:message code="prompt.organization.name"/></content>
                            ${contact?.organizationName}
                        </g:applyLayout>

                        <g:applyLayout name="form/text">
                            <content tag="label"><g:message code="prompt.user.name"/></content>
                            ${contact?.firstName} ${contact?.lastName}
                        </g:applyLayout>
                    </div>
                </div>

                <!-- user permissions -->
                <g:each var="permissionType" status="n" in="${permissionTypes}">
                    <div class="form-columns">
                        <h3>${permissionType.description}</h3>

                        <!-- column 1 -->
                        <div class="column">
                            <g:each var="permission" status="i" in="${permissionType.permissions}">
                                <g:set var="userPermission" value="${permissions.find{ it.id == permission.id }}"/>
                                <g:set var="rolePermission" value="${role.permissions.find{ it.id == permission.id }}"/>

                                <g:if test="${i % 2 == 0}">

                                    %{
                                        permission.initializeAuthority()
                                    }%

                                    <g:applyLayout name="form/checkbox">
                                        <content tag="group.label">
                                            ${permission.id}
                                            <em>(<g:formatBoolean boolean="${rolePermission != null}" true="${message(code: 'boolean.on')}" false="${message(code: 'boolean.off')}"/>)</em>:
                                        </content>
                                        <content tag="label">
                                            <g:if test="${(userPermission && !rolePermission) || (!userPermission && rolePermission)}">
                                                <strong>
                                                    ${permission.getDescription(session['language_id']) ?: permission.authority}
                                                </strong>
                                            </g:if>
                                            <g:else>
                                                ${permission.getDescription(session['language_id']) ?: permission.authority}
                                            </g:else>
                                        </content>
                                        <content tag="label.for">permission.${permission.id}</content>

                                        <g:checkBox name="permission.${permission.id}" class="check cb" checked="${userPermission}"/>
                                    </g:applyLayout>
                                </g:if>
                            </g:each>
                        </div>

                        <!-- column 2 -->
                        <div class="column">
                            <g:each var="permission" status="i" in="${permissionType.permissions}">
                                <g:set var="userPermission" value="${permissions.find{ it.id == permission.id }}"/>
                                <g:set var="rolePermission" value="${role.permissions.find{ it.id == permission.id }}"/>

                                <g:if test="${i % 2 == 1}">

                                    %{
                                        permission.initializeAuthority()
                                    }%

                                    <g:applyLayout name="form/checkbox">
                                        <content tag="group.label">
                                            ${permission.id}
                                            <em>(<g:formatBoolean boolean="${rolePermission != null}" true="${message(code: 'boolean.on')}" false="${message(code: 'boolean.off')}"/>)</em>:
                                        </content>
                                        <content tag="label">
                                            <g:if test="${(userPermission && !rolePermission) || (!userPermission && rolePermission)}">
                                                <strong>
                                                    ${permission.getDescription(session['language_id']) ?: permission.authority}
                                                </strong>
                                            </g:if>
                                            <g:else>
                                                ${permission.getDescription(session['language_id']) ?: permission.authority}
                                            </g:else>
                                        </content>
                                        <content tag="label.for">permission.${permission.id}</content>

                                        <g:checkBox name="permission.${permission.id}" class="check cb" checked="${userPermission}"/>
                                    </g:applyLayout>
                                </g:if>
                            </g:each>
                        </div>
                    </div>

                    <g:if test="${n < permissionTypes.size()}">
                        <!-- spacer between permission types -->
                        <div>
                            &nbsp;<br/>
                        </div>
                    </g:if>
                </g:each>

                <!-- spacer -->
                <div>
                    &nbsp;<br/>
                </div>

                <div class="buttons">
                    <ul>
                        <li>
                            <a onclick="$('#user-permission-edit-form').submit()" class="submit save"><span><g:message code="button.save"/></span></a>
                        </li>
                        <li>
                            <g:link action="list" class="submit cancel"><span><g:message code="button.cancel"/></span></g:link>
                        </li>
                    </ul>
                </div>

            </fieldset>
        </g:form>
    </div>
</div>
</body>
</html>