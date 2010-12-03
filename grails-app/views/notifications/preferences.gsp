<div class="column-hold">

	<div class="heading">
	    <strong style="width:100%">
			<g:message code="prompt.notifications.preferences"/>
	    </strong>
	</div>

	<div class="box">
		<dl class="other">
			<dt><g:message code="notification.preference.selfDeliver.prompt"/>:</dt>
			<dd>${ (dto?.getIntValue() != 0) ? "Yes": "No" }</dd>
		
			<dt><g:message code="notification.preference.showNotes.prompt"/>:</dt>
			<dd>${ (dto?.getIntValue() != 0)? "Yes": "No"}</dd>
			
			<dt><g:message code="notification.preference.orderDays1.prompt"/>:</dt>
			<dd>${dto?.getIntValue()}</dd>
			
			<dt><g:message code="notification.preference.orderDays2.prompt"/>:</dt>
			<dd>${dto?.getIntValue()}</dd>
					
			<dt><g:message code="notification.preference.orderDays3.prompt"/>:</dt>
			<dd>${dto?.getIntValue()}</dd>
		
			<dt><g:message code="notification.preference.invoiceRemiders.prompt"/>:</dt>
			<dd>${(dto?.getIntValue() != 0)?"Yes":"No"}</dd>
		
			<dt><g:message code="notification.preference.reminders.first"/>:</dt>
			<dd>${dto?.getIntValue()}</dd>
		
			<dt><g:message code="notification.preference.reminders.next"/>:</dt>
			<dd>${dto?.getIntValue()}</dd>
		</dl>
	</div>
	<div class="btn-box">
	    <a href="javascript:void(0)" class="submit edit">
	    	<span><g:message code="button.edit"/></span></a>
	</div>
</div>