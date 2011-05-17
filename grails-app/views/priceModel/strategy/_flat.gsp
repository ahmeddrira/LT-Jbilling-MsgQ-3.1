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

<%@ page import="com.sapienter.jbilling.server.pricing.db.PriceModelStrategy" %>

<%--
  Flat pricing form.

  @author Brian Cowdery
  @since  08-Feb-2011
--%>

<g:hiddenField name="model.${modelIndex}.id" value="${model?.id}"/>

<g:applyLayout name="form/select">
    <content tag="label"><g:message code="plan.model.type"/></content>
    <content tag="label.for">model.${modelIndex}.type</content>
    <g:select name="model.${modelIndex}.type" class="model-type"
              from="${types}"
              valueMessagePrefix="price.strategy"
              value="${model?.type ?: type.name()}"/>

    <g:hiddenField name="model.${modelIndex}.oldType" value="${model?.type ?: type.name()}"/>

    <g:if test="${modelIndex > 0}">
        <a onclick="removeChainModel(this, ${modelIndex});">
            <img src="${resource(dir:'images', file:'cross.png')}" alt="remove"/>
        </a>
    </g:if>
</g:applyLayout>

<g:hiddenField name="model.${modelIndex}.rateAsDecimal" value="${BigDecimal.ZERO}"/>
