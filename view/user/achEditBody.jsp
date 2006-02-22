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

<logic:notPresent parameter="confirm">
   <p class="title">
	   <bean:message key="ach.edit.title" />
   </p>
   <p class="instr">
	   <bean:message key="ach.edit.instr" />
   </p>
   
   <html:link page="/user/achEdit.jsp?confirm=do">
   	  <bean:message key="all.prompt.delete" />
   </html:link>
</logic:notPresent>

<html:errors/>
<html:messages message="true" id="myMessage">
	<p><bean:write name="myMessage"/></p>
</html:messages>

<logic:present parameter="confirm">
	<logic:equal parameter="confirm" value="do">
		  <p>
		      <bean:message key="ach.delete.confirm"/> <br/>
              <html:link page="/achMaintain.do?action=delete&mode=ach">
              	   <bean:message key="all.prompt.yes"/>
              </html:link><br/>
              <html:link page="/user/achEdit.jsp?confirm=no">
              	   <bean:message key="all.prompt.no"/>
              </html:link><br/>
   	    </p>
	</logic:equal>
	
	<logic:equal parameter="confirm" value="no">
          <bean:message key="ach.delete.notDone"/>
	</logic:equal>	
</logic:present>

<html:form action="/achMaintain?action=edit&mode=ach" >
     <table class="form">
      	  <tr class="form">
	 		  <td class="form_prompt"><bean:message key="ach.aba.prompt"/></td>
	 		  <td colspan="4"><html:text property="aba_code" size="9" /></td>
	 	  </tr>
      	  <tr class="form">
	 		  <td class="form_prompt"><bean:message key="ach.account_number.prompt"/></td>
	 		  <td colspan="4"><html:text property="account_number" size="20" /></td>
	 	  </tr>
      	  <tr class="form">
	 		  <td class="form_prompt"><bean:message key="ach.bank_name.prompt"/></td>
	 		  <td colspan="4"><html:text property="bank_name" size="30" /></td>
	 	  </tr>
      	  <tr class="form">
	 		  <td class="form_prompt"><bean:message key="ach.account_name.prompt"/></td>
	 		  <td colspan="4"><html:text property="account_name" size="30" /></td>
	 	  </tr>
      	  <tr class="form">
	 		  <td class="form_prompt"><bean:message key="ach.account_type.prompt"/></td>
	 		  <td><bean:message key="ach.account_type.chq.prompt"/></td>
	 		  <td><html:radio property="account_type" value="1"/></td>
	 		  <td><bean:message key="ach.account_type.sav.prompt"/></td>
	 		  <td><html:radio property="account_type" value="2"/></td>
	 	  </tr>
	 	  
      	  <tr class="form">
	 		  <td class="form_prompt"><bean:message key="creditcard.usethis.prompt"/></td>
	 		  <td colspan="4"><html:checkbox property="chbx_use_this"/></td>
	 	  </tr>
	 	  <tr class="form">
	 	  	 <td class="form_button" colspan="5">
	 	         <html:submit styleClass="form_button">
	 	         	<bean:message key="all.prompt.submit"/>
	 	         </html:submit>
	 	     </td>
	 	  </tr>
	</table>
</html:form>