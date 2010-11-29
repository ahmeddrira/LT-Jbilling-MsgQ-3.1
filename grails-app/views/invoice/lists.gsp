<%@ page import="com.sapienter.jbilling.server.util.Util"%>
<html>
<head>
<meta name="layout" content="main" />
<script type='text/javascript'>
	$(function ()
	{
    	//$("#invTbl tbody").sortable({
    	//    helper: fixHelper
    	//}).disableSelection();
    	
		// Apply a class on mouse click
		$('.link-table tr').click(function ()
		{
			$('.Highlight').removeClass();
			$(this).addClass('Highlight');

			// Assign a click handler that grabs item Id from the first cell
			var tdVal= $(this).find('#invId').text();
			//alert ('tdVal=' + tdVal);
			$("#selectedInvId").val(tdVal);
			
			$("#selectedUserId").val($(this).find('#userId').text());			
		});

		$('.link-table tr').dblclick(function()
		{
		    var testVal= $("#selectedInvId").val();		    
		    if ( null == testVal || "" == testVal ) return false;
		    //window.location = '/jbilling/invoice/show/' + testVal;			
		    $('#invoice').attr('action', '/jbilling/invoice/show/' + testVal);	    	  
	    	$('#invoice').submit();
		});
		$('.delete-invoice').click(function() {
			var testVal= $("#selectedInvId").val();
			if (null== testVal || '' == testVal) {
				alert('<g:message code="invoice.not.selected.message"/>');
				return false;
			}
			Show_Popup();
			//if (confirm("Are you sure you want to delete the invoice# " 
			//		+ document.getElementById("selectedInvId").value + "]")){
			//	return true;
			//}
			return false;
		});	
	});
</script>
<style type="text/css">
	.Highlight {
		background-color: red;
		cursor: pointer;
	}
</style>
<style type="text/css">
#popup {
	height: 100%;
	width: 100%;
	background: #000000;
	position: absolute;
	top: 0;
	-moz-opacity: 0.75;
	-khtml-opacity: 0.75;
	opacity: 0.75;
	filter: alpha(opacity = 75);
}

#window {
	width: 600px;
	height: 300px;
	margin: 0 auto;
	border: 1px solid #000000;
	background: #ffffff;
	position: absolute;
	top: 200px;
	left: 25%;
}
</style>
<script type="text/javascript">
function Show_Popup(action, userid) {	
	$('#popup').fadeIn('fast');
	$('#window').fadeIn('fast');
}
function Close_Popup() {	
	$('#popup').fadeOut('fast');
	$('#window').fadeOut('fast');
}
</script>
</head>
<body><p><jB:renderErrorMessages /></p>

<g:form name="invoice">

<div class="deleteprompt">
	<div id="popup" style="display: none;"></div>
	<div id="window" style="display: none;">
		<div id="popup_content">
			<h1><g:message code="invoice.prompt.delete"/></h1>
			<p><g:message code="invoice.prompt.are.you.sure"/></p>
			<h6>This action is not reversible</h6>
			<g:actionSubmit value="Delete Invoice" action="delete" class="form_button"
				onclick=""/> 
			<input type="button" value="Cancel" class="form_button" 
				onclick="javascript: Close_Popup()" />
		</div>
	</div>
</div>

<input type="hidden" name="selectedInvId" id="selectedInvId" value=""/>
<input type="hidden" name="selectedUserId" id="selectedUserId" value=""/>

<input type="hidden" name="_userId" value="${userId}"/>
<table cellpadding="5" id="invTbl" class="link-table">
	<thead>
		<tr>
			<td>Date</td>
			<td>Due Date</td>
			<td>ID</td>
			<td>#</td>
			<td>Stat.</td>
			<td>Amount</td>
			<td>Balance</td>
		</tr>
	</thead>
	<tbody>
		<g:each var="inv" in="${invoices}">
			<tr id="invTbltr">
				<td>
				${Util.formatDate(inv.createDateTime, session["user_id"]) }
				</td>
				<td>
				${Util.formatDate(inv.dueDate, session["user_id"]) }
				</td>
				<td id="invId">
				${inv.id }
				</td>
				<td>
				${inv.number }
				</td>
				<td>
				${inv.statusDescr }
				</td>
				<td>
				${Util.formatMoney(new BigDecimal(inv.total),
					session["user_id"],inv.currencyId, false)}
				</td>
				<td>
				${Util.formatMoney(new BigDecimal(inv.balance),
					session["user_id"],inv.currencyId, false)}
					<input type="hidden" id="userId" value="${inv.userId}"/>
				</td>
			</tr>
		</g:each>
	</tbody>
</table>

    <div class="btn-box">
        <a href="#" class="delete-invoice"><span><g:message code="button.delete"/></span></a>
    </div>
</g:form>
</body>
</html>