<html>
<head>
<title>This is the title</title>
</head>
<body>
<p><g:message code="prompt.create.user.form" /></p>
<p></p>
<table class="body">
	<tr>
		<td valign="top">
		    <g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMsg}" />
		    <g:each in="${session.errorMessages}">
		       <p>Error: ${it}</p>
		    </g:each>
		</td>

		<p class="title"><g:message code"prompt.create.user"/></p>


		<g:form action="create">
			<table class="form">
				<tr class="form">
					<td class="form_prompt"><g:message code="prompt.login.name" />
					</td>
					<td><input type="text" name="userName" maxlength="20"
						size="20" value=""></td>
				</tr>

				<tr class="form">
					<td class="form_prompt"><g:message code="prompt.password" /></td>
					<td><input type="password" name="password" maxlength="20"
						size="20" value=""></td>
				</tr>

				<tr class="form">
					<td class="form_prompt"><g:message
						code="prompt.verify.password" /></td>
					<td><input type="password" name="verifyPassword"
						maxlength="20" size="20" value=""></td>
				</tr>

				<tr class="form">
					<td class="form_prompt"><g:message code="prompt.email" /></td>
					<td><input type="email" name="email" maxlength="50" size="50"
						value=""></td>
				</tr>

				<tr class="form">
					<td class="form_prompt"><g:message code="prompt.role" /></td>
					<td><g:selectRoles name="mainRoleId"
						languageId="${languageId}" /></td>
				</tr>

				<tr>
					<td colspan="2" class="form_button"><input type="submit"
						name="submit" value="Submit" class="form_button"></td>
				</tr>

			</table>

		</g:form></td>
	</tr>
	<tr>
		<td colspan="3">
		<p class="footer">Visit&nbsp;<a target="jbsite"
			href="http://www.jbilling.com">jbilling.com</a>&nbsp;for
		documentation and support.<br />
		Copyright Enterprise jBilling Software Ltd. (c) 2009. All rights
		reserved.</p>

		</td>
	</tr>
</table>
</body>
</html>