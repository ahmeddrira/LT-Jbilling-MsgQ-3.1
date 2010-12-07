<%@page import="com.sapienter.jbilling.server.util.Constants;"%>

<div class="column-hold">
	<div class="heading">
	    <strong style="width:100%">
			<g:message code="prompt.notifications.preferences"/>
	    </strong>
	</div>

	<div class="box">
		<dl class="other">
			<g:set var="dto" value="${subList.get(Constants.PREFERENCE_TYPE_SELF_DELIVER_PAPER_INVOICES)}" />
			<dt><g:message code="notification.preference.selfDeliver.prompt"/>:</dt>
			<dd>${ ((dto?.getIntValue() != 0) ? "Yes": "No") }</dd>

			<g:set var="dto" value="${subList.get(Constants.PREFERENCE_TYPE_INCLUDE_CUSTOMER_NOTES)}" />
			<dt><g:message code="notification.preference.showNotes.prompt"/>:</dt>
			<dd>${ (dto?.getIntValue() != 0)? "Yes": "No"}</dd>

			<g:set var="dto" value="${subList.get(Constants.PREFERENCE_TYPE_DAY_BEFORE_ORDER_NOTIF_EXP)}" />
			<dt><g:message code="notification.preference.orderDays1.prompt"/>:</dt>
			<dd>${dto?.getIntValue()}</dd>
			<g:set var="dto" value="${subList.get(Constants.PREFERENCE_TYPE_DAY_BEFORE_ORDER_NOTIF_EXP2)}" />
			<dt><g:message code="notification.preference.orderDays2.prompt"/>:</dt>
			<dd>${dto?.getIntValue()}</dd>

			<g:set var="dto" value="${subList.get(Constants.PREFERENCE_TYPE_DAY_BEFORE_ORDER_NOTIF_EXP3)}" />
			<dt><g:message code="notification.preference.orderDays3.prompt"/>:</dt>
			<dd>${dto?.getIntValue()}</dd>

			<g:set var="dto" value="${subList.get(Constants.PREFERENCE_TYPE_USE_INVOICE_REMINDERS)}" />
			<dt><g:message code="notification.preference.invoiceRemiders.prompt"/>:</dt>
			<dd>${(dto?.getIntValue() != 0)?"Yes":"No"}</dd>

			<g:set var="dto" value="${subList.get(Constants.PREFERENCE_TYPE_NO_OF_DAYS_INVOICE_GEN_1_REMINDER)}"/>
			<dt><g:message code="notification.preference.reminders.first"/>:</dt>
			<dd>${dto?.getIntValue()}</dd>

			<g:set var="dto" value="${subList.get(Constants.PREFERENCE_TYPE_NO_OF_DAYS_NEXT_REMINDER)}" />
			<dt><g:message code="notification.preference.reminders.next"/>:</dt>
			<dd>${dto?.getIntValue()}</dd>
		</dl>
	</div>
	<div class="btn-box">
		<a href="${createLink(action: 'editPreferences')}" class="submit edit">
	    	<span><g:message code="button.edit"/></span></a>
	</div>
</div>