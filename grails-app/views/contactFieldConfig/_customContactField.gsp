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

<%@ page import="com.sapienter.jbilling.server.util.db.EnumerationDTO" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<div class="form-edit" >

    <div class="heading">
        <strong><g:message code="configuration.title.contact.fields"/></strong>
    </div>

    <div class="form-hold">
        <g:form name="save-customcontactfields-form" action="save">
        
            <g:set var="dataTypeList" value="${['','String','Integer', 'Decimal', 'Boolean']}"/>
            <%
                dataTypeList.addAll(EnumerationDTO.list().collect{it.name})
            %>
            
            <g:hiddenField name="recCnt" value="${types?.size()}"/>
            <fieldset>
            
                <div class="form-columns single">
                
                    <table cellpadding="0" cellspacing="0" id="custom_fields" class="innerTable" width="100%">
                        <thead class="innerHeader">
                             <tr>
                                <th class="tiny"><g:message code="contact.field.type.id"/></th>
                                <th class="medium"><g:message code="contact.field.name"/></th>
                                <th class="medium"><g:message code="contact.field.datatype"/></th>
                                <th class="medium"><g:message code="contact.field.isReadOnly"/></th>
                                <th class="medium"><g:message code="contact.field.displayInView"/></th>
                             </tr>
                         </thead>
                         
                         <tbody>
                         
                            <g:each status="iter" var="type" in="${types}">
                                <tr>
                                    <td class="tiny">
                                        ${type.id}
                                    </td>
                                    
                                    <td class="medium">
                                        <g:textField
                                            class="field" style="float: right;width: 150px" 
                                            name="obj[${iter}].description" 
                                            value="${type.getDescriptionDTO(session['language_id'])?.content}"/>
                                            
                                    </td>
                                    
                                    <td class="medium">
                                        <g:select 
                                            class="field" 
                                            style="float: right; position: relative; width:120px" 
                                            name="obj[${iter}].dataType" 
                                            from="${dataTypeList}"
                                            value="${type?.dataType}" />
                                    </td>
                                    
                                    <td class="medium">
                                    
                                        <g:select 
                                            class="field" 
                                            style="float: right; width: 100px;position: relative;" 
                                            name="obj[${iter}].readOnly" 
                                            keys="[1,0]" 
                                            from="['Yes', 'No']" 
                                            value="${type.readOnly}"/>
                                            
                                        <g:hiddenField name="obj[${iter}].companyId" value="${session['company_id']}"/>
                                        <g:hiddenField name="obj[${iter}].id" value="${type.id}"/>
                                    </td>
                                    
                                    <td class="medium">
                                    
                                        <g:checkBox 
                                            class="cb checkbox" 
                                            style="float: right; width: 100px;position: relative;" 
                                            name="obj[${iter}].displayInView" 
                                            value="${(type?.displayInView?.intValue() > 0) ? true :false}"/>
                                    
                                    </td>
                                    
                                </tr>
                            </g:each>
                            
                            <tr>
                                <td class="tiny"></td>
                                <td class="medium">
                                    <g:textField
                                        class="field" style="float: right;width: 150px" 
                                        name="description" 
                                        value=""/>
                                </td>
                                <td class="medium">
                                
                                    <g:select 
                                        class="field" 
                                        style="float: right; position: relative; width:120px" 
                                        name="dataType" 
                                        from="${dataTypeList}" 
                                        value="" />
                                        
                                </td>
                                
                                <td class="medium">
                                
                                    <g:select 
                                        class="field" 
                                        style="float: right; width: 100px;position: relative;" 
                                        name="readOnly" 
                                        keys="[1,0]" 
                                        from="['Yes', 'No']" 
                                        value=""/>
                                        
                                    <g:hiddenField name="companyId" value="${session['company_id']}"/>
                                </td>
                                
                                <td class="medium">

                                    <g:checkBox 
                                            class="cb checkbox" 
                                            style="float: right; width: 100px;position: relative;" 
                                            name="displayInView" 
                                            value="${false}"/>
                                    
                                    </td>
                                
                            </tr>
                        </tbody>
                        
                    </table>
                    
                </div>
                
                <!-- spacer -->
                <div>
                    <br/>&nbsp;
                </div>
                
                
            </fieldset>
            
        </g:form>
    </div>

     <div class="btn-box">
         <div class="row">
            <a onclick="$('#save-customcontactfields-form').submit();" class="submit save"><span><g:message code="button.save"/></span></a>
            <g:link controller="config" action="index" class="submit cancel"><span><g:message code="button.cancel"/></span></g:link>
        </div>
    </div>

</div>