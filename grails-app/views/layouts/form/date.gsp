<%--
  Text field with a date selector.

  @author Brian Cowdery
  @since  06-Jan-2011
--%>

<g:set var="jquerySelector" value="#${pageProperty(name: 'page.label.for').replaceAll('\\.','\\\\\\\\\\.')}"/>

<div class="row">
    <label for="<g:pageProperty name="page.label.for"/>"><g:pageProperty name="page.label"/></label>
    <div class="inp-bg date">
        <g:layoutBody/>
    </div>

    <script type="text/javascript">
        $(function() {
            $("${jquerySelector}").datepicker($.datepicker.regional['${session.locale.language}']);
            $("${jquerySelector}").datepicker("option", "dateFormat", "${message(code: 'datepicker.jquery.ui.format')}");
            $("${jquerySelector}").datepicker("option", "showOn", "both");
            $("${jquerySelector}").datepicker("option", "buttonImage", "${resource(dir:'images', file:'icon04.gif')}");
            $("${jquerySelector}").datepicker("option", "buttonImageOnly", true);
        });
    </script>
</div>

