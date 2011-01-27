<html>
<head>
    <meta name="layout" content="configuration" />
</head>
<body>

<!-- selected configuration menu item -->
<content tag="menu.item">contactType</content>

<content tag="column1">
    <g:render template="contactTypes" />
</content>

<content tag="column2">
    <g:if test="${selected}">
        <!-- show selected contact type -->
        <g:render template="edit" model="['selected': selected]"/>
    </g:if>
</content>

</body>
</html>