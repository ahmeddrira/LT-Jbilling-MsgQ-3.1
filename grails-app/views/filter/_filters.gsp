
<%--
  _filters

  @author Brian Cowdery
  @since  03-12-2010
--%>

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
    <a href="#" class="submit apply" onclick="applyFilters()"><span><g:message code="filters.apply.button"/></span></a>

    <g:if test="${filters.find { !it.visible }}">
        <div class="dropdown">
            <a href="#" class="submit add open"><span><g:message code="filters.add.button"/></span></a>
            <div class="drop">
                <ul>
                    <g:each var="filter" in="${filters}">
                        <g:if test="${!filter.visible}">
                            <li>
                                <g:remoteLink controller="filter" action="add" id="${filter.id}" update="filters">
                                    ${filter.field}
                                </g:remoteLink>
                            </li>
                        </g:if>
                    </g:each>
                </ul>
            </div>
        </div>
    </g:if>

    <a href="_filters.gsp#" class="submit2 save"><span><g:message code="filters.save.button"/></span></a>
    <a href="_filters.gsp#" class="submit2 load"><span><g:message code="filters.load.button"/></span></a>
</div>

<script type="text/javascript">
    /*
        Todo: see R
     */
    initCustomForms();
    initPopups();
    initScript();    
</script>