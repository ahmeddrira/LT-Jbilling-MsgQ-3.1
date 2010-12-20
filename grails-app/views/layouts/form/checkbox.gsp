<%--
  Layout for labeled and styled checkbox input elements.

  Usage:

    <g:applyLayout name="form/checkbox">
        <content tag="label">Checkbox Label</content>
        <content tag="label.for">element_name</content>
        
        <g:checkbox name="element_name" class="cb check" ... />
    </g:applyLayout>


  @author Brian Cowdery
  @since  25-11-2010
--%>

<div class="row">
    <label>&nbsp;</label>    
    <g:layoutBody/>
    <label for="<g:pageProperty name="page.label.for"/>" class="lb"><g:pageProperty name="page.label"/></label>
</div>
