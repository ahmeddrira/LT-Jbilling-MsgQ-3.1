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
            var options = $.datepicker.regional['${session.locale.language}'];
            if (options == null) options = $.datepicker.regional[''];

            options.dateFormat = "${message(code: 'datepicker.jquery.ui.format')}";
            options.showOn = "both";
            options.buttonImage = "${resource(dir:'images', file:'icon04.gif')}";
            options.buttonImageOnly = true;

            $("${jquerySelector}").datepicker(options);
        });
    </script>
</div>

