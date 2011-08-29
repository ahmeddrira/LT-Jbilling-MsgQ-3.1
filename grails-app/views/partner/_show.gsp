%{--
  jBilling - The Enterprise Open Source Billing System
  Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

  This file is part of jbilling.

  jbilling is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  jbilling is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
  --}%

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="com.sapienter.jbilling.server.process.db.PeriodUnitDTO"  %>

<%--
  Shows a Partner

  @author Vikas Bodani
  @since  26-Jul-2011
--%>

<div class="column-hold">
    <div class="heading">
        <strong>
            <g:if test="${contact?.firstName || contact?.lastName}">
                ${contact.firstName} ${contact.lastName}
            </g:if>
            <g:else>
                ${selected?.baseUser.userName}
            </g:else>
            <em><g:if test="${contact}">${contact.organizationName}</g:if></em>
        </strong>
    </div>

    <!-- partner user details -->
    <div class="box">
        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
            <tr>
                <td><g:message code="partner.detail.id"/></td>
                <td class="value">${selected.id}</td>
            </tr>
            <tr>
                <td><g:message code="customer.detail.user.username"/></td>
                <td class="value">${selected?.baseUser.userName}</td>
            </tr>
            <tr>
                <td><g:message code="customer.detail.user.status"/></td>
                <td class="value">${selected?.baseUser.userStatus.description}</td>
            </tr>
            <tr>
                <td><g:message code="user.language"/></td>
                <td class="value">${selected?.baseUser.language.getDescription()}</td>
            </tr>

            <tr>
                <td><g:message code="customer.detail.user.created.date"/></td>
                <td class="value"><g:formatDate date="${selected?.baseUser.createDatetime}" formatName="date.pretty.format"/></td>
            </tr>
            <tr>
                <td><g:message code="user.last.login"/></td>
                <td class="value"><g:formatDate date="${selected?.baseUser.lastLogin}" formatName="date.pretty.format"/></td>
            </tr>
            </tbody>
        </table>
    </div>

    <!-- partner details -->
    <div class="heading">
        <strong><g:message code="partner.details.title"/></strong>
    </div>
    <g:if test="${selected}">
    <div class="box">
        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
            <tr>
                <td><g:message code="partner.detail.balance"/></td>
                <td class="value">
                    <g:formatNumber number="${selected?.balance?:0}" formatName="money.format"/>
                </td>
            </tr>
            <tr>
                <td><g:message code="partner.detail.fees"/></td>
                <td class="value">
                    <g:formatNumber number="${selected?.totalPayments}" formatName="money.format"/>
                </td>
            </tr>
            <tr>
                <td><g:message code="partner.detail.refunded.fees"/></td>
                <td class="value">
                    <g:formatNumber number="${selected?.totalRefunds}" formatName="money.format"/>
                </td>
            </tr>
            <tr>
                <td><g:message code="partner.detail.payouts"/></td>
                <td class="value">
                    <g:formatNumber number="${selected?.totalPayouts}" formatName="money.format"/>
                </td>
            </tr>

            <tr>
                <td><g:message code="partner.detail.rate"/></td>
                <td class="value">
                    <g:formatNumber number="${selected?.percentageRate}" formatName="money.format"/>
                </td>
            </tr>
            <tr>
                <td><g:message code="partner.detail.ref.fee"/></td>
                <td class="value">
                    <g:formatNumber number="${selected?.referralFee}" formatName="money.format"/>
                </td>
            </tr>
            <tr>
                <td><g:message code="partner.detail.payout.period"/></td>
                <td class="value">
                    ${selected?.periodValue} ${selected?.periodUnit?.getDescription(session['language_id'])}
                </td>
            </tr> 
            <%--  TODO - User PartnerPayout.java to populate payouts and amount due
            <tr>
                <td><g:message code="partner.detail.next.payout"/></td>
                <td class="value">${selected.id}</td>
            </tr>
            <tr>
                <td><g:message code="partner.detail.amount.due"/></td>
                <td class="value">${selected.id}</td>
            </tr>
            <tr>
                <td><g:message code="partner.detail.batch"/></td>
                <td class="value">${selected.id}</td>
            </tr>
            <tr>
                <td><g:message code="partner.detail.related.clerk"/></td>
                <td class="value">${selected.id}</td>
            </tr>
            --%>
            </tbody>
        </table>
    </div>
    </g:if>
    
    <!-- contact details -->
    <div class="heading">
        <strong><g:message code="customer.detail.contact.title"/></strong>
    </div>
    <g:if test="${contact}">
    <div class="box">

        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
                <tr>
                    <td><g:message code="customer.detail.user.email"/></td>
                    <td class="value"><a href="mailto:${contact?.email}">${contact?.email}</a></td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.contact.telephone"/></td>
                    <td class="value">
                        <g:if test="${contact.phoneCountryCode}">${contact.phoneCountryCode}.</g:if>
                        <g:if test="${contact.phoneAreaCode}">${contact.phoneAreaCode}.</g:if>
                        ${contact.phoneNumber}
                    </td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.contact.address"/></td>
                    <td class="value">${contact.address1} ${contact.address2}</td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.contact.city"/></td>
                    <td class="value">${contact.city}</td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.contact.state"/></td>
                    <td class="value">${contact.stateProvince}</td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.contact.country"/></td>
                    <td class="value">${contact.countryCode}</td>
                </tr>
                <tr>
                    <td><g:message code="customer.detail.contact.zip"/></td>
                    <td class="value">${contact.postalCode}</td>
                </tr>
            </tbody>
        </table>
    </div>
    </g:if>

    <div class="btn-box">
        <div class="row">
            <g:link action="edit" id="${selected.id}" class="submit edit"><span><g:message code="button.edit"/></span></g:link>
            <a onclick="showConfirm('delete-${selected.id}');" class="submit delete"><span><g:message code="button.delete"/></span></a>
        </div>
    </div>

    <g:render template="/confirm"
              model="['message': 'partner.delete.confirm',
                      'controller': 'partner',
                      'action': 'delete',
                      'id': selected.id,
                      'ajax': true,
                      'update': 'column1',
                      'onYes': 'closePanel(\'#column2\')'
                     ]"/>

</div>