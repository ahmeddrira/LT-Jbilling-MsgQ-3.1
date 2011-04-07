<%@ page import="com.sapienter.jbilling.server.util.Constants" %>

<div class="form-edit">
    <div class="heading">
        <strong>Currencies</strong>
    </div>
    <div class="form-hold">
        <g:form name="save-currencies-form" url="[action: 'saveCurrencies']">
            <fieldset>
                <div class="form-columns">
                    <g:applyLayout name="form/select">
                        <content tag="label">Default Currency</content>
                        <g:select name="defaultCurrency" from="${currencies}"
                                  optionKey="id"
                                  optionValue="${{ it.getDescription(session['language_id']) }}"
                                  value="${entityCurrency}"/>
                    </g:applyLayout>
                </div>


                <div class="form-columns" style="width: auto;">
                    <table cellpadding="0" cellspacing="0" class="innerTable" width="100%">
                        <thead class="innerHeader">
                            <tr>
                                <th></th>
                                <th class="left">Symbol</th>
                                <th class="left">Active</th>
                                <th class="left">Exchange Rate</th>
                                <th class="left">System Rate</th>
                            </tr>
                        </thead>
                        <tbody>

                        <g:each var="currency" in="${currencies}">
                            <tr>
                                <td class="innerContent">
                                    ${currency.getDescription(session['language_id'])}
                                </td>
                                <td class="innerContent">
                                    ${currency.symbol}
                                </td>
                                <td class="innerContent">
                                    <g:checkBox name="${currency.id}.inUse" class="cb" checked="${currency.inUse}"/>
                                </td>
                                <td class="innerContent">
                                    <div class="inp-bg inp4">
                                        <g:textField name="${currency.id}.rate" class="field" value="${formatNumber(number: currency.rate, formatName: 'decimal.format')}"/>
                                    </div>
                                </td>
                                <td class="innerContent">
                                    <div class="inp-bg inp4">
                                        <g:textField name="${currency.id}.sysRate" class="field" value="${formatNumber(number: currency.sysRate, formatName: 'decimal.format')}"/>
                                    </div>
                                </td>
                            </tr>
                        </g:each>

                        </tbody>
                    </table>
                 </div>

                <!-- spacer -->
                <div>
                    <br/>&nbsp;
                </div>


            </fieldset>
        </g:form>
    </div>

    <div class="btn-box">
        <a onclick="$('#save-currencies-form').submit();" class="submit save"><span><g:message code="button.save"/></span></a>
        <g:link controller="config" action="index" class="submit cancel"><span><g:message code="button.cancel"/></span></g:link>
    </div>
</div>