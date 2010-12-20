package jbilling

import com.sapienter.jbilling.server.util.db.LanguageDTO;
import com.sapienter.jbilling.server.user.UserBL;


import com.sapienter.jbilling.server.item.db.ItemTypeDTO;
import com.sapienter.jbilling.server.item.ItemTypeWS;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.ItemDAS;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.ItemPriceDTOEx;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import grails.plugins.springsecurity.Secured;

@Secured(['isAuthenticated()'])
class ProductController {

    static pagination = [ max: 25, offset: 0 ]

    def webServicesSession
    Integer languageId= session["language_id"]
    int typeId

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
        def categoryId = products?.get(0)?.itemTypes?.asList()?.get(0)?.id

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

            if (!products) {
                flash.info = 'product.category.no.products.warning'
                flash.args = [params.id]
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

        if (params.template) {
            // render requested template, usually "_show.gsp"
            render template: params.template, model: [ selectedProduct: product, selectedCategoryId: params.category ]

        } else {
            // render default "list" view - needed so a breadcrumb can link to a product by id
            def filters = filterService.getFilters(FilterType.PRODUCT, params)
            def categories = getCategories();
            def products = getItemsByTypeId(categoryId, filters);
            render view: 'list', model: [ categories: categories, products: products, selectedProduct: product, selectedCategoryId: params.category, filters: filters ]
        }
    }

    def deleteCategory = {
        if (params.id) {
            webServicesSession.deleteItemCategory(params.int('id'))

            log.debug("Deleted item category ${params.id}.");

            flash.message= 'item.category.deleted'
            flash.args = [ params.id ]
        }

        def categories = getCategories()
        render template: 'categories', model: [ categories: categories ]
    }

    def deleteProduct = {
        if (params.id) {
            webServicesSession.deleteItem(params.int('id'))

            log.debug("Deleted item ${params.id}.");

            flash.message = 'item.delete.success'
            flash.args = [ params.id ]
        }

        if (params.category) {
            // return the products list, pass the category so the correct set of products is returned.
            chain(action: 'products', params: [ id: params.category ])
        } else {
            // no category means we deleted from the 'allProducts' view
            chain(action: 'allProducts', params: params )
        }

    }


    def addEditCategory = {
        //redirect to add item category form
        log.info "addEditCategory - if Edit id=${params.id}"
        ItemTypeDTO dto= null
        if (params.id) {
            dto= ItemTypeDTO.findById(params.id)
        }
        render view: 'addEdit/category', model:[dto:dto, languageId:languageId]
    }

    def saveCategory = {
        log.info 'Save called..'
        int loggedUserId= webServicesSession.getCallerId()
        log.info "Logged User Id="+ loggedUserId

        def dto = new ItemTypeWS()
        bindData(dto, params)

        log.info "dto.id=" + dto.getId() + " dto.description=" + dto.getDescription() + " dto.orderLineTypeId=" + dto.getOrderLineTypeId()
        try {
            if (dto.getId() == 0 || !(dto.getId()) ){
                webServicesSession.createItemCategory(dto)
            } else {
                webServicesSession.updateItemCategory(dto)
            }
            flash.message = 'item.category.saved'
        } catch (SessionInternalError e) {
            log.error "Error Updating/Creating Item Category " + dto.getId()
            flash.errorMessages?.addAll(e.getErrorMessages())
            //boolean retValue = viewUtils.resolveExceptionForValidation(flash, session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE', e);
        }
        if (!(flash.errorMessages?.size() > 0) )
        {
            flash.message = 'item.category.saved'
        }
        //TODO move this to product controller
        redirect (action: 'index')
    }

    def addEditProduct ={
        log.info "Edit: params.Id=" + params.id
        ItemDTO dto=null
        Integer itemTypeId= params['itemTypeId']?.toInteger()
        if (params.id) {
            Integer itemId= params?.id?.toInteger()
            log.info "Editing item=" + itemId
            dto= ItemDTO.findById(itemId)
            if ((!itemTypeId) && dto.getItemTypes()?.iterator()?.hasNext())
            {
                itemTypeId= ((ItemTypeDTO)dto?.getItemTypes().iterator()?.next()).getId();
            }
        }
        boolean exists= (dto!=null)
        UserBL userbl = new UserBL(webServicesSession.getCallerId());
        Integer entityId= userbl.getEntityId(userbl.getEntity().getUserId())
        CurrencyDTO[] currs= new CurrencyBL().getCurrencies(languageId, entityId)
        log.info "LanguageId=${languageId} ,EntityId=${entityId} ,fOUND Currencies=${currs.length} ,itemTypeId=${itemTypeId}"
        render view:"addEdit/product", model: [item:dto, exists:exists,languageId:languageId, currencies:currs,itemTypeId:itemTypeId]
    }

