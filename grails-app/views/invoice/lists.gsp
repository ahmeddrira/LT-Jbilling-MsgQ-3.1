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
		});

		$('.link-table tr').dblclick(function()
		{
		    var testVal= $("#selectedInvId").val();
		    if ('' == testVal || null == testVal) return false;
		    window.location = '/jbilling/invoice/show/' + testVal;			
		});
	});
</script>
<style type="text/css">
	.Highlight {
		background-color: red;
		cursor: pointer;
	}
</style>
</head>
<body><p><jB:renderErrorMessages /></p>
<input type="hidden" name="selectedInvId" id="selectedInvId" value=""/>
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
				</td>
			</tr>
		</g:each>
	</tbody>
</table>

</body>
</html>