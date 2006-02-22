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

<%@ page language="java" import="com.sapienter.jbilling.client.util.Constants, org.apache.struts.validator.DynaValidatorForm, com.sapienter.jbilling.client.list.PagedList"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
	
<logic:equal name='<%=Constants.REQUEST_LIST_IS_PAGED%>' 
			 scope="request"
			 value = "true">

<html:errors/>
<logic:notPresent name='<%=Constants.SESSION_PAGED_LIST%>' 
	   property="doSearch">			
<p class="title">
	<bean:message name='<%=Constants.SESSION_PAGED_LIST%>' 
	              property="titleKey"/>
</p> 


<%-- customers don't need to search on any list --%>
<logic:notEqual name='<%=Constants.SESSION_USER_DTO%>'
					 property="mainRoleId"
					 scope="session"
					 value='<%=Constants.TYPE_CUSTOMER.toString()%>'>	
<p align="center">	
<table class="listSearch">
<logic:iterate name='<%=Constants.SESSION_PAGED_LIST%>' 
			   scope="session"
			   id="field"
			   property="fields">
	<html:form action="/genericListSelect?action=search">	
		<input type="hidden" name="fieldId"
			value="<bean:write name="field" property="id"/>"/>
		<input type="hidden" name="dataType"
			value="<bean:write name="field" property="dataType"/>"/>
		<input type="hidden" name="key"
			value="<bean:write name="field" property="titleKey"/>"/>
		<input type="hidden" name="listType"
			value="<bean:write name='<%=Constants.SESSION_PAGED_LIST%>' 
				               property="listId"/>"/>
						   
	<tr class="listSearch">
		<td><bean:message name="field" property="titleKey"/></td>
		<td>
		<logic:equal name="field" property="dataType" value="integer">
			<html:text property="search1" size="9" value=" "/>
		</logic:equal>
		<logic:equal name="field" property="dataType" value="string">
			<html:text property="search1" size="30" value=""/>
		</logic:equal>
		<logic:equal name="field" property="dataType" value="date">
			<table> 
				<tr class="listSearch">
					<td><bean:message key="list.prompt.from"/></td>
					<jbilling:dateFormat format="mm-dd">
					<td><html:text property="search1" size="2" value="" maxlength="2"/>/</td>
					<td><html:text property="search2" size="2" value="" maxlength="2"/>/</td>
					</jbilling:dateFormat>
					<jbilling:dateFormat format="dd-mm">
					<td><html:text property="search2" size="2" value="" maxlength="2"/>/</td>
					<td><html:text property="search1" size="2" value="" maxlength="2"/>/</td>
					</jbilling:dateFormat>
					<td><html:text property="search3" size="4" value="" maxlength="4"/></td>
				</tr>
				<tr class="listSearch">
					<td><bean:message key="list.prompt.to"/></td>
					<jbilling:dateFormat format="mm-dd">
					<td><html:text property="search4" size="2" value="" maxlength="2"/>/</td>
					<td><html:text property="search5" size="2" value="" maxlength="2"/>/</td>
					</jbilling:dateFormat>
					<jbilling:dateFormat format="dd-mm">
					<td><html:text property="search5" size="2" value="" maxlength="2"/>/</td>
					<td><html:text property="search4" size="2" value="" maxlength="2"/>/</td>
					</jbilling:dateFormat>
					<td><html:text property="search6" size="4" value="" maxlength="4"/></td>
				</tr>
				<tr class="listSearch">
					<td></td>
					<td colspan="3"><bean:message key="all.prompt.dateFormat"/></td>
				</tr>
			</table>
		</logic:equal>
		</td>
		<td>
			<input class="form_button" type="submit" value="<bean:message key="list.prompt.serach"/>"/>
		</td>
	</tr>
	</html:form>
 </logic:iterate>
</table>
</p>
</logic:notEqual>
		
