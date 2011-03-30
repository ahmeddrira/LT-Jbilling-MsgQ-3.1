<%@ page import="org.joda.time.DateMidnight" %>

<%--
  Parameters for the GL Detail report.

  @author Brian Cowdery
  @since  30-Mar-2011
--%>

<div class="form-columns">
    <g:applyLayout name="form/date">
        <content tag="label"><g:message code="end_date"/></content>
        <content tag="label.for">date</content>
        <g:textField class="field" name="date" value="${formatDate(date: new Date(), formatName: 'datepicker.format')}"/>
    </g:applyLayout>
</div>