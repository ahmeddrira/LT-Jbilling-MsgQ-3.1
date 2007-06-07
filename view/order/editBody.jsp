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

<html:errors/>

<%-- I will need to convert the id to string, so I ask for the map to be in the session --%>
<jbilling:getOptions orderPeriod="true" inSession="true"/>
<jbilling:getOptions billingType="true" inSession="true"/>
<jbilling:getOptions generalPeriod="true"/>

<%-- Define if the period will be editable or not --%>
<bean:define id="jsp_period_editable" value="yes"/>
<logic:present name='<%=Constants.SESSION_ORDER_DTO%>' 
			  scope="session"
			  property="invoices">
   <logic:iterate name='<%=Constants.SESSION_ORDER_DTO%>' 
						   scope="session"
						   id="invoice"
						   property="invoices"
						   length="1">
       <bean:define id="jsp_period_editable" value="no"/>
   </logic:iterate>
</logic:present>

<html:form action="/orderMaintain?action=edit&mode=order" >
	<table class="form">
		<tr class="form">
			<td>
				 <jbilling:help page="orders" anchor="period">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
			<td class="form_prompt"><bean:message key="order.prompt.period"/></td>
			<td>
				<logic:equal name="jsp_period_editable" value="yes">
		          <html:select property="period">
		          <html:options collection='<%=Constants.PAGE_ORDER_PERIODS%>' 
				             property="code"
				            labelProperty="description"	/>
		          </html:select>
		        </logic:equal>
				<logic:equal name="jsp_period_editable" value="no">
					<bean:write name='<%=Constants.SESSION_ORDER_DTO%>' 
										property="periodStr" 
										scope="session"/>
		        </logic:equal>
             </td>
		</tr>
			<tr class="form">
				<td>
					 <jbilling:help page="orders" anchor="type">
						 <img border="0" src="/billing/graphics/help.gif"/>
					 </jbilling:help>
				</td>
				<td class="form_prompt"><bean:message key="order.prompt.billingType"/></td>
				<td>
				   <logic:equal name="jsp_period_editable" value="yes">
		               <html:select property="billingType">
			               <html:options collection='<%=Constants.PAGE_BILLING_TYPE%>' 
				             property="code"
				            labelProperty="description"	/>
		              </html:select>
		        </logic:equal>
				<logic:equal name="jsp_period_editable" value="no">
			         <bean:write name='<%=Constants.SESSION_ORDER_DTO%>' 
				                    property="billingTypeStr" 
				                    scope="session"/>
		        </logic:equal>
             </td>
		</tr>
		<tr class="form">
			<td>
				 <jbilling:help page="orders" anchor="promoCode">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
			<td class="form_prompt"><bean:message key="order.prompt.promotion"/></td>
			<td>
				<html:text property="promotion_code"/>
			</td>
		</tr>

	 	<tr class="form">
			<td>
				 <jbilling:help page="orders" anchor="active">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
	 		<td class="form_prompt"><bean:message key="order.prompt.activeSince"/></td>
	 		<td> <table> <tr class="form">
	 		<jbilling:dateFormat format="mm-dd">
		 		<td><html:text property="since_month" size="2" maxlength="2"/></td>
		 		<td><html:text property="since_day" size="2" maxlength="2"/></td>
	 		</jbilling:dateFormat>
	 		<jbilling:dateFormat format="dd-mm">
		 		<td><html:text property="since_day" size="2" maxlength="2"/></td>
		 		<td><html:text property="since_month" size="2" maxlength="2"/></td>
	 		</jbilling:dateFormat>
	 		<td><html:text property="since_year" size="4" maxlength="4"/></td>
	 		<td><bean:message key="all.prompt.dateFormat"/></td>
	 		</tr></table></td>
	 	</tr>
 	 	<tr class="form">
			<td>
				 <jbilling:help page="orders" anchor="active">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
	 		<td class="form_prompt"><bean:message key="order.prompt.activeUntil"/></td>
	 		<td> <table> <tr class="form">
	 		<jbilling:dateFormat format="mm-dd">
		 		<td><html:text property="until_month" size="2" maxlength="2"/></td>
		 		<td><html:text property="until_day" size="2" maxlength="2"/></td>
	 		</jbilling:dateFormat>
	 		<jbilling:dateFormat format="dd-mm">
		 		<td><html:text property="until_day" size="2" maxlength="2"/></td>
		 		<td><html:text property="until_month" size="2" maxlength="2"/></td>
	 		</jbilling:dateFormat>
	 		<td><html:text property="until_year" size="4" maxlength="4"/></td>
	 		<td><bean:message key="all.prompt.dateFormat"/></td>
	 		</tr></table></td>
	 	</tr>
       <%-- Avoid showing the next billiable day for new orders --%>	 	  
	 	<logic:present name='<%=Constants.SESSION_ORDER_DTO%>' 
			  scope="session">
	 	<tr class="form">
			<td>
				 <jbilling:help page="orders" anchor="active">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
	 		<td class="form_prompt"><bean:message key="order.prompt.nextBillableDay"/></td>
	 		<td> <table> <tr class="form">
	 		<jbilling:dateFormat format="mm-dd">
		 		<td><html:text property="next_billable_month" size="2" maxlength="2"/></td>
		 		<td><html:text property="next_billable_day" size="2" maxlength="2"/></td>
	 		</jbilling:dateFormat>
	 		<jbilling:dateFormat format="dd-mm">
		 		<td><html:text property="next_billable_day" size="2" maxlength="2"/></td>
		 		<td><html:text property="next_billable_month" size="2" maxlength="2"/></td>
	 		</jbilling:dateFormat>
	 		<td><html:text property="next_billable_year" size="4" maxlength="4"/></td>
	 		<td><bean:message key="all.prompt.dateFormat"/></td>
	 		</tr></table></td>
	 	</tr>
	 	</logic:present>
	 	<tr class="form">
			<td>
				 <jbilling:help page="orders" anchor="dueDate">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
			<td class="form_prompt"><bean:message key="user.prompt.dueDate"/></td>
			<td> <table> <tr class="form">
			<td >
		   		<html:text property="due_date_value" size="2" maxlength="2"/>
		   	</td>
		   	<td>
		   		<html:select property="due_date_unit_id">
			   		<html:options collection='<%=Constants.PAGE_GENERAL_PERIODS%>' 
				 		property="code"
						labelProperty="description"	/>
		  		</html:select>
 	    	</td>
 	    	<td>
 	    		<bean:message key="user.dueDate.help"/>
 	    	</td>
 	    	</tr></table></td>
		</tr>
		
		<jbilling:getPreference preferenceId='<%=Constants.PREFERENCE_USE_DF_FM%>'
				beanName="preference"/> 
		<logic:equal name="preference" value="1">
		<tr class="form">
			<td></td>
			<td class="form_prompt"><bean:message key="process.configuration.prompt.df_fm"/></td>
			<td>
			   <html:checkbox property="chbx_df_fm"/>
			</td>
		</tr>
		</logic:equal>

		<jbilling:getPreference preferenceId='<%=Constants.PREFERENCE_USE_ORDER_ANTICIPATION%>'
				beanName="preference"/> 
		<logic:equal name="preference" value="1">
		<tr class="form">
			<td></td>
			<td class="form_prompt"><bean:message key="order.prompt.anticipate"/></td>
			<td>
			   <html:text property="anticipate_periods" size="2"/>
			</td>
		</tr>
		</logic:equal>
		
		<jbilling:getPreference preferenceId='<%=Constants.PREFERENCE_ORDER_OWN_INVOICE%>'
				beanName="preference"/> 
		<logic:equal name="preference" value="1">
		<tr class="form">
			<td></td>
			<td class="form_prompt"><bean:message key="process.configuration.prompt.own_invoice"/></td>
			<td>
			   <html:checkbox property="chbx_own_invoice"/>
			</td>
		</tr>
		</logic:equal>
		
	 	<tr class="form">
			<td>
				 <jbilling:help page="orders" anchor="notify">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
			<td class="form_prompt"><bean:message key="order.prompt.notify"/></td>
			<td>
				<html:checkbox property="chbx_notify"/>
			</td>
		</tr>
	 	<tr class="form">
			<td>
				 <jbilling:help page="orders" anchor="notes">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
			<td class="form_prompt"><bean:message key="order.prompt.notes"/></td>
			<td>
				<html:textarea rows="3" cols="35" property="notes"/>
			</td>
		</tr>
	 	<tr class="form">
			<td>
				 <jbilling:help page="orders" anchor="notes">
					 <img border="0" src="/billing/graphics/help.gif"/>
				 </jbilling:help>
			</td>
			<td class="form_prompt"><bean:message key="order.prompt.notesInInvoice"/></td>
			<td>
				<html:checkbox property="chbx_notes"/>
			</td>
		</tr>
	 	
		<tr class="form">
		   <td></td>
		   <td class="form_button" colspan="2">
		    <html:submit styleClass="form_button">
		    	<bean:message key="order.prompt.submit"/>
		    </html:submit>
		   </td>
	    </tr>
	</table>
</html:form>


