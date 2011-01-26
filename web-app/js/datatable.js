
/**
 * Fixes scrollable table <tbody> height so that overflow can scroll. Tables that do
 * not need scrolling will not have their height adjusted.
 */
function setTableBodyHeight() {
    $('.table-box table tbody').each(function() {
        if ($(this).height() > 600)
            $(this).height(600)
    });
}

$(document).ready(function() {
    /*
        Highlight clicked rows in table lists and store the selected row id.
     */
    $('body').delegate('.table-box tr', 'click', function() {
        var box = $(this).parents('.table-box')[0];
        $(box).find('tr.active').removeClass('active');
        $(this).addClass('active');
    });

    /*
        Fix table body height so that overflow can scroll
     */
    $(document).ajaxSuccess(function() {
        setTableBodyHeight();
    });
    setTableBodyHeight();
});