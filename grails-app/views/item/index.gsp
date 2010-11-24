<html>
<head>
<meta name="layout" content="main" />
<script type='text/javascript'>
	$(document).ready( function ()
	    {
	      // Apply a class on mouse click
	      $('.link-table tr').click(function ()
	      {
	    	  $(".Highlight").removeClass();
			  $(this).addClass('Highlight');
	      });
	  
	      // Assign a click handler that grabs item Id from the first cell
	      $('.link-table tr').click(function ()
	      {
		      var tdVal= $(this).find('td input').attr('value')
		      //alert ('tdVal=' + tdVal);
	          $("#deleteItemId").val(tdVal);
	      });

	      $('.link-table tr').dblclick(function()
	      {
	          var testVal= ($(this).find('td :input').attr('value'));
	          //alert ('testVal=' + testVal);
	          //document.forms[0].action='/jbilling/product/type/' + $(this).find('td input').attr('value');
	          //document.forms[0].submit();
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
	var tblBody = document.getElementById('catTbl').tBodies[0];
	//alert(tblBody.rows.length);
	var newNode = tblBody.rows[0].cloneNode(true);
	tblBody.appendChild(newNode);
	var count = parseInt(document.getElementById("recCnt").value);
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
<p><g:message code="prompt.product.category" /></p>
<p>
	<jB:renderErrorMessages/>
	<g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMsg}"/> 
</p>
<g:form name="item" controller="item" action="save">
	<g:hiddenField name="recCnt" value="0" />
	<g:hiddenField name="deleteItemId" value="0" />
	<!-- g:hiddenField name="delOrderTypeId" value="0"/-->

	<table id="catTbl" cellspacing='4' class="link-table">
		<thead>
			<tr>

				<th><g:message code="product.category.id" /></th>
				<th><g:message code="product.category.name" /></th>
				<th><g:message code="product.category.type" /></th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${categories}" status="idx" var="cat">
				<tr>
					<td><g:textField readonly="readonly"
						name="categories[${idx}].id" value="${cat.id}" /></td>
					<td><g:textField name="categories[${idx}].description"
						value="${cat.description}" /></td>
					<td><g:select name="categories[${idx}].orderLineTypeId"
						from="${com.sapienter.jbilling.server.order.db.OrderLineTypeDTO.list()}"
						optionKey="id" optionValue="description"
						value="${cat.orderLineTypeId}" /></td>
				</tr>
			</g:each>
		</tbody>
	</table>
	<table>
		<tr>
			<td><input type="button" value="Add" onclick="add('catTbl')"
				class="form_button" /></td>
			<td> <input type="submit" value="Delete"
				onclick="javascript: return del();" class="form_button"/>
				<!-- <g:actionSubmit type="button" value="Delete"
				onclick="javascript: return del()" class="form_button"
				action="delete" /> -->
			</td>
		</tr>
	</table>

	<table>
		<tr>
			<td><input type="submit" value="Save Changes" class="form_button" />
				<!-- <g:actionSubmit value="Save Changes" action="save"
				class="form_button" /> -->
			</td>
			<td><g:actionSubmit value="Cancel" action="index"
				class="form_button" onclick="javascript: history.back();"/></td>
		</tr>
	</table>

</g:form>
</body>
</html>