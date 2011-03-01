<%@ page import="com.sapienter.jbilling.server.util.Util; com.sapienter.jbilling.server.user.contact.db.ContactDTO"%>

<%-- 
    Orders list template. 
    
    @author Vikas Bodani
    @since 20-Jan-2011
 --%>
 
<div class="table-box">
    <div class="table-scroll">
        <table id="orders" cellspacing="0" cellpadding="0">
            <thead>
                <tr>
                    <th class="small"><g:message code="order.label.id"/></th>
                    <th class="large"><g:message code="order.label.customer"/></th>
                    <th class="small"><g:message code="order.label.date"/></th>
                    <th class="small"><g:message code="order.label.amount"/></th>
                </tr>
            </thead>
            <tbody>
                <g:each var="ordr" in="${orders}">
                    <g:set var="contact" value="${ContactDTO.findByUserId(ordr?.baseUserByUserId?.id)}"/>
                    <tr id="order-${ordr.id}" class="${(order?.id == ordr?.id) ? 'active' : ''}">
                        <td class="small">
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${ordr.id}" params="['template': 'show']"
                                before="register(this);" onSuccess="render(data, next);">
                                <strong>${ordr.id}</strong>
                            </g:remoteLink>
                        </td>
                        <td class="large">
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${ordr.id}"
                                params="['template': 'show']" before="register(this);" onSuccess="render(data, next);">
                                <strong>
                                    <g:if test="${contact?.firstName || contact?.lastName}">
                                        ${contact.firstName} &nbsp;${contact.lastName}
                                    </g:if> 
                                    <g:else>
                                        ${ordr?.baseUserByUserId?.userName}
                                    </g:else>
                                </strong>
                                <em>${contact?.organizationName}</em>
                            </g:remoteLink>
                        </td>
                        <td class="small">
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${ordr.id}" params="['template': 'show']"
                                before="register(this);" onSuccess="render(data, next);">
                                <strong>
                                    ${Util.formatDate(ordr?.createDate, session["user_id"])}
                                </strong>
                            </g:remoteLink>
                        </td>
                        <td class="small">
                            <g:remoteLink breadcrumb="id" class="cell" action="show" id="${ordr.id}" params="['template': 'show']"
                                    before="register(this);" onSuccess="render(data, next);">
                                <strong>
                                    ${Util.formatMoney(ordr.total,
                                        session["user_id"],
                                        ordr.currency.id, 
                                        false)?.substring(2)}
                                </strong>
                            </g:remoteLink>
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>
     </div>
</div>


<g:if test="${orders?.totalCount > params.max}">
    <div class="pager-box">
        <util:remotePaginate controller="order" action="list" params="[applyFilter: true]" total="${orders.totalCount}" update="column1"/>
    </div>
</g:if>

<div class="btn-box">
</div>