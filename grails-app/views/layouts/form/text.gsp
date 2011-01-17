<%--
  Layout for labeled line of text (for use with values that should never be edited like IDs). You
  may also place a hidden field here as a convenient way of passing a record ID.

  Usage:

    <g:applyLayout name="form/input">
        <content tag="label">Field Label</content>
        ${textValue}
    </g:applyLayout>


  @author Brian Cowdery
  @since  20-Dec-2010
--%>

<div class="row">
    <label for="${pageProperty(name: 'page.label.for')}"><g:pageProperty name="page.label"/></label>
    <span><g:layoutBody/></span>
</div>