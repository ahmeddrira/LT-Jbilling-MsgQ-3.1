function singleSelectCheckbox(checkbox) {
	$(checkbox).addClass('checked-checkbox');
	var aGroup = $(checkbox).parents('div.form-hold');
	var allInputs = aGroup.find('input.check');
	allInputs.each(function () {
		if ($(this).hasClass('checked-checkbox')) {
			$(this).removeClass('checked-checkbox');
		} else {
			$(this).attr('checked', false);
			$(this).parent().find('.checkboxAreaChecked').attr('class', 'checkboxArea');
		}
	});
}