<%@ page import="com.sapienter.jbilling.server.util.db.JbillingTableDAS" %>

<%--
  @author Emiliano Conde - Hong Kong Airoport :)
  @since  24-02-2011
--%>

<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.${filter.field}.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" params="[name: filter.name]" update="filters"/>
    
    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="select-bg">
                    <g:select name="filters.${filter.name}.stringValue"
                            value="${filter.stringValue}"
                            from="${new JbillingTableDAS().findAll().sort{it.name}}"
                            optionKey="name" 
                            optionValue="name"
                            noSelection="['': '']" />

                </div>
                <label for="filters.${filter.name}.stringValue"><g:message code="filters.table.label"/></label>
            </div>
        </fieldset>
    </div>
</div>

