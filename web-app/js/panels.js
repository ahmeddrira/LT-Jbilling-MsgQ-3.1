var width;
var clicked;

var next = {
    index: function() {
        return clicked + 1;
    },
    visible: function() {
        var position = $('.columns-holder [index=' + this.index() + ']').position().left;
        return position > 0 && position < $('.columns-holder').width();
    },
    animate: function() {
        var columns = $('.columns-holder .column');
        width = columns.first().width();

        columns.each(function() {
            $(this).animate({
                left: '-=' + width
            });
        });        
    }
};

var prev = {
    index: function() {
        return clicked - 1;
    },
    visible: function() {       
        var position = $('.columns-holder [index=' + this.index() + ']').position().left;
        return position > 0 && position < $('.columns-holder').width();
    },
    animate: function() {
        var columns = $('.columns-holder .column');
        width = columns.first().width();

        columns.each(function() {
            $(this).animate({
                left: '+=' + width
            });
        });
    }
};

/**
 * Registers a click on an AJAX link so that the source column is known to the renderer.
 *
 * @param element source element
 */
function register(element) {
    clicked = parseInt($(element).parents('.column').first().attr('index'));
}

/**
 * Renders the AJAX return value in the target column (next or prev). If the target
 * column is not visible the view will be changed to show the target column.
 *
 * The target column is given as a simple object that can calculate the next column
 * position and animate the transition to show the column (if necessary). Currently
 * supported targets are next and prev.
 *
 * E.g.,
 *      render(data, next);
 *      render(data, prev);
 *
 * @param data data to render in target column
 * @param target column function. next or prev.
 */
function render(data, target) {
    $('.columns-holder [index= ' + target.index() + '] .column-hold').html(data);
    if (!target.visible()) target.animate();
}

/**
 * Resets the view back it's original position.
 */
function reset() {
    $('.columns-holder .column').css('left','');
    width = null;
}

$(window).resize(function() {
    if (width != null) {
        var columns = $('.columns-holder .column');
        var delta = columns.first().width() - width;

        columns.css('left', function(index, val){
            return parseInt(val) - delta + 'px';
        });
        
        width = columns.first().width();
    }
});


