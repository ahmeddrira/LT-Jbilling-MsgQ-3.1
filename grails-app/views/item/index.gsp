<html>
<head>
<meta name="layout" content="panels" />
<script type='text/javascript'>

$(document).ready( function () {
	$("#recCnt").val(${categories?.size()});

	// Apply a class on mouse click
	$('#catTbl li').click(function () {
		$(".Highlight").removeClass();
		$(this).addClass('Highlight');
	});

	// Assign a click handler that grabs item Id from the first cell
	$('#catTbl li').click(function () {
		var tdVal= $(this).find('strong :input').attr('value')
		//alert ('tdVal=' + tdVal);
		$("#deleteItemId").val(tdVal);
	});

	$('#catTbl li').dblclick(function() {
		var testVal= ($(this).find('strong :input').attr('value'));
		//alert ('testVal=' + testVal);	          
		$('#item').attr('action', '/jbilling/product/type/' + testVal);	    	  
		$('#item').submit();
	});	      
});

function del() {
	if (0 == parseInt( $("#deleteItemId").val() ) )
	{
		alert('Please select a row to delete');
		return false;
	}
	if (confirm("Are you sure you want to delete [" + document.getElementById("deleteItemId").value + "]")){
		$('#item').attr('action', '/jbilling/item/delete/');	    	  
		$('#item').submit();
		return true;
	}
	return false;
}

function add(tblId) {

	var count = parseInt(document.getElementById("recCnt").value);
	var lastCnt= (count - 1);
	//alert('count is '+count);
	var elm= $('#row'+ lastCnt).clone().find('input').val('')
		.end().appendTo('#catTbl ul');

	elm.attr("id", ("row" + count));
	elm.find('input:eq(0)').attr('id', ('categories['+ count +'].id'))
		.attr('name', ('categories['+ count +'].id'))
		.attr('disabled', 'disabled').click (function () {return false});
	
	elm.find('input:eq(1)').attr("id", ('categories['+ count +'].description'))
		.attr("name", ('categories['+ count +'].description'))
		.focus().click (function () {return false});
	
	elm.find('select').attr('id',('categories['+ count +'].orderLineTypeId'))
		.attr('name',('categories['+ count +'].orderLineTypeId'))
		.click (function () {return false});
	count++;
	$("#recCnt").val(count);

}
</script>

<style type="text/css">
	.Highlight {
		background-color: red;
		cursor: pointer;
	}
</style>

</head>

<body>
<content tag="filters">
</content>

<content tag="column1">
    <g:render template="categories" model="['categories': categories]"/> 
</content>

<content tag="column2">
</content>
</body>
</html>