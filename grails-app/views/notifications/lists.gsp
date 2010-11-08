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

</head>
<script language="javascript">
	$(function ()
	{
		$('.sub-table tr', this).click(function()
		{
			$(".Highlight").removeClass();
			$(this).addClass('Highlight');
		});

		$('.sub-table tr', this).dblclick(function()
		{
			var typeId = $(this).find("#typeId").val();	    	
	    	document.getElementById("selectedId").value= typeId;	
			document.forms[0].action='/jbilling/notifications/edit/' + typeId;
			document.forms[0].submit();
		});
	});
	</script>
<body>
<p>
	<g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMsg}" />
	<jB:renderErrorMessages />
</p>
<g:form>
	<g:hiddenField name="selectedId" value="0" />
	<div>
	<table id="catTbl" cellspacing='4' class="sub-table">
		<thead>
			<tr>
				<th><g:message code="title.notification" /></th>
				<th><g:message code="title.notification.active" /></th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${lst}" status="idx" var="dto">
				<tr>
					<td><g:hiddenField id="typeId" name="typeId${idx}"
						value="${dto?.getId()}" /> ${dto.getDescription(languageId)}
					</td>
					<td align="right"><g:set var="flag" value="${true}" /> 
						<g:each status="iter" var="var" in="${dto.getNotificationMessages()}">
						<g:if test="${languageId == var.getLanguage().getId() 
							&& var.getEntity().getId() == entityId && var.getUseFlag() > 0}">
							Yes
							<g:set var="flag" value="${false}" />
						</g:if>
					</g:each> <g:if test="${flag}">
						No
					</g:if></td>
				</tr>
			</g:each>
		</tbody>
	</table>
	</div>
</g:form>
</body>
</html>