<html>
<head>
<meta name="layout" content="panels" />
</head>

<body>
<content tag="filters">
</content>

<content tag="column1">
    <g:render template="lists" model="['invoices': invoices"/> 
</content>

<content tag="column2">
	<g:if test="${invoice}">
        <g:render template="show" model="['invoice': invoice]"/>
        <!-- display third panel -->
        <script type="text/javascript">
            $(function() { third.animate(); });
        </script>
    </g:if>
</content>
</body>
</html>