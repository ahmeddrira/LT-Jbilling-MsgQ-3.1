
<script type="text/javascript">
    function toggleEditor(id) {
        $('#' + id + '-editor').toggle('blind');
        $('#' + id).toggleClass('active');
    }
</script>

<div id="review-box">
    <div class="box no-heading">
        <div class="header">
            <div class="column">
                <h1>Order #</h1>
            </div>

            <div class="column">
                <h2>Monthly Post-Paid</h2>
                <h3>01-Jan-2011 <span>to</span> 01-Jan-2012</h3>
            </div>

            <div style="clear: both;"></div>
        </div>

        <hr/>

        <ul id="order-lines">
            <li id="line-1" class="line">
                <span class="description">Drink</span>
                <span class="sub-total">US$ 5.00</span>
                <span class="qty-price">1 x $5.00</span>
            </li>
            <li id="line-2" class="line">
                <span class="description">Drink Pass</span>
                <span class="sub-total">US$ 7.50</span>
                <span class="qty-price">1 x $2.50</span>
            </li>
            <li id="line-3" class="line active">
                <span class="description">Lemonade</span>
                <span class="sub-total">...</span>
                <span class="qty-price">...</span>
            </li>
            <li id="line-3-editor" class="editor">
                <div class="box">
                    <div class="form-columns">
                        <g:applyLayout name="form/input">
                            <content tag="label">Quantity</content>
                            <g:textField name="quantity" class="field" value="1"/>
                        </g:applyLayout>

                        <g:applyLayout name="form/input">
                            <content tag="label">Price US$</content>
                            <g:textField name="price" class="field" value="2.50"/>
                        </g:applyLayout>
                    </div>
                </div>
                <div class="btn-box">
                    <a class="submit save" onclick="toggleEditor('line-3');"><span><g:message code="button.add"/></span></a>
                    <a class="submit cancel"><span><g:message code="button.remove"/></span></a>
                </div>
            </li>
        </ul>

        <hr/>

        <div class="total">
            Total = US$ 13.50
        </div>
    </div>

    <div class="btn-box">
        <a class="submit save"><span><g:message code="button.save"/></span></a>
        <a class="submit cancel"><span><g:message code="button.cancel"/></span></a>
    </div>
</div>