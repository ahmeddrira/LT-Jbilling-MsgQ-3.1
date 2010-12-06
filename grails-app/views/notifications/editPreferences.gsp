<div class="column-hold">
<div class="heading table-heading">
    <strong style="width:100%">
		<g:message code="prompt.notifications.preferences"/>
    </strong>
</div>

<g:form name="notifications" controller="notifications" action="savePrefs">
<g:hiddenField name="recCnt" value="8"/>
	<div>
	<table cellspacing="1">
		<tbody>
			<g:set var="dto" value="${subList.get(13)}" />
			<tr>
				<!--  
				<td>$dto?.preferenceType?.getDescription(languageId):</td>
				-->
				<td><g:message code="notification.preference.selfDeliver.prompt"/>:</td>
				<td><g:hiddenField value="${dto?.id}" name="pref[0].id"/>
					<g:hiddenField value="${5}" name="pref[0].tableId"/>
					<g:hiddenField value="${13}" name="pref[0].preferenceType.id"/>
					<g:checkBox name="pref[0].value" checked="${dto?.getIntValue() != 0}"/>
				</td>
			</tr>
			
			<g:set var="dto" value="${subList.get(14)}" />
			<tr>
				<td><g:message code="notification.preference.showNotes.prompt"/>:</td>
				<td><g:hiddenField value="${dto?.id}" name="pref[1].id"/>
					<g:hiddenField value="${5}" name="pref[1].tableId"/>
					<g:hiddenField value="${14}" name="pref[1].preferenceType.id"/>		
					<g:checkBox name="pref[1].value" checked="${dto?.getIntValue() != 0}"/>
				</td>
			</tr>
			
			<g:set var="dto" value="${subList.get(15)}" />
			<tr>
				<td><g:message code="notification.preference.orderDays1.prompt"/>:</td>
				<td><g:hiddenField value="${dto?.id}" name="pref[2].id"/>
					<g:hiddenField value="${5}" name="pref[2].tableId"/>
					<g:hiddenField value="${15}" name="pref[2].preferenceType.id"/>		
					<g:textField size="4" name="pref[2].value" value="${dto?.getIntValue()}"/>
				</td>
			</tr>
			
			<g:set var="dto" value="${subList.get(16)}" />
			<tr>
				<td><g:message code="notification.preference.orderDays2.prompt"/>:</td>
				<td><g:hiddenField value="${dto?.id}" name="pref[3].id"/>
					<g:hiddenField value="${5}" name="pref[3].tableId"/>
					<g:hiddenField value="${16}" name="pref[3].preferenceType.id"/>		
					<g:textField size="4" name="pref[3].value" value="${dto?.getIntValue()}"/>
				</td>
			</tr>
			
			<g:set var="dto" value="${subList.get(17)}" />		
			<tr>
				<td><g:message code="notification.preference.orderDays3.prompt"/>:</td>
				<td><g:hiddenField value="${dto?.id}" name="pref[4].id"/>
					<g:hiddenField value="${5}" name="pref[4].tableId"/>
					<g:hiddenField value="${17}" name="pref[4].preferenceType.id"/>		
					<g:textField size="4" name="pref[4].value" value="${dto?.getIntValue()}"/>
				</td>
			</tr>		
			
			<g:set var="dto" value="${subList.get(21)}" />
			<tr>
				<td><g:message code="notification.preference.invoiceRemiders.prompt"/>:</td>
				<td><g:hiddenField value="${dto?.id}" name="pref[5].id"/>
					<g:hiddenField value="${5}" name="pref[5].tableId"/>
					<g:hiddenField value="${21}" name="pref[5].preferenceType.id"/>		
					<g:checkBox name="pref[5].value" checked="${dto?.getIntValue() != 0}"/>
				</td>
			</tr>
			
			<g:set var="dto" value="${subList.get(22)}" />
			<tr>
				<td><g:message code="notification.preference.reminders.first"/>:</td>
				<td><g:hiddenField value="${dto?.id}" name="pref[6].id"/>
					<g:hiddenField value="${5}" name="pref[6].tableId"/>
					<g:hiddenField value="${22}" name="pref[6].preferenceType.id"/>		
					<g:textField size="4" name="pref[6].value" value="${dto?.getIntValue()}"/>
				</td>
			</tr>
			
			<g:set var="dto" value="${subList.get(23)}" />
			<tr>
				<td><g:message code="notification.preference.reminders.next"/>:</td>
				<td><g:hiddenField value="${dto?.id}" name="pref[7].id"/>
					<g:hiddenField value="${5}" name="pref[7].tableId"/>
					<g:hiddenField value="${23}" name="pref[7].preferenceType.id"/>		
					<g:textField size="4" name="pref[7].value" value="${dto?.getIntValue()}"/>
				</td>
			</tr>								
		</tbody>
	</table>
	<div class="btn-box">
	    <a href="javascript:void(0)" onclick="$('#notifications').submit();" class="submit save">
	    	<span><g:message code="button.save"/></span></a>
	    <a href="javascript:void(0)" class="submit cancel" onclick="closePanel(this);">
	    	<span><g:message code="button.cancel"/></span></a>
	</div>
	<%--
	<table>
		<tr>
			<td><g:actionSubmit onclick="$('#notifications').submit();" value="Save Changes"
				action="savePrefs" class="form_button" /></td>
			<td><input type="button" value="Cancel"
				onClick="closePanel(this);" /></td>
		</tr>
	</table>
	--%>
	</div>
</g:form>
</div>