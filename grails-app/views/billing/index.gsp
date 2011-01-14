<%--
	@author Vikas Bodani
	@since 13 Jan 2011
 --%>

<html>
<head>
	<meta name="layout" content="panels"/>
</head>

<body>

<content tag="filters"></content>

<content tag="column1">
    <g:render template="list" model="[lstBillingProcesses: lstBillingProcesses, dataHashMap:dataHashMap]"/>
</content>

</body>
</html>