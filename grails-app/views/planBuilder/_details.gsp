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

<%@ page import="com.sapienter.jbilling.server.util.Constants; com.sapienter.jbilling.server.user.db.CompanyDTO" %>

<%--
  Order details form. Allows editing of primary order attributes.

  @author Brian Cowdery
  @since 01-Feb-2011
--%>

<div id="details-box">
    <g:formRemote name="plan-details-form" url="[action: 'edit']" update="column2" method="GET">
        <g:hiddenField name="_eventId" value="update"/>
        <g:hiddenField name="execution" value="${flowExecutionKey}"/>

        <div class="form-columns">
            <g:applyLayout name="form/text">
                <content tag="label"><g:message code="product.id"/></content>
                ${planItem.id}
            </g:applyLayout>

            <g:applyLayout name="form/text">
                <content tag="label"><g:message code="product.internal.number"/></content>
                ${planItem.internalNumber}
            </g:applyLayout>

            <g:applyLayout name="form/text">
                <content tag="label"><g:message code="plan.item.description"/></content>
                ${planItem.description}
            </g:applyLayout>
        </div>

        <hr/>

        <div class="form-columns">
            <div class="box-text">
                <label class="lb"><g:message code="plan.description"/></label>
                <g:textArea name="description" rows="5" cols="60" value="${plan?.description}"/>
            </div>
        </div>
    </g:formRemote>

    <script type="text/javascript">
        $(function() {
            $('#plan-details-form').find(':text.hasDatepicker, select, :checkbox').change(function() {
                $('#plan-details-form').submit();
            });

            $('#plan-details-form').find('textarea, :text').not('.hasDatepicker').blur(function() {
                $('#plan-details-form').submit();
            });
        });
    </script>
</div>


