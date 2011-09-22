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

<div id="timeline">
    <div class="form-columns">
        <ul>
            <%
                def dates = []
                plan.planItems.each{ dates.addAll(it.models.keySet()) }
            %>

            <g:each var="date" status="i" in="${dates}">
                <li class="${startDate.equals(date) ? 'current' : ''}">
                    <g:remoteLink action="edit" params="[_eventId: 'editDate', startDate : formatDate(date: date)]"
                                  update="column2" method="GET" onSuccess="timeline.refresh();">
                        <g:formatDate date="${date}"/>
                    </g:remoteLink>
                </li>
            </g:each>

            <li class="new">
                <a onclick="$('#add-date-dialog').dialog('open');">
                    <g:message code="button.add.price.date"/>
                </a>
            </li>
        </ul>
    </div>

    <div id="add-date-dialog" title="Add Date">
        <g:formRemote name="add-date-form" url="[action: 'edit']" update="column2" method="GET">
            <g:hiddenField name="_eventId" value="addDate"/>
            <g:hiddenField name="execution" value="${flowExecutionKey}"/>

            <div class="column">
                <div class="columns-holder">
                    <fieldset>
                        <div class="form-columns">
                            <g:applyLayout name="form/date">
                                <content tag="label">Start Date</content>
                                <content tag="label.for">startDate</content>
                                <g:textField class="field" name="startDate" value="${formatDate(date: new Date(), formatName: 'datepicker.format')}"/>
                            </g:applyLayout>
                        </div>
                    </fieldset>
                </div>
            </div>
        </g:formRemote>
    </div>

    <script type="text/javascript">
        $(function(){
            $('#add-date-dialog').dialog({
                 autoOpen: false,
                 height: 300,
                 width: 520,
                 modal: true,
                 buttons: {
                     Cancel: function() {
                         $(this).dialog("close");
                     },
                     Save: function() {
                         $('#add-date-form').submit();
                         $(this).dialog("close");
                         timeline.refresh();
                     }
                 }
             });
        });
    </script>
</div>

