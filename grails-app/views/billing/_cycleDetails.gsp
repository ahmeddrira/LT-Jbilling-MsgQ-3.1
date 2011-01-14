<%@page import="com.sapienter.jbilling.server.process.BillingProcessBL;com.sapienter.jbilling.common.CommonConstants;com.sapienter.jbilling.server.util.Util"%>

<div class="table-info" >
    <em><g:message code="billing.details.label.process.id"/>: 
        <strong> ${process.id} </strong> 
    </em> 
    <em><g:message code="billing.details.label.start.date"/>: 
        <strong> ${new java.text.SimpleDateFormat("dd-MMM-yyyy").format(process.billingDate)} </strong></em> 
    <em><g:message code="billing.details.label.end.date"/>: 
        <strong> ${new java.text.SimpleDateFormat("dd-MMM-yyyy").format(BillingProcessBL.getEndOfProcessPeriod(process))}</strong></em> 
    <em><g:message code="billing.details.label.number.runs" />: 
        <strong>${process?.processRuns?.size()} </strong></em>
</div>

<div class="table-area">
<table>
    <thead>
        <tr>
            <td class="first"><g:message code="billing.details.label.start.date.short"/>&nbsp;&nbsp;</td>
            <td><g:message code="billing.details.label.end.date.short"/>&nbsp;</td>
            <td><g:message code="billing.details.label.payment.processing.ended"/></td>
            <td><g:message code="billing.details.label.process.result"/></td>
            <td><g:message code="billing.details.label.invoices.generated"/></td>
            <td><g:message code="billing.details.label.total.invoiced"/></td>
            <td class="col04"><g:message code="billing.details.label.total.paid"/></td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td><g:message code="billing.details.label.total.not.paid"/></td>
            <td class="last"><g:message code="billing.details.label.currency"/></td>
        </tr>
    </thead>
    <tbody>
    <g:each var="run" in="${process.processRuns}">
        <g:set var="dateFormat" value="${new java.text.SimpleDateFormat('dd MMM yyyy')}"/>
        <g:set var="timeFormat" value="${new java.text.SimpleDateFormat('hh:mm:ss a')}"/>
        <g:set var="flashTotal" value="${new BigDecimal(0)}"/>
        <%--
        <g:each var="prttl" in="${process.processRuns.processRunTotals}">
            ${prttl[0].currency.getDescription(session.language_id)}
        </g:each>
         --%>
        <tr>
            <td class="col02">${dateFormat.format(run.started)}<br>${timeFormat.format(run.started) }</td>
            <td>${dateFormat.format(run.finished)}<br>${timeFormat.format(run.finished) }</td>
            <td>
                <g:if test="${run.paymentFinished != null}">
                    ${dateFormat.format(run?.paymentFinished)}<br>${timeFormat.format(run?.paymentFinished)}
                </g:if>
            </td>
            <td>${run?.status?.getDescription(session.language_id) }</td>
            <td>${run?.invoicesGenerated}</td>
            <td></td>
            <td class="col01">
                <em>$ 90.65</em> <em>$ 8807.10</em> 
                <em>$5307.70</em> <em>$ 10063.64</em>
            </td>
            <td>
                <em>Discovery</em> <em>Visa</em> 
                <em>Mastercard</em> <em>Amex</em>
            </td>
            <td></td>
            <td>US Dollar</td>
        </tr>
        <tr class="bg">
            <td class="col02"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td><strong>$ 67796.61</strong></td>
            <td class="col01"><em>$ 43527.64</em></td>
            <td></td>
            <td><strong>$ 24269.01</strong></td>
            <td></td>
        </tr>
    </g:each>
    </tbody>
</table>
</div>