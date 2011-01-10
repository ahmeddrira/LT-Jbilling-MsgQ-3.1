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
            $("${jquerySelector}").datepicker({
                dateFormat: "${message(code: 'datepicker.jquery.ui.format')}",
                showOn: "both",
                buttonImage: "${resource(dir:'images', file:'icon04.gif')}",
                buttonImageOnly: true
            });
        });
    </script>
</div>

