%{--
  jBilling - The Enterprise Open Source Billing System
  Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

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

<%--
  Security proof of concept page that contains examples of security tags.

  @see http://burtbeckwith.github.com/grails-spring-security-core/docs/manual/guide/6%20Helper%20Classes.html

  @author Brian Cowdery
  @since  06-10-2010
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head><title>Spring Security POC</title></head>
  <body>
    <p>
      <sec:ifLoggedIn>
        Welcome Back <sec:loggedInUserInfo field="username"/>! <%-- or <sec:username/> --%>
      </sec:ifLoggedIn>
    </p>

    <p>
      <%-- tags to check security using an expression --%>
      <%-- role names are pulled from the database, allowing user-defined roles --%>

      <sec:access expression="hasRole('ROLE_SUPER_USER')">
        Your logged in as a
        <sec:access expression="authentication.name == 'admin'">
          system admin
        </sec:access>
        super user!
      </sec:access>
      <sec:access expression="hasRole('ROLE_INTERNAL')">
        Your logged in as an internal user.
      </sec:access>
      <sec:access expression="hasRole('ROLE_CLERK')">
        Your logged in as a clerk.
      </sec:access>
      <sec:access expression="hasRole('ROLE_PARTNER')">
        Your logged in as a partner.
      </sec:access>
      <sec:access expression="hasRole('ROLE_CUSTOMER')">
        Your logged in as a clerk.
      </sec:access>

      <%--
        You can also check to see if a user has access to a specific URL. This is useful
        for checking access to specific sets of pages without needing to know all of the
        permissions involved.

        <sec:access url="/admin/user">
          <g:link controller="admin" action="user">Manager users</g:link>
        </sec:access>
      --%>
    </p>

    <p>
      <%-- tags that check if access been granted for a specific role or permission --%>
      <sec:ifAllGranted roles="ROLE_SUPER_USER, WEB_SERVICES_120">
        Super user with access to web-services.
      </sec:ifAllGranted>

      <sec:ifAllGranted  roles="ROLE_SUPER_USER, MADE_UP_PERMISSION_999, ANOTHER_FAKE_999">
        <%-- user DOES NOT have these permissions (because they don't exist), this text should never show! --%>
        You have access to some fake permissions! Somethings broken!
      </sec:ifAllGranted>

      <sec:ifAnyGranted roles="USER_CREATION_10, USER_CREATION_11, USER_CREATION_115, USER_CREATION_7, USER_CREATION_9">
        User has access to some user creation tools.
      </sec:ifAnyGranted>

      <%--
        There are also inverted security checks available to see if a user does NOT
        have access granted to a set of roles or permissions.
      --%>
      <sec:ifNotGranted roles="ROLE_SUPER_USER, ROLE_INTERNAL, ROLE_CLERK">
        You shouldn't be here!
      </sec:ifNotGranted>
    </p>

  </body>
</html>