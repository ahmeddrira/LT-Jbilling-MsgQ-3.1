<%@ page import="jbilling.FilterSet" %>

<%--
  Filter side panel template. Prints all filters contained in the "filters" page variable.

  @author Brian Cowdery
  @since  03-12-2010
--%>

<div id="filters">
    <div class="heading">
        <strong><g:message code="filters.title"/></strong>
    </div>

    <ul class="accordion">
        <g:each var="filter" in="${filters}">
            <g:if test="${filter.visible}">
                <li>
                    <g:render template="/filter/${filter.template}" model="[filter: filter]"/>
                </li>
            </g:if>
        </g:each>
    </ul>

    <div class="btn-hold">
        <!-- apply filters -->
        <a class="submit apply" onclick="applyFilters()"><span><g:message code="filters.apply.button"/></span></a>

        <!-- add another filter -->
        <g:if test="${filters.find { !it.visible }}">
            <div class="dropdown">
                <a class="submit add open"><span><g:message code="filters.add.button"/></span></a>
                <div class="drop">
                    <ul>
                        <g:each var="filter" in="${filters}">
                            <g:if test="${!filter.visible}">
                                <li>
                                    <g:remoteLink controller="filter" action="add" params="[name: filter.name]" update="filters">
                                        <g:message code="filters.${filter.field}.title"/>
                                    </g:remoteLink>
                                </li>
                            </g:if>
                        </g:each>
                    </ul>
                </div>
            </div>
        </g:if>

        <!-- save current filter set-->
        <a class="submit2 save" onclick="$('#filter-save-dialog').dialog('open');">
            <span><g:message code="filters.save.button"/></span>
        </a>

        <!-- load saved filter set -->
        <div class="dropdown">
            <a class="submit2 load open"><span><g:message code="filters.load.button"/></span></a>
            <div class="drop">
                <ul>
                    <g:each var="filterset" in="${FilterSet.findAllByUserId((Integer) session['user_id'])}">
                        <li>
                            <g:remoteLink controller="filter" action="load" id="${filterset.id}" update="filters">
                                ${filterset.name}
                            </g:remoteLink>
                        </li>
                    </g:each>
                </ul>
            </div>
        </div>
    </div>
</div>