<table class="listControls">	
	<%-- customers don't get to change their page size --%>
	<logic:notEqual name='<%=Constants.SESSION_USER_DTO%>'
						 property="mainRoleId"
						 scope="session"
						 value='<%=Constants.TYPE_CUSTOMER.toString()%>'>	
	<tr class="listControls"><td class="listControls">
		<table class="listControls"><tr class="listControls">
		<td class="listControls">
			<bean:message key="list.prompt.page"/>&nbsp;
			<bean:write name='<%=Constants.SESSION_PAGED_LIST%>' 
				property="pageNumber"/>
			<logic:present name='<%=Constants.SESSION_PAGED_LIST%>' 
					property="numberOfPages">
				&nbsp;<bean:message key="list.prompt.pageOf"/>&nbsp;
				<bean:write name='<%=Constants.SESSION_PAGED_LIST%>' 
					property="numberOfPages"/>
			</logic:present>
		</td>
		<html:form action="/genericListSelect?action=size">	
		<td class="listControls">
			<%
			DynaValidatorForm myForm = (DynaValidatorForm) session.getAttribute("list");
			myForm.set("pageSize", ((PagedList)session.getAttribute(
					Constants.SESSION_PAGED_LIST)).getPageSize().toString());
			%>
			<bean:message key="list.prompt.pageSize"/>
			<html:text property="pageSize" size="3" maxlength="3"/>
			<input class="form_button" type="submit" 
				value="<bean:message key="list.prompt.change"/>"/>
		</td>
		</html:form>
		</tr></table>
	 </td></tr>	 
	 </logic:notEqual>
	 
	 <tr class="listControls"><td class="listControls">
	 	<table class="listControls"><tr class="listControls">
		 <td class="listControls" style="text-align: left;">
		 	<html:link page="/genericListSelect.do?action=prev"
		 		paramId="listId"
		 		paramName='<%=Constants.SESSION_PAGED_LIST%>' 
		 		paramProperty="listId"
		 		paramScope="session">
				<bean:message key="list.prompt.prev"/>
			</html:link> 
		 </td>
		 <td  class="listControls" style="text-align: center;">
		 	<html:link page="/genericListSelect.do?action=inverse"
		 		paramId="listId"
		 		paramName='<%=Constants.SESSION_PAGED_LIST%>' 
		 		paramProperty="listId"
		 		paramScope="session">
				<bean:message key="list.prompt.inverse"/>
			</html:link> 
		 </td>
		 <td  class="listControls"style="text-align: right;">
		 	<html:link page="/genericListSelect.do?action=next"
		 		paramId="listId"
		 		paramName='<%=Constants.SESSION_PAGED_LIST%>' 
		 		paramProperty="listId"
		 		paramScope="session">
				<bean:message key="list.prompt.next"/>
			</html:link> 
		 </td>
		 </tr></table>
	 </td></tr>
 </table>
</logic:notPresent>
<logic:present name='<%=Constants.SESSION_PAGED_LIST%>' 
	   property="doSearch">	
   <p class="instr"><br/><bean:message key="list.search.results"/></p>
   <p>
   <html:link page="/genericListSelect.do?action=back">
		<bean:message key="list.link.back"/>
   </html:link>
   </p>
</logic:present>
</logic:equal>

<table class="list">
	<tr class="listH">
		<logic:iterate name='<%=Constants.REQUEST_LIST_COLUMNS%>' 
                           id="columnName">
                  <td><bean:message name="columnName"/></td>      
		</logic:iterate>
		<td></td>
	</tr>	

	<html:form action="/genericListSelect">	
		<input type="hidden" name='<%=Constants.REQUEST_LIST_TYPE%>' 
			value="<bean:write name='<%=Constants.REQUEST_LIST_TYPE%>'  scope="request"/>"/>
		   
		<input type="hidden" name='<%=Constants.REQUEST_LIST_METHOD%>' 
			value="<bean:write name='<%=Constants.REQUEST_LIST_METHOD%>' scope="request" />"/>
			
		<input type="hidden" name='<%=Constants.REQUEST_FORWARD_FROM%>' 
			value="<bean:write name='<%=Constants.SESSION_FORWARD_FROM%>' scope="session" />"/>
		<input type="hidden" name='<%=Constants.REQUEST_FORWARD_TO%>' 
			value="<bean:write name='<%=Constants.SESSION_FORWARD_TO%>' scope="session" />"/>
		<c:set var="flag" value="1"/>
		<jbilling:genericList>
			<c:choose>
				<c:when test="${flag == 1}">
					<tr class="listA">
					<c:remove var="flag"/>
				</c:when>
				<c:otherwise>
					<tr class="listB">
					<c:set var="flag" value="1"/>
				</c:otherwise>
		    </c:choose>
				
				<jbilling:insertDataRow/>

				<td class="list">
				<input type="radio" name='<%=Constants.REQUEST_SELECTION_ID%>' 
					value="<bean:write name="rowID"/>" onclick="form.submit()"/>
				</td>
			</tr>
		</jbilling:genericList>
		<noscript>
		<tr>
			<td colspan="4" align="center">
				<input type="submit" 
					value="<bean:message key="all.prompt.submit"/>"/>
			</td>
		</tr>
		</noscript>
	</html:form>
	
</table>
