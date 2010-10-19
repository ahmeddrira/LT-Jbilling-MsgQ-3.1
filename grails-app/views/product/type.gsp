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

    //$.get("/jbilling/product/show/" + document.getElementById("selectedId").value,
   	//    "{productId:" + document.getElementById("selectedId").value + "}",
   	//    	function(data) { document.getElementById("newTbl").innerHTML=data; },
   	//    "html"
   	//);
	
  </script>


<title>
${title}
</title>
</head>
<script language="javascript">
function nLoad() {
	document.getElementById("recCnt").value = ${list.size()};
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
</g:form>
<div id="newTbl">
ADSFA
</div>
</body>
</html>