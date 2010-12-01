
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

<script language="javascript">

//test cursor position code
var targetElement;
var position;
var glFlag= false;//implement onchange
var askPreference='${askPreference}';

$(function (){
	//alert('on document ready' + $ { l anguageId});
	$("#language.id").val(${languageId});
});

function Show_Popup(action, userid) {	
	$('#popup').fadeIn('fast');
	$('#window').fadeIn('fast');
}

function Close_Popup() {	
	$('#popup').fadeOut('fast');
	$('#window').fadeOut('fast');
	checkCookieValue();
	justGo()
}

function checkCookieValue(){
	//alert("Called checkcookievalue()");
	var doNotAskJS = $('#popupCheckbox:checked').val();	
	//alert('Checbox value=' + doNotAskJS);
	if ('on' == doNotAskJS) {
		//alert("its on");
		$('#doNotAskAgain').val(true);
	} else {
		$('#doNotAskAgain').val(false);
	}
}

function anychange(elm) {
	glFlag=true;
	elementClick(elm);
}

function saveFirst() {
	checkCookieValue();
	$('#askPreference').val('saveFirst');
	glFlag=false;
	document.forms[0].action='/jbilling/notifications/saveAndRedirect/' + document.getElementById('_id').value;
    document.forms[0].submit();
}

function justGo(){
	glFlag= false;
	$('#askPreference').val('justGo');
	//alert (document.getElementById('language.id').value);
    document.forms[0].action='/jbilling/notifications/edit/' + document.getElementById('_id').value;
    document.forms[0].submit();	
}

function lchange() {
	if (glFlag) {
		if ( null == askPreference || '' == askPreference ) {
			Show_Popup(null,null);			
		} else if ('saveFirst' == askPreference ) {
			saveFirst();
		} else {
			justGo();
		}			
	} else {
		justGo();	
	}
}

function elementClick(tempElm) { 
	//alert('msie=' + $.browser.msie);
	//alert('mozilla='+$.browser.mozilla);	
	targetElement= tempElm;
	//position= tempElm.SelectionStart
}

function testfunc(testval) { 
	//alert ('testfunc called. Elm=' + testval);
	if ($.browser.msie) {
		//if (document.selection) {
			targetElement.focus();
			sel = document.selection.createRange();
			sel.text = testval;
		//}
	} else if ($.browser.mozilla) {
		//alert('not ie');
		var startPos = targetElement.selectionStart;
		var endPos = targetElement.selectionEnd;
		//alert('Start=' + startPos + ' End=' + endPos);
		targetElement.value = targetElement.value.substring(0, startPos)
		+ testval
		+ targetElement.value.substring(endPos, targetElement.value.length);				
	} else {
		targetElement.value+=testval;
	}
	//to record changes
	glFlag=true;
	//alert(targetElement.Text);
	//alert(targetElement.SelectionStart);
	//alert(targetElement.Text.Substring(0, targetElement.SelectionStart).Length);
}
</script>

<div class="column-hold">

<div class="heading table-heading">
	<strong style="width: 100%">
		<g:message code="prompt.edit.notification"/>
	</strong>
</div>

