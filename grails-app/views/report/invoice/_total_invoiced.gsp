<%@ page import="org.joda.time.DateMidnight" %>

<%--
  Parameters for the Total Invoiced Amount report.

  @author Brian Cowdery
  @since  08-Mar-2011
--%>

<div class="form-columns">
    <g:applyLayout name="form/date">
        <content tag="label"><g:message code="start_date"/></content>
        <content tag="label.for">start_date</content>
        <g:textField class="field" name="start_date" value="${formatDate(date: new DateMidnight().minusMonths(1).toDate(), formatName: 'datepicker.format')}"/>
    </g:applyLayout>

    <g:applyLayout name="form/date">
        <content tag="label"><g:message code="end_date"/></content>
        <content tag="label.for">end_date</content>
        <g:textField class="field" name="end_date" value="${formatDate(date: new Date(), formatName: 'datepicker.format')}"/>
    </g:applyLayout>

    <g:applyLayout name="form/select">
        <content tag="label"><g:message code="period"/></content>
        <content tag="label.for">period</content>
        <g:select name="period" from="[1, 2, 3]" valueMessagePrefix="period"/>
    </g:applyLayout>
</div>