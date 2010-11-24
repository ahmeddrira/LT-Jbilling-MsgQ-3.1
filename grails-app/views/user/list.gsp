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
    <ul class="accordion">
        <li><a href="#">User ID</a></li>
        <li><a href="#">Status</a></li>
        <li class="other">
            <a href="#" class="delete"></a>
            <a href="#"><strong>Login Name</strong></a>
        </li>
        <li class="open other">
            <a href="#" class="delete"></a>
            <a href="#"><strong>Created Date</strong></a>
            <div class="slide">
                <form action="#">
                    <fieldset>
                        <div class="input-row">
                            <div class="input-bg">
                                <a href="#"></a>
                                <input type="text" class="text" value="05/15/2010" id="item01" />
                            </div>
                            <label for="item01">From</label>
                        </div>
                        <div class="input-row">
                            <div class="input-bg">
                                <a href="#"></a>
                                <input type="text" class="text select" value="Select" id="item02" />
                            </div>
                            <label for="item02">To</label>
                        </div>
                    </fieldset>
                </form>
            </div>
        </li>
        <li><a href="#">Email</a></li>
    </ul>
    <div class="btn-hold">
        <a href="#" class="submit apply"><span><g:message code="filters.apply.button"/></span></a>
        <a href="#" class="submit add"><span><g:message code="filters.add.button"/></span></a>
        <a href="#" class="submit2 save"><span><g:message code="filters.save.button"/></span></a>
        <a href="#" class="submit2 load"><span><g:message code="filters.load.button"/></span></a>
    </div>   
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