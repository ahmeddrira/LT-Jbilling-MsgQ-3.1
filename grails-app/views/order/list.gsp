<html>
<head>
<meta name="layout" content="panels" />
</head>

<body>
<content tag="filters">
</content>

<content tag="column1">
    <g:render template="orders" model="['orders': orders"/> 
</content>

<content tag="column2">
    <g:if test="${order}">
        <g:render template="show" model="['order': order]"/>
        <!-- display third panel -->
        <script type="text/javascript">
            $(function() { third.animate(); });
        </script>
    </g:if>
</content>
</body>
</html>