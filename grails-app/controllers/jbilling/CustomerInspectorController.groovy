package jbilling

import com.sapienter.jbilling.client.pricing.util.PlanHelper
import com.sapienter.jbilling.common.SessionInternalError
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO
import com.sapienter.jbilling.server.item.CurrencyBL
import com.sapienter.jbilling.server.item.PlanItemWS
import com.sapienter.jbilling.server.item.db.ItemDTO
import com.sapienter.jbilling.server.order.db.OrderDAS
import com.sapienter.jbilling.server.payment.db.PaymentDTO
import com.sapienter.jbilling.server.pricing.PriceModelWS
import com.sapienter.jbilling.server.user.ContactWS
import com.sapienter.jbilling.server.user.CustomerPriceBL
import com.sapienter.jbilling.server.user.contact.db.ContactTypeDAS
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.user.db.UserDTO
import com.sapienter.jbilling.server.payment.blacklist.BlacklistBL

class CustomerInspectorController {
	
	def webServicesSession
    def viewUtils

    def breadcrumbService
    def productService

	def index = { 
		redirect action: 'inspect', params: params
	}

	def inspect = {
		def user = params.id ? UserDTO.get(params.int('id')) : null

        if (!user) {
            flash.error = 'no.user.found'
            flash.args = [ params.id ]
            return // todo: show generic error page
        }

        def revenue =  webServicesSession.getTotalRevenueByUser(user.id)
        def subscriptions = webServicesSession.getUserSubscriptions(user.id)

        // last invoice
        def invoiceIds = webServicesSession.getLastInvoices(user.id, 1)
        def invoice = invoiceIds ? InvoiceDTO.get(invoiceIds.first()) : null

        // last payment
        def paymentIds = webServicesSession.getLastPayments(user.id, 1)
        def payment = paymentIds ? PaymentDTO.get(paymentIds.first()) : null

        // extra contacts and contact type descriptions
        def contacts = user ? webServicesSession.getUserContactsWS(user.id) : null
        for (ContactWS contact : contacts) {
            def contactType = new ContactTypeDAS().find(contact.type)
            contact.setContactTypeDescr(contactType?.getDescription(session['language_id'].toInteger()))
        }

        // blacklist matches
        def blacklistMatches = BlacklistBL.getBlacklistMatches(user.id)

        // used to find the next invoice date
        def cycle = new OrderDAS().findEarliestActiveOrder(user.id)

        // all customer prices and products
        def company = CompanyDTO.get(session['company_id'])
        def itemTypes = company.itemTypes.sort{ it.id }
        params.typeId = !itemTypes.isEmpty() ? itemTypes?.asList()?.first()?.id : null

        def products = productService.getFilteredProducts(company, params)
        def prices = new CustomerPriceBL(user.id).getCustomerPrices()

        breadcrumbService.addBreadcrumb(controllerName, actionName, null, params.int('id'))

        [
                user: user,
                contacts: contacts,
                blacklistMatches: blacklistMatches,
                invoice: invoice,
                payment: payment,
                subscriptions: subscriptions,
                prices: prices,
                company: company,
                itemTypes: itemTypes,
                products: products,
                currencies: currencies,
                cycle: cycle,
                revenue: revenue
        ]
    }


    // Customer specific pricing

    def filterProducts = {
        def company = CompanyDTO.get(session['company_id'])
        def itemTypes = company.itemTypes.sort{ it.id }

        // filter using the first item type by default
        if (params.typeId == null)
            params.typeId = itemTypes?.asList()?.first()?.id

        def products =  productService.getFilteredProducts(company, params)

        render template: 'products', model: [ itemTypes: itemTypes, products: products ]
    }

    def productPrices = {
        def itemId = params.int('id')
        def prices = new CustomerPriceBL(params.int('userId')).getCustomerPrices(itemId)
        def product = ItemDTO.get(itemId)

        log.debug("prices for customer ${params.userId} and item ${itemId}, ${prices.size()}")

        render template: 'prices', model: [ prices: prices, product: product, userId: params.userId ]
    }

    def allProductPrices = {
        def prices = new CustomerPriceBL(params.int('userId')).getCustomerPrices()
        render template: 'prices', model: [ prices: prices, userId: params.userId ]
    }

