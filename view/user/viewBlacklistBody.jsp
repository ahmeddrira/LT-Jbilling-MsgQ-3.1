<%--
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
--%>

<%@ page language="java" import="com.sapienter.jbilling.client.util.Constants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jbilling:getPreference preferenceId='<%=Constants.PREFERENCE_USE_BLACKLIST%>'
        beanName="use_blacklist"/>
<logic:notEqual name="use_blacklist" value="0">
    <bean:define id="blacklist_matches" name='<%=Constants.SESSION_CUSTOMER_DTO%>' property="blacklistMatches"/>
    <table class="info">
	    <tr>
		    <th class="info" colspan="2"><bean:message key="blacklist_matches.info.title"/></th>
	    </tr>
	    <logic:equal name="blacklist_matches" property="empty" value="true">
    	    <tr class="infoA">
        		<td class="infoprompt"><bean:message key="blacklist_matches.none"/></td>
        	</tr>
    	</logic:equal>
	    <logic:iterate id="message" name="blacklist_matches">
		    <c:choose>
			    <c:when test="${colorFlag == 1}">
				    <tr class="infoB">
				    <c:remove var="colorFlag"/>
			    </c:when>
			    <c:otherwise>
				    <tr class="infoA">
				    <c:set var="colorFlag" value="1"/>
			    </c:otherwise>
	        </c:choose>
                <td class="infodata"><bean:write name="message"/></td>
            </tr>
        </logic:iterate>
    </table>
</logic:notEqual>
