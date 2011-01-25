<%@ page import="com.sapienter.jbilling.server.util.Constants; com.sapienter.jbilling.server.user.db.CompanyDTO" %>

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
            <g:applyLayout name="form/select">
                <content tag="label">Period</content>
                <content tag="label.for">period</content>
                <g:select from="${orderPeriods}"
                          optionKey="id" optionValue="${{it.getDescription(session['language_id'])}}"
                          name="period"
                          value="${order?.period}"/>
            </g:applyLayout>

            <g:applyLayout name="form/select">
                <content tag="label">Type</content>
                <content tag="label.for">billingTypeId</content>
                <g:select from="${orderBillingTypes}"
                          optionKey="id" optionValue="${{it.getDescription(session['language_id'])}}"
                          name="billingTypeId"
                          value="${order?.billingTypeId}"/>
            </g:applyLayout>

            <g:applyLayout name="form/date">
                <content tag="label">Active Since</content>
                <content tag="label.for">activeSince</content>
                <g:textField class="field" name="activeSince" value="${formatDate(date: order?.activeSince, formatName: 'datepicker.format')}"/>
            </g:applyLayout>

            <g:applyLayout name="form/date">
                <content tag="label">Active Until</content>
                <content tag="label.for">activeUntil</content>
                <g:textField class="field" name="activeUntil" value="${formatDate(date: order?.activeUntil, formatName: 'datepicker.format')}"/>
            </g:applyLayout>

            <g:applyLayout name="form/date">
                <content tag="label">Cycle Start</content>
                <content tag="label.for">cycleStarts</content>
                <g:textField class="field" name="cycleStarts" value="${formatDate(date: order?.cycleStarts, formatName: 'datepicker.format')}"/>
            </g:applyLayout>

            <g:applyLayout name="form/checkbox">
                <content tag="label">Main Subscription</content>
                <content tag="label.for">mainSubscription</content>
                <g:checkBox class="cb checkbox" name="mainSubscription"/>
            </g:applyLayout>

            <g:applyLayout name="form/checkbox">
                <content tag="label">Notify User When Order Expires</content>
                <content tag="label.for">notify</content>
                <g:checkBox class="cb checkbox" name="notify" checked="${order?.notify > 0}"/>
            </g:applyLayout>
        </div>

        <hr/>

        <div class="form-columns">
            <div class="box-text">
                <label class="lb">Notes</label>
                <g:textArea name="notes" rows="5" cols="60" value="${order?.notes}"/>
            </div>

            <g:applyLayout name="form/checkbox">
                <content tag="label">Include Notes on Invoice</content>
                <content tag="label.for">notesInInvoice</content>
                <g:checkBox class="cb checkbox" name="notesInInvoice" value="${order?.notesInInvoice > 0}"/>
            </g:applyLayout>
        </div>
    </g:formRemote>

    <script type="text/javascript">
        $(function() {
            $('#order-details-form :checkbox, #order-details-form select').change(function() {
                $('#order-details-form').submit();
            });
            $('#order-details-form :input').not(':password,:checkbox,:radio,select').blur(function() {
                $('#order-details-form').submit();
            });
        });
    </script>
</div>


