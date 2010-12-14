<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <g:render template="/layouts/includes/head"/>
    <g:javascript library="panels"/>

    <script type="text/javascript">
        $(document).ajaxSuccess(function(e, xhr, settings) {
            $.ajax({
                url: "${resource(dir:'')}/messages",
                global: false,
                success: function(data) { $("#messages").replaceWith(data); }
            });

            $.ajax({
                url: "${resource(dir:'')}/recentItem",
                global: false,
                success: function(data) { $("#recent-items").replaceWith(data) }
            });
        });

        function applyFilters() {
            $('#filters-form input:visible, #filters-form select:visible').each(function() {
                var title = $(this).parents('li').find('.title');
                if ($(this).val()) {
                    title.addClass('active');
                } else {
                    title.removeClass('active');
                }
            });

            $('#filters-form').submit();
        }
    </script>

    <g:layoutHead/>
</head>
<body>
<div id="wrapper">
    <g:render template="/layouts/includes/header"/>

    <div id="main">
        <g:render template="/layouts/includes/breadcrumbs"/>

        <div id="left-column">
            <!-- filters -->
            <g:formRemote id="filters-form" name="filters-form" url="[action: list]" onSuccess="render(data, first);">
                <g:hiddenField name="applyFilter" value="true"/>
                <g:render template="/layouts/includes/filters"/>
            </g:formRemote>
            <g:render template="/layouts/includes/filterSaveDialog"/>

            <!-- shortcuts -->
            <g:render template="/layouts/includes/shortcuts"/>

            <!-- recently viewed items -->
            <g:render template="/layouts/includes/recent"/>
        </div>


        <!-- content columns -->
        <div class="columns-holder">
            <g:render template="/layouts/includes/messages"/>

            <div id="viewport">
                <div class="column panel" index="1">
                    <div class="column-hold">
                        <g:pageProperty name="page.column1"/>
                    </div>
                </div>

                <div class="column panel" index="2">
                    <div class="column-hold">
                        <g:pageProperty name="page.column2"/>
                    </div>
                </div>

                <div class="column panel" index="3">
                    <div class="column-hold">
                        <g:pageProperty name="page.column3"/>
                    </div>
                </div>

                <div class="column panel" index="4">
                    <div class="column-hold">
                        <g:pageProperty name="page.column4"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
