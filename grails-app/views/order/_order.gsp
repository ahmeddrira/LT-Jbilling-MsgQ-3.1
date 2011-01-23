<%@ page import="com.sapienter.jbilling.server.util.Util" %>

<div class="column-hold">

    <div class="heading">
        <strong><g:message code="order.label.details"/>&nbsp;${order?.id}</strong>
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
                <td class="value"><g:link controller="user" action="list"
                        id="${user?.id}">
                    ${user?.id}
                </g:link></td>
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
            <tr><td><g:message code="order.label.period"/>:</td><td class="value">${order.period}</td></tr>
            <tr><td><g:message code="order.label.total"/>:</td>
                    <td class="value">${Util.formatMoney(order.total as BigDecimal,
                        session["user_id"],
                        order.currencyId, 
                        false)?.substring(3)}</td></tr>
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
                            <td class="innerContent">${line?.itemId}</td>
                            <td class="innerContent">${line.description}</td>
                            <td class="innerContent">${new BigDecimal(line.quantity?: "0.0").intValue()}</td>
                            <td class="innerContent">${Util.formatMoney( new BigDecimal(line?.price?:"0.0"),
                                session["user_id"],
                                order.currencyId, 
                                false)?.substring(3)}</td>
                            <td class="innerContent">${Util.formatMoney( new BigDecimal(line?.amount?:"0.0"),
                                session["user_id"],
                                order.currencyId, 
                                false)?.substring(3)}</td>
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
    <%-- 
    <div class="heading">
        <strong><g:message code="order.label.invoices.generated"/></strong>
    </div>
    
    <div class="box">
        <table class="innerTable" >
            <thead class="innerHeader">
                 <tr>
                    <th></th>
                 </tr>
            </thead>
        </table>
    </div>
    --%>
    
    <div class="btn-box">
        <div class="row">
            <a onclick="javascript: void(0)" class="submit">
                <span><g:message code="order.button.generate"/></span>
            </a>
        </div>
        <div class="row">
            <a onclick="javascript: void(0)" class="submit">
                <span><g:message code="order.button.apply"/></span>
            </a>
            <a onclick="javascript: void(0)" class="submit">
                <span><g:message code="order.button.edit"/></span>
            </a>
        </div>
    </div>
</div>