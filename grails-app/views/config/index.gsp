<html>
<head>
    <meta name="layout" content="configuration" />
</head>
<body>

<!-- selected configuration menu item -->
<content tag="menu.item">all</content>

<content tag="column1">
    <g:render template="all" />
</content>

<content tag="column2">
    <g:if test="${selected}">
        <!-- show selected preference type -->
        <g:render template="show" model="[selected: selected]"/>
    </g:if>
</content>

</body>
</html>