    /**
     * Get the price to be edited and show the 'editCustomerPrice.gsp' view. If no ID is given
     * this screen will allow creation of a new customer-specific price.
     */
    def editCustomerPrice = {
        def userId = params.int('userId')
        def priceId = params.int('id')


        def product = webServicesSession.getItem(params.int('itemId'), userId, null)
        def user = webServicesSession.getUserWS(userId)

        def price
        if (priceId) {
            price = webServicesSession.getCustomerPrices(userId).find{ it.id == priceId }
        } else {
            price = new PlanItemWS()
            price.model = product.defaultPrice
        }

        [ price: price, product: product, user: user, currencies: currencies ]
    }

    def updateStrategy = {
        def priceModel = PlanHelper.bindPriceModel(params)
        render template: '/priceModel/model', model: [ model: priceModel, currencies: currencies ]
    }

    def addChainModel = {
        PriceModelWS priceModel = PlanHelper.bindPriceModel(params)

        // add new price model to end of chain
        def model = priceModel
        while (model.next) {
            model = model.next
        }
        model.next = new PriceModelWS();

        render template: '/priceModel/model', model: [ model: priceModel, currencies: currencies ]
    }

    def removeChainModel = {
        PriceModelWS priceModel = PlanHelper.bindPriceModel(params)
        def modelIndex = params.int('modelIndex')

        // remove price model from the chain
        def model = priceModel
        for (int i = 1; model != null; i++) {
            if (i == modelIndex) {
                model.next = model.next?.next
                break
            }
            model = model.next
        }

        render template: '/priceModel/model', model: [ model: priceModel, currencies: currencies ]
    }

    def addAttribute = {
        PriceModelWS priceModel = PlanHelper.bindPriceModel(params)

        def modelIndex = params.int('modelIndex')
        def attribute = message(code: 'plan.new.attribute.key', args: [ params.attributeIndex ])

        // find the model in the chain, and add a new attribute
        def model = priceModel
        for (int i = 0; model != null; i++) {
            if (i == modelIndex) {
                model.attributes.put(attribute, '')
            }
            model = model.next
        }

        render template: '/priceModel/model', model: [ model: priceModel, currencies: currencies ]
    }

    def removeAttribute = {
        PriceModelWS priceModel = PlanHelper.bindPriceModel(params)

        def modelIndex = params.int('modelIndex')
        def attributeIndex = params.int('attributeIndex')

        // find the model in the chain, remove the attribute
        def model = priceModel
        for (int i = 0; model != null; i++) {
            if (i == modelIndex) {
                def name = params["model.${modelIndex}.attribute.${attributeIndex}.name"]
                model.attributes.remove(name)
            }
            model = model.next
        }

        render template: '/priceModel/model', model: [ model: priceModel, currencies: currencies ]
    }

    /**
     * Validate and save a customer-specific price.
     */
    def saveCustomerPrice = {
        def user = webServicesSession.getUserWS(params.int('userId'))
        def price = new PlanItemWS()
        bindData(price, params, 'price')
        price.model = PlanHelper.bindPriceModel(params)

        try {
            if (!price.id || price.id == 0) {
                log.debug("creating customer ${user.userId} specific price ${price}")

                price = webServicesSession.createCustomerPrice(user.userId, price);

                flash.message = 'created.customer.price'
                flash.args = [ price.itemId ]
            } else {
                log.debug("updating customer ${user.userId} specific price ${price.id}")

                webServicesSession.updateCustomerPrice(user.userId, price);

                flash.message = 'updated.customer.price'
                flash.args = [ price.itemId ]
            }
        } catch (SessionInternalError e) {
            viewUtils.resolveException(flash, session.locale, e);
            render view: 'editCustomerPrice', model: [ price: price, user: user, products: products, currencies: currencies ]
            return
        }

        chain controller: 'customerInspector', action: 'inspect', params: [ id: user.userId ]
    }

    /**
     * Deletes a customer-specific price.
     */
    def deleteCustomerPrice = {
        def userId = params.int('userId')
        def planItemId = params.int('id')

        try {
            log.debug("deleting customer ${userId} price ${planItemId}")

            webServicesSession.deleteCustomerPrice(userId, planItemId)

            flash.message = 'deleted.customer.price'
            flash.args = [ params.itemId ]

        } catch (SessionInternalError e) {
            viewUtils.resolveException(flash, session.locale, e)
        }

        // render remaining prices for the priced product
        productPrices(params: [id: params.itemId, userId: userId])
    }

    def getCurrencies() {
        def currencies = new CurrencyBL().getCurrencies(session['language_id'].toInteger(), session['company_id'].toInteger())
        return currencies.findAll { it.inUse }
    }

}
