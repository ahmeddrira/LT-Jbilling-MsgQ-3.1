
function handleBreadcrumb() {
    var breadcrumb = $(this).attr('breadcrumb');
    var params = parseUrl($(this).attr('href'));

    if (breadcrumb == "controller") {
        addBreadcrumb({ c: params.controller });

    } else if (breadcrumb == "action") {
        addBreadcrumb({ c: params.controller, a: params.action });

    } else if (breadcrumb == "id") {
        addBreadcrumb({ c: params.controller, a: params.action, id: params.id });

    } else {
        try {
            var data = eval('(' + breadcrumb + ')');
            addBreadcrumb({
                c: data.controller ? data.controller : params.controller,
                a: data.action ? data.action : params.action,
                id: data.id ? data.id : params.id,
                name: data.name ? data.name : ''
            });
        } catch (Exception) {
            addBreadcrumb({ c: params.controller, a: params.action, id: params.id, name: breadcrumb });
        }
    }
}

function parseUrl(url) {
    var tokens = url.match(/\/(\w+)*/g);
    $.each(tokens, function(index, value) { tokens[index] = value.replace('/', ''); });
    return { controller: tokens[1], action: tokens[2], id: tokens[3] };
}

function addBreadcrumb(data) {
    $.ajax({
        url: '/jbilling/breadcrumb',
        async: false,
        global: false,
        data: data,
        success: function(data) { $('#breadcrumbs').replaceWith(data); }
    });
}

$(document).ready(function() {
    $('body').delegate('a[breadcrumb]', 'click', handleBreadcrumb);
});