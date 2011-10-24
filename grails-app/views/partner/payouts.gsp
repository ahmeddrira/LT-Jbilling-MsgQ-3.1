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

<html>
<head>
	<meta name="layout" content="main"/>
</head>

<body>

    <!-- partner info -->
    <div class="table-info">
        <em><strong><g:message code="partner.payout.partner.id"/> ${partner.id}</strong></em>
        <em><strong><g:message code="partner.payout.total.payouts"/> <g:formatNumber number="${partner.totalPayouts}" formatName="money.format"/></strong></em>
        <em><strong><g:message code="partner.payout.next.payout.date"/> <g:formatDate date="${partner.nextPayoutDate}" formatName="date.pretty.format"/></strong></em>
    </div>

    <!-- payouts -->
    <div class="table-area">
        <table>
            <thead>
                <tr>
                    <td class="first"><g:message code="partner.payout.th.start"/></td>
                    <td><g:message code="partner.payout.th.end"/></td>
                    <td><g:message code="partner.payout.th.payment.amount"/></td>
                    <td><g:message code="partner.payout.th.refund.amount"/></td>
                    <td><g:message code="partner.payout.th.balance"/></td>
                    <td class="last"><g:message code="partner.payout.th.payment"/></td>
                </tr>
            </thead>
            <tbody>

            <g:each var="payout" status="i" in="${partner.partnerPayouts}">
                <tr class="${i % 2 == 0 ? 'even' : 'odd'}">
                    <td class="col02">
                        <g:formatDate date="${payout.startingDate}" formatName="date.pretty.format"/>
                    </td>
                    <td>
                        <g:formatDate date="${payout.endingDate}" formatName="date.pretty.format"/>
                    </td>
                    <td>
                        <g:formatNumber number="${payout.paymentsAmount}" formatName="money.format"/>
                    </td>
                    <td>
                        <g:formatNumber number="${payout.refundsAmount}" formatName="money.format"/>
                    </td>
                    <td>
                        <g:formatNumber number="${payout.balanceLeft}" formatName="money.format"/>
                    </td>
                    <td>
                        <div>
                            <span class="small">
                                <g:formatNumber number="${payout.payment.amount}" type="currency" currencySymbol="${payout.payment.currency.symbol}"/>
                            </span>
                            <span class="small">
                                ${payout.payment.paymentMethod.getDescription(session['language_id'])}
                            </span>
                        </div>

                    </td>
                </tr>
            </g:each>

            <!-- totals -->
            <tr class="bg">
                <td class="col02"></td>
                <td></td>
                <td><g:formatNumber number="${partner.totalPayments}" formatName="money.format"/></td>
                <td><g:formatNumber number="${partner.totalPayouts}" formatName="money.format"/></td>
                <td><g:formatNumber number="${partner.balance}" formatName="money.format"/></td>
                <td></td>
            </tbody>
        </table>
    </div>

</body>