$(document).ready(function(){
	 initCustomForms();
    // Slide effect
    var _parentSlide = 'div.box-cards';
    var _linkSlide = 'a.btn-open';
    var _slideBlock = 'div.box-card-hold';
    var _openClassS = 'box-cards-open';
    var _durationSlide = 500;
    
    $(_parentSlide).each(function(){
	if (!$(this).is('.'+_openClassS)) {
	    $(this).find(_slideBlock).css('display','none');
	}
    });
    $(_linkSlide,_parentSlide).click(function(){
	if ($(this).parents(_parentSlide).is('.'+_openClassS)) {
	    $(this).parents(_parentSlide).removeClass(_openClassS);
	    $(this).parents(_parentSlide).find(_slideBlock).slideUp(_durationSlide);
	    //$(this).text(_textOpenS);
	} else {
	    $(this).parents(_parentSlide).addClass(_openClassS);
	    $(this).parents(_parentSlide).find(_slideBlock).slideDown(_durationSlide);
	    //$(this).text(_textCloseS);
	}
	
	return false;
	
    });

});
