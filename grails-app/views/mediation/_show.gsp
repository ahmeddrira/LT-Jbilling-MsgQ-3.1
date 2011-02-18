<%@ page import="com.sapienter.jbilling.server.util.Constants;" %>
<%--
    @author Vikas Bodani
    @since 18 Feb 2011
 --%>
<div class="column-hold">

    <div class="heading">
        <strong><g:message code="label.mediation.details"/> <em>${processId}</em>
        </strong>
    </div>
 
    <div class="box">
        <table class="dataTable">
            <tr>
                <td>
                    <g:message code="label.mediation.done.billable"/>:
                </td>
                <td class="value">
                    ${map.get(Constants.MEDIATION_RECORD_STATUS_DONE_AND_BILLABLE)?:'0'}
                </td>
            </tr>
            <tr>
                <td>
                    <g:message code="label.mediation.done.not.billable"/>:
                </td>
                <td class="value">
                    ${map.get(Constants.MEDIATION_RECORD_STATUS_DONE_AND_NOT_BILLABLE)?:'0'}
                </td>
            </tr>
            <tr>
                <td>
                    <g:message code="label.mediation.errors.detected"/>:
                </td>
                <td class="value">
                    ${map.get(Constants.MEDIATION_RECORD_STATUS_ERROR_DETECTED)?:'0'}
                </td>
            </tr>
            <tr>
                <td>
                    <g:message code="label.mediation.errors.declared"/>:
                </td>
                <td class="value">
                    ${map.get(Constants.MEDIATION_RECORD_STATUS_ERROR_DECLARED)?:'0'}
                </td>
            </tr>
        </table>
    </div>
    <div class="btn-box"></div>
</div>