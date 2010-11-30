<html>
<head>
<meta name="layout" content="panels" />
<script type='text/javascript'>
	$(document).ready( function ()
	    {
	      // Apply a class on mouse click
	      $('#catTbl li').click(function ()
	      {
	    	  $(".Highlight").removeClass();
			  $(this).addClass('Highlight');
	      });
	  
	      // Assign a click handler that grabs item Id from the first cell
	      $('#catTbl li').click(function ()
	      {
		      var tdVal= $(this).find('strong :input').attr('value')
		      alert ('tdVal=' + tdVal);
	          $("#deleteItemId").val(tdVal);
	      });

	      $('#catTbl li').dblclick(function()
	      {
	          var testVal= ($(this).find('strong :input').attr('value'));
	          //alert ('testVal=' + testVal);	          
	          $('#item').attr('action', '/jbilling/product/type/' + testVal);	    	  
	    	  $('#item').submit();
	      });
	      $("#recCnt").val(${categories?.size()});
	});
      
function nLoad() {
	document.getElementById("recCnt").value = ${categories.size()};
}

function del() {
	if (0 == parseInt(document.getElementById("deleteItemId").value))
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
	var elm= $('#row'+ lastCnt).clone().find('input').val('')//#categories[' + lastCnt + '].id').removeAttr("id").attr('id','categories[' + count + '].id').val('')
		.end().appendTo('#catTbl');

	elm.attr("id", ("row" + count));
	//alert(elm.attr('id'));
	elm.find('input:eq(0)').removeAttr("id").attr('id', ('categories['+ count +'].id'));
	//elm.find('input:eq(1)').attr('id', ('categories['+ count +'].description'));
	//elm.find('select').attr('id',('categories['+ count +'].orderLineTypeId'));

	alert(elm.find('select').attr('id');
	
	var tblBody = document.getElementById('catTbl').ul[0];
	//alert(tblBody.rows.length);
	var newNode = tblBody.li[0].cloneNode(true);
	tblBody.appendChild(newNode);
	//alert("Count Found=" + count);
	var cells = newNode.cells;
	//alert("Cells Length=" + cells.length)
	for (var i=0; i < cells.length ; i ++) {
	    if (cells[i].firstElementChild.id.indexOf("id") != -1) {
	        cells[i].firstElementChild.id = "categories["+(count)+"].id";
	        cells[i].firstElementChild.name = "categories["+(count)+"].id";
	        cells[i].firstElementChild.value="";    
	    } else if (cells[i].firstElementChild.id.indexOf("description") != -1) {
	        cells[i].firstElementChild.id = "categories["+(count)+"].description";
	        cells[i].firstElementChild.name = "categories["+(count)+"].description";	       
	    } else if (cells[i].firstElementChild.id.indexOf("orderLineTypeId") != -1) {
	        cells[i].firstElementChild.id = "categories["+(count)+"].orderLineTypeId";
	        cells[i].firstElementChild.name = "categories["+(count)+"].orderLineTypeId";
	    }	   
	    cells[i].firstElementChild.value="";
	    //alert(cells[i].firstElementChild.id);	    
	}	
	//document.getElementById('catTbl').tBodies[0].appendChild(newNode);	
	count++;
	//alert("New Count Value=" + count);
	document.getElementById("recCnt").value = count;
	//alert("New recCnt Value=" + document.getElementById("recCnt").value);
}
</script>

<style type="text/css">
.Highlight {
	background-color: red;
	cursor: pointer;
}
</style>

</head>

<body onload="nLoad();">
<content tag="filters">
</content>

<content tag="column1">
    <g:render template="categories" model="['categories': categories]"/> 
</content>

<content tag="column2">
</content>
</body>
</html>