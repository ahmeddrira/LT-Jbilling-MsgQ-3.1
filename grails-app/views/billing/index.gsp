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

<%--
	<g:render template="/layouts/includes/filters"/>
	<g:render template="list" model="[lstBillingProcesses: lstBillingProcesses, dataHashMap:dataHashMap]"/> 
 --%>

</body>
</html>