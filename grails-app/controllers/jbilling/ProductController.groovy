package jbilling

import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.item.ItemDTOEx
import com.sapienter.jbilling.server.item.ItemPriceDTOEx
import com.sapienter.jbilling.server.item.ItemTypeWS
import com.sapienter.jbilling.server.item.db.ItemDTO
import com.sapienter.jbilling.server.item.db.ItemTypeDTO
import com.sapienter.jbilling.server.user.db.CompanyDTO
import grails.plugins.springsecurity.Secured

@Secured(['isAuthenticated()'])
class ProductController {

    static pagination = [ max: 25, offset: 0 ]

    def webServicesSession
    def viewUtils
    def filterService
    def recentItemService
    def breadcrumbService

    def index = {
        redirect action: list, params: params
    }

    /**
     * Get a list of categories and render the "_categories.gsp" template. If a category ID is given as the
     * "id" parameter, the corresponding list of products will also be rendered.
     */
    def list = {
        def filters = filterService.getFilters(FilterType.PRODUCT, params)
        def categories = getCategories()
        def products = params.id ? getItemsByTypeId(params.int('id'), filters) : null
        def categoryId = params.int('id') ?: products ? products.get(0)?.itemTypes?.asList()?.get(0)?.id : null

        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))

        if (params.applyFilter) {
            render template: 'products', model: [ products: products, selectedCategoryId: categoryId ]
        } else {
            [ categories: categories, products: products, selectedCategoryId: categoryId, filters: filters, filterRender: 'second', filterAction: 'allProducts' ]
        }
    }

    def getCategories() {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        return ItemTypeDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            eq('entity', new CompanyDTO(session['company_id']))
        }
    }

    /**
     * Get a list of products for the given item type id and render the "_products.gsp" template.
     */
    def products = {
        if (params.id) {
            def filters = filterService.getFilters(FilterType.PRODUCT, params)
            def products = getItemsByTypeId(params.int('id'), filters)

            breadcrumbService.addBreadcrumb(controllerName, 'list', null, params.int('id'))

            if (!products) {
                flash.info = 'product.category.no.products.warning'
                flash.args = [ params.id ]
            }

            render template: 'products', model: [ products: products, selectedCategoryId: params.id ]
        }
    }

    def getItemsByTypeId(Integer id, filters) {
        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        return ItemDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            and {
                filters.each { filter ->
                    if (filter.value) {
                        switch (filter.constraintType) {
                            case FilterConstraint.EQ:
                                eq(filter.field, filter.value)
                                break

                            case FilterConstraint.LIKE:
                                like(filter.field, filter.stringValue)
                                break
                        }
                    }
                }
                itemTypes {
                    eq('id', id)
                }
                eq('deleted', 0)
                eq('entity', new CompanyDTO(session['company_id']))
            }
        }
    }

    /**
     * Get a list of ALL products regardless of the item type selected, and render the "_products.gsp" template.
     */
    def allProducts = {
        def filters = filterService.getFilters(FilterType.PRODUCT, params)

        params.max = params?.max?.toInteger() ?: pagination.max
        params.offset = params?.offset?.toInteger() ?: pagination.offset

        def products = ItemDTO.createCriteria().list(
                max:    params.max,
                offset: params.offset
        ) {
            and {
                filters.each { filter ->
                    if (filter.value) {
                        switch (filter.constraintType) {
                            case FilterConstraint.EQ:
                                eq(filter.field, filter.value)
                                break

                            case FilterConstraint.LIKE:
                                like(filter.field, filter.stringValue)
                                break
                        }
                    }
                }
                eq('deleted', 0)
                eq('entity', new CompanyDTO(session['company_id']))
            }
        }

        render template: 'products', model: [ products: products ]
    }

    /**
     * Show details of the selected product. By default, this action renders the entire list view
     * with the product category list, product list, and product details rendered. When rendering
     * for an AJAX request the template defined by the "template" parameter will be rendered.
     */
    def show = {
        ItemDTO product = ItemDTO.get(params.int('id'))
        recentItemService.addRecentItem(product?.id, RecentItemType.PRODUCT)
        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))

        if (params.template) {
            // render requested template, usually "_show.gsp"
            render template: params.template, model: [ selectedProduct: product, selectedCategoryId: params.category ]

        } else {
            // render default "list" view - needed so a breadcrumb can link to a product by id
            def filters = filterService.getFilters(FilterType.PRODUCT, params)
            def categories = getCategories();

            def productCategoryId = params.category ?: product?.itemTypes?.asList()?.get(0)?.id
            def products = getItemsByTypeId(productCategoryId, filters);

            render view: 'list', model: [ categories: categories, products: products, selectedProduct: product, selectedCategoryId: productCategoryId, filters: filters ]
        }
    }

    /**
     * Delete the given category id
     */
    def deleteCategory = {
        if (params.id) {
            webServicesSession.deleteItemCategory(params.int('id'))

            log.debug("Deleted item category ${params.id}.");

            flash.message = 'product.category.deleted'
            flash.args = [ params.id ]
        }

        render template: 'categories', model: [ categories: categories ]
    }

    /**
     * Delete the given product id
     */
    def deleteProduct = {
        if (params.id) {
            webServicesSession.deleteItem(params.int('id'))

            log.debug("Deleted item ${params.id}.");

            flash.message = 'product.deleted'
            flash.args = [ params.id ]
        }

        // call the rendering action directly instead of using 'chain' or 'redirect' which results
        // in a second request that clears the flash messages.
        if (params.category) {
            // return the products list, pass the category so the correct set of products is returned.
            params.id = params.category
            products()
        } else {
            // no category means we deleted from the 'allProducts' view
            allProducts()
        }
    }

    /**
     * Get the item category to be edited and show the "editCategory.gsp" view. If no ID is given
     * this view will allow creation of a new category.
     */
    def editCategory = {
        def category = params.id ? ItemTypeDTO.get(params.id) : null

        breadcrumbService.addBreadcrumb(controllerName, actionName, params.id ? 'update' : 'create', params.int('id'))

        [ category : category ]
    }

    /**
     * Validate and save a category.
     */
    def saveCategory = {
        def category = new ItemTypeWS()

        // grails has issues binding the ID for ItemTypeWS object...
        // bind category ID manually
        bindData(category, params, 'id')
        category.id = !params.id?.equals('') ? params.int('id') : null

        // save or update
        try {
            if (!category.id || category.id == 0) {
                log.debug("creating product category ${category}")

                category.id = webServicesSession.createItemCategory(category)

                flash.message = 'product.category.created'
                flash.args = [ category.id ]

            } else {
                log.debug("saving changes to product category ${category.id}")

                webServicesSession.updateItemCategory(category)

                flash.message = 'product.category.updated'
                flash.args = [ category.id ]
            }

        } catch (SessionInternalError e) {
            viewUtils.resolveException(flash, session.locale, e);
            render view: 'editCategory', model: [ category : category ]
            return
        }

        chain action: 'list', params: [ id: category.id ]
    }

    /**
     * Get the item to be edited and show the "editProduct.gsp" view. If no ID is given
     * this screen will allow creation of a new item.
     */
    def editProduct = {
        def product = params.id ? webServicesSession.getItem(params.int('id'), session['user_id'], null) : null

        breadcrumbService.addBreadcrumb(controllerName, actionName, params.id ? 'update' : 'create', params.int('id'))

        [ product: product, currencies: currencies, categoryId: params.category ]
    }

    /**
     * Validate and save a product.
     */
    def saveProduct = {
        def product = new ItemDTOEx()
        bindData(product, params, "product")

        // bind parameters with odd types (integer booleans, string integers  etc.)
        product.priceManual = params.priceManual ? 1 : 0
        product.hasDecimals = params.hasDecimals ? 1 : 0
        product.percentage = !params.percentage?.equals('') ? params.percentage : null

        // bind prices
        def prices = params.prices.collect { currencyId, price ->
            new ItemPriceDTOEx(null, !price?.equals('') ? price.toBigDecimal() : null, currencyId.toInteger())
        }
        product.prices = prices

        // todo: replace with custom validator annotations
        ///      validate that at least one price has a set dollar value
        //       validate that the type array is not empty (arrays are not supported by base hibernate validations!)

        // validate that at least one price is set
        if (!product.prices.any { it.price }) {
            flash.error = 'product.without.price'
            render view: 'editProduct', model: [ product: product, currencies: currencies ]
            return
        }

        // validate that at least one type is set
        if (!product.types) {
            flash.error = 'product.without.type'
            render view: 'editProduct', model: [ product: product, currencies: currencies ]
            return
        }

        // save or update
        try{
            if (!product.id || product.id == 0) {
                log.debug("creating product ${product}")

                product.id = webServicesSession.createItem(product)

                flash.message = 'product.created'
                flash.args = [ product.id ]

            } else {
                log.debug("saving changes to product ${product.id}")

                webServicesSession.updateItem(product)

                flash.message = 'product.updated'
                flash.args = [ product.id ]
            }

        } catch (SessionInternalError e) {
            viewUtils.resolveException(flash, session.locale, e);
            render view: 'editProduct', model: [ product: product, currencies: currencies ]
            return
        }

        chain action: 'show', params: [ id: product.id ]
    }

    def getCurrencies() {
        def currencies = new CurrencyBL().getCurrencies(session['language_id'].toInteger(), session['company_id'].toInteger())
        return currencies.findAll { it.inUse }
    }

}
