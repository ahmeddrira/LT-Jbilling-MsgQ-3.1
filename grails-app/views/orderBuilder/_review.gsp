
<%--
  Renders an OrderWS as a quick preview of the order being built. This view also allows
  individual order lines to be edited and removed from the order.

  @author Brian Cowdery
  @since 23-Jan-2011
--%>

<div id="review-box">
    <g:formRemote name="filter-form" url="[action: 'edit']" update="column2" method="GET">
        <g:hiddenField name="_eventId" value="updateLine"/>
        <g:hiddenField name="execution" value="${flowExecutionKey}"/>

        <div class="box no-heading">

            <!-- order header -->
            <div class="header">
                <div class="column">
                    <h1><g:message code="order.review.id" args="[order.id ?: '']"/></h1>

                    <h3>
                        <g:if test="${contact?.firstName || contact?.lastName}">
                            ${contact.firstName} ${contact.lastName}
                        </g:if>
                        <g:else>
                            ${user.userName}
                        </g:else>
                    </h3>

                    <g:if test="${contact.organizationName}">
                        <h3>${contact.organizationName}</h3>
                    </g:if>
                </div>

                <div class="column">
                    <h2 class="right capitalize">
                        <g:set var="period" value="${orderPeriods.find{ order.period == it.id }}"/>
                        ${period?.getDescription(session['language_id'])},

                        <g:set var="orderBillingType" value="${orderBillingTypes.find{ order.billingTypeId == it.id }}"/>
                        ${orderBillingType?.getDescription(session['language_id'])}
                    </h2>

                    <h3 class="right capitalize">
                        <g:set var="activeSince" value="${formatDate(date: order.activeSince ?: order.createDate)}"/>
                        <g:set var="activeUntil" value="${formatDate(date: order.activeUntil)}"/>

                        <g:if test="${order.activeUntil}">
                            <g:message code="order.review.active.date.range" args="[activeSince, activeUntil]"/>
                        </g:if>
                        <g:else>
                            <g:message code="order.review.active.since" args="[activeSince]"/>
                        </g:else>
                    </h3>
                </div>

                <div style="clear: both;"></div>
            </div>

            <hr/>

            <!-- list of order lines -->
            <ul id="order-lines">
                <g:each var="line" status="index" in="${order.orderLines}">
                    <g:render template="orderLine" model="[ line: line, index: index, user: user ]"/>
                </g:each>

                <g:if test="${!order.orderLines}">
                    <li><em><g:message code="order.review.no.order.lines"/></em></li>
                </g:if>
            </ul>

            <hr/>

            <!-- order total -->
            <div class="total">
                <g:message code="order.review.total" args="[formatNumber(number: order.getTotalAsDecimal() ?: new BigDecimal('0.00'), type: 'currency', currencyCode: user.currency.code)]"/>
            </div>

            <!-- order notes -->
            <g:if test="${order.notes}">
                <div class="box-text">
                    <ul>
                        <li><p>${order.notes}</p></li>
                    </ul>
                </div>
            </g:if>
        </div>
    </g:formRemote>

    <div class="btn-box">
        <a class="submit save"><span><g:message code="button.save"/></span></a>
        <a class="submit cancel"><span><g:message code="button.cancel"/></span></a>
    </div>

</div>