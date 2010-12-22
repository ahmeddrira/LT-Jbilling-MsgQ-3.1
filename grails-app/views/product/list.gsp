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
        <div class="btn-box">
            <g:remoteLink action="allProducts" update="column2" class="submit show"><span><g:message code="button.show.all"/></span></g:remoteLink>
        </div>
    </g:else>
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