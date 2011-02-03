<%--
  Layout for an attribute name / value pair.

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
    <label for="<g:pageProperty name="page.label.for"/>">
        ${pageProperty(name: 'page.label') ?: '&nbsp;'}
    </label>
    <div class="inp-bg inp4">
        <g:pageProperty name="page.name"/>
    </div>
    <div class="inp-bg inp4">
        <g:pageProperty name="page.value"/>
    </div>
    <g:layoutBody/>
</div>