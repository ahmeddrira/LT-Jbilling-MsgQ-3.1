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
                    <td>Report Design File</td>
                    <td class="value">
                        <em title="${selected.reportFilePath}">${selected.fileName}</em>
                    </td>
                </tr>
            </tbody>
        </table>

        <!-- report description -->
        <p class="description">
            ${selected.getDescription(session['language_id'])}
        </p>

        <hr/>

        <!-- report parameter form -->
        <g:form name="run-report-form" url="[action: 'run']">
            <g:hiddenField name="id" value="${selected.id}"/>

            <g:render template="/report/${selected.type.name}/${selected.name}"/>
        </g:form>

        <br/>&nbsp;
    </div>

    <div class="btn-box">
        <a class="submit edit" onclick="$('#run-report-form').submit();">
            <span><g:message code="button.run.report"/></span>
        </a>
    </div>
</div>

