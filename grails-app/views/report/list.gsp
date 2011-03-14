<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>
<html>
<head>
    <meta name="layout" content="panels" />
</head>
<body>

<g:if test="${!selected}">
    <!-- show report types and reports -->
    <content tag="column1">
        <g:render template="types" model="[types: types]"/>
    </content>

    <content tag="column2">
        <g:render template="reports" model="[reports: reports, selectedTypeId: selectedTypeId ]"/>
    </content>
</g:if>
<g:else>
    <!-- show reports list and selected report -->
    <content tag="column1">
        <g:render template="reports" model="[reports: reports, selectedTypeId: selectedTypeId ]"/>
    </content>

    <content tag="column2">
        <g:render template="show" model="[selected: selected]"/>
    </content>
</g:else>

</body>
</html>