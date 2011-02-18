<html>
<head>
    <meta name="layout" content="panels" />
</head>
<body>

<content tag="filters">
</content>

<content tag="column1">
    <g:render template="list" model="[processes: processes, filters:filters]"/>
</content>

<content tag="column2">
    <g:render template="show" model="[map:map, processId: processId]"/> 
</content>

</body>
</html>