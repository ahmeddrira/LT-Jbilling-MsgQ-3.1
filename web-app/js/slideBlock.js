
function toggleSlide(element) {
    var parent = $(element).is('.box-cards') ? element : $(element).parents('.box-cards');

    if ($(parent).is('.box-cards-open')) {
        closeSlide(parent);
    } else {
        openSlide(parent);
    }
}

function openSlide(parent) {
    if ($(parent).not('.box-cards-open')) {
        $(parent).addClass('box-cards-open');
        $(parent).find('.box-card-hold').slideDown(500, function() {
            eval($(parent).attr('onOpen'));
            eval($(parent).attr('onSlide'));
        });
    }
}

function closeSlide(parent) {
    if ($(parent).is('.box-cards-open')) {
        $(parent).removeClass('box-cards-open');
        $(parent).find('.box-card-hold').slideUp(500, function() {
            eval($(parent).attr('onClose'));
            eval($(parent).attr('onSlide'));
        });
    }
}

$(document).ready(function(){
    // hide closed box-cards
    $('.box-cards').each(function(){
        if (!$(this).is('.box-cards-open'))
            $(this).find('.box-card-hold').css('display','none');
    });

    // toggle box-cards on click
    $('a.btn-open', '.box-cards').click(function() {
        toggleSlide(this);
        return false;
    });
});
