<html>
<head>
    <meta name="layout" content="panels" />
</head>
<body>

<content tag="column1">
    <g:render template="categories" model="['categories': categories]"/>
</content>

<content tag="column2">
    <g:if test="${products}">
        <!-- show item list for selected category -->
        <g:render template="products" model="['products': products]"/>
    </g:if>
    <g:else>
        <!-- show empty block -->
        <div class="heading"><strong><em><g:message code="product.category.not.selected.title"/></em></strong></div>
        <div class="box"><em><g:message code="product.category.not.selected.message"/></em></div>
        <div class="btn-box"></div>
    </g:else>
</content>

<content tag="column3">
    <g:if test="${selectedProduct}">
        <!-- todo: javascript magic to show the 3rd panel -->
        <g:render template="show" model="['selectedProduct': selectedProduct]"/>
    </g:if>
</content>

</body>
</html>