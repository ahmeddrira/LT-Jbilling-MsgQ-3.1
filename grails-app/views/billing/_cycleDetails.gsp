<%@page import="com.sapienter.jbilling.server.process.BillingProcessBL;com.sapienter.jbilling.common.CommonConstants;com.sapienter.jbilling.server.util.Util"%>
<g:set var="dtFmt" value="${new java.text.SimpleDateFormat('dd-MMM-yyyy')}"/>
<div class="table-info" >
    <em><g:message code="billing.details.label.process.id"/>: 
        <strong> ${process.id} </strong> 
    </em> 
    <em><g:message code="billing.details.label.start.date"/>: 
        <strong> ${dtFmt.format(process.billingDate)} </strong></em> 
    <em><g:message code="billing.details.label.end.date"/>: 
        <strong> ${dtFmt.format(BillingProcessBL.getEndOfProcessPeriod(process))}</strong></em> 
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
        <g:set var="dtFmt" value="${new java.text.SimpleDateFormat('dd MMM yyyy')}"/>
        <g:set var="timeFmt" value="${new java.text.SimpleDateFormat('hh:mm:ss a')}"/>
        <g:set var="ttlInvcd" value="${new BigDecimal(0)}"/>
        <g:set var="ttlSuccessAmt" value="${new BigDecimal(0)}"/>
        <g:set var="ttlFailedAmt" value="${new BigDecimal(0)}"/>
        
        <g:each status="idx" var="cur" in="${countAndSumByCurrency}">
            <tr>
                <g:if test="${idx == 0}">
                    <td class="col02">${dtFmt.format(run.started)}<br>${timeFmt.format(run.started) }</td>
                    <td>${dtFmt.format(run.finished)}<br>${timeFmt.format(run.finished) }</td>
                    <td>
                        <g:if test="${run.paymentFinished != null}">
                            ${dtFmt.format(run?.paymentFinished)}<br>${timeFmt.format(run?.paymentFinished)}
                        </g:if>
                    </td>
                    <td>${run?.status?.getDescription(session.language_id) }</td>
                    <td>${run?.invoicesGenerated}</td>
                </g:if>
                <g:else>
                    <td class="col02"></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </g:else>
                
                <td></td>
                <td class="col01">
                    <g:each var="pymArr" in="${mapOfPaymentListByCurrency?.get( cur[2]?.getId() as Integer )}">
                        <!-- Compute Total Paid (Sum of all successful payments for all currencies) -->
                        <g:set var="ttlSuccessAmt" value="${(ttlSuccessAmt as BigDecimal).add( (pymArr as Object[])[1] as BigDecimal )}"/>
                        <em>${Util.formatMoney( ((pymArr as Object[])[1] as BigDecimal) ?: ("0.0" as BigDecimal),
                                session["user_id"], cur[2]?.getId() as Integer, false)?.substring(2)}</em>
                    </g:each>
                </td>
                <td>
                    <g:each var="pymArr" in="${mapOfPaymentListByCurrency?.get(cur[2]?.getId() as Integer)}">
                        <em>${(pymArr as Object[])[0]?.getDescription(session.language_id)}</em>
                    </g:each>
                </td>
                <td></td>
                <td>${cur[2]?.getDescription(session.language_id)}</td>
            </tr>
            <g:set var="ttlInvcd" value="${(ttlInvcd as BigDecimal).add(cur[1] as BigDecimal)}"/>
        </g:each>
        
        <!-- Compute Total Not Paid (Sum of all failed payments) -->
        <g:each var="failedAmt" in="${failedAmountsByCurrency}">
            <g:set var="ttlFailedAmt" value="${(ttlFailedAmt as BigDecimal).add(failedAmt as BigDecimal)}"/>
        </g:each>
        
        <tr class="bg">
            <td class="col02"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td><strong><!-- Total Invoiced -->
                    ${Util.formatMoney( ttlInvcd ?: ("0.0" as BigDecimal),
                        session["user_id"], 1, false)?.substring(2)}
	        </strong></td>
            <td class="col01"><em>
                    ${Util.formatMoney( ttlSuccessAmt ?: ("0.0" as BigDecimal),
                        session["user_id"], 1, false)?.substring(2)}
            </em></td>
            <td></td>
            <td><strong>${Util.formatMoney( ttlFailedAmt ?: ("0.0" as BigDecimal),
                        session["user_id"], 1, false)?.substring(2)}</strong></td>
            <td></td>
        </tr>
    </g:each>
    </tbody>
</table>
</div>