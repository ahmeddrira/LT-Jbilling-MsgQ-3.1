<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>
<html>
<head>
    <meta name="layout" content="panels" />
</head>
<body>

<content tag="column1">
    <g:render template="payments" model="['payments': payments]"/>
</content>

<content tag="column2">
    <g:if test="${selected}">
        <!-- show selected payment details -->
        <g:render template="show" model="['selected': selected]"/>
    </g:if>
    <g:else>
        <!-- show empty block -->
        <div class="heading"><strong><em><g:message code="payment.not.selected.title"/></em></strong></div>
        <div class="box"><em><g:message code="payment.not.selected.message"/></em></div>
        <div class="btn-box"></div>
    </g:else>
</content>

</body>
</html>