<%--
	@author Vikas Bodani
	@since 17 Feb 2011
 --%>

<html>
<head>
	<meta name="layout" content="panels"/>
</head>

<body>

<content tag="filters"></content>

<content tag="column1">
    <g:render template="list" model="[processes: processes, filters:filters]"/>
</content>

</body>
</html>