<g:form name="notifications" controller="notifications" action="saveNotification">

	<g:hiddenField name="_id" value="${params.id}" />
	<g:hiddenField id="doNotAskAgain" name="doNotAskAgain" value="${false}"/>
	<g:hiddenField id="askPreference" name="askPreference" value="${askPreference}"/>
	<g:hiddenField name="msgDTOId" value="${dto?.getId()}" />
	<g:hiddenField name="entity.id" value="${entityId}" />
	<g:hiddenField name="_languageId" value="${languageId}"/>
	
	
	<div class="table-box">
		<ul>
		<li>
			<strong>			 
			<input type="button" class="submit add" value="$first_name" onclick="testfunc(this.value);">
			<input type="button" class="submit add" value="$last_name" onclick="testfunc(this.value);">
			<input type="button" class="submit add" value="$company_id" onclick="testfunc(this.value);">
			<input type="button" class="submit add" value="$company_name" onclick="testfunc(this.value);">
			<input type="button" class="submit add" value="$number" onclick="testfunc(this.value);">
			<input type="button" class="submit add" value="$password" onclick="testfunc(this.value);">
			<input type="button" class="submit add" value="$order_id" onclick="testfunc(this.value);">
			</strong>
		</li>
		<li>
			<strong><g:message code="title.notification.active" />:</strong>
			<strong><g:checkBox onchange="anychange(this)" name="useFlag"
				checked="${(dto?.getUseFlag() > 0)}" /></strong>
		</li>
		<li>
			<strong><g:message code="prompt.product.language" />:</strong>
			<strong><g:select name="language.id"
				from="${com.sapienter.jbilling.server.util.db.LanguageDTO.list()}"
				optionKey="id" optionValue="description" value="${languageId}"
				onchange="lchange()" /></strong>
		</li>

		<g:set var="flag" value="${true}" />
		<li>
			<strong><g:message code="prompt.edit.notification.subject" />:</strong>
			<strong><g:each in="${dto?.getNotificationMessageSections()}"
				var="section">
				<g:if test="${section.section == 1}">
					<g:hiddenField
						name="messageSections[${section.section}].id"
						value="${section.id}" />
					<g:hiddenField
						name="messageSections[${section.section}].section"
						value="${section.section}" />
					<g:set var="tempContent" value="" />
					<g:each in="${section.getNotificationMessageLines().sort{it.id}}"
						var="line">
						<g:set var="tempContent"
							value="${tempContent=tempContent + line?.getContent()}" />
					</g:each>
					<g:textField onchange="anychange(this)" size="30"
						name="messageSections[${section.section}].notificationMessageLines.content"
						value="${tempContent}" />
					<g:set var="flag" value="${false}" />
				</g:if>
			</g:each> <g:if test="${flag}">
				<g:hiddenField
						name="messageSections[1].id" value="" />
				<g:hiddenField
						name="messageSections[1].section" value="1" />
				<g:textField onchange="anychange(this)" size="30"
					name="messageSections[1].notificationMessageLines.content"
					value="" />
			</g:if></strong>
		</li>

		<g:set var="flag" value="${true}" />
		<li>
			<strong><g:message code="prompt.edit.notification.bodytext" />:</strong>
			<strong><g:each in="${dto?.getNotificationMessageSections()}"
				var="section">
				<g:if test="${section.section == 2}">
					<g:hiddenField
						name="messageSections[${section.section}].id"
						value="${section.id}" />
					<g:hiddenField
						name="messageSections[${section.section}].section"
						value="${section.section}" />
					<g:set var="tempContent" value="" />
					<g:each in="${section.getNotificationMessageLines().sort{it.id}}"
						var="line">
						<g:set var="tempContent"
							value="${tempContent=tempContent + line?.getContent()}" />
					</g:each>
					<g:textArea onclick="elementClick(this)" onchange="anychange(this)" cols="20" rows="10"
						name="messageSections[${section.section}].notificationMessageLines.content"
						value="${tempContent}" />
					<g:set var="flag" value="${false}" />
				</g:if>
			</g:each> <g:if test="${flag}">
				<g:hiddenField
						name="messageSections[2].id" value="" />
				<g:hiddenField
						name="messageSections[2].section" value="2" />
				<g:textArea onclick="elementClick(this)" onchange="anychange(this)" cols="20" rows="10"
					name="messageSections[2].notificationMessageLines.content"
					value="" />
			</g:if></strong>
		</li>

		<g:set var="flag" value="${true}" />
		<li>
			<strong><g:message code="prompt.edit.notification.bodyhtml" />:</strong>
			<strong><g:each in="${dto?.getNotificationMessageSections()}"
				var="section">
				<g:if test="${section?.section == 3}">
					<g:hiddenField
						name="messageSections[${section.section}].id"
						value="${section?.id}" />
					<g:hiddenField
						name="messageSections[${section.section}].section"
						value="${section?.section}" />
					<g:set var="tempContent" value="" />
					<g:each in="${section?.getNotificationMessageLines().sort{it.id}}"
						var="line">
						<g:set var="tempContent"
							value="${tempContent=tempContent + line?.getContent()}" />
					</g:each>
					<g:textArea onclick="elementClick(this)" onchange="anychange(this)" cols="20" rows="10"
						name="messageSections[${section.section}].notificationMessageLines.content"
						value="${tempContent}" />
					<g:set var="flag" value="${false}" />
				</g:if>
			</g:each> <g:if test="${flag}">
				<g:hiddenField
						name="messageSections[3].id" value="" />
				<g:hiddenField
						name="messageSections[3].section" value="3" />
				<g:textArea onclick="elementClick(this)" onchange="anychange(this)" cols="20" rows="10"
					name="messageSections[3].notificationMessageLines.content"
					value="" />
			</g:if></strong>
		</li>
		</ul>
	</div>
	
	<div class="btn-box">
	    <a href="#" onclick="$('#notifications').submit();" class="submit save">
	    	<span><g:message code="button.save"/></span></a>
	   	<a href="#" onclick="javascript: closePanel(this);" class="submit cancel">
	    	<span><g:message code="button.cancel"/></span></a>
	</div>
	<%-- 
	<table>
		<tr>
			<td><g:actionSubmit value="Save Changes"
				action="saveNotification" class="form_button" /></td>
			<td><input type="button" value="Cancel"
				onClick="javascript: closePanel(this);" /></td>
		</tr>
	</table>
	--%>
</g:form>
<div id="popup" style="display: none;"></div>
<div id="window" style="display: none;">
<div id="popup_content">
<h1>Notification has changed.</h1>
<p>You've made changes to this notification. Do you want to save the changes before switching?</p>
<p><g:checkBox id="popupCheckbox" value="${false}" />Don't ask me again</p>
<g:actionSubmit value="Yes"
				action="saveAndRedirect" class="form_button" onclick="saveFirst()"/>
<g:actionSubmit value="No"
				action="" class="form_button" onclick="Close_Popup();"/></div>
</div>
</div>