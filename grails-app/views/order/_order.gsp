<%@ page import="com.sapienter.jbilling.server.util.Util" %>
<%@ page import="com.sapienter.jbilling.client.util.Constants" %>
<%@ page import="com.sapienter.jbilling.server.item.db.ItemDTO; com.sapienter.jbilling.server.item.db.ItemDAS"%>

<div class="column-hold">

    <div class="heading">
        <strong><g:message code="order.label.details"/>&nbsp;<em>${order?.id}</em></strong>
    </div>
    
    <!-- Order Details -->
    <div class="box">
        <table class="dataTable">
            <tr><td><strong>
                    <g:if test="${user?.contact?.firstName || user?.contact?.lastName}">
                        ${user.contact.firstName}&nbsp;${user.contact.lastName}
                    </g:if>
                    <g:else>
                        ${user?.userName}
                    </g:else>
                </strong><br>
                <em>${user?.contact?.organizationName}</em>
            </td></tr>
            <tr>
                <td><g:message code="order.label.user.id"/>:</td>
                <td class="value">
                    <g:remoteLink controller="user" action="show" id="${user?.id}" before="register(this);" onSuccess="render(data, next);">
                        ${user?.id}
                    </g:remoteLink>
                </td>
            </tr>
            <tr>
                <td><g:message code="order.label.user.name" />:</td>
                <td class="value">${user?.userName}</td>
            </tr>
        </table>
        
        <table class="dataTable">
            <tr><td><g:message code="order.label.create.date"/>:</td>
                <td class="value">
                    <g:formatDate format="MMM-dd-yyyy" date="${order?.createDate}"/>
                </td>
            </tr>
            <tr><td><g:message code="order.label.active.since"/>:</td>
                <td class="value">
                    <g:formatDate format="MMM-dd-yyyy" date="${order?.activeSince}"/>
                </td>
            </tr>
            <tr><td><g:message code="order.label.active.until"/>:</td>
                <td class="value">
                    <g:formatDate format="MMM-dd-yyyy" date="${order?.activeUntil}"/>
                </td>
            </tr>
            <tr><td><g:message code="order.label.next.invoice"/>:</td>
                <td class="value">
                    <g:if test="${order?.nextBillableDay}">
                        <g:formatDate format="MMM-dd-yyyy" date="${order?.nextBillableDay}"/>
                    </g:if>
                    <g:else>
                        <g:formatDate format="MMM-dd-yyyy" date="${order?.createDate}"/>
                    </g:else>
                </td>
            </tr>
            <tr><td><g:message code="order.label.period"/>:</td><td class="value">${order.periodStr}</td></tr>
            <tr><td><g:message code="order.label.total"/>:</td>
                    <td class="value">${Util.formatMoney(order.total as BigDecimal,
                        session["user_id"],
                        order.currencyId, 
                        false)?.substring(2)}</td></tr>
            <tr><td><g:message code="order.label.status"/>:</td>
                <td class="value">${order?.statusStr}</td></tr>
        </table>
    </div>
    
    <div class="heading">
        <strong><g:message code="order.label.notes"/></strong>
    </div>
    
    <!-- Order Notes -->
    <div class="box">
        <g:if test="${order?.notes}">
                <table class="innerTable">
                    <tbody>
                        <tr>
                            <td class="innerContent" style="text-align: left">
                                ${order?.notes}
                            </td>
                        </tr>
                    </tbody>
                </table>
        </g:if>
        <g:else>
            <em><g:message code="order.prompt.no.notes"/></em>
        </g:else>
    </div>
    
    <div class="heading">
        <strong><g:message code="order.label.lines"/></strong>
    </div>
    
    <!-- Order Lines -->
    <div class="box">
        <g:if test="${order?.orderLines}">
            <table class="innerTable" >
                <thead class="innerHeader">
                     <tr>
                        <th><g:message code="order.label.line.item"/></th>
                        <th><g:message code="order.label.line.descr"/></th>
                        <th><g:message code="order.label.line.qty"/></th>
                        <th><g:message code="order.label.line.price"/></th>
                        <th><g:message code="order.label.line.total"/></th>
                     </tr>
                 </thead>
                 <tbody>
                     <g:each var="line" in="${order.orderLines}" status="idx">
                         <tr>
                            <td class="innerContent">
                                <g:set var="itemDto" value="${new ItemDAS().find(line?.itemId)}"/>
                                <g:if test="${itemDto?.plans?.size() == 0}">
                                   <g:remoteLink controller="product" action="show" id="${line?.itemId}" params="['template': 'show']"
                                        before="register(this);" onSuccess="render(data, next);">
                                        ${line?.itemId}
                                   </g:remoteLink>
                                </g:if>
                                <g:else>
                                    <g:set var="planId" value="${itemDto?.plans?.iterator().next()?.id}" />
                                    <g:remoteLink controller="plan" action="show" id="${planId}" params="['template': 'show']"
                                        before="register(this);" onSuccess="render(data, next);">
                                        ${planId}
                                   </g:remoteLink>
                                </g:else>
                            </td>
                            <td class="innerContent">${line.description}</td>
                            <td class="innerContent">${new BigDecimal(line.quantity?: "0.0").intValue()}</td>
                            <td class="innerContent">${Util.formatMoney( new BigDecimal(line?.price?:"0.0"),
                                session["user_id"],
                                order.currencyId, 
                                false)?.substring(2)}</td>
                            <td class="innerContent">${Util.formatMoney( new BigDecimal(line?.amount?:"0.0"),
                                session["user_id"],
                                order.currencyId, 
                                false)?.substring(2)}</td>
                         </tr>
                     </g:each>
                 </tbody>
           </table>
        </g:if>
        <g:else>
            <em><g:message code="order.prompt.no.lines"/></em>
        </g:else>
    </div>
    
    <!-- Invoices Generated -->
    <g:if test="${order?.generatedInvoices}">
        <div class="heading">
            <strong><g:message code="order.label.invoices.generated"/></strong>
        </div>
        
        <div class="box">
            <table class="innerTable" >
                <thead class="innerHeader">
                     <tr>
                        <th><g:message code="order.invoices.id"/></th>
                        <th><g:message code="order.invoices.date"/></th>
                        <th><g:message code="order.invoices.total"/></th>
                     </tr>
                </thead>
                <tbody>
                     <g:each var="invoice" in="${order.generatedInvoices}" status="idx">
                         <tr>
                            <td class="innerContent">
                                <g:remoteLink controller="invoice" action="show" id="${invoice.id}" before="register(this);" onSuccess="render(data, next);">
                                    ${invoice.id}
                                </g:remoteLink>
                            </td>
                            <td class="innerContent">
                                <g:formatDate format="dd-MMM-yyyy HH:mm:ss a" date="${invoice?.createDateTime}"/>
                            </td>
                            <td class="innerContent">${Util.formatMoney(new BigDecimal(invoice.total ?: "0.0"),
                                session["user_id"],invoice?.currencyId, false)}</td>
                         </tr>
                     </g:each>
                </tbody>
            </table>
        </div>
    </g:if>
    
    <div class="btn-box">
        <div class="row">
            <g:if test="${Constants.ORDER_STATUS_ACTIVE == order.statusId}">
                <a href="${createLink (action: 'generateInvoice', params: [id: order?.id])}" class="submit order">
                    <span><g:message code="order.button.generate"/></span>
                </a>
                <a href="${createLink (action: 'applyToInvoice', params: [id: order?.id, userId: user?.id])}" class="submit order">
                    <span><g:message code="order.button.apply.invoice"/></span>
                </a>
            </g:if>
            <a href="${createLink (controller: 'orderBuilder', action: 'edit', params: [id: order?.id])}" class="submit edit">
                <span><g:message code="order.button.edit"/>
            </span></a></div><div class="row">
            <a onclick="showConfirm('deleteOrder-' + ${order?.id});" class="submit delete">
                <span><g:message code="order.button.delete"/></span>
            </a>
	   </div>
    </div>
</div>

<g:render template="/confirm"
     model="['message':'order.prompt.are.you.sure',
             'controller':'order',
             'action':'deleteOrder',
             'id':order.id,
            ]"/>