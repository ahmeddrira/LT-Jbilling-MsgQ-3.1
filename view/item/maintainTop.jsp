<%--
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>

<jbilling:permission permission='<%=Constants.P_ITEM_EDIT%>'>
<logic:notPresent parameter="confirm">
   <p class="title">
	   <bean:message key="item.edit.title" />
   </p>
   <p class="instr">
	   <bean:message key="item.edit.instr" />
   </p>
   <p class="instr">
	   <bean:message key="all.prompt.help" />
	   <jbilling:help page="items" anchor="overview">
	   		<bean:message key="all.prompt.here" />
	   </jbilling:help>
   </p>
   
   <logic:notPresent parameter="create">
   <html:link page="/item/maintain.jsp?confirm=do">
   	  <bean:message key="all.prompt.delete" />
   	</html:link>
   </logic:notPresent>

</logic:notPresent>

<logic:present parameter="confirm">
	<logic:equal parameter="confirm" value="do">
		  <p>
		      <bean:message key="item.delete.confirm"/> <br/>
              <html:link page="/itemMaintain.do?action=delete&mode=item">
              	   <bean:message key="all.prompt.yes"/>
              </html:link><br/>
              <html:link page="/item/maintain.jsp?confirm=no">
              	   <bean:message key="all.prompt.no"/>
              </html:link><br/>
   	    </p>
	</logic:equal>
	
	<logic:equal parameter="confirm" value="yes">
          <p><bean:message key="item.delete.done"/></p>
	</logic:equal>	
	<logic:equal parameter="confirm" value="no">
          <p><bean:message key="item.delete.notDone"/></p>
	</logic:equal>	

</logic:present>
</jbilling:permission>
