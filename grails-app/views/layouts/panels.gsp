<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <g:render template="/layouts/includes/head"/>
    <g:javascript library="panels"/>

    <script type="text/javascript">
        function renderRecentItems() {
            $.ajax({
                url: "${resource(dir:'')}/recentItem",
                global: false,
                success: function(data) { $("#recent-items").replaceWith(data) }
            });
        }

        $(document).ajaxSuccess(function(e, xhr, settings) {
            renderRecentItems();
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
            <g:if test="${filters}">
                <g:set var="target" value="${filterRender ?: 'next'}"/>
                <g:set var="action" value="${filterAction ?: 'list'}"/>
                <g:formRemote id="filters-form" name="filters-form" url="[action: action]" onSuccess="render(data, ${target});">
                    <g:hiddenField name="applyFilter" value="true"/>
                    <g:render template="/layouts/includes/filters"/>
                </g:formRemote>
                <g:render template="/layouts/includes/filterSaveDialog"/>
            </g:if>

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
                    <div id="column1" class="column-hold">
                        <g:pageProperty name="page.column1"/>
                    </div>
                </div>

                <div class="column panel" index="2">
                    <div id="column2" class="column-hold">
                        <g:pageProperty name="page.column2"/>
                    </div>
                </div>

                <div class="column panel" index="3">
                    <div id="column3" class="column-hold">
                        <g:pageProperty name="page.column3"/>
                    </div>
                </div>

                <div class="column panel" index="4">
                    <div id="column4" class="column-hold">
                        <g:pageProperty name="page.column4"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
