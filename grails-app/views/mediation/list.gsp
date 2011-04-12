<%--
	@author Vikas Bodani
	@since 17 Feb 2011
 --%>

<html>
<head>
	<meta name="layout" content="panels"/>
</head>
<body>

    <content tag="column1">
        <g:render template="processes" model="[processes: processes, filters:filters]"/>
    </content>

    <content tag="column2">
        <!-- show selected process if set -->
        <g:if test="${selected}">
            <g:render template="show" model="[process: process]"/>
        </g:if>
    </content>

</body>
</html>