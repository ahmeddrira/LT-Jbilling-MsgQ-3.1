<html>
<head>
<link
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css"
	rel="stylesheet" type="text/css" />
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>	

</head>
<script language="javascript">

$(function (){
	$("#language.id").val(${languageId});
});

//variable to store the registered element that was last clicked
var targetElement

//function to initialize targetElement for token insert
function elementClick(tempElm) { 
	//alert('msie=' + $.browser.msie);
	//alert('mozilla='+$.browser.mozilla);	
	targetElement= tempElm	
	//alert(tempElm.SelectionStart);
}

//this function inserts the token at the last cursor position on the targetElement.
//it uses jQuery to determine browser and perform browser specific action
function testfunc() {
	if ( null == targetElement ) {
		return;
	}
	var cdate = new Date()	
	var monArr = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sept', 'Oct', 'Nov', 'Dec']
	var testval= monArr[cdate.getMonth()] + " " + cdate.getDate() + ", " 
				+ cdate.getFullYear() + " " + cdate.getHours()+ ":" + cdate.getMinutes() +  ":\n";
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
}
</script>
<body>
<h2><g:message code="prompt.edit.note" /></h2>
<p>
	<jB:renderErrorMessages />
</p>
<g:form action="saveNote">
<g:hiddenField name="_id" value="${id}"/>
<g:textArea onclick="elementClick(this)" onchange="elementClick(this)" cols="20" rows="10"
						name="notes"
						value="${notes}" />
<input type="button" value="Add Timestamp" onclick="testfunc();">
<table>
		<tr>
			<td><g:actionSubmit value="Save Changes"
				action="saveNote" class="form_button" /></td>
			<td><input type="button" value="Cancel"
				onClick="javascript: history.back()" /></td>
		</tr>
	</table>
</g:form>
</body>
</html>