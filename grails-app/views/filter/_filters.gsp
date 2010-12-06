
<%--
  Filter side panel template. Prints all filters contained in the "filters" page variable.

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
                                <g:remoteLink controller="filter" action="add" id="${filter.name}" update="filters">
                                    <g:message code="filters.${filter.field}.title"/>
                                </g:remoteLink>
                            </li>
                        </g:if>
                    </g:each>
                </ul>
            </div>
        </div>
    </g:if>

    <g:link class="submit2 save" controller="filter" action="edit" params="[fromAction: actionName, fromController: controllerName]">
        <span><g:message code="filters.save.button"/></span>
    </g:link>
    
    <a href="#" class="submit2 load"><span><g:message code="filters.load.button"/></span></a>
</div>

<script type="text/javascript">
    /*
        Need to call JS methods to apply theme to AJAX rendered input forms.    
        Todo: see Requirement #641 - remove JavaScript input field styling
     */
    initCustomForms();
    initPopups();
    initScript();    
</script>