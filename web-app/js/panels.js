
var clicked;

var first = {
    index: function() {
        return 1;
    },
    visible: function() {
        return true;
    },
    animate: function() {
    }
};

var second = {
    index: function() {
        return 2;
    },
    visible: function() {
        return true;
    },
    animate: function() {
    }
};

var third = {
    index: function() {
        return 3;
    },
    visible: function() {
        return false;
    },
    animate: function() {
        $('#viewport .column:first-child').animate(
            { marginLeft: '-=100%' },
            1000,
            function() { $(this).remove(); }
        );
    }
};

var next = {
    index: function() {
        return clicked + 1;
    },
    visible: function() {
        return this.index() > 0 && this.index() < 3;
    },
    animate: function() {
        $('#viewport .column:first-child').animate(
            { marginLeft: '-=100%' },
            1000,
            function() { $(this).remove(); }
        );
    }
};

/**
 * Registers a click on an AJAX link so that the source column is known to the renderer.
 *
 * @param element source element
 */
function register(element) {
    var column = $(element).parents('.column');
    $('#viewport').children().each(function(index, element) {
        if ($(column).get(0) == $(element).get(0)) {
            clicked = index + 1;
        }
    });
}

/**
 * Renders the AJAX return value in the target column (next or prev). If the target
 * column is not visible the view will be changed to show the target column.
 *
 * The target column is given as a simple object that can calculate the next column
 * position and animate the transition to show the column (if necessary).
 *
 * E.g.,
 *      render(data, next);
 *      render(data, first);
 *      render(data, second); // etc
 *
 * @param data data to render in target column
 * @param target column function.
 */
function render(data, target) {
    if (target.visible()) {
        // render data in visible column
        var column = $('#viewport .column:nth-child(' + target.index() + ')');
        column.find('.column-hold').html(data);

    } else {
        // re-number columns before shifting view, prevents transversal errors
        // in embedded javascript while the browser re-calculates the DOM
        $('#viewport .column:first-child .column-hold').attr('id', '');
        $('#viewport .column:nth-child(2) .column-hold').attr('id', 'column1');

        // build a new column node and append to viewport list
        var column = $('#panel-template').clone().attr('id', '');
        column.find('.column-hold').attr('id', 'column2').html(data);
        column.show();
        $('#viewport').append(column);

        target.animate();
    }
}

/**
 * Returns the first selected element ID residing in the same column as the given element.
 *
 * E.g., getSelectedElementId(this);  #=>  "user-123"
 *
 * @param element in the same column as the selected row
 */
function getSelectedElementId(element) {
    var column = $(element).is('.column-hold') ? element : $(element).parents('.column-hold')[0];
    return $(column).find('.table-box .active').attr('id');
}

/**
 * Returns the object ID of the first selected element residing in the same column as the
 * given element.
 *
 * This method assumes that the list row contains the object's id as part of the element
 * ID. All non-digit characters are stripped and the remaining digits are returned as the
 * object id.
 *
 * E.g.,
 *      <tr id="user-123" class="active"></tr>
 *      <tr id="user-456"></tr>
 *
 *      getSelectedId(this);   #=>   "123"
 *
 * @param element in the same column as the selected row
 */
function getSelectedId(element) {
    var elementId = getSelectedElementId(element);
    return elementId ? elementId.replace(/\D+/, "") : undefined;
}


