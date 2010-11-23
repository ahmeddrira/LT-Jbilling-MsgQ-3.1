<%--
  _breadcrumbs

  @author Brian Cowdery
  @since  23-11-2010
--%>

<!-- breadcrumbs -->
<div class="breadcrumbs">
    <ul>
        <%-- todo: add the grails breadcrumbs plugin for easy breadcrumb trails - http://www.grails.org/plugin/breadcrumbs --%>
        <%-- quick and dirty breadcrumbs --%>
        <li><a href="${resource(dir:'')}"><g:message code="breadcrumb.link.home"/></a></li>
        <g:if test="${controllerName != null}">
            <li><g:link controller="${controllerName}"><g:message code="breadcumb.link.${controllerName}"/></g:link></li>
        </g:if>
        <g:if test="${actionName != null && actionName != 'index'}">
            <li><g:link controller="${controllerName}"><g:message code="breadcumb.link.${controllerName}.${actionName}"/></g:link></li>
        </g:if>
    </ul>
</div>