<%@ page import="com.sapienter.jbilling.server.user.contact.db.ContactFieldTypeDTO; com.sapienter.jbilling.common.Constants; com.sapienter.jbilling.client.user.UserHelper" %>

<g:set var="ipAddressType" value="${ContactFieldTypeDTO.list().find{ it.promptKey ==~ /.*ip_address.*/ }}"/>

<div class="form-edit">
    <div class="heading">
        <strong><g:message code="blacklist.title"/></strong>
    </div>
    <div class="form-hold">
        <fieldset>
            <div class="form-columns single">
                <div class="column single">

                    <!-- blacklist upload -->
                    <g:uploadForm name="save-blacklist-form" url="[action: 'save']">
                        <g:applyLayout name="form/text">
                            <content tag="label"><g:message code="blacklist.label.csv.file"/></content>
                            <input type="file" name="csv"/>
                        </g:applyLayout>

                        <g:applyLayout name="form/radio">
                            <content tag="label">&nbsp;</content>

                            <g:radio class="rb" id="csvUpload.add" name="csvUpload" value="add" />
                            <label class="rb" for="csvUpload.add"><g:message code="blacklist.label.upload.type.add"/></label>

                            <g:radio class="rb" id="csvUpload.modify" name="csvUpload" value="modify" checked="${true}"/>
                            <label class="rb" for="csvUpload.modify"><g:message code="blacklist.label.upload.type.upload"/></label>
                        </g:applyLayout>

                        <div class="btn-row">
                            <br/>
                            <a onclick="$('#save-blacklist-form').submit();" class="submit save"><span><g:message code="button.update"/></span></a>
                        </div>
                    </g:uploadForm>


                    <!-- separator -->
                    <div>
                        <hr/>
                    </div>

                    <!-- blacklist entry list -->
                    <g:applyLayout name="form/input">
                        <content tag="label"><g:message code="filters.title"/></content>
                        <content tag="label.for">filterBy</content>
                        <g:textField name="filterBy" class="field default" placeholder="ID, User Name or Credit Card" value="${params.filterBy}"/>
                    </g:applyLayout>

                    <div style="height: 400px; overflow-y: scroll; margin: 10px 0; border: 1px solid #bbb;">
                        <table cellpadding="0" cellspacing="0" class="blacklist" width="100%">
                            <thead>
                                <tr>
                                    <th class="medium"><g:message code="blacklist.th.name"/></th>
                                    <th class="small2"><g:message code="blacklist.th.credit.card"/></th>
                                    <th class="small2"><g:message code="blacklist.th.ip.address"/></th>
                                </tr>
                            </thead>

                            <tbody>
                                <g:each var="entry" status="i" in="${blacklist}">
                                    <tr class="${i % 2 == 0 ? 'even' : 'odd'}">
                                        <td id="entry-${entry.id}">
                                            <g:remoteLink class="cell" action="show" id="${entry.id}" before="register(this);" onSuccess="render(data, next);">
                                                <g:set var="name" value="${UserHelper.getDisplayName(entry.user, entry.contact)}"/>
                                                ${name ?: entry.user?.id ?: entry.contact?.userId ?: entry.contact?.id}
                                            </g:remoteLink>
                                        </td>
                                        <td>
                                            <g:remoteLink class="cell" action="show" id="${entry.id}" before="register(this);" onSuccess="render(data, next);">
                                                %{-- obscure credit card by default, or if the preference is explicitly set --}%
                                                <g:if test="${entry.creditCard?.number && preferenceIsNullOrEquals(preferenceId: Constants.PREFERENCE_HIDE_CC_NUMBERS, value: 1, true)}">
                                                    <g:set var="creditCardNumber" value="${entry.creditCard.number.replaceAll('^\\d{12}','************')}"/>
                                                    ${creditCardNumber}
                                                </g:if>
                                                <g:else>
                                                    ${entry.creditCard?.number}
                                                </g:else>
                                            </g:remoteLink>
                                        </td>
                                        <td>
                                            <g:remoteLink class="cell" action="show" id="${entry.id}" before="register(this);" onSuccess="render(data, next);">
                                                <g:if test="${ipAddressType}">
                                                    <g:set var="field" value="${entry.contact?.fields?.find{ it.type.id == ipAddressType.id }}"/>
                                                    ${field?.content}
                                                </g:if>
                                            </g:remoteLink>
                                        </td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                    </div>

                    <strong><g:message code="blacklist.label.entries" args="[blacklist.size()]"/></strong>

                    <!-- spacer -->
                    <div>
                        <br/>&nbsp;
                    </div>

                </div>
            </div>
        </fieldset>
    </div>
</div>