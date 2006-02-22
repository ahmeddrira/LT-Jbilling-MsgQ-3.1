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

<p class="instr">
   <bean:message key="all.prompt.help" />
   <jbilling:help page="partners" anchor="defaults">
		 <bean:message key="all.prompt.here" />
   </jbilling:help>
</p>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<jbilling:getOptions generalPeriod="true"/>
<jbilling:getOptions currencies="true"/>

<html:form action="/partnerDefaults?action=edit&mode=partnerDefault" >
  <table class="form">
	  <tr class="form">
		  <td>
			<jbilling:help page="partners" anchor="parameter.rate">
		      <img border="0" src="/billing/graphics/help.gif"/>
		    </jbilling:help>
		  </td>
		  <td class="form_prompt"><bean:message key="partner.prompt.rate"/></td>
		  <td colspan="2"><html:text property="rate" /></td>
	  </tr>
	  <tr class="form">
		  <td>
			<jbilling:help page="partners" anchor="parameter.fee">
		      <img border="0" src="/billing/graphics/help.gif"/>
		    </jbilling:help>
		  </td>
		  <td class="form_prompt"><bean:message key="partner.prompt.fee"/></td>
		  <td colspan="2"><html:text property="fee" /></td>
	  </tr>
	  <tr class="form">
		  <td>
			<jbilling:help page="partners" anchor="parameter.currency">
		      <img border="0" src="/billing/graphics/help.gif"/>
		    </jbilling:help>
		  </td>
	      <td class="form_prompt"><bean:message key="partner.prompt.feeCurrency"/></td>
	 	  <td colspan="2">
	 		<html:select property="fee_currency">
				<html:options collection='<%=Constants.PAGE_CURRENCIES%>' 
							  property="code"
							  labelProperty="description"/>
			</html:select>
          </td>
	  </tr>
	 	<tr class="form">
		  <td>
			<jbilling:help page="partners" anchor="parameter.oneTime">
		      <img border="0" src="/billing/graphics/help.gif"/>
		    </jbilling:help>
		  </td>
	 		<td class="form_prompt"><bean:message key="partner.prompt.onetime"/></td>
	 		<td colspan="2"><html:checkbox property="chbx_one_time"/></td>
	 	</tr>
	 	<tr class="form">
		  <td>
			<jbilling:help page="partners" anchor="parameter.period">
		      <img border="0" src="/billing/graphics/help.gif"/>
		    </jbilling:help>
		  </td>
	 		<td class="form_prompt"><bean:message key="partner.prompt.period"/></td>
	 		<td><html:text property="period_value" size="2" /></td>
	 		<td>
	 			<html:select property="period_unit_id">
					<html:options collection='<%=Constants.PAGE_GENERAL_PERIODS%>' 
								  property="code"
								  labelProperty="description"/>
				</html:select>
            </td>
	 	</tr>
	 	<tr class="form">
		  <td>
			<jbilling:help page="partners" anchor="parameter.batch">
		      <img border="0" src="/billing/graphics/help.gif"/>
		    </jbilling:help>
		  </td>
	 		<td class="form_prompt"><bean:message key="partner.prompt.process"/></td>
	 		<td colspan="2"><html:checkbox property="chbx_process"/></td>
	 	</tr>
	 	<tr class="form">
		  <td>
			<jbilling:help page="partners" anchor="parameter.clerk">
		      <img border="0" src="/billing/graphics/help.gif"/>
		    </jbilling:help>
		  </td>
	 		<td class="form_prompt"><bean:message key="partner.prompt.clerk"/></td>
	 		<td colspan="2"><html:text property="clerk" size="5" /></td>
	 	</tr>
	  
	<tr class="form">
		<td></td>
		<td colspan="3" class="form_button">
			<html:submit styleClass="form_button">
                <bean:message key="all.prompt.submit"/>                
            </html:submit>
        </td>
	</tr>

  </table>
</html:form>
