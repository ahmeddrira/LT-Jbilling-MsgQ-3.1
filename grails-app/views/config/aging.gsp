<html>
<head>
     <meta name="layout" content="configuration" />
     
     <script type="text/javascript">
        var selected;

        // todo: should be attached to the ajax "success" event.
        // row should only be highlighted when it really is selected.
        $(document).ready(function() {
            $('.table-box li').bind('click', function() {
                if (selected) selected.attr("class", "");
                selected = $(this);
                selected.attr("class", "active");
            })
        });
    </script>
    
</head>

<body>

<!-- selected configuration menu item -->
<content tag="menu.item">aging</content>

<content tag="column1">
    <g:render template="aging/form" model="[ageingSteps:ageingSteps,gracePeriod:gracePeriod]"/>
</content>

</body>
</html>