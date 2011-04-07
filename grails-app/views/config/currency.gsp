<html>
<head>
     <meta name="layout" content="configuration" />
</head>

<body>

<!-- selected configuration menu item -->
<content tag="menu.item">currency</content>

<content tag="column1">
    <g:render template="currency/form" />
</content>

<content tag="column2">
    <g:if test="${currency}">
        <g:render template="currency/edit" model="[currency: currency]"/>
    </g:if>
</content>

</body>
</html>