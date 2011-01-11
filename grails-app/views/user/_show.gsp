<%@ page import="com.sapienter.jbilling.server.user.UserBL; com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>

<%--
  Shows details of a selected user.

  @author Brian Cowdery
  @since  23-Nov-2010
--%>

<g:set var="customer" value="${selected.customer}"/>
<g:set var="contact" value="${ContactDTO.findByUserId(selected.id)}"/>

<div class="column-hold">
    <!-- user notes -->
    <div class="heading">
        <strong>
            <g:if test="${contact && (contact.firstName || contact.lastName)}">
                ${contact.firstName} ${contact.lastName}
            </g:if>
            <g:else>
                ${selected.userName}
            </g:else>
            <em><g:if test="${contact}">${contact.organizationName}</g:if></em>
        </strong>
    </div>
    <div class="box edit">
        <g:remoteLink action="show" id="${selected.id}" params="[template: 'notes']" before="register(this);" onSuccess="render(data, next);" class="edit"/>
        <strong><g:message code="customer.detail.note.title"/></strong>
        <g:if test="${customer && customer.notes}">
            <p>${customer.notes}</p>
        </g:if>
        <g:else>
            <p><em><g:message code="customer.detail.note.empty.message"/></em></p>
        </g:else>
    </div>

    <!-- user details -->
    <div class="heading">
        <strong><g:message code="customer.detail.user.title"/></strong>
    </div>
    <div class="box">
        <dl>
            <dt><g:message code="customer.detail.user.user.id"/></dt>
            <dd>${selected.id}</dd>
            <dt><g:message code="customer.detail.user.username"/></dt>
            <dd>${selected.userName}</dd>
            <dt><g:message code="customer.detail.user.status"/></dt>
            <dd>${selected.userStatus.description}</dd>
            <dt><g:message code="customer.detail.user.created.date"/></dt>
            <dd><g:formatDate format="MMM-dd-yyyy" date="${selected.createDatetime}"/></dd>
            <g:if test="${contact}">
            <dt><g:message code="customer.detail.user.email"/></dt>
            <dd><a href="mailto:${contact.email}">${contact.email}</a></dd>
            </g:if>
            <dt>&nbsp;</dt>
            <dd>
                <g:link controller="customerInspector" action="inspect" id="${selected.id}">Inspect Customer ${selected.id}</g:link>
            </dd>
        </dl>
    </div>

    <!-- user payment details -->
    <div class="heading">
        <strong><g:message code="customer.detail.payment.title"/></strong>
    </div>
    <div class="box">
        <g:set var="invoice" value="${selected.invoices ? selected.invoices.asList().first() : null}"/>
        <g:set var="payment" value="${selected.payments ? selected.payments.asList().first() : null}"/>

        <dl class="other other2">
            <dt><g:message code="customer.detail.payment.invoiced.date"/></dt>
            <dd><g:formatDate format="MMM-dd-yyyy" date="${invoice?.createDatetime}"/> &nbsp;</dd>
            <dt><g:message code="customer.detail.payment.due.date"/></dt>
            <dd><g:formatDate format="MMM-dd-yyyy" date="${invoice?.dueDate}"/> &nbsp;</dd>
            <dt><g:message code="customer.detail.payment.invoiced.amount"/></dt>
            <dd><g:formatNumber number="${invoice?.total}" type="currency" currencyCode="${selected.currency.code}"/> &nbsp;</dd>
            <dt><g:message code="customer.detail.payment.amount.owed"/></dt>
            <dd><g:formatNumber number="${new UserBL().getBalance(selected.id)}" type="currency" currencyCode="${selected.currency.code}"/> &nbsp;</dd>
            <dt><g:message code="customer.detail.payment.lifetime.revenue"/></dt>
            <dd>&nbsp;</dd>
        </dl>

        <g:set var="card" value="${selected.creditCards ? selected.creditCards.asList().first() : null}"/>
        <dl class="other">
            <dt><g:message code="customer.detail.payment.credit.card"/></dt>
            <dd>${card?.number} &nbsp;</dd>
            <dt><g:message code="customer.detail.payment.credit.card.expiry"/></dt>
            <dd><g:formatDate format="MMM-dd-yyyy" date="${card?.ccExpiry}"/> &nbsp;</dd>
        </dl>
    </div>

    <!-- contact details -->    
    <div class="heading">
        <strong><g:message code="customer.detail.contact.title"/></strong>
    </div>
    <g:if test="${contact}">
    <div class="box">
        <dl>
            <dt><g:message code="customer.detail.contact.telephone"/></dt>
            <dd>
                <g:if test="${contact.phoneCountryCode}">${contact.phoneCountryCode}.</g:if>
                <g:if test="${contact.phoneAreaCode}">${contact.phoneAreaCode}.</g:if>
                ${contact.phoneNumber} &nbsp;
            </dd>
            <dt><g:message code="customer.detail.contact.address"/></dt>
            <dd>${contact.address1} ${contact.address2} &nbsp;</dd>
            <dt><g:message code="customer.detail.contact.city"/></dt>
            <dd>${contact.city} &nbsp;</dd>
            <dt><g:message code="customer.detail.contact.state"/></dt>
            <dd>${contact.stateProvince} &nbsp;</dd>
            <dt><g:message code="customer.detail.contact.country"/></dt>
            <dd>${contact.countryCode} &nbsp;</dd>
            <dt><g:message code="customer.detail.contact.zip"/></dt>
            <dd>${contact.postalCode} &nbsp;</dd>
        </dl>
    </div>
    </g:if>

    <div class="btn-box">
        <div class="row">
            <a href="#" class="submit order"><span><g:message code="button.create.order"/></span></a>
            <g:link controller="payment" action="edit" params="[userId: selected.id]" class="submit payment"><span><g:message code="button.create.payment"/></span></g:link>
        </div>
        <div class="row">
            <g:link action="edit" id="${selected.id}" class="submit edit"><span><g:message code="button.edit"/></span></g:link>
        </div>
    </div>
</div>