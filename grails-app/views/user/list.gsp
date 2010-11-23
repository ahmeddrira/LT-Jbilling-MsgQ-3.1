<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>
<html>
<head>
    <meta name="layout" content="list" />

    <script type="text/javascript">
        $(document).ready(function() {
            $('.table-box li a').bind('click', function() {
                $.ajax({
                    url: "select/" + $(this).attr("id"),
                    success: function(data) {
                        var column = $(".columns-holder .column").last();
                        column.html(data);
                    }
                })
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
        <a href="#" class="submit apply"><span>Apply Filters</span></a>
        <a href="#" class="submit add"><span>Add Filter</span></a>
        <a href="#" class="submit2 save"><span>Save</span></a>
        <a href="#" class="submit2 load"><span>Load</span></a>
    </div>   
</content>

<content tag="column1">
    <div class="heading table-heading">
        <strong class="name"><g:message code="customers.table.th.name"/></strong>
        <strong class="parent"><g:message code="customers.table.th.hierarchy"/></strong>
        <strong class="owed"><g:message code="customers.table.th.balance"/></strong>
        <strong class="status"><g:message code="customers.table.th.status"/></strong>
        <strong class="user"><g:message code="customers.table.th.user.id"/></strong>
    </div>

    <div class="table-box">
        <ul>
            <g:each in="${users}" var="user">
                <g:set var="customer" value="${user.customer}"/>
                <g:set var="contact" value="${ContactDTO.findByUserId(user.id)}"/>
                
                <li>
                    <a href="#user-${user.id}" id="${user.id}"
                        <g:if test="${selected && selected.id == user.id}">class="active"</g:if>>
                        <span class="block last">
                            <g:if test="${customer}">
                                <g:if test="${customer.isParent == 1 && customer.parent}">
                                    <%-- is a parent, but also a child of another account --%>
                                    <img src="${resource(dir:'images', file:'icon17.gif')}" alt="parent and child" />
                                    <span>${customer.children.size()}</span>
                                </g:if>
                                <g:elseif test="${customer.isParent == 1 && !customer.parent}">
                                    <%-- is a top level parent --%>
                                    <img src="${resource(dir:'images', file:'icon18.gif')}" alt="parent" />
                                    <span>${customer.children.size()}</span>
                                </g:elseif>
                                <g:elseif test="${customer.isParent == 0 && customer.parent}">
                                    <%-- is a child account, but not a parent --%>
                                    <img src="${resource(dir:'images', file:'icon19.gif')}" alt="child" />                                    
                                </g:elseif>
                                <g:else>
                                    <span>&nbsp;</span>
                                </g:else>
                            </g:if>
                            <g:else>
                                <span>&nbsp;</span>
                            </g:else>
                        </span>
                        <span class="block">
                            <span>999.99</span>
                        </span>
                        <span class="block">
                            <span>
                                <g:if test="${user.userStatus.id > 1 && user.userStatus.id < 5}">
                                    <img src="${resource(dir:'images', file:'icon15.gif')}" alt="overdue" />
                                </g:if>
                                <g:elseif test="${user.userStatus.id >= 5}">
                                    <img src="${resource(dir:'images', file:'icon16.gif')}" alt="suspended" />
                                </g:elseif>
                                <g:else>
                                    <span>&nbsp;</span>
                                </g:else>
                            </span>
                        </span>
                        <span class="block">
                            <span>${user.id}</span>
                        </span>
                        <strong>
                            <g:if test="${contact && (contact.firstName || contact.lastName)}">
                                ${contact.firstName} ${contact.lastName}
                            </g:if>
                            <g:else>
                                ${user.userName}
                            </g:else>
                        </strong>
                        <em>
                            <g:if test="${contact}">${contact.organizationName}</g:if>
                        </em>
                    </a>
                </li>
            </g:each>
        </ul>
    </div>

    <div class="btn-box">
        <a href="${createLink(action: 'create')}" class="submit add"><span>Add New</span></a>                
        <a href="${createLink(action: 'delete')}" class="submit delete"><span>Delete Selected</span></a>
    </div>
</content>

<content tag="column2">
    <g:if test="${selected}">
        <g:render template="details" model="['selected': selected]"/>
    </g:if>
    <g:else>
        <!-- no user selected -->
        <div class="heading"><strong><em>No user selected.</em></strong></div>
        <div class="box"><em>Please select a user to view.</em></div>

        <div class="heading"><strong>User Info</strong></div>
        <div class="heading"><strong>Payment Info</strong></div>
        <div class="heading"><strong>Contact Info</strong></div>
    </g:else>
</content>

</body>
</html>