
<%--
  edit

  @author Brian Cowdery
  @since  06-12-2010
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main" />
</head>
<body>
    <div class="form-edit">
        <div class="heading">
            <strong>Edit Filter Set</strong>
        </div>
        <div class="form-hold">
            <g:form id="filter-save-form" url="[action: 'save']">
                <g:hiddenField name="id" value="${filterset?.id}"/>
                <g:hiddenField name="fromAction" value="${fromAction}"/>
                <g:hiddenField name="fromController" value="${fromController}"/>
                
                <fieldset>
                    <div class="form-columns">
                        <div class="column">
                            <g:applyLayout name="form/input">
                                <content tag="label">Name</content>
                                <content tag="label.for">name</content>
                                <g:textField name="name" value="${filterset?.name}" class="field"/>
                            </g:applyLayout>                            
                        </div>
                        <div class="column">
                            <ul>
                                <g:each var="filter" in="${filters}">
                                    <g:if test="${filter.visible && filter.value}">
                                        <li><g:message code="filters.${filter.field}.title"/> = <strong>${filter.value}</strong></li>
                                    </g:if>
                                </g:each>
                            </ul>
                        </div>
                    </div>

                    <div class="buttons">
                        <ul>
                            <li><a class="submit save" onclick="$('#filter-save-form').submit();" href="#"><span>Save Changes</span></a></li>
                            <li><a class="submit cancel" href="#"><span>Cancel</span></a></li>        
                        </ul>
                    </div>
                </fieldset>
            </g:form>
        </div>
    </div>
</body>
</html>