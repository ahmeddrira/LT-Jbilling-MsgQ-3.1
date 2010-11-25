<%--
  Layout for labeled and styled checkbox input elements.

  Usage:

    <g:applyLayout name="form/checkbox">
        <content tag="label">Checkbox Label</content>
        <content tag="name">element_name</content>
        
        <!-- optional -->
        <content tag="checked">true</content>
    </g:applyLayout>


  @author Brian Cowdery
  @since  25-11-2010
--%>

<div class="row">
    <label>&nbsp;</label>    
    <input type="checkbox" class="cb check" onclick="selectSingleCheckbox(this)"
           name="<g:pageProperty name="page.name"/>"
           id="<g:pageProperty name="page.name"/>"
           <g:if test="${pageProperty(name:'page.checked')}">checked</g:if>/>
    <label for="<g:pageProperty name="page.name"/>" class="lb"><g:pageProperty name="page.label"/></label>
</div>
