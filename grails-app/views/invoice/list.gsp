<html>
<head>
    <meta name="layout" content="panels" />
</head>
<body>

<content tag="column1">
    <g:render template="invoices" model="[invoices: invoices]"/>
</content>

<content tag="column2">
	<g:if test="${selected}">
        <g:render template="show" model="[selected: selected]"/>
    </g:if>
</content>

</body>
</html>