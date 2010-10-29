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
<head>
<title>
${title}
</title>
</head>
<script language="javascript">
	$(function ()
	{
		  // Apply a class on mouse over and remove it on mouse out.
	      $('.link-table tr').click(function ()
	      {
		    $(".Highlight").removeClass()
	        $(this).addClass('Highlight');
	      });
	      
	      $('.link-table tr').click(function ()
	      {
	          var categId = $(this).find("#categId").val();
	    	  //alert(categId)
	    	  document.getElementById("selectedId").value= categId;	    	  
	      });

	      $('.link-table tr').click(function(){
		      $("div#newTbl").html('Retrieving...');
			  $.ajax({
			      type: "POST",
				  data: "categoryId=" +  document.getElementById("selectedId").value,
				  url: "/jbilling/notifications/lists/" + document.getElementById("selectedId").value,
				  success: function(msg){
					  $("div#newTbl").html(msg)
				  }
		  });});

	      $('.link-table tr').dblclick(function()
	      {
	    	  alert(document.getElementById("selectedId").value);
	          document.forms[0].action='/jbilling/notifications/edit/' + document.getElementById("selectedId").value;
	          document.forms[0].submit();
	      });
	      
	});
	    	
</script>
<body>
<jB:renderErrorMessages />
<g:form>
<g:hiddenField name="selectedId" value="0" />
	<div>
	<table id="catTbl" cellspacing='4' class="link-table">
		<thead>
			<tr>
				<th><g:message code="title.notification.category" /></th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${lst}" status="idx" var="dto">
				<tr>
					<td><g:hiddenField id="categId" name="categId${idx}"
						value="${dto?.getId()}" /> ${dto.getDescription(languageId)}
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
	<div id="newTbl"></div>

	<table>
		<tr>
			<td><g:actionSubmit value="Preferences" action="preferences"
				class="form_button" /></td>			
		</tr>
	</table>
	</div>
</g:form>
</body>
</html>