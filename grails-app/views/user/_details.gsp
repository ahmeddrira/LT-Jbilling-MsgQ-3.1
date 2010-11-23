<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactDTO" %>

<%--
  details

  @author Brian Cowdery
  @since  23-11-2010
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
    <div class="box">
        <a href="#" class="edit"></a>
        <strong>Note:</strong>
        <g:if test="${customer && customer.notes}">
            <p>${customer.notes}</p>
        </g:if>
        <g:else>
            <p><em>No customer notes.</em></p>
        </g:else>
    </div>


    <!-- user details -->
    <div class="heading">
        <strong>User Info</strong>
    </div>
    <div class="box">
        <a href="#" class="edit"></a>
        <dl>
            <dt>User ID</dt>
            <dd>${selected.id}</dd>
            <dt>Login Name</dt>
            <dd>${selected.userName}</dd>
            <dt>Status</dt>
            <dd>${selected.userStatus.description}</dd>
            <dt>Created Date</dt>
            <dd>${selected.createDatetime}</dd>
            <g:if test="${contact}">
            <dt>Email Address</dt>
            <dd><a href="mailto:${contact.email}">${contact.email}</a></dd>
            </g:if>
        </dl>
    </div>

    <!-- user payment details -->
    <div class="heading">
        <strong>Payment Info</strong>
    </div>
    <div class="box">
        <a href="#" class="edit"></a>
        <dl class="other other2">
            <dt>Last Invoiced Date</dt>
            <dd>Jun-01-2010</dd>
            <dt>Due Date</dt>
            <dd>Jun-15-2010</dd>
            <dt>Invoiced Amount</dt>
            <dd>78.23</dd>
            <dt>Amount Owed</dt>
            <dd>123.45</dd>
            <dt>Lifetime revenue</dt>
            <dd>214.21</dd>
        </dl>
        <dl class="other">
            <dt>Credit Card</dt>
            <dd>4800 0000 0000 0000</dd>
            <dt>Expiry Date</dt>
            <dd>May 2012</dd>
        </dl>
    </div>

    <!-- contact details -->    
    <div class="heading">
        <strong>Contact Info</strong>
    </div>
    <g:if test="${contact}">
    <div class="box">
        <a href="#" class="edit"></a>
        <dl>
            <dt>Telephone</dt>
            <dd>
                <g:if test="${contact.phoneCountryCode}">
                    ${contact.phoneCountryCode}.
                </g:if>
                <g:if test="${contact.phoneAreaCode}">
                    ${contact.phoneAreaCode}.
                </g:if>
                ${contact.phoneNumber} &nbsp;
            </dd>
            <dt>Address</dt>
            <dd>${contact.address1} ${contact.address2} &nbsp;</dd>
            <dt>City</dt>
            <dd>${contact.city} &nbsp;</dd>
            <dt>State/Province</dt>
            <dd>${contact.stateProvince} &nbsp;</dd>
            <dt>Country</dt>
            <dd>${contact.countryCode} &nbsp;</dd>
            <dt>Postal/Zip Code</dt>
            <dd>${contact.postalCode} &nbsp;</dd>
        </dl>
    </div>
    </g:if>

    <div class="btn-box">
        <a href="#" class="submit order"><span>Create Order</span></a>
        <a href="#" class="submit payment"><span>Create Payment</span></a>
    </div>
</div>