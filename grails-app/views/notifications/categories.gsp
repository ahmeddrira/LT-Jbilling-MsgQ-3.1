<html>
<head>
<meta name="layout" content="main" />

<script language="javascript">
	$(document).ready(function() {
		  // Apply a class on mouse over and remove it on mouse out.
	      $('.link-table tr').click(function() {
		    $(".Highlight").removeClass()
	        $(this).addClass('Highlight');
	      });
	      
	      $('.link-table tr').click(function() {
	          var categId = $(this).find("#categId").val();
	    	  //alert(categId)
	    	  $("#selectedId").val(categId);	    	  
	      });

	      $('.link-table tr').dblclick(function() {
	    	  //var selVal= $('#selectedId').val();
	    	  $('#notifications').attr('action', '/jbilling/notifications/lists/' + $('#selectedId').val());	    	  
	    	  $('#notifications').submit();
	      });	      
	});	    	
</script>

<style type="text/css">
.Highlight {
	background-color: red;
	cursor: pointer;
}
</style>
<title>
</title>
</head>

<body>
<p>
	<g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMsg}" />
	<jB:renderErrorMessages />
</p>
<g:form name="notifications" controller="notifications" action="preferences">
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
			<td><input type="submit" name="preferences" value="Preferences" class="form_button" />
			<!--  <g:actionSubmit value="Preferences" action="preferences"
				class="form_button" />
			-->
			</td>			
		</tr>
	</table>
	</div>
</g:form>
</body>
</html>