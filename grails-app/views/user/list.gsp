<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>
<html>
<head>
    <meta name="layout" content="panels" />

    <script type="text/javascript">
        var selected;
        
        $(document).ready(function() {
            $('.table-box li').bind('click', function() {
                if (selected) selected.attr("class", "");
                selected = $(this);
                selected.attr("class", "active");
            })
        });
    </script>
</head>
<body>

<content tag="column1">
    <g:render template="table" model="['users': users]"/> 
</content>

<content tag="column2">
    <g:if test="${selected}">
        <!-- show selected user details -->
        <g:render template="details" model="['selected': selected]"/>
    </g:if>
    <g:else>
        <!-- show empty block -->
        <div class="heading"><strong><em><g:message code="customer.detail.not.selected.title"/></em></strong></div>
        <div class="box"><em><g:message code="customer.detail.not.selected.message"/></em></div>
        <div class="btn-box"></div>
    </g:else>
</content>

</body>
</html>