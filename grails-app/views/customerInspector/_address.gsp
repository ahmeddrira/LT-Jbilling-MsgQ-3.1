<%--
  Address data-table template for the customer inspector.

  @author Brian Cowdery
  @since  11-Jan-2011
--%>

<table class="dataTable" cellspacing="0" cellpadding="0">
    <tbody>
    <tr>
        <td><g:message code="customer.detail.contact.address"/></td>
        <td class="value">${contact?.address1} ${contact?.address2}</td>

    </tr>
    <tr>
        <td><g:message code="customer.detail.contact.city"/></td>
        <td class="value">${contact?.city}</td>

        <td><g:message code="customer.detail.contact.state"/></td>
        <td class="value">${contact?.stateProvince}</td>

        <td><g:message code="customer.detail.contact.zip"/></td>
        <td class="value">${contact?.postalCode}</td>
    </tr>
    <tr>
        <td><g:message code="customer.detail.contact.country"/></td>
        <td class="value">${contact?.countryCode}</td>
    </tr>
    <tr>
        <td><g:message code="prompt.include.in.notifications"/></td>
        <td class="value"><g:formatBoolean boolean="${contact?.include > 0}"/></td>
    </tr>
    </tbody>
</table>



