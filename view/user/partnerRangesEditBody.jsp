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

<p class="title">
<bean:message key="partner.ranges.edit.title"/>
</p>
<p class="instr">
<bean:message key="partner.ranges.edit.instr"/>
</p>

<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>
<html:errors/>


<html:form action="/partnerRangesMaintain?action=edit&mode=ranges">
  <table class="form">
  	<tr class="form">
  		<td class="form_prompt"><bean:message key="partner.ranges.prompt.from"/></td>
  		<td class="form_prompt"><bean:message key="partner.ranges.prompt.to"/></td>
  		<td class="form_prompt"><bean:message key="partner.prompt.rate"/></td>
  		<td class="form_prompt"><bean:message key="partner.prompt.fee"/></td>
  		<td></td>
  	</tr>
  	
  	<logic:iterate id="anId" name="partnerRanges" property="range_from"
			           scope="session" indexId="index">
	   <tr class="form">
	    	<td>
	    		<html:text property='<%= "range_from[" + index + "]" %>' size="2"/>
	    	</td>
	    	<td>
	    		<html:text property='<%= "range_to[" + index + "]" %>' size="2"/>
	    	</td>
	    	<td>
	    		<html:text property='<%= "percentage_rate[" + index + "]" %>' size="4"/>
	    	</td>
	    	<td>
	    		<html:text property='<%= "referral_fee[" + index + "]" %>' size="4"/>
	    	</td>
    	</tr>
    </logic:iterate>

	<tr><td colspan="4"  class="form_button">
		<html:submit styleClass="form_button">
			<bean:message key="all.prompt.submit"/>
		</html:submit>
	</td></tr>
  </table>
</html:form>