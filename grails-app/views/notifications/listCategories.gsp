<html>
<head>
<meta name="layout" content="panels" />

<script language="javascript">
	$(document).ready(function() {
		  // Apply a class on mouse over and remove it on mouse out.
	      $('#catTbl li').click(function() {
		    $(".Highlight").removeClass()
	        $(this).addClass('Highlight');
	      });
	      
	      $('#catTbl li').click(function() {
	          var categId = $(this).find("#categId").val();
	    	  //alert(categId)
	    	  $("#selectedId").val(categId);	    	  
	      });
<%--
	      $('#catTbl li').dblclick(function() {
	    	  //var selVal= $('#selectedId').val();
	    	  $('#notifications').attr('action', '/jbilling/notifications/lists/' + $('#selectedId').val());	    	  
	    	  $('#notifications').submit();
	      });	      
--%>	      
	});	    	
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
    <g:render template="categories" model="['lst': lst]"/> 
</content>

<content tag="column2">
</content>
</body>
</html>