<%--
  _created

  @author Brian Cowdery
  @since  30-11-2010
--%>

<div id="${filter.name}">
    <span class="title <g:if test='${filter.value}'>active</g:if>"><g:message code="filters.${filter.field}.title"/></span>
    <g:remoteLink class="delete" controller="filter" action="remove" id="${filter.name}" update="filters"/>
    
    <div class="slide">
        <fieldset>
            <div class="input-row">
                <div class="input-bg">
                    <a href="#" onclick="$('#created-from-date').datepicker('show')"></a>
                    <g:textField id="created-from-date" name="filters.${filter.name}.startDateValue" value="${filter.startDateValue}"/>
                </div>
                <label for="filters.${filter.name}.startDateValue"><g:message code="filters.date.from.label"/></label>
            </div>

            <div class="input-row">
                <div class="input-bg">
                    <a href="#" onclick="$('#created-to-date').datepicker('show')"></a>
                    <g:textField id="created-to-date" name="filters.${filter.name}.endDateValue" value="${filter.endDateValue}"/>
                </div>
                <label for="filters.${filter.name}.endDateValue"><g:message code="filters.date.to.label"/></label>
            </div>
        </fieldset>

        <script type="text/javascript">
            $(function() {
                $("#created-from-date").datepicker({dateFormat: 'dd/mm/yy'});
                $("#created-to-date").datepicker({dateFormat: 'dd/mm/yy'});
            });
        </script>
    </div>
</div>