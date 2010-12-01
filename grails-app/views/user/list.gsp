<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>
<html>
<head>
    <meta name="layout" content="panels" />

    <script type="text/javascript">
        var selected;

        // todo: should be attached to the ajax "success" event.
        // row should only be highlighted when it really is selected.
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

<content tag="filters">
    <g:formRemote id="filters-form" name="filters-form" url="[action: list]" onSuccess="render(data, first);">
        <g:hiddenField name="applyFilter" value="true"/> 

        <ul class="accordion">
            <g:each var="filter" in="${filters}">
                <li>
                    <g:render template="/filter/${filter.template}" model="[filter: filter]"/>
                </li>
            </g:each>            
        </ul>

        <div class="btn-hold">
            <a href="#" class="submit apply" onclick="$('#filters-form').submit();"><span><g:message code="filters.apply.button"/></span></a>
            <a href="#" class="submit add"><span><g:message code="filters.add.button"/></span></a>            
            <a href="#" class="submit2 save"><span><g:message code="filters.save.button"/></span></a>
            <a href="#" class="submit2 load"><span><g:message code="filters.load.button"/></span></a>
        </div>
    </g:formRemote>
</content>

<content tag="column1">
    <g:render template="table" model="['users': users]"/> 
</content>

<content tag="column2">
    <g:if test="${selected}">
        <!-- show selected user details -->
        <g:render template="details" model="['selected': selected]"/>
    </g:if>
    <g:else>
        <!-- no user selected -->
        <div class="heading"><strong><em><g:message code="customer.detail.not.selected.title"/></em></strong></div>
        <div class="box"><em><g:message code="customer.detail.not.selected.message"/></em></div>
        <div class="btn-box"></div>
    </g:else>
</content>

</body>
</html>