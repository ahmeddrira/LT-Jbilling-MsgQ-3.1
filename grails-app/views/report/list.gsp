<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>
<html>
<head>
    <meta name="layout" content="panels" />
</head>
<body>

<content tag="column1">
    <g:render template="types" model="[types: types]"/>
</content>

<content tag="column2">
    <g:render template="reports" model="[reports: reports, selectedTypeId: selectedTypeId ]"/>
</content>

<content tag="column3">
    <g:if test="${selected}">
        <g:render template="show" model="[selected: selected]"/>

        <!-- display third panel -->
        <script type="text/javascript">
            $(function() { third.animate(); });
        </script>
    </g:if>
</content>

</body>
</html>