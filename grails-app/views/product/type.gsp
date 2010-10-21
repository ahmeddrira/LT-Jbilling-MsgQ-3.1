<html>
<link
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css"
	rel="stylesheet" type="text/css" />
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
<style type="text/css">
.Highlight {
	background-color: red;
	cursor: pointer;
}
</style>

<script type="text/javascript">	

    $(function ()
    {

    	// Return a helper with preserved width of cells
    	var fixHelper = function(e, ui) {
    	    ui.children().each(function() {
    	        $(this).width($(this).width());
    	    });
    	    return ui;
    	};
    	 
    	$("#catTbl tbody").sortable({
    	    helper: fixHelper
    	}).disableSelection();
        
      // Apply a class on mouse over and remove it on mouse out.
      $('.link-table tr').click(function ()
      {
        $(this).toggleClass('Highlight');
      });
      
      $('.link-table tr').click(function ()
      {
          var productId = $(this).find("#productId").val();
    	  document.getElementById("selectedId").value= productId;
    	  //alert(document.getElementById("selectedId").value);
    	  //jQuery.get();
      });
      
	  $('.link-table tr').click(function(){
	      $("div#newTbl").html('Retrieving...');
		  $.ajax({
		      type: "POST",
			  data: "productId=" +  document.getElementById("selectedId").value,
			  url: "/jbilling/product/show/" + document.getElementById("selectedId").value,
			  success: function(msg){
				  $("div#newTbl").html(msg)
			  }
	  });});

      $('.link-table tr').dblclick(function()
      {
    	  //alert(document.getElementById("selectedId").value);
          document.forms[0].action='/jbilling/product/edit/' + $(this).find("#productId").val();
          document.forms[0].submit();
      });
      
    });

  </script>


<title>
${title}
</title>
</head>
<script language="javascript">
function nLoad() {
	document.getElementById("recCnt").value = ${list.size()};
}

function del() {
	if (0 == parseInt(document.getElementById("selectedId").value))
	{
		alert('Please select a row to delete');
		return false;
	}
	if (confirm("Are you sure you want to delete [" + document.getElementById("selectedId").value + "]")){		
		return true;
	}
	return false;
}
function add() {
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
	        cells[i].firstElementChild.id = "list["+(count)+"].id";
	        cells[i].firstElementChild.name = "list["+(count)+"].id";
	        cells[i].firstElementChild.value="";    
	    } else if (cells[i].firstElementChild.id.indexOf("description") != -1) {
	        cells[i].firstElementChild.id = "list["+(count)+"].description";
	        cells[i].firstElementChild.name = "list["+(count)+"].description";	       
	    } else if (cells[i].firstElementChild.id.indexOf("internalNumber") != -1) {
	        cells[i].firstElementChild.id = "list["+(count)+"].internalNumber";
	        cells[i].firstElementChild.name = "list["+(count)+"].internalNumber";
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
<body onload="nLoad()">
<h2><g:message code="prompt.products" /></h2>
<g:form>
<g:hiddenField name="recCnt" value="0" />
<g:hiddenField name="selectedId" value="0" />
<div id="replaceTbl">
<table id="catTbl" cellspacing='4' class="link-table">
	<thead>
		<tr>

			<th><g:message code="product.name" /></th>
			<th><g:message code="product.id" /></th>
			<th><g:message code="product.internal.number" /></th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${list}" status="idx" var="item">
			<tr>

				<td><g:textField size="40" readonly="readonly"
					name="item[${idx}].description" value="${item.description}" /></td>
				<td><g:textField id="productId" readonly="readonly" name="item[${idx}].id"
					value="${item.id}" /></td>
				<td><g:textField readonly="readonly"
					name="item[${idx}].internalNumber" value="${item.internalNumber}" /></td>
				<td></td>
			</tr>
		</g:each>
	</tbody>
</table>
</div>

<div id="newTbl">
</div>

<table>
		<tr>
			<td>
				<input type="button" value="Add Item" onclick="add()"
				class="form_button" /></td>
			<td><g:actionSubmit value="Delete Item" onclick="javascript: return del()"
				class="form_button" action="del" /></td>
			<td><g:actionSubmit value="Show All Items" 
				class="form_button" action="showAll" /></td>
		</tr>
	</table>
	</g:form>
</body>
</html>