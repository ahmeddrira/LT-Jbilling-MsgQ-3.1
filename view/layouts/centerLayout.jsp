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

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="jbilling" %>


<%-- Centered Layout Tiles 
  This layout render a heade tile, body and footer.
  Created initially for the login screen

  @param header Header tile (jsp url or definition name)
  @param body Body or center tile
  @param footer Footer tile
--%>

<HTML>
  <HEAD>
    <title><tiles:getAsString name="title"/></title>
    <link rel="stylesheet" type="text/css" 
  		href="<jbilling:getPreference preferenceId='<%=Constants.PREFERENCE_CSS_LOCATION%>'/>" /> 
  </HEAD>

<body>

<p align="center">
<table class="body">
<tr>
  <td valign="top">
    <tiles:insert attribute='body' />
  </td>
</tr>
<tr>
  <td colspan="3">
    <tiles:insert attribute="footer" />
  </td>
</tr>
</table>
</p>

</body>
</html>
