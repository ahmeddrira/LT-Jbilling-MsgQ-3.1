<%@ page contentType="text/html;charset=UTF-8" %>

<%--
  Order builder view.

  This view doubles as a way to render partial page templates by setting the 'template' parameter. This
  is used as a workaround for rendering AJAX responses from within the web-flow.

  @author Brian Cowdery
  @since 25-Jan-2011
--%>

<g:if test="${params.template}">
    <g:render template="${params.template}"/>
</g:if>

<g:else>
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
</g:else>