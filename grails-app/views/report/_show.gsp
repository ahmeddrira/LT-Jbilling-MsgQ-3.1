<%@ page import="com.sapienter.jbilling.server.pricing.db.PriceModelStrategy; com.sapienter.jbilling.server.util.Util"%>

<%--
  Report details template.

  @author Brian Cowdery
  @since  07-Mar-2011
--%>


<div class="column-hold">
    <div class="heading">
	    <strong><g:message code="${selected.name}"/></strong>
	</div>

	<div class="box">
        <!-- report info -->
        <table class="dataTable" cellspacing="0" cellpadding="0">
            <tbody>
                <tr>
                    <td>Report Id</td>
                    <td class="value">${selected.id}</td>
                </tr>
                <tr>
                    <td>Report Type</td>
                    <td class="value">${selected.type.getDescription(session['language_id'])}</td>
                </tr>
                <tr>
                    <td>Report File</td>
                    <td class="value"><em>${selected.fileName}</em></td>
                </tr>
            </tbody>
        </table>

        <p class="description">
            ${selected.getDescription(session['language_id'])}
        </p>

        <hr/>

        <!-- report parameter form -->

    </div>

    <div class="btn-box">
        <g:link action="run" id="${selected.id}" class="submit edit"><span><g:message code="button.run.report"/></span></g:link>
    </div>

</div>

