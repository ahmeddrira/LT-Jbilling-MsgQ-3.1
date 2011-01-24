<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="builder"/>

    <script type="text/javascript">
        $(document).ready(function() {
            $('#builder-tabs').tabs();
        });
    </script>
</head>
<body>
    <content tag="builder">
        <div id="builder-tabs">
            <ul>
                <li><a href="${createLink(action: 'edit', event: 'details')}">Details</a></li>
                <li><a href="${createLink(action: 'edit', event: 'products')}">Products</a></li>
            </ul>
        </div>
    </content>

    <content tag="review">
        <g:render template="review"/>
    </content>
</body>
</html>