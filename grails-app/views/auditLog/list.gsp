<html>
<head>
    <meta name="layout" content="panels" />
</head>
<body>

<content tag="column1">
    <g:render template="logs" model="['logs': logs]"/>
</content>

<content tag="column2">
    <g:if test="${selected}">
        <!-- show selected log details -->
        <g:render template="show" model="['selected': selected]"/>
    </g:if>
    <g:else>
        <!-- show empty block -->
        <div class="heading"><strong><em><g:message code="log.not.selected.title"/></em></strong></div>
        <div class="box"><em><g:message code="log.not.selected.message"/></em></div>
        <div class="btn-box"></div>
    </g:else>
</content>

</body>
</html>