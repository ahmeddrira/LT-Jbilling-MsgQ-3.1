<%@ page import="com.sapienter.jbilling.server.util.Constants; org.apache.commons.lang.StringUtils; org.apache.commons.lang.WordUtils" contentType="text/html;charset=UTF-8" %>

<%--
  Shows a list of all preferences

  @author Brian Cowdery
  @since  01-Apr-2011
--%>

<div class="table-box">
    <table id="users" cellspacing="0" cellpadding="0">
        <thead>
            <tr>
                <th>Preference</th>
                <th class="medium2">Value</th>
            </tr>
        </thead>

        <tbody>
            <g:each var="type" in="${preferenceTypes}">
                <tr id="type-${type.id}" class="${selected?.id == type.id ? 'active' : ''}">
                    <td>
                        <g:remoteLink class="cell double" action="show" id="${type.id}" before="register(this);" onSuccess="render(data, next);">
                            <strong>${StringUtils.abbreviate(type.getDescription(session['language_id']), 50)}</strong>
                            <em>Id: ${type.id}</em>
                        </g:remoteLink>
                    </td>

                    <td class="medium2">
                        <g:remoteLink class="cell" action="show" id="${type.id}" before="register(this);" onSuccess="render(data, next);">

                            <g:if test="${type.preferences}">
                                %{
                                    def preference = type.preferences.find{
                                                        it.jbillingTable.name == Constants.TABLE_ENTITY && it.foreignId == session['company_id']
                                                    } ?: type.preferences.asList().first()
                                }%

                                <g:if test="${preference.strValue}">
                                    ${preference.strValue}
                                </g:if>

                                <g:if test="${preference.intValue}">
                                    ${preference.intValue}
                                </g:if>

                                <g:if test="${preference.floatValue}">
                                    <g:formatNumber number="${preference.floatValue}" formatName="decimal.format"/>
                                </g:if>
                            </g:if>
                            <g:else>
                                <g:if test="${type.strDefValue}">
                                    ${type.strDefValue}
                                </g:if>

                                <g:if test="${type.intDefValue}">
                                    ${type.intDefValue}
                                </g:if>

                                <g:if test="${type.floatDefValue}">
                                    <g:formatNumber number="${type.floatDefValue}" formatName="decimal.format"/>
                                </g:if>
                            </g:else>

                        </g:remoteLink>
                    </td>
                </tr>
            </g:each>

        </tbody>
    </table>
</div>

<div class="btn-box">
    <g:remoteLink action='edit' class="submit add" before="register(this);" onSuccess="render(data, next);">
        <span><g:message code="button.create"/></span>
    </g:remoteLink>
</div>