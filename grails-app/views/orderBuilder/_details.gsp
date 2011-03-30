<%@ page import="com.sapienter.jbilling.server.util.Constants" %>

<%--
  Order details form. Allows editing of primary order attributes.

  @author Brian Cowdery
  @since 23-Jan-2011
--%>

<div id="details-box">
    <g:formRemote name="order-details-form" url="[action: 'edit']" update="column2" method="GET">
        <g:hiddenField name="_eventId" value="update"/>
        <g:hiddenField name="execution" value="${flowExecutionKey}"/>

        <div class="form-columns">
            <g:set var="hasPlan" value="${order.orderLines.find{ l -> plans.find{ p -> p.id == l.itemId }} != null}"/>
            <g:applyLayout name="form/select">
                <content tag="label"><g:message code="order.label.period"/></content>
                <content tag="label.for">period</content>
                <g:select from="${orderPeriods}"
                          optionKey="id" optionValue="${{it.getDescription(session['language_id'])}}"
                          name="period"
                          value="${order?.period}" disabled="${hasPlan}"/>
            </g:applyLayout>

            <g:applyLayout name="form/select">
                <content tag="label"><g:message code="order.label.billing.type"/></content>
                <content tag="label.for">billingTypeId</content>
                <g:select from="${orderBillingTypes}"
                          optionKey="id" optionValue="${{it.getDescription(session['language_id'])}}"
                          name="billingTypeId"
                          value="${order?.billingTypeId}"/>
            </g:applyLayout>

            <g:applyLayout name="form/select">
                <content tag="label"><g:message code="order.label.status"/></content>
                <content tag="label.for">statusId</content>
                <g:select from="${orderStatuses}"
                          optionKey="statusValue" optionValue="${{it.getDescription(session['language_id'])}}"
                          name="statusId"
                          value="${order?.statusId}" 
                          disabled="${(!order.id || order.id == 0)}"/>
            </g:applyLayout>

            <g:applyLayout name="form/date">
                <content tag="label"><g:message code="order.label.active.since"/></content>
                <content tag="label.for">activeSince</content>
                <g:textField class="field" name="activeSince" value="${formatDate(date: order?.activeSince, formatName: 'datepicker.format')}"/>
            </g:applyLayout>

            <g:applyLayout name="form/date">
                <content tag="label"><g:message code="order.label.active.until"/></content>
                <content tag="label.for">activeUntil</content>
                <g:textField class="field" name="activeUntil" value="${formatDate(date: order?.activeUntil, formatName: 'datepicker.format')}"/>
            </g:applyLayout>

            <g:applyLayout name="form/date">
                <content tag="label"><g:message code="order.label.cycle.start"/></content>
                <content tag="label.for">cycleStarts</content>
                <g:textField class="field" name="cycleStarts" value="${formatDate(date: order?.cycleStarts, formatName: 'datepicker.format')}"/>
            </g:applyLayout>

            <g:applyLayout name="form/text">
                <content tag="label"><g:message code="prompt.due.date.override"/></content>
                <content tag="label.for">dueDateValue</content>

                <div class="inp-bg inp4">
                    <g:textField class="field" name="dueDateValue" value="${order?.dueDateValue}"/>
                </div>
                <div class="select4">
                    <g:select from="${periodUnits}"
                              optionKey="id"
                              optionValue="${{it.getDescription(session['language_id'])}}"
                              name="dueDateUnitId"
                              value="${order?.dueDateUnitId ?: Constants.PERIOD_UNIT_DAY}"/>
                </div>
            </g:applyLayout>

            <g:applyLayout name="form/checkbox">
                <content tag="label"><g:message code="order.label.main.subscription"/></content>
                <content tag="label.for">mainSubscription</content>
                <g:checkBox class="cb checkbox" name="isCurrent" value="${order?.isCurrent > 0}"/>
            </g:applyLayout>

            <g:applyLayout name="form/checkbox">
                <content tag="label"><g:message code="order.label.notify.on.expire"/></content>
                <content tag="label.for">notify</content>
                <g:checkBox class="cb checkbox" name="notify" checked="${order?.notify > 0}"/>
            </g:applyLayout>
        </div>

        <hr/>

        <div class="form-columns">
            <div class="box-text">
                <label class="lb"><g:message code="prompt.notes"/></label>
                <g:textArea name="notes" rows="5" cols="60" value="${order?.notes}"/>
            </div>

            <g:applyLayout name="form/checkbox">
                <content tag="label"><g:message code="order.label.include.notes"/></content>
                <content tag="label.for">notesInInvoice</content>
                <g:checkBox class="cb checkbox" name="notesInInvoice" value="${order?.notesInInvoice > 0}"/>
            </g:applyLayout>
        </div>
    </g:formRemote>

    <script type="text/javascript">
        var statusIdPrevValue = ${order?.statusId};
        $(function() {
            $('#period').change(function() {
                if ($(this).val() == ${Constants.ORDER_PERIOD_ONCE}) {
                    $('#billingTypeId').val(${Constants.ORDER_BILLING_POST_PAID});
                    $('#billingTypeId').attr('disabled', true);
                } else {
                    $('#billingTypeId').attr('disabled', '');
                }
            }).change();

            $('#statusId').change(function() {
            	if ($(this).val() == ${Constants.ORDER_STATUS_SUSPENDED}) {
                    //('Are you sure');
            		showConfirm('edit-' + ${order?.id});
                    //return true;
                } else {
                	statusIdPrevValue= $(this).val();
                }
            })
            
            $('#order-details-form').find(':text.hasDatepicker, select, :checkbox').change(function() {
                $('#order-details-form').submit();
            });

            $('#order-details-form').find('textarea, :text').blur(function() {
                $('#order-details-form').submit();
            });
        });
    </script>
</div>


<g:render template="/confirm" 
	   model="['message': 'order.prompt.set.suspended',
       'controller': 'orderBuilder',
       'action': 'edit',
       'id': order?.id,
       'ajax': false,
       'onNo': '$(\'#statusId\').val(statusIdPrevValue)',
       'onYes': '$(\'#confirm-dialog-edit-${order?.id}\').dialog(\'close\')'
     ]"/>
