<html>
<head>
<title>Main Page</title>
<meta name="layout" content="main" />

</head>
<body>
            <div class="form-edit">
                <div class="heading">
                    <strong>
                    <g:message code="plugins.create.title"/>
                    </strong>
                </div>
                <div class="form-hold">
                    <g:form name="plugin-form" action="save">
                        <fieldset>
                            <div class="form-columns">
                                <div class="column">
                                    <div class="row">
                                        <p><g:message code="plugins.create.category"/></p>
                                        <span>${description}</span>
                                    </div>
                                    <div class="row">
                                        <p><g:message code="plugins.plugin.type"/></p>
                                        <span>
                                           <g:select name="typeId"
                                                     from="${types}"
                                                     optionKey="id"
                                                     optionValue="className"
                                                     value="${pluginws?.typeId}" />
                                        </span>
                                    </div>
                                    <div class="row">
                                        <label> <g:message code="plugins.plugin.order"/> </label>
                                        <div class="inp-bg inp4">
                                           <g:textField class="field" name="processingOrder" size="2" 
                                                        value="${pluginws?.processingOrder}" />
                                        </div>
                                    </div>
                                </div>
                            </div>
 
                            <!-- box cards -->
                            <div class="box-cards box-cards-open">
                                <div class="box-cards-title">
                                    <span style="float:left;padding-left:20px;">
                                         <g:message code="plugins.create.parameters"/>
                                    </span>
                                </div>
                                <g:render template="formParameters" model="[parametersDesc:parametersDesc]"/>
                            </div>
                            <!-- box text -->
                            <div class="box-text">
                                <g:textArea name="notes" rows="7" cols="63" value="${pluginws?.notes}" />
                            </div>
                            <div class="buttons">
                                <ul>
                                    <li><a class="submit save" onclick="$('#plugin-form').submit();" href="#">
                                        <span><g:message code="plugins.create.save"/></span>
                                    </a></li>
                                    <li><a class="submit cancel" href="#"><span>Cancel</span></a></li>
                                </ul>
                            </div>
                        </fieldset>
                    </g:form>
                </div>
            </div>
            
 <script type="text/javascript">
    $("#typeId").change(function() {
    	  var typeSelected = "";
          $("#typeId option:selected").each(function () {
              typeSelected = $(this).val();
          });
    	  $.post('/jbilling/plugin/getTypeParametersDescriptions', 
    	    	  {typeId: typeSelected},
    	    	  function(msg){
    	    	      $("#plugin-parameters").html(msg)
    	    	  });
    });
</script>
</body>
</html>