    def edit = {
        log.info "Edit: params.selectedId=" + params.selectedId
        log.info "Edit: params.Id=" + params.id
        Integer itemId= params?.id?.toInteger()
        log.info "Editing item=" + itemId
        ItemDTO dto= ItemDTO.findById(itemId)
        boolean exists= (dto!=null)
        UserBL userbl = new UserBL(webServicesSession.getCallerId());
        Integer entityId= userbl.getEntityId(userbl.getEntity().getUserId())
        CurrencyDTO[] currs= new CurrencyBL().getCurrencies(languageId, entityId)
        log.info "LanguageId=" + languageId + " EntityId=" + entityId + " found Currencies=" + currs.length
        render(template:"addEdit", model: [item:dto, exists:exists,languageId:languageId, currencies:currs])
    }

    def changeLanguage = {
        log.info "Id=" + params.id + " languageId=" + params.languageId
        Integer _languageId = Integer.parseInt(params.languageId)
        ItemDTO dto= null;
        if (params.id)
        {
            dto= ItemDTO.findById(params.id?.toInteger())
        }
        boolean exists= (dto!=null)
        UserBL userbl = new UserBL(webServicesSession.getCallerId());
        Integer entityId= userbl.getEntityId(userbl.getEntity().getUserId())
        CurrencyDTO[] currs= new CurrencyBL().getCurrencies(_languageId, entityId)
        log.info "LanguageId=" + _languageId + " EntityId=" + entityId + " found Currencies=" + currs.length
        render(view:"addEdit/product", model: [item:dto, exists:exists,languageId:_languageId, currencies:currs])
    }

    def add = {
        log.info "Add: " + params["id"]
        UserBL userbl = new UserBL(webServicesSession.getCallerId());
        Integer entityId= userbl.getEntityId(userbl.getEntity().getUserId())
        CurrencyDTO[] currs= new CurrencyBL().getCurrencies(languageId, entityId)
        log.info "LanguageId=" + languageId + " EntityId=" + entityId + " found Currencies=" + currs.length
        render(template:"addEdit", model:[languageId:languageId, currencies:currs])
    }

    def updateOrCreate ={
        ItemDTOEx dto= new ItemDTOEx();
        log.info "Item Id=" + params.id
        int pricesCnt= params.pricesCnt?.toInteger()
        log.info "pricesCnt= ${pricesCnt}"
        List<ItemPriceDTOEx> prices= new ArrayList<ItemPriceDTOEx>();
        for (int iter=0 ; iter < pricesCnt ; iter++ )
        {
            prices.add(new ItemPriceDTOEx())
        }
        dto.setPrices(prices);
        bindData(dto, params)

        for (ItemPriceDTOEx price : dto.prices) {
            if (!price.getPrice())
            {
                price.setPrice "0"
                log.info "dto.prices.price=${price.getPrice()}"
                log.info "dto.prices.currencyId=${price.getCurrencyId()}"
            }
        }

        dto.setHasDecimals((params.hasDecimals? 1: 0))
        dto.setPriceManual((params.priceManual? 1 : 0))
        log.info "dto.hasDecimals=" + dto.getHasDecimals()
        log.info "dto.priceManual=" + dto.getPriceManual()
        Integer _languageId= params.languageId?.toInteger()

        log.info "dto.id=" + dto.getId()
        log.info "dto.number=" + dto.getNumber()
        log.info "dto.description=" + dto.getDescription()
        log.info "dto.types=" + dto.getTypes()
        log.info "dto.percentage=" + dto.getPercentage()
        log.info "dto.prices=" + dto?.prices?.size()

        try{
            if (null != dto.getId() && 0 != dto.getId()) {
                webServicesSession.updateItem(dto)
                flash.message = "item.update.success"
            } else {
                webServicesSession.createItem(dto)
                flash.message = "item.create.success"
            }
        } catch (SessionInternalError e) {
            log.error "Error Updating/Creating ${dto.getId()}\n" + e.printStackTrace()
            flash.errorMessages?.addAll(e.getErrorMessages())
            //boolean retValue = viewUtils.resolveExceptionForValidation(flash, session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE', e);
        }
        flash.args= dto.getId()
        redirect (action: "index")
    }



    def cancelEditProduct = {

        log.info "Method: cancelEditProduct id=${params['id']} and itemTypeId=${params['itemTypeId']}"

        def itemTypeId= Integer.parseInt(params["itemTypeId"])
        ItemTypeDTO dto= ItemTypeDTO.findById(itemTypeId);
        def items= dto.getItems()
        log.info "Lets see the item size here.. " + items?.size()

        def prodId= params["id"]?.toInteger()
        if (null != prodId) {
            ItemDTO item = new ItemBL(prodId).getEntity();
            LanguageDTO lang= new LanguageDTO(languageId);
            String language= lang.getDescription();
            render view: 'cancelEditProduct', model: [list: items,languageId:languageId, itemTypeId:itemTypeId, item:item, language:language]
            return
        }

        List<ItemTypeDTO> categories= ItemTypeDTO.findAllByEntity(new CompanyDTO(webServicesSession.getCallerCompanyId()))
        render view: 'cancelAddProduct', model: [categories: categories, list: items,languageId:languageId, itemTypeId:itemTypeId]
    }

}
