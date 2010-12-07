<html>
<head>
    <meta name="layout" content="main" />
	<script language="javascript">
		function toggle(obj) {
			if (obj.checked) {
				if (obj.name == 'isAutomaticPaymentCC') {
					document.forms[0].isAutomaticPaymentAch.checked=false;
				} else if (obj.name == 'isAutomaticPaymentAch' ) {
					document.forms[0].isAutomaticPaymentCC.checked=false;
				}
			}
		}
	</script>
</head>
<body>
<div class="form-edit">
	<div class="heading">
		<strong><g:message code="prompt.create.edit.cust"/></strong>
	</div>
	<div class="form-hold">
		<g:form action="postEdit">

		<g:hiddenField name="contact.id" value="${user?.contact?.id}"/>
		<fieldset>
			<div class="form-columns">
				<div class="column">
					<div class="row">
						<p><g:message code="prompt.customer.number"/>:</p>
						<span>${user?.userId}</span>
					</div>
					<div class="row">
						<p><g:message code="prompt.customer.type"/></p>
						<span>${user?.role}</span>
					</div>
					<div class="row">
						<label><g:message code="prompt.login.name"/></label>
						<div class="inp-bg">
							<g:textField class="field" name="userName" value="${user?.userName}" />
						</div>
					</div>
					<div class="row">
						<label><g:message
										code="prompt.current.password" /></label>
						<div class="inp-bg">
							<g:passwordField  class="field" name="password" value="${user?.password}" />
						</div>
					</div>
					<div class="row">
						<label><g:message code="prompt.password" /></label>
						<div class="inp-bg">
							<g:passwordField  class="field" name="newPassword" />
						</div>
					</div>
					<div class="row">
						<label><g:message code="prompt.verify.password" /></label>
						<div class="inp-bg">
							<g:passwordField  class="field" name="verifyPassword" />
						</div>
					</div>
					<div class="row">
						<label><g:message
										code="prompt.user.status" /></label>
						<div style="width: 220px; " class="selectArea ">
							<g:userStatus name="statusId"
										value="${user?.statusId}" languageId="${languageId}" />
						</div>
					</div>
					<div class="row">
						<label><g:message
									code="prompt.user.subscriber.status" /></label>
						<div style="width: 220px; " class="selectArea ">
							<g:subscriberStatus name="subscriberStatusId"
								value="${user?.subscriberStatusId}" languageId="${languageId}" />
						</div>
					</div>
					<div class="row">
						<label><g:message
									code="prompt.user.language" /></label>
						<div style="width: 220px; " class="selectArea ">
							<g:select name="languageId"
								from="${com.sapienter.jbilling.server.util.db.LanguageDTO.list()}"
								optionKey="id" optionValue="description"
								value="${user?.languageId}"  />
						</div>	
					</div>
					<div class="row">
						<label><g:message
									code="prompt.user.currency" /></label>
						<div style="width: 220px; " class="selectArea ">
							<g:select name="currencyId"
								from="${com.sapienter.jbilling.server.util.db.CurrencyDTO.list()}"
								optionKey="id" optionValue="description"
								value="${user?.currencyId}" />
						</div>
					</div>
					<div class="row">
						<label>&nbsp;</label>
						<g:checkBox class="cb" name="excludeFromAgeing" checked=""/>
						<label for="excludeFromAgeing" class="lb"><g:message code="prompt.exclude.ageing" /></label>
					</div>
				</div>
				<div class="column">
					<div class="row">
						<label><g:message
									code="prompt.contact.type" /></label>
						<div class="inp-bg">
							${user?.contact?.type}
						</div>
					</div>
					<div class="row">
						<label><g:message code="prompt.organization.name" /></label>
						<div class="inp-bg">
							<g:textField class="field" name="contact.organizationName"
								value="${user?.contact?.organizationName}" />
						</div>
					</div>
					
					<div class="row">
						<label><g:message
									code="prompt.first.name" /></label>
						<div class="inp-bg">
							<g:textField class="field" name="contact.firstName"
										value="${user?.contact?.firstName}" />
						</div>
					</div>
					
					<div class="row">
						<label><g:message
									code="prompt.last.name" /></label>
						<div class="inp-bg">
							<g:textField class="field" name="contact.lastName"
									value="${user?.contact?.lastName}" />
						</div>
					</div>
					<div class="row">
						<label><g:message code="prompt.phone.number" /></label>
						<div class="inp-bg">
							<g:textField class="field" name="contact.phoneCountryCode"
								value="${user?.contact?.phoneCountryCode}" size="5" /> -
							<g:textField class="field" name="contact.phoneAreaCode"
								value="${user?.contact?.phoneAreaCode}" size="5" /> -
							<g:textField class="field" name="contact.phoneNumber"
								value="${user?.contact?.phoneNumber}" size="12" />
						</div>
					</div>
					<div class="row">
						<label><g:message
									code="prompt.email.address" /></label>
						<div class="inp-bg">
							<g:textField class="field" name="contact.email"
								value="${user?.contact?.email}" />
						</div>
					</div>
					<div class="row">
						<label><g:message
									code="prompt.address1" /></label>
						<div class="inp-bg">
							<g:textField class="field" name="contact.address1"
									value="${user?.contact?.address1}" />
						</div>
					</div>
					<div class="row">
						<label><g:message
									code="prompt.address2" /></label>
						<div class="inp-bg">
							<g:textField class="field" name="contact.address2"
									value="${user?.contact?.address2}" />
						</div>
					</div>
					<div class="row">
						<label><g:message
									code="prompt.city" /></label>
						<div class="inp-bg">
							<g:textField class="field" name="contact.city"
									value="${user?.contact?.city}" />
						</div>
					</div>
					<div class="row">
						<label><g:message code="prompt.state" /></label>
						<div class="inp-bg">
							<g:textField class="field" name="contact.stateProvince"
									value="${user?.contact?.stateProvince}" />
						</div>	
					</div>
					<div class="row">
						<label><g:message
									code="prompt.zip" /></label>
						<div class="inp-bg">
							<g:textField class="field" name="contact.postalCode"
									value="${user?.contact?.postalCode}" />
						</div>
					</div>
					<div class="row">
						<label><g:message code="prompt.country" /></label>
						<div class="inp-bg">
							<g:textField class="field" name="contact.countryCode"
									value="${user?.contact?.countryCode}" />
						</div>
					</div>
					<div class="row">
						<label>&nbsp;</label>
						<g:checkBox name="includeInNotifications" class="cb" checked="" />
						<label for="includeInNotifications" class="lb">
							<g:message code="prompt.include.in.notifications" /></label>
					</div>
					
				</div>
			</div>
			
			<!-- box cards -->
			<div class="box-cards">
				<div class="box-cards-title">
					<a class="btn-open" href="#"><span><g:message code="prompt.credit.card"/></span></a>
				</div>
				<div class="box-card-hold">
					<div class="form-columns">
						<div class="column">
							<div class="row">
								<label><g:message code="prompt.credit.card"/>:</label>
								<div class="inp-bg"><g:textField class="field" name="creditCard.number"
									value="${user?.creditCard?.number}" /></div>
							</div>
							<div class="row">
								<label><g:message
									code="prompt.name.on.card" />:</label>
								<div class="inp-bg"><g:textField class="field" name="creditCard.name"
									value="${user?.creditCard?.name}" /></div>
							</div>
							<div class="row">
								<label><g:message code="prompt.expiry.date" />:</label>
								<div class="sel">
									<g:textField class="field" name="creditCard.month" size="2"
										value="${expiryMonth}" />-
								</div>
								<div class="sel">
									<g:textField class="field" name="creditCard.year"
										size="4" value="${expiryYear}" />
								</div>
							</div>
						</div>
						<div class="column">
							<div class="row">
								<g:checkBox class="cb check" name="isAutomaticPaymentCC"  
									checked="${isAutoCC}" onclick="toggle(this)"/>
								<label for="isAutomaticPaymentCC" class="lb"><g:message
									code="prompt.preferred.auto.payment" /></label>
							</div>
						</div>
					</div>
				</div>
			</div>

			<!-- box cards -->
			<div class="box-cards box-cards-open">
				<div class="box-cards-title">
					<a class="btn-open" href="#"><span><g:message code="prompt.ach"/></span></a>
				</div>
				<div class="box-card-hold">
					<div class="form-columns">
						<div class="column">
							<div class="row">
								<label><g:message
									code="prompt.aba.routing.num" />:</label>
								<div class="inp-bg inp4"><g:textField class="field" name="ach.abaRouting"
									value="${user?.ach?.abaRouting}" /></div>
							</div>
							<div class="row">
								<label><g:message code="prompt.bank.acc.num" />:</label>
								<div class="inp-bg"><g:textField class="field" name="ach.bankAccount"
									value="${user?.ach?.bankAccount}" /></div>
							</div>
							<div class="row">
								<label><g:message code="prompt.bank.name" />:</label>
								<div class="inp-bg"><g:textField class="field" name="ach.bankName"
									value="${user?.ach?.bankName}" /></div>
							</div>
							<div class="row">
								<label><g:message
									code="prompt.name.customer.account" />:</label>
								<div class="inp-bg"><g:textField class="field" name="ach.accountName"
									value="${user?.ach?.accountName}" /></div>
							</div>
							<div class="row">
								<label><g:message
									code="prompt.account.type" />:</label>
								<input checked="${((user?.ach?.accountType == 1)?"checked":"")}" type="radio" value="1" class="rb" name="ach.accountType" id="rb1" />
								<label for="rb1" class="rb"><g:message code="label.account.checking"/></label>
								<input checked="${((user?.ach?.accountType == 2)?"checked":"")}" type="radio" value="2" class="rb" name="ach.accountType" id="rb2" />
								<label for="rb2" class="rb"><g:message code="label.account.savings"/></label>
							</div>
						</div>
						<div class="column">
							<div class="row">
								<g:checkBox name="isAutomaticPaymentAch" checked="${isAutoAch}" class="cb check" onclick="toggle(this)"/>
								<label for="cb3" class="lb"><g:message 
									code="prompt.preferred.auto.payment" /></label>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- box text -->
			<div class="box-text">
				<g:textArea id='notes' value="${notes}" name="notes"
							rows="8" cols="90"/>
			</div>

			<div class="buttons">
				<ul>
					<li>
						<a href="${createLink(action: 'postEdit')}" class="submit save">
							<span><g:message code="button.save"/></span></a>
					</li>
					<li>
						<a href="${createLink(action: 'cancel')}" class="submit cancel">
							<span><g:message code="button.cancel"/></span></a>
					</li>
				</ul>
			</div>

		</fieldset>
		</g:form>
	</div>
</div>
</body>
</html>