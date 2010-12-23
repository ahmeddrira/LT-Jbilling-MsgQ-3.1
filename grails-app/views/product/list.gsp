<html>
<head>
    <meta name="layout" content="panels" />
</head>
<body>

<content tag="column1">
    <g:render template="categories" model="['categories': categories]"/>
</content>

<content tag="column2">
    <g:render template="products" model="['products': products]"/>
</content>

<content tag="column3">
    <g:if test="${selectedProduct}">
        <g:render template="show" model="['selectedProduct': selectedProduct]"/>

        <!-- display third panel -->
        <script type="text/javascript">
            $(function() { third.animate(); });
        </script>
    </g:if>
</content>

</body>
</html>