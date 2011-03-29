<%@page import="com.sapienter.jbilling.server.process.db.PeriodUnitDTO" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<div class="form-edit" style="width:450px">

    <div class="heading">
        <strong><g:message code="orderPeriod.title"/></strong>
    </div>

    <div class="form-hold">
        <g:form name="save-orderPeriods-form" action="save">
            <g:hiddenField name="recCnt" value="${periods?.size()}"/>
            <fieldset>
                <div class="form-columns" style="width:450px">
                    <div class="one_column" style="padding-right: 0px;width:420px">
                    <table class="innerTable" id="custom_fields" style="width: 100%;">
                        <thead class="innerHeader">
                             <tr>
                                <th class="small"><g:message code="orderPeriod.id"/></th>
                                <th class="medium"><g:message code="orderPeriod.value"/></th>
                                <th class="medium"><g:message code="orderPeriod.unit"/></th>
                                <th class="large"><g:message code="orderPeriod.description"/></th>
                                <th></th>
                             </tr>
                         </thead>
                         <tbody>
                            <g:each status="iter" var="period" in="${periods}">
                                <tr>
                                    <td class="small" style="text-align: center;">${period.id}</td>
                                    <td class="medium">
                                        <g:textField class="field numericOnly" style="float: right;width: 50px" 
                                            name="obj[${iter}].value" value="${period.value}"/>
                                    </td>
                                    <td class="medium">
                                        <g:select style="float: right; width: 100px;" class="field" 
                                            name="obj[${iter}].periodUnitId" from="${PeriodUnitDTO.list()}" 
                                            optionKey="id" optionValue="${{it.getDescription(session['language_id'])}}"
                                            value="${period?.periodUnit?.id}"/>
                                        <g:hiddenField name="obj[${iter}].entityId" value="${period.company?.id}"/>
                                        <g:hiddenField name="obj[${iter}].id" value="${period.id}"/>
                                    </td>
                                    <td class="large">
                                        <g:textField class="field" style="float: right;" 
                                            name="obj[${iter}].description" value="${period.getDescriptionDTO(session['language_id'])?.content}"/>
                                    </td>
                                    <td>
                                        <a onclick="showConfirm('remove-${period.id}');" class="delete" style="
                                            width:9px;
                                            height:9px;
                                            text-indent:-9999px;
                                            overflow:hidden;
                                            background:url(../images/icon03.gif) no-repeat;
                                            float:right;
                                            margin:11px 8px 0 0;
                                            padding:0;"/>
                                        <g:render template="/confirm" model="['message': 'config.period.delete.confirm',
                                                  'controller': 'orderPeriod',
                                                  'action': 'remove',
                                                  'id': period.id,
                                                  'ajax': false,
                                                  'onYes': 'closePanel(\'#column2\')'
                                                 ]"/>
                                    </td>
                                </tr>
                            </g:each>
                            <tr>
                                <td class="small" style="text-align: center;"><g:message code="orderPeriod.new"/></td>
                                <td class="medium">
                                    <g:textField class="field numericOnly" style="float: right;width: 50px" 
                                        name="value" value=""/>
                                </td>
                                <td class="medium">
                                    <g:select style="float: right; width: 100px;" class="field" 
                                        name="periodUnitId" from="${PeriodUnitDTO.list()}" 
                                        optionKey="id" optionValue="${{it.getDescription(session['language_id'])}}"
                                        value=""/>
                                </td>
                                <td class="large">
                                    <g:textField class="field" style="float: right;" 
                                        name="description" value=""/>
                                </td>
                                <td></td>
                            </tr>
                         </tbody>
                    </table>
                    </div>
                    <div class="row">&nbsp;<br></div>
                </div>
            </fieldset>
            <div class="btn-box">
                <a onclick="$('#save-orderPeriods-form').submit();" class="submit save"><span><g:message code="button.save"/></span></a>
                <g:link controller="config" action="index" class="submit cancel"><span><g:message code="button.cancel"/></span></g:link>
            </div>
       </g:form>
   </div>
</div>
<script type="text/javascript">
    $(function() {
        $(".numericOnly").keydown(function(event){
        	// Allow only backspace, delete, left & right 
            if ( event.keyCode==37 || event.keyCode== 39 || event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 ) {
                // let it happen, don't do anything
            }
            else {
                // Ensure that it is a number and stop the keypress
                if (event.keyCode < 48 || event.keyCode > 57 ) {
                    event.preventDefault(); 
                }   
            }
        });
    });
</script>