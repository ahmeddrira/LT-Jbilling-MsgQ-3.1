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
    	  $(".Highlight").removeClass();
		  $(this).addClass('Highlight');
      });
      
      $('.link-table tr').click(function ()
      {
          var productId = $(this).find("#productId").val();
    	  $("#selectedId").val(productId);
    	  //alert(document.getElementById("selectedId").value);
    	  //jQuery.get();
      });
      
	  $('.link-table tr').click(function(){
	      $("div#newTbl").html('Retrieving...');
		  $.ajax({
		      type: "POST",
			  data: "productId=" +  $("#selectedId").val(),
			  url: "/jbilling/product/show/" + $("#selectedId").val(),
			  success: function(msg){
				  $("div#newTbl").html(msg)
			  }
	  });});

      $('.link-table tr').dblclick(function()
      {
    	  //alert(document.getElementById("selectedId").value);
          document.forms[0].action='/jbilling/product/edit/' + $("#selectedId").val();
          document.forms[0].submit();
      });
      
    });

  </script>


<title>
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
}


</script>
<body onload="nLoad()">
<h2><g:message code="prompt.products" /></h2>
<p>
	<g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMsg}" />
	<jB:renderErrorMessages />
</p>
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
				<g:if test="${item.deleted == 0 }">
					<tr>
						<td><g:textField size="40" readonly="readonly"
							name="item[${idx}].description" value="${item.description}" /></td>
						<td><g:textField id="productId" readonly="readonly"
							name="item[${idx}].id" value="${item.id}" /></td>
						<td><g:textField readonly="readonly"
							name="item[${idx}].internalNumber" value="${item.internalNumber}" /></td>
						<td></td>
					</tr>
				</g:if>
			</g:each>
		</tbody>
	</table>
	</div>

	<div id="newTbl"></div>

	<table>
		<tr>
			<td><g:actionSubmit value="Add Item" action="add"
				class="form_button" /></td>
			<td><g:actionSubmit value="Delete Item"
				onclick="javascript: return del()" class="form_button" action="del" /></td>
			<td><g:actionSubmit value="Show All Items" class="form_button"
				action="showAll" /></td>
		</tr>
	</table>
</g:form>
</body>
</html>