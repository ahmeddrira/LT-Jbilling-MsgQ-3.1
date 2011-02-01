
$(document).ready(function() {
    /*
        Highlight clicked rows in table lists and store the selected row id.
     */
    $('body').delegate('.table-box tr', 'click', function() {
        var box = $(this).parents('.table-box')[0];
        $(box).find('tr.active').removeClass('active');
        $(this).addClass('active');
    });
});