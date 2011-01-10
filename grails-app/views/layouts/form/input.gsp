<%--
  Layout for labeled and styled form input elements.

  Usage:

    <g:applyLayout name="form/input">
        <content tag="label">Field Label</content>
        <content tag="label.for">element_id</content>
        <input type="text" class="field" name="name" id="element_id"/>
    </g:applyLayout>

	style: Use a content tag 'style' to apply additional css class to layoutBody.

  @author Brian Cowdery
  @since  25-11-2010
--%>

<div class="row">    
    <label for="<g:pageProperty name="page.label.for"/>"><g:pageProperty name="page.label"/></label>
    <div class="inp-bg <g:pageProperty name="page.style"/>">
        <g:layoutBody/>
    </div>
</div>