<%@ page import="com.sapienter.jbilling.server.user.db.CompanyDTO" %>

<div id="details-box">
    <div class="form-columns">
        <g:applyLayout name="form/text">
            <content tag="label">User ID</content>
            2
        </g:applyLayout>

        <g:applyLayout name="form/text">
            <content tag="label">Login Name</content>
            gandalf
        </g:applyLayout>

        <g:applyLayout name="form/checkbox">
            <content tag="label">Notify When Order Expires</content>
            <content tag="label.for">notifyOnExpiry</content>
            <g:checkBox class="cb checkbox" name="notifyOnExpiry"/>
        </g:applyLayout>
    </div>

    <hr/>

    <div class="form-columns">
        <g:applyLayout name="form/select">
            <content tag="label">Period</content>
            <g:select from="${CompanyDTO.get(session['company_id']).orderPeriods.collect{ it.periodUnit }}"
                    optionKey="id" optionValue="description"
                    name="period"/>
        </g:applyLayout>

        <g:applyLayout name="form/select">
            <content tag="label">Type</content>
            <g:select from="['Post-Paid', 'Pre-Paid']"
                    name="type"/>
        </g:applyLayout>

        <g:applyLayout name="form/date">
            <content tag="label">Cycle Start</content>
            <content tag="label.for">cycleStart</content>
            <g:textField class="field" name="cycleStart"/>
        </g:applyLayout>

        <g:applyLayout name="form/date">
            <content tag="label">Cycle End</content>
            <content tag="label.for">cycleEnd</content>
            <g:textField class="field" name="cycleEnd"/>
        </g:applyLayout>

        <g:applyLayout name="form/date">
            <content tag="label">Active Until</content>
            <content tag="label.for">activeUntil</content>
            <g:textField class="field" name="activeUntil"/>
        </g:applyLayout>

        <g:applyLayout name="form/checkbox">
            <content tag="label">Main Subscription</content>
            <content tag="label.for">mainSubscription</content>
            <g:checkBox class="cb checkbox" name="mainSubscription"/>
        </g:applyLayout>
    </div>

    <hr/>

    <div class="form-columns">
        <div class="box-text">
            <label class="lb">Notes</label>
            <g:textArea name="notes" rows="5" cols="60"/>
        </div>

        <g:applyLayout name="form/checkbox">
            <content tag="label">Include Notes on Invoice</content>
            <content tag="label.for">notesOnInvoice</content>
            <g:checkBox class="cb checkbox" name="notesOnInvoice"/>
        </g:applyLayout>
    </div>
</div>