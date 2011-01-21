<%@ page import="com.sapienter.jbilling.server.user.db.CompanyDTO; com.sapienter.jbilling.server.item.db.ItemTypeDTO" %>

<div id="product-box">
    <!-- filter -->
    <div class="form-columns">
        <g:applyLayout name="form/input">
            <content tag="label">Filter By</content>
            <content tag="label.for">filterBy</content>
            <g:textField name="filterBy" class="field" value="ID, Name or Description"/>
        </g:applyLayout>

        <g:applyLayout name="form/select">
            <content tag="label">Product Category</content>
            <content tag="label.for">categoryId</content>
            <g:select name="currencyId"
                    from="${CompanyDTO.get(session['company_id']).itemTypes}"
                    optionKey="id" optionValue="description"/>
        </g:applyLayout>
    </div>

    <!-- product list -->
    <div class="table-box tab-table">
        <div class="table-scroll">
            <table id="products" cellspacing="0" cellpadding="0">
                <tbody>

                <tr>
                    <td>
                        Drink
                    </td>
                    <td class="medium">US$ 5.00</td>
                </tr>

                <tr>
                    <td>Lemonaid</td>
                    <td class="medium">US$ 2.50</td>
                </tr>


                <tr>
                    <td>Drink Pass</td>
                    <td class="medium">US$ 35.99</td>
                </tr>

                </tbody>
            </table>
        </div>
    </div>
</div>