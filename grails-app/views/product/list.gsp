<html>
<head>
    <meta name="layout" content="panels" />
</head>
<body>

<g:if test="${!selectedProduct}">
    <!-- show product categories and products -->
    <content tag="column1">
        <g:render template="categories" model="[categories: categories]"/>
    </content>

    <content tag="column2">
        <g:render template="products" model="[products: products]"/>
    </content>
</g:if>
<g:else>
    <!-- show product list and selected product -->
    <content tag="column1">
        <g:render template="products" model="[products: products]"/>
    </content>

    <content tag="column2">
        <g:render template="show" model="[selectedProduct: selectedProduct]"/>
    </content>
</g:else>

</body>
</html>