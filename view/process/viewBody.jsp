<%--
The contents of this file are subject to the Jbilling Public License
 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
--%>

<%@ page language="java" import="com.sapienter.jbilling.client.util.Constants"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/session-1.0" prefix="sess" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt" %>


<%	  
  session.removeAttribute(Constants.SESSION_LIST_KEY + Constants.LIST_TYPE_PROCESS_INVOICES);  
%>

<logic:present name='<%=Constants.SESSION_PROCESS_DTO%>' 
	           scope="session">

<logic:notPresent parameter="review">
	<p class="title"><bean:message key="process.view.title"/></p>
	
	<logic:present parameter="latest">
	   <p class="instr"><bean:message key="process.latest.instr"/></p>
	</logic:present>
	<logic:notPresent parameter="latest">
	   <p class="instr"><bean:message key="process.view.instr"/></p>
	</logic:notPresent>
	<p class="instr">
	   <bean:message key="all.prompt.help" />
	   <jbilling:help page="process" anchor="view">
			 <bean:message key="all.prompt.here" />
	   </jbilling:help>
	</p>
	
</logic:notPresent>

<table class="info">
	<tr>
		<th class="info" colspan="2">
			<bean:message key="process.external.id"/>
		</th>
	</tr>
	<logic:equal name='<%=Constants.SESSION_PROCESS_DTO%>' 
				 property="isReview"
				 scope="session"
				 value="0" >
	
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="process.prompt.id"/></td>
		<td class="infodata">	
            <bean:write name='<%=Constants.SESSION_PROCESS_DTO%>' 
                        property="id"
                        scope="session"/>
        </td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="process.prompt.runs"/></td>
		<td class="infodata">	
            <bean:write name='<%=Constants.SESSION_PROCESS_DTO%>' 
                        property="retries"
                        scope="session"/>
        </td>
	</tr>
	</logic:equal>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="process.prompt.date"/></td>
		<td class="infodata">	
            <bean:write name='<%=Constants.SESSION_PROCESS_DTO%>' 
                        property="billingDate"
                        scope="session"
                        formatKey="format.date"/>
        </td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="process.prompt.dateEnd"/></td>
		<td class="infodata">	
            <bean:write name='<%=Constants.SESSION_PROCESS_DTO%>' 
                        property="billingDateEnd"
                        scope="session"
                        formatKey="format.date"/>
        </td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="process.prompt.orders"/></td>
		<td class="infodata">	
            <bean:write name='<%=Constants.SESSION_PROCESS_DTO%>' 
                        property="ordersProcessed"
                        scope="session"/>
            <html:link page="/order/listProcess.jsp">
            	<bean:message key="process.prompt.viewList"/>
            </html:link>
        </td>
	</tr>
	
	<%-- Now show the grand total --%>
	<tr>
		<th class="info" colspan="2"><bean:message key="process.prompt.grandTotal"/></th>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="process.run.prompt.invoicesGenerated"/></td>
		<td class="infodata">	
            <bean:write name='<%=Constants.SESSION_PROCESS_DTO%>' 
                        property="grandTotal.invoiceGenerated"
                        scope="session"/>
            <html:link page="/invoice/listProcess.jsp">
            	<bean:message key="process.prompt.viewList"/>
            </html:link>
        </td>
	</tr>
	
	<%-- Loop over each total, one per currency --%>
	<logic:iterate name='<%=Constants.SESSION_PROCESS_DTO%>' 
				   property="grandTotal.totals"
				   id="total"
				   scope="session">
	
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="currency.external.prompt.name"/></td>
		<td class="infodata">	
            <bean:write name="total" property="currencyName"/>
        </td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="process.run.prompt.totalInvoiced"/></td>
		<td class="infodata">	
			<bean:define id="index" name="total"
				      property="currencyId"/>
			<bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				      property='<%= "symbols[" + index + "].symbol" %>'
				      scope="application"
				      filter="false"/>
            <bean:write name="total"
                        property="totalInvoiced"
                        formatKey="format.money"/>
        </td>
	</tr>
	
	<logic:equal name='<%=Constants.SESSION_PROCESS_DTO%>' 
				 property="isReview"
				 scope="session"
				 value="0" >
	
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="process.run.prompt.totalPaid"/></td>
		<td class="infodata">	
			<bean:define id="index" name="total"
				      property="currencyId"/>
			<bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				      property='<%= "symbols[" + index + "].symbol" %>'
				      scope="application"
				      filter="false"/>
            <bean:write name="total"
                        property="totalPaid"
                        formatKey="format.money"/>
        </td>
	</tr>
	<tr class="infoA">
		<td class="infoprompt"><bean:message key="process.run.prompt.totalNotPaid"/></td>
		<td class="infodata">	
			<bean:define id="index" name="total"
				      property="currencyId"/>
			<bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				      property='<%= "symbols[" + index + "].symbol" %>'
				      scope="application"
				      filter="false"/>
            <bean:write name="total"
                        property="totalNotPaid"
                        formatKey="format.money"/>
        </td>
	</tr>
	<tr class="infoB">
		<td class="infoprompt"><bean:message key="process.run.prompt.totalPaymentMethod"/></td>
		<td></td>
	</tr>
	<logic:iterate name="total" 
				   property="pmTotals"
				   id="methodTotal">

		<c:choose>
			<c:when test="${colorFlag == 1}">
				<tr class="listB">
				<c:remove var="colorFlag"/>
			</c:when>
			<c:otherwise>
				<tr class="listA">
				<c:set var="colorFlag" value="1"/>
			</c:otherwise>
	    </c:choose>
			<td><bean:write name="methodTotal" property="key"/></td>
			<td>
			<bean:define id="index" name="total"
				      property="currencyId"/>
			<bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				      property='<%= "symbols[" + index + "].symbol" %>'
				      scope="application"
				      filter="false"/>
				<bean:write name="methodTotal" property="value" 
							formatKey="format.money"/>
			</td>
	   </tr>
	</logic:iterate> <%-- end pm loop --%>
	</logic:equal>   <%-- end if it is not a review--%>
	
	</logic:iterate> <%-- end totals loop --%>

	<logic:equal name='<%=Constants.SESSION_PROCESS_DTO%>' 
				 property="isReview"
				 scope="session"
				 value="0" >
	
	<%-- Now show each run --%>
	<tr>
		<th class="info" colspan="2"><bean:message key="process.run.prompt"/></th>
	</tr>
	<logic:iterate name='<%=Constants.SESSION_PROCESS_DTO%>' 
				   property="runs"
				   id="run"
				   scope="session">
		<tr>
			<%-- a white bar to divide the run records --%>
			<td class="infocommands" colspan="2">&nbsp;</td>
		</tr>
		<tr class="infoB">
			<td class="infoprompt"><bean:message key="process.run.prompt.started"/></td>
			<td class="infodata">	
				<bean:write name="run" 
							property="started"
							formatKey="format.timestamp" />
			</td>
		</tr>
		<tr class="infoA">
			<td class="infoprompt"><bean:message key="process.run.prompt.finished"/></td>
			<td class="infodata">	
				<bean:write name="run" 
							property="finished"
							formatKey="format.timestamp" />
			</td>
		</tr>
		<tr class="infoB">
			<td class="infoprompt"><bean:message key="process.run.prompt.invoicesGenerated"/></td>
			<td class="infodata">	
				<bean:write name="run" 
							property="invoiceGenerated" />
			</td>
		</tr>
		
	<%-- Loop over each total, one per currency --%>
	<logic:iterate name="run"
				   property="totals"
				   id="total">
		
		<tr class="infoA">
			<td class="infoprompt"><bean:message key="currency.external.prompt.name"/></td>
			<td class="infodata">	
				<bean:write name="total" property="currencyName"/>
			</td>
		</tr>
		
		<tr class="infoB">
			<td class="infoprompt"><bean:message key="process.run.prompt.totalInvoiced"/></td>
			<td class="infodata">	
				<bean:define id="index" name="total"
				      property="currencyId"/>
				<bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				      property='<%= "symbols[" + index + "].symbol" %>'
				      scope="application"
				      filter="false"/>
				<bean:write name="total" 
							property="totalInvoiced"
							formatKey="format.money"/>
			</td>
		</tr>
		
		<tr class="infoA">
			<td class="infoprompt"><bean:message key="process.run.prompt.totalPaid"/></td>
			<td class="infodata">	
				<bean:define id="index" name="total"
				      property="currencyId"/>
				<bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				      property='<%= "symbols[" + index + "].symbol" %>'
				      scope="application"
				      filter="false"/>
				<bean:write name="total" 
							property="totalPaid"
							formatKey="format.money"/>
			</td>
		</tr>
		<tr class="infoB">
			<td class="infoprompt"><bean:message key="process.run.prompt.totalNotPaid"/></td>
			<td class="infodata">	
				<bean:define id="index" name="total"
				      property="currencyId"/>
				<bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				      property='<%= "symbols[" + index + "].symbol" %>'
				      scope="application"
				      filter="false"/>
				<bean:write name="total" 
							property="totalNotPaid"
							formatKey="format.money"/>
			</td>
		</tr>
		<tr class="infoA">
			<td class="infoprompt"><bean:message key="process.run.prompt.totalPaymentMethod"/></td>
			<td></td>
		</tr>
		<c:remove var="colorFlag"/>
		<logic:iterate name="total" 
					   property="pmTotals"
					   id="methodTotal">
			<c:choose>
				<c:when test="${colorFlag == 1}">
					<tr class="listB">
					<c:remove var="colorFlag"/>
				</c:when>
				<c:otherwise>
					<tr class="listA">
					<c:set var="colorFlag" value="1"/>
				</c:otherwise>
			</c:choose>
				<td><bean:write name="methodTotal" property="key"/></td>
			<td>
			    <bean:define id="index" name="total"
				      property="currencyId"/>
			    <bean:write name='<%= Constants.APP_CURRENCY_SYMBOLS %>'
				      property='<%= "symbols[" + index + "].symbol" %>'
				      scope="application"
				      filter="false"/>
				<bean:write name="methodTotal" property="value" 
							formatKey="format.money"/>
			</td>
		   </tr>
		</logic:iterate> <%-- end loop over pms --%>
  	  </logic:iterate>	<%-- end loop over totals --%>
	</logic:iterate><%-- end loop over runs --%>
	</logic:equal>  <%-- end if it is not a review--%>
</table>
</logic:present>
