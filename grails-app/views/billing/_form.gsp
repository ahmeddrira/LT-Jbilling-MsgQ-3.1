
<div class="form-edit">

    <div class="heading">
        <strong><g:message code="configuration.title.billing"/></strong>
    </div>

    <div class="form-hold">
        <g:form name="save-billing-form" action="saveConfig">
            <fieldset>
                <div class="form-columns">
                    <div class="column">
                        <g:applyLayout name="form/input">
                            <content tag="label"><g:message code="billing.next.run.date"/></content>
                            <content tag="label.for">nextRunDate</content>
                            <g:textField class="field" name="nextRunDate" value="${configuration?.nextRunDate}" size="10"/>
                        </g:applyLayout>

                        <g:applyLayout name="form/checkbox">
                            <content tag="label"><g:message code="billing.generate.report"/></content>
                            <content tag="label.for">generateReport</content>
                            <g:checkBox class="cb checkbox" name="generateReport" checked="${configuration?.generateReport > 0}"/>
                        </g:applyLayout>
                        
                        <g:applyLayout name="form/input">
                            <content tag="label"><g:message code="billing.days.to.review"/></content>
                            <content tag="label.for">daysForReport</content>
                            <g:textField class="field" name="daysForReport" value="${configuration?.daysForReport}" size="2"/>
                        </g:applyLayout>
                        
                        <g:applyLayout name="form/input">
                            <content tag="label"><g:message code="billing.number.retries"/></content>
                            <content tag="label.for">retries</content>
                            <g:textField class="field" name="retries" value="${configuration?.retries}" size="2"/>
                        </g:applyLayout>
                        
                        <g:applyLayout name="form/input">
                            <content tag="label"><g:message code="billing.days.for.retry"/></content>
                            <content tag="label.for">daysForRetry</content>
                            <g:textField class="field" name="daysForRetry" value="${configuration?.daysForRetry}" size="2"/>
                        </g:applyLayout>
                        
                        <g:applyLayout name="form/input">
                            <content tag="label"><g:message code="billing.period"/></content>
                            <content tag="label.for">period</content>
                            <g:textField class="field" name="period" value="${configuration?.period}" size="10"/>
                        </g:applyLayout>
                        
                        <g:applyLayout name="form/input">
                            <content tag="label"><g:message code="billing.due.date"/></content>
                            <content tag="label.for">dueDateValue</content>
                            <g:textField class="field" name="dueDateValue" value="${configuration?.dueDateValue}" size="10"/>
                        </g:applyLayout>
                        
                        <g:applyLayout name="form/checkbox">
                            <content tag="label"><g:message code="billing.require.recurring"/></content>
                            <content tag="label.for">onlyRecurring</content>
                            <g:checkBox class="cb checkbox" name="onlyRecurring" checked="${configuration?.onlyRecurring > 0}"/>
                        </g:applyLayout>
                        
                        <g:applyLayout name="form/checkbox">
                            <content tag="label"><g:message code="billing.use.process.date"/></content>
                            <content tag="label.for">invoiceDateProcess</content>
                            <g:checkBox class="cb checkbox" name="invoiceDateProcess" checked="${configuration?.invoiceDateProcess > 0}"/>
                        </g:applyLayout>
                        
                        <g:applyLayout name="form/checkbox">
                            <content tag="label"><g:message code="billing.auto.payment"/></content>
                            <content tag="label.for">autoPayment</content>
                            <g:checkBox class="cb checkbox" name="autoPayment" checked="${configuration?.autoPayment > 0}"/>
                        </g:applyLayout>
                        
                        <g:applyLayout name="form/input">
                            <content tag="label"><g:message code="billing.maximum.period"/></content>
                            <content tag="label.for">maximumPeriods</content>
                            <g:textField class="field" name="maximumPeriods" value="${configuration?.maximumPeriods}" size="2"/>
                        </g:applyLayout>
                        
                        <g:applyLayout name="form/checkbox">
                            <content tag="label"><g:message code="billing.auto.payment.application"/></content>
                            <content tag="label.for">autoPaymentApplication</content>
                            <g:checkBox class="cb checkbox" name="autoPaymentApplication" 
                            	checked="${configuration?.autoPaymentApplication > 0}"/>
                        </g:applyLayout>
                        
                    </div>
                </div>
                
                <!-- spacer -->
                <div>
                    <br/>&nbsp;
                </div>

                <div class="buttons">
                    <ul>
                        <li><a onclick="$('#save-billing-form').submit();" class="submit save"><span><g:message code="button.save"/></span></a></li>
                        <li><g:link controller="config" action="index" class="submit cancel"><span><g:message code="button.cancel"/></span></g:link></li>
                    </ul>
                </div>
                
            </fieldset>
        </g:form>
    </div>
</div>