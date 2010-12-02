$(document).ready(function() {
    $('.dropdown').each(function() {
        var open = $(this).find('a.open');
        var drop = $(this).find('.drop');

        // position drop menu in line with the "open" link
        drop.css('top', open.position().top + open.outerHeight())
            .css('left', open.position().left);
    });
});
