<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>
<html>
<head>
    <meta name="layout" content="panels" />
</head>
<body>

<content tag="column1">
    <g:render template="plans" model="['plans': plans]"/>
</content>

<content tag="column2">
    <g:if test="${selected}">
        <!-- show selected payment details -->
        <g:render template="show" model="['plan': selected]"/>
    </g:if>
    <g:else>
        <!-- show empty block -->
        <div class="heading"><strong><em><g:message code="plan.not.selected.title"/></em></strong></div>
        <div class="box"><em><g:message code="plan.not.selected.message"/></em></div>
        <div class="btn-box"></div>
    </g:else>
</content>

</body>
</html>